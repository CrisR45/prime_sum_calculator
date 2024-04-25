/*
Exit Codes Used:
(1): The amount of given command line arguments total an odd quantity.
(2): No command line arguments were given
(3): A negative number was given as input
(4): A non integer value was given as input
(5): Beginning value in pair was greater than the end value.
(6): Interrupted Exception caught when using join()
*/

class newPrimes {
	// -----------------------------------------------------------
	public static void main(String[] args) {
		// Check for sets of pairs in ComArgs as well as making sure ComArgs are
		// actually given
		if (args.length % 2 != 0) {
			System.err.println("*** ERROR: ALL VALUES PROVIDED MUST COME IN PAIRS.");
			System.exit(1);
			return;
		} else if (args.length == 0) {
			System.err.println("*** ERROR: NO COMMAND LINE ARGUMENTS GIVEN.");
			System.exit(2);
			return;
		}

		int pairs = args.length / 2;
		int threadCount = pairs;
		double grandTotal = 0;
		int[] startVal = new int[pairs];
		int[] endVal = new int[pairs];
		Thread[] workingThreads = new Thread[pairs];
		Worker[] workers = new Worker[threadCount];
		double[] sum = new double[pairs];

		//Command line arguments are loaded into their corresponding arrays
		//Also checks to make sure all values are positive, and integers
		try {
			for (int i = 0, j = 0; i < startVal.length; i++, j += 2) {
				startVal[i] = Integer.parseInt(args[j]);
				if (startVal[i] < 0) {
					System.err.println("***ERROR: ALL VALUES MUST BE POSITIVE");
					System.exit(3);
					return;
				}
			}
			for (int i = 0, j = 1; i < startVal.length; i++, j += 2) {
				endVal[i] = Integer.parseInt(args[j]);
				if (endVal[i] < 0) {
					System.err.println("***ERROR: ALL VALUES MUST BE POSITIVE");
					System.exit(3);
					return;
				}
			}
		} catch (NumberFormatException e) {
			System.err.println("*** ERROR: INPUT MUST BE MADE UP OF INTEGERS");
			System.exit(4);
			return;
		}
		
		//Check to ensure no beginning value is greater than the end value for the corresponding range
		for (int i = 0; i < pairs; i++) {
			if (startVal[i] >= endVal[i]) {
				System.err.println("***ERROR: THE FIRST VALUE IN EACH PAIR MUST BE GREATER THAN THE SEOND.");
				System.exit(5);
				return;
			}
		}

		id();
		System.out.printf("Creating %d thread(s)\n", threadCount);
		
		//Threads are created for each pair of args along with their corresponding worker
		for (int i = 0; i < threadCount; i++) {
			workers[i] = new Worker(startVal[i], endVal[i], sum[i]);
			workingThreads[i] = new Thread(workers[i]);
			workingThreads[i].start();
		}

		//Finalizes each thread once their calculation is done
		//Sum is collected from each thread in the sum array in order to be returned to main.
		for (int i = 0; i < threadCount; i++) {
			try {
				workingThreads[i].join();
				sum[i] = workers[i].getTotal();
				
			} catch (InterruptedException e) {
				System.err.println("***ERROR: Could not perform join().");
				System.exit(6);
				return;
			}
		}
		
		for (int i = 0; i < sum.length; i++) {
			System.out.printf("Sum of all primes [%,8d - %,9d) is %13.0f\n", startVal[i], endVal[i], sum[i]);
			grandTotal += sum[i];
		}
		
		System.out.printf("\nThe grand sum of all primes calculations is %,13.0f\n", grandTotal);
		id();
	}
	
	// -----------------------------------------------
		// print id and system info
		// -----------------------------------------------
		private static void id() {
			final String YOUR_NAME = "Cristian Rodriguez";

			int cores = Runtime.getRuntime().availableProcessors();
			String osName = System.getProperty("os.name");
			String osVer = System.getProperty("os.version");
			String osArch = System.getProperty("os.arch");
			String javaVer = System.getProperty("java.version");
			String javaName = System.getProperty("java.vm.name");

			System.out.printf("\n%14s | %s (%s) | Cores: %d\n", osArch, osName, osVer, cores);
			System.out.printf("%14s | %s\n", javaVer, javaName);
			System.out.printf("%14s | %s\n\n", "PrimeThreads", YOUR_NAME);
		}
}

	class Worker implements Runnable {
		private int start;
		private int end;
		private double total;
		private long threadId;

		// Worker constructor
		Worker(int start, int end, double total) {
			this.start = start;
			this.end = end;
			this.total = total;
		}
		
		//getter to retrieve total;
		public double getTotal() {
			double total = this.total;
			return total;
		}
		// required run method
		public void run() {
			threadId = Thread.currentThread().getId();
			System.out.printf("Thread [%03d] started.\n", threadId);
			this.total = computeTotal(start, end);
		}
	

	// -----------------------------------------------------------
	// sum up primes within range [start, finish)
	// -----------------------------------------------------------
	private static double computeTotal(int start, int finish) {
		double total = 0;

		for (int i = start; i < finish; i++)
			if (Slow_isPrime(i)) {
				total += i;
			}

		return total;
	}

	// ------------------------------------------------
	// determines if a number is a prime number
	// you must use this code, and can't modify it.
	// -----------------------------------------------
	private static boolean Slow_isPrime(int val) {
		int i = 0;

		if (val <= 1)
			return false;

		if (val == 2)
			return true;

		if ((val % 2) == 0)
			return false;

		for (i = 3; i < val; i++)
			if ((val % i) == 0)
				return false;

		return true;
	}
}
