# Minikube
```bash
# 시작
minikube start

# 컨테이너 이미지를 minikube 에서 직접 관리하도록 설정: 이미지를 로컬 환경에 저장하고 레지스트리로 전송한 다음 Minikube 에서 가져오는 대신 컨테이너 이미지를 Minikube 인스턴스에 직접 저장할 수 있다. 도커 허브등에 푸시할 필요가 없어진다.
eval $(minikube docker-env)
## 설정 해제
eval $(minikube docker-env -u)

# LoadBalancer 타입 service 사용할 때 사용
minikube tunnel

# Clean up
## Minikube VM 제거, 초기화 후 재시작하려 하고 싶을 때 사용
minikube delete
## Minikube VM 정지, 리소스 유지할 때 사용
minikube stop
```

# Docker
```bash
# 이미지 빌드
docker build <경로> -t <image-name>:<tag> --build-arg <arg-name>=<arg-value>

# 이미지 목록
docker images
```

# Kubernetes
```bash
# 배포
kubectl apply -f <yaml-file>

# port forwarding
kubectl port-forward svc/frontendservice 8080:80

# 동작 확인
kubectl get pod,svc,deploy,all -o yaml,-w,-o wide
kubectl describe pod,svc,deploy <name>
kubectl logs <pod-name>
kubectl exec pod/<pod-name> -it -- sh -il
frontend-deployment-8d465cb6-lmd46:/# wget -qO - http://webservice
kubectl exec pod/frontend-deployment-8496d98df7-c299q -- wget -O - http://webservice/

# 삭제
kubectl delete -f <yaml-file>
kubectl delete all --all
```

# Istio
- https://istio.io/latest/docs/setup/getting-started/
```bash
# Istio 설치
cd istio-1.21.0
export PATH=$PWD/bin:$PATH
istioctl install --set profile=demo -y
kubectl label namespace default istio-injection=enabled

kubectl apply -f samples/addons
kubectl rollout status deployment/kiali -n istio-system
istioctl dashboard kiali

# 사이드카 로그
kubectl logs <pod-name> -c istio-proxy -n <namespace> -f

# Clean up
## 실행 중인 Istio 프로세스 제거
killall istioctl
kubectl delete -f samples/addons
istioctl uninstall -y --purge
kubectl delete namespace istio-system
kubectl label namespace default istio-injection-

```

# Fortio
- https://github.com/fortio/fortio
```bash
#-c 1: 동시에 수행될 요청의 수(concurrency level)를 1로 설정합니다. 이는 한 번에 하나의 요청만 서버로 보내겠다는 의미입니다.
#-n 30: 총 요청 횟수를 30으로 설정합니다. 즉, 테스트 동안 서버에 총 30개의 요청을 보냅니다.
#-qps 1: 초당 쿼리(Query Per Second) 수를 1로 설정합니다. 이는 매 초마다 하나의 요청을 서버로 보내겠다는 의미입니다.
#-nocatchup: 이 옵션은 Fortio가 느려진 요청을 보상하기 위해 빠르게 추가 요청을 보내는 "catch-up" 모드를 비활성화합니다. 각 요청은 정확히 지정된 간격으로 전송됩니다.
#-uniform: 요청 간 시간 간격을 일정하게 유지합니다. 기본적으로 Fortio는 지연 시간을 시뮬레이션하기 위해 요청 간에 약간의 변동을 추가할 수 있지만, 이 옵션을 사용하면 모든 요청이 정확히 같은 간격으로 전송됩니다.
#-loglevel Warning: 로그 수준을 "Warning"으로 설정합니다. 이는 Fortio가 경고(warnings) 또는 그보다 더 심각한 메시지만 출력하도록 설정하는 것입니다. 정보성 메시지(info)나 디버그 메시지(debug)는 표시되지 않습니다.
#http://10.102.146.12/: 테스트 대상 URL입니다.
#요약하자면, 이 명령은 http://10.102.146.12/ 주소로 총 30개의 HTTP 요청을 보내며, 한 번에 하나씩, 매초마다 요청을 보내는 테스트를 실행합니다. 출력 로그는 경고 또는 그보다 중요한 메시지만 포함할 것입니다.
fortio load -c 1 -n 30 -qps 1 -nocatchup -uniform -loglevel Warning http://10.102.146.12/
```
