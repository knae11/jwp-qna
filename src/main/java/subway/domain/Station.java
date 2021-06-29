package subway.domain;


import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "station")
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull // 필드도!
    @Column(nullable = false) // DB 스키마상에서만!
    private String name;

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
}
