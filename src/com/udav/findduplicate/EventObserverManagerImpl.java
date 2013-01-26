package com.udav.findduplicate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

public class EventObserverManagerImpl implements EventObserverManager {
	private final Map<Class, Set<EventObserver>> observerMap = new HashMap<Class, Set<EventObserver>>();
	
	@Override
	public void registerEventObserver(Class subject, EventObserver eventObserver) {
		getObserverSet(subject).add(eventObserver);
	}
	@Override
	public void remoteEventObserver(Class subject, EventObserver eventObserver) {
		remoteEventObserver(subject, eventObserver);
	}
	@Override
	public void notifyAll(Class subject, int pos) {
		for (EventObserver eventObserver : observerMap.get(subject)) {
			eventObserver.event(pos);
		}
	}
	private Set<EventObserver> getObserverSet(Class subject) {
		Set<EventObserver> result = observerMap.get(subject);
		if (result == null) {
            result = new HashSet<EventObserver>();
            observerMap.put(subject, result);
        }
		return result;
	}
	private void remoteObserver(Class subject, EventObserver eventObserver) {
		Set<EventObserver> observerSet = observerMap.get(subject);
		if (observerSet.contains(eventObserver)){
			observerSet.remove(eventObserver);
		}
	}

}
