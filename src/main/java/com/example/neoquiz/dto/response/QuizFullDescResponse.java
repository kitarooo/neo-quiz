package com.example.neoquiz.dto.response;

import com.example.neoquiz.enums.Genre;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizFullDescResponse {
    String name;
    String description;
    Genre genre;
}
