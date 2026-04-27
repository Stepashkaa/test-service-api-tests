package com.test.api.factories;

import com.test.api.models.AdditionRequest;
import com.test.api.models.EntityRequest;

import java.util.List;
import java.util.UUID;

public final class EntityTestDataFactory {

    private EntityTestDataFactory() {
    }

    public static EntityRequest buildEntityRequest(String title,
                                                   Boolean verified,
                                                   String additionalInfo,
                                                   Integer additionalNumber,
                                                   List<Integer> importantNumbers) {
        return EntityRequest.builder()
                .title(title)
                .verified(verified)
                .addition(
                        AdditionRequest.builder()
                                .additionalInfo(additionalInfo)
                                .additionalNumber(additionalNumber)
                                .build()
                )
                .importantNumbers(importantNumbers)
                .build();
    }

    public static String uniqueTitle(String prefix) {
        return prefix + " " + UUID.randomUUID();
    }
}
