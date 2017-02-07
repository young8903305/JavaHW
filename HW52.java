package apjp2016;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;


public class HW52 {
	

	/**
	 * CubbyHole2 can hold at most 2 pairs of numbers
	 * Implement this class to satisfy the requirement 
	 * 
	 * @author chencc
	 * 
	 */
	public static class CubbyHole2 {
		
		//The following two methods are dummy methods used simply to make
		//the program no syntax error. 
//		public synchronized int get() { return 0 ;}
//		public synchronized void put(int a, int b) { }

		private int x, y, isFull = 0;
		private int[][] box = new int[2][2];
//		private boolean available = false; // condition var
		private Lock lock = new ReentrantLock();
		private Condition threadCond = lock.newCondition();
		
		public  int get() {
			lock.lock();
			try{
				while (isFull == 0) {
					try {
						threadCond.await();
					} catch (InterruptedException e) {
					}
				}
				isFull--;
				x = box[isFull][0];
				y = box[isFull][1];
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
				while (isFull == 2) {
					try {
						threadCond.await();
					} catch (InterruptedException e) {
					}
				}
				box[isFull][0] = a;
				box[isFull][1] = b;
				isFull++; 
				threadCond.signalAll();
			}
			finally{
				lock.unlock();
			}
		}
	}

	public static class Producer implements Runnable {
		private CubbyHole2 cubbyhole;
		private int id;

		public Producer(CubbyHole2 c, int id) {
			cubbyhole = c;
			this.id = id;
		}

		public void run() {
			for (int i = 0; i < 5; i++)
				for (int j = 0; j < 5; j++) {
					cubbyhole.put(i, j);
					System.out.println("Producer #" + this.id + " put: (" + i
							+ "," + j + ").");
					try {
						Thread.sleep((int) (Math.random() * 100));
					} catch (InterruptedException e) {
					}
				}
			;
		}
	}

	public static class Consumer implements Runnable {
		private CubbyHole2 cubbyhole;
		private int id;

		public Consumer(CubbyHole2 c, int id) {
			cubbyhole = c;
			this.id = id;
		}

		public void run() {
			int value = 0;
			for (int i = 0; i < 25; i++) {
				value = cubbyhole.get();
				System.out.println("Consumer #" + this.id + " got: " + value);
				try {
					Thread.sleep((int) (Math.random() * 100));
				} catch (InterruptedException e) {
				}
			}
			
		}
	}

	public static void main(String[] args) {

		CubbyHole2 c = new CubbyHole2();
		

		Producer[] ps = new Producer[5];
		for (int k = 0; k < 5; k++) {
			ps[k] = new Producer(c, k);
		}

		Consumer[] cs = new Consumer[5];
		for (int k = 0; k < 5; k++) {
			cs[k] = new Consumer(c, k);
		}

		// Using fixed-sized ThreadPool with size(3) < number of producers (5)
		// will result in deadlock. The available threads(3) will be hold by producers which
		// are blocked in the blockingQueue and cannot terminate.
		
		// ExecutorService es = Executors.newFixedThreadPool(3);
		ExecutorService es = Executors.newCachedThreadPool();

		long startTime = System.currentTimeMillis();

		for (Producer p : ps) {
			es.execute(p);
		}

		for (Consumer csumer : cs) {
			es.execute(csumer);
		}

		es.shutdown();

		while (!es.isTerminated()) {
			try {
				es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			} catch (InterruptedException e) {
			}
		}

		long endTime = System.currentTimeMillis();
		System.out.println("The total execution time is "
				+ (endTime - startTime) + " milliseconds");

	}

}
