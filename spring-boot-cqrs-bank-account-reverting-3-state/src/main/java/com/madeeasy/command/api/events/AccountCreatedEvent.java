package com.madeeasy.command.api.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountCreatedEvent {
    private String accountId;
    private BigDecimal initialBalance;
    private String state;
}
