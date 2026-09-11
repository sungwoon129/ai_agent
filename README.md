# AI Customer Inquiry Agent

> 고객 문의를 접수하고, 간단한 LLM 분류 결과와 함께 저장·조회하는 Spring Boot API

![Java](https://img.shields.io/badge/Java-25-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F?logo=springboot&logoColor=white)
![Database](https://img.shields.io/badge/Database-H2-4479A1)

## 소개

고객이 남긴 문의를 받아 카테고리, 우선순위, 요약을 생성하고 데이터베이스에 저장합니다. 현재는 외부 API 호출 없이 동작하도록 키워드 기반 `MockLlmService`를 사용하므로, 로컬에서 바로 실행하고 테스트할 수 있습니다.

```text
고객 문의
   │
   ▼
InquiryController ──► InquiryService ──► LlmService
                           │                 │
                           ▼                 ▼
                    InquiryRepository     분류 · 우선순위 · 요약
                           │
                           ▼
                         H2 DB
```

## 주요 기능

- 문의 등록 및 자동 분류
- 문의 단건 조회 및 전체 목록 조회
- 결제·배송·회원·기타 카테고리와 우선순위 부여
- 긴 문의의 앞 30자 요약
- H2 인메모리 데이터베이스 및 콘솔 제공
- 서비스 로직과 LLM 분류 로직 단위 테스트

## 분류 규칙

| 포함 키워드 | 카테고리 | 우선순위 |
| --- | --- | --- |
| `결제`, `카드`, `환불` | `PAYMENT` | `HIGH` |
| `배송`, `택배` | `DELIVERY` | `MEDIUM` |
| `회원`, `로그인` | `MEMBER` | `MEDIUM` |
| 그 외 | `ETC` | `LOW` |

## 기술 스택

| 영역 | 사용 기술 |
| --- | --- |
| Language | Java 25 |
| Framework | Spring Boot 4.1.1, Spring MVC |
| Persistence | Spring Data JPA, Hibernate |
| Database | H2 |
| Test | JUnit 5, Mockito, AssertJ |
| AI 확장 기반 | Spring AI OpenAI Starter |

## 빠르게 시작하기

### 요구 사항

- JDK 25

### 실행

```bash
cd ai_agent
./gradlew bootRun
```

Windows에서는 다음 명령을 사용합니다.

```powershell
cd ai_agent
.\gradlew.bat bootRun
```

애플리케이션은 기본적으로 `http://localhost:8080`에서 실행됩니다.

### LLM 구현체 전환

기본값은 API 키 없이 실행할 수 있는 키워드 기반 mock 구현입니다.

```yaml
app:
  llm:
    provider: mock
```

실제 OpenAI 기반 구현체를 사용하려면 환경 변수에 API 키를 설정하고 아래처럼 바꿉니다. 두 구현체는 같은 `LlmService` 인터페이스를 사용하므로 서비스·컨트롤러 코드를 수정할 필요가 없습니다.

```yaml
spring:
  ai:
    model:
      chat: openai
    openai:
      api-key: ${OPENAI_API_KEY}

app:
  llm:
    provider: openai
```

### 테스트

```bash
cd ai_agent
./gradlew test
```

### H2 콘솔

애플리케이션을 실행한 뒤 [http://localhost:8080/h2-console](http://localhost:8080/h2-console)에 접속합니다.

| 항목 | 값 |
| --- | --- |
| JDBC URL | `jdbc:h2:mem:llmdb` |
| User Name | `sa` |
| Password | 비워 둠 |

## API

기본 경로: `/api/inquiries`

### 문의 등록

`POST /api/inquiries`

```json
{
  "message": "카드 결제가 두 번 처리되었습니다. 빠른 환불 처리를 부탁드립니다."
}
```

응답 `201 Created`

```json
{
  "id": 1,
  "message": "카드 결제가 두 번 처리되었습니다. 빠른 환불 처리를 부탁드립니다.",
  "category": "PAYMENT",
  "priority": "HIGH",
  "summary": "카드 결제가 두 번 처리되었습니다. 빠른 환불 처리를 ..."
}
```

### 문의 단건 조회

`GET /api/inquiries/{id}`

```bash
curl http://localhost:8080/api/inquiries/1
```

존재하지 않는 ID를 조회하면 `IllegalArgumentException`이 발생합니다.

### 문의 전체 조회

`GET /api/inquiries`

```bash
curl http://localhost:8080/api/inquiries
```

## 프로젝트 구조

```text
ai_agent/
├── src/main/java/com/example/cs_agent/
│   ├── controller/     # HTTP API
│   ├── domain/         # JPA 엔티티
│   ├── dto/            # 요청·응답 DTO
│   ├── repository/     # 데이터 접근
│   └── service/        # 문의 처리 및 LLM 분류
├── src/main/resources/
│   └── application.yml
└── src/test/           # 서비스·분류 로직 테스트
```

## 향후 확장 아이디어

- `MockLlmService`를 실제 OpenAI 기반 `LlmService` 구현체로 교체
- 요청 검증 및 예외 응답 형식 표준화
- 문의 상태(접수, 처리 중, 완료)와 담당자 관리
- Swagger/OpenAPI 문서화 및 API 통합 테스트
