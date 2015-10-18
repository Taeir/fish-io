package com.github.fishio;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Class that can be used to run tasks multithreaded.
 */
public final class MultiThreadedUtility {
	private static ThreadPoolExecutor longExecutor =
			new ThreadPoolExecutor(4, 4, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	private static ThreadPoolExecutor shortExecutor =
			new ThreadPoolExecutor(12, 12, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	
	static {
		longExecutor.allowCoreThreadTimeOut(true);
		shortExecutor.allowCoreThreadTimeOut(true);
	}
	
	private MultiThreadedUtility() { }
	
	/**
	 * Submits the given runnable task, and returns a Future that can be used
	 * to check when it is done.
	 * 
	 * @param runnable
	 * 		the runnable task to submit.
	 * @param isShort
	 * 		<code>true</code> to submit this as a short-lived task.
	 * 
	 * @return
	 * 		a Future that can be used to wait until the task is done.
	 */
	public static Future<?> submitTask(Runnable runnable, boolean isShort) {
		if (isShort) {
			return shortExecutor.submit(runnable);
		} else {
			return longExecutor.submit(runnable);
		}
	}
	
	/**
	 * Submits the given runnable task, and returns a Future that can be used
	 * to check when it is done.
	 * 
	 * @param <T>
	 * 		the type of the callable.
	 * @param callable
	 * 		the callable task to submit.
	 * @param isShort
	 * 		<code>true</code> to submit this as a short-lived task.
	 * 
	 * @return
	 * 		a Future that can be used to get the result and to wait until
	 * 		the task is done.
	 */
	public static <T> Future<T> submitTask(Callable<T> callable, boolean isShort) {
		if (isShort) {
			return shortExecutor.submit(callable);
		} else {
			return longExecutor.submit(callable);
		}
	}
}
