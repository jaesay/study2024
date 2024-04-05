# 2장. 서비스 메시 소개
```bash
# 예제 서비스(greeting-service) 실행
./gradlew bootBuildImage
docker run --rm -p 8080:8080 greeting-service:0.0.1-SNAPSHOT

# 버전 지정 안하면 이미지를 못 가져와서 버전 지정, 최신 버전을 사용하면 service-envoy.yaml 설정 오류 발생
docker run -v $(pwd)/envoy-conf:/envoy-conf -p 80:80 -p 8081:8081 -it envoyproxy/envoy-alpine:v1.10.0 envoy -c ./envoy-conf/service-envoy.yaml
```
