# 제이슨 피드백
- [ ] 도메인 테스트 작성하기
- [x] 사용하지 않는 getter, setter 삭제
- [x] `@MappedSuperClass` 사용하여 제거
- [ ] 엔티티의 생성시간과 마지막 수정시간 업데이트 되는거 맞는지? -> 테스트 작성하여 확인
- [ ] Question, Answer 양방향 관계인데 메소드는?
- [ ] Question, Answer도 DeleteHistory 알아야 하나?
- [x] 매개변수 없는 생성자의 접근 제어자가 public일 필요가 있나?
- [ ] Question, Answer의 deleted 필드는 소프트 삭제 플레그 의도 
  -> `questionRepository.delete(question)`은 호출되면 안됨
  
# 학습내용
- Auditing
  - Audit 은 감시하다, 감사하다라는 뜻으로 Spring Data JPA에서 시간에 대해서 자동으로 값을 넣어주는 기
```java
    @EntityListeners(AuditingEntityListener.class)
    @MappedSuperclass
    public abstract class BaseEntity{
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
  