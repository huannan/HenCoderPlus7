package com.hencoder.generics;

import com.hencoder.generics.sim.Sim;

import java.io.Closeable;

/**
 * 多个类型参数
 * 类型参数的上界（可以有多个）
 */
interface SimShop<T, C extends Sim & Cloneable & Runnable & Closeable> extends Shop<T> {
  C getSim(String name, String id);
}
