import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class myFamily {
	public static void main(String[] args) {
		ArrayList<String> inputArray = new ArrayList<String>();
		try {
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
			int caseNumber = Integer.parseInt(inputReader.readLine());
			for (int i = 0; i < caseNumber; i++) {
				inputArray.add(inputReader.readLine());
			}
		} catch (IOException e) {
			System.out.println("Invalid input(s) provided");
		}
	
		int index = 0;
		while (index < inputArray.size()) {
			String[] currentInput = inputArray.get(index).split(" ");
			int counter = 0, currentValue = 0, currentLength = Integer.parseInt(currentInput[0]);
			int[] streetArray = new int[currentLength];
			while (counter < currentLength){
				currentValue = Integer.parseInt(currentInput[(counter+1)]);
				streetArray[counter] = currentValue;
				counter += 1;
			}
			
			Arrays.sort(streetArray);
			int median = streetArray[(currentLength/2)], totalDistance = 0;
			for (int i = 0; i < currentLength; i++) {
				int currentAddress = streetArray[i];
				if (currentAddress < median) {
					totalDistance += (median - currentAddress);
				} else {
					totalDistance += (currentAddress - median);
				}
			}
			System.out.println(totalDistance);
			
			index += 1;
		}
	}
}
