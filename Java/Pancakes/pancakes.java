import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class pancakes {
	public static void main(String[] args) {
		ArrayList<String> inputArray = new ArrayList<String>();
		try {
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
			String input = "";
			while (!input.equals("0")) {
				input = inputReader.readLine();
				inputArray.add(input);
			}
		} catch (IOException e) {
			System.out.println("Invalid input(s) provided");
		}
		
		int index = 0;
		while (index < (inputArray.size() - 1)) {
			//Creates the stack of pancakes
			String[] currentInput = inputArray.get(index).split(" ");
			Integer currentStack[] = new Integer[currentInput.length];
			for (int i = 0; i < currentInput.length; i++){
				currentStack[i] = Integer.parseInt(currentInput[i]);
			}
			
			System.out.println(inputArray.get(index));
			boolean isSorted = false;
			while (!isSorted) {
				for (int i = 0; i < (currentInput.length-1); i++) {
					if (currentStack[i] > currentStack[i+1]) {
						isSorted = false;
						break;
					} else {
						isSorted = true;
					}
				}
				
				if (!isSorted) {
					currentStack = flipArray(currentStack);
				}
			}
			System.out.println("0");
			index += 1;
		}
		System.out.println("0");
	}
	
	public static Integer[] flipArray(Integer[] flipArray) {
		Integer flippedArray[] = new Integer[flipArray.length];
		for (int i = 0; i < flipArray.length; i++) {
			flippedArray[flippedArray.length-i] = flipArray[i];
		}
		return flippedArray;
	}
}
