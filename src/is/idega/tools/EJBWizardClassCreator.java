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

public class EJBWizardClassCreator {

  private Class originalClass;
  private ClassIntrospector introspector;
  private String baseName;
  private File workingDir;
  private String factorySuffix = "HomeImpl";

  private String entityBeanClassSuffix = "";
  private EJBWizard ejbWizard;
  protected boolean legacyIDO = true;

  private String factorySuperClass="com.idega.data.IDOFactory";
  private String homeSuperInterface="com.idega.data.IDOHome";
  private String _remoteInterfaceSuperInterface = "com.idega.data.IDOEntity";

  private boolean throwRemoteExceptions=true;
  private boolean entityBeanClassAlreadyCreated=false;

  public EJBWizardClassCreator(String originalClassName,EJBWizard wizard)throws Exception{
    this.ejbWizard=wizard;
    this.setBeanSuffix(wizard.getBeanSuffix());
    initialize(originalClassName);
  }

  public EJBWizardClassCreator(Class originalClass,EJBWizard wizard)throws Exception{
    this.ejbWizard=wizard;
    this.setBeanSuffix(wizard.getBeanSuffix());
    initialize(originalClass);
  }

  public void initialize(String originalClassName)throws Exception{
    try{
      initialize(Class.forName(originalClassName));
    }
    catch(java.lang.NoClassDefFoundError e){
    	e.printStackTrace();
//      System.out.println("ClassNotFound:\""+originalClassName+"\"");
      initialize(Class.forName(originalClassName+entityBeanClassSuffix));
    }
  }

  public void initialize(Class originalClass)throws Exception{
    this.originalClass=originalClass;
    Class beanClass = originalClass;
    if(originalClass.isInterface()){
      entityBeanClassAlreadyCreated=true;
      beanClass = Class.forName(originalClass.getName()+entityBeanClassSuffix);
    }
    this.introspector = new ClassIntrospector(beanClass,this.ejbWizard);
  }

  public void setBeanSuffix(String beanSuffix){
    this.entityBeanClassSuffix=beanSuffix;
  }

  protected boolean throwRemoteExceptions(){
    return throwRemoteExceptions;
  }

  protected boolean throwRemoteExceptionsInHome(){
    return throwRemoteExceptions();
  }

  public void setToThrowRemoteExceptions(boolean ifToThrow){
    this.throwRemoteExceptions=ifToThrow;
  }

  public void setLegacyIDO(boolean ifLegacy){
    this.legacyIDO=ifLegacy;
  }

  public String getRemoteInterfaceSuperInterface(){
    return _remoteInterfaceSuperInterface;
  }

  public void setRemoteInterfaceSuperInterface(String remoteInterfaceSuperInterface){
    //System.out.println("EJBWizardClassCreator - Setting RemoteSuperInterface: "+remoteInterfaceSuperInterface+" for "+this.baseName);
    this._remoteInterfaceSuperInterface=remoteInterfaceSuperInterface;
  }

  public String getFactorySuperClass(){
    return factorySuperClass;
  }

  public void setFactorySuperClass(String factorySuperClass){
    this.factorySuperClass=factorySuperClass;
  }

  public String getRemote(){
    String codeString="";

    // -- generate package name if there is one --
    if (!this.introspector.getPackage().equals("")){
      codeString+="package "+this.introspector.getPackage()+";\n\n";
    }
      // -- ejb import statement --
      codeString+="import javax.ejb.*;\n";

      // -- interface declaration --
      codeString+="\npublic interface ";
      codeString+=getRemoteName()+ " extends "+getRemoteInterfaceSuperInterface();
      Class[] superInterfaces = this.introspector.getImplementedInterfaces();
      if(superInterfaces!=null){
        String thisInterfaceWithFullPackageName = introspector.getPackage()+"."+getRemoteName();
        for (int i = 0; i < superInterfaces.length; i++) {
          String interfaceName = superInterfaces[i].getName();
          if(interfaceName.equals(thisInterfaceWithFullPackageName)){
            //nothing
          }
          else if(interfaceName.equals(this.getRemoteInterfaceSuperInterface())){
            //nothing
          }
          else if(interfaceName.equals(this.getRemoteName())){
            //nothing
          }
          else{
            codeString+=","+interfaceName;
          }
        }

      }
      codeString+="\n";
      codeString+="{\n";

      // -- public methods --
      String[] methods=this.introspector.getInterfaceMethods();
      for (int i=0; i<methods.length; i++)
      {
        String methodString = methods[i];
        codeString += " " + getMethodSignatureWithAddedThrowsClause(methodString);
      }

      codeString=codeString+"}\n";

      return codeString;

  }

