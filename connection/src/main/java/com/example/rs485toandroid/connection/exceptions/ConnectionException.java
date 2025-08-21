package com.example.rs485toandroid.connection.exceptions;

/**
 * Базовое исключение для ошибок соединения.
 * Служит родительским классом для всех специализированных исключений соединения.
 */
public class ConnectionException extends Exception {
    /**
     * Создает исключение с указанным сообщением об ошибке.
     *
     * @param message описание ошибки
     */
    public ConnectionException(String message) {
        super(message);
    }
}

/**
 * Исключение, возникающее при попытке подключения к неподдерживаемому устройству.
 */
class DeviceNotSupportedException extends ConnectionException {
    /**
     * Создает исключение с указанным сообщением об ошибке.
     *
     * @param message описание ошибки
     */
    public DeviceNotSupportedException(String message) {
        super(message);
    }
}

/**
 * Исключение, возникающее при отсутствии разрешения на доступ к USB-устройству.
 */
class PermissionDeniedException extends ConnectionException {
    /**
     * Создает исключение с указанным сообщением об ошибке.
     *
     * @param message описание ошибки
     */
    public PermissionDeniedException(String message) {
        super(message);
    }
}

/**
 * Исключение, возникающее при превышении времени ожидания операции соединения.
 */
class ConnectionTimeoutException extends ConnectionException {
    /**
     * Создает исключение с указанным сообщением об ошибке.
     *
     * @param message описание ошибки
     */
    public ConnectionTimeoutException(String message) {
        super(message);
    }
}