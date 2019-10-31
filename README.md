Neo4j API 接口服务，使用低版本的Spring Boot，没有集成 SDN 和 OGM
---
1.项目结构

```
root
├── logs
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── service
│   │   │           └── guarantee
│   │   │               ├── apps
│   │   │               ├── callback
│   │   │               ├── config
│   │   │               ├── controller
│   │   │               ├── dao
│   │   │               ├── domain
│   │   │               │   ├── request
│   │   │               │   └── response
│   │   │               ├── exception
│   │   │               ├── methods
│   │   │               ├── service
│   │   │               │   └── impl
│   │   │               ├── thread
│   │   │               └── utils
│   │   ├── resources
│   │   └── webapp
│   └── test
│       └── java
│           └── com
│               └── service
│                   └── guarantee
```
