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


  public static void main(String[] args)throws Exception{
    try{
      String className = args[0];
      EJBWizard instance = new EJBWizard();
      instance.doJavaFileCreate(className);
    }
    catch(java.lang.ArrayIndexOutOfBoundsException e){
      System.out.println("EJBWizard: You have to supply a valid ClassName as an argument");
    }
  }



  public void doJavaFileCreate(String className)throws Exception{
      EJBWizardClassCreator inst = new EJBWizardClassCreator(className);
      setClassCreatorProperties(inst);
      inst.createAllFiles();
  }

  public void setClassCreatorProperties(EJBWizardClassCreator inst){
  }

}
