package com.idega.block.importer.data;

import java.util.Collection;
import java.util.ArrayList;
import java.io.File;

public interface ImportFile{

  public Collection getRecords();
  public Object getNextRecord(); 
  public ArrayList getValuesFromRecordString(String recordString);
  public String getValueAtIndexFromRecordString(int index, String recordString);
  public void setFile(File file);
  public String getEmptyValueString();
  public void close();
/**
 * Method setEmptyValueString. This will be the value returned if a column you want is empty in the import file.
 * @param emptyValueString
 */
  public void setEmptyValueString(String emptyValueString);
  public File getFile();
  
  //public Object getRecordAtIndex(int index);
 // public boolean parse();
}
