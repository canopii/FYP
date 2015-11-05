import java.util.ArrayList;

import InputOuputFile.IOFile;


public class SenticNet implements Comparable<SenticNet> {

	private String wnhome = "";
	
	// Attributes
	private String word;
	private String[] semantics;
	private double pleasantness;
	private double attention;
	private double sensitivity;
	private double aptitude;
	private double polarity;
	
	// Default Constructor
	public SenticNet() { }
	
	// Non-Default Constructor
	public SenticNet(String wnhome) {
		this.wnhome = wnhome;
	}
	
	public SenticNet(String word, String[] semantics, double pleasantness, double attention, double sensitivity, double aptitude, double polarity) {
		this.word = word;
		this.semantics = semantics;
		this.pleasantness = pleasantness;
		this.attention = attention;
		this.sensitivity = sensitivity;
		this.aptitude = aptitude;
		this.polarity = polarity;
	}
	
	// Accessor Methods
	public void setWord(String word) {
		this.word = word;
	}
	public String getWord() {
		return word;
	}
	public void setSemantics(String[] semantics) {
		this.semantics = semantics;
	}
	public String[] getSemantics() {
		return semantics;
	}
	public void setPleasantness(double pleasantness) {
		this.pleasantness = pleasantness;
	}
	public double getPleasantness() {
		return pleasantness;
	}
	public void setAttention(double attention) {
		this.attention = attention;
	}
	public double getAttention() {
		return attention;
	}
	public void setSensitivity(double sensitivity) {
		this.sensitivity = sensitivity;
	}
	public double getSensitivity() {
		return sensitivity;
	}
	public void setAptitude(double aptitude) {
		this.aptitude = aptitude;
	}
	public double getAptitude() {
		return aptitude;
	}
	public void setPolarity(double polarity) {
		this.polarity = polarity;
	}
	public double getPolarity() {
		return polarity;
	}
	
	// CompareTo Method
	public int compareTo(SenticNet sn) {
		int result = 0;
		result = word.compareTo(sn.word);
			
		return result;
	}
	
	// ToString Method
	public String toString() {
		String result = "[" + word + "] = [";
		for (int i = 0; i < semantics.length; i++) {
			result += semantics[i];
			result += ", ";
		}
		//result += polarity + "]";
		result += convertPolarity() + "]";
		
		return result;
	}
	
	// Convert Polarity
	public String convertPolarity() {
		String p;
		if (polarity > 0.0)
			p = "1";
		else if (polarity < 0.0)
			p = "-1";
		else if (polarity == 0.0)
			p = "0";
		else
			p = null;
		
		return p;
	}

	// Check Common Word
	public SenticNet checkCommonWord(String word, ArrayList<SenticNet> snList) {
		SenticNet snCommon = null;
		
		for (SenticNet sn : snList) {
			if (sn.word.equals(word)) {
				snCommon = sn;
				break;
			}
		}
		
		return snCommon;
	}
	
	// Read SenticNet
	public ArrayList<SenticNet> readSenticNet() {
		ArrayList<SenticNet> senticNetList = new ArrayList<SenticNet>();
		
		IOFile snFile = new IOFile(wnhome, "senticnet.py");
		ArrayList<String> snConceptList = snFile.readInputFile();
		
		String cWord; String[] cSemantics;
		double cPleasantness, cAttention, cSensitivity, cAptitude, cPolarity;
		
		for (String snConcept : snConceptList) {
			if (!snConcept.equals("senticnet = {}")) 
			{
				String[] snC = snConcept.split(" ");
				
				cWord = snC[0].substring(11, snC[0].length() - 2);
				cSemantics = new String[5];
				cSemantics[0] = snC[7].substring(1, snC[7].lastIndexOf("'"));
				cSemantics[1] = snC[8].substring(1, snC[8].lastIndexOf("'"));
				cSemantics[2] = snC[9].substring(1, snC[9].lastIndexOf("'"));
				cSemantics[3] = snC[10].substring(1, snC[10].lastIndexOf("'"));
				cSemantics[4] = snC[11].substring(1, snC[11].lastIndexOf("'"));
				
				cPleasantness = Double.parseDouble(snC[2].substring(1, snC[2].lastIndexOf(",")));
				cAttention = Double.parseDouble(snC[3].substring(0, snC[3].lastIndexOf(",")));
				cSensitivity = Double.parseDouble(snC[4].substring(0, snC[4].lastIndexOf(",")));
				cAptitude = Double.parseDouble(snC[5].substring(0, snC[5].lastIndexOf(",")));
				cPolarity = Double.parseDouble(snC[6].substring(0, snC[6].lastIndexOf(",")));
				
				senticNetList.add(new SenticNet(cWord, cSemantics, cPleasantness, cAttention, cSensitivity, cAptitude, cPolarity));
			}
		}
		
		return senticNetList;
	}
	
}
