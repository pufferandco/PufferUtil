package com.pufferenco.async;

import java.util.concurrent.Callable;

public class pipelinePromise {
   Object current;
   volatile boolean isFinished =false;
   Object returnValue=null;

   void Complete(){

      isFinished = true;

   }
   public boolean isFinished(){
      synchronized (this){
         return isFinished;
      }
   }

   public Object await(int queryCoolDown){
      while(true) {
            if (isFinished)
               return returnValue;

      }
   }
   public Object await(){
      return await(2);
   }
}
