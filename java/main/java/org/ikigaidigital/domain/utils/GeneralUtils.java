package org.ikigaidigital.domain.utils;

public final class GeneralUtils {
    public static String generateTraceId() {
        return java.util.UUID.randomUUID().toString();
    }
}
