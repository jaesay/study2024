# Getting Started

```bash
# Helm 설치
$ brew install helm
$ helm version

# Minikube 실행
$ minikube start
$ minikube status
$ eval $(minikube docker-env)

# 도커 이미지 빌드
$ ./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=spring-helm-example:1.0
$ docker images

# Helm Chart 생성
$ helm create spring-helm-example-chart
$ tree spring-helm-example-chart

# Helm Chart 실행
$ helm list
$ helm install spring-helm-example spring-helm-example-chart

# 서비스 확인
$ kubectl get all
$ kubectl get svc
$ minikube service spring-helm-example-spring-helm-example-chart --url
$ http get 127.0.0.1:61795/hello

# Helm Chart 삭제
$ helm uninstall spring-helm-example

# Minikube 종료
$ minikube delete
```