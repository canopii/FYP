import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import SentiWordNet.SentiWordNet;
import WordNet.WordNet;


public class SenticNetWN implements Comparable<SenticNetWN> {

	static String condition = "";
	private static String wordPolarity = null;
	private String wnhome = "";
	
	// Attributes
	private String word;
	private double polarity;
	
	// Default Constructor
	public SenticNetWN() { }
	
	// Non-Default Constructor
	public SenticNetWN(String wnhome) {
		this.wnhome = wnhome;
	}
	public SenticNetWN(String word, double polarity) {
		this.word = word;
		this.polarity = polarity;
	}
	
	// Accessor Methods
	public void setWord(String word) {
		this.word = word;
	}
	public String getWord() {
		return word;
	}
	public void setPolarity(double polarity) {
		this.polarity = polarity;
	}
	public double getPolarity() {
		return polarity;
	}
	
	// CompareTo Method
	public int compareTo(SenticNetWN wn) {
		int result = 0;
		
		if (condition.equals("p")){
			Double p1 = polarity;
			Double p2 = wn.polarity;
			
			if (wordPolarity.equals("-1"))
				result = p1.compareTo(p2);
			else if (wordPolarity.equals("1"))
				result = p2.compareTo(p1);
		}
		else {
			result = word.compareTo(wn.word);
		}
		
		return result;
	}

	// ToString Method
	public String toString() {
		String p;
		if (polarity > 0.0)
			p = "1";
		else if (polarity < 0.0)
			p = "-1";
		else if (polarity == 0.0)
			p = "0";
		else
			p = null;
		
		return word + "(" + p + ")";
	}
	
	// Get the Semantic Words
	public ArrayList<ArrayList<SenticNetWN>> getSemanticWords(String searchWord) throws IOException {
		
		String word = "", pos = "";
		if (searchWord.contains("#")) {
			word = searchWord.substring(0, searchWord.lastIndexOf("#"));
			pos = searchWord.substring(searchWord.lastIndexOf("#") + 1);
		}
		else {
			word = searchWord;
		}
		
		WordNet WN = new WordNet(wnhome);
		SentiWordNet SWN = new SentiWordNet(wnhome);
		
		ArrayList<ArrayList<String>> sWordsList = WN.searchDictionary(searchWord);
		
		ArrayList<SenticNetWN> snWords = null;
		ArrayList<SenticNetWN> snWordsNeutral = null;
		ArrayList<ArrayList<SenticNetWN>> snWordsList = null;
		
		if (sWordsList != null) {
			snWordsList = new ArrayList<ArrayList<SenticNetWN>>();
			
			for (int i = 0; i < sWordsList.size(); i++) {
				ArrayList<String> sWords = sWordsList.get(i);
				wordPolarity = SWN.extractWordPolarity(word + "#" + sWords.get(0).substring(0, 1));
				if (wordPolarity == null)
					break;
				snWords = new ArrayList<SenticNetWN>();
				snWordsNeutral = new ArrayList<SenticNetWN>();
				
				for (int j = 1; j < sWords.size(); j++) {
					String w = sWords.get(j);
					double p;
					try {
						p = Double.parseDouble(SentiWordNet.extractPolarity(w).toString());
					}
					catch (Exception e) {
						continue;
					}
					
					if (p == 0.0 && !wordPolarity.equals("0")) {
						if (checkCommonWord(w, snWordsNeutral) == false)
							snWordsNeutral.add(new SenticNetWN(w, p));
					}
					else {
						if (checkCommonWord(w, snWords) == false)
							snWords.add(new SenticNetWN(w, p));
					}
				}
				
				SenticNetWN.condition = "p";
				Collections.sort(snWords);
				
				for (SenticNetWN n : snWordsNeutral)
					snWords.add(n);
				
				snWords.add(0, new SenticNetWN(sWords.get(0), Double.parseDouble(wordPolarity)));
				snWordsList.add(snWords);
			}
			
			if (wordPolarity == null)
				snWordsList = null;
		}
		
		return snWordsList;
	}
	
	// Check Common Word
	public boolean checkCommonWord(String word, ArrayList<SenticNetWN> snWordsList) {
		boolean common = false;
		
		if (checkBE(word)) {
			common = true;
		}
		else {
			for (SenticNetWN snWord : snWordsList) {
				if (snWord.word.equals(word)) {
					common = true;
					break;
				}
			}
		}
		
		return common;
	}
	
	// Read SentiWordNet Dictionary
	public ArrayList<SenticNetWN> readDictionary() {
		ArrayList<SenticNetWN> dictionary = new ArrayList<SenticNetWN>();
		
		new SentiWordNet(wnhome);
		Object[] keyArr = SentiWordNet.dictionaryWord.keySet().toArray();
		Object[] valueArr = SentiWordNet.dictionaryWord.values().toArray();
		
		for (int i = 0; i < keyArr.length; i++) {
			String word = keyArr[i].toString();
			if (checkBE(word) == false) {
				double polarity = Double.parseDouble(valueArr[i].toString());
				dictionary.add(new SenticNetWN(word, polarity));
			}
		}
		
		SenticNetWN.condition = "w";
		Collections.sort(dictionary);
		
		return dictionary;
	}
	
	// Check for British English words
	public boolean checkBE(String word) {
		boolean BE = false;
		String [] endWords = {"bre", "tre", "tred", "our", "ise", "ised", "iser", "isation", "yse", "yser"};
		String [] words = word.split("_");
		
		for (String w : words) {
			for (int i = 0; i < endWords.length; i++) {
				if (w.endsWith(endWords[i])) {
					BE = true;
					break;
				}
			}
			
			if (BE)
				break;
		}
		
		return BE;
	}
	
}
