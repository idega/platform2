package com.idega.projects.lv.templates;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import java.sql.*;
import java.io.*;


public class OrkuTemplate extends JSPModule {

	String align;
	Table myTable;
	Table myTable2;
	Table contentTable;
	Table contentTable2;

  public void initializePage(){

    super.initializePage();

	getPage().setLinkColor("000000");
	getPage().setVlinkColor("000000");
	getPage().setAlinkColor("000000");

    getPage().setMarginWidth(0);
    getPage().setMarginHeight (0);
    getPage().setLeftMargin(0);
    getPage().setTopMargin(0);

	super.add(template());
	super.add("<br>");
	super.add(template2());
  }

	public boolean isAdmin() {
		if (getSession().getAttribute("member_access") != null) {
			if (getSession().getAttribute("member_access").equals("admin")) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	public Table template(){
		if (myTable == null){
			myTable = new Table(3,2);
			myTable.setCellpadding(0);
			myTable.setCellspacing(0);
			myTable.setBorder(0);
			myTable.mergeCells(1,1,2,1);
			myTable.setHeight(1,"73");
			myTable.setWidth(1,"160");
			myTable.setHeight(2,"127");
			myTable.setWidth(2,"860");


			myTable.setBackgroundImage(1,1,new Image("/orkusvid/pics/topp1.jpg"));
			myTable.setBackgroundImage(3,1,new Image("/orkusvid/pics/topp2tiler.gif"));
			//myTable.add(new Image("/images/spot.gif","spot",160,10),1,2);
			myTable.setBackgroundImage(1,2,new Image("/orkusvid/pics/2row1.jpg"));
			myTable.setBackgroundImage(2,2,new Image("/orkusvid/pics/2row2.jpg"));

                        myTable.setVerticalAlignment(1,1,"top");
                        myTable.setVerticalAlignment(1,2,"top");
                        myTable.setRowVerticalAlignment(2,"top");
                        myTable.setAlignment(1,2,"left");
                        myTable.setAlignment(1,1,"left");
                        //myTable.setRowAlignment(2,"left");


			myTable.setBackgroundImage(3,2,new Image("/orkusvid/pics/2row3tiler.gif"));
			myTable.setWidth("100%");

			Link landsvirkjun = new Link(new Image("/images/spot.gif","Heimasíða Landsvirkjunar",110,57),"http://www.lv.is");
			myTable.add(landsvirkjun,1,1);

                      Link intranet = new Link(new Image("/images/spot.gif","Intranet",100,40),"http://172.16.172.7");
		        myTable.add(intranet,1,2);
                }

		return myTable;
	}

	public Table template2(){
		if (myTable2 == null){
			myTable2 = new Table(3,1);
			myTable2.setCellpadding(0);
			myTable2.setCellspacing(0);
			myTable2.setBorder(0);
			myTable2.setWidth(1,"85");
			myTable2.setHeight("100%");
			myTable2.setWidth("100%");
			myTable2.setAlignment(3,1,"right");

			myTable2.setRowVerticalAlignment(1,"top");
			myTable2.setBackgroundImage(1,1,new Image("/orkusvid/pics/5rowtiler.gif"));
			myTable2.add(new Image("/orkusvid/pics/4row1.gif"),1,1);


			try {
				myTable2.add(content(),2,1);
				myTable2.add(content2(),3,1);
			}
			catch (IOException io) {
				System.err.println(io.getMessage());
			}

			myTable2.add(new Image("/images/spot.gif","",455,1),2,1);

		}

		return myTable2;
	}

	public Table content()throws IOException {
		if (contentTable == null){
			contentTable = new Table(1,1);
			contentTable.setBorder(0);
			contentTable.setVerticalAlignment(1,1,"top");
			contentTable.setWidth("100%");
			contentTable.setHeight("100%");
			contentTable.setCellpadding(0);
			contentTable.setCellspacing(0);
		}
		return contentTable;
	}

	public Table content2()throws IOException {
		if (contentTable2 == null){
			contentTable2 = new Table(1,1);
			contentTable2.setBorder(0);
			contentTable2.setVerticalAlignment(1,1,"top");
			contentTable2.setAlignment(1,1,"left");
			contentTable2.setWidth("240");
			contentTable2.setHeight("100%");
			contentTable2.setCellpadding(0);
			contentTable2.setCellspacing(0);
		}
		return contentTable2;
	}

// ###########  Public - Föll
	public void add(ModuleObject objectToAdd){
		try{
			content().add(objectToAdd,1,1);
		}
		catch(IOException ex){
			System.err.println(ex.getMessage());
		}
	}

	public void add2(ModuleObject objectToAdd){
		try{
			content2().add(objectToAdd,1,1);
		}
		catch(IOException ex){
			System.err.println(ex.getMessage());
		}
	}

}  // class LVMainJSPModule
