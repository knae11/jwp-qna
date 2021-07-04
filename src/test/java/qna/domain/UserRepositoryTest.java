package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("qna")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository users;

    @DisplayName("유저 저장")
    @Test
    void save() {
        User actual = users.save(new User("javajigi", "pass", "KimJava", "email@email.com"));

        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("수정시간 확인")
    @Test
    void createDate() {
        User user = new User("javajigi", "pass", "KimJava", "email@email.com");
        User savedUser = users.save(user);
        final LocalDateTime initialUpdatedAt = savedUser.getUpdatedAt();

        User actual = users.findById(savedUser.getId()).get();
        actual.changeName("LeeJava");
        users.flush();

        assertThat(initialUpdatedAt).isNotEqualTo(actual.getUpdatedAt());
    }

}