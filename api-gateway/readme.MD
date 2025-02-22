# API-GATEWAY
## URL 설정
0. 게이트웨이에서 각종 서비스를 등록하는 RouteLocator
    - ApiGatewayApplication.java
   ```
        .route("msa-user-service",
                r -> r.path("/auth/**").uri("lb://msa-user-service"))
    ```
1. 인증없이 통과 처리
   - application.properties
       ```
       security.free-paths= /, /auth/login, ...추가
       ```