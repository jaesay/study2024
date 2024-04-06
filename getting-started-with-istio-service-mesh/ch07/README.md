# 7장. 애플리케이션 메트릭

Istio 1.5 이후 버전에서는 Mixer가 제거되면서 메트릭 구성 방식도 변화하였습니다. Mixer는 이전 버전의 Istio에서 정책 결정과 텔레메트리 데이터의 처리를 담당하는 구성 요소였습니다. 하지만 성능과 유연성을 개선하기 위해 Istio는 Mixer를 제거하고 Envoy 프록시와 통합하여 메트릭을 직접 수집하고 보고하는 방식으로 전환했습니다.

- https://istio.io/latest/docs/tasks/observability/metrics/querying-metrics/
```bash
# Istio Prometheus 설치
cd istio-1.21.0
export PATH=$PWD/bin:$PATH
istioctl install --set profile=demo -y
kubectl label namespace default istio-injection=enabled
kubectl apply -f samples/addons

# Istio Prometheus 접속
kubectl get svc -n istio-system
istioctl dashboard prometheus

# bookinfo 샘플 애플리케이션 배포
kubectl apply -f samples/bookinfo/platform/kube/bookinfo.yaml
kubectl get pod -w
kubectl exec "$(kubectl get pod -l app=ratings -o jsonpath='{.items[0].metadata.name}')" -c ratings -- curl -sS productpage:9080/productpage | grep -o "<title>.*</title>"

# Ingress Gateway 배포
kubectl apply -f samples/bookinfo/networking/bookinfo-gateway.yaml
istioctl analyze

minikube tunnel
export INGRESS_HOST=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
export INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].port}')
export SECURE_INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="https")].port}')
export GATEWAY_URL=$INGRESS_HOST:$INGRESS_PORT
echo "$GATEWAY_URL"

# 샘플 애플리케이션에 트래픽 보내기
curl "http://$GATEWAY_URL/productpage"
fortio load -c 1 -n 30 -qps 1 -nocatchup -uniform -loglevel Warning "http://$GATEWAY_URL/productpage"

# Prometheus 쿼리
## 전체 요청 수
istio_requests_total
## productpage service에 대한 모든 요청의 총 개수
istio_requests_total{destination_service="productpage.default.svc.cluster.local"}
## reviews service v3에 대한 모든 요청의 총 개수
istio_requests_total{destination_service="reviews.default.svc.cluster.local", destination_version="v3"}
## 지난 5분 동안 productpage service 모든 인스턴스에 대한 요청 비율
# rate(istio_requests_total{destination_service=~"productpage.*", response_code="200"}[5m])

# Clean up
killall istioctl
samples/bookinfo/platform/kube/cleanup.sh
kubectl delete -f samples/addons
istioctl uninstall -y --purge
kubectl delete namespace istio-system
kubectl label namespace default istio-injection-
```