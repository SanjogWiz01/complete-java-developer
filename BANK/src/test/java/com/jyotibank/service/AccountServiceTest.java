package com.jyotibank.service;

import com.jyotibank.dao.AccountDao;
import com.jyotibank.dao.TransactionDao;
import com.jyotibank.exception.InsufficientBalanceException;
import com.jyotibank.exception.InvalidAmountException;
import com.jyotibank.model.Account;
import com.jyotibank.model.SavingsAccount;
import com.jyotibank.model.enums.AccountStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock private AccountDao accountDao;
    @Mock private TransactionDao transactionDao;
    @Mock private AuditService auditService;

    private AccountService service;

    private Account savingsWithBalance(String balance) {
        SavingsAccount account = new SavingsAccount();
        account.setAccountId(42L);
        account.setCustomerId(7L);
        account.setAccountNumber("SAV-TEST-0001");
        account.setBalance(new BigDecimal(balance));
        account.setStatus(AccountStatus.ACTIVE);
        return account;
    }

    @BeforeEach
    void setUp() {
        service = new AccountService(accountDao, transactionDao, auditService);
    }

    @Test
    @DisplayName("deposit credits the balance and writes a ledger row")
    void depositUpdatesBalanceAndRecordsTransaction() {
        when(accountDao.findByAccountNumber("SAV-TEST-0001"))
                .thenReturn(Optional.of(savingsWithBalance("1000.00")));

        var tx = service.deposit(1L, "SAV-TEST-0001", new BigDecimal("250"), null);

        assertEquals(new BigDecimal("1250.00"), tx.getBalanceAfter());
        verify(accountDao).updateBalance(eq(42L), eq(new BigDecimal("1250.00")));
        verify(transactionDao).create(any());
        verify(auditService).record(eq(1L), eq("DEPOSIT"), eq("ACCOUNT"), eq(42L), any());
    }

    @Test
    @DisplayName("withdraw below minimum balance throws InsufficientBalanceException")
    void withdrawBelowMinimumThrows() {
        // savings minimum is 500.00 — withdrawing 600 of 1000 breaks the floor
        when(accountDao.findByAccountNumber("SAV-TEST-0001"))
                .thenReturn(Optional.of(savingsWithBalance("1000.00")));

        assertThrows(InsufficientBalanceException.class,
                () -> service.withdraw(1L, "SAV-TEST-0001", new BigDecimal("600"), null));

        verify(accountDao, never()).updateBalance(anyLong(), any());
        verify(transactionDao, never()).create(any());
    }

    @Test
    @DisplayName("withdraw beyond the daily limit is rejected before any debit")
    void dailyWithdrawalLimitEnforced() {
        when(accountDao.findByAccountNumber("SAV-TEST-0001"))
                .thenReturn(Optional.of(savingsWithBalance("90000.00")));
        // default configured daily limit is 100000 — pretend 20000 already withdrawn today
        when(transactionDao.sumAmountByTypeOnDate(eq(42L), any(), any()))
                .thenReturn(new BigDecimal("20000"));

        assertThrows(InvalidAmountException.class,
                () -> service.withdraw(1L, "SAV-TEST-0001", new BigDecimal("81000"), null));

        verify(accountDao, never()).updateBalance(anyLong(), any());
    }

    @Test
    @DisplayName("opening a savings account under its minimum is refused")
    void openingDepositBelowMinimumRejected() {
        assertThrows(InvalidAmountException.class,
                () -> service.openSavingsAccount(1L, 7L, new BigDecimal("100")));

        verify(accountDao, never()).create(any());
    }

    @Test
    @DisplayName("closing an account requires a zero balance first")
    void closeRequiresZeroBalance() {
        when(accountDao.findByAccountNumber("SAV-TEST-0001"))
                .thenReturn(Optional.of(savingsWithBalance("500.00")));

        assertThrows(InvalidAmountException.class,
                () -> service.closeAccount(1L, "SAV-TEST-0001"));

        verify(accountDao, never()).updateStatus(anyLong(), any());
    }

    @Test
    @DisplayName("blocking an active account updates status and audits")
    void blockAccountAudits() {
        when(accountDao.findByAccountNumber("SAV-TEST-0001"))
                .thenReturn(Optional.of(savingsWithBalance("500.00")));

        service.blockAccount(1L, "SAV-TEST-0001");

        var statusCaptor = ArgumentCaptor.forClass(com.jyotibank.model.enums.AccountStatus.class);
        verify(accountDao).updateStatus(eq(42L), statusCaptor.capture());
        assertEquals(AccountStatus.BLOCKED, statusCaptor.getValue());
    }
}
