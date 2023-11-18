package com.luxoft.bankapp.service;

import com.luxoft.bankapp.domain.Email;
import com.luxoft.bankapp.exceptions.EmailException;
import com.luxoft.bankapp.queue.Queue;

public class EmailService implements Runnable {
  private Queue<Email> emailQueue = new Queue<>();
  private Thread currentThread;
  private boolean isClosed = false;


  public EmailService() {
    currentThread = new Thread(this);
    currentThread.start();
  }

  public void sendNotificationEmail(Email email) throws EmailException {
    if (!isClosed) {
      emailQueue.add(email);
      synchronized (emailQueue) {
        emailQueue.notify();
      }
    } else {
      throw new EmailException("Email service is closed! Can not send emails!");
    }
  }

  @Override
  public void run() {
    for (;;) {
      if (isClosed) {
        return;
      }

      emailSenderEmulator();

      synchronized (emailQueue) {
        try {
          emailQueue.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void emailSenderEmulator() {
    Email email = emailQueue.get();
    if (email != null) {
      System.out.println("Email sent!\n" + email);
    }
  }

  public void close() {
    isClosed = true;
    synchronized (emailQueue) {
      emailQueue.notify();
    }

    try {
      currentThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
