package com.idega.block.importer.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import com.idega.block.importer.business.NoRecordsException;
import com.idega.util.Timer;
import com.idega.util.text.TextSoap;

/**
 * <p>Title: IdegaWeb classes</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Idega Software</p>
 * @author <a href="mailto:eiki@idega.is"> Eirikur Sveinn Hrafnsson</a>
 * @version 1.0
 */

public class GenericImportFile implements ImportFile{
	
	  private File file;
	  private String recordDilimiter = "\n";
	  private String valueSeparator = ";";
	  private String emptyValueString = " ";
	  private FileReader fr;
	  private BufferedReader br;
	  private boolean addNewLineAfterRecord = false;

	  public GenericImportFile(){}
	
	  public GenericImportFile(File file) {
	    this.file = file;
	  }
	
	  /**
	  *
	  * @return the String value of recordDilimiter.
	  */
	  public String getRecordDilimiter(){
	        return recordDilimiter;
	  }
	
	  /**
	  *
	  * @param aRecordDilimiter - the new value for recordDilimiter
	  */
	  public void setRecordDilimiter(String aRecordDilimiter){
	        recordDilimiter = aRecordDilimiter;
	  }
	
	
	 // public Collection getRecords(){return null;}
	 // public Object getRecordAtIndex(int i){return null;}
	
	 /**
	  * This method works like an iterator. When the end of the file is reached it returns Null
	  */
	 public Object getNextRecord(){
	  String line;
	  StringBuffer buf = new StringBuffer();
	
	  try {
	    if( fr == null ){
	      fr = new FileReader(getFile());
	      br = new BufferedReader(fr);
	    }
	
		while ( ( (line=br.readLine()) != null ) && ( line.indexOf(getRecordDilimiter())== -1 ) ){
	      buf.append(line);
	      if( addNewLineAfterRecord ){
	      	buf.append('\n');
	      }
	      
	      if( getRecordDilimiter().equals("\n") ) break;//need to check because readline strips this token away.
	      
	    }
	
	    return buf.toString();
	  }
	  catch( FileNotFoundException ex ){
	    ex.printStackTrace(System.err);
	    return null;
	  }
	  catch( IOException ex ){
	    ex.printStackTrace(System.err);
	    return null;
	  }
	
	
	 }
	
	 /**
	  * @deprecated
	  * This method parses the file into records (ArrayList) and returns the complete list.<p>
	  *  it throws a NoRecordsFoundException if no records where found.
	  */
	  public Collection getRecords() throws NoRecordsException{
	
	    try{
	      fr = new FileReader(getFile());
	      br = new BufferedReader(fr);
	      String line;
	      StringBuffer buf = new StringBuffer();
	      ArrayList list = new ArrayList();
	
	      int cnt = 0;
	      int records = 0;
	
	      Timer clock = new Timer();
	      clock.start();
	
	      while ( (line=br.readLine()) != null){
	        if( buf == null ){
	          buf = new StringBuffer();
	        }
	
	        buf.append(line);
	
	        /**@todo this should be an option with a setMethod?**/
	    if(addNewLineAfterRecord){
	    	buf.append('\n');
	    }
	
	    if( line.indexOf(getRecordDilimiter())!= -1 ){
	      records++;
	      if( (records % 1000) == 0 ){
	        System.out.println("Importer: Reading record nr.: "+records+" from file "+getFile().getName());
	      }
	
	     list.add(buf.toString());
	     buf = null;
	    }
	
	    cnt++;
	  }
	
	  line = null;
	  buf = null;
	
	  br.close();
	  fr = null;
	  br = null;
	
	  clock.stop();
	
	  if( records == 0 ){
	   throw new NoRecordsException("No records where found in the selected file"+file.getAbsolutePath());
	  }
	
	  //System.gc();
	
	  System.out.println("Time for operation: "+clock.getTime()+" ms  OR "+((int)(clock.getTime()/1000))+" s");
	  System.out.println("Number of Lines: "+cnt);
	  System.out.println("Number of records = "+records);
	
	      return list;
	    }
	    catch( FileNotFoundException ex ){
	      ex.printStackTrace(System.err);
	      return null;
	    }
	    catch( IOException ex ){
	      ex.printStackTrace(System.err);
	      return null;
	    }
	
	  }
	
	
	
	
	  public File getFile(){
	    return file;
	  }

