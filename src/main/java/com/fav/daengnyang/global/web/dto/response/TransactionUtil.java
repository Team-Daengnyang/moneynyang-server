package com.fav.daengnyang.global.web.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class TransactionUtil {

    private TransactionUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String generateUniqueTransactionNo() {
        LocalDateTime dateTime = LocalDateTime.now();

        String dateTimePrefix = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomSuffix = String.format("%06d", new Random().nextInt(1000000)); // 6자리 난수 생성
        return dateTimePrefix + randomSuffix;
    }
}
