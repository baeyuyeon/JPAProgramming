## 1. 프로젝트 환경
#### JPA 프로그래밍 환경
IDE : IntelliJ
DB : Mysql


***pom.xml***
하이버네이트 5.3.1 버전과, mysql 5.1.49 버전을 사용하였다.
```xml
<dependencies>
    <dependency>
      <groupId>org.hibernate</groupId>
      <artifactId>hibernate-entitymanager</artifactId>
      <version>5.3.1.Final</version>
    </dependency>
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.1.49</version>
    </dependency>
    <dependency>
      <groupId>javax.xml.bind</groupId>
      <artifactId>jaxb-api</artifactId>
      <version>2.3.1</version>
    </dependency>
  </dependencies>
```


## 2. 객체 매핑 시작

회원클래스에 JPA가 제공하는 매핑 어노테이션을 추가해보자.
```java
@Entity
@Table(name = "MEMBER"))
public class Member {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String username;
    private Integer age; // 생략해도 매핑됨.
 	...  
}

```
- @Entity
이 클래스를 테이블과 매핑한다고 JPA에게 알려준다.
- @Table
	엔터티 클래스에 매핑할 테이블 정보를 알려준다. name 속성으로 테이블이름을 지정할 수 있다.(생략시 클래스이름을 테이블 이름으로 매핑한다.)
- @Id
필드를 기본 키에 매핑한다.
- @Column
필드를 컬럼에 매핑한다. name 속성을 생략하면 필드명을 사용해서 컬럼명으로 매핑한다.


