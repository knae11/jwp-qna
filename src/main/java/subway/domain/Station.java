package subway.domain;


import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "station")
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull // 필드도!
    @Column(nullable = false) // DB 스키마상에서만!
    private String name;

    @ManyToOne
    @JoinColumn(name = "line_id") // 외래키의 이름을 지정(생략가능) -> if 생략: 엔티티이름+id
    private Line line; // 연관관계 엔티티끼리는 설정해라!! (다른 엔티티를 참조할때는 무조건 연관관계를 설정해 주어야 한다.)

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station(String name) {
        this.name = name;
    }

    public Station() {
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        if (Objects.nonNull(line)) {
            line.getStations().remove(this);
        }
        // 이미 연관관계 매핑되었다면 무한루프 빠지지 않기 위한 코드가 필요!
        this.line = line;
        //line.addStation(this); // 여기도 연관관계 편의 메소드 -> 무한루프!!! ㅠㅠ
        line.getStations().add(this);
    }
}
