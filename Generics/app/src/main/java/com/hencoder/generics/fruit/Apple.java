package com.hencoder.generics.fruit;

import android.view.ScaleGestureDetector;

import java.util.List;

public class Apple implements Fruit {
  @Override
  public float getWeight() {
    return 1;
  }

  /**
   * 下界
   */
  public void addMeToList(List<? super Apple> list) {
    list.add(this);
    ScaleGestureDetector
  }
}
