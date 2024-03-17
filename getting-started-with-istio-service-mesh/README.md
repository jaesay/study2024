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

# scale in/out
kubectl scale deployment --replicas=3 deployment <deployment-name> 

# 삭제
kubectl delete -f <yaml-file>
kubectl delete all --all
```

*Envoy*
```bash
# 예제 서비스(greeting-service) 실행
./gradlew bootBuildImage
docker run --rm -p 8080:8080 greeting-service:0.0.1-SNAPSHOT

# 버전 지정 안하면 이미지를 못 가져와서 버전 지정, 최신 버전을 사용하면 service-envoy.yaml 설정 오류 발생
docker run -v $(pwd)/envoy-conf:/envoy-conf -p 80:80 -p 8081:8081 -it envoyproxy/envoy-alpine:v1.10.0 envoy -c ./envoy-conf/service-envoy.yaml
```

*Istio*

ch03 예제 코드는 1.2.2 버전에 맞춰져 있지만 1.2.2 istioctl 동작하지 않아서 공식 문서 사이트 예제로 확인
- https://istio.io/latest/docs/setup/getting-started/
- issue: https://github.com/istio/istio/issues/44090
