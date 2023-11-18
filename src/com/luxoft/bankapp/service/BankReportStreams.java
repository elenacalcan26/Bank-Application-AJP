package com.luxoft.bankapp.service;

import com.luxoft.bankapp.domain.Account;
import com.luxoft.bankapp.domain.Bank;
import com.luxoft.bankapp.domain.CheckingAccount;
import com.luxoft.bankapp.domain.Client;

import java.util.*;
import java.util.stream.Collectors;

public class BankReportStreams {
  public int getNumberOfClients(Bank bank) {
    return bank.getClients().size();
  }

  public int getNumberOfAccounts(Bank bank) {
    return bank
        .getClients()
        .stream()
        .map(client -> client.getAccounts().size())
        .reduce(0, Integer::sum);
  }

  public SortedSet<Client> getClientSorted(Bank bank) {
    return bank
        .getClients()
        .stream()
        .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Client::getName))));
  }

  public double getTotalSumInAccounts(Bank bank) {
    return bank
        .getClients()
        .stream()
        .map(client -> client
            .getAccounts()
            .stream()
            .map(Account::getBalance).reduce(0.0, Double::sum))
        .reduce(0.0, Double::sum);
  }

  public SortedSet<Account> getSortedAccountsBySum(Bank bank) {
    SortedSet<Account> sortedAccounts = new TreeSet<>(Comparator.comparing(Account::getBalance));
    sortedAccounts.addAll(
        bank.getClients()
            .stream()
            .map(Client::getAccounts)
            .flatMap(Set::stream)
            .toList());
    return sortedAccounts;
  }

  public double getBankCreditSum(Bank bank) {
    return bank
        .getClients()
        .stream()
        .map(Client::getAccounts)
        .flatMap(Set::stream)
        .filter(account -> account instanceof CheckingAccount)
        .map(Account::getBalance)
        .filter(balance -> Double.compare(balance, 0.0) < 0)
        .reduce(0.0, Double::sum);
  }

  public Map<Client, Collection<Account>> getCustomerAccounts(Bank bank) {
    Map<Client, Collection<Account>> customersAccounts = new HashMap<>();
    bank.getClients().forEach(client -> customersAccounts.put(client, client.getAccounts()));
    return customersAccounts;
  }

  public Map<String, List<Client>> getClientsByCity(Bank bank) {
    return new TreeMap<>(
        bank.getClients()
            .stream()
            .collect(Collectors.groupingBy(Client::getCity)));
  }
}
