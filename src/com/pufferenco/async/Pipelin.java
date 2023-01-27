package com.pufferenco.async;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Function;

public class Pipelin {
   private final Runner[] pieces;
   @SafeVarargs
   public Pipelin(long queryCoolDown, Function<Object,Object>... pipes){
      pieces = new Runner[pipes.length];
      for (int i = 0; i < pipes.length; i++) {
         Runner runner = new Runner();
         pieces[i] = runner;
         runner.id = i;
         runner.Task = pipes[i];
         runner.queryCoolDown = queryCoolDown;
      }
      for (Runner piece : pieces) {
         piece.thread = new Thread(piece);
         piece.thread.start();
      }
      pieces[pieces.length-1].isLast = true;
   }

   public pipelinePromise input(Object param){
      pipelinePromise e = new pipelinePromise();
      e.current = param;
      pieces[0].Heap.add(e);
      return e;
   }

   public void Finish(long queryCoolDown){
      for (Runner piece : pieces) {
         if (!piece.Heap.isEmpty())
            try {Thread.sleep(queryCoolDown);break;}
            catch (InterruptedException e) {throw new RuntimeException(e);}

         synchronized (piece.FinishLock){
               piece.shouldFinish = true;
            }
      }
   }

   private class Runner implements Runnable{
      final Object FinishLock = new Object();
      Boolean shouldFinish = false;
      long queryCoolDown;
      boolean isLast = false;
      Thread thread;
      int id;
      Function<Object,Object> Task;
      LinkedBlockingDeque<pipelinePromise> Heap = new LinkedBlockingDeque<>();

      @Override
      public void run() {
         while(true){
            if(Heap.isEmpty()){
               synchronized (FinishLock){if(shouldFinish)break;}

               try {Thread.sleep(queryCoolDown);}
               catch (InterruptedException e) {throw new RuntimeException(e);}
               continue;
            }
            pipelinePromise e = Heap.remove();
            e.current = Task.apply(e.current);

            if(isLast)
               e.Complete();
            else
               pieces[id+1].Heap.add(e);
         }
      }
   }



}
