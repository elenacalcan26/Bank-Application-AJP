package com.luxoft.bankapp.queue;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Queue<T> {
  private List<T> elements = Collections.synchronizedList(new LinkedList<>());

  public void add(T object) {
    elements.add(object);
  }

  public T get() {
    if (!elements.isEmpty()) {
      return elements.remove(0);
    }
    return null;
  }
}
