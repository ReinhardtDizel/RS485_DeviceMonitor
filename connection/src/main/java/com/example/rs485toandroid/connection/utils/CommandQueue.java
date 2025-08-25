package com.example.rs485toandroid.connection.utils;

import com.example.rs485toandroid.core.interfaces.IConnectionManager;
import com.example.rs485toandroid.core.models.Command;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CommandQueue {
    private final PriorityBlockingQueue<Command> queue;
    private final IConnectionManager connectionManager;
    private final ScheduledExecutorService executorService;
    private boolean isProcessing = false;
    private Command currentCommand;
    private int retryCount = 0;

    public CommandQueue(IConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.queue = new PriorityBlockingQueue<>(11,
                Comparator.comparingInt(Command::getPriority).reversed());
        this.executorService = Executors.newSingleThreadScheduledExecutor();

        // Запускаем обработчик очереди
        startQueueProcessor();
    }

    public void addCommand(Command command) {
        queue.offer(command);
        processNext();
    }

    public void clearQueue() {
        queue.clear();
    }

    public int getQueueSize() {
        return queue.size();
    }

    private void startQueueProcessor() {
        executorService.scheduleAtFixedRate(this::processNext, 0, 100, TimeUnit.MILLISECONDS);
    }

    private void processNext() {
        if (isProcessing || queue.isEmpty() || !connectionManager.isConnected()) {
            return;
        }

        isProcessing = true;
        currentCommand = queue.poll();
        retryCount = 0;

        sendCommandWithRetry();
    }

    private void sendCommandWithRetry() {
        if (currentCommand == null) {
            isProcessing = false;
            return;
        }

        try {
            connectionManager.sendData(currentCommand.getData());

            // TODO: Здесь нужно добавить логику ожидания ответа
            // Пока просто имитируем успешную отправку
            handleCommandSuccess();

        } catch (Exception e) {
            handleCommandError(e);
        }
    }

    private void handleCommandSuccess() {
        // Команда успешно отправлена
        currentCommand = null;
        isProcessing = false;
        retryCount = 0;
    }

    private void handleCommandError(Exception e) {
        retryCount++;

        if (retryCount >= currentCommand.getRetryCount()) {
            // Превышено количество попыток
            System.err.println("Command failed after " + retryCount + " attempts: " + e.getMessage());
            currentCommand = null;
            isProcessing = false;
            retryCount = 0;
        } else {
            // Повторная попытка после таймаута
            long delay = currentCommand.getTimeout() * retryCount;
            executorService.schedule(this::sendCommandWithRetry, delay, TimeUnit.MILLISECONDS);
        }
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}