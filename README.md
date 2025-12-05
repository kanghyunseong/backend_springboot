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
│   │   └── notice/성] - [관리자 페이지] (예: 관리자 기능)

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

