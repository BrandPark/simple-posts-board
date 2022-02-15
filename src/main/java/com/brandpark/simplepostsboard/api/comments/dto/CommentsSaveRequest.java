package com.brandpark.simplepostsboard.api.comments.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
public class CommentsSaveRequest {

    @Length(min = 1, max = 100)
    @NotBlank
    private String content;
}
