package com.example.neoquiz.service;

import com.example.neoquiz.dto.response.QuestionsResponse;
import com.example.neoquiz.dto.response.QuizFullDescResponse;
import com.example.neoquiz.dto.response.QuizResponse;
import com.example.neoquiz.entity.Question;
import com.example.neoquiz.entity.Quiz;
import com.example.neoquiz.exception.NotFoundException;
import com.example.neoquiz.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final ImageUploadService uploadService;

    public List<QuizResponse> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAll();
        List<QuizResponse> quizResponses = new ArrayList<>();

        for (Quiz quiz : quizzes) {
            quizResponses.add(QuizResponse.builder()
                                          .name(quiz.getName())
                                          .imageUrl(quiz.getImageUrl())
                                          .colorId(quiz.getColorId())
                                          .id(quiz.getId())
                                          .build());
        }
        return quizResponses;
    }

    public QuizFullDescResponse getFullDescriptionById(Long id) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new NotFoundException("Quiz not found!"));

        return QuizFullDescResponse.builder()
                .name(quiz.getName())
                .description(quiz.getDescription())
                .genre(quiz.getGenre())
                .imageUrl(quiz.getImageUrl())
                .id(quiz.getId())
                .build();
    }

    public List<QuizResponse> getFourQuizzesByName() {
        List<Quiz> quizzes = quizRepository.findAllByName();
        List<QuizResponse> quizResponses = new ArrayList<>();

        for (Quiz quiz : quizzes) {
            quizResponses.add(QuizResponse.builder()
                    .name(quiz.getName())
                    .imageUrl(quiz.getImageUrl())
                    .build());
        }

        return quizResponses;
    }

    public List<QuestionsResponse> getQuestionsByName(String name) {
        Quiz quiz = quizRepository.findByName(name).orElseThrow(() -> new NotFoundException("Quiz not found!"));

        return quiz.getQuestions().stream().map(this::mapToQuestionResponse).toList();
    }

    public String uploadImage(MultipartFile multipartFile, String name) {
        Quiz quiz = quizRepository.findByName(name).orElseThrow(() -> new NotFoundException("Article not found!"));

        quiz.setImageUrl(uploadService.saveImage(multipartFile));
        quizRepository.save(quiz);
        return "Изображение успешно обновлено";
    }

    private QuestionsResponse mapToQuestionResponse(Question question) {
        return QuestionsResponse.builder()
                .name(question.getName())
                .AnswerTrue(question.getAnswerTrue())
                .firstAnswerFalse(question.getFirstAnswerFalse())
                .secondAnswerFalse(question.getSecondAnswerFalse())
                .thirdAnswerFalse(question.getThirdAnswerFalse())
                .build();
    }
}
