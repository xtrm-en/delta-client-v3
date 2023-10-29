package me.xtrm.delta.client.utils;

public class TimeHelper {
	
	private long lastMS = 0L;
	
	public long getCurrentMS() {
		return System.nanoTime() / 1000000L;
	}
	
	public long getDiff() {
		return getCurrentMS() - lastMS;
	}
	
	public void setLastMS(long lastMS) {
		this.lastMS = System.currentTimeMillis();
	}
	
	public int convertToMS(int perSecond) {
		return 1000/perSecond;
	}
	
	public boolean hasReached(long milliseconds) {
		return getDiff() >= milliseconds;
	}
	
	public void reset() {
		lastMS = getCurrentMS();
	}
	
	public void reset(long offset) {
		lastMS = getCurrentMS() - offset;
	}
}
