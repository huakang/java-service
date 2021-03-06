package org.gearman.impl.worker;

/**
 * A funky mechanism that executes a task when there are zero threads in the critical
 * code block. Where this is used, the critical code block is not a single block of
 * code. The the thread that calls "lock" may not be the same thread that calls "unlock,"
 * so we need to be absolute sure there are equal number of "lock" calls as "unlock"
 * calls.
 * 
 * 
 * @author isaiah van der elst
 */
class ZeroLock {
	private int count = 0;
	private final Runnable task;
	
	public ZeroLock(Runnable task) {
		this.task = task;
	}
	
	public synchronized void reset() {
		count=0;
	}
	
	public synchronized void lock() {
		count++;
	}
	
	public boolean isLocked() {
		return count>0;
	}
	
	public synchronized void unlock() {
		count--;
		assert count>=0;
		runIfNotLocked();
	}
	
	public synchronized void runIfNotLocked() {
		if(count==0) task.run();
	}
}
