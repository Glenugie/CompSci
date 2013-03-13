import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;
import java.lang.Character;
		
public class evilHangman {
	/**
	 * Main method for Evil Hangman
	 * @param args Unused, no parameters required
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		boolean playAgain = true;
		
		while (playAgain) {
			ArrayList<String> dictionaryWords = readDictionary();
			Random rand = new Random();
			
			//Get inputs from the user
			int wordLength = getWordLength(dictionaryWords);
			int numberOfGuesses = getNumberOfGuesses();
			boolean giveRunningTotal = getRunningTotal();
			
			//Remove all words that aren't of the desired length
			dictionaryWords = trimDictionary(dictionaryWords, wordLength, "", null);
			
			//Setup array to hold guesses
			String[] guesses = new String[numberOfGuesses];
			for (int i = 0; i < numberOfGuesses; i++) {
				guesses[i] = "";
			}
			
			//Setup the active word wit5h the appropriate number of dashes
			String activeWord = "";
			String currentWord = dictionaryWords.get(rand.nextInt(dictionaryWords.size()));
			for (int i = 0; i < wordLength; i++) {
				activeWord += "-";
			}
			
			//Run the main game
			int turnCounter = 0, roundCounter = 0;
			boolean gameWon = false;
			while (turnCounter < numberOfGuesses && !gameWon) {
				//Statistics for the turn
				System.out.println("Round "+(roundCounter+1)+" ("+(numberOfGuesses-turnCounter)+" guesses left)");
				if (giveRunningTotal) {
					System.out.println("Words remaining: "+dictionaryWords.size());
				}
				if (turnCounter != 0) {
					System.out.print("So far you have guessed: ");
					for (int i = 0; i < turnCounter; i++) {
						System.out.print(guesses[i]+" ");
					}
					System.out.println("");
				}
				System.out.println("Your word is: "+activeWord);
				System.out.println("");
				
				guesses[turnCounter] = getGuess(dictionaryWords, guesses);
				String workingWord = "";
				gameWon = true;
				boolean guessCorrect = false;
				for (int i = 0; i < currentWord.length(); i++) {
					if (currentWord.charAt(i) == guesses[turnCounter].charAt(0)) {
						workingWord += currentWord.charAt(i);
						guessCorrect = true;
					} else if (activeWord.charAt(i) != "-".charAt(0)){
						workingWord += currentWord.charAt(i);
					} else {
						workingWord += "-";
						gameWon = false;
					}
				}
				activeWord = workingWord;
				dictionaryWords = trimDictionary(dictionaryWords, wordLength, activeWord, guesses);
				currentWord = dictionaryWords.get(rand.nextInt(dictionaryWords.size()));
				
				if (guessCorrect == false) { turnCounter += 1;}
				roundCounter += 1;
			}
			
			/*Statistics Phase Start*/
				//Results
				if (dictionaryWords.size() != 1) {
					System.out.println("You lost, commiserations");
				} else {
					System.out.println("You won, congratulations");
				}
				
				//Remaining words
				if (giveRunningTotal) {
					System.out.println("Words remaining: "+dictionaryWords.size());
				}
				
				//Guesses
				System.out.print("Your guesses were: ");
				for (int i = 0; i < turnCounter; i++) {
					System.out.print(guesses[i]+" ");
				}
				System.out.println("");
				
				//Word details
				System.out.println("The word you had was: "+activeWord);
				System.out.println("The actual word was: "+dictionaryWords.get(rand.nextInt(dictionaryWords.size())));
				System.out.println("");
			/*Statistics Phase End*/
				
