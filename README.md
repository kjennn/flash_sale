
# 🚀 Flash Sale - 선착순 구매 시스템

이 프로젝트는 대규모 트래픽이 몰리는 선착순 구매 상황에서 **데이터 정합성(재고 관리)**을 보장하고, 안정적인 API 응답을 제공하기 위해 설계되었습니다.

## 1. 프로젝트 실행 방법

### 환경 요구 사항

* Java 17+
* Spring Boot 3.x
* MySQL 8.0 (Docker 사용 권장)

### 실행 순서

1. **DB 실행 (Docker)**
* `docker-compose up -d`


2. **애플리케이션 실행**
* `./gradlew bootRun`


3. **테스트 코드 실행 (동시성 검증)**
* `./gradlew test` (특히 `OrderServiceTest`의 100명 동시 요청 테스트 확인)



---

## 2. 문제 해결 전략

### ✅ 선택한 Lock 방식: **비관적 락 (Pessimistic Lock)**

* **이유:** 선착순 구매 특성상 짧은 시간에 동일한 로우(Row)에 대한 수정 요청이 집중됩니다. 낙관적 락(Optimistic Lock)은 충돌 발생 시 롤백 및 재시도 로직을 직접 구현해야 하며, 충돌이 잦을수록 오히려 성능이 저하될 수 있습니다.
* **장점:** DB 수준에서 `FOR UPDATE` 쿼리를 통해 확실한 잠금을 보장하므로 데이터 정합성이 완벽하게 유지됩니다.

### ✅ 트랜잭션 범위 및 동시성 제어

* **트랜잭션 범위:** `OrderService.createOrder` 전체에 `@Transactional`을 적용했습니다.
1. 상품 재고 차감
2. 유저 포인트 차감
3. 주문 내역 생성


* **원자성 보장:** 위 과정 중 하나라도 실패(예: DB 제약 조건 위반)할 경우 전체 로직을 롤백하여, **'돈은 빠져나갔는데 물건은 못 사는'** 상황을 원천 차단했습니다.

### ✅ 공통 응답 규격 (Common Response)

* 모든 API 응답을 `ApiResponse<T>` 객체로 통일하여 성공 여부, 비즈니스 에러 코드, 메시지를 일관되게 제공합니다. 이를 통해 클라이언트(프론트엔드)와의 협업 효율을 극대화했습니다.

---

## 3. API 명세

### [POST] 주문 생성 (선착순 구매)

* **URL:** `/api/orders`
* **Request Body:**
```json
{
  "userId": 1,
  "productId": 10
}

```


* **Response (Success):**
```json
{
  "success": true,
  "code": "SUCCESS",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": 123
}

```



### [POST] 포인트 충전

* **URL:** `/api/users/{id}/charge`
* **Request Body (raw/text):** `5000`
* **Response (Success):**
```json
{
  "success": true,
  "code": "SUCCESS",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": null
}

```



### [에러 응답 예시]

```json
{
  "success": false,
  "code": "ORDER_ERROR_001",
  "message": "상품 재고가 소진되어 구매할 수 없습니다.",
  "data": null
}

```

---

## 4. 핵심 기술 역량 (Self-Check)

* **멀티스레드 테스트:** `ExecutorService`와 `CountDownLatch`를 활용하여 100개의 동시 요청 상황에서 재고가 정확히 0이 됨을 검증했습니다.
* **예외 처리:** `GlobalExceptionHandler`를 통해 비즈니스 예외(재고 부족, 포인트 부족)를 관리하고 적절한 HTTP 상태 코드와 메시지를 반환합니다.

