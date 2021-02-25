package com.example.taxengine;

import com.example.taxengine.model.TaxResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.example.taxengine.Utils.toBigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TaxEngineApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaxController controller;

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    public void testInvalidYear() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(getRootUrl() + "/tax/2019/100", String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testTaxYear2020() {
        ResponseEntity<TaxResult> responseEntity = restTemplate.getForEntity(getRootUrl() + "/tax/2020/15000", TaxResult.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getYear()).isEqualTo(2020);
        assertThat(responseEntity.getBody().getIncome()).isEqualTo(toBigDecimal(15000.00));
        assertThat(responseEntity.getBody().getTax()).isEqualTo(toBigDecimal(500.00));
        assertThat(responseEntity.getBody().getIncomeDisplay()).isEqualTo("$15,000.00");
        assertThat(responseEntity.getBody().getTaxDisplay()).isEqualTo("$500.00");
    }

    @Test
    public void testBadRequest() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(getRootUrl() + "/tax/2020/150h00", String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
