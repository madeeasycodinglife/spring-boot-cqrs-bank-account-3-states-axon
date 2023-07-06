package com.madeeasy.command.api.aggregate;

import com.madeeasy.command.api.commands.*;
import com.madeeasy.command.api.events.*;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

@Aggregate
@SuppressWarnings("all")
@Slf4j
public class BankAccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private BigDecimal initialBalance;
    private String state;

    public BankAccountAggregate() {
        // Default no-arg constructor required by Axon
    }

    @CommandHandler
    public BankAccountAggregate(CreateAccountCommand createAccountCommand) {
        AccountCreatedEvent accountCreatedEvent =
                AccountCreatedEvent.builder()
                        .accountId(createAccountCommand.getAccountId())
                        .initialBalance(createAccountCommand.getInitialBalance())
                        .state("ACTIVE")
                        .build();
        AggregateLifecycle.apply(accountCreatedEvent);
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.accountId = event.getAccountId();
        this.initialBalance = event.getInitialBalance();
        this.state = event.getState();
    }

    @CommandHandler
    public void handle(FreezeAccountCommand command) {
        log.info("Account frozen command is received");
        if (this.state.equals("ACTIVE")) {
            AggregateLifecycle.apply(new AccountFrozenEvent(accountId));
            log.info("Account frozen event is applied in AccountAggregateLifecycle");
        }
    }

    @EventSourcingHandler
    public void on(AccountFrozenEvent event) {
        this.state = "FROZEN";
        log.info("Account frozen status is applied in AccouneAggregate");
    }

    @CommandHandler
    public void handle(UnfreezeAccountCommand command) {
        if (this.state.equals("FROZEN")) {
            AggregateLifecycle.apply(new AccountUnfrozenEvent(accountId));
        }
    }

    @EventSourcingHandler
    public void on(AccountUnfrozenEvent event) {
        this.state = "ACTIVE";
    }

    @CommandHandler
    public void handle(CloseAccountCommand command) {
        if (this.state == "ACTIVE" || this.state == "FROZEN") {
            AggregateLifecycle.apply(new AccountClosedEvent(accountId));
        }
    }

    @EventSourcingHandler
    public void on(AccountClosedEvent event) {
        this.state = "CLOSED";
    }

    @CommandHandler
    public void handle(ReopenAccountCommand command) {
        if (this.state == "CLOSED") {
            AggregateLifecycle.apply(new AccountReopenedEvent(accountId));
        }
    }

    @EventSourcingHandler
    public void on(AccountReopenedEvent event) {
        this.state = "ACTIVE";
    }

}
