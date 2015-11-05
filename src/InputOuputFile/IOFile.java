package InputOuputFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class IOFile {

	private String ioFile = "";
	private String wnhome = "";
	private String fileName = "";
	
	// Default Constructor
	public IOFile() { }
	
	// Non-Default Constructor
	public IOFile(String wnhome, String fileName) { 
		ioFile = wnhome + File.separator + fileName;
		this.wnhome = wnhome;
		this.fileName = fileName;
	}
	
	public ArrayList<String> readInputFile() {
		String line = null;
		ArrayList<String> lines = new ArrayList<String>();
		
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(ioFile);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
            	lines.add(line);
            }    

            // Always close files.
            bufferedReader.close();            
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");                   
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        
        return lines;
	}
	
	public boolean writeOutputFile(ArrayList<String> lines) {
		boolean success = false;
		
		try {
            // Assume default encoding.
            FileWriter fileWriter = new FileWriter(ioFile);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Note that write() does not automatically append a newline character.
            for (String line : lines) {
            	bufferedWriter.write(line);
            	bufferedWriter.newLine();
            }
            
            // Always close files.
            bufferedWriter.close();
            
            success = true;
        }
        catch(IOException ex) {
            System.out.println("Error writing to file '" + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
		
		return success;
	}
	
}
