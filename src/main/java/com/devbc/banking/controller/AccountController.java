package com.devbc.banking.controller;

import com.devbc.banking.dto.AccountDto;
import com.devbc.banking.dto.TransferFundDto;
import com.devbc.banking.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Add Account REST API
    @PostMapping
    public ResponseEntity<AccountDto> addAccount(@RequestBody AccountDto accountDto) {
        return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);
    }

    //Get account
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) {
        AccountDto accountDto = accountService.getAccountById(id);
        return ResponseEntity.ok(accountDto);
    }

    //Deposit
    @PutMapping("/{id}/deposit")
    public ResponseEntity<AccountDto> depositAmount(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        Double amount = request.get("amount");
        AccountDto accountDto = accountService.deposit(id, amount );
        return ResponseEntity.ok(accountDto);
    }

    //Withdraw
    @PutMapping("/{id}/withdraw")
    public ResponseEntity<AccountDto> withdrawAmount(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        Double amount = request.get("amount");
        AccountDto accountDto = accountService.withdraw(id, amount );
        return ResponseEntity.ok(accountDto);
    }

    //Get all accounts
    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<AccountDto> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    //Delete Account
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok("Account deleted successfully");
    }

    //Transfer API
    @PostMapping("/transfer")
    public ResponseEntity<String> transferFund(@RequestBody TransferFundDto transferFundDto) {
        accountService.transferFunds(transferFundDto);

        return ResponseEntity.ok("Transfer Successfully");
    }
}
