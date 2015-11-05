package SentiWordNet;
//    Copyright 2013 Petter Törnberg
//
//    This demo code has been kindly provided by Petter Törnberg <pettert@chalmers.se>
//    for the SentiWordNet website.
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SentiWordNet {

	private String wnhome = "";
	
	public static Map<String, Double> dictionary;
	public static Map<String, Double> dictionaryWord;

	// Default Constructor
	public SentiWordNet() { }
	
	// Non-Default Constructor
	public SentiWordNet(String wnhome) {
		this.wnhome = wnhome;
		
		try {
			formatDictionary();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void formatDictionary() throws IOException{
		
		String pathToSWN = wnhome + File.separator + "SentiWordNet_JunSheng.txt";
		
		// This is our main dictionary representation
		dictionary = new HashMap<String, Double>();
		dictionaryWord = new HashMap<String, Double>();

		// From String to list of doubles.
		HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();

		BufferedReader csv = null;
		try {
			csv = new BufferedReader(new FileReader(pathToSWN));
			int lineNumber = 0;

			String line;
			while ((line = csv.readLine()) != null) {
				lineNumber++;

				// If it's a comment, skip this line.
				if (!line.trim().startsWith("#")) {
					// We use tab separation
					String[] data = line.split("\t");
					String wordTypeMarker = data[0];
					
					// Example line:
					// POS ID PosS NegS SynsetTerm#sensenumber Desc
					// a 00009618 0.5 0.25 spartan#4 austere#3 ascetical#2 ascetic#2 practicing great self-denial;...etc

					// Is it a valid line? Otherwise, through exception.
					if (data.length != 6) {
						throw new IllegalArgumentException("Incorrect tabulation format in file, line: " + lineNumber);
					}

					// Calculate synset score as score = PosS - NegS
					Double synsetScore = Double.parseDouble(data[2]) - Double.parseDouble(data[3]);

					// Get all Synset terms
					String[] synTermsSplit = data[4].split(" ");

					// Go through all terms of current synset.
					for (String synTermSplit : synTermsSplit) {
						// Get synterm and synterm rank
						String[] synTermAndRank = synTermSplit.split("#");
						String synTerm = synTermAndRank[0] + "#" + wordTypeMarker;

						int synTermRank = Integer.parseInt(synTermAndRank[1]);
						// What we get here is a map of the type:
						// term -> {score of synset#1, score of synset#2...}

						// Add map to term if it doesn't have one
						if (!tempDictionary.containsKey(synTerm)) {
							tempDictionary.put(synTerm,	new HashMap<Integer, Double>());
						}

						// Add synset link to synterm
						tempDictionary.get(synTerm).put(synTermRank, synsetScore);
					}
				}
			}

			// Go through all the terms.
			for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary.entrySet()) {
				String word = entry.getKey();
				Map<Integer, Double> synSetScoreMap = entry.getValue();

				// Calculate weighted average. Weigh the synsets according to their rank.
				// Score= 1/2*first + 1/3*second + 1/4*third ..... etc.
				// Sum = 1/1 + 1/2 + 1/3 ...
				double score = 0.0;
				double sum = 0.0;
				for (Map.Entry<Integer, Double> setScore : synSetScoreMap.entrySet()) {
					score += setScore.getValue() / (double) setScore.getKey();
					sum += 1.0 / (double) setScore.getKey();
					
					if (setScore.getKey() != 1) // Only calculate #1
						break;
				}
				score /= sum;
				
				word = word.replace("-", "_");
				
				dictionary.put(word, score);
				if (dictionaryWord.containsKey(word.substring(0, word.lastIndexOf("#")))) {
					if (score != 0)
						dictionaryWord.replace(word.substring(0, word.lastIndexOf("#")), score);
				}
				else {
					dictionaryWord.put(word.substring(0, word.lastIndexOf("#")), score);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (csv != null) {
				csv.close();
			}
		}
	}
	
	public String extract(String word, String pos) {
		
		try {
			double polarity = dictionary.get(word + "#" + pos);
			String polarityStr = "";
			// i added two for wild card 
			// define key words as wild card if + is in frount of wild card, it is postive.
			//else if wild card is first then need check the next polarity of the word to determine like or dislike
			if(polarity >1.0)
				polarityStr="2";
			else if (polarity > 0.0)
				polarityStr = "1";
			else if (polarity < 0.0)
				polarityStr = "-1";
			else if (polarity == 0.0)
				polarityStr = "0";
			else
				polarityStr = null;
			
			//System.out.println(word + "(" + polarity + ")");
			return polarityStr;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	// Extract the converted Polarity
	public String extractWordPolarity(String searchWord) {
//		 System.out.println("working");
		searchWord = searchWord.replace(" ", "_");
		
		String word = "", pos = "";
		if (searchWord.contains("#")) {
			word = searchWord.substring(0, searchWord.lastIndexOf("#"));
			pos = searchWord.substring(searchWord.lastIndexOf("#") + 1);
		}
		else {
			word = searchWord;
		}
		
		double polarity = 0.0;
		String polarityStr = null;
		
		if (pos.length() != 0) {
			try {
				polarity = dictionary.get(word + "#" + pos);
				
				if(polarity >1.0)
				{	polarityStr="2";
					System.out.println(polarity + "Test");
				}
				else if (polarity > 0.0)
					polarityStr = "1";
				else if (polarity < 0.0)
					polarityStr = "-1";
				else if (polarity == 0.0)
					polarityStr = "0";
				else
					polarityStr = null;
			}
			catch (Exception e) {
				polarityStr = null;
			}
		}
		
		if (polarityStr == null) {
			try {
				polarity = dictionaryWord.get(word);
				
				if (polarity > 0.0)
					polarityStr = "1";
				else if (polarity < 0.0)
					polarityStr = "-1";
				else if (polarity == 0.0)
					polarityStr = "0";
				else
					polarityStr = null;
			}
			catch (Exception e) {
				polarityStr = null;
			}
		}
		
		//System.out.println(word + "(" + polarity + ")");
		return polarityStr;
	}
	
	// Extract the default Polarity
	public static Object extractPolarity(String searchWord) {
		
		searchWord = searchWord.replace(" ", "_");
		
		String word = "", pos = "";
		if (searchWord.contains("#")) {
			word = searchWord.substring(0, searchWord.lastIndexOf("#"));
			pos = searchWord.substring(searchWord.lastIndexOf("#") + 1);
		}
		else {
			word = searchWord;
		}
		
		double polarity = 0.0;
		boolean polarityFound = false;
		
		if (pos.length() != 0) {
			try {
				polarity = dictionary.get(word + "#" + pos);
				polarityFound = true;
			}
			catch (Exception e) {
				return null;
			}
		}
		
		if (polarityFound == false) {
			try {
				polarity = dictionaryWord.get(word);
			}
			catch (Exception e) {
				return null;
			}
		}
		
		return polarity;
	}
	
}