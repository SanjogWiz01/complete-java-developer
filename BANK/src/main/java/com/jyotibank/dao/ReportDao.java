package com.jyotibank.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportDao {

    /** A bank-wide report row for accounts with no activity in a given window. */
    record DormancyCandidate(long accountId, String accountNumber, String accountType,
                             BigDecimal balance, LocalDateTime lastActivity) {}

    record TypeCount(String accountType, long count) {}

    /** Sum of balances across all ACTIVE accounts — the bank's deposit liability. */
    BigDecimal totalActiveDeposits();

    /** Accounts with no ledger activity within {@code days} (or none ever). */
    List<DormancyCandidate> findDormancyCandidates(int days);

    /** Number of accounts grouped by type. */
    List<TypeCount> countAccountsByType();
}
