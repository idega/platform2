package com.idega.projects.lv.templates;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.projects.golf.templates.*;
import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.poll.moduleobject.*;
import java.sql.*;
import java.io.*;


public class LvMainJSPModule extends JSPModule {

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
    getPage().setTitle("Flutningssvið");

    Script script = new Script("javascript");
    	script.setScriptSource("/js/lvmenu2.js");
	getPage().setAssociatedScript(script);
    getPage().setAttribute("onload","init()");

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
		myTable.setHeight(1,"67");
		myTable.setWidth(1,"92");
		myTable.setHeight(2,"103");
		myTable.setWidth(2,"657");
		myTable.setRowVerticalAlignment(2,"top");
		myTable.setRowAlignment(2,"center");
		myTable.setBackgroundImage(1,1,new Image("/images/a1-header.jpg"));
		myTable.setBackgroundImage(3,1,new Image("/images/a1-header_b.jpg"));

		Link intranet = new Link(new Image("/images/spot.gif","Intranet",92,20),"http://172.16.172.7");
		myTable.add(intranet,1,2);

		myTable.setBackgroundImage(1,2,new Image("/images/c1.jpg"));
		myTable.setBackgroundImage(2,2,new Image("/images/c2.jpg"));
		myTable.setBackgroundImage(3,2,new Image("/images/c3.jpg"));
		myTable.setWidth("100%");

		Link landsvirkjun = new Link(new Image("/images/spot.gif","Heimasíða Landsvirkjunar",92,57),"http://www.lv.is");
		myTable.add(landsvirkjun,1,1);
		}

		return myTable;
	}

	public Table template2(){
	if (myTable2 == null){
		myTable2 = new Table(3,1);
		myTable2.setCellpadding(0);
		myTable2.setCellspacing(0);
		myTable2.setBorder(0);
		myTable2.setWidth(1,"92");
		myTable2.setHeight("100%");
		myTable2.setWidth("100%");
		myTable2.setAlignment(3,1,"right");

		myTable2.setRowVerticalAlignment(1,"top");
		myTable2.setBackgroundImage(1,1,new Image("/images/d2-kantur-tiler.gif"));
		myTable2.add(new Image("/images/d1-kantur.jpg"),1,1);


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
