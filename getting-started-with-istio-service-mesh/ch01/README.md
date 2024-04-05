# 1장. 쿠버네티스 훑어보기
```bash
# 미니큐브 실행
minikube start

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
