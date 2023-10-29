package me.xtrm.xeon.loader.update;

public interface IUpdaterCallback {
	
	void start();
	void setMax(int size);
	int getCurrent();
	void setCurrent(int curr);
	void finished();

}
