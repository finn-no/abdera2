package org.apache.abdera2.common.misc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.MoreExecutors;

public class MoreExecutors2 {

  public static ExecutorService getExitingExecutor() {
    ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    return MoreExecutors.getExitingExecutorService(tpe);
  }
  
  public static ExecutorService getExitingExecutor(ThreadFactory factory) {
    ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newCachedThreadPool(factory);
    return MoreExecutors.getExitingExecutorService(tpe);
  }
  
  public static ExecutorService getExitingFixedExecutor(int n) {
    ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newFixedThreadPool(n);
    return MoreExecutors.getExitingExecutorService(tpe);
  }
  
  public static ExecutorService getExitingFixedExecutor(int n, ThreadFactory factory) {
    ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newFixedThreadPool(n,factory);
    return MoreExecutors.getExitingExecutorService(tpe);
  }
  
  
  public static ExecutorService getExitingExecutor(int timeout, TimeUnit unit) {
    ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    return MoreExecutors.getExitingExecutorService(tpe,timeout,unit);
  }
  
  public static ExecutorService getExitingExecutor(ThreadFactory factory,int timeout, TimeUnit unit) {
    ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newCachedThreadPool(factory);
    return MoreExecutors.getExitingExecutorService(tpe,timeout,unit);
  }
  
  public static ExecutorService getExitingFixedExecutor(int n,int timeout, TimeUnit unit) {
    ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newFixedThreadPool(n);
    return MoreExecutors.getExitingExecutorService(tpe,timeout,unit);
  }
  
  public static ExecutorService getExitingFixedExecutor(int n, ThreadFactory factory,int timeout, TimeUnit unit) {
    ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newFixedThreadPool(n,factory);
    return MoreExecutors.getExitingExecutorService(tpe,timeout,unit);
  }
  
}
