import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class shellSort {
	public static void main(String[] args) {
		String[] inputArray, desiredArray;
		int numberOfInputs, numberOfTurtles;
		try {
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
			numberOfInputs = Integer.parseInt(inputReader.readLine());
			for (int i = 0; i < numberOfInputs; i++) {
				numberOfTurtles = Integer.parseInt(inputReader.readLine());
				inputArray = new String[numberOfTurtles];
				desiredArray = new String[numberOfTurtles];
				for (int j = 0; j < numberOfTurtles; j++) {
					inputArray[j] = inputReader.readLine();
				}
				for (int j = 0; j < numberOfTurtles; j++) {
					desiredArray[j] = inputReader.readLine();
				}
				if (!inputArray.equals(desiredArray)) {
					processTurtles(inputArray, desiredArray, numberOfTurtles);
				}
			}
		} catch (IOException e) {
			System.out.println("Invalid input(s) provided");
		}
	}
	
	public static void processTurtles(String[] inputArray, String[] desiredArray, int numberOfTurtles) {
		int firstIndex = 0;
		for (int search = 0; search < numberOfTurtles; search++) {
			if (inputArray[search].equals(desiredArray[numberOfTurtles-1])) {
				firstIndex = search;
			}
		}
		for (int scan = (numberOfTurtles-2); scan >= 0; scan--) {
			String turtleToMove = desiredArray[scan];
			if (!inputArray[0].equals(turtleToMove) || !desiredArray[numberOfTurtles-1].equals(turtleToMove)) {
				int indexToMove = 0;
				for (int search = 0; search < numberOfTurtles; search++) {
					if (inputArray[search].equals(turtleToMove)) {
						indexToMove = search;
					}
				}
				if ((scan == (numberOfTurtles-2) && indexToMove > firstIndex) || scan != (numberOfTurtles-2)) {
					inputArray = climbStack(inputArray, indexToMove);
					System.out.println(turtleToMove);
				}
			}
		}
		System.out.println();
	}
	
	public static String[] climbStack(String[] inputArray, int turtleToMove) {
		String moveName = inputArray[turtleToMove];
		for (int turtleIndex = turtleToMove; turtleIndex > 0; turtleIndex--) {
			inputArray[turtleIndex] = inputArray[turtleIndex-1];
		}
		inputArray[0] = moveName;
		return inputArray;
	}
}
