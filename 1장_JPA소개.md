## 1.1 SQL을 직접 다룰 때 발생하는 문제점

###  반복적인 CRUD 쿼리가 문제다.
반복한다는것이 어떤걸 반복한다는 것일까?
```java
public class Member{
	private String memberId;
    private String name;
    ...
}
```
1. 회원객체를 만들었으니 일단 조회를 위한 DAO 객체를 만든다.
```java
public class MemberDAO{
	public Member find(String memberId){...}
}
```
2. 이제 쿼리를 만들어야겠죠?
```sql
SELECT MEMBER_ID, NAME FROM MEMBER M WHERE MEMBER_ID=?
```
3. JDBC API 를 사용해서 SQL을 실행해야한다..
```java
ResultSet rs = stmt.executeQuery(sql);
```
4. 조회 결과를 Member 객체로 매핑한다..
```java
String memberId = rs.getString("MEMBER_ID");
String name = rs.getString("NAME");

Member member = new Member();
member.setMemberId(memberId);
member.setName(name);

```

조회만들었으니까 등록기능도 만들어야겠죠...? 반복의 시작이다. (수정, 삭제 도 추가해야한다..)

반면 자바 콜렉션에 보관할 때는?
```java
list.add(member);
```
아주 간단하다..
이를 데이터베이스에 적용할 때 힘든 이유는 개발자가 객체지향 애플리케이션과 데이터베이스 중간에서 SQL과 JDBC API를 사용해서 변환 작업을 직접 해주어야하기 때문이다.



###  SQL에 의존적인 개발을 한다.
기획자가 갑자기 회원에 연락처도 같이 저장해 달라고 한다.

1. 등록코드 먼저 변경하자.
일단 객체부터 수정하자.
```java
public class Member{
	private String memberId;
    private String name;
    private String tel; //추가
    ...
}
```
그 다음 쿼리를 수정하자.
```sql 
String sql = "INSERT INTO MEMBER(MEMBER_ID, NAME, TEL) VALUES (?, ?, ?)";
```
그 다음 JDBC API 를 이용해서 등록 SQL에 연락처 값을 전달한다.
```java
pstmt.setString(3, member.getTel());
```
2. 이제 조회/수정 코드도 변경해야한다.(반복의 시작...!)

---
회원은 어떤 한팀에 필수로 소속되어야한다는 요구사항이 생겼을 때?
```java
public class Member{
	private String memberId;
    private String name;
    private String tel; //추가
    private Team team;
    ...
}
class Team{
	private String teamName;
    ...
}
```
팀 이름을 출력해보겠다.
```java
소속팀 : member.getTeam().getTeamName(); 
```
결과는 null이 출력된다. Dao 코드를 열어서 확인해보니까 쿼리 수정이 안되어있다.

---
결론은 DAO를 열어서 어떤 SQL 이 실행되고 어떤 객체들이 함께 조회되는지 일일이 확인해야한다.

SQL에 지나치게 의존적인 개발을 해야한다.


### 간단한 JPA 소개!
만약 JPA를 사용하였을 때 위 문제들은 어떻게 해결이 될까?
JPA 를 사용하면 개발자가 직접 SQL을 작성하는 것이 아니라, JPA를 제공하는 API를 사용한다.

- 저장기능
```java
jpa.persist(member); 
```
- 조회기능
```java
String memberId = "abcd123";
Member member = jpa.find(Member.class, memberId); 
```
- 수정기능
```java
Member member = jpa.find(Member.class, memberId); 
member.setName("이름변경");
```
JPA는 수정메소드가 따로 없고 객체의 값을 변경하면 Transaction 커밋할 때 적절한 SQL이 전달된다.

- 연관된 객체 조회
```java
Member member = jpa.find(Member.class, memberId); 
Team team = member.getTeam();
```
얼마나 간단한가....🥹

## 1.2 패러다임의 불일치

**패러다임 불일치란?**
객체와 관계형 데이터베이스는 지향하는 목적이 서로 다르므로 둘의 기능과 표현 방법도 다르다. 이것을 패러다임 불일치라 한다.
따라서 객체구조를 테이블 구조에 저장하는 데는 한계가 있다.

### 상속 패러다임 불일치

객체에는 상속이라는 기능을 가지고 있지만 테이블은 상속이라는 기능이 없다.

**객체 상속 모델**

<img src="https://velog.velcdn.com/images/baeyuyeon/post/d658791c-945e-4579-82c0-60ab0f2ccaa5/image.png" width="500px">

이를 데이터베이스로 표현하면?

**데이터베이스 ERD**
<img src="https://velog.velcdn.com/images/baeyuyeon/post/08b7be74-47c4-4524-bf5b-a52167195747/image.png" width="500px" height="200px">

위 그림처럼 ITEM테이블의 DTYPE이란 컬럼을 추가해서 어떤 자식 테이블과 관계가 있는지 정의한다.

여기서 Album 객체의 데이터를 저장하려고하면?
```sql
INSERT INTO ITEM...
INSERT INTO ALBUM...
```
이렇게 부모테이블과 자식테이블 모두 insert query 를 작성해야한다. 그리고 자식타입에 따라서 DTYPE을 저장해야한다.
조회할 때도 조인을 통해 가져와야한다.

이런과정이 모두 패러다임의 불일치를 해결하려고 할때 소모하는 비용이다.


**JPA와 상속**

JPA를 사용한다면 패러다임의 불일치문제를 개발자 대신 해결해준다.

