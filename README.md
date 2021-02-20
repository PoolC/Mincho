# MINCHO

풀씨 백엔드 API 개편 프로젝트

## Getting Started

### Requirements

- Java 11 이상

### Environment Variables

**Terminal에서 실행할 경우**

```shell
export PROJECT_NAME_HERE_SECRET_KEY=example_secret_key
export EXPIRE_LENGTH_IN_MILLISECONDS=3600000
```

혹은 [direnv](https://direnv.net/) 를 사용하여 환경변수 설정을 자동화 할 수 있습니다.

**IntelliJ에서 실행할 경우**

1. Run | Edit Configurations (`⌃⌥R` + `0`)
2. Templates
3. **Gradle** 에서

- Gradle project: 프로젝트 root(/path/to/PROJECT_NAME_HERE)
- tasks: *:test*
- Environment variables: 위 환경변수 추가

4. **Spring Boot** 도 Environment variables을 설정합니다.

### Build

```shell
./gradlew build
```

### Usage

```shell
./gradlew bootRun
```

**주의:** *build & run* 및 *test* (`⌘,` > `Build, Execution, Deployment` > `Build Tools` > `Gradle`)를 intelliJ로 설정할 경우,
java
compiler(`⌘,` > `Build, Execution, Deployment` > `Compiler` > `Java Compiler` > `Additional command line parameters`)
에 `-parameters` 파라미터를 추가해야 합니다.

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
