import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

import InputOuputFile.IOFile;
import SentiWordNet.SentiWordNet;
import WordNet.WordNet;


public class SenticNetPlus {
	
	static String wnhome = "WNHOME";
	
	// Create ArrayList to store the data.
	ArrayList<String> notFoundList = new ArrayList<String>();
	ArrayList<String> neutralList = new ArrayList<String>();
	ArrayList<String> commonList_Same = new ArrayList<String>();
	ArrayList<String> commonList_Different = new ArrayList<String>();
	ArrayList<String> notCommonList_5 = new ArrayList<String>();
	ArrayList<String> notCommonList_Less5 = new ArrayList<String>();
			
	// Default Constructor
	public SenticNetPlus() { }
	
	public static void main (String [] args) throws IOException {
		Date start = new Date(); // Start of program - Used to calculate how long it takes to complete.
		SenticNetPlus SNP = new SenticNetPlus();
		
		
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter word: ");
		String inputWord = sc.nextLine();
		inputWord = inputWord.replace(" ", "_");
		String word = inputWord;
		if (inputWord.contains("#"))
			word = inputWord.substring(0, inputWord.lastIndexOf("#"));
		SNP.checkSemantic(word, inputWord);
		
		
		/*
		// Read words from the affect_verbs.txt file.
		IOFile read = new IOFile(wnhome, "affect_verbs.txt");
	    ArrayList<String> dataRead = SNP.removeFileDuplicate(read.readInputFile());
	    System.out.println(dataRead.size() + " words");
		for (int k = 0; k < dataRead.size(); k++) {
			String word, inputWord;
			word = inputWord = dataRead.get(k);//dictionary.get(k).getWord();
			
			SNP.checkSemantic(word, inputWord);
		}
		*/
		
			
//		/*// Read words from the SentiWordNet dictionary
//	    SenticNetWN SNWN = new SenticNetWN(wnhome);
//		ArrayList<SenticNetWN> dictionary = SNWN.readDictionary(); // Read WordNet Dictionary (ALL Words)
//		System.out.println(dictionary.size() + " words");
//		for (int k = 100000; k < dictionary.size(); k++) {
//			String word, inputWord;
//			word = inputWord = dictionary.get(k).getWord();
//			
//			SNP.checkSemantic(word, inputWord);
//			if (k % 1000 == 0)
//				System.out.println("Now: " + k);
//		}*/
		
		
		SNP.writeToFile();
		Date end = new Date(); // End of program
		int sec = (int) (((end.getTime() - start.getTime())/1000)%60);
		long min = (((end.getTime() - start.getTime())/1000)/60)%60;
		int hr = (int) (((end.getTime() - start.getTime())/1000)/60)/60;
		System.out.println("\n" + hr + " hours " + min + " minutes " + sec + " seconds");
	}
	
	public void writeToFile()
	{
		// Write the list of data into files.
		String[] fileName = {"notFoundList.txt", "neutralList.txt", "commonList_Same.txt", "commonList_Different.txt", "notCommonList_5.txt", "notCommonList_Less5.txt"};
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		data.add(notFoundList);
		data.add(neutralList);
		data.add(commonList_Same);
		data.add(commonList_Different);
		data.add(notCommonList_5);
		data.add(notCommonList_Less5);
		
		System.out.println();
		for (int p = 0; p < fileName.length; p++) {
			IOFile file = new IOFile("FILES", fileName[p]);
			boolean success = file.writeOutputFile(data.get(p));
	        
	        if (success)
	        	System.out.println("Successfully write data into file: " + fileName[p] + " [" + data.get(p).size() + " words]");
	        else
	        	System.out.println("Error occur in writing data into file: " + fileName[p]);
		}
	}
	
