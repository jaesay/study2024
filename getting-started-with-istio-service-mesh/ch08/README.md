# 8장. 로그 및 추적

## Distributed Tracing
- https://istio.io/latest/about/faq/distributed-tracing/

Istio는 애플리케이션이 B3 추적 헤더와 Envoy가 생성한 요청 ID를 전파하는 데 의존합니다. 이러한 헤더에는 다음이 포함됩니다:

- `x-request-id`: 각 요청을 고유하게 식별하는 데 사용되는 ID입니다. 이 헤더는 로깅, 추적 및 요청 추적을 위해 사용될 수 있습니다. 
- `x-b3-traceid`: 전체 추적을 고유하게 식별하는 데 사용되는 ID입니다. 이는 여러 서비스에 걸쳐 전파되며, 모든 관련된 스팬들이 같은 추적 ID를 공유합니다.
- `x-b3-spanid`: 특정 스팬을 식별하는 데 사용되는 ID입니다. 스팬은 요청의 한 부분을 나타내며, 각 스팬은 고유한 ID를 가집니다.
- `x-b3-parentspanid`: 현재 스팬의 부모 스팬을 식별하는 ID입니다. 이는 스팬 간의 부모-자식 관계를 나타내며, 추적 구조를 이해하는 데 도움을 줍니다.
- `x-b3-sampled`: 이 헤더는 추적 데이터가 샘플링되어야 하는지 여부를 나타냅니다. 값이 "1"이면 해당 요청은 추적 데이터를 수집해야 함을 의미하고, "0"은 그렇지 않음을 의미합니다. 
- `x-b3-flags`: 추가적인 플래그를 위해 사용되며, 예를 들어 디버깅이 필요한 경우에 사용될 수 있습니다. 
- `b3`: 이 헤더는 x-b3- 계열 헤더들의 정보를 하나의 헤더로 압축한 것입니다. 이는 추적 ID, 스팬 ID, 부모 스팬 ID, 샘플링 및 플래그 정보를 단일 헤더에서 전달할 수 있게 해줍니다: `{x-b3-traceid}-{x-b3-spanid}-{if x-b3-flags 'd' else x-b3-sampled}-{x-b3-parentspanid}`

- https://istio.io/latest/docs/tasks/observability/distributed-tracing/jaeger/
```bash
istioctl dashboard jaeger

curl "http://$GATEWAY_URL/productpage"
fortio load -c 1 -n 30 -qps 1 -nocatchup -uniform -loglevel Warning "http://$GATEWAY_URL/productpage"
```

## Logging

- https://istio.io/latest/docs/tasks/observability/logs/otel-provider/

```bash
# Setting
kubectl apply -f samples/sleep/sleep.yaml

export SOURCE_POD=$(kubectl get pod -l app=sleep -o jsonpath={.items..metadata.name})
kubectl apply -f samples/httpbin/httpbin.yaml
kubectl apply -f samples/open-telemetry/otel.yaml -n istio-system

kubectl edit configmap istio -o yaml -n istio-system
#extensionProviders:
#- name: otel
#  envoyOtelAls:
#    service: opentelemetry-collector.istio-system.svc.cluster.local
#    port: 4317
kubectl apply -f sleep-logging-telemetry.yaml

# Test
kubectl exec "$SOURCE_POD" -c sleep -- curl -sS -v httpbin:8000/status/418
kubectl logs -l app=opentelemetry-collector -n istio-system

# Clean up
kubectl delete telemetry sleep-logging
kubectl delete -f samples/sleep/sleep.yaml
kubectl delete -f samples/httpbin/httpbin.yaml
kubectl delete -f samples/open-telemetry/otel.yaml -n istio-system
```