  public String getHome(){
    String codeString="";
    if (!this.introspector.getPackage().equals("")){
      codeString+="package "+this.introspector.getPackage()+";\n\n";
    }
    codeString+="\npublic interface ";
    codeString=codeString+getHomeName()+ " extends "+getHomeSuperInterface()+"\n{\n";

    /*String[] constructors=this.introspector.getConstructors();
    for (int i=0; i<constructors.length; i++)
      if (constructors[i].indexOf("throws")>=0){
        codeString=codeString+" public "+this.introspector.getShortName()+" create"+constructors[i]+",javax.ejb.CreateException, java.rmi.RemoteException;\n";
      }
      else{
        codeString=codeString+" public "+this.introspector.getShortName()+" create"+constructors[i]+" throws javax.ejb.CreateException, java.rmi.RemoteException;\n";
      }*/

        codeString+=" public "+this.introspector.getShortName()+" create() throws javax.ejb.CreateException";
        if(throwRemoteExceptionsInHome()){
          codeString+=", java.rmi.RemoteException";
        }
        codeString+=";\n";

        if(legacyIDO){
          codeString+=" public "+this.introspector.getShortName()+" createLegacy()";
          if(throwRemoteExceptionsInHome()){
            codeString+=", java.rmi.RemoteException";
          }
          codeString+=";\n";
        }
        if(ejbWizard.finderMethodsAllowed()){

          codeString+=" public "+this.introspector.getShortName()+" findByPrimaryKey(Object pk) throws javax.ejb.FinderException";
          if(throwRemoteExceptionsInHome()){
            codeString+=", java.rmi.RemoteException";
          }
          codeString+=";\n";

          if(legacyIDO){

            codeString+=" public "+this.introspector.getShortName()+" findByPrimaryKey(int id) throws javax.ejb.FinderException";
            if(throwRemoteExceptionsInHome()){
              codeString+=", java.rmi.RemoteException";
            }
            codeString+=";\n";

            codeString+=" public "+this.introspector.getShortName()+" findByPrimaryKeyLegacy(int id) throws java.sql.SQLException";
            if(throwRemoteExceptionsInHome()){
              codeString+=", java.rmi.RemoteException";
            }
            codeString+=";\n";
          }
        }

      // -- create methods --
      String[] methods=this.introspector.getCreateMethods();
      for (int i=0; i<methods.length; i++)
      {
        String methodString = methods[i];
        codeString += " " + getMethodSignatureWithAddedThrowsClause(methodString);
      }


      // -- finder methods --
      methods=this.introspector.getFinderMethods();
      for (int i=0; i<methods.length; i++)
      {
        String methodString = methods[i];
        codeString += " " + getMethodSignatureWithAddedThrowsClause(methodString);
      }

      // -- home methods --
      methods=this.introspector.getHomeMethods();
      for (int i=0; i<methods.length; i++)
      {
        String methodString = methods[i];
        codeString += " " + getMethodSignatureWithAddedThrowsClause(methodString);
      }


    codeString +="\n}";
    return codeString;
  }


  //public String getBean(){
  //}

