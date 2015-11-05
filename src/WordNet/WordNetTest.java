package WordNet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class WordNetTest {
	
	public static void main(String [] args) throws IOException {
		
		// Create ConceptHandler object with the path to dictionary
		//System.out.println(new java.io.File( "." ).getCanonicalPath());
		WordNet WN = new WordNet("WNHOME");
		
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter word: ");
		String inputWord = sc.nextLine();
		String word = inputWord;
		if (inputWord.contains("#"))
			word = inputWord.substring(0, inputWord.lastIndexOf("#"));
		int counter = 0;
		
		ArrayList<ArrayList<String>> resultWordsList = WN.searchDictionary(inputWord);
		
		if (resultWordsList != null) {
			for (int i = 0; i < resultWordsList.size(); i++) {
				counter = 0;
				ArrayList<String> resultWords = resultWordsList.get(i);
				
				String result = "[" + word + "(" + resultWords.get(0).substring(0, 1) + ")] = [";
				
				for (int j = 1; j < resultWords.size(); j++){
					result += resultWords.get(j);
					counter++;
					
					if (j < 5)
						result += ", ";
					if (j >= 5)
						break;
				}
				
				result += "]";
				result += " - " + counter + " words";
				
				System.out.println(result);
			}
		}
		else {
			System.out.println("\"" + inputWord + "\" word is not found.");
		}
		
	}
	
}
