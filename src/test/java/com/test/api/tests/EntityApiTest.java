package com.test.api.tests;

import com.test.api.models.EntityFilterResponse;
import com.test.api.models.EntityRequest;
import com.test.api.models.EntityResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static com.test.api.assertions.EntityAssertions.assertEntityMatchesRequest;
import static com.test.api.factories.EntityTestDataFactory.buildEntityRequest;
import static com.test.api.factories.EntityTestDataFactory.uniqueTitle;
import static io.qameta.allure.SeverityLevel.CRITICAL;
import static io.qameta.allure.SeverityLevel.NORMAL;

@Epic("Test Service API")
@Feature("Entity API")
public class EntityApiTest extends BaseTest {

    @Test(description = "Создание сущности через POST /api/create")
    @Story("Создание сущности")
    @Severity(CRITICAL)
    public void shouldCreateEntity() {
        EntityRequest request = buildEntityRequest(
                uniqueTitle("Created entity"),
                true,
                "Create test additional info",
                100,
                List.of(1, 2, 3)
        );

        Integer id = createEntityAndRemember(request);

        Assert.assertNotNull(id, "ID созданной сущности не должен быть null");
        Assert.assertTrue(id > 0, "ID созданной сущности должен быть больше 0");

        EntityResponse actualEntity = entityClient.getEntityById(id);

        assertEntityMatchesRequest(actualEntity, id, request);
    }

    @Test(description = "Получение сущности по ID через GET /api/get/{id}")
    @Story("Получение сущности по ID")
    @Severity(CRITICAL)
    public void shouldGetEntityById() {
        EntityRequest request = buildEntityRequest(
                uniqueTitle("Get entity"),
                true,
                "Get test additional info",
                200,
                List.of(4, 5, 6)
        );

        Integer id = createEntityAndRemember(request);

        EntityResponse actualEntity = entityClient.getEntityById(id);

        assertEntityMatchesRequest(actualEntity, id, request);
    }

    @Test(description = "Получение списка сущностей через GET /api/getAll")
    @Story("Получение списка сущностей")
    @Severity(NORMAL)
    public void shouldGetAllEntities() {
        String title = uniqueTitle("Get all entity");

        EntityRequest request = buildEntityRequest(
                title,
                true,
                "Get all test additional info",
                300,
                List.of(7, 8, 9)
        );

        Integer id = createEntityAndRemember(request);

        EntityFilterResponse response = entityClient.getAllEntities(title, true, 1, 10);

        Assert.assertNotNull(response, "Ответ GET /getAll не должен быть null");
        Assert.assertNotNull(response.getEntity(), "Список entity не должен быть null");
        Assert.assertEquals(response.getPage(), Integer.valueOf(1), "Номер страницы должен совпадать");
        Assert.assertEquals(response.getPerPage(), Integer.valueOf(10), "Количество элементов на странице должно совпадать");

        EntityResponse actualEntity = response.getEntity()
                .stream()
                .filter(entity -> id.equals(entity.getId()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Созданная сущность не найдена в ответе GET /getAll"));

        assertEntityMatchesRequest(actualEntity, id, request);
    }

    @Test(description = "Обновление сущности через PATCH /api/patch/{id}")
    @Story("Обновление сущности")
    @Severity(CRITICAL)
    public void shouldUpdateEntity() {
        EntityRequest createRequest = buildEntityRequest(
                uniqueTitle("Entity before update"),
                true,
                "Before update additional info",
                400,
                List.of(10, 20, 30)
        );

        Integer id = createEntityAndRemember(createRequest);

        EntityRequest updateRequest = buildEntityRequest(
                uniqueTitle("Entity after update"),
                false,
                "After update additional info",
                500,
                List.of(40, 50, 60)
        );

        entityClient.updateEntity(id, updateRequest);

        EntityResponse actualEntity = entityClient.getEntityById(id);

        assertEntityMatchesRequest(actualEntity, id, updateRequest);
    }

    @Test(description = "Удаление сущности через DELETE /api/delete/{id}")
    @Story("Удаление сущности")
    @Severity(CRITICAL)
    public void shouldDeleteEntity() {
        EntityRequest request = buildEntityRequest(
                uniqueTitle("Entity for delete"),
                true,
                "Delete test additional info",
                600,
                List.of(70, 80, 90)
        );

        Integer id = createEntityAndRemember(request);

        entityClient.deleteEntitySuccessfully(id);

        removeEntityFromCleanUp(id);
    }
}