```bash
# 시작
$ minikube start

# 컨테이너 이미지를 minikube 에서 직접 관리하도록 설정: 이미지를 로컬 환경에 저장하고 레지스트리로 전송한 다음 Minikube 에서 가져오는 대신 컨테이너 이미지를 Minikube 인스턴스에 직접 저장할 수 있다. 도커 허브등에 푸시할 필요가 없어진다.
$ eval $(minikube docker-env)

$ ./gradlew bootBuildImage
$ docker images

$ cd _k8s
$ kubectl apply -f k8s.yaml
$ kubectl get pod -w

# LoadBalancer 타입 service 사용할 때 사용
$ minikube tunnel
$ kubectl get svc

# Clean up
## Minikube VM 제거, 초기화 후 재시작하려 하고 싶을 때 사용
minikube delete
## Minikube VM 정지, 리소스 유지할 때 사용
minikube stop
```
