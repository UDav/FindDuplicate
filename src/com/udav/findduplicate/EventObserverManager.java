package com.udav.findduplicate;

public interface EventObserverManager {
	void registerEventObserver(Class subject, EventObserver eventObserver);
	void remoteEventObserver(Class subject, EventObserver eventObserver);
	void notifyAll(Class subject, int pos);
}
