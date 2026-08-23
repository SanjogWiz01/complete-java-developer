package com.jyotibank.service;

import com.jyotibank.dao.TransactionDao;
import com.jyotibank.model.Transaction;
import com.jyotibank.model.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock private TransactionDao transactionDao;

    private TransactionService service;

    @BeforeEach
    void setUp() {
        service = new TransactionService(transactionDao);
    }

    private Transaction tx(TransactionType type, String amount) {
        return new Transaction("REF-1", 1L, type, new BigDecimal(amount),
                BigDecimal.TEN, BigDecimal.TEN, "test");
    }

    @Test
    @DisplayName("computeTotals classifies credits and debits correctly")
    void totalsClassifyMoneyInAndOut() {
        var totals = service.computeTotals(java.util.List.of(
                tx(TransactionType.DEPOSIT, "100.00"),
                tx(TransactionType.INTEREST, "3.25"),
                tx(TransactionType.WITHDRAWAL, "40.00"),
                tx(TransactionType.TRANSFER, "13.50"),
                tx(TransactionType.FEE, "0.50")
        ));

        assertEquals(5, totals.entries());
        assertEquals(new BigDecimal("103.25"), totals.totalIn());
        assertEquals(new BigDecimal("54.00"), totals.totalOut());
    }
}
