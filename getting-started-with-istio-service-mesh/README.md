## Getting Started

### 기본 명령어
*Kubernetes*
```bash
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

# 파드 수 조절 
kubectl scale deployment --replicas=3 deployment <deployment-name> 

# 삭제
kubectl delete -f <yaml-file>
kubectl delete all --all
```

*Envoy*
```bash
# --version: Envoy 버전 확인
docker run -it envoyproxy/envoy-alpine envoy --version

# 예제 서비스(greeting-service) 실행
./gradlew bootBuildImage
docker run --rm -p 8080:8080 greeting-service:0.0.1-SNAPSHOT

# 버전 안지정해주면 이미지를 못가져와서 버전 지정
docker run -v $(pwd)/envoy-conf:/envoy-conf -p 80:80 -p 8081:8081 -it envoyproxy/envoy-alpine:v1.10.0 envoy -c ./envoy-conf/service-envoy.yaml
```