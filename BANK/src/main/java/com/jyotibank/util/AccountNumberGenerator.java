package com.jyotibank.util;

import com.jyotibank.model.enums.AccountType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public final class AccountNumberGenerator {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;
    private static final AtomicInteger SEQUENCE = new AtomicInteger(1000);

    private AccountNumberGenerator() {}

    public static String generate(AccountType accountType) {
        String typeCode = switch (accountType) {
            case SAVINGS -> "SAV";
            case CURRENT -> "CUR";
            case FIXED_DEPOSIT -> "FDP";
        };
        int seq = SEQUENCE.updateAndGet(current -> current >= 9999 ? 1000 : current + 1);
        int random = ThreadLocalRandom.current().nextInt(10, 99);
        return typeCode + LocalDate.now().format(DATE_FORMAT) + random + seq;
    }
}