			playAgain = getPlayAgain();
		}
		//Running time
		System.out.println("Running time: "+(System.currentTimeMillis()-start)+"ms");
	}
	
	/**
	 * Reads in the dictionary file from dictionary.txt, which should be in the same directory as the .jar file
	 * @return ArrayList<String> An array list containing all the words from dictionary.txt
	 */
	public static ArrayList<String> readDictionary() {
		ArrayList<String> dictionaryWords = new ArrayList<String>();
		try {
			FileInputStream dictionaryFile = new FileInputStream("dictionary.txt");
			DataInputStream input = new DataInputStream(dictionaryFile);
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line = reader.readLine();
			while (line != null)   {
				dictionaryWords.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch(Exception e) {
			System.out.println("Unhandled dictionary input, check your file");
		}
		return dictionaryWords;
	}
	
	/**
	 * Trims the dictionary in one of two ways: If a word length is provided, it trims out all words not of that length, else if a match pattern is provided, it trims out all words that do not match this pattern
	 * @param dictionaryWords ArrayList<String> An array list containing the current list of words in the dictionary
	 * @param wordLength int Specifies the desired word length to be kept
	 * @param matchPattern String SPecifies a match pattern to match dictionary words against
	 * @param guesses String[] Contains all the current guesses from the players
	 * @return ArrayList<String> Array list containing the trimmed dictionary file
	 */
	public static ArrayList<String> trimDictionary(ArrayList<String> dictionaryWords, int wordLength, String matchPattern, String[] guesses) {
		if (!matchPattern.equals("")) {
			String exception = "[^";
			for (int j = 0; j < guesses.length; j++) {
				exception +=  guesses[j];
			}
			exception += "]";
			
			String backupPattern = matchPattern;
			matchPattern = "";
			for (int j = 0; j < backupPattern.length(); j++) {
				if (backupPattern.charAt(j) == "-".charAt(0)) {
					matchPattern += exception;
				} else {
					matchPattern += "["+backupPattern.charAt(j)+"]";
				}
			}
		}
		
		ArrayList<String> newDictionary = new ArrayList<String>();
		for (int i = (dictionaryWords.size() - 1); i >= 0; i--) {
			if (matchPattern.equals("")) {
				//Trimming off words not of a set length
				if (dictionaryWords.get(i).length() == wordLength) {
					newDictionary.add(dictionaryWords.get(i));
				}
			} else {
				if (Pattern.matches(matchPattern, dictionaryWords.get(i))) {
					newDictionary.add(dictionaryWords.get(i));
				}
			}
		}
		return newDictionary;
	}
	
	/**
	 * Gets a word length from the user, and validates it against the dictionary to ensure it is valid
	 * @param dictionaryWords ArrayList<String> Array list containing all the words in the dictionary
	 * @return int The desired word length
	 */
	public static int getWordLength(ArrayList<String> dictionaryWords) {
		int wordLength = 0;
		boolean validLength = false;
		while (!validLength) {
			try {
				BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
				while (!validLength) {
					System.out.println("How long is your word?");
					System.out.print("> ");
					wordLength = Integer.parseInt(inputReader.readLine());
					int i = 0;
					boolean found = false;
					while (i < dictionaryWords.size() && !found) {
						if (dictionaryWords.get(i).length() == wordLength) {
							found = true;
							validLength = true;
						}
						i += 1;
					}
					if (!validLength) {
						System.out.println("Invalid word length");
					}
				}
			} catch (Exception e) {
				System.out.println("Invalid input(s) provided");
			}
			System.out.println("");
		}
		return wordLength;
	}
	
	/**
	 * Requests and returns the number of guesses the player wants
	 * @return int Number of guesses
	 */
	public static int getNumberOfGuesses() {
		int numberOfGuesses = 1;
		boolean validNumber = false;
		while (!validNumber) {
			try {
				BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
				while (!validNumber) {
					System.out.println("How many guesses do you want?");
					System.out.print("> ");
					numberOfGuesses = Integer.parseInt(inputReader.readLine());
					if (numberOfGuesses > 0 && numberOfGuesses <= 26) {
						validNumber = true;
					} else {
						System.out.println("Invalid number of guesses, please enter a number greater than 0");
					}
				}
			} catch (Exception e) {
				System.out.println("Invalid input(s) provided");
			}
			System.out.println("");
		}
		return numberOfGuesses;
	}
	
	/**
	 * Finds out whether the user wants to display a running total of words in the dictionary
	 * @return boolean Whether the user wants a running total
	 */
	public static boolean getRunningTotal() {
		boolean runningTotal = false;
		boolean validInput = false;
		while (!validInput) {
			try {
				BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
				while (!validInput) {
					System.out.println("Display running total of remaining words? (Y/N)");
					System.out.print("> ");
					String input = inputReader.readLine().toUpperCase();
					if (input.equals("Y")) {
						runningTotal = true;
						validInput = true;
					} else if (input.equals("N")) {
						runningTotal = false;
						validInput = true;
					} else {
						System.out.println("Invalid answer given, please enter either Y or N");
					}
				}
			} catch (Exception e) {
				System.out.println("Invalid input(s) provided");
			}
			System.out.println("");
		}
		return runningTotal;
	}
	
	/**
	 * Asks the user if they want to play again
	 * @return boolean Whether the user wants to play again
	 */
	public static boolean getPlayAgain() {
		boolean playAgain = false;
		boolean validInput = false;
		while (!validInput) {
			try {
				BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
				while (!validInput) {
					System.out.println("Do you want to play again? (Y/N)");
					System.out.print("> ");
					String input = inputReader.readLine().toUpperCase();
					if (input.equals("Y")) {
						playAgain = true;
						validInput = true;
					} else if (input.equals("N")) {
						playAgain = false;
						validInput = true;
					} else {
						System.out.println("Invalid answer given, please enter either Y or N");
					}
				}
			} catch (Exception e) {
				System.out.println("Invalid input(s) provided");
			}
			System.out.println("");
		}
		return playAgain;
	}
	
	/**
	 * Gets a guess from the user, validates it, and returns the guess
	 * @param dictionaryWords ArrayList<String> The current array list containing the dictionary words
	 * @param guesses String[] The list of the users previous guesses
	 * @return String The players guess
	 */
	public static String getGuess(ArrayList<String> dictionaryWords, String[] guesses) {
		String currentGuess = "";
		boolean validInput = false;
		while (!validInput) {
			try {
				BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
				while (!validInput) {
					validInput = true;
					System.out.println("What is your guess?");
					System.out.print("> ");
					currentGuess = inputReader.readLine().toUpperCase();
					if (currentGuess.length() != 1) {
						validInput = false;
						System.out.println("Invalid input, please guess a single letter.");
					} else if (!Character.isLetter(currentGuess.charAt(0))) {
						validInput = false;
						System.out.println("That wasn't actually a letter");
					} else {
						for (int i = 0; i < guesses.length; i++) {
							if (guesses[i].equals(currentGuess)) {
								validInput = false;
								System.out.println("You already guessed that.");
							}
						}
					}
				}
			} catch (Exception e) {
				System.out.println("Invalid input(s) provided");
			}
			System.out.println("");
		}
		return currentGuess;
	}
}