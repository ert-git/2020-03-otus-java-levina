package ru.otus.edu.numbers;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class App {
    private static final int PAUSE_BASE = 500;
    private static final int MAX = 10;
    private static final int MIN = 0;

    private static class PrintingThread extends Thread {

        private final BlockingQueue<Boolean> readQueue;
        private final BlockingQueue<Boolean> writeQueue;
        private final String name;
        private int current;
        private boolean grow = true;

        public PrintingThread(BlockingQueue<Boolean> readQueue, BlockingQueue<Boolean> writeQueue, String name) {
            super();
            this.readQueue = readQueue;
            this.writeQueue = writeQueue;
            this.name = name;
            this.current = MIN;
        }

        public void run() {
            try {
                while (true) {
                    boolean flag = readQueue.take();
                    if (!flag) {
                        return;
                    }
                    if (current == MAX && grow) {
                        grow = false;
                    } else if (current == MIN && !grow) {
                        grow = true;
                    }
                    System.out.println(name + ": " + (grow ? current++ : current--));
                    Thread.sleep(Math.round(Math.random() * PAUSE_BASE + PAUSE_BASE));
                    writeQueue.put(true);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Boolean> q1 = new ArrayBlockingQueue<>(1);
        BlockingQueue<Boolean> q2 = new ArrayBlockingQueue<>(1);
        q1.put(true);
        new PrintingThread(q1, q2, "t1").start();
        new PrintingThread(q2, q1, "t2").start();
    }
}