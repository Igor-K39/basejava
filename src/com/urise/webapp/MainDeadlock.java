package com.urise.webapp;

public class MainDeadlock {
    private static final Object LOCKER_1 = new Object();
    private static final Object LOCKER_2 = new Object();

    public static void main(String[] args) {
        new DeadLocker(true).start();
        new DeadLocker(false).start();
    }

    private static class DeadLocker extends Thread {
        private final boolean reverse;

        DeadLocker(boolean reverse) {
            this.reverse = reverse;
        }

        @Override
        public void run() {
            if (reverse) {
                doLock();
            } else {
                doAnotherLock();
            }
        }

        private void doLock() {
            synchronized (LOCKER_1) {
                System.out.println(Thread.currentThread().getName() + " has locked on LOCKER_1");
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (LOCKER_2) {
                    System.out.println(Thread.currentThread().getName() + ": it doesn't work");
                }
            }
        }

        private void doAnotherLock() {
            synchronized (LOCKER_2) {
                System.out.println(Thread.currentThread().getName() + " has locked on LOCKER_2");

                synchronized (LOCKER_1) {
                    System.out.println(Thread.currentThread().getName() + ": it doesn't work");
                }
            }

        }
    }
}
