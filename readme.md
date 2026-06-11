# 🏦 Java 기반 은행 입출금 시스템 (Bank System)

> **Java 객체지향 프로그래밍 실습 프로젝트**
> 사용자가 계좌를 생성하고 로그인하여 입금, 출금, 송금 등 다양한 금융 서비스를 이용할 수 있는 Swing GUI 기반 애플리케이션입니다.

---

## 📌 프로젝트 개요
- **팀명**: 5조
- **개발 기간**: 2026.05.11 ~ 2026.06.15
- **개발 언어**: Java
- **운영 방식**: Swing GUI Application

---

## 🛠 개발 환경 및 기술 스택

### Environment
- **IDE**: Visual Studio Code, Eclipse
- **Version Control**: GitHub
- **Collaboration**: 생성형 AI

### Tech Stack
- **Core**: Java (OOP, Method, ArrayList)
- **UI**: Java Swing (JFrame, CardLayout, JDialog)
- **Logic**: Control Statements (Loop, Condition)
- **Stability**: Exception Handling (Try-Catch)
- **Storage**: In-memory Data Structure (ArrayList)

---

## 📋 주요 기능
1. **회원 관리**: 회원가입 및 로그인 기능
2. **계좌 관리**: 신규 계좌 생성, 조회, 삭제
3. **금융 서비스**: 입금, 출금, 송금 기능
4. **부가 서비스**: 거래 내역 조회, 이자 계산 기능
5. **안정성**: 데이터 유효성 검사 및 예외 처리

---

## 🏗 시스템 구조 (System Architecture)

### 📂 패키지 및 클래스 구조
```
src/
├── main/
│   ├── Main.java                  # 프로그램 실행 진입점
├── account/
│   ├── User.java                  # 사용자 정보
│   ├── UserManager.java           # 사용자 관리 (생성, 수정, 로그인)
│   ├── Account.java               # 계좌 정보 및 잔액 관리
│   ├── AccountManager.java        # 계좌 생성, 삭제, 조회
│   └── Bank.java                  # 은행 enum (이자율 포함)
├── transaction/
│   ├── Transaction.java           # 거래 내역 기록
│   ├── TransactionManager.java    # 입금, 출금, 송금 처리
│   └── InterestCalculator.java    # 이자 계산 로직
└── ui/
    ├── AuthSwingUI.java           # 로그인/회원가입 UI
    └── MainSwingUI.java           # 메인 화면 UI (사이드바 + CardLayout)
```

### 🗺 화면 구조
```
로그인 / 회원가입 (AuthSwingUI)
└── 메인 화면 (MainSwingUI)
    ├── 계좌 관리
    │   ├── 계좌 목록 조회
    │   ├── 계좌 생성 (팝업)
    │   └── 계좌 상세
    │       ├── 거래 내역
    │       └── 계좌 삭제
    ├── 거래
    │   ├── 입금
    │   ├── 출금
    │   ├── 송금
    │   └── 이자 적용
    └── 내 정보
        ├── 정보 조회
        ├── 이름 변경 (팝업)
        ├── 전화번호 변경 (팝업)
        └── 비밀번호 변경 (팝업)
```

---

## 📅 개발 일정
| 주차 | 기간 | 주요 목표 |
|:---:|:---:|---|
| **1주차** | 05/11 ~ 05/17 | 프로젝트 설계, GitHub 환경 구축, 클래스 구조 설계 |
| **2주차** | 05/18 ~ 05/24 | 회원가입, 로그인, 입출금 등 핵심 기능 구현 |
| **3주차** | 05/25 ~ 05/31 | 송금, 거래 내역, 이자 계산 및 데이터 관리 기능 구현 |
| **4주차** | 06/01 ~ 06/07 | 예외 처리 도입, 전체 기능 테스트 및 UI 정돈 |
| **5주차** | 06/08 ~ 06/15 | Swing GUI 전환, 최종 점검 및 프로젝트 제출 |

---

## 👥 역할 분담
| 담당 기능 | 내용 |
|:---:|---|
| **구조 설계 / UI** | 전체 클래스 구조 설계, Swing GUI, 계좌 관리 |
| **메인 메뉴** | BankSystem 메뉴 흐름 제어 |
| **입금** | 입금 기능 구현 |
| **출금** | 출금 기능 구현 |
| **송금** | 송금 기능 구현 |
| **이자율 계산** | 이자율 계산 기능 구현 |

---

## 🌿 Git 브랜치 전략
| 브랜치 | 설명 |
|:---:|---|
| `main` | 최종 제출 및 배포용 버전 |
| `dev` | 개발 통합용 메인 브랜치 |
| `feature-ui` | Swing GUI 개발 |
| `feature-bank` | 계좌 및 입출금 기능 개발 |
| `feature-transfer` | 송금 및 이자 계산 기능 개발 |

---

## ✏️ Git 커밋 컨벤션
원활한 협업을 위해 아래 규칙에 맞춰 커밋 메시지를 작성합니다.

### 커밋 메시지 형식
예)
```
[Add] Account Manager
계정 관리 기능 추가
- 계정 생성, 수정 및 제거
- 로그인, 로그아웃
```

### Type 종류
| Type | Description |
|:---:|---|
| **Add** | 새로운 기능 추가 |
| **Fix** | 버그 수정 |
| **Docs** | 문서 수정 (README, 주석 등) |
| **Refactor** | 코드 리팩토링 (기능 변경 없는 구조 변경) |
| **Test** | 테스트 코드 추가 및 수정 |
| **Chore** | 빌드 설정 등 기타 단순 작업 |

---

© 2026 5조 Bank System Project. All rights reserved.