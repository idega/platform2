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

  private String entityBeanClassSuffix = "BMPBean";

  private String factorySuperClass="com.idega.data.IDOFactory";
  private String remoteInterfaceSuperInterface = "com.idega.data.IDOEntity";

  private boolean throwRemoteExceptions=true;

  public EJBWizardClassCreator(String originalClassName)throws Exception{
      this(Class.forName(originalClassName));
  }

  public EJBWizardClassCreator(Class originalClass)throws Exception{
    this.originalClass=originalClass;
    this.introspector = new ClassIntrospector(originalClass);
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

  public String getRemoteInterfaceSuperInterface(){
    return remoteInterfaceSuperInterface;
  }

  public void setRemoteInterfaceSuperInterface(String remoteInterfaceSuperInterface){
    this.remoteInterfaceSuperInterface=remoteInterfaceSuperInterface;
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
    if (!this.introspector.getPackage().equals(""))
      codeString+="package "+this.introspector.getPackage()+";\n\n";

      // -- ejb import statement --
      codeString+="import javax.ejb.*;\n";

      // -- interface declaration --
      codeString+="\npublic interface ";
      codeString+=getRemoteName()+ " extends "+getRemoteInterfaceSuperInterface();
      Class[] superInterfaces = this.introspector.getImplementedInterfaces();
      if(superInterfaces!=null){
        for (int i = 0; i < superInterfaces.length; i++) {
          codeString+=superInterfaces[i].getName();
        }

      }
      codeString+="\n";
      codeString+="{\n";

      // -- public methods --
      String[] methods=this.introspector.getMethods();
      for (int i=0; i<methods.length; i++)
      {
        if(throwRemoteExceptions()){
          if (methods[i].indexOf("throws")>=0)
          {
            codeString+=" "+methods[i]+", java.rmi.RemoteException;\n";
          }
          else
          {
            codeString+=" "+methods[i]+" throws java.rmi.RemoteException;\n";
          }
        }
        else{
          codeString+=" "+methods[i] +";\n";
        }
      }

      codeString=codeString+"}\n";

      return codeString;

  }

  public String getHome(){
    String codeString="\npublic interface ";
    codeString=codeString+getHomeName()+ " extends com.idega.data.IDOHome\n{\n";

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
        codeString+="\n;";
        codeString+=" public "+this.introspector.getShortName()+" findByPrimaryKey(int id) throws javax.ejb.FinderException";
        if(throwRemoteExceptionsInHome()){
          codeString+=", java.rmi.RemoteException";
        }
        codeString+="\n;";
        codeString+=" public "+this.introspector.getShortName()+" findByPrimaryKey(Integer id) throws javax.ejb.FinderException";
        if(throwRemoteExceptionsInHome()){
          codeString+=", java.rmi.RemoteException";
        }
        codeString+="\n;";

    codeString +="\n}";
    return codeString;
  }


  //public String getBean(){
  //}

  public String getFactory(){
    String codeString="\npublic class ";
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

        codeString+=" Class getEntityInterfaceClass()";
        codeString+="{\n";
        codeString+="  return "+this.introspector.getShortName()+".class;";
        codeString+="\n }\n";

        codeString+=" public "+this.introspector.getShortName()+" create() throws javax.ejb.CreateException";
        codeString+="{\n";
        codeString+="  return ("+this.introspector.getShortName()+") super.create(getEntityInterfaceClass());";
        codeString+="\n }\n";

        codeString+=" public "+this.introspector.getShortName()+" findByPrimaryKey(int id) throws javax.ejb.FinderException";
        codeString+="{\n";
        codeString+="  return ("+this.introspector.getShortName()+") super.findByPrimaryKey(getEntityInterfaceClass(),id);";
        codeString+="\n }\n";

        codeString+=" public "+this.introspector.getShortName()+" findByPrimaryKey(Integer id) throws javax.ejb.FinderException";
        codeString+="{\n";
        codeString+="  return ("+this.introspector.getShortName()+") super.findByPrimaryKey(getEntityInterfaceClass(),id);";
        codeString+="\n }\n";

    codeString +="\n}";
    return codeString;
  }


  public void createAllFiles()throws Exception{
    createRemote();
    createHome();
    createFactory();
  }

  public File getFile(String name)throws Exception{
    File f = new File(name+".java");
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
    return this.introspector.getShortName()+"Factory";
  }

  public boolean moveEntityBean(){
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

  public String getBaseName(){
    return introspector.getShortName();
  }

  public String getEntityBeanName(){
    return introspector.getShortName()+this.entityBeanClassSuffix;
  }
}
