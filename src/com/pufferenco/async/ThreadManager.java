package com.pufferenco.async;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * allows code snippets to execute while your main code is working
 */
public class ThreadManager {
   private final threader[] threads;
   private final Stack<Task> Tasks = new Stack<>();
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

   public Task exec(Callable<Object> function){
      Task task = new Task();
      task.task = function;
      Tasks.add(task);
      return task;
   }


   public synchronized void stop(){
      for (threader thread : threads) {
         thread.Cancel.set(true);
      }
   }

   private class threader implements Runnable{

      private final AtomicBoolean Cancel = new AtomicBoolean(false);
      Thread activeThread;
      @Override
      public void run() {
         while (!Cancel.get()) {
            try {
               if(Tasks.isEmpty())
                  Thread.sleep(queryCoolDown);
               else {
                  Task e = Tasks.pop();
                  Object output = e.task.call();
                  e.Complete(output);
               }

            } catch (InterruptedException ignore){

            }catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }
   }
}
