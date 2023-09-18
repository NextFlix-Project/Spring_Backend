package com.nextflix.app.dtos.subscription;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextflix.app.dtos.stripe.SubscriptionProductDto;
import com.nextflix.app.dtos.user.UserResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionUserInfoDto {
    @JsonProperty("subscription")
    private SubscriptionProductDto subscription;
    @JsonProperty("user")
    private UserResponseDto user;
    
}
