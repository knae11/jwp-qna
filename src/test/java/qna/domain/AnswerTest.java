package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnswerTest {
    private User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    private User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    private Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
    private Question Q2 = new Question("title2", "contents2").writeBy(SANJIGI);

    private Answer a1;
    private Answer a2;

    @BeforeEach
    void setUp() {
        a1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");
        a2 = new Answer(SANJIGI, Q1, "Answers Contents2");
    }

    @DisplayName("작성자 확인")
    @Test
    void isOwner() {
        assertTrue(a1.isOwner(JAVAJIGI));
        assertFalse(a1.isOwner(SANJIGI));
    }

    @DisplayName("문제 설정")
    @Test
    void toQuestion() {
        a1.toQuestion(Q2);

        assertThat(a1.getQuestion()).isEqualTo(Q2);
        assertTrue(a1.getQuestion().containsAnswer(a1));
    }

    @DisplayName("작성자가 삭제")
    @Test
    void delete() throws CannotDeleteException {
        a1.delete(JAVAJIGI);

        assertTrue(a1.isDeleted());
    }

    @DisplayName("작성자가 아닐 때, 삭제")
    @Test
    void deleteFail() {
        assertThatThrownBy(() -> a1.delete(SANJIGI)).isInstanceOf(CannotDeleteException.class);
    }
}