	/**
	 * Returns the addNewLineAfterRecord.
	 * @return boolean
	 */
	public boolean isAddNewLineAfterRecord() {
		return addNewLineAfterRecord;
	}
	
	/**
	 * Sets if to add a \n after each record
	 * @param addNewLineAfterRecord The addNewLineAfterRecord to set
	 */
	public void setAddNewLineAfterRecord(boolean addNewLineAfterRecord) {
		this.addNewLineAfterRecord = addNewLineAfterRecord;
	}
	
	/**
	 * Returns the valueSeparator.
	 * @return String
	 */
	public String getValueSeparator() {
		return valueSeparator;
	}
	
	/**
	 * Sets the file.
	 * @param file The file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}
	
	/**
	 * Sets the valueSeparator.
	 * @param valueSeparator The valueSeparator to set
	 */
	public void setValueSeparator(String valueSeparator) {
		this.valueSeparator = valueSeparator;
	}


	/**
	 * Method getValueAtIndexFromRecordString. Uses the valueSeparator and a stringtokenizer to read and return a value at a selected index.
	 * @param index The index starts at 1
	 * @param recordString
	 * @return The value at the selected index. Remember if the value was empty the returned value will be getEmptyValueString().
	 */
	public String getValueAtIndexFromRecordString(int index , String recordString){		
		int i = 1;
		recordString = TextSoap.findAndReplace(recordString,valueSeparator+valueSeparator,valueSeparator+emptyValueString+valueSeparator);
		recordString = TextSoap.findAndReplace(recordString,valueSeparator+valueSeparator,valueSeparator+emptyValueString+valueSeparator);
		StringTokenizer tokens = new StringTokenizer(recordString,valueSeparator);
		String value = null;
		while( tokens.hasMoreTokens() && i<=index ){
			value = tokens.nextToken();	
			//System.out.println("GenericImportFile : index = "+index+" value = "+value);
			if (tokens.hasMoreTokens()) {
				i++;
			}
		}
		if (i < index) {
			return getEmptyValueString();
		}
		
		return value;
	} 
	
	
	/**
	 * Method getValuesFromRecordString. Uses the valueSeparator and a stringtokenizer to read the record and create an ArrayList of values.
	 * @param recordString
	 * @return An ArrayList of values or null is no value was found
	 */
	public ArrayList getValuesFromRecordString(String recordString){	
		ArrayList values = null;	
		recordString = TextSoap.findAndReplace(recordString,valueSeparator+valueSeparator,valueSeparator+emptyValueString+valueSeparator);
		recordString = TextSoap.findAndReplace(recordString,valueSeparator+valueSeparator,valueSeparator+emptyValueString+valueSeparator);
		StringTokenizer tokens = new StringTokenizer(recordString,valueSeparator);
		String value = null;
		while( tokens.hasMoreTokens() ){
			if(values==null) values = new ArrayList();
			value = tokens.nextToken();
			values.add(value);	
			//System.out.println("GenericImportFile : value = "+value);
		}
		
		
		return values;
	} 


	/**
	 * Returns the ignoreIfFoundValue.
	 * @return String
	 */
	public String getEmptyValueString() {
		return emptyValueString;
	}

	public void setEmptyValueString(String emptyValueString) {
		this.emptyValueString = emptyValueString;
	}

	/**
	 * Closes the FileReader and BufferedReader
	 * @see com.idega.block.importer.data.ImportFile#close()
	 */
	public void close() {
		if(null!=br){
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(null!=fr){
			try {
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}