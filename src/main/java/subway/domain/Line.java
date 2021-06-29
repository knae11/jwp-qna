package subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // 부모가 없는 자식은 삭제됨
    @OneToMany(mappedBy = "line" , orphanRemoval = true) // fk는 Station의 어느필드가 가지고 있지?
    // 관리자가 아니기 때문에 읽기만 가능!
    private List<Station> stations = new ArrayList<>();

    protected Line() {
    }

    public Line(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public void addStation(Station station) {
        station.setLine(this);
        stations.add(station);
    }
}
