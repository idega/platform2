package is.idega.tools;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.io.File;


public class SourceFileInfo {

  private File sourceFile;
  private String className;
  private String packageName;

  public SourceFileInfo(File file) {
    setSourceFile(file);
  }

  public File getSourceFile(){
    return sourceFile;
  }

  public void setSourceFile(File file){
    sourceFile=file;
  }

  public String getClassName(){
    return this.className;
  }

  public void setClassName(String name){
    className=name;
  }

  public String getPackage(){
    return this.packageName;
  }

  public void setPackage(String packageName){
    this.packageName=packageName;
  }

  public String getFullClassNameWithPackage(){
    return getPackage()+"."+this.getClassName();
  }

}