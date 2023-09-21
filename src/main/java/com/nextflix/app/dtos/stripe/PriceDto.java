package com.nextflix.app.dtos.stripe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceDto {
    private Long price;
    private String priceId;
}
