package dev.alexengrig._6_monitor;

public interface DataChannel {

    String receive() throws InterruptedException;

    void send(String data) throws InterruptedException;

}
