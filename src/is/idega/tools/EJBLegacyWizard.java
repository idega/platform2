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

public class EJBLegacyWizard extends EJBWizard{


  public EJBLegacyWizard(String className){
    super(className);
    setLegacyIDO(true);
    setRemoteInterfaceSuperInterface("com.idega.data.IDOLegacyEntity");
  }

  public EJBLegacyWizard(Class entityClass){
    super(entityClass);
    setLegacyIDO(true);
  }


  public static void main(String[] args)throws Exception{
    try{
      String className = args[0];
      EJBLegacyWizard instance = new EJBLegacyWizard(className);
      instance.setWorkingDirectory(new File("."));
      instance.doJavaFileCreate();
    }
    catch(java.lang.ArrayIndexOutOfBoundsException e){
      System.out.println("EJBLegacyWizard: You have to supply a valid ClassName as an argument");
    }
  }

  protected void setClassCreatorProperties(EJBWizardClassCreator inst){
      inst.setToThrowRemoteExceptions(false);
      //inst.setRemoteInterfaceSuperInterface("com.idega.data.IDOLegacyEntity");
  }
}
