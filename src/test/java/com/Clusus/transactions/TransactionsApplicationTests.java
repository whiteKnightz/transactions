package com.Clusus.transactions;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= TransactionsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionsApplicationTests {

    @LocalServerPort
    private int port;
    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

	@Test
	public void contextLoads() throws Exception{
        HttpEntity<String> entity = new HttpEntity<String>(null,headers);

        JSONObject expected = new JSONObject();
        expected.put("Violation","Null");
        expected.put("Balanced","true");
        expected.toString();

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/getCsv"),
                HttpMethod.GET, entity, String.class);

        System.out.println(response.getBody());
        try {
            JSONAssert.assertEquals(expected.toString(), response.getBody(), false);
        }
        catch (JSONException je){
            throw new RuntimeException(je);
        }

        response = restTemplate.exchange(
                createURLWithPort("/getXml"),
                HttpMethod.GET, entity, String.class);

        try {
            JSONAssert.assertEquals(expected.toString(), response.getBody(), false);
        }
        catch (JSONException je){
            throw new RuntimeException(je);
        }
	}

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}

