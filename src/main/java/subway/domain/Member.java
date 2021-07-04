package subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany // OneToMany -> 중간테이블이 생기는 단점이 있었음!
    @JoinColumn(name = "member_id") // joinColumn 으로 이름 직접 지정 -> favorite 테이블에 member_id를 추가해줌
    // favorite 이 관리자인데 연관관계 알고있니? 모름 -> 무조건 업대이트 쿼리가 나감
    private List<Favorite> favorites = new ArrayList<>();

    public Member(String name) {
        this.name = name;
    }

    public Member() {

    }

    public void addFavorite(Favorite favorite) {
        favorites.add(favorite);
    }
}
