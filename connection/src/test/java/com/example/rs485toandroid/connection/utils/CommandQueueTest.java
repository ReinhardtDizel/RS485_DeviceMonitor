// CommandQueueTest.java
package com.example.rs485toandroid.connection.utils;

import com.example.rs485toandroid.core.interfaces.IConnectionManager;
import com.example.rs485toandroid.core.models.Command;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommandQueueTest {

    @Mock
    private IConnectionManager mockConnectionManager;

    private CommandQueue commandQueue;

    @Before
    public void setUp() {
        commandQueue = new CommandQueue(mockConnectionManager);
    }

    @Test
    public void testAddCommand() {
        Command command = new Command(new byte[]{0x01, 0x02}, 1, 1000, 3);

        commandQueue.addCommand(command);

        assertEquals(1, commandQueue.getQueueSize());
    }

    @Test
    public void testClearQueue() {
        Command command = new Command(new byte[]{0x01, 0x02}, 1, 1000, 3);

        commandQueue.addCommand(command);
        commandQueue.clearQueue();

        assertEquals(0, commandQueue.getQueueSize());
    }

    @Test
    public void testProcessCommandWhenConnected() {
        when(mockConnectionManager.isConnected()).thenReturn(true);
        Command command = new Command(new byte[]{0x01, 0x02}, 1, 1000, 3);

        commandQueue.addCommand(command);

        // Даем время на обработку
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        verify(mockConnectionManager).sendData(command.getData());
    }

    @Test
    public void testShutdown() {
        commandQueue.shutdown();

        // Проверяем, что исключений не было
        assertTrue(true);
    }

    // Вспомогательный метод для проверки состояния
    private static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError();
        }
    }
}