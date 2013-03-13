import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class jollyJumpers {
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
			String[] currentInput = inputArray.get(index).split(" ");
			boolean isJolly = true;
			int counter = 2, previousValue = 0, calculation = 0, currentLength = Integer.parseInt(currentInput[0]);
			int currentValue = Integer.parseInt(currentInput[1]);
			ArrayList<Integer> calculationArray = new ArrayList<Integer>();
			while (counter <= currentLength){
				previousValue = currentValue;
				currentValue = Integer.parseInt(currentInput[counter]);
				calculation = previousValue-currentValue;
				
				if (calculation < 0) { calculation -= (2*calculation);}
				if (calculation >= currentInput.length) { isJolly = false;}
				
				calculationArray.add(calculation);
				
				counter += 1;
			}
			
			for (int checkCounter = 1; checkCounter <= currentLength; checkCounter += 1) {
				calculationArray.remove((Integer)checkCounter);
			}
			if (!calculationArray.isEmpty()) { isJolly = false;}
						
			if (isJolly) { System.out.println("Jolly");}
			else { System.out.println("Not Jolly");}
			 
			index += 1;
		}
	}
}