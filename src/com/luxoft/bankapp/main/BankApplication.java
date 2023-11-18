package com.luxoft.bankapp.main;

import com.luxoft.bankapp.domain.Account;
import com.luxoft.bankapp.domain.Bank;
import com.luxoft.bankapp.domain.CheckingAccount;
import com.luxoft.bankapp.domain.Client;
import com.luxoft.bankapp.domain.Gender;
import com.luxoft.bankapp.domain.SavingAccount;
import com.luxoft.bankapp.exceptions.ClientExistsException;
import com.luxoft.bankapp.exceptions.NotEnoughFundsException;
import com.luxoft.bankapp.exceptions.OverdraftLimitExceededException;
import com.luxoft.bankapp.service.BankReport;
import com.luxoft.bankapp.service.BankReportStreams;
import com.luxoft.bankapp.service.BankService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BankApplication {
	
	private static Bank bank;
	private final static String STATISTICS_FLAG = "-statistics";
	
	public static void main(String[] args) {
		bank = new Bank();
		modifyBank();
		printBalance();
		BankService.printMaximumAmountToWithdraw(bank);

		if (args.length > 0 && args[0].equals(STATISTICS_FLAG)) {
			printBankStatistics();
		}

		bank.closeEmailService();
	}
	
	private static void modifyBank() {
		Client client1 = new Client("John", Gender.MALE, "Rome");
		Account account1 = new SavingAccount(1, 100);
		Account account2 = new CheckingAccount(2, 100, 20);
		client1.addAccount(account1);
		client1.addAccount(account2);
		
		try {
		   BankService.addClient(bank, client1);
		} catch(ClientExistsException e) {
			System.out.format("Cannot add an already existing client: %s%n", client1.getName());
	    } 

		account1.deposit(100);
		try {
		  account1.withdraw(10);
		} catch (OverdraftLimitExceededException e) {
	    	System.out.format("Not enough funds for account %d, balance: %.2f, overdraft: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getOverdraft(), e.getAmount());
	    } catch (NotEnoughFundsException e) {
	    	System.out.format("Not enough funds for account %d, balance: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getAmount());
	    }
		
		try {
		  account2.withdraw(90);
		} catch (OverdraftLimitExceededException e) {
	      System.out.format("Not enough funds for account %d, balance: %.2f, overdraft: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getOverdraft(), e.getAmount());
	    } catch (NotEnoughFundsException e) {
	      System.out.format("Not enough funds for account %d, balance: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getAmount());
	    }
		
		try {
		  account2.withdraw(100);
		} catch (OverdraftLimitExceededException e) {
	      System.out.format("Not enough funds for account %d, balance: %.2f, overdraft: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getOverdraft(), e.getAmount());
	    } catch (NotEnoughFundsException e) {
	      System.out.format("Not enough funds for account %d, balance: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getAmount());
	    }
		
		try {
		  BankService.addClient(bank, client1);
		} catch(ClientExistsException e) {
		  System.out.format("Cannot add an already existing client: %s%n", client1);
	    } 
	}
	
	private static void printBalance() {
		System.out.format("%nPrint balance for all clients%n");
		for(Client client : bank.getClients()) {
			System.out.println("Client: " + client);
			for (Account account : client.getAccounts()) {
				System.out.format("Account %d : %.2f%n", account.getId(), account.getBalance());
			}
		}
	}

	private static void printBankStatistics() {
		BankReport bankReport = new BankReport();

		System.out.format("Total number of clients: %d%n", bankReport.getNumberOfClients(bank));
		System.out.format("Total number of accounts: %d%n", bankReport.getNumberOfAccounts(bank));
		System.out.format("Clients sorted in alphabetically order: %s%n", bankReport.getClientSorted(bank));
		System.out.format("Total balance from accounts of all bank clients: %s%n", bankReport.getTotalSumInAccounts(bank));
		System.out.format("Ordered accounts by current balance: %s%n", bankReport.getSortedAccountsBySum(bank));
		System.out.format("Total amount of credits granted to the bank clients: %f%n", bankReport.getBankCreditSum(bank));

		System.out.println("Printing client to accounts mappings!");
		Map<Client, Collection<Account>> clientAccountsMap = bankReport.getCustomerAccountsBank(bank);
		clientAccountsMap.keySet().forEach(client -> System.out.format("Client %s has accounts: %s%n", client, clientAccountsMap.get(client)));

		System.out.println("Printing bank clients grouped by city");
		Map<String, List<Client>> clientsGroupedByCity = bankReport.getClientsByCity(bank);
		clientsGroupedByCity.keySet().forEach(city -> System.out.format("From %s city are the next clients: %s%n", city, clientsGroupedByCity.get(city)));
		System.out.println("Done!");

		BankReportStreams bankReportStreams = new BankReportStreams();

		System.out.format("Total number of clients: %d%n", bankReportStreams.getNumberOfClients(bank));
		System.out.format("Total number of accounts: %d%n", bankReportStreams.getNumberOfAccounts(bank));
		System.out.format("Clients sorted in alphabetically order: %s%n", bankReportStreams.getClientSorted(bank));
		System.out.format("Total balance from accounts of all bank clients: %s%n", bankReportStreams.getTotalSumInAccounts(bank));
		System.out.format("Ordered accounts by current balance: %s%n", bankReportStreams.getSortedAccountsBySum(bank));
		System.out.format("Total amount of credits granted to the bank clients: %f%n", bankReportStreams.getBankCreditSum(bank));

		System.out.println("Printing client to accounts mappings!");
		Map<Client, Collection<Account>> clientAccountsMap2 = bankReportStreams.getCustomerAccounts(bank);
		clientAccountsMap2.keySet().forEach(client -> System.out.format("Client %s has accounts: %s%n", client, clientAccountsMap2.get(client)));

		System.out.println("Printing bank clients grouped by city");
		Map<String, List<Client>> clientsGroupedByCity2 = bankReportStreams.getClientsByCity(bank);
		clientsGroupedByCity2.keySet().forEach(city -> System.out.format("From %s city are the next clients: %s%n", city, clientsGroupedByCity2.get(city)));
		System.out.println("Done!");
	}
}
