// idega - Laddi
package com.idega.projects.sbudgeting.templates;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.data.*;
import com.idega.util.*;

public abstract class MainTemplate extends JSPModule implements JspPage{

public Table tafla;
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
                jmodule.setHoverColor("#FF9310");
                jmodule.setTextDecoration("none");
                jmodule.setStyleSheetURL("/style/idega.css");
                setPage(jmodule);

                jmodule.add(template());
		jmodule.setTitle("Stratetic Budgeting");

	}


	public Table template() {

		ModuleInfo modinfo = getModuleInfo();

                Table myTable = new Table(1,1);
                  myTable.setCellpadding(0);
                  myTable.setCellspacing(0);
                  myTable.setWidth("100%");
                  myTable.setHeight("100%");

                tafla = new Table(2,3);
                    //tafla.setBorder(1);
                    tafla.setCellpadding(0);
                    tafla.setCellspacing(0);
                    tafla.setWidth("800");
                    tafla.setHeight("100%");
                    tafla.setWidth(1,2,"214");
                    tafla.setWidth(2,2,"586");
                    tafla.setHeight(1,"110");
                    tafla.setHeight(2,"100%");
                    tafla.mergeCells(1,1,2,1);
                    tafla.setAlignment(1,2,"center");
                    tafla.setAlignment("top");
                    tafla.setVerticalAlignment("top");
                    tafla.setVerticalAlignment(1,2,"top");
                    tafla.setVerticalAlignment(2,2,"top");
                    tafla.setVerticalAlignment(2,3,"bottom");
                    tafla.setAlignment(2,3,"right");

                    Image headerImage = new Image("/pics/topp.jpg","Stratetic Budgeting",800,110);
                    tafla.add(headerImage,1,1);

                    Window piWindow = new Window("loginWindow",250,150,"/login.jsp?");
                      piWindow.setScrollbar(false);
                    Image pi = new Image("/pics/pi.gif","",7,7);
                      pi.setAttribute("vspace","3");
                      pi.setAttribute("hspace","3");
                    Link piLink = new Link(pi,piWindow);

                    tafla.add(piLink,2,3);

                myTable.add(tafla,1,1);

		return myTable;

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

	public void add(ModuleObject objectToAdd){
		tafla.add(objectToAdd,2,2);
	}

        public void addLeft(ModuleObject objectToAdd){
                tafla.add(objectToAdd,1,2);
        }

	public void setLeftAlignment(String align) {
		tafla.setAlignment(1,2,align);
	}

	public void setAlignment(String align) {
		tafla.setAlignment(2,2,align);
	}
}
