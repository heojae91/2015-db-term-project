package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.StringTokenizer;

public class TestField {

	public void readTextFile() {
		System.out.println("Reading File from Java code");
		// Name of the file
		String fileName = "C:/Users/Ashton Heo/workspace/2015-db-term-project/data.txt";
		String[] splittedString = new String[0];
		
		try {
			
			// Create object of FileReader
			FileReader inputFile = new FileReader(fileName);

			// Instantiate the BufferedReader Class
			BufferedReader bufferReader = new BufferedReader(inputFile);

			// Variable to hold the one line data
			String line;

			// Read file line by line and print on the console
			while ((line = bufferReader.readLine()) != null)
			{
				if (line.matches("\\d+"))
					System.out.println(line + "¼ýÀÚ");
				else {
					splittedString = splitLine(line);
				}
				
				int attributes = splittedString.length;
				
				switch (attributes) {
					case 4:
						//customer
						System.out.println("customer");
						for (int i = 0; i < attributes; i++)
						{
							System.out.println(splittedString[i]);
						}

						break;
					case 3:
						//staff
						System.out.println("staff");
						for (int i = 0; i < attributes; i++)
						{
							System.out.println(splittedString[i]);
						}

						break;
					case 2:
						// menu
						
						System.out.println("menu");
						for (int i = 0; i < attributes; i++)
						{
							System.out.println(splittedString[i]);
						}
						break;
					case 0 :
						System.out.println();
				}
			}
			// Close the buffer reader
			bufferReader.close();
		}
		catch (Exception e) {
			System.out.println("Error while reading file line by line:"
				+ e.getMessage());
		}

	}
	
	public String[] splitLine(String line)
	{
		String[] splittedStrings;
		splittedStrings = line.split("\t");
		for (int i = 0; i < splittedStrings.length; i++)
		{
			System.out.println(splittedStrings[i]);
		}
		return splittedStrings;
	}

	public static void main(String[] args) {
		new TestField().readTextFile();
	}
}
