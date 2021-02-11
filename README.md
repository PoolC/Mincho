# PROJECT_NAME_HERE

풀씨 백엔드 API 개편 프로젝트

## Getting Started

### Requirements

- Java 11 이상
- Set environment variables

**Terminal**

```shell
export PROJECT_NAME_HERE_SECRET_KEY=example_secret_key
export EXPIRE_LENGTH_IN_MILLISECONDS=3600000
```

혹은 [direnv](https://direnv.net/) 를 사용하여 환경변수 설정을 자동화 할 수 있습니다.

**IntelliJ**

1. Run | Edit Configurations (`⌃⌥R` + `0`)
2. Templates
3. **Gradle** 에서

- Gradle project: 프로젝트 root(/path/to/PROJECT_NAME_HERE)
- tasks: *:test*
- Environment variable`s: 위 환경변수 추가

4. **Spring Boot** 도 Environment variables을 설정합니다.

### Build

```shell
./gradlew build
```

### Usage

```shell
./gradlew bootRun
```

### Tests

```shell
./gradlew test
```

### Docs

```shell
./gradlew asciidoctor
```

`/build/asciidoc/html5/api-doc.html` 에서 api 문서를 확인할 수 있습니다.

### Deploy

TODO with docker