package com.deco2800.game.utils;

import java.util.function.Function;

/**
 * Apply a custom toString() for a class without overriding the class, using a decorator.
 * @param <T> Class to decorate
 */
public class StringDecorator<T> {
  private final T object;
  private final Function<T, String> printFn;

  /**
   * Create a string-decorated object.
   * @param object Object to decorate.
   * @param printFn Function which takes the object and returns the desired string representation.
   */
  public StringDecorator(T object, Function<T, String> printFn) {
    this.object = object;
    this.printFn = printFn;
  }

  public T getObject() {
    return object;
  }

  @Override
  public String toString() {
    return printFn.apply(object);
  }
}
