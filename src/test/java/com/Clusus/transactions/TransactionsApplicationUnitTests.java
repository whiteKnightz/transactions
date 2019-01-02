package com.Clusus.transactions;

import com.Clusus.transactions.controllers.TransactionProcessorController;
import org.hamcrest.CoreMatchers;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(value= TransactionsApplication.class, secure = false)
public class TransactionsApplicationUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionProcessorController transactionProcessorController;


	@Test
	public void contextLoads() throws Exception{

        JSONObject expected = new JSONObject();
        expected.put("Violation","Null");
        expected.put("Balanced","true");
        expected.toString();

        Mockito.when(
                transactionProcessorController.csvFunction()
        ).thenReturn(expected.toString());
        Mockito.when(
                transactionProcessorController.xmlFunction()
        ).thenReturn(expected.toString());


        RequestBuilder requestBuilder1 = MockMvcRequestBuilders.get(
                "/getCsv").accept(
                MediaType.ALL);
        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get(
                "/getXml").accept(
                MediaType.ALL);

        MvcResult result1 = mockMvc.perform(requestBuilder1).andReturn();
        MvcResult result2 = mockMvc.perform(requestBuilder2).andReturn();

        Assert.assertThat(result1.getResponse().getContentAsString(), CoreMatchers.containsString(expected.toString()));
        Assert.assertThat(result2.getResponse().getContentAsString(), CoreMatchers.containsString(expected.toString()));

    }

}

