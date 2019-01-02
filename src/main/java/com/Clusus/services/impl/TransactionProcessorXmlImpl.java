package com.Clusus.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.Clusus.services.TransactionProcessor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.Clusus.models.Transaction;
import com.Clusus.models.Violation;

public class TransactionProcessorXmlImpl implements TransactionProcessor {

	List<Transaction> transactions = new ArrayList<>();

    public void importTransactions(InputStream is){
        int i;
        char c;
        try {
            String Data = new String();
            while((i = is.read())!=-1) {
                c = (char)i;
                Data = Data + c;
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;
            try {
                db = dbf.newDocumentBuilder();
                InputSource isou = new InputSource();
                isou.setCharacterStream(new StringReader(Data));
                try {
                    Document doc = db.parse(isou);
//                    String message = doc.getDocumentElement().getTextContent();
                    doc.getDocumentElement().normalize();


                    NodeList nList = doc.getElementsByTagName("Transaction");

                    for (int temp = 0; temp < nList.getLength(); temp++) {

                        Node nNode = nList.item(temp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                            Element eElement = (Element) nNode;

                            Transaction transLine = new Transaction(eElement.getAttribute("type"),new BigDecimal(eElement.getAttribute("amount")),eElement.getAttribute("narration"));
                            transactions.add(transLine);


                        }
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (ParserConfigurationException e1) {
                e1.printStackTrace();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        try {
            if(is!=null) {
                is.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> getImportedTransactions(){
        return this.transactions;
    }

    public boolean isBalanced(){
        BigDecimal bal = new BigDecimal("0");
        boolean balanced;
        for(Transaction transaction : this.transactions){
            if (transaction.getType().charAt(0)=='D'){
                bal=bal.subtract(transaction.getAmount());
            }
            else if (transaction.getType().charAt(0)=='C'){
                bal=bal.add(transaction.getAmount());
            }
        }
//        System.out.println(bal);
        balanced = (bal.compareTo(new BigDecimal("0"))==0)?true:false;
        return balanced;
    }

    public List<Violation> validate(){
        List<Violation> violations = new ArrayList<>();
        int i=1;
        for(Transaction transaction : this.transactions){
            if (transaction.getType().charAt(0)!='D' && transaction.getType().charAt(0)!='C'){
                Violation violation = new Violation(i,"Type","Invalid Transaction Type");
                violations.add(violation);
            }
            if ((transaction.getAmount().compareTo(new BigDecimal("0"))<=0)){
                Violation violation = new Violation(i,"Amount","Invalid Amount");
                violations.add(violation);
            }
            if (transaction.getNarration()==null){
                Violation violation = new Violation(i,"Narration","Invalid Narration");
                violations.add(violation);
            }
            i++;
        }
        return violations;
    }
}
