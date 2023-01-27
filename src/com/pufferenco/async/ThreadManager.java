package com.pufferenco.async;

import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * allows code snippets to execute asynchronously while your main code is working
 */
public class ThreadManager {
   private final threader[] threads;
   private final LinkedBlockingDeque<asyncPromise> Tasks = new LinkedBlockingDeque<>();
   final String name;
   private final int queryCoolDown;

   /**
    * creates a thread-manager for easy code snippets
    * useful for loading and writing files
    * @param name the naming of the Threads
    * @param Threads the amount of threads
    * @param queryCoolDown the coolDown each thread waits when no task is found on the stack
    */
   public ThreadManager(String name,int Threads,int queryCoolDown){
      this.name = name;
      this.queryCoolDown = queryCoolDown;
      threads = new threader[Threads];
      for (int i = 0; i < threads.length; i++) {
         threads[i] = new threader();
         threads[i].activeThread = new Thread(threads[i]);
         threads[i].activeThread.setName(name+"("+i+")");
         threads[i].activeThread.start();
      }
   }
   public ThreadManager(String name,int Threads){
      this(name,Threads,0);
   }
   public ThreadManager(String name){
      this(name,2,0);
   }
   public ThreadManager(){
      this("async",2,0);
   }

   /**
    * puts the function on the stack to be executed
    * Last in First out (LiFo)
    * O(1)
    * @param function the function to run async
    * @return a asyncPromise which can be read once required
    */
   public asyncPromise exec(Callable<Object> function){
      asyncPromise task = new asyncPromise();
      task.task = function;
      Tasks.add(task);
      return task;
   }


   /**
    * Finishes the current worked tasks
    * and then stop all threads.
    * Beware: does not finish the stack
    */
   public void stop(){
      for (threader thread : threads) {
         thread.Cancel.set(true);
      }
   }

   private class threader implements Runnable{

      volatile AtomicBoolean Cancel = new AtomicBoolean(false);
      Thread activeThread;
      @Override
      public void run() {
         while (!Cancel.get()) {
            try {
               asyncPromise e = Tasks.pollFirst();
               if(e == null) {
                  Thread.sleep(queryCoolDown);
                  continue;
               }

               Object output = e.task.call();
               e.Complete(output);

            } catch (InterruptedException ignore){
            }catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }
   }
}
