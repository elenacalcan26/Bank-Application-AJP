package com.luxoft.bankapp.service;

import com.luxoft.bankapp.domain.Account;
import com.luxoft.bankapp.domain.Bank;
import com.luxoft.bankapp.domain.CheckingAccount;
import com.luxoft.bankapp.domain.Client;

import java.util.*;

public class BankReport {
  public int getNumberOfClients(Bank bank) {
    return bank.getClients().size();
  }

  public int getNumberOfAccounts(Bank bank) {
    int accountsNumber = 0;

    for (var client : bank.getClients()) {
      accountsNumber += client.getAccounts().size();
    }

    return accountsNumber;
  }

  public SortedSet<Client> getClientSorted(Bank bank) {
    SortedSet<Client> sortedClients = new TreeSet<Client>(Comparator.comparing(Client::getName));
    sortedClients.addAll(bank.getClients());
    return sortedClients;
  }

  public double getTotalSumInAccounts(Bank bank) {
    double totalSum = 0.0;

    for (var client : bank.getClients()) {
      for (var account : client.getAccounts()) {
        totalSum += account.getBalance();
      }
    }

    return totalSum;
  }

  public SortedSet<Account> getSortedAccountsBySum(Bank bank) {
    SortedSet<Account> sortedAccounts = new TreeSet<Account>(Comparator.comparing(Account::getBalance));
    List<Account> accounts = new ArrayList<>();

    for (var client : bank.getClients()){
      accounts.addAll(client.getAccounts());
    }

    sortedAccounts.addAll(accounts);
    return sortedAccounts;
  }

  public double getBankCreditSum(Bank bank) {
    double sum = 0.0;

    for (var client : bank.getClients()) {
      for (var account : client.getAccounts()) {
        if (account instanceof CheckingAccount) {
          if (Double.compare(account.getBalance(), 0.0) < 0) {
            sum -= account.getBalance();
          }
        }
      }
    }

    return sum;
  }

  public Map<Client, Collection<Account>> getCustomerAccountsBank(Bank bank) {
    Map<Client, Collection<Account>> customersAccounts = new HashMap<>();
    for (var client : bank.getClients()) {
      customersAccounts.put(client, client.getAccounts());
    }
    return customersAccounts;
  }

  public Map<String, List<Client>> getClientsByCity(Bank bank) {
    Map<String, List<Client>> clientsByCity = new TreeMap<>();
    SortedSet<String> cities = new TreeSet<>();
    for (var client : bank.getClients()) {
      cities.add(client.getCity());
    }

    for (var city : cities) {
      List<Client> clients = new ArrayList<>();
      for (var client : bank.getClients()) {
        if (client.getCity().equals(city)) {
          clients.add(client);
        }
      }
      clientsByCity.put(city, clients);
    }

    return clientsByCity;
  }

}
