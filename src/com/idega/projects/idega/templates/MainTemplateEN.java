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
import java.util.*;

public abstract class MainTemplateEN extends JSPModule implements JspPage{

public Table tafla;
public Table frame;
public Table header;
public Table outerTable;
private boolean isMerged = false;

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

		 frame = new Table(1,4);
			frame.setWidth("628");
                        frame.setHeight("100%");
			frame.setAlignment("center");
			frame.setHeight(1,3,"100%");
                        frame.setHeight(4,"15");
			frame.setCellpadding(0);
			frame.setCellspacing(0);
			frame.setColor(1,1,"FFFFFF");
			frame.setColor(1,2,"FFFFFF");
			frame.setColor(1,3,"FFFFFF");
			frame.setColor(1,4,"FFFFFF");
			frame.setRowVerticalAlignment(1,"top");
			frame.setVerticalAlignment(1,1,"top");
			frame.setVerticalAlignment(1,3,"top");
                        frame.setVerticalAlignment(1,4,"top");
                        frame.setRowAlignment(4,"center");

		header = new Table(1,1);
			header.setWidth("100%");
			header.setCellpadding(0);
			header.setCellspacing(0);
			header.setColor(1,1,"#FFFFFF");

		outerTable = new Table(2,2);
			outerTable.setWidth("100%");
			outerTable.setHeight("100%");
			outerTable.setCellpadding(0);
			outerTable.setCellspacing(1);
			outerTable.setColor("#000000");
			outerTable.setColor(1,2,"#FFFFFF");
			outerTable.setColor(1,1,"#ECEFF1");
			outerTable.setColor(2,1,"#FF9310");
			outerTable.mergeCells(1,2,2,2);
			outerTable.setWidth(1,1,"101");
			outerTable.setWidth(2,1,"524");
			outerTable.setHeight(1,"12");
			outerTable.setHeight(2,"100%");
			outerTable.setAlignment(1,1,"center");
			outerTable.setVerticalAlignment(1,1,"middle");
			outerTable.setVerticalAlignment(1,2,"top");

		idegaTimestamp clock = new idegaTimestamp();

		Text clockText = new Text(clock.getENGDate());
			clockText.setFontSize(1);
			outerTable.add(clockText,1,1);

		tafla = new Table(5,2);
			tafla.setAlignment(1,1,"left");
			tafla.setAlignment(3,1,"left");
			tafla.setAlignment(5,1,"left");
			tafla.setWidth(1,1,"190");
			tafla.setWidth(2,1,"1");
                        tafla.mergeCells(2,1,2,2);
                        if ( !isMerged ) {
                          tafla.mergeCells(3,1,3,2);
                          tafla.mergeCells(4,1,4,2);
                        }
                        tafla.setVerticalAlignment(1,2,"bottom");
                        tafla.setVerticalAlignment(5,2,"bottom");
                        tafla.setAlignment(5,2,"right");
                        tafla.setAlignment(1,2,"center");
			tafla.setWidth(4,1,"1");
			tafla.setWidth(5,1,"190");
			tafla.setWidth("100%");
			tafla.setHeight("100%");
			tafla.setAlignment("top");
			tafla.setVerticalAlignment("top");
			tafla.setVerticalAlignment(1,1,"top");
			tafla.setVerticalAlignment(3,1,"top");
			tafla.setVerticalAlignment(5,1,"top");
			tafla.setBackgroundImage(2,1,new Image("/pics/divider.gif",""));
			if ( isMerged == false ) {
                          tafla.setBackgroundImage(4,1,new Image("/pics/divider.gif",""));
                          tafla.addText("",4,1);
                        }
                        tafla.addText("",2,1);

                        Window piWindow = new Window("loginWindow",250,150,"/login.jsp?");
                          piWindow.setScrollbar(false);
                        Image pi = new Image("/pics/pi.gif","",7,7);
                        Link piLink = new Link(pi,piWindow);

                        Image idegaLogo = new Image("/pics/idega.gif","idega margmiðlun",150,53);

                        tafla.add(idegaLogo,1,2);

