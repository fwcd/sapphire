package com.fwcd.sapphire.utils;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

public class TaskQueue {
	private final Queue<Runnable> taskQueue = new ArrayDeque<>();
	
	public void offer(Runnable task) {
		taskQueue.offer(task);
	}
	
	public <T> Future<T> offer(Callable<T> task) {
		RunnableFuture<T> result = new FutureTask<>(task);
		taskQueue.offer(result);
		return result;
	}
	
	public boolean pollAndRun() {
		if (taskQueue.isEmpty()) {
			return false;
		} else {
			taskQueue.poll().run();
			return true;
		}
	}
	
	public void pollAndRunAll() {
		while (!taskQueue.isEmpty()) {
			taskQueue.poll().run();
		}
	}
}
