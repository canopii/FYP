package SentiWordNet;

import java.io.IOException;
import java.util.Scanner;

public class SentiWordNetTest {

	public static void main(String [] args) throws IOException {
		
		//System.out.println(new java.io.File( "." ).getCanonicalPath());
		SentiWordNet SWN = new SentiWordNet("WNHOME");
		
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter word: ");
		String inputWord = sc.nextLine();
		String word = inputWord;
		if (inputWord.contains("#"))
			word = inputWord.substring(0, inputWord.lastIndexOf("#"));
		
		String polarity = SWN.extractWordPolarity(inputWord);
		int polarityInt = Integer.parseInt(polarity);
		if (polarity != null) {
			System.out.println(word + " = " + polarityInt);
		}
		else {
			System.out.println("\"" + polarityInt + "\" word is not found.");
		}
		
	}
	
}
