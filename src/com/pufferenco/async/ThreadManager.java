package com.pufferenco.async;

import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * allows code snippets to execute asynchronously while your main code is working
 */
public class ThreadManager {
   private final Object ThreadMonitor = new Object();
   private final threader[] threads;
   private final LinkedBlockingDeque<Promise> Tasks = new LinkedBlockingDeque<>();
   private final LinkedList<Promise<?>> ids = new LinkedList<>();

   /**
    * creates a thread-manager for easy code snippets
    * useful for loading and writing files
    * @param name the naming of the Threads
    * @param Threads the amount of threads
    */
   public ThreadManager(String name,int Threads){
      threads = new threader[Threads];
      for (int i = 0; i < threads.length; i++) {
         threads[i] = new threader();
         threads[i].activeThread = new Thread(threads[i]);
         threads[i].activeThread.setName(name+"("+i+")");
         threads[i].activeThread.start();
      }
   }
   /**
    * creates a thread-manager for easy code snippets
    * useful for loading and writing files
    * same as running ThreadManager(name,2)
    * @param name the naming of the Threads
    */
   public ThreadManager(String name){
      this(name,2);
   }
   /**
    * creates a thread-manager for easy code snippets
    * useful for loading and writing files
    * same as running ThreadManager("async",2)
    */
   public ThreadManager(){
      this("async",2);
   }

   /**
    * puts the function on the queue
    * O(1)
    * @param function the function to run async
    * @return a Promise which can be read once required
    */
   public <V> Promise<V> exec(Callable<V> function){
      Promise<V> task = new Promise<>();
      task.task = function;
      Tasks.add(task);

      synchronized (ThreadMonitor) {
         ThreadMonitor.notifyAll();
      }

      return task;
   }

   /**
    * runs execute but with an id which you can put in the ThreadManager.get(int)
    * method to get the Promise without needing a variable
    *
    * @param function the function to use
    * @param id       the id to use
    */
   public <V> void exec(Callable<V> function, int id) {
      ids.add(id, exec(function));
   }

   public <V> Promise<V> get(int id) {
      return (Promise<V>) ids.get(id);
   }

   /**
    * Finishes the queue
    * and then close all threads.
    */
   public void Finish() {
      for (threader thread : threads) {
         thread.Finish = true;
      }
      synchronized (ThreadMonitor) {
         ThreadMonitor.notifyAll();
      }
   }

   private class threader implements Runnable{
      volatile boolean Finish = false;
      Thread activeThread;

      @Override
      public void run() {
         while (true) {
            try {
               Promise e = Tasks.pollFirst();

               if (e != null) {
                  e.Complete(e.task.call());
                  continue;
               }

               if (Finish)
                  break;

               synchronized (ThreadMonitor) {
                  ThreadMonitor.wait();
               }

            }catch (Exception exception) {throw new RuntimeException(exception);}
            }
         }
      }
   }

