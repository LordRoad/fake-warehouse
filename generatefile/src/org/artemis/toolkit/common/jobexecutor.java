/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.artemis.toolkit.common;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * jobexecutor simple wrapper for executor
 * jobexecutor.java is written at Jun 19, 2014
 * @author junli
 * @since 0.2
 */
public class jobexecutor {
	private static final Logger LOG = LoggerFactory.getLogger(jobexecutor.class);
	
	private ExecutorService mExecutorService;
	
	public jobexecutor() {
		mExecutorService = //Executors.newCachedThreadPool();
				Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
		LOG.info("executor fixed threads count: " + Integer.toString(Runtime.getRuntime().availableProcessors() * 2));
	}
	
	/**
	 * @throws 
	 * 		RejectedExecutionException - if this task cannot be accepted for execution.
	 * 		NullPointerException - if command is null
	 */
	public void execute(Runnable iJob) {
		mExecutorService.execute(iJob);
	}
	
	/**
	 * @see java.util.concurrent.ExecutorService.submit
	 * @throws 
	 * 		RejectedExecutionException - if this task cannot be accepted for execution.
	 * 		NullPointerException - if command is null
	 */
	public Future<?> submit(Runnable iJob) {
		return mExecutorService.submit(iJob);
	}
	
	public void shutdown() {
		mExecutorService.shutdown();
	}
	
	public void shutdownnow() {
		List<Runnable> lRunnableSet = mExecutorService.shutdownNow();
		LOG.warn("unrunning jobs: ");
		for (Runnable iRunnable : lRunnableSet) {
			LOG.warn(iRunnable.toString());
		}
	}
	
	public boolean wait(long timeout, TimeUnit unit) {
		try {
			mExecutorService.shutdown(); // stop receiving new tasks
			return mExecutorService.awaitTermination(timeout, unit);
		} catch (InterruptedException e) {
			LOG.warn(e.getLocalizedMessage());
		}
		return false;
	}
	
	public boolean isShutdown() {
		return mExecutorService.isShutdown();
	}
	
	/**
	 * Returns true if all tasks have completed following shut down. 
	 * Note that isTerminated is never true unless either shutdown or shutdownNow was called first.
	 * @see java.util.concurrent.ExecutorService.isTerminated()
	 */
	public boolean isTerminated() {
		return mExecutorService.isTerminated();
	}
	
}
