package is.idega.tools;

import java.io.*;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class EJBWizard {

  protected boolean legacyIDO=false;
  public static String entityBeanClassSuffix = "BMPBean";
  private String className;
  private File workingDirectory;
  private String superInterface;

  public EJBWizard(String className){
    setEntityClassName(className);
  }

  public EJBWizard(Class entityClass){
    setEntityClass(entityClass);
  }

  public static void main(String[] args)throws Exception{
    try{
      String className = args[0];
      EJBWizard instance = new EJBWizard(className);
      instance.setWorkingDirectory(new File("."));
      instance.doJavaFileCreate();
    }
    catch(java.lang.ArrayIndexOutOfBoundsException e){
      System.out.println("EJBWizard: You have to supply a valid ClassName as an argument");
    }
  }

  public void setLegacyIDO(boolean ifLegacy){
    this.legacyIDO=ifLegacy;
  }


  public void doJavaFileCreate()throws Exception{
      EJBWizardClassCreator inst = new EJBWizardClassCreator(className);
      if(this.superInterface!=null){
        inst.setRemoteInterfaceSuperInterface(superInterface);
      }
      inst.setWorkingDirectory(getWorkingDirectory());
      inst.setLegacyIDO(this.legacyIDO);
      setClassCreatorProperties(inst);
      inst.createAllFiles();
  }

  public void setEntityClassName(String className){
    this.className=className;
  }

  public void setEntityClass(Class entityClass){
    this.setEntityClassName(entityClass.getName());
  }

  public void setWorkingDirectory(File directory){
    this.workingDirectory=directory;
  }

  public File getWorkingDirectory(){
    return this.workingDirectory;
  }

  public void setRemoteInterfaceSuperInterface(String interfaceClass){
    //System.out.println("EJBWizard - Setting RemoteSuperInterface: "+interfaceClass+" for "+this.className);
    this.superInterface=interfaceClass;
  }

  /**
   * Overrided in sublcasses
   * @param inst
   */
  protected void setClassCreatorProperties(EJBWizardClassCreator inst){
  }

}