## 3. persistence.xml 설정
JPA는 persistence.xml 을 사용해서 필요한 설정 정보를 관리한다.
META-INF/persistence.xml 위치에 두면 별도 설정없이 JPA가 인식할 수 있다.
![](https://velog.velcdn.com/images/baeyuyeon/post/83aadecf-b726-42f3-8672-c5b756d31c85/image.png)

***persistence.xml 코드 분석***
```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence" version="2.1">

  <persistence-unit name="jpabook"> 
    <properties>

      <!-- 필수 속성 -->
      <property name="javax.persistence.jdbc.driver" value="com.mysql.jdbc.Driver"/>
      <property name="javax.persistence.jdbc.user" value="user_id"/>
      <property name="javax.persistence.jdbc.password" value="password"/>
      <property name="javax.persistence.jdbc.url"
        value="jdbc:mysql://127.0.0.1:3307/jpabegin?characterEncoding=UTF-8"/>
      <property name="hibernate.dialect" value="org.hibernate.dialect.MySQL8Dialect"/>

      <!-- 옵션 -->
      <property name="hibernate.show_sql" value="true"/>
      <property name="hibernate.format_sql" value="true"/>
      <property name="hibernate.use_sql_comments" value="true"/>
      <property name="hibernate.id.new_generator_mappings" value="true"/><!-- 키생성전략 사용 -->
      <property name="hibernate.hbm2ddl.auto" value="create"/>
      <property name="hibernate.ejb.naming_strategy"
        value="org.hibernate.cfg.ImprovedNamingStrategy"/>

    </properties>

  </persistence-unit>


</persistence>
```

코드를 하나씩 분석해보자.
```
<persistence-unit name="jpabook"> 
```
JPA 설정은 영속성 유닛이라는 것부터 시작하는데 일반적으로 연결할 데이터베이스당 하나의 영속성 유닛을 등록한다.
```
<properties>
      <property name="javax.persistence.jdbc.driver" value="com.mysql.jdbc.Driver"/>
      ...
```
JDBC 드라이버 속성을 지정한다.
```
 <property name="javax.persistence.jdbc.user" value="user_id"/>
 <property name="javax.persistence.jdbc.password" value="password"/>
 <property name="javax.persistence.jdbc.url"
        value="jdbc:mysql://127.0.0.1:3307/jpabegin?characterEncoding=UTF-8"/>
```
데이터베이스 연결정보를 지정한다.

```
<property name="hibernate.dialect" value="org.hibernate.dialect.MySQL8Dialect"/>
```
데이터베이스 방언을 설정한다.

위 처럼 hibernate 로 시작하는 속성은 hibernate 전용이다.
사용된 하이버네이트 전용 속성이다.
- hibernate.show_sql : 하이버네이트가 실행한 SQL을 출력한다.
- hibernate.format_sql : 하이버네이트가 실행할 SQL을 출력할 때 보기 쉽게 정렬한다.
- hibernate.use_sql_comments : 쿼리를 출력할 때 주석도 함께 출력한다.
- hibernate.id.new_generator_mappings : JPA 표준에 맞춘 새로운 키 생성 전략을 사용한다.

### 데이터베이스 방언
SQL 표준을 지키지 않거나 특정 데이터베이스만의 고유한 기능을 JPA에서는 방언이라고 한다. 하이버네이트가 특정 DB에 종속되는 기능을 사용한 문제들을 해결하려고 다양한 데이터베이스 방언 클래스를 제공한다.
즉, 개발자는 JPA가 제공하는 표준 문법에 맞추어 JPA를 사용하면 되고, 특정 DB에 의존적인 SQL은 데이터베이스 방언이 처리해준다.
나는 MySQL8Dialect 를 사용하였다.

## 3.애플리케이션 개발
일단 코드를 보자.
```java
package jpabook.start;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        // [엔터티 매니저 팩토리 생성] - 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        // [엔터티 매니저] - 생성
        EntityManager em = emf.createEntityManager();
        // [트랜잭션] - 획득
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            logic(em);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
    ...
    
}

```
코드는 엔티티 매니저 설정, 트랜잭션 관리, 비즈니스 로직 총 3개의 부분으로 나뉘어져있다.
### 엔티티 매니저 설정
![](https://velog.velcdn.com/images/baeyuyeon/post/bffa5d26-77d8-4c45-9411-bd9a02d057c7/image.png)
***설정 가져오기***
JPA를 시작할 때 우선 persistence.xml 의 설정 정보를 사용해서 엔터티 매니저 팩토리를 생성한다.

***엔터티 매니저 팩토리 생성***
엔터티매너지 팩토리를 생성할 때 Persistence 클래스를 사용하는데, 이 클래스는 엔티티 매니저 팩토리를 생성해서 JPA를 사용할 수 있게 준비한다.
```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
```
위 코드 대로 하면 META-INF/persistence.xml 에서 "jpabook"이라는 영속성 유닛을 찾아서 엔터티 매니저 팩토리를 생성한다.
이때 JPA 를 동작시키기 위한 기반 객체를 만들고 JPA구현체에 따라서 데이터베이스 커넥션 풀도 생성하므로 엔티티 매니저 팩토리를 생성하는 비용은 아주 크다.
따라서 _엔티티 매니저 팩토리는 애플리케이션 전체에서 딱 한번만 생성하고 공유해서 사용해야한다._

***엔티티 매니저 생성***
```java
 EntityManager em = emf.createEntityManager();
```
엔티티 매니저 팩토리에서 엔티티 매니저를 생성한다. 엔티티 매니저는 내부에 데이터소스를 유지하면서 데이터베이스와 통신한다.
참고로 엔티티 매니저는 데이터베이스 커넥션과 밀접한 관계가 있으므로 스레드 간에 공유하거나 재사용하면 안 된다.

***종료***
```java
 em.close();

 emf.close();
```
반드시 엔티티 매니저와 엔티티 매니저 팩토리는 종료해야한다.

### 트랜잭션 관리
JPA는 항상 트랜잭션 안에서 데이터를 변경해야한다.
트랜잭션 API를 사용해서 비즈니스 로직이 정상 동작하면 트랜잭션을 커밋하고 예외가 발생하면 트랜잭션을 롤백한다.

### 비즈니스 로직
마찬가지로 코드를 먼저보자.
```java
private static void logic(EntityManager em) {
	String id = "id1";
    Member member = new Member();
    member.setId(id);
    member.setUsername("유저명");
    member.setAge(11);

    //등록
    em.persist(member);

    //수정
    member.setAge(20);

    //한 건 조회
    Member findMember = em.find(Member.class, id);
    System.out.println(
                "findMember = " + findMember.getUsername() + ", age = " + findMember.getAge());

    //목록 조회
    List<Member> members = em.createQuery("select m from Member10_2_7 m", Member.class).getResultList();
	System.out.println("member.size = " + members.size());

    //삭제
    em.remove(member);

}
```
#### 등록
```java
String id = "id1";
Member member = new Member();
member.setId(id);
member.setUsername("유저명");
member.setAge(11);

//등록
em.persist(member);

```
엔티티를 저장하려면 엔티티 매니저의 persist() 메소드에 저장할 엔티티를 넘겨주면 된다.

#### 수정
```java
member.setAge(20);
```
수정은 단순히 엔티티의 값만 변경했다.
#### 삭제
```java
em.remove(member);
```

#### 한건 조회
```java
Member findMember = em.find(Member.class, id);
```

#### JPQL 
```java
//목록 조회
List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
```
JPQL(Java Persistence Query Language)는 JPA에서 SQL을 추상화한 객체지향 쿼리 언이다.
거의 SQL문법과 유사하다.

차이점은 어떻게 될까?
> JPQL은 엔티티 객체를 대상으로 쿼리한다. (대소문자를 명확하게 구분) 
SQL은 데이터베이스 테이블을 대상으로 쿼리한다. ( 관례상 구분 안함.)


JPQL을 사용하려면 먼저 em.createQuery 메소드를 실행해서 쿼리 객체를 생성한 후 쿼리 객체의 getResultList() 메소드를 호출하면 된다.
