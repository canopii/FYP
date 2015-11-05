package WordNet;
/*
 * FileName: ConceptHandler.java
 * -----------------------------
 * This class provides methods to search wordNet dictionary using MIT JWI.
 * http://projects.csail.mit.edu/jwi/
 */
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import SentiWordNet.SentiWordNet;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

public class WordNet {
	
	private String wnhome = "";
	
	// Default Constructor
	public WordNet() { }
	
	// Non-Default Constructor
	public WordNet(String wnhome) {
		this.wnhome = wnhome;
	}
	
	// Search the WordNet dictionary for the searchWord
	public ArrayList<ArrayList<String>> searchDictionary(String searchWord) throws IOException {
		
		searchWord = searchWord.replace(" ", "_");
		
		String wordSearch = "", pos = "";
		if (searchWord.contains("#")) {
			wordSearch = searchWord.substring(0, searchWord.lastIndexOf("#"));
			pos = searchWord.substring(searchWord.lastIndexOf("#") + 1);
		}
		else {
			wordSearch = searchWord;
		}
		
		ArrayList<String> resultWords = null;
		ArrayList<ArrayList<String>> resultWordsList = null;
		POS [] posArr = {POS.NOUN, POS.VERB, POS.ADJECTIVE};
		
		// Construct the URL to the Wordnet dictionary directory
		// String wnhome = System.getenv("WNHOME");
		String path = wnhome + File.separator + "dict";
		URL url = new URL("file", null, path);
		
		// Construct the dictionary object and open it
		IDictionary dict = new Dictionary(url);
		dict.open();
		
		try {
			// Get the synset
			IIndexWord idxWord = null;
			ArrayList<IIndexWord> idxWordList = new ArrayList<IIndexWord>();
			
			if (pos.length() != 0) {
				if (pos.equals("n"))
					idxWord = dict.getIndexWord(wordSearch, POS.NOUN);
				else if (pos.equals("v"))
					idxWord = dict.getIndexWord(wordSearch, POS.VERB);
				else if (pos.equals("a"))
					idxWord = dict.getIndexWord(wordSearch, POS.ADJECTIVE);
				
				if (idxWord != null) {
					idxWordList.add(idxWord);
				}
				else {
					for (int i = 0; i < posArr.length; i++) {
						idxWord = dict.getIndexWord(wordSearch, posArr[i]);
						
						if (idxWord != null) {
							idxWordList.add(idxWord);
						}
					}
				}
			}
			else {
				for (int i = 0; i < posArr.length; i++) {
					idxWord = dict.getIndexWord(wordSearch, posArr[i]);
					
					if (idxWord != null) {
						idxWordList.add(idxWord);
					}
				}
			}
			
			if (idxWordList.isEmpty() == false) {
				resultWordsList = new ArrayList<ArrayList<String>>();
				
				for (int counter = 0; counter < idxWordList.size(); counter++) {
					resultWords = new ArrayList<String>();
					
					IWordID wordID = idxWordList.get(counter).getWordIDs().get(0); // 1st meaning
					IWord word = dict.getWord(wordID);
					ISynset synset = word.getSynset();
					
					String resultPOS = idxWordList.get(counter).getPOS().toString();
					resultWords.add(resultPOS);
					
					if (resultPOS.equals("adjective")) {
						// Get the similar_to
						List<ISynsetID> similar_to = synset.getRelatedSynsets(Pointer.SIMILAR_TO);
						
						// Add each similar_to into the resultWords
						List<IWord> words;
						for (ISynsetID sid : similar_to){
							words = dict.getSynset(sid).getWords();
							for (Iterator<IWord> i = words.iterator(); i.hasNext();){
								//if (resultWords.size() < 6)
									resultWords.add(i.next().getLemma());
								//else
									//break;
							}
						}
					}
					else {
						// Get the hyponyms
						List<ISynsetID> hyponyms = synset.getRelatedSynsets(Pointer.HYPONYM);
						
						// Add each hyponyms into the resultWords
						List<IWord> words;
						for (ISynsetID sid : hyponyms){
							words = dict.getSynset(sid).getWords();
							for (Iterator<IWord> i = words.iterator(); i.hasNext();){
								//if (resultWords.size() < 6)
									resultWords.add(i.next().getLemma());
								//else
									//break;
							}
						}
						
						//if (resultWords.size() < 6){
							// Get the hypernyms
							List<ISynsetID> hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);
							
							// Add each hypernyms into the resultWords
							for (ISynsetID sid : hypernyms){
								words = dict.getSynset(sid).getWords();
								for (Iterator<IWord> i = words.iterator(); i.hasNext();){
									//if (resultWords.size() < 6)
										resultWords.add(i.next().getLemma());
									//else
										//break;
								}
							//}
						}
					}
					
					resultWordsList.add(resultWords);
				}
			}
		}
		catch (Exception e) {
			resultWordsList = null;
		}
		
		return resultWordsList;
	}
	
}
