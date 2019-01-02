package com.Clusus.transactions.controllers;

import com.Clusus.models.Violation;
import com.Clusus.services.impl.TransactionProcessorCsvImpl;
import com.Clusus.services.impl.TransactionProcessorXmlImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class TransactionProcessorController {

    @Value(value = "classpath:sample.csv")
    private Resource sampleCsv;

    @Value(value = "classpath:sample.xml")
    private Resource sampleXml;

    @GetMapping("/getCsv")
    public String csvFunction() {
        JSONObject jo = new JSONObject();
        TransactionProcessorCsvImpl transactionProcessorCsv = new TransactionProcessorCsvImpl();
        try{
//            transactionProcessorCsv.importTransactions(new FileInputStream("resources\\sample.csv"));
            transactionProcessorCsv.importTransactions(sampleCsv.getInputStream());
        }
        catch (IOException ioe){
            throw new RuntimeException(ioe);
        }
        List<Violation> violations = transactionProcessorCsv.validate();
        if (violations.size()==0) {
            boolean balanced = transactionProcessorCsv.isBalanced();
            jo.put("Violation","Null");
//            jo.put("Transactions",transactionProcessorCsv.getImportedTransactions());
            jo.put("Balanced",String.valueOf(balanced));
            return jo.toString();
        }
        else{
            jo.put("Violation",violations.toString());
//            jo.put("Transactions","N/A due to Violation");
            jo.put("Balanced","false");
            return jo.toString();
        }
    
    }

    @GetMapping("/getXml")
    public String xmlFunction() {
        JSONObject jo = new JSONObject();
        TransactionProcessorXmlImpl transactionProcessorXmlImpl = new TransactionProcessorXmlImpl();
        try {
//            transactionProcessorXmlImpl.importTransactions(new FileInputStream("resources\\sample.xml"));
            transactionProcessorXmlImpl.importTransactions(sampleXml.getInputStream());
        }
        catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        List<Violation> violations = transactionProcessorXmlImpl.validate();
        if (violations.size()==0){
            boolean balanced = transactionProcessorXmlImpl.isBalanced();
            jo.put("Violation","Null");
//            jo.put("Transactions",transactionProcessorXmlImpl.getImportedTransactions());
            jo.put("Balanced",String.valueOf(balanced));
            return jo.toString();
        }
        else{
            jo.put("Violation",violations.toString());
//            jo.put("Transactions","N/A due to Violation");
            jo.put("Balanced","false");
            return jo.toString();
        }


    }
}
