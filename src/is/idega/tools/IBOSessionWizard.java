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
		try {
			String firstArg=args[0];
			String className = getClassName(args);
			IBOSessionWizard instance = new IBOSessionWizard(className);
			if (firstArg.endsWith(".java") || firstArg.endsWith(".JAVA")) {
				File javaFile = new File(firstArg);
				instance.setWorkingDirectory(javaFile.getParentFile());
			}
			instance.doJavaFileCreate();
		}
		catch (java.lang.ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			System.out.println("IBOWizard: You have to supply a valid ClassName as an argument");
		}
  }
  
  
	protected String getSessionBeanType(EJBWizardClassCreator classCreator){
		return "Stateful";	
	}


}
