package com.example.rs485toandroid.connection.models;

/**
 * Класс конфигурации соединения.
 * Содержит параметры для настройки последовательного порта.
 */
public class ConnectionConfig {
    private int baudRate;
    private int dataBits;
    private int stopBits;
    private int parity;
    private int flowControl;

    /**
     * Создает конфигурацию соединения с параметрами по умолчанию.
     * По умолчанию: 9600 бод, 8 бит данных, 1 стоп-бит, нет четности, нет контроля потока.
     */
    public ConnectionConfig() {
        this(9600, 8, 1, 0, 0);
    }

    /**
     * Создает конфигурацию соединения с указанными параметрами.
     *
     * @param baudRate скорость передачи данных в бодах
     * @param dataBits количество битов данных (5, 6, 7 или 8)
     * @param stopBits количество стоп-битов (1 или 2)
     * @param parity контроль четности (0 = нет, 1 = нечет, 2 = чет)
     * @param flowControl контроль потока (0 = нет, 1 = RTS/CTS, 2 = XON/XOFF)
     */
    public ConnectionConfig(int baudRate, int dataBits, int stopBits, int parity, int flowControl) {
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
        this.flowControl = flowControl;
    }

    /**
     * Возвращает скорость передачи данных.
     *
     * @return скорость в бодах
     */
    public int getBaudRate() { return baudRate; }

    /**
     * Устанавливает скорость передачи данных.
     *
     * @param baudRate скорость в бодах
     */
    public void setBaudRate(int baudRate) { this.baudRate = baudRate; }

    /**
     * Возвращает количество битов данных.
     *
     * @return количество битов данных
     */
    public int getDataBits() { return dataBits; }

    /**
     * Устанавливает количество битов данных.
     *
     * @param dataBits количество битов данных (5, 6, 7 или 8)
     */
    public void setDataBits(int dataBits) { this.dataBits = dataBits; }

    /**
     * Возвращает количество стоп-битов.
     *
     * @return количество стоп-битов
     */
    public int getStopBits() { return stopBits; }

    /**
     * Устанавливает количество стоп-битов.
     *
     * @param stopBits количество стоп-битов (1 или 2)
     */
    public void setStopBits(int stopBits) { this.stopBits = stopBits; }

    /**
     * Возвращает тип контроля четности.
     *
     * @return тип контроля четности
     */
    public int getParity() { return parity; }

    /**
     * Устанавливает тип контроля четности.
     *
     * @param parity тип контроля четности (0 = нет, 1 = нечет, 2 = чет)
     */
    public void setParity(int parity) { this.parity = parity; }

    /**
     * Возвращает тип контроля потока.
     *
     * @return тип контроля потока
     */
    public int getFlowControl() { return flowControl; }

    /**
     * Устанавливает тип контроля потока.
     *
     * @param flowControl тип контроля потока (0 = нет, 1 = RTS/CTS, 2 = XON/XOFF)
     */
    public void setFlowControl(int flowControl) { this.flowControl = flowControl; }
}