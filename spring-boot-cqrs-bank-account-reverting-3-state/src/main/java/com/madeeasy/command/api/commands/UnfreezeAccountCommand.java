package com.madeeasy.command.api.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnfreezeAccountCommand {
    @TargetAggregateIdentifier
    private String accountId;

}
