package com.example.rs485toandroid.connection.utils;

import com.example.rs485toandroid.connection.interfaces.ConnectionManager;
import com.example.rs485toandroid.connection.models.Command;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class CommandQueue {
    private final PriorityBlockingQueue<Command> queue;
    private final ConnectionManager connectionManager;
    private boolean isProcessing = false;

    public CommandQueue(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.queue = new PriorityBlockingQueue<>(11, Comparator.comparingInt(Command::getPriority));
    }

    public void addCommand(Command command) {
        queue.offer(command);
        processNext();
    }

    private void processNext() {
        if (isProcessing || queue.isEmpty() || !connectionManager.isConnected()) {
            return;
        }

        isProcessing = true;
        Command command = queue.poll();

        try {
            connectionManager.sendData(command.getData());
            // TODO: Implement timeout and retry logic
        } catch (Exception e) {
            // Handle error
        } finally {
            isProcessing = false;
            processNext();
        }
    }
}