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


}