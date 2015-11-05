package InputOuputFile;

import java.util.ArrayList;

public class IOFileTest {
	
	public static void main(String [] args) {
/*
        // The name of the file to open.
		String wnhome = "C:\\Users\\zhihao\\Desktop";
        String iFileName = "inputFile.txt";
        String oFileName = "outputFile.txt";

        // READ
        IOFile read = new IOFile(wnhome, iFileName);
        ArrayList<String> dataRead = read.readInputFile();
        
        for (String data: dataRead) {
        	System.out.println(data);
        	
        	String[] d = data.split(" ");
        	for (int k = 0; k < d.length; k++)
        		System.out.println("-" + d[k] + "-");
        	
        	break;
        }
        
        // WRITE
        ArrayList<String> dataWrite = new ArrayList<String>();
        
        for (String data: dataRead) {
        	String[] dataArr = data.split(" ");
        	
        	String word = dataArr[0].substring(dataArr[0].indexOf("\"") + 1, dataArr[0].lastIndexOf("\""));
        	String [] sWord = new String[5];
        	for (int i = 0; i < sWord.length; i++)
        		sWord[i] = dataArr[i+7].substring(dataArr[i+7].indexOf("\'") + 1, dataArr[i+7].lastIndexOf("\'"));
        	String polarity = dataArr[6].substring(0, dataArr[6].lastIndexOf(","));
        	
        	String newData = "[" + word + "] = [";
        	for (int i = 0; i < sWord.length; i++)
        		newData += sWord[i] + ", ";
        	newData += polarity + "]";
        	
        	dataWrite.add(newData);
        }
        
        IOFile write = new IOFile(wnhome, oFileName);
        boolean success = write.writeOutputFile(dataWrite);
        
        if (success)
        	System.out.println("Successfully write data into file: " + oFileName);
*/
        
		String[] directory = {"SWN (0-49999)", "SWN (50000-99999)", "SWN (100000-146763)"};
		String[] fileName = {"commonList_Different.txt", "commonList_Same.txt", "neutralList.txt", "notCommonList_5.txt", "notCommonList_Less5.txt", "notFoundList.txt"};
		
		for (int j = 0; j < fileName.length; j++) {
			ArrayList<String> newData = new ArrayList<String>();
			for (int i = 0; i < directory.length; i++) {
				IOFile swn = new IOFile("FILES\\SWN\\" + directory[i], fileName[j]);
				ArrayList<String> swnData = swn.readInputFile();
				
				for (String data: swnData)
					newData.add(data);
			}
			
			IOFile newFile = new IOFile("FILES\\SentiWordNet_Dictionary", fileName[j]);
			boolean success = newFile.writeOutputFile(newData);
			
			System.out.println(success + ": " + newData.size());
		}
    }
	
}
