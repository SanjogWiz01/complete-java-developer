package com.jyotibank.util;

import com.jyotibank.model.Transaction;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class ReceiptPrinter {

    private ReceiptPrinter() {}

    public static Path printTransactionReceipt(Transaction transaction, Path receiptsDir) throws IOException {
        Files.createDirectories(receiptsDir);
        String fileName = "receipt-" + transaction.getReferenceNumber() + ".txt";
        Path filePath = receiptsDir.resolve(fileName);

        String content = """
                ================== JYOTI BANK ==================
                Receipt Ref : %s
                Account ID  : %d
                Type        : %s
                Amount      : %s
                Status      : %s
                Created At  : %s
                =================================================
                """
                .formatted(
                        transaction.getReferenceNumber(),
                        transaction.getAccountId(),
                        transaction.getTransactionType(),
                        transaction.getAmount(),
                        transaction.getStatus(),
                        DateUtil.format(transaction.getCreatedAt()));

        Files.writeString(filePath, content, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        return filePath;
    }
}
