
/**
 * Title:        null<p>
 * Description:  null<p>
 * Copyright:    null<p>
 * Company:      idega multimedia<p>
 * @author null
 * @version null
 */
package com.idega.projects.golf.templates;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.projects.golf.*;
import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import java.sql.*;
import java.io.*;


public class GolfMainJSPModule2 extends GolfMainJSPModule{


  private Table mainTable;



  public void initializePage(){
    super.initializePage();

  }


  protected void Admin(){

  }




  protected void User() throws SQLException,IOException{
    getPage().setTextDecoration("none");
    setTopMargin(5);
    setMainTable();
    Top();
    add( "top", golfHeader());
    add("top", Top());
    add("bottom", golfFooter());
  }






        private void setMainTable(){

          mainTable = new Table (1,1);
          mainTable.setVerticalAlignment(1,1,"top");
//          mainTable.setBorder(1);
          mainTable.setCellpadding(0);
          mainTable.setCellspacing(0);
          mainTable.setWidth("100%");
          mainTable.setHeight("100%");
          add("middle", mainTable);
        //  setVerticalAlignment( "top" );
       }




        public void setVerticalAlignment( String alignment ){
          mainTable.setVerticalAlignment( 1, 1, alignment);
        }



	public void add(ModuleObject objectToAdd){
	  mainTable.add(objectToAdd,1,1);
	}






}   //class GolfMainJSPModule2
