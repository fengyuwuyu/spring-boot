package com.ll.spring.boot.core.util;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 用于执行耗时任务
 * @author lilei
 *
 */
public class ThreadPool {

	private static ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 2);
	private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() / 2);
	
	public static void execute(Runnable task, long delay, TimeUnit unit){
		unit = unit == null ? TimeUnit.MILLISECONDS : unit;
		scheduledExecutorService.schedule(task, delay, unit);
	}
	
	public static void execute(Runnable task, long initialDelay, long delay, TimeUnit unit){
		unit = unit == null ? TimeUnit.MILLISECONDS : unit;
		scheduledExecutorService.scheduleWithFixedDelay(task, initialDelay, delay, unit);
	}
	
	public static void execute(Runnable task){
		service.execute(task);
	}
	
	public static Future<Object> execute(Callable<Object> task){
		return service.submit(task);
	}
	
	/**
	 * 阻塞主线程直到传入的tasks全部完成再继续
	 * @param tasks
	 */
	public static void synchronizedExecute(Runnable [] tasks){
		if(tasks==null||tasks.length==0){
			return;
		}
		final CountDownLatch countDownLatch = new CountDownLatch(tasks.length);
		for(final Runnable task : tasks){
			service.execute(new Runnable() {
				
				public void run() {
					task.run();
					countDownLatch.countDown();
				}
			});
		}
		try {
			//等待任务全部完成
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
