// idega - Gimmi & Eiki
package com.idega.projects.vf.templates;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.presentation.ui.*;
import com.idega.jmodule.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.data.*;
import com.idega.util.*;

public abstract class MainTemplate2 extends JSPModule implements JspPage{

public Table tafla;
public Table frame;

public String language = "IS";

	public void initializePage(){
        super.initializePage();

		Page jmodule = getPage();
		jmodule.setMarginHeight(0);
		jmodule.setMarginWidth(0);
		jmodule.setLeftMargin(0);
		jmodule.setTopMargin(0);
		jmodule.setBackgroundColor("FFFFFF");
		jmodule.setAlinkColor("black");
		jmodule.setVlinkColor("black");
		jmodule.setLinkColor("black");
		jmodule.setHoverColor("#c7c7c7");
                setPage(jmodule);

		Script script = new Script("javascript");
			script.setScriptSource("/js/lvmenu3.js");
		getPage().setAssociatedScript(script);
		getPage().setAttribute("onload","init()");

		jmodule.add(template());
		jmodule.setTitle("Verkfræði og framkvæmdasvið");

	}


	public Table template() {

		String language2 = getIWContext().getRequest().getParameter("language");
			if (language2==null) language2 = ( String ) getIWContext().getSession().getAttribute("language");
			if ( language2 != null) language = language2;

		getIWContext().setSpokenLanguage( language );

		getIWContext().getSession().setAttribute("language",language);

		 frame = new Table(1,1);
			frame.setWidth("100%");
			frame.setAlignment("center");
			frame.setHeight("100%");
			frame.setCellpadding(0);
			frame.setCellspacing(0);
			frame.setColor("000000");
			frame.setColor(1,1,"FFFFFF");
			frame.setRowVerticalAlignment(1,"top");
			frame.setVerticalAlignment(1,1,"top");

		tafla = new Table(4,1);
			tafla.setCellpadding(0);
			tafla.setCellspacing(0);
			tafla.setAlignment(1,1,"left");
			tafla.setAlignment(2,1,"left");
			tafla.setAlignment(3,1,"center");
			tafla.setWidth(1,1,"55");
			tafla.setWidth(2,1,"555");
			tafla.setWidth(3,1,"6");
			tafla.setWidth(4,1,"180");
			tafla.setWidth("796");
			tafla.setHeight("100%");
			tafla.setAlignment("top");
			tafla.setVerticalAlignment("top");
			tafla.setVerticalAlignment(1,1,"top");
			tafla.setVerticalAlignment(2,1,"top");
			tafla.setVerticalAlignment(4,1,"top");
			tafla.addBreak(4,1);
			tafla.setBackgroundImage(1,1,new Image("/pics/leftspacer.gif",""));

		Table header = new Table(2,2);
			header.setWidth(1,"800");
			header.setHeight(1,"70");
			header.setHeight(2,"20");
			header.mergeCells(1,2,2,2);
			header.setCellpadding(0);
			header.setColor(1,1,"#FFFFFF");
			header.setCellspacing(0);
			header.setWidth("100%");
			header.setBackgroundImage(1,1,new Image("/pics/header.jpg","Verkfræði og framkvæmdasvið"));
			header.setBackgroundImage(2,1,new Image("/pics/bannert.gif",""));
			header.setBackgroundImage(1,2,new Image("/pics/bar.gif",""));

		frame.add(header,1,1);
		frame.add(tafla, 1,1);

		return frame;

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

	public void add(PresentationObject objectToAdd){
		tafla.add(Text.getBreak(),2,1);
		tafla.add(objectToAdd,2,1);
	}

    public void addLeft(PresentationObject objectToAdd){
		tafla.add(objectToAdd,1,1);
	}

    public void addBreakLeft(){
		tafla.addBreak(1,1);
	}

    public void addRight(PresentationObject objectToAdd){
		tafla.add(objectToAdd,4,1);
	}

    public void addBreakRight(){
		tafla.addBreak(4,1);
	}

	public void merge(){
		tafla.mergeCells(2,1,3,1);
		tafla.setWidth(1,1,"55");
		tafla.setWidth(2,1,"731");
		tafla.setWidth(4,1,"10");
	}

	public void mergeAll(){
		tafla.mergeCells(2,1,3,1);
		tafla.setWidth(1,1,"10");
		tafla.setWidth(4,1,"10");
		tafla.setWidth(2,1,"776");
		tafla.setBackgroundImage(1,1,new Image("",""));
	}

	public void setLeftAlignment(String align) {
		tafla.setAlignment(1,1,align);
	}

	public void setRightAlignment(String align) {
		tafla.setAlignment(4,1,align);
	}

	public void setAlignment(String align) {
		tafla.setAlignment(2,1,align);
	}

	public void setLeftBackground(Image image) {
		tafla.setBackgroundImage(1,1,image);
	}

}
