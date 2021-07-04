package qna.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import qna.CannotDeleteException;
import qna.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QnaServiceTest {
    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private DeleteHistoryService deleteHistoryService;

    @InjectMocks
    private QnaService qnaService;

    private Question question;
    private Answer answer;
    private User JAVAJIGI;
    private User SANJIGI;

    @BeforeEach
    public void setUp() throws Exception {
        JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
        question = new Question(1L, "title1", "contents1").writeBy(JAVAJIGI);
        answer = new Answer(1L, JAVAJIGI, question, "Answers Contents1");
        question.addAnswer(answer);
    }

    @Test
    public void delete_성공() throws Exception {
        when(questionRepository.findById(question.getId()))
                .thenReturn(Optional.of(question));
        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_다른_사람이_쓴_글() throws Exception {
        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(SANJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_성공_질문자_답변자_같음() throws Exception {
        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));

        qnaService.deleteQuestion(JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_답변_중_다른_사람이_쓴_글() throws Exception {
        Answer answer2 = new Answer(2L, SANJIGI, question, "Answers Contents1");
        question.addAnswer(answer2);

        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(JAVAJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter()),
                new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter())
        );
        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
