package com.Clusus.services;

import com.Clusus.models.Transaction;
import com.Clusus.models.Violation;

import java.io.InputStream;
import java.util.List;

public interface TransactionProcessor {
	void importTransactions(InputStream is);

    List<Transaction> getImportedTransactions();

    List<Violation> validate();

    boolean isBalanced();

}
