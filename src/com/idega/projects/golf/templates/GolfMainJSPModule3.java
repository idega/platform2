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


public class GolfMainJSPModule3 extends GolfMainJSPModule{


  private Table centerTable;
  private Table insertTable;
  private String align;


  public void initializePage(){
    super.initializePage();
  }



       protected void User()throws SQLException,IOException{
              getPage().setTextDecoration("none");
              setTopMargin(5);
              add( "top", golfHeader());
              add("top", Top());
              add("bottom", golfFooter());
              add("middle", Center());
	}


        protected Table Center()throws SQLException, IOException{
          if (centerTable==null){
          	centerTable = new Table(2,1);
          	centerTable.add(Left(), 1, 1);
          	centerTable.setWidth(1 ,"" + LEFTWIDTH);
          	centerTable.setWidth(2 ,"" + (SIDEWIDTH-LEFTWIDTH));
//        	  centerTable.setWidth("100%");
          	centerTable.setCellpadding(0);
          	centerTable.setCellspacing(0);
          	setVerticalAlignment( "top" );
          }//else centerTable.empty();

          return centerTable;
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
