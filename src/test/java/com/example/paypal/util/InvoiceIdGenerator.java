package com.example.paypal.util;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InvoiceIdGenerator {

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("ddMMyyyyHHmmssSSS");

    public static synchronized String generate() {
        return "INC_" + LocalDateTime.now().format(FORMAT);
    }
}
