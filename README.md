# GG
| 2025.02.07 ~ 2025.02.18
### 프로젝트명 (5인 팀 프로젝트)
> 연예인 통합 서비스 StarLink (연예인 구독 전용 콘텐츠, 연예인 굿즈 스토어, 팬과 스타의 라이브 채팅)


|서비스 | 상세 내용 및 역할 분담 |
|---|---|
| eureka-server, API Gateway | MSA 환경 구성, 서비스 등록 및 라우팅, 배포 |
| msa-user | 회원가입, 로그인, 회원정보 관리 |
| msa-starboard | 구독 전용 게시판 |
| msa-products | 굿즈 스토어 |
| msa-chat | 팬과 스타의 라이브 채팅 |

---



## 개발 환경
| 공통 | • 문서화 도구 : Notion <br> • 버전 관리 : Github <br> • 디자인 도구 : canva, Figma|
|---|---|
|IDE(통합 개발 환경)|IntelliJ IDEA|
|언어 및 프레임워크|Java17, Spring Boot|
|데이터베이스| • ORM: JPA <br> • 데이터베이스: MySQL (Docker 컨테이너로 실행) <br> • 캐싱 : Redis (Docker 컨테이너로 실행) |
|버전관리|Git|
|의존성| Thymeleaf, Lombok, Spring Boot DebTools, Spring Reactive Web, H2 Database, Spring Data JPA, Spring Security, Kafka, Redis, Eureka, JWT |
|배포 및 환경|컨테이너 오케스트레이션: Docker Compose <br> 서버 환경: AWS EC2, GitHub Actions <br> 관리 서비스: MySQL, Redis, Kafka, Eureka (모두 Docker Compose로 실행)|



## 배포 과정
1. Docker Compose로 MSA 프로젝트를 구성하고, 
2. GitHub Actions에서 `docker-compose.yml`을 기반으로 애플리케이션을 빌드한 후 Docker Hub에 push 
3. 이후 GitHub Actions가 EC2에 연결해 이미지를 pull 
4. `docker-compose.prod.yml` 파일을 사용해 컨테이너를 배포 및 실행

- 기타사항
    - 메모리 확보(용량 확인 : `free -h`)
        - t2.micro(프리티어) 버전은 1G메모리 => 스프링부트 빌드 과정이 진행 안될 수 있음
        - swap 설정(스왑 메모리 추가(가상 메모리))
    - 디스크 확보 : 디스크 부족으로 서버 에러(용량 확인 : `df -h`)
        - AWS에서 서버(EC2)의 디스크가 부족하면 → `EBS 볼륨 크기 변경(10GB → 20GB)`
        1. **AWS 콘솔 → EC2 → Elastic Block Store(EBS) → 볼륨 클릭**
        2. **"Actions" → "Modify Volume" 클릭**
        3. **새로운 크기 입력 (예: 10GB → 20GB)**
        4. **"Modify" 클릭 후 적용**

<details>
<summary><b>aws ec2에서 docker-compose 실시간 로그 확인</b></summary>
- aws에서 docker-compose.prod.yml 파일을 사용 중
- `docker-compose -f docker-compose.prod.yml logs -f`
</details>
-
<details>
<summary><b>로컬에서 docker-compose 테스트</b></summary>
<div markdown="1">

- Docker Compose 기반의 MSA 환경 구축 (Redis, Kafka, Eureka, Database 등 포함)

- application.properties, redisconfig 수정

```
    docker-compose up --build
```
</div>
</details>



## API 명세

---

### **msa-user | 회원가입, 로그인, 회원정보 관리**

#### AuthController API

| **Endpoint**   | **Method** | **Description**       | **Request Parameters**              | **Response**                                                                 |
|-----------------|------------|-----------------------|--------------------------------------|-------------------------------------------------------------------------------|
| `/auth/login`   | `POST`     | 사용자 로그인 처리    | `LoginReqDto` (JSON)                | `200 OK`: JWT 토큰, 이메일, 역할 반환`401 Unauthorized`: 로그인 실패 메시지 반환 |
| `/auth/logout`  | `POST`     | 사용자 로그아웃 처리  | Header: `X-Auth-User`, `Authorization` | `200 OK`: `"로그아웃 성공"` 반환                                             |

#### UserController API

