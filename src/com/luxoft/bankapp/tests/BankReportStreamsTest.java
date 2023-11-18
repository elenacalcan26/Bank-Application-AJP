package com.luxoft.bankapp.tests;

import com.luxoft.bankapp.domain.*;
import com.luxoft.bankapp.exceptions.ClientExistsException;
import com.luxoft.bankapp.service.BankReportStreams;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import static org.junit.Assert.assertEquals;

public class BankReportStreamsTest {
  private Bank bank = new Bank();
  private final BankReportStreams bankReportStreams = new BankReportStreams();
  private Client client1;
  private Client client2;
  private Client client3;

  @Before
  public void init() throws ClientExistsException {
    client1 = new Client("John", Gender.MALE, "Rome");
    Account account1 = new SavingAccount(1, 100);
    Account account2 = new CheckingAccount(2, 100, 20);
    client1.addAccount(account1);
    client1.addAccount(account2);

    client2 = new Client("Jane", Gender.FEMALE, "Madrid");
    Account account3 = new SavingAccount(3, 330);
    Account account4 = new CheckingAccount(4, 200, 20);
    client2.addAccount(account3);
    client2.addAccount(account4);

    client3 = new Client("Jim", Gender.MALE, "Madrid");
    Account account5 = new SavingAccount(5, 370);
    Account account6 = new CheckingAccount(6, 600, 80);
    client3.addAccount(account5);
    client3.addAccount(account6);

    bank.addClient(client1);
    bank.addClient(client2);
    bank.addClient(client3);
  }

  @Test
  public void testNumberOfClients() {
    assertEquals(3, bankReportStreams.getNumberOfClients(bank));
  }

  @Test
  public void testNumberOfAccounts() {
    assertEquals(6, bankReportStreams.getNumberOfAccounts(bank));
  }

  @Test
  public void testClientSorted() {
    SortedSet<Client> sortedClients = bankReportStreams.getClientSorted(bank);

    assertEquals(3, sortedClients.size());
    assertEquals(client2, sortedClients.first());
    assertEquals(client1, sortedClients.last());
  }

  @Test
  public void testTotalSumInAccounts() {
    assertEquals(1700, bankReportStreams.getTotalSumInAccounts(bank), 0);
  }

  @Test
  public void testBankCreditSum() {
    assertEquals(0, bankReportStreams.getBankCreditSum(bank), 0);
  }

  @Test
  public void testSortedAccountsBySum() {
    SortedSet<Account> accounts = bankReportStreams.getSortedAccountsBySum(bank);

    assertEquals(5, accounts.size());
    assertEquals("SavingAccount{balance=100.0}", accounts.first().toString());
    assertEquals("CheckingAccount{overdraft=80.0, balance=600.0}", accounts.last().toString());
  }

  @Test
  public void testCustomerAccounts() {
    Map<Client, Collection<Account>> customerAccounts = bankReportStreams.getCustomerAccounts(bank);

    assertEquals(3, customerAccounts.size());
    assertEquals("SavingAccount{balance=100.0}",
        customerAccounts.get(client1).iterator().next().toString());
  }

  @Test
  public void testClientByCity() {
    Map<String, List<Client>> clientsByCity = bankReportStreams.getClientsByCity(bank);

    assertEquals(2, clientsByCity.get("Madrid").size());
    assertEquals(1, clientsByCity.get("Rome").size());
  }
}
