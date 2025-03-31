import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InputMismatchException;

/*
 * CSCI 221, Spring 2021, Programming HW 1
 * Matt Stewart
 * This work is entirely my own, though it did require advanced reading through zyBooks Chapter 13 and 19 to complete.
 *
 * This Class analyzes movie review data to determine if words have a
 * negative or positive meaning. If a word is used more often in positive
 * reviews, it is assumed that the word is positive, and vice versa.
 * Currently, it reads review data line by line - each line is a single review in the form:
 * <integer rating of movie> <Written review - text supporting the rating> <newline>
 *
 * This Class will also give out an averaged rating for reviews that contain a user-picked
 * word, as well as go through a list of words and determine the most positive and most negative
 * words from said list.
 *
 * For #4, while we have had some introduction to Outprint and FileOutputStream via the first chapter, it does not go
 * into the way to actually write to another file. So, I checked the zyBooks and Chapter 19 goes over it. Ergo, some
 * advanced reading to do it, but found a way. As written there it didn't work and I had to make a File first (gauging
 * from the provided coded as this being a way to do it), but managed to get it. Very curious to see how to do this
 * that would be more in line with our current progress.
 *
 * Considering how little this looks like the original sample code I'm sure I overly complicated things here in many way,
 * but I didn't like the way it looked having it all in main(), and I like to dummy-proof my code as much as my knowledge
 * and skills will allow.
 */

public class HW1 {
	/*
	* I made this function to check if a word even exists in the review file, because I was getting errors in calculations
	* as well as getting NaN values if the only word(s) in the list were not in the review. I wanted initially to have an
	* int array defined in this area and adjust/check it as needed depending on if the word exist but I couldn't quite get
	* that to work out right. I then thought about making a Word Object that was defined by a double, a String, and a boolean
	* but that was even more clunky and would require a complete rewrite of the code, so I went this method.
	*/
	public static boolean isInReview(String word) throws FileNotFoundException {
		File reviewFile = new File("movieReviews.txt");
		Scanner reviewScanner = new Scanner(reviewFile);
		String reviewText;
		double reviewScore = 0.0;
		String scoreWord = word.toLowerCase();
		while(reviewScanner.hasNext()) {
			// Reads the numeric movie rating and then the text.
			reviewScore = reviewScanner.nextInt();
			reviewText = reviewScanner.nextLine().toLowerCase();
			if(reviewText.contains(scoreWord)) { //All this to determine if the word exists or not...
				return true;
			}
		}
		return false;
	}

	/* getWordScore will be the base function used in the others. It is here that the average score of a
	* word will be determined, and from there the other functions can do that per word they are looking up
	* to use for their individual purposes. It will take a String input and convert it to lower case.
	*/
	public static double getWordScore(String word) throws FileNotFoundException {
		//Let's get all the objects/variables out of the way at the start...
		File reviewFile = new File("movieReviews.txt");
		Scanner reviewScanner = new Scanner(reviewFile);
		int reviewScore;
		String reviewText;
		String scoreWord = word.toLowerCase();
		int avgAcc = 0;
		double valueAcc = 0.0;

		while(reviewScanner.hasNext()) {
			// Reads the numeric movie rating and then the text.
			reviewScore = reviewScanner.nextInt();
			reviewText = reviewScanner.nextLine().toLowerCase();
			if(reviewText.contains(scoreWord)) {
				avgAcc += 1;
				valueAcc += reviewScore;
			}
		}
		/* This is here to check if the word was found in the review, primarily for the later parts of the assignment, so that
		* it will output false if the word is not found and true if the review average is 0. This will help keep the averages
		* accurate for Part 3 (avgWordScore), otherwise it would consider a word not part of the list as a zero instead of not
		* including it and affect the average negatively.
		*/
		if (isInReview(word) == false) {
			return 0;
		}
		else {
		return (valueAcc / avgAcc);
		}
	}

	public static int getWordCount(String word) throws FileNotFoundException {
		File reviewFile = new File("movieReviews.txt");
		Scanner reviewScanner = new Scanner(reviewFile);
		int reviewScore;
		String reviewText;
		String countWord = word.toLowerCase();
		int usedAcc = 0;

		while(reviewScanner.hasNext()) {
			//Since I don't care about the score here, can just read the whole line at once.
			reviewText = reviewScanner.nextLine();
			if(reviewText.contains(countWord)) {
				usedAcc += 1;
			}
		}
		return usedAcc;
	}

