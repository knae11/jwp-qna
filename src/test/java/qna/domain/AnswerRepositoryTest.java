package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("qna")
@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("생성")
    @Test
    void save() {
        User user = new User("javajigi", "pass", "KimJava", "email@email.com");
        Question question = new Question("제목", "abc 질문", user);
        User savedUser = users.save(user);
        Question savedQuestion = questions.save(question);
        Answer actual = answers.save(new Answer(savedUser, savedQuestion, "abc"));

        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("생성")
    @Test
    void findById() {
        User user = new User("javajigi", "pass", "KimJava", "email@email.com");
        Question question = new Question("제목", "abc 질문", user);
        User savedUser = users.save(user);
        Question savedQuestion = questions.save(question);
        Answer answer = answers.save(new Answer(savedUser, savedQuestion, "abc"));

        testEntityManager.flush();

        Answer actual = answers.findById(answer.getId()).get();

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getQuestion()).isNotNull();
        assertThat(actual.getWriter()).isNotNull();
    }

    @DisplayName("삭제")
    @Test
    void delete() throws CannotDeleteException {
        User user = new User("javajigi", "pass", "KimJava", "email@email.com");
        Question question = new Question("제목", "abc 질문", user);
        User savedUser = users.save(user);
        Question savedQuestion = questions.save(question);
        Answer answer = answers.save(new Answer(savedUser, savedQuestion, "abc"));

        answer.delete(user);
        Answer actual = answers.findById(answer.getId()).get();
        testEntityManager.flush();

        assertThat(actual.isDeleted()).isTrue();
    }
}