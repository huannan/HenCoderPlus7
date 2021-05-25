package com.hencoder.generics;

/**
 * 泛型类型的创建
 */
class Wrapper<T> {
  private T instance;

  public T get() {
    return instance;
  }

  public void set(T newInstance) {
    instance = newInstance;
  }
}
