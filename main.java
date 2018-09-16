/*-------------------------
---------------------------

Module 3 Assignment
Cayla JS Parks
CEN3024 - Software Development I, Prof. Shah
9/15/18

main processing class

---------------------------
-------------------------*/

package module3_spellcheck;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.BreakIterator;

public class main
{
	public static ArrayList<String> listWords(String input) {
	    ArrayList<String> wordsList = new ArrayList<String>();
	    int i = input.indexOf(' ');
	    if (i == -1)	// no spaces (i.e. no words) found, just return original input.
	    {
	    	wordsList.add(input);
	    	return wordsList;
	    }
	    else			// there is at least one space (words indicated)
	    {
		    BreakIterator wordBoundary = BreakIterator.getWordInstance();
		    wordBoundary.setText(input);
		    int start = wordBoundary.first();
		    int end = wordBoundary.next();
		    while (end != BreakIterator.DONE)
		    {
		    	if (Character.isLetterOrDigit(input.charAt(start)))
		    		wordsList.add(input.substring(start, end));
		        start = end;
		        end = wordBoundary.next();
		    }
		    return wordsList;
	    }
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{
		String kbInput;
		Scanner scaninput = new Scanner(System.in);
		Boolean inputQuit = false;
		String nl = System.getProperty("line.separator");	// "new line" shortcut string
		String rootHelpText = "Valid commands:" + nl + 
						  "'spellcheck <filename.txt> <dictionary.txt>' to spellcheck a file" + nl + 
						  "'exit', 'quit', or 'x' to exit program." + nl + 
						  "'help', or '?' to display this list of commands.";
		String spellcheckHelpText = 
						  "Missing or excessive parameters for 'spellcheck' command.  Valid usage:" + nl + 
						  "    spellcheck <filename.txt> <dictionary.txt>";
		
		System.out.println(rootHelpText);
		
		//main command line loop
		while (inputQuit == false)
		{
			System.out.println(" ");
			kbInput = scaninput.nextLine();
			ArrayList<String> parseCommand = listWords(kbInput);

			switch (parseCommand.get(0))
			{
				case "spellcheck":
				{
					/*
					 * command parse debug
					for(int i = 0; i < parseCommand.size(); i++)
					{
					System.out.println(parseCommand.get(i));
					}
					*/
					if (parseCommand.size() != 3)	// we're missing one or more parameters
					{
						System.out.println(spellcheckHelpText);
					}
					else	// "spellcheck" command has valid syntax
					{
						File fileText = new File(parseCommand.get(1));
						File fileDict = new File(parseCommand.get(2));
						
						// test for files:
						if (fileText.exists() == false || fileDict.exists() == false)
							System.out.println("One or more specified files do not exist.");
						else	// both files exist;  we're good to go!
						{
							// read in both files, concatenate each file's line into respective files' strings.
							String strFileText = "";
							String strFileDict = "";
							Scanner fileInput = new Scanner(fileText);
							while (fileInput.hasNext())
							{
								strFileText = strFileText + " " + fileInput.nextLine();
							}
							fileInput.close();
							
							fileInput = new Scanner(fileDict);
							while (fileInput.hasNext())
							{
								strFileDict = strFileDict + " " + fileInput.nextLine();
							}
							fileInput.close();
							
							// break out strings into ArrayLists, so their elements can be compared
							ArrayList<String> textList = listWords(strFileText);
							ArrayList<String> dictList = listWords(strFileDict);
							
							/*
							 * Debugging messages:
							System.out.println(strFileText);
							System.out.println(strFileDict);
							System.out.println(dictList.get(5));
							*/
							
							// compare elements, and record each textList element with no match in dictList
							ArrayList<String> mismatches = new ArrayList<String>();
							Boolean Match = false;
							for(int i = 0; i < textList.size(); i++)
							{
								Match = false;
								for(int j = 0; j < dictList.size(); j++)
								{
									//System.out.println(textList.get(i)+ " == " + dictList.get(j) + "?");
									if (textList.get(i).equals(dictList.get(j)))
									{
										//System.out.println(textList.get(i)+ " == " + dictList.get(j));
										Match = true;
										break;
									}
								}
								if (Match == false)
									mismatches.add(textList.get(i));
									//System.out.println(mismatches.get(i));
							}
								
							// display all mismatches
							if (mismatches.isEmpty())
							{
								System.out.println("No unknown words were detected.");
							}
							else
							{
								System.out.println("The following unknown words were detected:");
								for(int i = 0; i < mismatches.size(); i++)
								{
									System.out.println(mismatches.get(i));
								}
							}
						}
					}
						
					break;
				}
				
				case "quit":
				case "exit":
				case "x":
				{
					//Exit, pursued by a bear.
					
					System.out.println(" ");
					System.out.println("Good-bye!");
					inputQuit = true;
					scaninput.close();
					break;
				}
				
				case "help":
				case "?":
				{
					//Help menu
					System.out.println(rootHelpText);
					break;
				}
				
				default:
				{
					//invalid entry.  give an error message, stay in the while loop
					System.out.println(" ");
					System.out.println("Invalid or unrecognized command entered.");
					break;
				}
			}
		}
	}

}
