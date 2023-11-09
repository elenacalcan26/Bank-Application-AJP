package com.luxoft.bankapp.domain;

import java.util.*;

public class Client {
	
	private String name;
	private Gender gender;
	private Set<Account> accounts = new LinkedHashSet<>();

	public Client(String name, Gender gender) {
		this.name = name;
		this.gender = gender;
	}
	
	public void addAccount(final Account account) {
		accounts.add(account);
	}
	
	public String getName() {
		return name;
	}
	
	public Gender getGender() {
		return gender;
	}
	
	public Set<Account> getAccounts() {
		return Collections.unmodifiableSet(accounts);
	}
	
	public String getClientGreeting() {
		if (gender != null) {
			return gender.getGreeting() + " " + name;
		} else {
			return name;
		}
	}
	
	@Override
	public String toString() {
		return getClientGreeting();
	}

}
