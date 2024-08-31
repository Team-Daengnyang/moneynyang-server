### 👩‍👧‍👧 Our Team

|        **🍀 [최승빈](https://github.com/csb9427)**  |    **🍀 [김수영](https://github.com/sootudio)**                 |**🍀 [김아영](https://github.com/a-young-kim)**                 |
  |:-----------------------------------:|:-----------------------------------:|:-----------------------------------:|
|   Leader, Server Developer  |    Server Developer     |   Server Developer |
|        <br />프로젝트 세팅<br /><br />인프라 구축<br /><br />마일스톤 관리<br /><br />핵심 기능 분리 및 정리<br /><br />자료 조사, API 명세서 작성<br /><br />서버 초기 세팅<br /><br />투자 계좌 생성 / 출금 / 계좌정보 조회 / 거래내역 조회 API 개발<br /><br />회원 정보 조회 API 개발<br /><br />입출금 계좌 정보 조회 / 거래내역 조회 / 송금 API 개발<br /><br />시스템 아키텍처 설계<br /><br />CI 설정과 인프라 구축<br /><br />개발 컨벤션 제작<br /><br />기능 명세서 작성|       프로젝트 세팅<br /><br/> Directory 초기 구조 구축<br /><br /> IA 작성, JIRA 일정 관리<br /><br />목표, 반려일지, 챗봇 관련 ERD 작성 및 DB 설계<br /><br />API 명세서 작성<br /><br /> 반려동물 일지 등록, 조회 API 개발<br /><br />저축목표 등록 / 송금 / 출금 / 삭제 / 요약 / 조회 / 상세내역 리스트 조회 API 개발<br /><br />멍이냥 챗봇 모듈 개발 (OpenAI Assistant 사용) <br /><br /> 챗봇 세션 입장 / 대화 API 개발 <br /><br />공통 API 예외 처리 로직 개발 <br /><br /> 프로젝트 초기 폴더 구조 생성 |   프로젝트 세팅<br /><br /> ERD 수정 및 데이터 수정<br /><br />기능 명세서 관리<br /><br />반려동물 관리, 보험, 회원관리 관련 ERD 작성 및 DB 설계<br /><br />반려동물 정보 등록 / 조회 / 수정 API 개발<br /><br />반려동물 관련 보험 데이터 수집 및 보험 상세 정보 조회 API 개발<br /><br />회원 로그인 / 회원가입 / 이메일 중복체크 API 개발<br /><br />계좌 커스텀 이미지 수정 API 개발<br /><br />이미지 업로드를 위한 S3 저장소 생성 및 백엔드 로직과 연결<br /><br />계좌 거래 내역을 이용한 소비 내역 분석 로직 구현 |  

### 📑 Architecture
<img width="1920" alt="21" src="https://github.com/user-attachments/assets/ab7ac72b-98ff-4b4b-94bd-3f0c50959a60">

### 📋 Flow Chart
<img width="1920" alt="23" src="https://github.com/user-attachments/assets/fb649314-e171-402d-ae60-e72b4460b3ea">

### 📖 Directory
```
src
├─main
│  ├─java
│  │  └─com
│  │      └─fav
│  │          └─daengnyang
│  │              ├─domain
│  │              │  ├─account
│  │              │  │  ├─controller
│  │              │  │  ├─entity
│  │              │  │  ├─repository
│  │              │  │  └─service
│  │              │  │      └─dto
│  │              │  │          ├─request
│  │              │  │          └─response
│  │              │  ├─cashwalk
│  │              │  │  ├─controller
│  │              │  │  ├─entity
│  │              │  │  ├─repository
│  │              │  │  └─service
│  │              │  │      └─dto
│  │              │  │          ├─request
│  │              │  │          └─response
│  │              │  ├─chatbot
│  │              │  │  ├─controller
│  │              │  │  ├─entity
│  │              │  │  ├─repository
│  │              │  │  └─service
│  │              │  │      └─dto
│  │              │  │          ├─request
│  │              │  │          └─response
│  │              │  ├─insurance
│  │              │  │  ├─controller
│  │              │  │  ├─entity
│  │              │  │  ├─repository
│  │              │  │  └─service
│  │              │  │      └─dto
│  │              │  │          ├─request
│  │              │  │          └─response
│  │              │  ├─member
│  │              │  │  ├─controller
│  │              │  │  ├─entity
│  │              │  │  ├─repository
│  │              │  │  └─service
│  │              │  │      └─dto
│  │              │  │          ├─request
│  │              │  │          └─response
│  │              │  ├─memberDetail
│  │              │  │  ├─controller
│  │              │  │  ├─entity
│  │              │  │  ├─repository
│  │              │  │  └─service
│  │              │  │      └─dto
│  │              │  │          ├─request
│  │              │  │          └─response
│  │              │  ├─pet
│  │              │  │  ├─controller
│  │              │  │  ├─entity
│  │              │  │  ├─repository
│  │              │  │  └─service
│  │              │  │      └─dto
│  │              │  │          ├─request
│  │              │  │          └─response
│  │              │  ├─target
│  │              │  │  ├─controller
│  │              │  │  ├─entity
│  │              │  │  ├─repository
│  │              │  │  └─service
│  │              │  │      └─dto
│  │              │  │          ├─request
│  │              │  │          └─response
│  │              │  └─targetDetail
│  │              │      ├─controller
│  │              │      ├─entity
│  │              │      ├─repository
│  │              │      └─service
│  │              │          └─dto
│  │              │              ├─request
│  │              │              └─response
│  │              └─global
│  │                  ├─auth
│  │                  │  ├─dto
│  │                  │  ├─exception
│  │                  │  ├─filter
│  │                  │  └─utils
│  │                  ├─aws
│  │                  │  └─service
│  │                  ├─config
│  │                  ├─dto
│  │                  │  └─response
│  │                  ├─exception
│  │                  ├─handler
│  │                  ├─transaction
│  │                  │  └─service
│  │                  │      └─dto
│  │                  │          └─request
│  │                  └─web
│  │                      └─dto
│  │                          └─response
│  └─resources
└─test
    ├─java
    │  └─com
    │      └─fav
    │          └─daengnyang
    └─resources
```

### 🧱 How to build

1. 프로젝트 폴더로 이동
   
   ```
   cd 프로젝트_폴더명
   ```
2. Build 명령어 입력
    - 맥/리눅스의 경우
        ```
        $./gradlew build
        ```
    
    - 윈도우의 경우
        ```
        $gradlew build
        ```
3. 실행 명령어 입력
   
   ```
   $java -jar build/libs/[프로젝트명]-0.0.1-SNAPSHOT.jar
   ```
4. 애플리케이션 실행 확인

    애플리케이션이 정상적으로 실행되었는지 확인하려면, 브라우저에서 `http://localhost:8080`을 열어봅니다.

### ✉️ Commit Messge Rules

**댕냥이** 들의 **Git Commit Message Rules**

- 반영사항을 바로 확인할 수 있도록 작은 기능 하나라도 구현되면 커밋을 권장합니다.
- 기능 구현이 완벽하지 않을 땐, 각자 브랜치에 커밋을 해주세요.

### 📌 Commit Convention

**[태그] 제목의 형태**

| 태그 이름 |                       설명                        |
| :-------: | :-----------------------------------------------: |
|   FEAT    |             새로운 기능을 추가할 경우             |
|    FIX    |                 버그를 고친 경우                  |
|   CHORE   |                    짜잘한 수정                    |
|   DOCS    |                     문서 수정                     |
|   INIT    |                     초기 설정                     |
|   TEST    |      테스트 코드, 리펙토링 테스트 코드 추가       |
|  RENAME   | 파일 혹은 폴더명을 수정하거나 옮기는 작업인 경우  |
|   STYLE   | 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우 |
| REFACTOR  |                   코드 리팩토링                   |

### **커밋 타입**

- `[태그] 설명` 형식으로 커밋 메시지를 작성합니다.
- 태그는 영어를 쓰고 대문자로 작성합니다.

예시 >

```
  [FEAT] 검색 api 추가
```

### **💻 Github mangement**

**댕냥이** 들의 WorkFlow : **Gitflow Workflow**

- Develop, Feature, Hotfix 브랜치

- 개발(develop): 기능들의 통합 브랜치

- 기능 단위 개발(feature): 기능 단위 브랜치

- 버그 수정 및 갑작스런 수정(hotfix): 수정 사항 발생 시 브랜치

- 개발 브랜치 아래 기능별 브랜치를 만들어 작성합니다.

### ✍🏻 Code Convention

#### Code
하나의 메서드(method) 길이 12줄, 깊이(depth) 3 이내로 작성합니다.
Lombok의 val을 사용합니다.
#### Entity
id 자동 생성 전략은 IDENTITY를 사용합니다.
@NoArgsConstructor 사용 시 access를 PROTECTED로 제한합니다.

#### 네이밍은 아래와 같이 정의합니다.
Controller DTO: ${Entity명}${복수형일 경우 List 추가}${행위 또는 상태}${Request/Response}<br>
Service DTO: ${Entity명}${복수형일 경우 List 추가}${행위 또는 상태}Service${Request/Response}
#### Response
요청 성공 시, BaseResponse와 SuccessCode(인터페이스)의 구현체를 사용합니다.
예외 발생 시, Exception과 FailureCode(인터페이스)의 구현체를 사용합니다.
#### Service, Repository
DB를 호출하는 경우 메서드명에 save, find, update, delete 용어를 사용합니다.
비즈니스 로직일 경우 메서드명에 create, get, update, delete, 그 외 용어를 사용합니다.
복수형은 ${Entity명}s로 표현합니다.

### 📍 Gitflow 규칙

- Develop에 직접적인 commit, push는 금지합니다.
- 커밋 메세지는 다른 사람들이 봐도 이해할 수 있게 써주세요.
- 작업 이전에 issue 작성 후 pullrequest 와 issue를 연동해 주세요.
- 풀리퀘스트를 통해 코드 리뷰를 전원이 코드리뷰를 진행합니다.
- 기능 개발 시 개발 브랜치에서 feature/기능 으로 브랜치를 파서 관리합니다.
- feature 자세한 기능 한 가지를 담당하며, 기능 개발이 완료되면 각자의 브랜치로 Pull Request를 보냅니다.
- 각자가 기간 동안 맡은 역할을 전부 수행하면, 각자 브랜치에서 develop브랜치로 Pull Request를 보냅니다.  
  **develop 브랜치로의 Pull Request는 상대방의 코드리뷰 후에 merge할 수 있습니다.**

### ❗️ branch naming convention

- develop
- feature/issue_number
- fix/issue_number
- release/version_number
- hotfix/issue_number

예시 >

```
  feature/#3
```

### 📋 Code Review Convention

- P1: 꼭 반영해주세요 (Request changes)
- P2: 적극적으로 고려해주세요 (Request changes)
- P3: 웬만하면 반영해 주세요 (Comment)
- P4: 반영해도 좋고 넘어가도 좋습니다 (Approve)
- P5: 그냥 사소한 의견입니다 (Approve)
  
### 🚀 Test Code Convention

1. given, when, then을 사용한다.
2. 테스트 메서드명은 다음과 같이 작성한다. -> 메서드명_테스트하고자하는상태_예상되는결과 (ex. customizeAccount_NoColorProvided_DefaultColorIsSet)
3. 설마 이런 거까지 생각해야하나싶은 거까지 작성한다. (ex. 계좌 커스텀 시에 값이 입력되지 않는다면 기본 색상으로 정해진다.)
4. 다수의 값을 다룰 때는 @ParameterizedTest를 활용한다.
