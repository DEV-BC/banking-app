package com.devbc.banking.impl;

import com.devbc.banking.dto.AccountDto;
import com.devbc.banking.dto.TransferFundDto;
import com.devbc.banking.entity.Account;
import com.devbc.banking.exception.AccountException;
import com.devbc.banking.mapper.AccountMapper;
import com.devbc.banking.repository.AccountRepository;
import com.devbc.banking.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new AccountException("Account does not exist"));

        return AccountMapper.mapToAccountDto(account);

    }

    @Override
    public AccountDto deposit(Long id, double amount) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new AccountException("Account does not exist"));

        double total = account.getBalance() + amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new AccountException("Account does not exist"));

        if(account.getBalance() < amount) {
            throw new AccountException("Insufficient amount");
        }

        double total = account.getBalance() - amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map((account) -> AccountMapper.mapToAccountDto(account)).collect(Collectors.toList());

    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new AccountException("Account does not exist"));
        accountRepository.deleteById(id);
    }

    @Override
    public void transferFunds(TransferFundDto transferFundDto) {
        //Get Account from which we send the amount
       Account fromAccount = accountRepository.findById(transferFundDto.fromAccountId()).orElseThrow(() -> new AccountException("Account does not exist"));

       //Get account to which we send the amount
        Account toAccount = accountRepository.findById(transferFundDto.fromAccountId()).orElseThrow(() -> new AccountException("Account does not exist"));

        //debit the amount from fromAccount
        fromAccount.setBalance(fromAccount.getBalance() - transferFundDto.amount());

        //credit the amount to toAccount
        toAccount.setBalance(toAccount.getBalance() + transferFundDto.amount());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }
}
