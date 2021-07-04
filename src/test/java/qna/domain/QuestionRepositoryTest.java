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
class QuestionRepositoryTest {
    @Autowired
    private AnswerRepository answers;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("question을 삭제해도 answer도 삭제")
    @Test
    void delete() throws CannotDeleteException {
        User user = new User("javajigi", "pass", "KimJava", "email@email.com");
        Question question = new Question("제목", "abc 질문", user);
        User savedUser = users.save(user);
        Question savedQuestion = questions.save(question);
        Answer savedAnswer = answers.save(new Answer(savedUser, savedQuestion, "abc"));
        savedQuestion.addAnswer(savedAnswer);

        Question foundQ = questions.findById(savedQuestion.getId()).get();
        foundQ.delete(savedUser);
        testEntityManager.flush();

        Answer actual = answers.findById(savedAnswer.getId()).get();
        assertThat(actual.isDeleted()).isTrue();
    }
}