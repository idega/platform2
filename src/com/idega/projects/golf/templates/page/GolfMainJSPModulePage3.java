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


public class GolfMainJSPModulePage3 extends GolfMainJSPModulePage{


  private Table insertTable;
  private String align;


  public GolfMainJSPModulePage3(){
    super();
    initCenter();
  }



       protected void User(ModuleInfo modinfo)throws SQLException,IOException{
              this.setTextDecoration("none");
              setTopMargin(5);
              centerTable.add(Left(modinfo), 1, 1);
              add( "top", golfHeader());
              add("top", Top(modinfo));
              add("bottom", golfFooter());
              add("middle", Center());
	}


        protected void initCenter(){
          if (centerTable==null){
          	centerTable = new Table(2,1);

          	centerTable.setWidth(1 ,"" + LEFTWIDTH);
          	centerTable.setWidth(2 ,"" + (SIDEWIDTH-LEFTWIDTH));
//        	  centerTable.setWidth("100%");
          	centerTable.setCellpadding(0);
          	centerTable.setCellspacing(0);
          	setVerticalAlignment( "top" );
          }//else centerTable.empty();

        }


        public void setVerticalAlignment( String alignment ){
          centerTable.setVerticalAlignment( alignment );
          centerTable.setVerticalAlignment( 1, 1, alignment);
          centerTable.setVerticalAlignment( 2, 1, alignment);
        }


	public void add(ModuleObject objectToAdd){
	  centerTable.add(objectToAdd,2,1);
	}


}  // class GolfMainJSPModule3
