## Getting Started

### 기본 명령어
*Kubernetes(ch01)*
```bash
# 미니큐브 실행


# 이미지를 로컬 환경에 저장하고 레지스트리로 전송한 다음 Minikube 에서 가져오는 대신 컨테이너 이미지를 Minikube 인스턴스에 직접 저장할 수 있다.
eval $(minikube docker-env)

# webapp
docker build -t web-app:1.0 .
kubectl apply -f webapp-deployment.yaml
kubectl apply -f webapp-service.yaml

# frontend
mvn clean package
docker build -t frontend-app:1.0 .
kubectl apply -f frontend-deployment.yaml
kubectl apply -f frontend-service.yaml

# port forwarding
kubectl port-forward svc/frontendservice 8080:80

# 동작 확인
kubectl get all
kubectl get pod,svc,deploy -w
kubectl get pod,svc,deploy -o wide
kubectl decsribe pod <pod-name>
kubectl logs <pod-name>

# scale in/out
kubectl scale deployment --replicas=3 deployment <deployment-name> 

# 삭제
kubectl delete -f <yaml-file>
kubectl delete all --all
```

*Envoy(ch02)*
```bash
# 예제 서비스(greeting-service) 실행
./gradlew bootBuildImage
docker run --rm -p 8080:8080 greeting-service:0.0.1-SNAPSHOT

# 버전 지정 안하면 이미지를 못 가져와서 버전 지정, 최신 버전을 사용하면 service-envoy.yaml 설정 오류 발생
docker run -v $(pwd)/envoy-conf:/envoy-conf -p 80:80 -p 8081:8081 -it envoyproxy/envoy-alpine:v1.10.0 envoy -c ./envoy-conf/service-envoy.yaml
```

*Istio(ch03)*

ch03 예제 코드는 1.2.2 버전에 맞춰져 있지만 1.2.2 istioctl 동작하지 않아서 공식 문서 사이트 예제로 확인
- https://istio.io/latest/docs/setup/getting-started/
- issue: https://github.com/istio/istio/issues/44090

*VirtualService (ch04)*

```bash
docker build . -t web-app:5.0 --build-arg ver=5.0
docker build . -t web-app:5.1 --build-arg ver=5.1
docker build . -t web-app:5.2 --build-arg ver=5.2

kubectl apply -f webapp-deployment-v5.yaml
kubectl apply -f webapp-service.yaml

kubectl apply -f frontend-deployment.yaml
kubectl apply -f frontend-service.yaml

kubectl port-forward svc/frontendservice 8080:80

# Istio 설치
istioctl install --set profile=demo -y

kubectl apply -f destination-rule.yaml

kubectl get destinationrules -o yaml
kubectl get dr -o yaml

kubectl apply -f webservice-simple-vs.yaml
kubectl get virtualservices
kubectl get vs

kubectl delete -f webapp-deployment-v5.yaml
kubectl delete -f frontend-deployment.yaml

# default 네임스페이스에 사이드카 인젝터가 envoy 사이드카를 자동으로 추가하기 위해 레이블 추가 
kubectl label namespace default istio-injection=enabled

kubectl apply -f webapp-deployment-v5.yaml
kubectl apply -f frontend-deployment.yaml

kubectl get pod webapp-deployment-5.0-85d5f6fc85-hbmkj -o yaml

kubectl port-forward svc/frontendservice 8080:80

# HTTP Rewrite
# 웹 서비스 호스트를 처리하는 VirtualService는 하나만 있어야 한다. 두개가 있으면 먼저 만든것 하나만 적용되는 것 같다.
kubectl create -f webservice-rewrite-vs.yaml

kubectl exec pod/frontend-deployment-8496d98df7-c299q -- wget -O - http://webservice/hello

# HTTP 속성 조회
kubectl apply -f webservice-httplookup-vs.yaml

kubectl exec pod/frontend-deployment-8496d98df7-c299q -- wget -O - http://webservice/
kubectl exec pod/frontend-deployment-8496d98df7-c299q -- wget -O - --header='x-upgrade: TRUE' http://webservice/

# 가중치 분배
kubectl apply -f webservice-wtdist-vs.yaml

kubectl exec pod/frontend-deployment-8496d98df7-c299q -it -- sh -il
frontend-deployment-8496d98df7-c299q:/# wget -qO - http://webservice
frontend-deployment-8496d98df7-c299q:/# wget -qO - http://webservice
frontend-deployment-8496d98df7-c299q:/# wget -qO - http://webservice
frontend-deployment-8496d98df7-c299q:/# exit
```
*Gateway (ch05)*

