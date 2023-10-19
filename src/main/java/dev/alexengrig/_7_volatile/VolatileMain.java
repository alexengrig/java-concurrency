package dev.alexengrig._7_volatile;

public class VolatileMain {

    private static int number;
    private static boolean ready;

    private static class Reader extends Thread {

        @Override
        public void run() {
            while (!ready) {
                Thread.yield();
            }

            System.out.println(number);
        }
    }

    public static void main(String[] args) {
        new Reader().start();
        number = 100500;
        ready = true;

// happens-before
//      while (!ready) {
//          Thread.yield();
//      }
//      System.out.println(number);
    }

}
