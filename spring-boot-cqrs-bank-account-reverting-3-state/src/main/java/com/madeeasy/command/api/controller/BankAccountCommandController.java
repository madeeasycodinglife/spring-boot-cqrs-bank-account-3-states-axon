package com.madeeasy.command.api.controller;

import com.madeeasy.command.api.commands.*;
import com.madeeasy.command.api.model.CreateAccountRestModel;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/bank-accounts")
@SuppressWarnings("all")
@Slf4j
public class BankAccountCommandController {

    private final CommandGateway commandGateway;

    public BankAccountCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountRestModel model) {
        String accountId = UUID.randomUUID().toString();
        CreateAccountCommand command = CreateAccountCommand.builder()
                .accountId(accountId)
                .initialBalance(model.getInitialBalance())
                .build();
        return commandGateway.send(command);
    }

    @PostMapping("/{accountId}/freeze")
    public CompletableFuture<String> freezeAccount(@PathVariable String accountId) {
        log.info("Freezing account started for {}", accountId);
        FreezeAccountCommand command = new FreezeAccountCommand(accountId);
        log.info("Freezing account completed for {}", accountId);
        return commandGateway.send(command);
    }

    @PostMapping("/{accountId}/unfreeze")
    public CompletableFuture<String> unfreezeAccount(@PathVariable String accountId) {
        log.info("UnFreezing account started for {}", accountId);
        UnfreezeAccountCommand command = new UnfreezeAccountCommand(accountId);
        log.info("UnFreezing completed for {}", accountId);
        return commandGateway.send(command);
    }

    @PostMapping("/{accountId}/close")
    public CompletableFuture<String> closeAccount(@PathVariable String accountId) {
        return commandGateway.send(new CloseAccountCommand(accountId));
    }

    @PostMapping("/{accountId}/reopen")
    public CompletableFuture<String> reopenAccount(@PathVariable String accountId) {
        return commandGateway.send(new ReopenAccountCommand(accountId));
    }

}

