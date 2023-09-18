package com.nextflix.app.dtos.rating;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateDto {
    private Long movieId;
    private Long userID;
    private int rating;
}
