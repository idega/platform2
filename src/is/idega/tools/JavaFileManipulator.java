package is.idega.tools;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

import java.util.List;
import java.util.Iterator;

/**
 * Title:        idega Data Objects
 * Description:  Idega Data Objects is a Framework for Object/Relational mapping and seamless integration between datastores
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author
 * @version 1.0
 */

public class JavaFileManipulator {

  private File javaFile;
  private String className;

  private List linesList;

  public JavaFileManipulator(File javaFile)throws Exception{
    setFile(javaFile);
    populateLinesList();
  }

  public void setFile(File javaFile){
    this.javaFile=javaFile;
  }

  public void setClassName(String className){
    this.className=className;
  }

  public void commit()throws Exception{

    changeLines();
    outputToFile();

    /*FileReader reader = new FileReader(javaFile);
    LineNumberReader lineReader = new LineNumberReader(reader);

    lineReader.mark(1);
    while (lineReader.read() != -1) {
        lineReader.reset();
        String s = lineReader.readLine();
        if (s.indexOf(searchString)!=-1){
          return true;
        }
        lineReader.mark(1);
    }*/
  }

  private void populateLinesList()throws java.io.IOException{
    this.linesList=com.idega.util.FileUtil.getLinesFromFile(javaFile);
  }

  private void outputToFile()throws java.io.FileNotFoundException,java.io.IOException{
    RandomAccessFile ras = new RandomAccessFile(javaFile,"rw");
    Iterator iter = this.linesList.iterator();
    while (iter.hasNext()) {
      String item = (String)iter.next();
      ras.writeChars(item);
    }
    ras.close();
  }


  private void changeLines(){
    changeClassName();
  }

  private void changeClassName(){
    String header = this.getClassHeaderLine();
    String newClassName = "public class "+this.className;
    boolean doChange = true;
    int index1 = header.indexOf(newClassName);
    if(index1==-1){

    }

  }



  public String getClassHeaderLine(){
    return getLine(this.getClassHeaderLineNumber());
  }

  public void setClassHeaderLine(String line){
    this.setLine(this.getClassHeaderLineNumber(),line);
  }


  private int getClassHeaderLineNumber(){
    Iterator iter = linesList.iterator();
    int linenr=1;
    while (iter.hasNext()) {
      String line = (String)iter.next();
      if(line.indexOf('{')!=-1){
        return linenr;
      }
      linenr++;
    }
    return linenr;
  }

  public String getLine(int linenr){
    return (String)linesList.get(linenr-1);
  }

  protected void setLine(int linenr,String line){
    linesList.set(linenr-1,line);
  }
}