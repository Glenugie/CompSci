import java.util.ArrayList;
import java.util.Random;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class boggle {
	public static ArrayList<ArrayList<ArrayList<String>>> dictionaryWords = new ArrayList<ArrayList<ArrayList<String>>>();
	public static ArrayList<ArrayList<String>> dictionaryList;
	public static String[] board = new String[25];
	public static Random rand = new Random();
	
	/**
	 * Main method for Boggle
	 * @param args Unused, no parameters required
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		
		ArrayList<String> dice = new ArrayList<String>();
		dice = createDice();
		String boardLetters = "";	
		for (int i = 24; i >= 0; i--) {
			int diceChoice = rand.nextInt(i+1);
			String currentDie = dice.get(diceChoice);
			int diceRoll = rand.nextInt(6);
			String chosenLetter = String.valueOf(currentDie.charAt(diceRoll));
			boardLetters += chosenLetter;
			board[i] = chosenLetter;
			dice.remove(diceChoice);
		}
		readDictionary(boardLetters);
		printBoard();
		playBoggle();

		System.out.println("Running time: "+(System.currentTimeMillis()-start)+"ms");
	}
	
	/**
	 * "Plays" Boggle, generating a list of legal words on the board it's been provided with
	 */
	public static void playBoggle() {
		//Initialise necessary arrays
		ArrayList<String> wordList = new ArrayList<String>();
		ArrayList<Integer> positionChain = new ArrayList<Integer>();
		int[] size = new int[25];
		int[] counter = new int[25];
		int[] pSearch = new int[25];
		String[] usedPositions = new String[25];
		for (int init = 0; init < 25; init++) {
			usedPositions[init] = "";
			counter[init] = 0;
		}
		
		//Begin playing at depth 1
		int currentLevel = 1 ,wordCounter = 0;
		String currentWord = "";
		size[1] = 25;
		for (counter[1] = 0; counter[1] < size[1]; counter[1]++) {
			dictionaryList = dictionaryWords.get(letterInt(board[counter[1]],0));
			pSearch[currentLevel] = counter[currentLevel];
			positionChain.add(pSearch[currentLevel]);
			size[(currentLevel+1)] = getAdjacentPositions(pSearch[currentLevel]).size();
			currentWord += board[counter[currentLevel]];
			if (board[counter[currentLevel]].equals("Q")) { currentWord += "U";}
			
			//Move down to depth 2 (and start main for loop)
			currentLevel++;
			for (counter[2] = 0; counter[2] < size[2]; counter[currentLevel]++) {
				//Select layer 2 primary search
				if (currentLevel == 2) {
					pSearch[currentLevel] = getRandomAdjacent(pSearch[(currentLevel-1)], usedPositions[currentLevel]);
					positionChain.add(pSearch[currentLevel]);
					currentWord += board[pSearch[currentLevel]];
					if (board[pSearch[currentLevel]].equals("Q")) { currentWord += "U";}
					size[(currentLevel+1)] = getAdjacentPositions(pSearch[currentLevel]).size();
				}
				
				//Increase depth
				if (currentLevel < 23 && usedPositions[currentLevel].length() < size[currentLevel]) {
					currentLevel++;
					usedPositions[currentLevel] = "";
					
					ArrayList<Integer> adjacentPositions = getAdjacentPositions(pSearch[currentLevel-1]);
					for (int usedCounter = 0; usedCounter < positionChain.size(); usedCounter++) {
						if (adjacentPositions.contains(positionChain.get(usedCounter))) {
							usedPositions[currentLevel] += board[positionChain.get(usedCounter)];
						}
					}
					
					pSearch[currentLevel] = getRandomAdjacent(pSearch[(currentLevel-1)], usedPositions[currentLevel]);
					size[(currentLevel+1)] = getAdjacentPositions(pSearch[currentLevel]).size();
				} else {
					pSearch[currentLevel] = getRandomAdjacent(pSearch[(currentLevel-1)], usedPositions[currentLevel]);
				}
				
				//Set variables for the current depth
				positionChain.add(pSearch[currentLevel]);
				currentWord += board[pSearch[currentLevel]];
				if (board[pSearch[currentLevel]].equals("Q")) { currentWord += "U";}
				usedPositions[(currentLevel+1)] += board[pSearch[(currentLevel-1)]];
				
				//Check if it's a word
				if (dictionaryList.get(letterInt(board[pSearch[2]],0)).contains(currentWord)) {
					if (!wordList.contains(currentWord)) {
						wordList.add(currentWord);
						wordCounter++;
						System.out.println("Word found: "+currentWord+" (Total found: "+wordCounter+")");
					}
				}
				
				//Mark Position as used
				usedPositions[currentLevel] += board[pSearch[currentLevel]];
				
				//Jump back up to previous primary search, if necessary
				boolean validSegment = validWordSegment(currentWord,pSearch[2]);
				if (usedPositions[currentLevel].length() >= size[currentLevel]  || !validSegment) {
					currentWord = currentWord.substring(0, (currentLevel-1));
						//System.out.println("Word segment: "+currentWord+" (Depth: "+currentLevel+") (Ground: "+counter[1]+")");
					currentLevel--;
						//System.out.println("After up-hop: "+currentWord+" (Depth: "+currentLevel+") (Ground: "+counter[1]+")");
					//positionChain.remove((positionChain.size()-1));
					usedPositions[currentLevel] += board[pSearch[currentLevel]];
					
					//Removes last element from the position chain
					ArrayList<Integer> tempChain = new ArrayList<Integer>();
					for (int chainCounter = 0; chainCounter < (positionChain.size()-1); chainCounter++) {
						tempChain.add(positionChain.get(chainCounter));
					}
					positionChain = tempChain;
					tempChain.clear();
				}
				
				//If the level exceeds the limits, cut the word length as appropriate
				if (currentLevel <= 2 || currentLevel >= 23) {
					currentWord = currentWord.substring(0, (currentLevel-1));
				}				
			}
			currentLevel--;
			ArrayList<Integer> tempChain = new ArrayList<Integer>();
			for (int chainCounter = 0; chainCounter < (positionChain.size()-1); chainCounter++) {
				tempChain.add(positionChain.get(chainCounter));
			}
			positionChain = tempChain;
			tempChain.clear();
			usedPositions[(currentLevel+1)] = "";
			currentWord = currentWord.substring(0, (currentLevel-1));
		}
		System.out.println(wordCounter+" words found");
	}
	
	/**
	 * Gets a random position adjacent to the given position
	 * @param i int The position to get a random adjacent for
	 * @param usedPositions String The list of previously used positions, so as not to check the same segment twice
	 * @return int The position that has been randomly selected
	 */
	public static int getRandomAdjacent(int i, String usedPositions) {
		int random = i;
		ArrayList<Integer> adjacentPositions = getAdjacentPositions(i);
		ArrayList<String> positionList = new ArrayList<String>();
		for (int usedCounter = 0; usedCounter < usedPositions.length(); usedCounter++) {
			positionList.add(Character.toString(usedPositions.charAt(usedCounter)));
		}
		//System.out.println("Used: "+positionList);
		//System.out.println("Adjacent: "+adjacentPositions);
		for (int positionCounter = (adjacentPositions.size()-1); positionCounter >= 0; positionCounter--) {
			if (positionList.contains(board[adjacentPositions.get(positionCounter)])) {
				if (adjacentPositions.size() > 1) {
					positionList.remove(board[adjacentPositions.get(positionCounter)]);
					adjacentPositions.remove(positionCounter);
				}
			}
		}
		random = adjacentPositions.get(rand.nextInt(adjacentPositions.size()));
		return random;
	}
	
	/** 
	 * Checks whether the given word segment is a valid word segment, or whether it is nonsense
	 * @param wordSegment String The word segment to check
	 * @param secondPosition int The position of the second letter on the board, used for scanning the correct portion of the dictionary
	 * @return boolean True/false value for whether the word segment is valid
	 */
	public static boolean validWordSegment(String wordSegment, int secondPosition) {
		ArrayList<String> workingList = dictionaryList.get(letterInt(board[secondPosition],0));
		for (int wordCheck = 0; wordCheck < workingList.size(); wordCheck++) {
			if (workingList.get(wordCheck).startsWith(wordSegment)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Generates an ArrayList of all the dice for the boggle board
	 * @return ArrayList<String> Dice for the boggle board
	 */
	public static ArrayList<String> createDice() {
		ArrayList<String> dice = new ArrayList<String>();
		dice.add("AAAFRS");	dice.add("AAEEEE");	dice.add("AAFIRS");	dice.add("ADENNN");	dice.add("AEEEEM");
		dice.add("AEEGMU");	dice.add("AEGMNN");	dice.add("AFIRSY");	dice.add("BJKQXZ");	dice.add("CCENST");
		dice.add("CEIILT");	dice.add("CEILPT");	dice.add("CEIPST");	dice.add("DDHNOT");	dice.add("DHHLOR");
		dice.add("DHLNOR");	dice.add("DHLNOR");	dice.add("EIIITT");	dice.add("EMOTTT");	dice.add("ENSSSU");
		dice.add("FIPRSY");	dice.add("GORRVW");	dice.add("IPRRRY");	dice.add("NOOTUW");	dice.add("OOOTTU");
		return dice;
	}
	
	/**
	 * Reads in the dictionary file from dictionary.txt, which should be in the same directory as the class file
	 */
	public static void readDictionary(String boardLetters) {
		//Can set a maximum number of words for the dictionary to hold
		int maxWords = 1000000;
		
		for (int counter = 0; counter < 26; counter++) {
			ArrayList<ArrayList<String>> layer1 = new ArrayList<ArrayList<String>>();
			for (int counterL1 = 0; counterL1 < 26; counterL1++) {
				ArrayList<String> layer2 = new ArrayList<String>();
				layer1.add(layer2);
			}
			dictionaryWords.add(layer1);
		}
		try {
			FileInputStream dictionaryFile = new FileInputStream("dictionary.txt");
			DataInputStream input = new DataInputStream(dictionaryFile);
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line = reader.readLine();
			int counter = 0;
			while (line != null && counter < maxWords)   {
				if (boardLetters.contains(Character.toString(line.charAt(0)))) {
					dictionaryWords.get(letterInt(line,0)).get(letterInt(line,1)).add(line);
				}
				line = reader.readLine();
				counter++;
			}
			reader.close();
		} catch(Exception e) {
			System.out.println("Unhandled dictionary input, check your file");
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Converts the character of a given word at the given position to an integer
	 * @param word String The input word containing the character to be converted
	 * @param position int The position in the word of the character to be converted
	 * @return
	 */
	public static int letterInt(String word, int position) {
		return (word.toLowerCase().charAt(position) - 'a');
	}
	
	/**
	 * Returns an ArrayList of Integers containing all the board positions surrounding the provided position
	 * @param i int The position to map adjacent positions for
	 * @return ArrayList<Integer> List of all adjacent positions of position i
	 */
	public static ArrayList<Integer> getAdjacentPositions(int i) {
		ArrayList<Integer> adjacentPositions = new ArrayList<Integer>();
		if ((i >= 6 && i <= 8) || (i >= 11 && i <= 13) || (i >= 16 && i <= 18)) {
			adjacentPositions.add((i-6));
			adjacentPositions.add((i-5));
			adjacentPositions.add((i-4));
			adjacentPositions.add((i-1));
			adjacentPositions.add((i+1));
			adjacentPositions.add((i+4));
			adjacentPositions.add((i+5));
			adjacentPositions.add((i+6));
		} else if (i == 5 || i == 10 || i == 15) {
			adjacentPositions.add((i-5));
			adjacentPositions.add((i-4));
			adjacentPositions.add((i+1));
			adjacentPositions.add((i+5));
			adjacentPositions.add((i+6));
		} else if (i >= 1 && i <= 3) {
			adjacentPositions.add((i-1));
			adjacentPositions.add((i+1));
			adjacentPositions.add((i+4));
			adjacentPositions.add((i+5));
			adjacentPositions.add((i+6));
		} else if (i == 9 || i == 14 || i == 19) {
			adjacentPositions.add((i-6));
			adjacentPositions.add((i-5));
			adjacentPositions.add((i-1));
			adjacentPositions.add((i+4));
			adjacentPositions.add((i+5));
		} else if (i >= 21 && i <= 23) {
			adjacentPositions.add((i-6));
			adjacentPositions.add((i-5));
			adjacentPositions.add((i-4));
			adjacentPositions.add((i-1));
			adjacentPositions.add((i+1));
		} else if (i == 0) {
			adjacentPositions.add((i+1));
			adjacentPositions.add((i+5));
			adjacentPositions.add((i+6));
		} else if (i == 4) {
			adjacentPositions.add((i-1));
			adjacentPositions.add((i+4));
			adjacentPositions.add((i+5));
		} else if (i == 20) {
			adjacentPositions.add((i-5));
			adjacentPositions.add((i-4));
			adjacentPositions.add((i+1));
		} else if (i == 24) {
			adjacentPositions.add((i-6));
			adjacentPositions.add((i-5));
			adjacentPositions.add((i-1));
		}
		return adjacentPositions;
	}
	
	/**
	 * Prints out the boggle board to the command line
	 */
	public static void printBoard() {
		for (int boardPosition = 0; boardPosition < 25; boardPosition++) {
			System.out.print(board[boardPosition]);
			if (board[boardPosition].equals("Q")) { System.out.print("U");}
			System.out.print(" ");
			if ((boardPosition % 5) == 4) { System.out.println("");}
		}
	}
}