package com.example.rs485toandroid.core.models;

import java.util.Arrays;

/**
 * Класс команды для отправки через последовательное соединение.
 * Инкапсулирует данные команды и параметры ее выполнения.
 */
public class Command {
    private byte[] data;
    private long timeout;
    private int retryCount;
    private int priority;

    /**
     * Создает команду с указанными параметрами.
     *
     * @param data данные команды для отправки
     * @param timeout таймаут ожидания ответа в миллисекундах
     * @param retryCount количество попыток повторной отправки при ошибке
     * @param priority приоритет команды в очереди (меньше число = выше приоритет)
     */
    public Command(byte[] data, long timeout, int retryCount, int priority) {
        this.data = data;
        this.timeout = timeout;
        this.retryCount = retryCount;
        this.priority = priority;
    }

    /**
     * Создает команду с параметрами по умолчанию.
     * Таймаут: 1000 мс, попытки: 3, приоритет: 0 (наивысший)
     *
     * @param data данные команды для отправки
     */
    public Command(byte[] data) {
        this(data, 1000, 3, 0);
    }

    /**
     * Возвращает данные команды.
     *
     * @return массив байтов данных команды
     */
    public byte[] getData() { return data; }

    /**
     * Возвращает таймаут ожидания ответа.
     *
     * @return таймаут в миллисекундах
     */
    public long getTimeout() { return timeout; }

    /**
     * Возвращает количество попыток повторной отправки.
     *
     * @return количество попыток
     */
    public int getRetryCount() { return retryCount; }

    /**
     * Возвращает приоритет команды.
     *
     * @return приоритет команды
     */
    public int getPriority() { return priority; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return Arrays.equals(data, command.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
