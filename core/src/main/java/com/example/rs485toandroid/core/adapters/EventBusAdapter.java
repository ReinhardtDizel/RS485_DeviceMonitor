package com.example.rs485toandroid.core.adapters;

import com.example.rs485toandroid.core.events.EventBus;
import com.example.rs485toandroid.core.interfaces.IEventPublisher;

public class EventBusAdapter implements IEventPublisher {
    @Override
    public void publish(Object event) {
        EventBus.getInstance().publish(event);
    }
}