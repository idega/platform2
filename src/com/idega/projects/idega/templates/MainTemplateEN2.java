// idega - Laddi
package com.idega.projects.idega.templates;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.data.*;
import com.idega.util.*;

public abstract class MainTemplateEN2 extends JSPModule implements JspPage{

public Table tafla;
public Table frame;
public Table header;
public Table outerTable;
public Table productTable;
public String subPage = "";
public Table rightTable;

public String language = "EN";

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
		jmodule.setTitle("idega margmiðlun hf.");

	}


	public Table template() {

		 ModuleInfo modinfo = getModuleInfo();

                 String page = modinfo.getRequest().getRequestURI();
                 subPage = page.substring(1,page.indexOf("."));

                 frame = new Table(1,4);
			frame.setWidth("628");
                        frame.setHeight("100%");
			frame.setAlignment("center");
			frame.setHeight(1,3,"100%");
                        frame.setHeight(4,"15");
			frame.setCellpadding(0);
			frame.setCellspacing(0);
			frame.setColor("000000");
			frame.setColor(1,1,"FFFFFF");
			frame.setColor(1,2,"FFFFFF");
			frame.setColor(1,3,"FFFFFF");
			frame.setColor(1,4,"FFFFFF");
			frame.setRowVerticalAlignment(1,"top");
			frame.setVerticalAlignment(1,1,"top");
                        frame.setVerticalAlignment(1,4,"top");
                        frame.setVerticalAlignment(1,3,"top");
                        frame.setRowAlignment(4,"center");

		header = new Table(1,1);
			header.setWidth("100%");
			//header.setHeight("278");
			header.setCellpadding(0);
			header.setCellspacing(0);
			header.setColor(1,1,"#FFFFFF");
			//header.setBorder(1);

		Table borderTable = new Table(1,1);
                      borderTable.setCellpadding(0);
                      borderTable.setCellspacing(1);
                      borderTable.setWidth("100%");
                      borderTable.setColor("#000000");
                      borderTable.setHeight("100%");

                outerTable = new Table(1,2);
			//outerTable.setBorder(1);
                        outerTable.setColor("#FFFFFF");
			outerTable.setWidth("100%");
			outerTable.setHeight("100%");
			outerTable.setCellpadding(0);
			outerTable.setCellspacing(0);
			outerTable.setColor(1,1,"#FFFFFF");
			outerTable.setColor(1,2,"#FFFFFF");
			outerTable.setHeight(1,2,"100%");
			//outerTable.setHeight(2,1,"100%");
                        outerTable.setHeight(1,1,"17");
                        outerTable.setVerticalAlignment(1,1,"top");
                        outerTable.setVerticalAlignment(1,2,"top");

                Image companyImage = new Image("/pics/undirsida/fyrirtaekid1.gif","About us",66,17);
                Image staffImage = new Image("/pics/undirsida/starfsmenn1.gif","Employees",69,17);
                Image productsImage = new Image("/pics/undirsida/vorur1.gif","Products",58,17);
                Image customersImage = new Image("/pics/undirsida/vidskiptavinir1.gif","Customers",68,17);
                Image associatesImage = new Image("/pics/undirsida/samstarfsadilar1.gif","Partners",58,17);
                Image supportImage = new Image("/pics/undirsida/adstod1.gif","Support",68,17);
                Image homeImage = new Image("/pics/undirsida/heim.gif","Home",43,17);

                if ( subPage != null ) {
                  if ( subPage.equalsIgnoreCase("company") ) {
                    companyImage.setSrc("/pics/undirsida/fyrirtaekid.gif");
                  }
                  if ( subPage.equalsIgnoreCase("staff") ) {
                    staffImage.setSrc("/pics/undirsida/starfsmenn.gif");
                  }
                  if ( subPage.equalsIgnoreCase("products") ) {
                    productsImage.setSrc("/pics/undirsida/vorur.gif");
                  }
                  if ( subPage.equalsIgnoreCase("customers") ) {
                    customersImage.setSrc("/pics/undirsida/vidskiptavinir.gif");
                  }
                  if ( subPage.equalsIgnoreCase("associates") ) {
                    associatesImage.setSrc("/pics/undirsida/samstarfsadilar.gif");
                  }
                  if ( subPage.equalsIgnoreCase("support") ) {
                    supportImage.setSrc("/pics/undirsida/adstod.gif");
                  }
                }

                Link companyLink = new Link(companyImage,"/company.jsp");
                Link staffLink = new Link(staffImage,"/staff.jsp");
                Link productsLink = new Link(productsImage,"/products.jsp");
                Link customersLink = new Link(customersImage,"/customers.jsp");
                Link associatesLink = new Link(associatesImage,"/associates.jsp");
                Link supportLink = new Link(supportImage,"/support.jsp");
                Link homeLink = new Link(homeImage,"/index.jsp");

		Table linkTable = new Table(2,1);
                  linkTable.setWidth("100%");
                  linkTable.setCellpadding(0);
                  linkTable.setCellspacing(0);
                  linkTable.setAlignment(1,1,"left");
                  linkTable.setAlignment(2,1,"right");
                  linkTable.setVerticalAlignment(1,1,"top");
                  linkTable.addText("",2,1);
                  linkTable.setBackgroundImage(new Image("/pics/undirsida/menuback.gif"));
                  linkTable.add(new Image("/pics/undirsida/horn.gif",""),2,1);

                linkTable.add(companyLink,1,1);
		linkTable.add(staffLink,1,1);
		linkTable.add(productsLink,1,1);
		linkTable.add(customersLink,1,1);
		linkTable.add(associatesLink,1,1);
		//linkTable.add(supportLink,1,1);
                linkTable.add(homeLink,1,1);

                Table innerTable = new Table(2,1);
                  innerTable.setCellpadding(0);
                  innerTable.setCellspacing(0);
                  innerTable.setWidth("100%");
                  innerTable.setHeight("100%");
                  innerTable.setWidth(2,"102");
                  innerTable.setColor("#FFFFFF");

                outerTable.add(linkTable,1,1);
                innerTable.add(outerTable,1,1);

                tafla = new Table(3,2);
			//tafla.setBorder(1);
			//tafla.setCellpadding(0);
			//tafla.setCellspacing(0);
			tafla.setWidth(1,1,"130");
			tafla.setWidth(2,1,"1");
                        tafla.mergeCells(3,1,3,2);
                        tafla.mergeCells(2,1,2,2);
                        tafla.setAlignment(1,2,"center");
                        tafla.setVerticalAlignment(1,2,"bottom");
			tafla.setWidth("100%");
			tafla.setHeight("100%");
			tafla.setAlignment("top");
			tafla.setVerticalAlignment("top");
			tafla.setVerticalAlignment(1,1,"top");
			tafla.setVerticalAlignment(3,1,"top");
			tafla.setBackgroundImage(2,1,new Image("/pics/divider.gif",""));
                        tafla.addText("",2,1);

                        Image idega = new Image("/pics/idega_bottom.gif","idega margmiðlun",121,41);
                        Link idegaLink = new Link(idega,"/index.jsp");

                        tafla.add(idegaLink,1,2);
                        //tafla.add(pageText,1,1);
                        //tafla.addBreak(1,1);

                        Image headerImage = new Image("/pics/header2.gif","",628,48);
                              header.add(headerImage,1,1);

                Paragraph idegaParagraph = new Paragraph("center","idega","idega");
                  idegaParagraph.add("idega inc. | Baejarlind 14 - 16 | phone: 554 - 7557 | fax: 554 - 7749 | email: <a href=\"mailto:idega@idega.is\">idega@idega.is</a> | web: <a href=\"http://www.idega.is\">www.idega.is</a>");

                rightTable = new Table(1,3);
                  rightTable.setBorder(0);
                  rightTable.setCellpadding(0);
                  rightTable.setCellspacing(0);
                  rightTable.setWidth("100%");
                  rightTable.setHeight("100%");
                  rightTable.setHeight(1,1,"6");
                  rightTable.setHeight(1,2,"100%");
                  rightTable.setVerticalAlignment(1,1,"top");
                  rightTable.setVerticalAlignment(1,2,"top");
                  rightTable.setVerticalAlignment(1,3,"bottom");
                  rightTable.setAlignment(1,2,"center");
                  rightTable.setAlignment(1,3,"right");
                  rightTable.add(new Image("/pics/undirsida/biti.gif","",102,6),1,1);

                  Window piWindow = new Window("loginWindow",250,150,"/login.jsp?");
                    piWindow.setScrollbar(false);
                  Image pi = new Image("/pics/pi.gif","",7,7);
                          pi.setAttribute("vspace","3");
                          pi.setAttribute("hspace","3");
                  Link piLink = new Link(pi,piWindow);

                  rightTable.add(piLink,1,3);


                  Flash flash = new Flash("/flash/rightside.swf","",102,430);
                  rightTable.add(flash,1,2);


                frame.add(idegaParagraph,1,4);

		outerTable.add(tafla,1,2);
                innerTable.add(rightTable,2,1);
		frame.add(header,1,2);
		borderTable.add(innerTable);
                frame.add(borderTable,1,3);

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

	public void add(ModuleObject objectToAdd){
		tafla.add(objectToAdd,3,1);
	}

        public void addRight(ModuleObject objectToAdd){
                rightTable.add(objectToAdd,1,2);
        }

        public void addLeft(ModuleObject objectToAdd){
                    tafla.add(objectToAdd,1,1);
        }

	public void setLeftAlignment(String align) {
		tafla.setAlignment(1,1,align);
	}

	public void setAlignment(String align) {
		tafla.setAlignment(3,3,align);
	}

        public void setSubPage(String subPage) {
            this.subPage=subPage;
        }

}
