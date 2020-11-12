package br.com.fatura.endpointsTestes;

import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import static io.restassured.RestAssured.given;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
public class AlteraVencimentoTestes {


    @LocalServerPort
    private int port;

    @Value("${fatura.teste}")
    private String nFatura;

    @Value("${cartao.teste}")
    private String nCartao;


    @Test
    public void deveRetornarNotFoundCasoOCartaoNaoExista() throws JSONException {


        JSONObject novoVencimento = new JSONObject()
                .put("dia",5);

        given()
                .basePath("/api/faturas/" + nCartao + "12345" + "/vencimentos/" + nFatura)
                .header("Authorization", getToken())
                .header("Content-Type", "application/json")
                .port(port)
                .body(novoVencimento.toString())
                .when()
                .put()
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    @Test
    public void deveRetornaroKCasoOCartaoExista() throws JSONException {


        JSONObject novoVencimento = new JSONObject()
                .put("dia",5);

        given()
                .basePath("/api/faturas/" + nCartao + "/vencimentos/" + nFatura)
                .header("Authorization", getToken())
                .header("Content-Type", "application/json")
                .port(port)
                .body(novoVencimento.toString())
                .when()
                .put()
                .then()
                .statusCode(HttpStatus.OK.value());

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
