# 문제 해결

```bash
# 샘플 애플리케이션 배포
$ kubectl apply -f samples/bookinfo/platform/kube/bookinfo.yaml

# Istio 세부 구성 정보 확인(ConfigMap 확인)
$ kubectl -n istio-system get cm

## istio: istio의 기본 구성, 프로메테우스, 그라파나, 집킨 등의 구성을 정의한다. 
$ kubectl -n istio-system get cm istio -o yaml

## istio-sidecar-injector: 사이드카 주입기 설정을 정의한다.
$ kubectl -n istio-system get cm istio-sidecar-injector -o yaml | head -n 20

## 사이드카 주입하는 네임스페이스 확인
$ kubectl get ns -L istio-injection

# 프록시 정보 확인
## 모든 프록시의 상태를 가져오는 명령이다.
$ istioctl proxy-status

## 프록시 부트스트랩 구성 확인
$ istioctl proxy-config bootstrap istio-ingressgateway-6db854c9f8-6s5zw.istio-system

# 프록시 로그 확인
$ kubectl logs pod/details-v1-795f98b94c-mzhsm -c istio-proxy

# 라우트 정보 확인
## 프록시가 수신 대기 중인 포트 확인
$ istioctl proxy-config listeners istio-ingressgateway-6db854c9f8-6s5zw.istio-system

## 프록시가 특정 포트의 트래픽을 설정하는 방법에 관한 세부 정보 확인
$ istioctl proxy-config listeners istio-ingressgateway-6db854c9f8-6s5zw.istio-system -o json --address 0.0.0.0 --port 15090

## 라우트에 관련된 호스트 확인
$ istioctl proxy-config routes details-v1-795f98b94c-mzhsm --name 9080 -o json

## 결정할 수 있는 클러스터 위치 확인
$ istioctl proxy-config cluster details-v1-795f98b94c-mzhsm --fqdn details.default.svc.cluster.local -o json

## 클러스터 위치에 관한 엔드포인트 확인
$ istioctl proxy-config endpoints details-v1-795f98b94c-mzhsm --cluster "outbound|9080||details.default.svc.cluster.local"
```