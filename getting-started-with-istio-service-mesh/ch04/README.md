# 4장. Istio VirtualService

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
