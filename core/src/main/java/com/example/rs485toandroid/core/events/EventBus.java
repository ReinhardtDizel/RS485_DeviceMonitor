package com.example.rs485toandroid.core.events;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
    private static EventBus instance;
    private final Map<Class<?>, CopyOnWriteArrayList<EventListener>> listeners = new HashMap<>();

    public static synchronized EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    public <T> void subscribe(Class<T> eventType, EventListener<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
                .add(listener);
    }

    public <T> void unsubscribe(Class<T> eventType, EventListener<T> listener) {
        if (listeners.containsKey(eventType)) {
            listeners.get(eventType).remove(listener);
        }
    }

    public <T> void publish(T event) {
        Class<?> eventType = event.getClass();
        if (listeners.containsKey(eventType)) {
            for (EventListener listener : listeners.get(eventType)) {
                listener.onEvent(event);
            }
        }
    }

    public interface EventListener<T> {
        void onEvent(T event);
    }
}