| **Endpoint**           | **Method** | **Description**                 | **Request Parameters**          | **Response**                                                                 |
|-------------------------|------------|---------------------------------|----------------------------------|-------------------------------------------------------------------------------|
| `/user/signup`          | `POST`     | 사용자 회원가입 처리            | `UserDto` (JSON)                | `200 OK`: `"회원가입 성공"` 반환                                              |
| `/user/msa/login`       | `GET`      | 로그인 페이지 반환              | 없음                             | HTML 페이지 반환                                                              |
| `/user/celebrity/{id}`  | `GET`      | 특정 사용자가 연예인인지 확인    | Path: `id` (Long)               | `200 OK`: Boolean 값 반환                                                     |
| `/user/id`              | `GET`      | 이메일로 사용자 ID 조회          | Query: `email` (String)         | `200 OK`: 사용자 ID 반환`404 Not Found`: 사용자 없음                      |

---

### **msa-starboard | 구독 전용 게시판**

#### PostController API

| **Endpoint**           | **Method**  | **Description**                 | **Request Parameters**                    | **Response**                                                                 |
|-------------------------|-------------|---------------------------------|-------------------------------------------|-------------------------------------------------------------------------------|
| `/api/posts/create`     | `POST`      | 게시글 생성 (연예인 전용)       | FormData: `post`, Optional: `image`       | `201 Created`: 생성된 게시글 반환                                            |
| `/api/posts/{id}`       | `DELETE`    | 게시글 삭제                    | Path: `id` (Long), Header: `X-Auth-User`  | `204 No Content`: 삭제 성공                                                  |
| `/api/posts`            | `GET`       | 모든 게시글 조회                | 없음                                      | `200 OK`: 게시글 목록 반환                                                   |
| `/api/posts/search`     | `GET`       | 게시글 검색                     | Query: `keyword`, Query: `filterType`     | `200 OK`: 검색된 게시글 목록 반환                                            |

#### CommentController API

| **Endpoint**           | **Method**  | **Description**                 | **Request Parameters**                    | **Response**                                                                 |
|-------------------------|-------------|---------------------------------|-------------------------------------------|-------------------------------------------------------------------------------|
| `/api/comments/create`  | `POST`      | 댓글 생성                      | Body: Comment 객체, Header: `X-Auth-User`  | `201 Created`: 생성된 댓글 반환                                              |
| `/api/comments/{id}`    | `DELETE`    | 댓글 삭제                      | Path: `id`, Header: `X-Auth-User`         | `204 No Content`: 삭제 성공                                                  |

---

### **msa-products | 굿즈 스토어**

#### ProductsController API

| **Endpoint**             | **Method**  | **Description**                 | **Request Parameters**                    | **Response**                                                                 |
|---------------------------|-------------|---------------------------------|-------------------------------------------|-------------------------------------------------------------------------------|
| `/pdts`                 		| `GET`      	| 굿즈 전체 목록 조회            	| 없음                                     		| 굿즈 목록 반환 (`ProductDto`)                                                	|
| `/pdts/detail/{pdtId}`  		| `GET`      	| 굿즈 상세 조회                 		| Path: pdtId                             		| 상세 정보 반환 (`ProductDetailDto`)                                           	|
| `/pdts/search?keyword=` 		| `GET`      	| 키워드로 굿즈 검색             		| Query: keyword                          		| 검색된 굿즈 목록 반환                                                       	|
| `/pdts/filter?minPrice=&maxPrice=`  	| `GET`    		| 가격 필터링                    			| Query: minPrice, maxPrice               		| 필터링된 굿즈 목록 반환                                                      |
| 서비스 확장 (customgoods 추가)-- | -- | -- | -- | -- |
| `/customgoods`  	| `GET`     		| 커스텀 굿즈 조회                   			| 없음               		| 커스텀 굿즈 목록 반환 (`CustomGoodsDto`) |
| `/customgoods/save`  	| `POST`     		| 커스텀 굿즈 저장                   			| Body: `CustomGoodsDto` (json)              		| `200 OK` : `커스텀 굿즈 저장 완료` |    

---

### **msa-chat | 팬과 스타의 라이브 채팅**

#### LiveController API

| **Endpoint**           | **Method**  | **Description**                 | **Request Parameters**                    | **Response**                                                                 |
|-------------------------|-------------|---------------------------------|-------------------------------------------|-------------------------------------------------------------------------------|
| `/chat/send`            | WebSocket   | 실시간 채팅 메시지 전송         | JSON Body: LiveMessageDto 객체            | WebSocket 메시지 전송                                                        |
| `/chat/history`         | `GET`       | 채팅 히스토리 조회              | 없음                                     			| 채팅 메시지 목록 반환 (`List`)                                |

---
