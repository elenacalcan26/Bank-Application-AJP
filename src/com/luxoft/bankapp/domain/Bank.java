package com.luxoft.bankapp.domain;

import java.text.DateFormat;
import java.util.*;

import com.luxoft.bankapp.exceptions.ClientExistsException;
import com.luxoft.bankapp.exceptions.EmailException;
import com.luxoft.bankapp.service.EmailService;
import com.luxoft.bankapp.utils.ClientRegistrationListener;

public class Bank {
	
	private final Set<Client> clients = new LinkedHashSet<Client>();
	private final List<ClientRegistrationListener> listeners = new ArrayList<ClientRegistrationListener>();
	private final EmailService emailService = new EmailService();
	
	private int printedClients = 0;
	private int emailedClients = 0;
	private int debuggedClients = 0;

	private Client bankSystemClient = new Client("Bank System", null, null);
	private Client bankAdminClient = new Client("Bank Admin", null, null);
	
	public Bank() {
		listeners.add(new PrintClientListener());
		listeners.add(new EmailNotificationListener());
		listeners.add(new DebugListener());
	}
	
	public int getPrintedClients() {
		return printedClients;
	}

	public int getEmailedClients() {
		return emailedClients;
	}

	public int getDebuggedClients() {
		return debuggedClients;
	}
	
	public void addClient(final Client client) throws ClientExistsException {
    	if (clients.contains(client)) {
    		throw new ClientExistsException("Client already exists into the bank");
    	} 
    		
    	clients.add(client);
        notify(client);
	}
	
	private void notify(Client client) {
        for (ClientRegistrationListener listener: listeners) {
            listener.onClientAdded(client);
        }
    }
	
	public Set<Client> 	getClients() {
		return Collections.unmodifiableSet(clients);
	}
	
	class PrintClientListener implements ClientRegistrationListener {
		@Override 
		public void onClientAdded(Client client) {
	        System.out.println("Client added: " + client.getName());
	        printedClients++;
	    }

	}

	public void closeEmailService() {
		emailService.close();
	}

	class EmailNotificationListener implements ClientRegistrationListener {
		private static String NEW_CLIENT_EMAIL_TITLE = "New client added!";
		@Override 
		public void onClientAdded(Client client) {
	        System.out.println("Notification email for client " + client.getName() + " to be sent");
					try {
						Email email = composeEmail(client);
						emailService.sendNotificationEmail(email);
					} catch (EmailException e) {
						e.printStackTrace();
					}
	        emailedClients++;
	    }

			private Email composeEmail(Client client) {
				return new Email(
						client,
						bankSystemClient,
						List.of(bankAdminClient),
						NEW_CLIENT_EMAIL_TITLE,
						composeEmailBody(client));
			}

			private String composeEmailBody(Client client) {
				return "New client " + client + " has been added!";
			}
	}
	
	class DebugListener implements ClientRegistrationListener {
        @Override 
        public void onClientAdded(Client client) {
            System.out.println("Client " + client.getName() + " added on: " + DateFormat.getDateInstance(DateFormat.FULL).format(new Date()));
            debuggedClients++;
        }
    }
}




