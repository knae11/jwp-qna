package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionTest {
    private User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    private User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    private Answer A2;
    private Answer A1;

    private Question q1;
    private Question q2;

    @BeforeEach
    void setUp() {
        q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
        q2 = new Question("title2", "contents2").writeBy(SANJIGI);
        A2 = new Answer(SANJIGI, q1, "Answers Contents2");
        A1 = new Answer(JAVAJIGI, q1, "Answers Contents1");
    }

    @DisplayName("답변추가")
    @Test
    void addAnswer() {
        q1.addAnswer(A1);

        assertTrue(q1.containsAnswer(A1));
        assertThat(A1.getQuestion()).isEqualTo(q1);
    }

    @DisplayName("작성자가 삭제")
    @Test
    void delete() throws CannotDeleteException {
        q1.delete(JAVAJIGI);

        assertTrue(q1.isDeleted());
    }

    @DisplayName("작성자가 아닌 경우, 삭제")
    @Test
    void deleteFail() {
        assertThatThrownBy(() -> q1.delete(SANJIGI)).isInstanceOf(CannotDeleteException.class);
    }
}
