package is.idega.tools;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class EJBLegacyWizard extends EJBWizard{

  public static void main(String[] args)throws Exception{
    try{
      String className = args[0];
      EJBLegacyWizard instance = new EJBLegacyWizard();
      instance.setLegacyIDO(true);
      instance.doJavaFileCreate(className);
    }
    catch(java.lang.ArrayIndexOutOfBoundsException e){
      System.out.println("EJBLegacyWizard: You have to supply a valid ClassName as an argument");
    }
  }

  public void setClassCreatorProperties(EJBWizardClassCreator inst){
      inst.setToThrowRemoteExceptions(false);
      inst.setRemoteInterfaceSuperInterface("com.idega.data.IDOLegacyEntity");
  }
}
