package com.test.api.tests;

import com.test.api.clients.EntityClient;
import com.test.api.config.Configuration;
import com.test.api.models.EntityRequest;
import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;
import org.testng.annotations.AfterMethod;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public abstract class BaseTest {

    protected static final Configuration config = ConfigFactory.create(Configuration.class);

    protected static final RequestSpecification spec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri(config.baseUrl())
            .setBasePath(config.basePath())
            .addFilter(new AllureRestAssured())
            .log(LogDetail.ALL)
            .build();

    protected final EntityClient entityClient = new EntityClient(spec);

    private final ThreadLocal<Set<Integer>> createdEntityIds = ThreadLocal.withInitial(HashSet::new);

    protected Integer createEntityAndRemember(EntityRequest request) {
        Integer id = entityClient.createEntity(request);
        createdEntityIds.get().add(id);
        return id;
    }

    protected void removeEntityFromCleanUp(Integer id) {
        createdEntityIds.get().remove(id);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        Set<Integer> ids = createdEntityIds.get();

        for (Integer id : ids) {
            try {
                Response response = entityClient.deleteEntity(id);

                if (response.statusCode() != 204 && response.statusCode() != 404) {
                    String message = String.format(
                            "Не удалось удалить тестовую сущность id=%s. Status code: %s. Body: %s",
                            id,
                            response.statusCode(),
                            response.asString()
                    );

                    log.warn(message);
                    Allure.addAttachment("Cleanup warning", "text/plain", message);
                }
            } catch (Exception exception) {
                String message = String.format(
                        "Ошибка при очистке тестовой сущности id=%s. Причина: %s",
                        id,
                        exception.getMessage()
                );

                log.warn(message, exception);
                Allure.addAttachment("Cleanup exception", "text/plain", message);
            }
        }

        ids.clear();
        createdEntityIds.remove();
    }
}