//idega 2001 - Tryggvi Larusson
/*
*Copyright 2001 idega.is All Rights Reserved.
*/
package com.idega.jmodule.datastore.business;


import java.util.*;
import com.idega.util.*;
import com.idega.util.database.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.business.IWEventListener;
import com.idega.idegaweb.IWException;



/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class PoolMonitorBusiness implements IWEventListener{


      private static String controlParameter = "poolmonitor_control";
      private PoolManager poolmanager;


      public PoolMonitorBusiness(){
          poolmanager = PoolManager.getInstance();
      }




      public void actionPerformed(ModuleInfo modinfo)throws IWException{


      }



      public static String getControlParameter(){
          return controlParameter;
      }

}
