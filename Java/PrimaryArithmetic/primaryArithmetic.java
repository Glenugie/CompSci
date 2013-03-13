import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class primaryArithmetic {
	public static void main(String[] args) {
		ArrayList<String> inputArray = new ArrayList<String>();
		try {
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
			String input = "";
			while (!input.equals("0 0")) {
				input = inputReader.readLine();
				inputArray.add(input);
			}
		} catch (IOException e) {
			System.out.println("Invalid input(s) provided");
		}
		
		int in1 = 0, in2 = 0, index = 0, workingNumber = 0;
		while (index < (inputArray.size() - 1)) {
			String[] currentInput = inputArray.get(index).split(" ");
			in1 = Integer.parseInt(currentInput[0]);
			in2 = Integer.parseInt(currentInput[1]);
			int working1 = in1 % 10, working2 = in2 % 10, carryCount = 0, currentCarry = 0;
			while (in1 >= 1 && in2 >= 1) {
				workingNumber = (working1 + working2 + currentCarry);
				//System.out.println(working1+" + "+working2+" + "+currentCarry+" = "+workingNumber+" (Carry "+currentCarry+")");
				currentCarry = 0;
				if (workingNumber >= 10) {
					carryCount += 1;
					currentCarry = (workingNumber-(workingNumber%10))/10;
				}
				in1 /= 10;
				in2 /= 10;
				working1 = (in1)%10;
				working2 = (in2)%10;
			}
			if (carryCount == 0) { System.out.print("No ");} else { System.out.print(carryCount+" ");}
			System.out.print("carry operation");
			if (carryCount > 1) { System.out.print("s");}
			System.out.println(".");
			index += 1;
		}
	}
}
