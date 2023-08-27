package com.nextflix.app.dtos.rating;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequestDto {
    private Long movieId;
    private int rating;
}
