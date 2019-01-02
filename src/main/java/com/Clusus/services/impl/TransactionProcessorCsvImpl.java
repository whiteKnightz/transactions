package com.Clusus.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.Clusus.models.Transaction;
import com.Clusus.models.Violation;
import com.Clusus.services.TransactionProcessor;

public class TransactionProcessorCsvImpl implements TransactionProcessor {

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

            String cvsSplitBy = ",";
            List<Transaction> transactions = new ArrayList<Transaction>();
            String[] lines = Data.split("\\s*\\r?\\n\\s*");

            int lNo = 0;
            while (lNo<lines.length) {

                String[] sample = lines[lNo].split(cvsSplitBy);
                Transaction transLine = new Transaction(sample[0],new BigDecimal(sample[1]),sample[2]);
                transactions.add(transLine);
                lNo++;
            }
            this.transactions = transactions;
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        try {
            if(is!=null) {
                is.close();
            }
        }
        catch(IOException e) {
            throw new RuntimeException(e);
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
