// idega 2000 - laddi
package com.idega.jmodule.object.interfaceobject;

import com.idega.jmodule.*;
import com.idega.data.*;
import java.io.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import javax.servlet.http.*;
import java.util.*;

public class ShadowBox extends JModuleObject {

private String boxWidth = "100";
private String boxHeight = "100";
private String horizontalAlignment = "left";
private String verticalAlignment = "top";
private Table contentTable;
private Table myTable;

public ShadowBox(){
	myTable = new Table(3,3);
	contentTable = new Table(1,1);
	super.add(contentTable);

}

	public void main(ModuleInfo modinfo) throws IOException {

		drawTables();
	}

	private void drawTables() throws IOException {

		contentTable.setCellpadding(0);
		contentTable.setCellspacing(0);
		contentTable.setColor("#FFFFFF");
		contentTable.setWidth(boxWidth);
		contentTable.setHeight(boxHeight);

		myTable.setBorder(0);
		myTable.setWidth("100%");
		myTable.setHeight("100%");
		myTable.setCellpadding(0);
		myTable.setCellspacing(0);
		myTable.setColor(2,2,"FFFFFF");
		myTable.setAlignment("center");
		myTable.setWidth(2,2,"100%");
		myTable.setHeight(3,"9");
		myTable.setHeight(1,"9");

		myTable.setAlignment(2,2,horizontalAlignment);
		myTable.setVerticalAlignment(2,2,verticalAlignment);

		myTable.add(new Image("/pics/jmodules/shadowBox/boxHorntoppV.gif"),1,1);
		myTable.setBackgroundImage(2,1,new Image("/pics/jmodules/shadowBox/boxTopphlid.gif"));
		myTable.addText("",2,1);
		myTable.add(new Image("/pics/jmodules/shadowBox/boxHorntoppH.gif"),3,1);

		myTable.setBackgroundImage(1,2,new Image("/pics/jmodules/shadowBox/boxVhlid.gif"));
		myTable.setBackgroundImage(3,2,new Image("/pics/jmodules/shadowBox/boxHhlid.gif"));

		myTable.add(new Image("/pics/jmodules/shadowBox/boxHornbotnV.gif"),1,3);
		myTable.setBackgroundImage(2,3,new Image("/pics/jmodules/shadowBox/boxBotnhlid.gif"));
		myTable.addText("",2,3);
		myTable.add(new Image("/pics/jmodules/shadowBox/boxHornbotnH.gif"),3,3);

		contentTable.add(myTable,1,1);

	}

	public void add(ModuleObject objectToAdd){
		myTable.add(objectToAdd,2,2);
	}

	public void setWidth(String boxWidth){
		this.boxWidth=boxWidth;
	}

	public void setHeight(String boxHeight){
		this.boxHeight=boxHeight;
	}

	public void setAlignment(String horizontalAlignment){
		this.horizontalAlignment=horizontalAlignment;
	}

	public void setVerticalAlignment(String verticalAlignment){
		this.verticalAlignment=verticalAlignment;
	}

}
