# 5장. Istio Gateway

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
