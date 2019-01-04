package com.ll.spring.boot.socket.server.nio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {

	private ExecutorService executorService;

	public ThreadPool(int maxPoolSize, int queueSize) {
		this.executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxPoolSize, 120L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueSize));
	}
	
	public void execute (Runnable task) {
		executorService.execute(task);
	}
}
