package com.test.api.assertions;

import com.test.api.models.EntityRequest;
import com.test.api.models.EntityResponse;
import org.testng.Assert;

public final class EntityAssertions {

    private EntityAssertions() {
    }

    public static void assertEntityMatchesRequest(EntityResponse actualEntity,
                                                  Integer expectedId,
                                                  EntityRequest expectedRequest) {
        Assert.assertNotNull(actualEntity, "Сущность в ответе не должна быть null");
        Assert.assertEquals(actualEntity.getId(), expectedId, "ID сущности должен совпадать");
        Assert.assertEquals(actualEntity.getTitle(), expectedRequest.getTitle(), "Title должен совпадать");
        Assert.assertEquals(actualEntity.getVerified(), expectedRequest.getVerified(), "Verified должен совпадать");
        Assert.assertEquals(
                actualEntity.getImportantNumbers(),
                expectedRequest.getImportantNumbers(),
                "Important numbers должны совпадать"
        );

        Assert.assertNotNull(actualEntity.getAddition(), "Addition не должен быть null");
        Assert.assertNotNull(actualEntity.getAddition().getId(), "ID addition не должен быть null");
        Assert.assertTrue(actualEntity.getAddition().getId() > 0, "ID addition должен быть больше 0");

        Assert.assertEquals(
                actualEntity.getAddition().getAdditionalInfo(),
                expectedRequest.getAddition().getAdditionalInfo(),
                "Additional info должен совпадать"
        );

        Assert.assertEquals(
                actualEntity.getAddition().getAdditionalNumber(),
                expectedRequest.getAddition().getAdditionalNumber(),
                "Additional number должен совпадать"
        );
    }
}