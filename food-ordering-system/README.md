## 모듈 구성
- common
  - common-application: GlobalExceptionHandler와 같이 애플리케이션 서비스(*-application 모듈)에서 공통적으로 필요한 모듈  
- infrastructure
  - kafka:
    -  kafka-config-data: Kafka Cluster에 연결하고 Producer, Consumer를 설정하기 위한 Configuration 데이터를 갖는다.

## Avro 스키마로 자바 코드 생성
```bash
# kafka-model 모듈에서 수행
$ mvn clean install
```
