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
```