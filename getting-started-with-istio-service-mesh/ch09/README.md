# Security
## Authentication
### 기본 개념
https://istio.io/latest/docs/concepts/security/#authentication

### 예제
https://istio.io/latest/docs/tasks/security/authentication/authn-policy/

```bash
# Istio 설치
$ minikube start
$ istioctl install --set profile=demo -y
$ kubectl label namespace default istio-injection=enabled

# 예제 애플리케이션 배포
$ eval $(minikube docker-env)
$ kubectl create ns foo
$ kubectl apply -f <(istioctl kube-inject -f samples/httpbin/httpbin.yaml) -n foo
$ kubectl apply -f <(istioctl kube-inject -f samples/sleep/sleep.yaml) -n foo
$ kubectl create ns bar
$ kubectl apply -f <(istioctl kube-inject -f samples/httpbin/httpbin.yaml) -n bar
$ kubectl apply -f <(istioctl kube-inject -f samples/sleep/sleep.yaml) -n bar
$ kubectl create ns legacy
$ kubectl apply -f samples/httpbin/httpbin.yaml -n legacy
$ kubectl apply -f samples/sleep/sleep.yaml -n legacy

# 통신 확인
$ kubectl exec "$(kubectl get pod -l app=sleep -n bar -o jsonpath={.items..metadata.name})" -c sleep -n bar -- curl http://httpbin.foo:8000/ip -s -o /dev/null -w "%{http_code}\n"
$ for from in "foo" "bar" "legacy"; do for to in "foo" "bar" "legacy"; do kubectl exec "$(kubectl get pod -l app=sleep -n ${from} -o jsonpath={.items..metadata.name})" -c sleep -n ${from} -- curl -s "http://httpbin.${to}:8000/ip" -s -o /dev/null -w "sleep.${from} to httpbin.${to}: %{http_code}\n"; done; done

# 설정 확인
$ kubectl get peerauthentication --all-namespaces
$ kubectl get destinationrules.networking.istio.io --all-namespaces -o yaml | grep "host:"

# Auto mTLS 확인
$ kubectl exec "$(kubectl get pod -l app=sleep -n foo -o jsonpath={.items..metadata.name})" -c sleep -n foo -- curl -s http://httpbin.foo:8000/headers -s | grep X-Forwarded-Client-Cert | sed 's/Hash=[a-z0-9]*;/Hash=<redacted>;/'
$ kubectl exec "$(kubectl get pod -l app=sleep -n foo -o jsonpath={.items..metadata.name})" -c sleep -n foo -- curl http://httpbin.legacy:8000/headers -s | grep X-Forwarded-Client-Cert

# Global mTLS 설정
$ kubectl apply -f peerauthentication-global.yaml
$ kubectl get peerauthentication --all-namespaces
$ for from in "foo" "bar" "legacy"; do for to in "foo" "bar" "legacy"; do kubectl exec "$(kubectl get pod -l app=sleep -n ${from} -o jsonpath={.items..metadata.name})" -c sleep -n ${from} -- curl "http://httpbin.${to}:8000/ip" -s -o /dev/null -w "sleep.${from} to httpbin.${to}: %{http_code}\n"; done; done
$ kubectl delete peerauthentication -n istio-system default

# Namespace mTLS 설정
$ kubectl apply -f peerauthentication-ns.yaml
$ kubectl get peerauthentication --all-namespaces
$ for from in "foo" "bar" "legacy"; do for to in "foo" "bar" "legacy"; do kubectl exec "$(kubectl get pod -l app=sleep -n ${from} -o jsonpath={.items..metadata.name})" -c sleep -n ${from} -- curl "http://httpbin.${to}:8000/ip" -s -o /dev/null -w "sleep.${from} to httpbin.${to}: %{http_code}\n"; done; done

# Workload mTLS 설정
$ kubectl apply -f peerauthentication-workload.yaml
$ kubectl get peerauthentication --all-namespaces
$ for from in "foo" "bar" "legacy"; do for to in "foo" "bar" "legacy"; do kubectl exec "$(kubectl get pod -l app=sleep -n ${from} -o jsonpath={.items..metadata.name})" -c sleep -n ${from} -- curl "http://httpbin.${to}:8000/ip" -s -o /dev/null -w "sleep.${from} to httpbin.${to}: %{http_code}\n"; done; done
$ kubectl delete peerauthentication default overwrite-example -n foo
$ kubectl delete peerauthentication httpbin -n bar

# Ingress Gateway 배포
$ kubectl apply -f samples/httpbin/httpbin-gateway.yaml -n foo
$ minikube tunnel
$ kubectl get -n istio-system svc -o wide
$ export INGRESS_NAME=istio-ingressgateway
$ export INGRESS_NS=istio-system
$ export INGRESS_HOST=$(kubectl -n "$INGRESS_NS" get service "$INGRESS_NAME" -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
$ export INGRESS_PORT=$(kubectl -n "$INGRESS_NS" get service "$INGRESS_NAME" -o jsonpath='{.spec.ports[?(@.name=="http2")].port}')
$ export SECURE_INGRESS_PORT=$(kubectl -n "$INGRESS_NS" get service "$INGRESS_NAME" -o jsonpath='{.spec.ports[?(@.name=="https")].port}')
$ export TCP_INGRESS_PORT=$(kubectl -n "$INGRESS_NS" get service "$INGRESS_NAME" -o jsonpath='{.spec.ports[?(@.name=="tcp")].port}')
$ echo $INGRESS_HOST:$INGRESS_PORT
$ curl "$INGRESS_HOST:$INGRESS_PORT/headers" -s -o /dev/null -w "%{http_code}\n"

# Ingress GW로 진입 시(End User 접근 시) JWT Authentication 추가
$ kubectl apply -f jwt-example.yaml
$ curl "$INGRESS_HOST:$INGRESS_PORT/headers" -s -o /dev/null -w "%{http_code}\n"
$ curl --header "Authorization: Bearer deadbeef" "$INGRESS_HOST:$INGRESS_PORT/headers" -s -o /dev/null -w "%{http_code}\n"
$ TOKEN=$(curl https://raw.githubusercontent.com/istio/istio/release-1.21/security/tools/jwt/samples/demo.jwt -s)
$ curl --header "Authorization: Bearer $TOKEN" "$INGRESS_HOST:$INGRESS_PORT/headers" -s -o /dev/null -w "%{http_code}\n"

# Clean up
$ kubectl -n istio-system delete requestauthentication jwt-example
$ kubectl -n istio-system delete authorizationpolicy frontend-ingress
$ kubectl delete ns foo bar legacy
```

