
/**
 * Title:        null<p>
 * Description:  null<p>
 * Copyright:    null<p>
 * Company:      idega multimedia<p>
 * @author null
 * @version null
 */
package com.idega.projects.golf.templates.page;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.projects.golf.*;
import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import java.sql.*;
import java.io.*;


public class GolfMainJSPModulePage2 extends GolfMainJSPModulePage{


  //private Table mainTable;



  public GolfMainJSPModulePage2(){
    super();
    //initMainTable();
  }






  protected void User(ModuleInfo modinfo) throws SQLException,IOException{
    this.setTextDecoration("none");
    setTopMargin(5);
    add("middle", Center());
    Top(modinfo);
    add( "top", golfHeader());
    add("top", Top(modinfo));
    add("bottom", golfFooter());
  }




        protected void initCenter(){
          centerTable = new Table (1,1);
          centerTable.setVerticalAlignment(1,1,"top");
//          mainTable.setBorder(1);
          centerTable.setCellpadding(0);
          centerTable.setCellspacing(0);
          centerTable.setWidth("100%");
          centerTable.setHeight("100%");
        }



        public void setVerticalAlignment( String alignment ){
          centerTable.setVerticalAlignment( 1, 1, alignment);
        }



	public void add(ModuleObject objectToAdd){
	  centerTable.add(objectToAdd,1,1);
	}






}   //class GolfMainJSPModule2
