package com.pufferenco.async;

import java.util.concurrent.Callable;

public class Task {
   Callable<Object> task;
   boolean isFinished =false;
   Object returnValue=null;

   void Complete(Object value){
      synchronized (this){
         isFinished = true;
         returnValue = value;
      }
   }
   public boolean isFinished(){
      synchronized (this){
         return isFinished;
      }
   }

   public Object await(int queryCoolDown){
      while(true) {
         synchronized (this) {
            if (isFinished)
               return returnValue;
         }
         try {
            Thread.sleep(queryCoolDown);
         } catch (InterruptedException e) {
            throw new RuntimeException(e);
         }
      }
   }
   public Object await(){
      return await(2);
   }
}
