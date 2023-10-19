package dev.alexengrig._6_monitor;

public class SingleDataChannel implements DataChannel {

    private String data;

    @Override
    public synchronized String receive() throws InterruptedException {
        while (this.data == null) {
            try {
                System.out.println("Wait receiving");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new InterruptedException("Receiving interrupted!");
            }
        }
        String returnData = this.data;
        this.data = null;
        notifyAll();
        return returnData;
    }

    @Override
    public synchronized void send(String data) throws InterruptedException {
        while (this.data != null) {
            try {
                System.out.println("Wait sending");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new InterruptedException("Sending '" + data + "' interrupted!");
            }
        }
        this.data = data;
        notifyAll();
    }

}
