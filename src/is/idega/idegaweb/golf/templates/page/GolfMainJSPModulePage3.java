package is.idega.idegaweb.golf.templates.page;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import is.idega.idegaweb.golf.*;
import com.idega.jmodule.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import java.sql.*;
import java.io.*;


public class GolfMainJSPModulePage3 extends GolfMainJSPModulePage{


  private Table insertTable;
  private String align;


  public GolfMainJSPModulePage3(){
    super();
    initCenter();
  }



       protected void User(IWContext iwc)throws SQLException,IOException{
              this.setTextDecoration("none");
              setTopMargin(5);
              centerTable.add(Left(iwc), 1, 1);
              add( "top", golfHeader());
              add("top", Top(iwc));
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


	public void add(PresentationObject objectToAdd){
	  centerTable.add(objectToAdd,2,1);
	}


}  // class GolfMainJSPModule3
