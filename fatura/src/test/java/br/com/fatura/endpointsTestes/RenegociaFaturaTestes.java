package br.com.fatura.endpointsTestes;

import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import static io.restassured.RestAssured.given;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RenegociaFaturaTestes {


    @LocalServerPort
    private int port;


    @Test
    public void deveriaRetornarCreatedAoRenegociarFatura() throws JSONException {

        var numeroCartaoExistente = "91766c61-7faf-4c62-bbcc-4a83cf6c4273";
        var identificadorFatura = "dbe2db33-b1c7-49ff-9a36-30251f299772";

        JSONObject solicitaParcelamento = new JSONObject()
                .put("identificadorFatura","dbe2db33-b1c7-49ff-9a36-30251f299772")
                .put("quantidade", 8)
                .put("valor", 7500);

        given()
                .basePath("/api/faturas/renegociacoes/" + numeroCartaoExistente + "/" + identificadorFatura)
                .header("Authorization", getToken())
                .header("Content-Type", "application/json")
                .port(port)
                .body(solicitaParcelamento.toString())
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());

    }


    @Test
    public void deveriaRetornarNotFoundAoTentarRenegociarFaturaNaoExistente() throws JSONException {

        var numeroCartaoExistente = "91766c61-7faf-4c62-bbcc-4a83cf6c4273";
        var identificadorFatura = "dbe2db33-b1c7-49ff-9a36-30251f291234";

        JSONObject solicitaParcelamento = new JSONObject()
                .put("identificadorFatura","dbe2db33-b1c7-49ff-9a36-30251f291234")
                .put("quantidade", 8)
                .put("valor", 7500);

        given()
                .basePath("/api/faturas/renegociacoes/" + numeroCartaoExistente + "/" + identificadorFatura)
                .header("Authorization", getToken())
                .header("Content-Type", "application/json")
                .port(port)
                .body(solicitaParcelamento.toString())
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());

    }


    public String getToken() throws JSONException {

        Response response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("grant_type", "password")
                .formParam("username", "bootcamp")
                .formParam("password", "bootcampproposta")
                .formParam("client_id", "nosso-cartao")
                .formParam("client_secret", "")
                .when()
                .post("http://localhost:18080/auth/realms/nosso-cartao/protocol/openid-connect/token");


        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        String accessToken = jsonObject.get("access_token").toString();

        return "Bearer " + accessToken;

    }

}
