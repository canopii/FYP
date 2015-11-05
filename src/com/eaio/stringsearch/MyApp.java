package com.eaio.stringsearch;
import java.text.BreakIterator;
import java.util.*;

import SentiWordNet.SentiWordNet;


public class MyApp {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String tags="pizza tea icecream western oily pineapple spicy";
		ArrayList<String> testArrayList= new ArrayList<String>();
		testArrayList.add("pizza");
		testArrayList.add("tea");
		testArrayList.add("icecream");
		testArrayList.add("pineapple");
		//String negwords="no dislike hate";
		//String poswords="like love";
		String testString= "I like pizza and pineapple";
		String UserCho;
		List<String> keywordsarray = getWords(tags); 
		Stack<String> stackwork= new Stack<String>();
		BNDM test= new BNDM();
		List<String>ContainKey ;
		ArrayList<String>WhiteList = new ArrayList<String>();
		ArrayList<String>BlackList = new ArrayList<String>();
		ArrayList<String>combine= new ArrayList<String>();
		String result;
//		Scanner sc = new Scanner (System.in);
//		UserCho=sc.nextLine();
		
		List<String> UserInput = getWords(testString); 
		
	
		//test if user string is negative or positive
		//key is a variable for user Input
	outLoop:
		//user input is testString
		for(String key : UserInput)
		{
			boolean lester;
			System.out.println(key+" PRINTING USER WORD BY WORD");
			 if(key.equals("and"))
				{
					 result="10";
					combine.add("10");		
				}
			 else 
			 {
			    	lester=keywordMatch(key, combine);
					String polarity=polaritySearch(key);
					
			 }
		}	
			
			//test negative or positive sentance.
			//key word will be how to identify by keywords
			//key word loop ?
			// if there is keyword add inside a array then test the array with database?
			//
			//check if word is keyword, if it is key word <String> or <int>
			//white list and black list of key word
			//
		System.out.println("test for loop");
		for (String combineLoop:combine)
		{
			System.out.println(combineLoop);
		}		
		
		//keyword array loop
//		for(String key : keywordsarray)
//		{
//			//test negative or positive sentance.
//			//key word will be how to identify by keywords
//			//key word loop ?
//			// if there is keyword add inside a array then test the array with database?
//			//
//			
//			SentiWordNet SWN = new SentiWordNet("WNHOME");
//			String word = key;
//	     	
//			if (key.contains("#"))
//				word = key.substring(0, key.lastIndexOf("#"));
//			
//			String polarity = SWN.extractWordPolarity(key);
//			
//			if (polarity != null) {
//				System.out.println(word + " = " + polarity);
//			}
//			else {
//				System.out.println("\"" + polarity + "\" word is not found.");
//			}
//			
//		}

	/*	byte [] input= testString.getBytes();
		int lengthOfS;
		lengthOfS=testString.length();

		UserCho=sc.nextLine();
		int UserCholength = UserCho.length();
		
		byte []pattern = UserCho.getBytes();
		
		Object j= test.processBytes(pattern);
		
	System.out.println(test.javaSearchBytes(input, 0, lengthOfS, pattern,  j));
	System.out.println(test.javaSearchBytes(input, 0, lengthOfS, pattern,  j));*/
	}
	
	public static List<String> getWords(String text) {
	    List<String> words = new ArrayList<String>();
	    BreakIterator breakIterator = BreakIterator.getWordInstance();
	    breakIterator.setText(text);
	    int lastIndex = breakIterator.first();
	    while (BreakIterator.DONE != lastIndex) {
	        int firstIndex = lastIndex;
	        lastIndex = breakIterator.next();
	        if (lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(text.charAt(firstIndex))) {
	            words.add(text.substring(firstIndex, lastIndex));
	        }
	    }

	    return words;
	}
	
	public static String polaritySearch(String key)
	{
		SentiWordNet SWN = new SentiWordNet("WNHOME");
		String word = key;
		String returnString;
     	
		if (key.contains("#"))
			word = key.substring(0, key.lastIndexOf("#"));
		
		String polarity = SWN.extractWordPolarity(key);
	
		if (polarity != null) {
			System.out.println(word + " = " + polarity);
			returnString=polarity;
		}
		else {
			System.out.println("\"" + polarity + "\" word is not found.");
			returnString=polarity;
			}
		return returnString;
	}
	
	//key , Arraylist of keywords to match to
	public static boolean keywordMatch(String key,ArrayList<String> testArrayList)
	{
		boolean isKeyword=false;
		
		for(int i=0;i<testArrayList.size();i++)
			//(String tag: testArrayList)
			{
				// this is to check if user input = key word
				System.out.println(key+ "   STAR    " +testArrayList.get(i));
				 if(key.equals(testArrayList.get(i)))
				{
					 isKeyword=true;
					 break;
				}
				 else
					 isKeyword= false;
			}

		return isKeyword;
	}

}