## Authorization
### 기본 개념
https://istio.io/latest/docs/concepts/security/#authorization

### 예제
https://istio.io/latest/docs/tasks/security/authorization/authz-jwt/
```bash
# 예제 애플리케이션 배포
$ kubectl create ns foo
$ kubectl apply -f <(istioctl kube-inject -f samples/httpbin/httpbin.yaml) -n foo
$ kubectl apply -f <(istioctl kube-inject -f samples/sleep/sleep.yaml) -n foo
$ kubectl exec "$(kubectl get pod -l app=sleep -n foo -o jsonpath={.items..metadata.name})" -c sleep -n foo -- curl http://httpbin.foo:8000/ip -sS -o /dev/null -w "%{http_code}\n"

# 기본적으로 토큰이 없거나 유효한 토큰이면 접근 가능
$ kubectl apply -f jwt-autorization-ra.yaml
$ kubectl exec "$(kubectl get pod -l app=sleep -n foo -o jsonpath={.items..metadata.name})" -c sleep -n foo -- curl "http://httpbin.foo:8000/headers" -sS -o /dev/null -H "Authorization: Bearer invalidToken" -w "%{http_code}\n"
$ kubectl exec "$(kubectl get pod -l app=sleep -n foo -o jsonpath={.items..metadata.name})" -c sleep -n foo -- curl "http://httpbin.foo:8000/headers" -sS -o /dev/null -w "%{http_code}\n"

# 권한이 있는 토큰만 접근 가능하도록 설정
$ kubectl apply -f jwt-autorization-ap.yaml
$ TOKEN_GROUP=$(curl https://raw.githubusercontent.com/istio/istio/release-1.21/security/tools/jwt/samples/groups-scope.jwt -s) && echo "$TOKEN_GROUP" | cut -d '.' -f2 - | base64 --decode -
$ kubectl exec "$(kubectl get pod -l app=sleep -n foo -o jsonpath={.items..metadata.name})" -c sleep -n foo -- curl "http://httpbin.foo:8000/headers" -sS -o /dev/null -H "Authorization: Bearer $TOKEN_GROUP" -w "%{http_code}\n"
$ kubectl exec "$(kubectl get pod -l app=sleep -n foo -o jsonpath={.items..metadata.name})" -c sleep -n foo -- curl "http://httpbin.foo:8000/headers" -sS -o /dev/null -H "Authorization: Bearer $TOKEN" -w "%{http_code}\n"

# Clean up
$ kubectl delete namespace foo
```