```bash
# Ingress Gateway 기본 구성
kubectl apply -f gateway.yaml
kubectl apply -f webservice-wtdist-vs.yaml

minikube tunnel
## 게이트웨이를 통한 외부 요청
curl -v -H "Host: webservice.greetings.com" http://10.103.160.254

## 서비스 프록시 (mesh)
kubectl exec pod/frontend-deployment-8496d98df7-c299q -it -- sh -il
frontend-deployment-8496d98df7-c299q:/# wget -qO - http://webservice
frontend-deployment-8496d98df7-c299q:/# wget -qO - http://webservice
frontend-deployment-8496d98df7-c299q:/# wget -qO - http://webservice
frontend-deployment-8496d98df7-c299q:/# wget -qO - http://webservice
frontend-deployment-8496d98df7-c299q:/# exit

# TLS
## 자체 서명 인증서 생성
openssl req -newkey rsa:2048 -nodes -keyout key.pem -x509 -days 365 -out cert.pem

## 키 암호 제거
openssl rsa -in key.pem -out key2.pem

## 시크릿 생성
kubectl create -n istio-system secret tls istio-ingressgateway-certs --key key2.pem --cert cert.pem
kubectl describe secret istio-ingressgateway-certs -n istio-system

for i in 1 2 3 4; do curl -H "Host: webservice.greetings.com" --resolve "webservice.greetings.com:443:10.103.160.254" -k https://webservice.greetings.com; echo ''; done

# Service Entry
kubectl exec pod/frontend-deployment-8496d98df7-c299q -it -- sh -il
## 기본적으로 모든 요청이 외부 접근이 허용
frontend-deployment-8496d98df7-c299q:/# wget -qSO - http://en.wikipedia.org > /dev/null

## 외부로 접근 제한 (다시 허용하기 위해서는 ALLOW_ANY 다시 명령 수행)
istioctl install --set profile=demo \
                 --set meshConfig.outboundTrafficPolicy.mode=REGISTRY_ONLY

kubectl exec pod/frontend-deployment-8496d98df7-c299q -it -- sh -il
## 외부 접근 제한 확인
frontend-deployment-8496d98df7-c299q:/# wget -qSO - http://en.wikipedia.org > /dev/null

## vs도 설정해야 됨
kubectl apply -f service-entry.yaml

kubectl exec pod/frontend-deployment-8496d98df7-c299q -it -- sh -il
frontend-deployment-8496d98df7-c299q:/# wget -qSO - http://en.wikipedia.org > /dev/null
frontend-deployment-8496d98df7-c299q:/# wget -qSO - https://en.wikipedia.org > /dev/null

# Egress Gateway
kubectl apply -f egress-gateway.yaml

kubectl exec pod/frontend-deployment-8496d98df7-c299q -it -- sh -il
frontend-deployment-8496d98df7-c299q:/# wget -qSO - http://en.wikipedia.org > /dev/null

## istio-system 네임스페이스 내에서 istio=egressgateway 레이블을 가진 모든 포드 중 istio-proxy 컨테이너의 로그를 조회
kubectl logs -l istio=egressgateway -c istio-proxy -n istio-system
```

*Service Resilience (ch06)*

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

minikube tunnel
curl -v http://10.99.93.216/

## 사이드카 로그
kubectl logs <pod-name> -c istio-proxy -n <namespace>
kubectl logs frontend-deployment-8496d98df7-v8w4d -c istio-proxy
```
