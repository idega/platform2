package is.idega.tools;

import java.lang.reflect.*;
import java.beans.*;
import java.util.*;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class ClassIntrospector {

  Class sourceClass;
  String shortName;
  BeanInfo info;
  private static String INITIALIZE_ATTRIBUTES="initializeAttributes";
  private static String GET_ENTITY_NAME="getEntityName";
  private static String GET_ID_COLUMN_NAME="getIDColumnName";
  //private static String DELETE="delete";
  private static String UPDATE="update";
  private static String INSERT="insert";
  private static String DELETE="delete";
  private static String REMOVE="remove";
  private static String SET_DEFAULT_VALUES="setDefaultValues";
  private static String INSERT_START_DATA = "insertStartData";
  private static String EJB_START = "ejb";
  private static String EJB_FIND_START = "ejbFind";
  private static String EJB_HOME_START = "ejbHome";
  private static String EJB_CREATE_START = "ejbCreate";
  private static String EJB_POST_CREATE_START = "ejbPostCreate";

  private static String GET_NAME_OF_MIDDLE_TABLE = "getNameOfMiddleTable";
  private boolean convertGenericEntityToIDOLegacyEntity=true;
  private String beanSuffix="";
  private EJBWizard wizard;

  public ClassIntrospector(Class sourceClass,EJBWizard wizard)throws Exception{
    this.wizard=wizard;
    this.setBeanSuffix(wizard.getBeanSuffix());
    this.sourceClass = sourceClass;
    this.info = Introspector.getBeanInfo(sourceClass,sourceClass.getSuperclass());
    String name = sourceClass.getName().substring(sourceClass.getName().lastIndexOf(".")+1);
    /*if(name.endsWith("Entity")){
      shortName = name.substring(name.indexOf("Entity"));
    }
    else if(name.endsWith("Bean")){
      shortName = name.substring(name.indexOf("Bean"));
    }
    else{*/
    if(name.endsWith(beanSuffix)){
      shortName = name.substring(0,name.indexOf(beanSuffix));
    }
    else{
      shortName = name;
    }
    //}
  }

  private Method[] getEntityInterfaceVisibleMethods(){
    MethodDescriptor[] descr =info.getMethodDescriptors();
    Method[] methods = null;
    Vector v = new Vector();
    int index = 0;
    for (int i = 0; i < descr.length; i++) {
      MethodDescriptor currentDesc = descr[i];
      Method m = currentDesc.getMethod();
      if((Modifier.isPublic(m.getModifiers())) && !(Modifier.isStatic(m.getModifiers()))){
        //if(m.getDeclaringClass().equals(this.sourceClass)){
        //  v.add(m);
        //}
        String methodName = m.getName();
        //System.out.println("methodName: "+methodName);
        if(methodName.equals(INITIALIZE_ATTRIBUTES)){
          //v.add(m);
        }
        else if(methodName.equals(SET_DEFAULT_VALUES)){
          //v.add(m);
        }
        else if(methodName.equals(INSERT_START_DATA)){
          //v.add(m);
        }
        else if(methodName.equals(GET_ENTITY_NAME)){
          //v.add(m);
        }
        else if(methodName.equals(UPDATE)){
          //v.add(m);
        }
        else if(methodName.equals(DELETE)){
          //v.add(m);
        }
        else if(methodName.equals(INSERT)){
          //v.add(m);
        }
        else if(methodName.equals(REMOVE)){
          //v.add(m);
        }
        else if(methodName.equals(GET_NAME_OF_MIDDLE_TABLE)){
          //v.add(m);
        }
        else if(methodName.equals(GET_ID_COLUMN_NAME)){
        	//v.add(m);
        }
        else if(methodName.startsWith(EJB_START)){
          //v.add(m);
        }
        else{
          v.add(m);
        }

      }
    }
    methods = (Method[])v.toArray(new Method[0]);
    return methods;
  }

  public void setBeanSuffix(String beanSuffix){
    this.beanSuffix=beanSuffix;
  }

  public String[] getInterfaceMethods(){
    return getMethodsStringsFromMethodArray(getEntityInterfaceVisibleMethods());
  }

  public String[] getFinderMethods(){
    return getMethodsStringsFromMethodArray(getFinderMethodsArray());
  }

  public String[] getHomeMethods(){
    return getMethodsStringsFromMethodArray(getHomeMethodsArray());
  }


  String getPostCreateMethodName(String ejbCreateMethodName){
      return EJB_POST_CREATE_START+ejbCreateMethodName.substring(EJB_CREATE_START.length());
  }

  public String[] getCreateMethods(){
    return getMethodsStringsFromMethodArray(getCreateMethodsArray());
  }




  public String[] getMethodsStringsFromMethodArray(Method[] methods){

    //Method[] methods=getVisibleMethods();
    String[] returnArray = new String[methods.length];
    for (int i = 0; i < methods.length; i++) {
      Method thisMethod = methods[i];
      Class returnTypeClass = thisMethod.getReturnType();


      /*if(returnTypeClass.isArray()){
        //returnType=returnTypeClass.getComponentType().getName()+"[]";
          Class arrayClass = returnTypeClass.getComponentType();
          if(arrayClass.isArray()){
            returnType=arrayClass.getComponentType().getName()+"[][]";
          }
          else{
            returnType=arrayClass.getName()+"[]";
          }
      }
      else{
        returnType=returnTypeClass.getName();
      }
      */
      String returnType = this.getClassParameterToString(returnTypeClass);

      String realMethodName = thisMethod.getName();
      String methodName = realMethodName;
      if(realMethodName.startsWith(EJB_FIND_START)){
        methodName = "find"+realMethodName.substring(EJB_FIND_START.length());
        returnType = this.getReturnTypeTranslated(returnTypeClass);
      }
      else if(realMethodName.startsWith(EJB_HOME_START)){
        String firstChar = realMethodName.substring(EJB_HOME_START.length(),EJB_HOME_START.length()+1);
        methodName = firstChar.toLowerCase()+realMethodName.substring(EJB_HOME_START.length()+1,realMethodName.length());
      }
      else if(realMethodName.startsWith(EJB_CREATE_START)){
        methodName = "create"+realMethodName.substring(EJB_CREATE_START.length());
        returnType = this.getReturnTypeTranslated(returnTypeClass);
      }
      Class[] parameters=thisMethod.getParameterTypes();
      Class[] exceptions=thisMethod.getExceptionTypes();
      String returnString = "public "+returnType+" "+methodName+"(";

      for (int j = 0; j < parameters.length; j++) {
        if(j!=0){
          returnString+=",";
        }
        Class parameterClass = parameters[j];
        String parameterType = getClassParameterToString(parameterClass);
        /*if(parameterClass.isArray()){
          Class arrayClass = parameterClass.getComponentType();
          if(arrayClass.isArray()){
            parameterType=arrayClass.getComponentType().getName()+"[][]";
          }
          else{
            //parameterType=parameterClass.getComponentType().getName()+"[]";
            parameterType=arrayClass.getName()+"[]";
          }
        }
        else{
          parameterType=parameterClass.getName();
        }*/
        returnString += parameterType+" p"+j;
      }
      returnString+=")";
      for (int j = 0; j < exceptions.length; j++) {
        if(j!=0){
          returnString+=",";
        }
        else{
          returnString += "throws ";
        }
        returnString += exceptions[j].getName();
      }
      returnArray[i]=returnString;
    }
    return returnArray;
  }

  //public String[] getMethodCalls(String callInstance, boolean returnFlag){

  //}

  public String[] getConstructors(){
      Constructor[] constructors = sourceClass.getConstructors();
      String[] returnArray = new String[constructors.length];
      for (int i = 0; i < constructors.length; i++) {
          String returnString = new String();
          returnArray[i]=returnString;
          Constructor thisConstructor = constructors[i];
          String constructorName=thisConstructor.getName();
          Class[] parameters=thisConstructor.getParameterTypes();
          Class[] exceptions=thisConstructor.getExceptionTypes();
          returnString = "public "+constructorName+"(";
          for (int j = 0; j < parameters.length; j++) {
            if(j!=0){
              returnString+=",";
            }
            Class parameterClass = parameters[j];

            String parameterType = this.getClassParameterToString(parameterClass);
/*            if(parameterClass.isArray()){
              parameterType=parameterClass.getComponentType().getName()+"[]";
            }
            else{
              parameterType=parameterClass.getName();
            }
*/
            returnString += parameterType+" p"+j;
          }
          returnString+=")";
          for (int j = 0; j < exceptions.length; j++) {
            if(j!=0){
              returnString+=",";
            }
            else{
              returnString += "throws ";
            }
            returnString += exceptions[j].getName();
          }

      }
      return returnArray;
  }

  //public String[] getConstructorCalls(){

  //}

  public String getPackage(){
    return sourceClass.getPackage().getName();
  }

  public String getShortName(){
    return shortName;
  }

  public Class[] getImplementedInterfaces(){
    return sourceClass.getInterfaces();
  }

  String getClassParameterToString(Class parameter){
    String returnType = "";
    if(parameter.isArray()){
      //returnType=returnTypeClass.getComponentType().getName()+"[]";
        Class arrayClass = parameter.getComponentType();
        if(arrayClass.isArray()){
          returnType=getClassTranslated(arrayClass.getComponentType())+"[][]";
        }
        else{
          returnType=getClassTranslated(arrayClass)+"[]";
        }
    }
    else{
      returnType=getClassTranslated(parameter);
    }
    return returnType;
  }

  private String getClassTranslated(Class parameter){
    String className = parameter.getName();
    if(convertGenericEntityToIDOLegacyEntity){
      if(className.equals("com.idega.data.GenericEntity")){
        return "com.idega.data.IDOLegacyEntity";
      }
      else{
        return className;
      }
    }
    return className;
  }


  Method[] getFinderMethodsArray(){
    return this.getMethodsStartingWith(EJB_FIND_START);
  }

  Method[] getHomeMethodsArray(){
    return this.getMethodsStartingWith(EJB_HOME_START);
  }

  Method[] getCreateMethodsArray(){
    return this.getMethodsStartingWith(EJB_CREATE_START);
  }


  private Method[] getMethodsStartingWith(String startingString){
    MethodDescriptor[] descr =info.getMethodDescriptors();
    Method[] methods = null;
    Vector v = new Vector();
    int index = 0;
    for (int i = 0; i < descr.length; i++) {
      MethodDescriptor currentDesc = descr[i];
      Method m = currentDesc.getMethod();
        String methodName = m.getName();
        //System.out.println("methodName: "+methodName);
        if(methodName.startsWith(startingString)){
          v.add(m);
        }

    }
    methods = (Method[])v.toArray(new Method[0]);
    return methods;
  }


  public String getEntityBeanName(){
    return this.shortName+beanSuffix;
  }

  String getParametersInForMethod(Method method){
    String theReturn = "";
    Class[] parameters = method.getParameterTypes();
    for (int i = 0; i < parameters.length; i++) {
      if(i==0){
        theReturn += "p"+i;
      }
      else{
        theReturn += ",p"+i;
      }
    }
    return theReturn;
  }

  /**
   * Returns the Class translated to the right type for finders and creator functions (translated PK class to Entity Class)
   */
  private String getReturnTypeTranslated(Class returnTypeClass){
    if(returnTypeClass.equals(java.util.Collection.class)){
      return returnTypeClass.getName();
    }
    else if(returnTypeClass.equals(java.util.Collection.class)){
      return returnTypeClass.getName();
    }
    else{
      return this.shortName;
    }
  }
}
