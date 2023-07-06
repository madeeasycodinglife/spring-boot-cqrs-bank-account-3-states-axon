package com.madeeasy.command.api.events.handler;

import com.madeeasy.command.api.data.BankAccount;
import com.madeeasy.command.api.data.BankAccountRepository;
import com.madeeasy.command.api.events.*;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class BankEventsHandler {
    private final BankAccountRepository bankAccountRepository;

    public BankEventsHandler(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }


    @EventHandler
    public void on(AccountCreatedEvent event) {
        BankAccount bankAccount = new BankAccount();
        BeanUtils.copyProperties(event, bankAccount);
        bankAccountRepository.save(bankAccount);
    }

    @EventHandler
    public void on(AccountFrozenEvent event) {
        log.info("AccountFrozenEvent for {} frozen has started", event.getAccountId());
        Optional<BankAccount> optionalAccount = bankAccountRepository.findById(event.getAccountId());
        if (optionalAccount.isPresent()) {
            BankAccount bankAccount = optionalAccount.get();
            bankAccount.setState("FROZEN");
            bankAccountRepository.save(bankAccount);
            log.info("Account {} frozen completed", event.getAccountId());
            log.info(String.valueOf(bankAccountRepository.findById(event.getAccountId())));
        }
    }

    @EventHandler
    public void on(AccountUnfrozenEvent event) {
        Optional<BankAccount> optionalAccount = bankAccountRepository.findById(event.getAccountId());
        if (optionalAccount.isPresent()) {
            BankAccount bankAccount = optionalAccount.get();
            bankAccount.setState("ACTIVE");
            bankAccountRepository.save(bankAccount);
        }
    }

    @EventHandler
    public void on(AccountClosedEvent event) {
        Optional<BankAccount> optionalAccount = bankAccountRepository.findById(event.getAccountId());
        if (optionalAccount.isPresent()) {
            BankAccount bankAccount = optionalAccount.get();
            bankAccount.setState("CLOSED");
            bankAccountRepository.save(bankAccount);
        }
    }
    @EventHandler
    public void on(AccountReopenedEvent event) {
        Optional<BankAccount> optionalAccount = bankAccountRepository.findById(event.getAccountId());
        if (optionalAccount.isPresent()) {
            BankAccount bankAccount = optionalAccount.get();
            bankAccount.setState("ACTIVE");
            bankAccountRepository.save(bankAccount);
        }
    }
}
