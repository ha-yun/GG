# GG
### 프로젝트명
> 연예인 통합 서비스 StarLink (연예인 구독 전용 콘텐츠, 연예인 굿즈 스토어, 팬과 스타의 라이브 채팅)


|서비스 | 상세 내용 |
|---|---|
| eureka-server, API Gateway | MSA 환경 구성, 서비스 등록 및 라우팅 |
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
    - 메모리 확보 : t2.micro(프리티어) 버전은 1G메모리 => 스프링부트 빌드 과정이 진행 안될 수 있음
        - swap 설정(스왑 메모리 추가(가상 메모리))

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

