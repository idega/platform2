package com.idega.projects.timereg.moduleobject;

import java.io.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.Link;
public class TimeRegPage extends Page{

Table myTable;
Table myTableHeader;

private String location;


private Image image1;
private Image image2;
private Image image3;
private Image image4;
private Image image5;



private Link link1;
private Link link2;
private Link link3;
private Link link4;
private Link link5;

	public TimeRegPage(){
		this("");
	}

	public TimeRegPage(String title){
		super(title);


			myTable = new Table(1,2);
				myTable.setHeight(1,"68");
				myTable.setWidth("100%");
				myTable.setAlignment(1,1,"center");
				myTable.setAlignment(1,2,"center");
				myTable.setVerticalAlignment(1,2,"top");

//				myTable.setBackgroundImage(1,1,new Image("/pics/timereg/Nyr-HeaderVinnsla.gif"));

			myTableHeader = new Table(4,1);
				myTableHeader.setBorder(0);
				myTableHeader.setCellspacing(0);
				myTableHeader.setCellpadding(0);

				myTableHeader.setHeight("67");
				myTableHeader.setWidth("100%");
				myTableHeader.setWidth(1,"235");
				myTableHeader.setWidth(3,"133");
				myTableHeader.setWidth(4,"320");

                                Image logo = new Image("/pics/timereg/HeaderLogo.gif");
				myTableHeader.add(new Link(logo,"/index.jsp"),1,1 );
				myTableHeader.setBackgroundImage(2,1,new Image("/pics/timereg/HeaderVinstriTiler.gif"));
				myTableHeader.setBackgroundImage(3,1,new Image("/pics/timereg/HeaderSamskeyti.gif"));
				myTableHeader.setBackgroundImage(4,1,new Image("/pics/timereg/HeaderHaegriTiler.gif"));

				image1 = new Image("/pics/timereg/M-V-1.gif","Mín verk");
				image2 = new Image("/pics/timereg/VB-1.gif","Verkbók");
				image3 = new Image("/pics/timereg/Skyslur-1.gif","Skýrslur");
				image4 = new Image("/pics/timereg/Bokun-1.gif","Bókun");
				image5 = new Image("/pics/timereg/Dagat-1.gif","Dagatal");

				link1= new Link(image1);
				link2= new Link(image2);
				link3= new Link(image3);
				link4= new Link(image4);
				link5= new Link(image5);

				myTableHeader.add(link2,4,1);
					myTableHeader.addText(" &nbsp;&nbsp;",4,1);
				myTableHeader.add(link5,4,1);
					myTableHeader.addText(" &nbsp;&nbsp;",4,1);
				myTableHeader.add(link1 ,4,1);
					myTableHeader.addText(" &nbsp;&nbsp;",4,1);
				myTableHeader.add(link3 ,4,1);
					myTableHeader.addText(" &nbsp;&nbsp;",4,1);
				myTableHeader.add(link4,4,1);

		myTable.add(myTableHeader,1,1);

		super.add(myTable);
	}

	public void add(ModuleObject mo){
		myTable.add(mo,1,2);
	}

	public void print(ModuleInfo modinfo)throws Exception{
		super.print(modinfo);
	}

	public void setLocation(String loc) {

		if (loc==null) {
			loc = "";
		}

		if (loc.equals("Verkbok")) {
			image2.setSrc("/pics/timereg/VB-Ljos.gif");
		}
		else if (loc.equals("MinVerk")) {
			image1.setSrc("/pics/timereg/M-V-Ljos.gif");
		}
		else if (loc.equals("Skyrslur")) {
			image3.setSrc("/pics/timereg/Skyslur-Ljos.gif");
		}
		else if (loc.equals("Bokun")) {
			image4.setSrc("/pics/timereg/Bokun-Ljos.gif");
		}
		else if (loc.equals("Dagatal")) {
			image5.setSrc("/pics/timereg/Dagat-Ljos.gif");
		}


	}

	public void setLinkOfImage(String imageName,String theLink){
		if (imageName.equals("MinVerk")){
			link1.setURL(theLink);
		}
		else if (imageName.equals("Verkbok")){
			link2.setURL(theLink);
		}
		else if (imageName.equals("Skyrslur")){
			link3.setURL(theLink);
		}
		else if (imageName.equals("Bokun")){
			link4.setURL(theLink);
		}
		else if (imageName.equals("Dagatal")){
			link5.setURL(theLink);
		}
	}

}
