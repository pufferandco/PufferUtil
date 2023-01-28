package com.pufferenco.async;

import java.util.concurrent.Callable;

public class Promise<V> {
   Callable<V> task;
   final private Object Lock = new Object();
   private volatile boolean isFinished =false;
   private V returnValue=null;

   void Complete(V value){
      returnValue = value;
      isFinished = true;
      synchronized (Lock){
         Lock.notify();
      }
   }
   public boolean isFinished(){
      return isFinished;
   }

   public V await(){
      try{
         if (!isFinished) {
            synchronized (Lock) {
               Lock.wait();
            }
         }
         return returnValue;
      } catch (InterruptedException e) {throw new RuntimeException(e);}
   }
}