	public static void singleWord(String searchWord) throws FileNotFoundException {
		double wordValue = getWordScore(searchWord);
		int wordCount = getWordCount(searchWord);
		if (isInReview(searchWord) == false) {
			System.out.println("The word " + searchWord + " does not appear in the reviews.");
			System.out.println(" ");
		}
		else {
			System.out.println(searchWord + " appears " + wordCount + " times.");
			System.out.println("The average score for reviews containing the word " + searchWord + " is " + wordValue + ".");
			System.out.println(" ");
		}
	}

	public static void avgWordsScore(String fileName) throws FileNotFoundException {
		//I hate having to reinstantiate the Scanners and variables every time >_>
		File avgWordsFile = new File(fileName);
		Scanner avgFileScanner = new Scanner(avgWordsFile);
		double avgWordsScore = 0.0;
		int avgWordsCount = 0;
		double avgWordsAvg = 0.0;
		String avgText = "";

		while(avgFileScanner.hasNext()) {
			avgText = avgFileScanner.nextLine().toLowerCase();
			/* This is where it will check the output to determine if the word from the list was found among the reviews or not,
			* and will make sure to not uptick the count if the word was not found.
			*/
			if (isInReview(avgText) == true) {
				avgWordsScore += getWordScore(avgText);
				avgWordsCount += 1;
			}
		}

		avgWordsAvg = avgWordsScore / avgWordsCount;
		if (avgWordsCount == 0) {
			System.out.println("The words in your file do not exist in the reviews.");
			System.out.println(" ");
		}
		else {
			System.out.println("The average score of words in " + fileName + " is " + avgWordsAvg + ".");
			if (avgWordsAvg > 2.0){
				System.out.println("The overall sentiment of " + fileName + " is positive.");
				System.out.println(" ");
			}
			if (avgWordsAvg < 2.0){
				System.out.println("The overall sentiment of " + fileName + " is negative.");
				System.out.println(" ");
			}
		}
	}

	public static void highLowScore(String fileName) throws FileNotFoundException {
		File highLowFile = new File(fileName);
		Scanner highLowFileScanner = new Scanner(highLowFile);
		double wordScore = 0.0;
		double highScore = 0.0;
		double lowScore = 4.0;
		String highLowString = "";
		String highWord = "";
		String lowWord = "";

		while(highLowFileScanner.hasNext()) {
			highLowString = highLowFileScanner.nextLine().toLowerCase();
			//To ensure words that do not exist won't mess up anything...
			if (isInReview(highLowString) == true) {
				wordScore = getWordScore(highLowString);
				if (wordScore > highScore) {
					highScore = wordScore;
					highWord = highLowString;
				}
				if (wordScore < lowScore) {
					lowScore = wordScore;
					lowWord = highLowString;
				}
			}
		}
		if (highWord == ""){
			System.out.println("The words in your file do not exist in the reviews.");
			System.out.println(" ");
		}
		else {
		System.out.println("The most positive word, with a score of " + highScore + ", is " + highWord + ".");
		System.out.println("The most negative word, with a score of " + lowScore + ", is " + lowWord + ".");
		System.out.println(" ");
		}
	}

	public static void sortWord(String fileName) throws FileNotFoundException, IOException {
		File sortFile = new File(fileName);
		Scanner sortFileScanner = new Scanner(sortFile);

		//All the setup to make the two text files that separate the positive and negative words. Yay Chapter 19.
		File pos = new File("positive.txt");
		File neg = new File("negative.txt");
		FileOutputStream sortPosFOS = new FileOutputStream(pos);
		PrintWriter sortPosOut = new PrintWriter(sortPosFOS);
		FileOutputStream sortNegFOS = new FileOutputStream(neg);
		PrintWriter sortNegOut = new PrintWriter(sortNegFOS);

		String sortedWord = "";
		double sortedValue = 0.0;

		//This will also ignore words that don't exist in the review text file, so those don't get sorted into the negative list.
		while(sortFileScanner.hasNext()) {
			sortedWord = sortFileScanner.nextLine().toLowerCase();
			sortedValue = getWordScore(sortedWord);
			if (isInReview(sortedWord) == true) {
				if (sortedValue > 2.1) {
					sortPosOut.println(sortedWord);
				}
				if (sortedValue < 1.9) {
					sortNegOut.println(sortedWord);
				}
			}
		}
		sortPosOut.close();
		sortNegOut.close();
		System.out.println("Words have been sorted. Please check the individual text files.");
		System.out.println(" ");
	}