                        if ( !isMerged ) {
                          tafla.add(piLink,5,2);
                        }

                        Image mainChoice = new Image("/pics/adalval.gif","",100,24);
                        Image newsImage = new Image("/pics/frettir.gif","",68,24);
                        Image products = new Image("/pics/hugbunadur.gif","",112,24);

                        tafla.add(mainChoice,1,1);
                        tafla.addBreak(1,1);
                        tafla.add(newsImage,3,1);
                        tafla.addBreak(3,1);
                        if ( !isMerged ) {
                          tafla.add(products,5,1);
                          tafla.addBreak(5,1);
                        }
                        tafla.add(getLinksTable(),1,1);

                        Image headerImage = new Image("/pics/header.jpg","",628,278);
                              //header.add(headerImage,1,1);

			Random number = new Random();
			int random = number.nextInt(2) + 1;

                        Flash flashHeader = new Flash("/flash/header.swf",628,278);
                              header.add(flashHeader,1,1);

                        Flash flash = new Flash("http://jgenerator.sidan.is/theTicker.swt",524,13);
                              outerTable.add(flash,2,1);

                Paragraph idegaParagraph = new Paragraph("center","idega","idega");
                  idegaParagraph.add("idega inc. | Baejarlind 14 - 16 | phone: 554 - 7557 | fax: 554 - 7749 | email: <a href=\"mailto:idega@idega.is\">idega@idega.is</a> | web: <a href=\"http://www.idega.is\">www.idega.is</a>");

                frame.add(idegaParagraph,1,4);

		outerTable.add(tafla,1,2);
		frame.add(header,1,2);
		frame.add(outerTable,1,3);

		return frame;

	}

        private Table getLinksTable() {

            Table myTable = new Table(2,6);
              myTable.setWidth("100%");
              myTable.setCellspacing(6);

                Image companyImage = new Image("/pics/fyrirtaekid.gif","About us",125,20);
                Image staffImage = new Image("/pics/starfsmenn.gif","Staff",125,20);
                Image productsImage = new Image("/pics/vorur.gif","Products",125,20);
                Image customersImage = new Image("/pics/vidskiptavinir.gif","Customers",125,20);
                Image associatesImage = new Image("/pics/samstarfsadilar.gif","Associates",125,20);
                Image supportImage = new Image("/pics/adstod.gif","Support",125,20);

                Link companyLink = new Link(companyImage,"/company.jsp");
                Link staffLink = new Link(staffImage,"/staff.jsp");
                Link productsLink = new Link(productsImage,"/products.jsp");
                Link customersLink = new Link(customersImage,"/customers.jsp");
                Link associatesLink = new Link(associatesImage,"/associates.jsp");
                Link supportLink = new Link(supportImage,"/support.jsp");

                myTable.add(companyLink,1,1);
		myTable.add(staffLink,1,2);
		myTable.add(productsLink,1,3);
		myTable.add(customersLink,1,4);
		myTable.add(associatesLink,1,5);
		//myTable.add(supportLink,1,6);

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
		tafla.add(objectToAdd,3,1);
	}

        public void addLeft(ModuleObject objectToAdd){
                    tafla.add(objectToAdd,1,1);
        }

        public void addBreakLeft(){
                    tafla.addBreak(1,1);
        }

        public void addRight(ModuleObject objectToAdd){
                    tafla.add(objectToAdd,5,1);
        }

        public void addBreakRight(){
                    tafla.addBreak(5,1);
        }

        public void addFlash(ModuleObject objectToAdd){
                    header.add(objectToAdd,1,1);
        }

        public void addScroller(ModuleObject objectToAdd){
                    outerTable.add(objectToAdd,2,1);
        }

	public void setLeftAlignment(String align) {
		tafla.setAlignment(1,1,align);
	}

	public void setRightAlignment(String align) {
		tafla.setAlignment(5,1,align);
	}

	public void setAlignment(String align) {
		tafla.setAlignment(3,1,align);
	}

        public void merge() {
            tafla.mergeCells(3,1,5,2);
            this.isMerged=true;
        }

}