  public String getFactory(){
    String codeString="";
    if (!this.introspector.getPackage().equals("")){
      codeString+="package "+this.introspector.getPackage()+";\n\n";
    }

    codeString+="\npublic class ";
    codeString=codeString+getFactoryName()+ " extends "+getFactorySuperClass()+" implements "+getHomeName()+"\n{\n";

    /*String[] constructors=this.introspector.getConstructors();
    for (int i=0; i<constructors.length; i++)
    {
      if (constructors[i].indexOf("throws")>=0){
        codeString=codeString+" public "+this.introspector.getShortName()+" create"+constructors[i]+",javax.ejb.CreateException, java.rmi.RemoteException;\n";
      }
      else{
        codeString=codeString+" public "+this.introspector.getShortName()+" create"+constructors[i]+" throws javax.ejb.CreateException, java.rmi.RemoteException;\n";
      }
    }*/

        /*codeString+=" protected Class getEntityInterfaceClass()";
        codeString+="{\n";
        codeString+="  return "+this.introspector.getShortName()+".class;";
        codeString+="\n }\n\n";

        codeString+=" public "+this.introspector.getShortName()+" create() throws javax.ejb.CreateException";
        codeString+="{\n";
        codeString+="  return ("+this.introspector.getShortName()+") super.idoCreate();";
        codeString+="\n }\n\n";

        if(legacyIDO){
          codeString+=" public "+this.introspector.getShortName()+" createLegacy()";
          codeString+="{\n";
          codeString+="\ttry{\n";
          codeString+="\t\treturn create();\n";
          codeString+="\t}\n";
          codeString+="\tcatch(javax.ejb.CreateException ce){\n";
          codeString+="\t\tthrow new RuntimeException(\"CreateException:\"+ce.getMessage());\n";
          codeString+="\t}\n";
          codeString+="\n }\n\n";
        }

        codeString+=" public "+this.introspector.getShortName()+" findByPrimaryKey(int id) throws javax.ejb.FinderException";
        codeString+="{\n";
        codeString+="  return ("+this.introspector.getShortName()+") super.idoFindByPrimaryKey(id);";
        codeString+="\n }\n\n";

        codeString+=" public "+this.introspector.getShortName()+" findByPrimaryKey(Object pk) throws javax.ejb.FinderException";
        codeString+="{\n";
        codeString+="  return ("+this.introspector.getShortName()+") super.idoFindByPrimaryKey(pk);";
        codeString+="\n }\n\n";

        if(legacyIDO){
          codeString+=" public "+this.introspector.getShortName()+" findByPrimaryKeyLegacy(int id) throws java.sql.SQLException";
          codeString+="{\n";
          codeString+="\ttry{\n";
          codeString+="\t\treturn findByPrimaryKey(id);\n";
          codeString+="\t}\n";
          codeString+="\tcatch(javax.ejb.FinderException fe){\n";
          codeString+="\t\tthrow new java.sql.SQLException(fe.getMessage());\n";
          codeString+="\t}\n";
          codeString+="\n }\n\n";
        }*/



        //internal method implementations
        String[] methods = ejbWizard.getInternalMethodImplementations(introspector);
        for (int i = 0; i < methods.length; i++) {
          codeString += methods[i];
          codeString += "\n";
        }

        //Createmethod implementations
        methods = ejbWizard.getCreateMethodImplementations(introspector);
        for (int i = 0; i < methods.length; i++) {
          codeString += methods[i];
          codeString += "\n";
        }

        if(ejbWizard.finderMethodsAllowed()){
          //FinderMethod implementations
          methods = ejbWizard.getFinderMethodImplementations(introspector);
          for (int i = 0; i < methods.length; i++) {
            codeString += methods[i];
            codeString += "\n";
          }
        }

        //HomeMethod implementations
        methods = ejbWizard.getHomeMethodImplementations(introspector);
        for (int i = 0; i < methods.length; i++) {
          codeString += methods[i];
          codeString += "\n";
        }


    codeString +="\n}";
    return codeString;
  }


  public void createAllFiles()throws Exception{
    createRemote();
    createHome();
    createFactory();
  }

  public File getFile(String name)throws Exception{
    File f = new File(getWorkingDirectory(),name+".java");
    try{
      f.createNewFile();
    }
    catch(Exception e){
      //e.printStackTrace(System.err);
    }
    return f;
  }

  public void createRemote()throws Exception{
    File f = getFile(getRemoteName());
    FileWriter w = new FileWriter(f);
    w.write(getRemote());
    w.flush();
    w.close();
  }

  public void createHome()throws Exception{
    File f = getFile(getHomeName());
    FileWriter w = new FileWriter(f);
    w.write(getHome());
    w.flush();
    w.close();
  }

  public void createFactory()throws Exception{
    File f = getFile(getFactoryName());
    FileWriter w = new FileWriter(f);
    w.write(getFactory());
    w.flush();
    w.close();
  }

  public String getRemoteName(){
    return this.introspector.getShortName();
  }

  public String getHomeName(){
    return this.introspector.getShortName()+"Home";
  }

  public String getFactoryName(){
    return this.introspector.getShortName()+factorySuffix;
  }

  public boolean moveEntityBean(){
    if(!entityBeanClassAlreadyCreated){
      String javaFilename = this.getBaseName()+".java";
      File f = new File(javaFilename);
      if(f.exists()){
        String testString = "public class "+this.getBaseName();
        try{
          boolean isClassValid=true;
          if(isClassValid){
            String newFileName = this.getEntityBeanName()+".java";
            com.idega.util.FileUtil.copyFile(f,newFileName);
          }
        }
        catch(Exception e){
          return false;
        }
      }
      return true;
    }
    return false;
  }

  public String getBaseName(){
    return introspector.getShortName();
  }

  public String getEntityBeanName(){
    return introspector.getShortName()+this.entityBeanClassSuffix;
  }

  public void setWorkingDirectory(File dir){
    this.workingDir=dir;
  }

  public File getWorkingDirectory(){
    return workingDir;
  }

  private String getMethodSignatureWithAddedThrowsClause(String methodString){
    if(throwRemoteExceptions()){
      if (methodString.indexOf("throws")>=0)
      {
        methodString += ", java.rmi.RemoteException;\n";
      }
      else
      {
        methodString +=" throws java.rmi.RemoteException;\n";
      }
    }
    else{
      methodString += ";\n";
    }
    return methodString;
  }

  public String getHomeSuperInterface(){
    return homeSuperInterface;
  }

  public void setHomeSuperInterface(String className){
    this.homeSuperInterface=className;
  }
}
