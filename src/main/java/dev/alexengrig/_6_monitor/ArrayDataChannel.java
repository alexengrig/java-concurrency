package dev.alexengrig._6_monitor;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("DuplicatedCode")
public class ArrayDataChannel implements DataChannel {

    private final int buffer = 8;
    private final List<String> datum = new ArrayList<>(buffer);

    @Override
    public synchronized String receive() throws InterruptedException {
        while (datum.isEmpty()) {
            try {
                System.out.println("Wait receiving");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new InterruptedException("Receiving interrupted!");
            }
        }
        String returnData = datum.remove(datum.size() - 1);
        notifyAll();
        return returnData;
    }

    @Override
    public synchronized void send(String data) throws InterruptedException {
        while (datum.size() >= buffer) {
            try {
                System.out.println("Wait sending");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new InterruptedException("Sending '" + data + "' interrupted!");
            }
        }
        datum.add(data);
        notifyAll();
    }

}
