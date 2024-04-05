# 6장. Service Resilience

```bash
# 기본 예제 추가
eval $(minikube docker-env)
docker build ../../ch04/webapp -t web-app:7.0 --build-arg ver=7.0

kubectl delete all --all
kubectl delete dr --all
kubectl delete vs --all

kubectl apply -f webapp-deployment-v7.yaml
kubectl apply -f webapp-service.yaml

kubectl apply -f frontend-deployment.yaml
kubectl apply -f frontend-service.yaml

kubectl apply -f destination-rule.yaml
kubectl apply -f gateway.yaml

kubectl apply -f webapp-vs.yaml
kubectl apply -f frontend-vs.yaml

minikube tunnel
curl -v http://10.99.93.216/

## 사이드카 로그
kubectl logs <pod-name> -c istio-proxy -n <namespace>
kubectl logs frontend-deployment-8496d98df7-v8w4d -c istio-proxy

# fortio 설치
brew install fortio
fortio server

# 리트라이
docker build . -t web-app:7.1 --build-arg ver=7.1
mvn clean package
docker build -t frontend-app:2.0 .

kubectl apply -f webapp-deployment-v7.yaml
kubectl apply -f frontend-deployment.yaml
kubectl apply -f destination-rule.yaml

kubectl exec pod/frontend-deployment-8d465cb6-lmd46 -it -- sh -il
frontend-deployment-8d465cb6-lmd46:/# wget -qO - http://webservice

minikube tunnel
kubectl get svc -n istio-system
curl -v http://10.102.146.12/

#-c 1: 동시에 수행될 요청의 수(concurrency level)를 1로 설정합니다. 이는 한 번에 하나의 요청만 서버로 보내겠다는 의미입니다.
#-n 30: 총 요청 횟수를 30으로 설정합니다. 즉, 테스트 동안 서버에 총 30개의 요청을 보냅니다.
#-qps 1: 초당 쿼리(Query Per Second) 수를 1로 설정합니다. 이는 매 초마다 하나의 요청을 서버로 보내겠다는 의미입니다.
#-nocatchup: 이 옵션은 Fortio가 느려진 요청을 보상하기 위해 빠르게 추가 요청을 보내는 "catch-up" 모드를 비활성화합니다. 각 요청은 정확히 지정된 간격으로 전송됩니다.
#-uniform: 요청 간 시간 간격을 일정하게 유지합니다. 기본적으로 Fortio는 지연 시간을 시뮬레이션하기 위해 요청 간에 약간의 변동을 추가할 수 있지만, 이 옵션을 사용하면 모든 요청이 정확히 같은 간격으로 전송됩니다.
#-loglevel Warning: 로그 수준을 "Warning"으로 설정합니다. 이는 Fortio가 경고(warnings) 또는 그보다 더 심각한 메시지만 출력하도록 설정하는 것입니다. 정보성 메시지(info)나 디버그 메시지(debug)는 표시되지 않습니다.
#http://10.102.146.12/: 테스트 대상 URL입니다.
#요약하자면, 이 명령은 http://10.102.146.12/ 주소로 총 30개의 HTTP 요청을 보내며, 한 번에 하나씩, 매초마다 요청을 보내는 테스트를 실행합니다. 출력 로그는 경고 또는 그보다 중요한 메시지만 포함할 것입니다.
fortio load -c 1 -n 30 -qps 1 -nocatchup -uniform -loglevel Warning http://10.102.146.12/

# 재시도
kubectl apply -f webapp-vs.yaml
## frontend VirtualService 수정
kubectl apply -f gateway.yaml

fortio load -c 1 -n 30 -qps 1 -nocatchup -uniform -loglevel Warning http://10.106.66.64/

curl -v http://10.106.66.64/

fortio load -c 10 -n 300 -qps 10 -nocatchup -uniform -loglevel Warning http://10.106.66.64/

# fault injection
kubectl apply -f webapp-vs.yaml
fortio load -c 10 -n 300 -qps 10 -nocatchup -uniform -loglevel Warning http://10.106.66.64/

# 타임아웃
kubectl apply -f webapp-vs.yaml
kubectl apply -f gateway.yaml

fortio load -c 1 -n 30 -qps 1 -nocatchup -uniform -loglevel Warning http://10.106.66.64/

# Circuit Breaking
docker build . -t web-app:7.1 --build-arg ver=7.1

## 리소스 및 istio 제거 후 재설치..

fortio load -c 4 -n 30 -qps 4 -nocatchup -uniform -loglevel Warning http://10.102.80.218/

kubectl apply -f samples/addons
kubectl rollout status deployment/kiali -n istio-system

istioctl dashboard kiali
```
