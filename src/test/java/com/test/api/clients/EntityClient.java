package com.test.api.clients;

import com.test.api.models.EntityFilterResponse;
import com.test.api.models.EntityRequest;
import com.test.api.models.EntityResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import static io.restassured.RestAssured.given;

public class EntityClient {

    private static final Object API_LOCK = new Object();

    private final RequestSpecification spec;

    public EntityClient(RequestSpecification spec) {
        this.spec = spec;
    }

    @Step("Создание сущности")
    public Integer createEntity(EntityRequest request) {
        synchronized (API_LOCK) {
            Response response = given()
                    .spec(spec)
                    .body(request)
                    .when()
                    .post("/create");

            Assert.assertEquals(
                    response.statusCode(),
                    200,
                    "POST /api/create вернул ошибку. Тело ответа: " + response.asString()
            );

            return Integer.valueOf(response.asString().trim());
        }
    }

    @Step("Получение сущности по ID: {id}")
    public EntityResponse getEntityById(Integer id) {
        synchronized (API_LOCK) {
            return given()
                    .spec(spec)
                    .when()
                    .get("/get/{id}", id)
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(EntityResponse.class);
        }
    }

    @Step("Получение списка сущностей")
    public EntityFilterResponse getAllEntities(String title, Boolean verified, Integer page, Integer perPage) {
        synchronized (API_LOCK) {
            return given()
                    .spec(spec)
                    .queryParam("title", title)
                    .queryParam("verified", verified)
                    .queryParam("page", page)
                    .queryParam("perPage", perPage)
                    .when()
                    .get("/getAll")
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(EntityFilterResponse.class);
        }
    }

    @Step("Обновление сущности по ID: {id}")
    public void updateEntity(Integer id, EntityRequest request) {
        synchronized (API_LOCK) {
            given()
                    .spec(spec)
                    .body(request)
                    .when()
                    .patch("/patch/{id}", id)
                    .then()
                    .statusCode(204);
        }
    }

    @Step("Удаление сущности по ID: {id}")
    public void deleteEntitySuccessfully(Integer id) {
        synchronized (API_LOCK) {
            deleteEntity(id)
                    .then()
                    .statusCode(204);
        }
    }

    public Response deleteEntity(Integer id) {
        synchronized (API_LOCK) {
            return given()
                    .spec(spec)
                    .when()
                    .delete("/delete/{id}", id);
        }
    }
}