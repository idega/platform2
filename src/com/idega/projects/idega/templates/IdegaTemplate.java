// idega - Laddi
package com.idega.projects.idega.templates;

import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.util.*;

public class IdegaTemplate extends Page{

public Table tafla;
public Table frame;
public Table header;
public Table outerTable;

public String language = "IS";

	public IdegaTemplate(){

		Page jmodule = this;
		jmodule.setMarginHeight(0);
		jmodule.setMarginWidth(0);
		jmodule.setLeftMargin(0);
		jmodule.setTopMargin(0);
		jmodule.setBackgroundColor("FFFFFF");
		jmodule.setAlinkColor("black");
		jmodule.setVlinkColor("black");
		jmodule.setLinkColor("black");

                super.add(template());
		jmodule.setTitle("idega margmiðlun hf.");

	}


	public Table template() {

		 frame = new Table(1,4);
			frame.setWidth("628");
			frame.setAlignment("center");
			frame.setHeight(1,3,"100%");
                        frame.setHeight(4,"30");
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
                        frame.setRowAlignment(4,"center");

		header = new Table(1,1);
			header.setWidth("100%");
			//header.setHeight("278");
			header.setCellpadding(0);
			header.setCellspacing(0);
			header.setColor(1,1,"#FFFFFF");
			//header.setBorder(1);

		outerTable = new Table(2,2);
			//outerTable.setBorder(1);
			outerTable.setWidth("100%");
			//outerTable.setHeight("100%");
			outerTable.setCellpadding(0);
			outerTable.setCellspacing(1);
			outerTable.setColor("#000000");
			outerTable.setColor(1,1,"#FFFFFF");
			outerTable.setColor(2,1,"#FFFFFF");
			outerTable.setColor(1,2,"#FFFFFF");
			outerTable.mergeCells(1,2,2,2);
			outerTable.setWidth(1,1,"101");
			outerTable.setWidth(2,1,"524");
			outerTable.setHeight(1,"12");
			outerTable.setHeight(2,"100%");
			outerTable.setAlignment(1,1,"center");
			outerTable.setVerticalAlignment(1,1,"middle");
			outerTable.setColor(1,1,"#ECEFF1");
			outerTable.setColor(2,1,"#FF9310");
			//outerTable.addText("",1,1);
                        //outerTable.addText("",2,1);

		idegaTimestamp clock = new idegaTimestamp();

		Text clockText = new Text(clock.getISLDate());
			clockText.setFontSize(1);
			outerTable.add(clockText,1,1);

		tafla = new Table(5,2);
			//tafla.setBorder(1);
			//tafla.setCellpadding(0);
			//tafla.setCellspacing(0);
			tafla.setAlignment(1,1,"left");
			tafla.setAlignment(3,1,"left");
			tafla.setAlignment(5,1,"center");
			tafla.setWidth(1,1,"190");
			tafla.setWidth(2,1,"1");
                        tafla.mergeCells(2,1,2,2);
                        tafla.mergeCells(3,1,3,2);
                        tafla.mergeCells(4,1,4,2);
			//tafla.setWidth(3,1,"100%");
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
			tafla.setBackgroundImage(2,1,new Image("/pics/idegaTemplate/divider.gif",""));
			tafla.setBackgroundImage(4,1,new Image("/pics/idegaTemplate/divider.gif",""));

                        Image pi = new Image("/pics/idegaTemplate/pi.gif");
                        Link piLink = new Link(pi,"/login.jsp");
                        Image idegaLogo = new Image("/pics/idegaTemplate/idega.gif");

                         Image headerImage = new Image("/pics/idegaTemplate/header.jpg");
                              header.add(headerImage,1,1);

                          Flash flash = new Flash("http://clarke.idega.is/theTicker.swt",524,13);
                              outerTable.add(flash,2,1);


                        tafla.add(idegaLogo,1,2);
                        tafla.add(piLink,5,2);

                Text idegaText = new Text("idega margmiðlun | Bæjarlind 14 - 16 | sími: 554 - 7557 | fax: 554 - 7749 | email: ");
                  idegaText.setFontSize(1);
                  idegaText.setFontFace("Arial, Helvetica, sans-serif");
                Text mailText = new Text("idega@idega.is");
                  mailText.setFontSize(1);
                  mailText.setFontFace("Arial, Helvetica, sans-serif");
                Text idegaText2 = new Text(" | vefur: ");
                  idegaText2.setFontSize(1);
                  idegaText2.setFontFace("Arial, Helvetica, sans-serif");
                Text webText = new Text("www.idega.is");
                  webText.setFontSize(1);
                  webText.setFontFace("Arial, Helvetica, sans-serif");

                Link mailLink = new Link(mailText,"mailto: idega@idega.is");
                Link webLink = new Link(webText,"http://www.idega.is");

                frame.add(idegaText,1,4);
                frame.add(mailLink,1,4);
                frame.add(idegaText2,1,4);
                frame.add(webLink,1,4);

		outerTable.add(tafla,1,2);
		frame.add(header,1,2);
		frame.add(outerTable,1,3);

		return frame;

	}

	public void add(ModuleObject objectToAdd){
		tafla.add(objectToAdd,3,1);
	}

        public void add2(ModuleObject objectToAdd){
                    tafla.add(objectToAdd,1,1);
        }

        public void add3(ModuleObject objectToAdd){
                    tafla.add(objectToAdd,5,1);
        }

}
