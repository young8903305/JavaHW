package apjp2016;

import java.util.concurrent.locks.*;

public class HW51 {

	private int x, y;
	private boolean available = false; // condition var
	private Lock lock = new ReentrantLock();
	private Condition threadCond = lock.newCondition();
	

	public  int get() {
		lock.lock();
		try{
			while (available == false) {
				try {
					threadCond.await();
				} catch (InterruptedException e) {
				}
			}
			available = false; // enforce next consumer to wait again.
			threadCond.signalAll();
		}
		finally{
			lock.unlock();
		}
		return x + y;
	}

	public void put(int a, int b) {
		lock.lock();
		try{
			while (available == true) {
				try {
					threadCond.await();
				} catch (InterruptedException e) {
				}
			}
			x = a;
			y = b;
			available = true; 
			threadCond.signalAll();
		}
		finally{
			lock.unlock();
		}
	}

	
	public static class Producer extends Thread {
		private HW51 HW51;
		private int id;

		public Producer(HW51 c, int id) {
			HW51 = c;
			this.id = id;
		}

		public void run() {
			for (int i = 0; i < 5; i++)
				for (int j = 0; j < 5; j++) {
					HW51.put(i, j);
					System.out.println("Producer #" + this.id + " put: (" + i
							+ "," + j + ").");
					try {
						sleep((int) (Math.random() * 100));
					} catch (InterruptedException e) {
					}
				}
			;
		}
	}

	public static class Consumer extends Thread {
		private HW51 HW51;
		private int id;

		public Consumer(HW51 c, int id) {
			HW51 = c;
			this.id = id;
		}

		public void run() {
			int value = 0;
			for (int i = 0; i < 25; i++) {
				value = HW51.get();
				System.out.println("Consumer #" + this.id + " got: " + value);
			}
		}
	}
	
	public static void main(String[] args) {
		HW51 c = new HW51();
		Producer p1 = new Producer(c, 1);
		Consumer c1 = new Consumer(c, 1);
		p1.start();
		c1.start();
		}

	

}
