package com.nextflix.app.dtos.subscription;

import com.nextflix.app.dtos.stripe.SubscriptionProductDto;
import com.nextflix.app.dtos.user.UserResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionUserInfoDto {
    private SubscriptionProductDto subscription;
    private UserResponseDto user;
    
}
