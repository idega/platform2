package is.idega.tools;

import java.io.File;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class IBOSessionWizard extends IBOServiceWizard{


  public IBOSessionWizard(String className){
    super(className);
    setRemoteInterfaceSuperInterface("com.idega.business.IBOSession");
  }

  public IBOSessionWizard(Class entityClass){
    super(entityClass);
    setLegacyIDO(true);
  }


  public static void main(String[] args)throws Exception{
    try{
      String className = args[0];
      IBOSessionWizard instance = new IBOSessionWizard(className);
      instance.doJavaFileCreate();
    }
    catch(java.lang.ArrayIndexOutOfBoundsException e){
      System.out.println("IBOWizard: You have to supply a valid ClassName as an argument");
    }
  }


}