여기서 Album 객체의 데이터를 저장하려고하면?
```java
jpa.persist(album); //알아서 item, album 두 테이블에 나누어 저장한다.
```
데이터를 가져오려고 하면?
```java
String albumId ="id100";
Album album = jpa.find(Album.class, albumId);
```
자동으로 Item과 Album 두 테이블을 조인해서 필요한 데이터를 조회한다.

### 연관관계
객체는 참조를 사용해서 다른 객체와 연관관계를 가지고 참조에 접근해서 연관된 객체를 조회한다.
테이블은 외래키를 사용해서 연관 테이블을 조회한다.
즉, 개발자가 중간에서 변환역할을 해야한다.

**JPA를 사용한다면? **

**저장할 때**
```java
member.setTeam(team); // 회원과 팀 연관관계 설정
jpa.persist(member); // 회원과 연관관계 함께 저장

```
JPA는 team 의 참조를 외래키로 변환해서 적절한 insert sql을 데이터베이스에 전달한다.

**조회 할 때**
```java
Member member = jpa.find(Member.class, memberId);
Team team = member.getTeam();
```
객체를 조회할 때 외래키를 참조로 변환하는 일도 JPA가 알아서 처리해준다.

### 객체 그래프 탐색
만약 아래 쿼리를 사용하는 MemberDao가 있다고 하자.
```sql
SELECT M.*, T.*
FROM MEMBER M
JOIN TEAM T ON M.TEAM_ID = T.TEAM_ID
```
MemberDao 에서 member객체를 조회할 때 member.getTeam()은 데이터를 가져오지만, member.getOrder()는 null을 반환한다.
_즉 SQL 을 직접 다루면 처음 실행하는 SQL에 따라 객체그래프를 어디까지 탐색할 수 있는지 정해진다._

**JPA와 객체 그래프 탐색**
JPA는 연관된 객체를 사용하는 시점에 적절한 SELECT SQL을 실행한다.
이를 지연로딩이라한다. JPA는 지연로딩을 투명하게 처리한다.
```java
//처음 조회 시점에 select member sql 실행함.
Member member = jpa.find(Member.class, memberId);
Order order = member.getORder();
order.getOrderDate(); //Order를 사용하는 시점에 SELECT ORDER SQL
```
위 코드 처럼 order의 date 컬럼을 꺼낼때 order select  문을 실행한다.

### 비교
객체는 동일성과 동등성 비교라는 것이 있다.
> 동일성 비교는 ==, 객체 인스턴스의 주소 값을 비교한다.
동등성 비교는 equlas() 메소드를 사용해서 객체 내부의 값을 비교한다.

데이터베이스는 기본 키의 값으로 각 로우를 구분한다.

```java
String memberId = "100";
Member member1 = memberDAO.getMember(memberId);
Member member2 = memberDAO.getMember(memberId);

if(member1 == member2;) return ;//false
```
위 member1과 member2는 다르다.  같은 데이터베이스 로우에서 조회했지만, 객체 측면에서 볼 때 둘은 다른 인스턴스기 때문이다.

***JPA와 비교***
JPA는 같은 트랜잭션일 때 같은 객체가 조회되는 것을 보장한다.
```java
String memberId = "100";
Member member1 = jpa.find(Member.class, memberId);
Member member2 = jpa.find(Member.class, memberId);

if(member1 == member2;) return ;//true
```

## 1.3 JPA란 무엇인가?

JPA (Java Persistence API)는 자바 진영의 ORM 기술 표준이다. JPA는 애플리케이션과 JDBC사이에서 동작한다.

그렇다면 ORM이란?
> ORM(Object-Relational Mapping)은 이름 그대로 객체와 관계형 데이터베이스를 매핑한다는 뜻이다.

ORM 프레임워크를 사용했을 때 객체를 db에 저장하는 내부 모습이다.
<img src="https://velog.velcdn.com/images/baeyuyeon/post/5c268c86-9e2e-4780-bfd2-3bc0fe6e8a4c/image.png">

위 처럼 insert sql 을 직접 생성하는것이 아니라 객체를 마치 자바 컬렉션에 저장하듯이 ORM 프레임워크에 저장하면 된다.
이를 통해 개발자는 데이터 중심인 데이터베이스를 사용해도 객체지향 애플리케이션 개발에 집중할 수 있다.
주로 하이버네이트 프레임워크를 많이 사용한다.
즉 JPA는 자바 ORM 기술에 대한 API 표준명세고, 하이버네이트는 JPA를 구현한 ORM 프레임워크이다.

### 왜 JPA를 사용해야 하는가?
#### 생산성
지루한 CRUD쿼리를 작성하지 않아도 되고, DDL쿼리를 자동으로 생성해 주는 기능도 있다.
#### 유지보수
필드를 추가하거나 삭제해도 수정해야할 코드가 줄어든다.
#### 패러다임의 불일치 해결
JPA는 상속, 연관관계, 객체 그래프 탐색, 비교하기와 같은 패러다임 불일치 문제를 해결해준다.
#### 성능 
```java
String memberId ="helloId";
Member member1 = jpa.find(memberId);
Member member2 = jpa.find(memberId);
```
일반 jdbc였다면 select  sql 을 수행할 때 데이터베이스와 두번 통신했을 것이다.
반면 JPA를 사용하면 회원을 조회하는 SELECT SQL 을 한 번만 데이터베이스에 전달하고 두번째는 조회한 회원 객체를 재사용한다.

#### 데이터 접근 추상화와 벤더 독립성
JPA는 애플리케이션과 데이터베이스 사이에 추상화된 데이터 접근 계층을 제공해서 애플리케이션이 특정 데이터베이스 기술에 종속되지 않도록 한다.


