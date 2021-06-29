package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StationRepositoryTest {

    @Autowired
    private StationRepository stations;

    @Test
    void save() {
        Station station = new Station("잠실역");

        Station actual = stations.save(station);

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo("잠실역");
    }

    @Test
    void findByName() {
        stations.save(new Station("잠실역"));

        Station actual = stations.findByName("잠실역");

        assertThat(actual.getId()).isNotNull();
    }


    @Test
    void findByName_equals() {
        Station station1 = stations.save(new Station("잠실역"));

        Station station2 = stations.findByName("잠실역");

        assertThat(station1).isSameAs(station2);
    }

    @Test
    void update() {
        Station station = stations.save(new Station("잠실역"));
        station.changeName("잠실나루역");

        Station actual = stations.findByName("잠실나루역");
        assertThat(actual).isNotNull();
    }


}