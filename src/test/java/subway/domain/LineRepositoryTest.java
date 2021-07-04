package subway.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LineRepositoryTest {

    @Autowired
    private StationRepository stations;

    @Autowired
    private LineRepository lines;

    @Test
    void saveWithLine_error() {
        Station station = new Station("교대역");
        station.setLine(new Line("3호선"));
        //예외 이유: 연관관계 매핑을 하는 엔티티들은 영속화된 상태여야 한다!!!!!!
        stations.save(station);
    }

    @Test
    void saveWithLine_success() {
        Station station = new Station("교대역");
        station.setLine(lines.save(new Line("3호선")));

        stations.save(station);
    }

    @Test
    void findByNameWithLine() {
        Station actual = stations.findByName("교대역"); // select 쿼리가 2개 나감

        assertThat(actual).isNotNull();
        assertThat(actual.getLine().getName()).isEqualTo("3호선");
    }

    @Test
    void findById() {
        Line line = lines.findByName("3호선");
        assertThat(line.getStations()).hasSize(1);
        assertThat(line.getStations().get(0).getName()).isEqualTo("교대역");
    }

    @Test
    void getStations() {
        Line line = lines.findByName("3호선");
        line.getStations().add(stations.save(new Station("옥수역"))); // null!! 수정, 등이 불가하기 때문에
    }

    @Test
    void addStation() {
        Line line = lines.findByName("3호선");
        Station station = stations.save(new Station("옥수역"));
        line.addStation(station);

        assertThat(station.getLine()).isNotNull();
    }

    @AfterEach
    void tearDown() {
        stations.flush();
    }

}