	/* So after a lot of trial and error, and some reading in Chapter 13 with the Try/Catch methods, I *think* I made a fully dummy-proof menu,
	* outside of not making the user re-enter a new word if they enter numbers. While it did add some code, it did have the side effect of greatly reducing
	* the amount of Scanners I had to make, which is nice. So, I'm quite happy with the result!
	*/
	public static void main(String[] args) throws FileNotFoundException, IOException {
		Scanner menu = new Scanner(System.in);
		boolean inMenu = true;
		while (inMenu == true) {
			System.out.println(" ");
			System.out.println("============USER MENU============");
			System.out.println("What would you like to do?");
			System.out.println("1: Get the score of a word.");
			System.out.println("2: Get the average score of words in a file (one word per line).");
			System.out.println("3: Find the highest/lowest scoring words in a file.");
			System.out.println("4: Sort words from a file into positive.txt and negative.txt.");
			System.out.println("5: Exit the program.");
			System.out.println(" ");
			System.out.println("Enter a number 1-5: ");
			boolean tryCatch = false;
			String fileName = "";
			String menuInput = menu.nextLine();
			switch (menuInput) {
				/* Each option has at least one "catch" built in to it, to either prevent FileNotFoundExceptions, to allow the user to back out to the main menu,
				* or in the case of the first option, to make it work even if someone types in multiple words or a sentence (it will check for a space and, if found,
				* will remove everything past and including the space before running the search with the newly truncated word). I didn't give the first the option
				* to back out since it was mainly included as a way to get out in the event of an exception on the other options.
				*/
				case "1":
					System.out.println("Enter a word: ");
					String searchWord;
					searchWord = menu.nextLine().toLowerCase();
					if (searchWord.contains(" ")) {
						System.out.println("You entered too many words. Searching first word only.");
						int searchIndex = searchWord.indexOf(' ');
						searchWord = searchWord.substring(0, searchIndex);
						singleWord(searchWord);
					}
					else {
						singleWord(searchWord);
					}
					break;
				case "2":
					tryCatch = false;
					while (tryCatch == false){
						try {
							System.out.println("Enter the name of the file with words you want to find the average score for or type 'quit' to return to the menu: ");
							fileName = menu.nextLine();
							if (fileName.compareToIgnoreCase("quit") ==  0){
								tryCatch = true;
							}
							avgWordsScore(fileName);
							tryCatch = true;
							break;
							}
						catch (FileNotFoundException e) {
							System.out.println(" ");
						}
					}
					break;
				case "3":
					tryCatch = false;
					while (tryCatch == false){
						try {
							System.out.println("Enter the name of the file with words you want to score or type 'quit' to return to the menu: ");
							fileName = menu.nextLine();
							if (fileName.compareToIgnoreCase("quit") ==  0){
								tryCatch = true;
							}
							highLowScore(fileName);
							tryCatch = true;
							break;
							}
							catch (FileNotFoundException e) {
								System.out.println(" ");
							}
						}
					break;
				case "4":
					tryCatch = false;
					while (tryCatch == false){
						try {
							System.out.println("Enter the name of the file with words you want to sort or type 'quit' to return to the menu: ");
							fileName = menu.nextLine();
							if (fileName.compareToIgnoreCase("quit") ==  0){
								tryCatch = true;
							}
							sortWord(fileName);
							tryCatch = true;
							break;
							}
							catch (FileNotFoundException e) {
								System.out.println(" ");
							}
						}
					break;
				case "5":
					inMenu = false;
					break;
				default:
					System.out.println("Please enter a number between 1 and 5.");
					System.out.println(" ");
					break;
			}
		}
	}
}