	public void checkSemantic(String word, String inputWord) throws IOException
	{
		SenticNetWN SNWN = new SenticNetWN(wnhome);
		SenticNet sn = new SenticNet(wnhome);
		ArrayList<SenticNet> snList = sn.readSenticNet();
		Collections.sort(snList);
		
		SenticNet snIW = sn.checkCommonWord(word, snList);
		if (snIW != null) // Word found in existing SenticNet
		{
			System.out.println("True - " + word + " - " + snIW.toString());
			
			ArrayList<ArrayList<SenticNetWN>> snWordsList = SNWN.getSemanticWords(inputWord);
			int counter = 0;
			
			if (snWordsList != null) {
				for (int j = 0; j < snWordsList.size(); j++) {
					counter = 0;
					ArrayList<SenticNetWN> snWords = snWordsList.get(j);
			
					String resultDisplay = "[" + word + "(" + snWords.get(0).getWord().substring(0, 1) + ")] = [";
					String result = "[" + word + "] = [";
					
					for (int i = 1; i < snWords.size(); i++) {
						resultDisplay += snWords.get(i).toString();
						result += snWords.get(i).getWord();
						counter++;
						
						//if (i < 5)
							resultDisplay += ", ";
							result += ", ";
						if (counter == 5)
							break;
					}
					
					resultDisplay += (int) snWords.get(0).getPolarity() + "]";
					result += (int) snWords.get(0).getPolarity() + "]";
					resultDisplay += " - " + counter + " words";
					
					System.out.println(resultDisplay);
					
					//for (int z = 0; z < snWords.size(); z++)
						//System.out.println(snWords.get(z).toString());
					
					if (snIW.convertPolarity().equals("" + (int) snWords.get(0).getPolarity())) {
						commonList_Same.add(snIW.toString());
						commonList_Same.add(result);
					}
					else {
						commonList_Different.add(snIW.toString());
						commonList_Different.add(result);
					}
					
					break; // Break after 1st snWords
				}
			}
			else { // Not Found List
				System.out.println("\"" + inputWord + "\" word is not found.");
				notFoundList.add(word);
			}
		}
		else // Word not found in existing SenticNet
		{
			//System.out.println("False - " + word);
			
			ArrayList<ArrayList<SenticNetWN>> snWordsList = SNWN.getSemanticWords(inputWord);
			int counter = 0;
			
			if (snWordsList != null) {
				for (int j = 0; j < snWordsList.size(); j++) {
					counter = 0;
					ArrayList<SenticNetWN> snWords = snWordsList.get(j);
					
					if (snWords.get(0).getPolarity() != 0.0) { // Non-Neutral List
						String resultDisplay = "[" + word + "(" + snWords.get(0).getWord().substring(0, 1) + ")] = [";
						String result = "[" + word + "] = [";
						
						for (int i = 1; i < snWords.size(); i++) {
							resultDisplay += snWords.get(i).toString();
							result += snWords.get(i).getWord();
							counter++;
							
							//if (i < 5)
								resultDisplay += ", ";
								result += ", ";
							if (counter == 5)
								break;
						}
						
						resultDisplay += (int) snWords.get(0).getPolarity() + "]";
						result += (int) snWords.get(0).getPolarity() + "]";
						resultDisplay += " - " + counter + " words";
						
						System.out.println(resultDisplay);
						
						//for (int z = 0; z < snWords.size(); z++)
							//System.out.println(snWords.get(z).toString());
						
						if (counter == 5)
							notCommonList_5.add(result);
						else
							notCommonList_Less5.add(result);
						
					}
					else { // Neutral List
						neutralList.add(word);
					}
					
					break; // Break after 1st snWords
				}
			}
			else { // Not Found List
				System.out.println("\"" + inputWord + "\" word is not found.");
				notFoundList.add(word);
			}
		}
	}
	
	public ArrayList<String> removeFileDuplicate(ArrayList<String> data)
	{
		Collections.sort(data);
		ArrayList<String> newData = new ArrayList<String>();
		
		newData.add(data.get(0));
		for (int i = 1; i < data.size(); i++) {
			if (!data.get(i).equals(data.get(i - 1)))
				newData.add(data.get(i));
		}
		
		return newData;
	}
	
}
