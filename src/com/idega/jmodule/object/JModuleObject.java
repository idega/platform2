//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object;

import com.idega.jmodule.object.textObject.*;
import java.util.*;
import java.io.*;
import com.idega.jmodule.login.business.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.3
*/
public class JModuleObject extends ModuleObjectContainer{


  private static Map permissionKeyMap = new Hashtable();

  public JModuleObject(){

  }

  public boolean isAdministrator(ModuleInfo modinfo)throws Exception{
    return AccessControl.isAdmin(modinfo);
  }

  /**
   * <H2>Unimplemented</H2>
   */
  public boolean isDeveloper(ModuleInfo modinfo)throws Exception{
    return false;
  }

  /**
   * <H2>Unimplemented</H2>
   */
  public boolean isUser(ModuleInfo modinfo)throws Exception{
    return false;
  }


  /**
   * <H2>Unimplemented</H2>
   */
  public boolean isMemberOf(ModuleInfo modinfo,String groupName)throws Exception{
    return false;
  }

  /**
   * <H2>Unimplemented</H2>
   */
  public boolean hasPermission(String permissionType,ModuleInfo modinfo,ModuleObject obj)throws Exception{
  //  return AccessControl.hasPermission(permissionType,modinfo,obj);
    return false;
  }

/* public boolean hasPermission(String permissionType,ModuleInfo modinfo)throws Exception{
    return hasPermission(permissionType,modinfo,this);
  }*/


  public String getModuleName(){
    return this.getClass().getName();
  }

  /**
   * Implement in subclasses:
   */
  protected void registerPermissionKeys(){
  }

  protected void registerPermissionKey(String permissionKey,String localizeableKey){
    Map m = (Map)getPermissionKeyMap().get(this.getClass());
    if(m==null){
      m = new Hashtable();
      getPermissionKeyMap().put(this.getClass(),m);
    }
    m.put(permissionKey,localizeableKey);
  }

  protected void registerPermissionKey(String permissionKey){
    registerPermissionKey(permissionKey,permissionKey);
  }

  private Map getPermissionKeyMap(){
    return permissionKeyMap;
  }

  String[] getPermissionKeys(JModuleObject obj){
    Map m = (Map)getPermissionKeyMap().get(this.getClass());
    if(m!=null){
      return (String[])m.keySet().toArray(new String[0]);
    }
    return null;
  }

}