package com.example.demo;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.restassured.response.Response;

@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    properties = {
        "management.server.port=0"
    })
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
class CallHealthActuatorSuccessfully {

    private Response response;

    @Value("${local.management.port}")
    private int adminPort;

    @BeforeAll
    void setUp() {
        response = given().accept(JSON).contentType(JSON)
            .when()
            .port(adminPort)
            .get("/gb/example-application/actuator/health")
            .then().extract().response();
    }

    @Test
    void should_return_overall_status_as_UP() {
        assertThat(response.path("status").toString()).isEqualTo("UP");
    }

}
