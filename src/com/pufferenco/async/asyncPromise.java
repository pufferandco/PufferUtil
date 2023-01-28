package com.pufferenco.async;

import java.util.concurrent.Callable;

public class asyncPromise {
   Thread Caller;
   Callable<Object> task;
   volatile boolean isFinished =false;
   Object returnValue=null;

   void Complete(Object value){
      returnValue = value;
      isFinished = true;
   }
   public boolean isFinished(){
      return isFinished;
   }

   public Object await(int queryCoolDown){
      while(true) {
            if (isFinished)
               return returnValue;
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
