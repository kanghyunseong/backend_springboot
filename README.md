
![Version](https://img.shields.io/badge/version-0.0.1--SNAPSHOT-blue?style=flat-square)
![License](https://img.shields.io/badge/license-MIT-green?style=flat-square)
![Status](https://img.shields.io/badge/status-Development-yellow?style=flat-square)

# PCAR - 전기차 공유 서비스

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen?style=flat-square&logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring%20Security-Latest-brightgreen?style=flat-square&logo=spring)
![MyBatis](https://img.shields.io/badge/MyBatis-3.0.5-red?style=flat-square)
![Oracle](https://img.shields.io/badge/Oracle-Database-red?style=flat-square&logo=oracle)
![Gradle](https://img.shields.io/badge/Gradle-7.x-02303A?style=flat-square&logo=gradle)
![JWT](https://img.shields.io/badge/JWT-0.12.3-black?style=flat-square&logo=jsonwebtokens)

## 한 줄 소개

전기차를 쉽게 예약하고 충전소를 찾을 수 있는 통합 플랫폼

## 프로젝트 소개

PCAR는 전기차 공유 서비스를 제공하는 웹 애플리케이션입니다. 사용자는 전기차를 조회하고 예약할 수 있으며, 주변 충전소를 검색하고 리뷰를 작성할 수 있습니다. 또한 커뮤니티 게시판을 통해 다른 사용자들과 정보를 공유할 수 있습니다. 관리자는 차량 관리, 예약 관리, 게시판 관리 등 다양한 관리 기능을 제공받습니다.

## 주요 기능

### 1. 전기차 예약 시스템

- 전기차 목록 조회 및 상세 정보 확인
- 예약 생성, 수정, 취소, 반납 기능
- 예약 내역 및 히스토리 조회
- 예약 확인 페이지 제공

### 2. 충전소 검색 및 리뷰

- 현재 위치 기반 주변 충전소 검색
- 충전소 이름으로 검색
- 충전소 상세 정보 조회
- 충전소 리뷰 작성 및 삭제
- 공공데이터 API 연동을 통한 실시간 충전소 정보 제공

### 3. 회원 관리 및 인증

- 일반 회원가입 및 로그인
- 네이버, 카카오 소셜 로그인 연동
- JWT 기반 인증 시스템
- 프로필 수정 및 비밀번호 변경
- 면허증 이미지 업로드 기능
- 회원 탈퇴 기능

### 4. 게시판 및 커뮤니티

- 일반 게시판 CRUD 기능
- 이미지 게시판 기능
- 댓글 작성, 수정, 삭제
- 게시글 검색 기능 (제목, 내용, 작성자)
- 공지사항 조회
- 게시글 신고 기능

### 5. 관리자 기능

- 전기차 등록, 수정, 삭제 관리
- 예약 내역 전체 조회 및 취소
- 일일 예약 통계 조회
- 게시판 신고 관리
- 공지사항 관리
- 회원 관리
- 환경 설정 관리

## 기술 스택

### Backend

- **Java** 21
- **Spring Boot** 3.5.7
- **Spring Security** - 인증 및 권한 관리
- **MyBatis** 3.0.5 - 데이터베이스 연동
- **JWT** (jjwt 0.12.3) - 토큰 기반 인증
- **Oracle Database** (ojdbc11) - 데이터베이스
- **Lombok** - 보일러플레이트 코드 감소
- **Gradle** - 빌드 도구

### API 연동

- **네이버 소셜 로그인 API**
- **카카오 소셜 로그인 API**
- **공공데이터포털 충전소 정보 API**

### 기타

- **Spring Validation** - 데이터 검증
- **MultipartFile** - 파일 업로드 처리

## 프로젝트 구조

```
back/
├── src/main/java/com/kh/pcar/back/
│   ├── admin/                    # 관리자 기능
│   │   ├── boardsDeclaration/    # 게시판 신고 관리
│   │   ├── cars/                 # 차량 관리
│   │   ├── Enviroments/          # 환경 설정
│   │   ├── notice/               # 공지사항 관리
│   │   └── user/                 # 회원 관리
│   ├── auth/                     # 인증 관련
│   │   ├── controller/           # 인증 컨트롤러
│   │   └── model/                # 인증 모델 (소셜 로그인 등)
│   ├── boards/                   # 게시판 기능
│   │   ├── board/                # 일반 게시판
│   │   ├── comment/              # 댓글
│   │   ├── imgBoard/             # 이미지 게시판
│   │   ├── imgComment/           # 이미지 게시판 댓글
│   │   └── notice/               # 공지사항
│   ├── cars/                     # 전기차 관련
│   │   ├── controller/           # 차량, 예약, 리뷰 컨트롤러
│   │   └── model/                # 차량, 예약, 리뷰 모델
│   ├── common/                   # 공통 응답 클래스
│   ├── configuration/            # 설정 클래스
│   │   ├── filter/               # JWT 필터
│   │   └── SecurityConfigure.java
│   ├── exception/                # 예외 처리
│   ├── file/                     # 파일 서비스
│   ├── main/                     # 메인 페이지
│   ├── member/                   # 회원 관리
│   ├── station/                  # 충전소 관련
│   ├── token/                    # 토큰 관리
│   └── util/                     # 유틸리티 (페이징 등)
│
├── src/main/resources/
│   ├── application.yml           # 설정 파일
│   └── mapper/                   # MyBatis 매퍼 XML
│
└── build.gradle                  # Gradle 빌드 설정
```

## 트러블슈팅

### 1. JWT 토큰 인증 문제

**문제**: 프론트엔드에서 토큰을 전송했지만 인증이 실패하는 경우 발생
**해결**: JWT 필터에서 토큰 검증 로직을 개선하고, Security 설정에서 인증 경로를 명확히 구분

### 2. 파일 업로드 크기 제한

**문제**: 대용량 이미지 파일 업로드 시 에러 발생
**해결**: `application.yml`에서 `multipart.max-file-size`와 `max-request-size`를 100MB로 설정

### 3. 소셜 로그인 콜백 처리

**문제**: 네이버와 카카오의 콜백 URL이 다르게 설정되어 있어 통합 관리가 어려움
**해결**: 각 소셜 로그인 서비스별로 별도의 서비스 클래스를 구현하여 분리

### 4. 충전소 API 연동

**문제**: 공공데이터 API의 응답 형식이 예상과 달라 파싱 오류 발생
**해결**: DTO 클래스를 API 응답 구조에 맞게 재설계하고 예외 처리 로직 추가

### 5. 예약 중복 방지

**문제**: 동일한 차량에 대해 동시에 여러 예약이 생성되는 문제
**해결**: 데이터베이스 트랜잭션 처리 및 예약 가능 여부 검증 로직 추가

## 얻은 것

### 기술적 성장

- Spring Boot와 Spring Security를 활용한 RESTful API 설계 및 구현 경험
- JWT를 이용한 토큰 기반 인증 시스템 구현
- MyBatis를 활용한 복잡한 쿼리 작성 및 최적화
- 소셜 로그인 API 연동 경험 (네이버, 카카오)
- 공공데이터 API 연동 및 데이터 파싱 경험
- 파일 업로드 및 관리 시스템 구현

### 협업 경험

- 팀 프로젝트를 통한 Git 협업 및 브랜치 전략 활용
- 코드 리뷰를 통한 코드 품질 향상
- API 명세서 작성 및 프론트엔드와의 협업 경험

### 문제 해결 능력

- 다양한 예외 상황에 대한 예외 처리 클래스 설계
- 디버깅 및 로깅을 통한 문제 해결 경험
- 성능 최적화를 위한 쿼리 개선

## 개선사항

### 단기 개선사항

- [ ] 예약 알림 기능 추가 (이메일, SMS)
- [ ] 결제 시스템 연동
- [ ] 실시간 차량 위치 추적 기능
- [ ] 리뷰 평점 시스템 개선
- [ ] 게시판 이미지 최적화 (썸네일 생성)

### 장기 개선사항

- [ ] 모바일 앱 개발 (React Native 또는 Flutter)
- [ ] AI 기반 추천 시스템 (차량 추천, 충전소 추천)
- [ ] 실시간 채팅 기능
- [ ] 관리자 대시보드 고도화 (차트, 그래프)
- [ ] 마이크로서비스 아키텍처 전환 검토
- [ ] Redis를 활용한 캐싱 시스템 도입
- [ ] Elasticsearch를 활용한 검색 기능 개선

## 팀원 정보

<!-- 아래 정보를 실제 팀원 정보로 수정해주세요 -->

- **팀원 1**: [김하늘] - [전기차 예약 시스템]
- **팀원 2**: [배주영] - [충전소 검색 및 리뷰]
- **팀원 3**: [강병준] - [회원 관리 및 인증]
- **팀원 4**: [유성현] - [게시판 및 커뮤니티]
- **팀원 5**: [강현성] - [관리자 기능]

## 시연 화면

<!-- 아래에 시연 화면 GIF를 추가해주세요 -->

![시연 화면 1](시연화면1.gif)
![시연 화면 2](시연화면2.gif)
![시연 화면 3](시연화면3.gif)

## 시연 영상

<!-- 아래에 유튜브 링크를 추가해주세요 -->

[시연 영상 보기](https://www.youtube.com/watch?v=YOUR_VIDEO_ID)

---

## 설치 및 실행 방법

### 필수 요구사항

- Java 21 이상
- Oracle Database
- Gradle 7.x 이상

### 실행 방법

1. 저장소 클론

```bash
git clone [repository-url]
cd spring/back
```

2. `application.yml` 설정

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@[DB_HOST]:[PORT]:[SID]
    username: [USERNAME]
    password: [PASSWORD]
```

서버는 `http://localhost:8081`에서 실행됩니다.
