import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Translator
{
    private InputReader inputReader;
    
    public Translator() {
        inputReader = new InputReader();
    }

    public void translateString () {
        String userInput = getString();
        
        boolean isBinary = true;
        
        int stringLength = userInput.length();
        
        for (int i = 0; i < stringLength; i++) {
			if (!userInput.substring(i,(i+1)).equals("0") && !userInput.substring(i,(i+1)).equals("1")) { isBinary = false;}
			if (stringLength < 8) { isBinary = false;}
		}
        
		System.out.println("\n_____________________________________________________________________________");
        System.out.print("Translating:\n\"" + userInput + "\"\n\nTranslation processed:\n\"");
        if (!isBinary) {
            for (int i = 0; i < stringLength; i++) {
                char currentLetter = userInput.charAt(i);
                String currentBinary = "";
                int currentASCII = currentLetter;
                
                for (int j = 0; j < 8; j++) {
                    if ((currentASCII - java.lang.Math.pow(2.0,(7.0-j))) >= 0) { currentBinary += "1"; currentASCII -= java.lang.Math.pow(2.0,(7.0-j));}
                    else { currentBinary += "0";}
                }
                System.out.print(currentBinary);
                if (i < stringLength) { System.out.print(" ");}
                if (((i+1) % 4) == 0 && i < stringLength && i > 0) { System.out.print("\n");}
            }
        } else {
            if ((stringLength % 8) != 0) { userInput = userInput.substring(0, ((int)java.lang.Math.floor(stringLength/8)*8));}
            stringLength = userInput.length();
        
            for (int i = 0; i < stringLength; i += 8) {
                String currentSequence = userInput.substring(i, (i+8));
                int binaryValue = 0;
                for (int j = 0; j < 8; j++) {           
                    if (currentSequence.substring(j,(j+1)).equals("1")) { binaryValue += java.lang.Math.pow(2.0,(7.0-j));}
                }
                char charValue = (char)binaryValue;
                System.out.print(charValue);
                if (((i+1) % 64) == 0 && i < stringLength && i > 0) { System.out.print("\n");}
            }
        }
        System.out.println("\"");
        System.out.println("_____________________________________________________________________________\n");
    }
	
	public String getString() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the string (either binary or english) that you wish to translate:");     // print prompt
        System.out.print("> ");
        return in.readLine();
	}
}
