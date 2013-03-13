
public class threeNPlusOne {
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Invalid parameters provided, please provide two integers");
		}
		try {
			int rangeMin = Integer.parseInt(args[0]);
			int rangeMax = Integer.parseInt(args[1]);
			long largestCount = 0, currentCount = 0;
			for (long i = rangeMin; i <= rangeMax; i += 1) {
				currentCount = countOperation(i);
				if (currentCount > largestCount) { largestCount = currentCount;}
				//Provides progress update when used 
				if ((i % 1000000) == 0) { System.out.println("Current progress: "+i+" / "+rangeMax);}
			}
			System.out.println(rangeMin+" "+rangeMax+" "+largestCount);
		} catch(Exception e) {
			System.out.println("Invalid parameters provided, please provide two integers");
		}
	}
	
	public static long countOperation(long startNumber) {
		long workingNumber = startNumber, counter = 0;
		while (workingNumber != 1) {
			if ((workingNumber % 2) == 1) {
				workingNumber *= 3;
				workingNumber += 1;
			} else {
				workingNumber /= 2;
			}
			counter += 1;
		}
		
		return (counter+1);
	}

}
