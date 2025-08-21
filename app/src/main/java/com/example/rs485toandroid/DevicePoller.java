package com.example.rs485toandroid;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Класс для организации периодического опроса устройства
 */
public class DevicePoller {

    /**
     * Интерфейс слушателя событий опроса
     */
    public interface PollingListener {
        /**
         * Вызывается когда требуется выполнить опрос устройства
         */
        void onPollingRequired();
    }

    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> pollingFuture;
    private PollingListener listener;

    /**
     * Запускает периодический опрос устройства
     * @param listener Слушатель событий опроса
     * @param intervalMs Интервал опроса в миллисекундах
     */
    public void startPolling(PollingListener listener, int intervalMs) {
        this.listener = listener;

        // Останавливаем предыдущий опрос, если он был
        stopPolling();

        // Создаем новый планировщик
        scheduler = Executors.newSingleThreadScheduledExecutor();

        // Запускаем периодический опрос
        pollingFuture = scheduler.scheduleWithFixedDelay(() -> {
            if (listener != null) {
                listener.onPollingRequired();
            }
        }, 0, intervalMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Останавливает периодический опрос
     */
    public void stopPolling() {
        if (pollingFuture != null) {
            pollingFuture.cancel(true);
            pollingFuture = null;
        }

        if (scheduler != null) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
            scheduler = null;
        }
    }
}