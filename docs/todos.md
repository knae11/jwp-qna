# 제이슨 피드백

- [x] 도메인 테스트 작성하기
- [x] 사용하지 않는 getter, setter 삭제
- [x] `@MappedSuperClass` 사용하여 제거
- [x] 엔티티의 생성시간과 마지막 수정시간 업데이트 되는거 맞는지? -> 테스트 작성하여 확인
- [x] Question, Answer 양방향 관계인데 메소드는?
- [x] Question, Answer도 DeleteHistory 알아야 하나?
- [x] 매개변수 없는 생성자의 접근 제어자가 public일 필요가 있나?
- [x] Question, Answer의 deleted 필드는 소프트 삭제 플레그 의도 -> `questionRepository.delete(question)`은 호출되면 안됨

# 학습내용

- Auditing
    - Audit 은 감시하다, 감사하다라는 뜻으로 Spring Data JPA에서 시간에 대해서 자동으로 값을 넣어주는 기

```java

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {
}
```

```java

@EnableJpaAuditing
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

- `@MappedSuperClass`
    - 부모클래스는 테이블과 매핑하지 않고 부모 클래스를 상속받는 자식 클래스에게 매핑정보 제공
    - 해당 어노테이션이 붙은 것은 실제 테이블과 매핑되지 않음
    - 추상클래스와 비슷

# TestEntityManager

- Alternative to EntityManager for use in JPA tests. Provides a subset of EntityManager methods that are useful for
  tests as well as helper methods for common testing tasks such as persist/flush/find.

- 쿼리 로그를 보고 싶은데 flush 등 중간에 넣어 보고 싶다면 TestEntityManager 를 `@Autowired` 로 사용하면 된다.

# 객체간 연결!
- 각각의 객체를 만들고 객체간의 연결을 해주지 않으니 ((1)번을 해주지 않으니) 아래 코드가 동작하지 않았다.
- 단순 영속성 상태 뿐만 아니라 객체 서로간의 연관을 맺어주어야 JPA가 제대로 update 쿼리를 실행된다.
```java
        @DisplayName("question을 삭제해도 answer도 삭제")
        @Test
        void delete() throws CannotDeleteException {
            User user=new User("javajigi","pass","KimJava","email@email.com");
            Question question=new Question("제목","abc 질문",user);
            User savedUser=users.save(user);
            Question savedQuestion=questions.save(question);
            Answer savedAnswer=answers.save(new Answer(savedUser,savedQuestion,"abc"));
            savedQuestion.addAnswer(savedAnswer); // (1) 

            Question foundQ = questions.findById(savedQuestion.getId()).get();
            foundQ.delete(savedUser);
            testEntityManager.flush();

            Answer actual = answers.findById(savedAnswer.getId()).get();
            assertThat(actual.isDeleted()).isTrue();
        }
```

  