package is.idega.tools;

import java.io.File;
import java.lang.reflect.Method;


/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class IBOServiceWizard extends EJBWizard{

  //protected String beanClassSuffix="Bean";

  public IBOServiceWizard(String className){
    super(className);
    setRemoteInterfaceSuperInterface("com.idega.business.IBOService");
  }

  public IBOServiceWizard(Class entityClass){
    super(entityClass);
    setLegacyIDO(true);
  }

  public String getBeanSuffix(){
    return "Bean";
  }


  public static void main(String[] args)throws Exception{
    try{
      String className = args[0];
      IBOServiceWizard instance = new IBOServiceWizard(className);
      instance.doJavaFileCreate();
    }
    catch(java.lang.ArrayIndexOutOfBoundsException e){
      e.printStackTrace();
      System.out.println("IBOWizard: You have to supply a valid ClassName as an argument");
    }
  }

  /**
   * Overrided in sublcasses
   * @param inst
   */
  protected void setClassCreatorProperties(EJBWizardClassCreator inst){
    inst.setFactorySuperClass("com.idega.business.IBOHomeImpl");
    inst.setHomeSuperInterface("com.idega.business.IBOHome");
  }

  public boolean finderMethodsAllowed(){
    return false;
  }



  public String[] getInternalMethodImplementations(ClassIntrospector introspector){
    int length = 1;
    String[] returningMethods = new String[length];
    for (int i = 0; i < length; i++) {
        String methodString =" protected Class getBeanInterfaceClass()";
        methodString+="{\n";
        methodString+="  return "+introspector.getShortName()+".class;";
        methodString+="\n }\n\n";

      returningMethods[i]=methodString;
    }
    return returningMethods;
  }




  public String[] getCreateMethodImplementations(ClassIntrospector introspector){
    String[] finderMethodStrings = introspector.getCreateMethods();
    Method[] methods = introspector.getCreateMethodsArray();
    int length = methods.length;
    length+=1;
    String[] returningMethods = new String[length];
    int i=0;
    for (i = 0; i < methods.length; i++) {
      Method method = methods[i];
      String methodString = finderMethodStrings[i]+"{\n";
      //methodString += "\tcom.idega.business.IBOService service = this.idoCheckOutPooledEntity();\n";
      methodString += "\tcom.idega.business.IBOService service = this.iboCheckOutPooledBean();\n";
      methodString += "\t(("+introspector.getEntityBeanName()+")service)."+method.getName()+"("+introspector.getParametersInForMethod(method)+");\n";
      //methodString += "\t(("+introspector.getEntityBeanName()+")entity)."+introspector.getPostCreateMethodName(method.getName())+"("+introspector.getParametersInForMethod(method)+");\n";
      //methodString += "\tthis.idoCheckInPooledEntity(entity);\n";
      methodString += "\treturn(("+introspector.getEntityBeanName()+")service);\n";
      methodString += "\tcatch(Exception e){\n\t\tthrow new com.idega.data.IDOCreateException(e);\n\t}\n";
      methodString += "}\n";
      returningMethods[i]=methodString;
    }

    String codeString =" public "+introspector.getShortName()+" create() throws javax.ejb.CreateException";
    codeString+="{\n";
    codeString+="  return ("+introspector.getShortName()+") super.createIBO();";
    codeString+="\n }\n\n";
    returningMethods[i]=codeString;
    i++;

    return returningMethods;
  }



}
