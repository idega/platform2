package is.idega.tools;

import java.io.*;

import java.lang.reflect.Method;

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
  //protected String beanClassSuffix = "BMPBean";
  private String className;
  private File workingDirectory;
  private String superInterface;

  public EJBWizard(String className){
    setEntityClassName(className);
    initialize();
  }

  public EJBWizard(Class entityClass){
    setEntityClass(entityClass);
    initialize();
  }

  public void initialize(){
    String currentDir = System.getProperty("user.dir");
    File workingDir = new File(currentDir);
    this.setWorkingDirectory(workingDir);
  }

  public static void main(String[] args)throws Exception{
    try{
      String className = args[0];
      EJBWizard instance = new EJBWizard(className);
      String currentDir = System.getProperty("user.dir");
      File workingDir = new File(currentDir);
      instance.setWorkingDirectory(workingDir);
      instance.doJavaFileCreate();
    }
    catch(java.lang.ArrayIndexOutOfBoundsException e){
      System.out.println("EJBWizard: You have to supply a valid ClassName as an argument");
    }
  }

  public void setLegacyIDO(boolean ifLegacy){
    this.legacyIDO=ifLegacy;
  }

  public String getBeanSuffix(){
    return "BMPBean";
  }

  public void doJavaFileCreate()throws Exception{
      EJBWizardClassCreator inst = new EJBWizardClassCreator(className,this);
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
    System.out.println("Setting working dir="+directory.getAbsolutePath());
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
    inst.setFactorySuperClass("com.idega.data.IDOFactory");
    inst.setHomeSuperInterface("com.idega.data.IDOHome");
  }


  public boolean finderMethodsAllowed(){
    return true;
  }

  public String[] getInternalMethodImplementations(ClassIntrospector introspector){
    int length = 1;
    String[] returningMethods = new String[length];
    for (int i = 0; i < length; i++) {
        String methodString =" protected Class getEntityInterfaceClass()";
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
    if(legacyIDO){
      length+=2;
    }
    else{
      length+=1;
    }
    String[] returningMethods = new String[length];
    int i=0;
    for (i = 0; i < methods.length; i++) {
      Method method = methods[i];
      String methodString = finderMethodStrings[i]+"{\n";
      methodString += "\tcom.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();\n";
      methodString += "\tObject pk = (("+introspector.getEntityBeanName()+")entity)."+method.getName()+"("+introspector.getParametersInForMethod(method)+");\n";
      methodString += "\t(("+introspector.getEntityBeanName()+")entity)."+introspector.getPostCreateMethodName(method.getName())+"("+introspector.getParametersInForMethod(method)+");\n";
      methodString += "\tthis.idoCheckInPooledEntity(entity);\n";
      methodString += "\ttry{\n\t\treturn this.findByPrimaryKey(pk);\n\t}\n";
      methodString += "\tcatch(javax.ejb.FinderException fe){\n\t\tthrow new com.idega.data.IDOCreateException(fe);\n\t}\n";
      methodString += "\tcatch(Exception e){\n\t\tthrow new com.idega.data.IDOCreateException(e);\n\t}\n";
      methodString += "}\n";
      returningMethods[i]=methodString;
    }

    String codeString =" public "+introspector.getShortName()+" create() throws javax.ejb.CreateException";
    codeString+="{\n";
    codeString+="  return ("+introspector.getShortName()+") super.createIDO();";
    codeString+="\n }\n\n";
    returningMethods[i]=codeString;
    i++;

    if(legacyIDO){
      codeString =" public "+introspector.getShortName()+" createLegacy()";
      codeString+="{\n";
      codeString+="\ttry{\n";
      codeString+="\t\treturn create();\n";
      codeString+="\t}\n";
      codeString+="\tcatch(javax.ejb.CreateException ce){\n";
      codeString+="\t\tthrow new RuntimeException(\"CreateException:\"+ce.getMessage());\n";
      codeString+="\t}\n";
      codeString+="\n }\n\n";
      returningMethods[i]=codeString;
      i++;
    }
    return returningMethods;
  }


  public String[] getFinderMethodImplementations(ClassIntrospector introspector){
    String[] finderMethodStrings = introspector.getFinderMethods();
    Method[] methods = introspector.getFinderMethodsArray();
    int length = methods.length;
    if(legacyIDO){
      length+=3;
    }
    else{
      length+=2;
    }
    String[] returningMethods = new String[length];
    int i = 0;
    for (i = 0; i < methods.length; i++) {
      Method method = methods[i];
      String methodString = null;
      if(method.getReturnType().equals(java.util.Collection.class)){
        methodString = finderMethodStrings[i]+"{\n";
        methodString += "\tcom.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();\n";
        methodString += "\tjava.util.Collection ids = (("+introspector.getEntityBeanName()+")entity)."+method.getName()+"("+introspector.getParametersInForMethod(method)+");\n";
        methodString += "\tthis.idoCheckInPooledEntity(entity);\n";
        methodString += "\treturn this.getEntityCollectionForPrimaryKeys(ids);\n";
        methodString += "}\n";
      }
      else if(method.getReturnType().equals(java.util.Set.class)){
        methodString = finderMethodStrings[i]+"{\n";
        methodString += "\tcom.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();\n";
        methodString += "\tjava.util.Set ids = (("+introspector.getEntityBeanName()+")entity)."+method.getName()+"("+introspector.getParametersInForMethod(method)+");\n";
        methodString += "\tthis.idoCheckInPooledEntity(entity);\n";
        methodString += "\treturn this.getEntitySetForPrimaryKeys(ids);\n";
        methodString += "}\n";
      }
      else{
        methodString = finderMethodStrings[i]+"{\n";
        methodString += "\tcom.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();\n";
        methodString += "\tObject pk = (("+introspector.getEntityBeanName()+")entity)."+method.getName()+"("+introspector.getParametersInForMethod(method)+");\n";
        methodString += "\tthis.idoCheckInPooledEntity(entity);\n";
        methodString += "\treturn this.findByPrimaryKey(pk);\n";
        methodString += "}\n";
      }
      returningMethods[i]=methodString;
    }


    String codeString =" public "+introspector.getShortName()+" findByPrimaryKey(int id) throws javax.ejb.FinderException";
    codeString+="{\n";
    codeString+="  return ("+introspector.getShortName()+") super.findByPrimaryKeyIDO(id);";
    codeString+="\n }\n\n";
    returningMethods[i]=codeString;
    i++;

    codeString =" public "+introspector.getShortName()+" findByPrimaryKey(Object pk) throws javax.ejb.FinderException";
    codeString+="{\n";
    codeString+="  return ("+introspector.getShortName()+") super.findByPrimaryKeyIDO(pk);";
    codeString+="\n }\n\n";
    returningMethods[i]=codeString;
    i++;

    if(legacyIDO){
      codeString =" public "+introspector.getShortName()+" findByPrimaryKeyLegacy(int id) throws java.sql.SQLException";
      codeString+="{\n";
      codeString+="\ttry{\n";
      codeString+="\t\treturn findByPrimaryKey(id);\n";
      codeString+="\t}\n";
      codeString+="\tcatch(javax.ejb.FinderException fe){\n";
      codeString+="\t\tthrow new java.sql.SQLException(fe.getMessage());\n";
      codeString+="\t}\n";
      codeString+="\n }\n\n";
      returningMethods[i]=codeString;
      i++;
    }


    return returningMethods;
  }


  public String[] getHomeMethodImplementations(ClassIntrospector introspector){
    String[] finderMethodStrings = introspector.getHomeMethods();
    Method[] methods = introspector.getHomeMethodsArray();
    int length = methods.length;
    String[] returningMethods = new String[length];
    for (int i = 0; i < methods.length; i++) {
      Method method = methods[i];
      String returnType = introspector.getClassParameterToString(method.getReturnType());
      String methodString = finderMethodStrings[i]+"{\n";
      methodString += "\tcom.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();\n";
      methodString += "\t"+returnType+" theReturn = (("+introspector.getEntityBeanName()+")entity)."+method.getName()+"("+introspector.getParametersInForMethod(method)+");\n";
      methodString += "\tthis.idoCheckInPooledEntity(entity);\n";
      methodString += "\treturn theReturn;\n";
      methodString += "}\n";
      returningMethods[i]=methodString;
    }
    return returningMethods;
  }


}
