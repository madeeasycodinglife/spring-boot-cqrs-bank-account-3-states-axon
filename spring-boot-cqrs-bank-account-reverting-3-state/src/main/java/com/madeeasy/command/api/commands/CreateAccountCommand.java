package com.madeeasy.command.api.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAccountCommand {

    @TargetAggregateIdentifier
    private String accountId;
    private BigDecimal initialBalance;
    private String state;
}

