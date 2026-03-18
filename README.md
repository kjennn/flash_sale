
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


### ✅ 결제 멱등성(Idempotency) 및 외부 API 격리

단순 주문을 넘어, **실제 결제(PG 연동)** 상황에서 발생할 수 있는 중복 결제와 타임아웃 문제를 해결하기 위해 다음과 같은 전략을 사용했습니다.

####  ✅ 퍼사드 패턴(Facade Pattern)을 통한 트랜잭션 분리**

* **문제:** 외부 PG사 API 호출을 `@Transactional` 내부에 두면, 네트워크 지연 시 DB 커넥션을 점유하여 시스템 전체 장애(Connection Pool 고갈)로 이어질 수 있습니다.
* **해결:** `PaymentFacade`를 도입하여 **[DB 준비] - [외부 API 호출] - [DB 완료]**의 각 단계를 별도의 트랜잭션으로 분리했습니다. 이를 통해 외부 시스템의 불안정성이 내부 DB 자원에 영향을 주지 않도록 격리했습니다.

#### ✅ DB Unique 제약 조건을 활용한 멱등성 보장**

* **문제:** 동일한 결제 요청이 아주 짧은 찰나에 동시 유입될 경우, 애플리케이션 레벨의 `if (isPresent)` 체크만으로는 중복 `INSERT`를 막을 수 없습니다. (Race Condition)
* **해결:** DB 테이블의 `order_id` 컬럼에 **Unique Index**를 설정
* 10명의 사용자가 동시에 요청해도 DB 레벨에서 1명만 성공시키고 9명은 거절합니다

#### ✅ 상태 기반 결제 프로세스 (State Machine)**

* 결제 상태를 `READY` -> `DONE` / `FAIL`로 엄격히 관리합니다.
* 외부 API 호출 전 `READY` 상태로 먼저 영속화하여, API 호출 도중 시스템이 셧다운되더라도 나중에 어떤 결제가 누락되었는지 추적할 수 있는 **결제 이력의 가시성**을 확보했습니다.


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
* **멱등성 검증 테스트:** `CountDownLatch`를 활용해 동일 주문에 대한 **동시 결제 요청 테스트**를 수행하여, 단 1건의 결제만 성공하고 DB 유니크 제약 조건이 정상 작동함을 검증했습니다.
  

