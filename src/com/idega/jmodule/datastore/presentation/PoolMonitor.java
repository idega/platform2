//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/
package com.idega.jmodule.datastore.presentation;


import java.util.*;

import com.idega.util.*;
import com.idega.util.database.*;
import com.idega.presentation.text.*;
import	com.idega.presentation.*;
import	com.idega.presentation.ui.*;



/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class PoolMonitor extends Block{

    PoolManager poolmanager;


        public PoolMonitor(){
            poolmanager = PoolManager.getInstance();
            Hashtable stats = poolmanager.getStatsHashtable();

            for (Enumeration e = stats.keys(); e.hasMoreElements();){

                  String name = (String) e.nextElement();
                  add((String)stats.get(name));
                  Form form = new Form();
                  form.setEventListener("com.idega.jmodule.datastore.business.PoolMonitorBusiness");
                  form.add(new SubmitButton("Refresh Datasource Pool Now","poolmonitor_refresh_",name));
                  add(form);
                  addBreak();
            }

        }

}
