//idega 2001 - Tryggvi Larusson
/*
*Copyright 2001 idega.is All Rights Reserved.
*/
package com.idega.projects.hysing.templates;


import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.idegaweb.template.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.0
*/
public class HysingSubPage extends HysingPage{


	/*private Table myTable;
	private Table myTable2;
	private Table contentTable;
	private Table contentTable2;*/
        private String mainBoxHeader;

        public HysingSubPage(){
              super();
        }



	/*public Table template(){

		if (myTable == null){
			myTable = new Table(1,2);
			myTable.setCellpadding(0);
			myTable.setCellspacing(0);
			myTable.setBorder(0);
			myTable.setAlignment("center");

			myTable.setHeight(1,"102");
			myTable.setHeight(2,"109");
			myTable.setRowVerticalAlignment(2,"top");
			myTable.setRowAlignment(2,"left");
			//myTable.setBackgroundImage(1,1,new Image("/pics/headers/vorur&thjonusta/V&Th_Topp.jpg"));

			myTable.setWidth("783");

			Table innerTable = new Table(1,2);
				innerTable.setHeight("100%");
				innerTable.setWidth("100%");
				innerTable.setCellpadding(0);
				innerTable.setCellspacing(0);
				innerTable.setBorder(0);
				innerTable.setAlignment(1,2,"right");
				innerTable.setVerticalAlignment(1,1,"top");
				innerTable.setVerticalAlignment(1,2,"bottom");
				innerTable.setBackgroundImage(new Image("/pics/headers/vorur&thjonusta/2v&th1a.gif"));

				Window gluggi = new Window("Gluggi1",471,404,"/mail.jsp?color=blatt");
					gluggi.setScrollbar(false);

				Image mailImage = new Image("/pics/mail/blattMail_icon.gif","Fyrirspurn til Hýsingar");
					mailImage.setAttribute("hspace","9");

				Link mail = new Link(mailImage,gluggi);

				innerTable.add(mail,1,2);

				innerTable.add(menubar(),1,1);

			myTable.add(innerTable,1,2);

		}

		return myTable;
	}

	public Table menubar() {

		Table menuTable = new Table(13,1);

			menuTable.setCellpadding(0);
			menuTable.setCellspacing(0);

			Image menuImage1 = new Image("/pics/menubar/menubar1.gif");
			Image menuImageBlue = new Image("/pics/menubar/menubar2blatt.gif");
			Image menuImageBrown = new Image("/pics/menubar/menubar2brunt.gif");

			Image menuItem1 = new Image("/pics/menubar/blattTakkabil.gif");
			Image menuItemBlueL = new Image("/pics/menubar/forsida_Vbil.gif");
			Image menuItemBlueR = new Image("/pics/menubar/forsida_Hbil.gif");
			Image menuItemBrownL = new Image("/pics/menubar/umFyrirtaekid_Vbil.gif");
			Image menuItemBrownR = new Image("/pics/menubar/umFyrirtaekid_Hbil.gif");

			Image button1 = new Image("Main page","/pics/menubar/Takkar/forsida1.gif","/pics/menubar/Takkar/forsida2.gif");
			Image button1s = new Image("/pics/menubar/Takkar/forsida_valid.gif","Main page");

			Image button2 = new Image("About","/pics/menubar/Takkar/umFyrir1.gif","/pics/menubar/Takkar/umFyrir2.gif");
			Image button2s = new Image("/pics/menubar/Takkar/umFyrir_valid.gif","About");

			Image button3 = new Image("Goods & Services","/pics/menubar/Takkar/V&Th1.gif","/pics/menubar/Takkar/V&Th2.gif");
			Image button3s = new Image("/pics/menubar/Takkar/V&Th_valid.gif","Goods & Services");

			Image button4 = new Image("Staff","/pics/menubar/Takkar/starfsm1.gif","/pics/menubar/Takkar/starfsm2.gif");
			Image button4s = new Image("/pics/menubar/Takkar/starfsm_valid.gif","Staff");

			Image button5 = new Image("Associates","/pics/menubar/Takkar/samstarfs1.gif","/pics/menubar/Takkar/samstarfs2.gif");
			Image button5s = new Image("/pics/menubar/Takkar/samstarfs_valid.gif","Associates");


			Link mainpage = new Link(button1,"/index.jsp");
			Link about = new Link(button2,"/about.jsp");
			Link goods = new Link(button3,"/goods.jsp");
			Link staff = new Link(button4,"/staff.jsp");
			Link associates = new Link(button5,"/associates.jsp");

			menuTable.add(menuImage1,1,1);
			menuTable.add(menuItem1,2,1);
			menuTable.add(mainpage,3,1);
			menuTable.add(menuItem1,4,1);
			menuTable.add(about,5,1);
			menuTable.add(menuItemBlueL,6,1);
			menuTable.add(button3s,7,1);
			menuTable.add(menuItemBlueR,8,1);
			menuTable.add(staff,9,1);
			menuTable.add(menuItem1,10,1);
			menuTable.add(associates,11,1);
			menuTable.add(menuItem1,12,1);
			menuTable.add(menuImageBlue,13,1);


		return menuTable;

	}*/

	public Table template2(){

		if (myTable2 == null){
			myTable2 = new Table(4,6);
			myTable2.setCellpadding(0);
			myTable2.setCellspacing(0);
			myTable2.setBorder(0);
			myTable2.setWidth(1,"6");
			//myTable2.setWidth(2,"170");
			myTable2.setWidth(3,"11");
			myTable2.setWidth(4,"596");
			myTable2.setHeight(1,"30");
			myTable2.setHeight(4,"50");
			myTable2.setHeight(6,"20");
			myTable2.setWidth("783");
			myTable2.setHeight("100%");
			myTable2.setHeight(5,"38");

			myTable2.setAlignment("center");

			myTable2.mergeCells(1,1,1,4);
			myTable2.mergeCells(2,4,4,4);

			myTable2.mergeCells(3,2,3,3);

			myTable2.mergeCells(4,2,4,3);

			myTable2.mergeCells(1,5,4,5);
			myTable2.mergeCells(1,6,4,6);

			myTable2.setColor(1,6,"FFFFFF");

			myTable2.setVerticalAlignment(2,2,"top");
			myTable2.setVerticalAlignment(4,2,"top");

			myTable2.setRowVerticalAlignment(4,"middle");
			myTable2.setRowAlignment(4,"center");

			myTable2.setBackgroundImage(new Image("/pics/BoxBackground/grunntiler2.gif"));
			myTable2.setBackgroundImage(1,1,new Image("/pics/BoxBackground/grunntiler1.gif"));
			myTable2.setBackgroundImage(1,5,new Image("/pics/footers/BlarFooter.gif"));
			myTable2.setBackgroundImage(3,2,new Image("/pics/BoxBackground/divider.gif"));

			Link mainpage = new Link("forsíða","/index.jsp");
				mainpage.setFontSize(1);
			Link associates = new Link("samstarfsaðilar","associates.jsp");
				associates.setFontSize(1);
			Link staff = new Link("starfsmenn","staff.jsp");
				staff.setFontSize(1);
			Link about = new Link("um&nbsp;fyrirtækið","about.jsp");
				about.setFontSize(1);
			Link goods = new Link("vörur&nbsp;&&nbsp;þjónusta","goods.jsp");
				goods.setFontSize(1);

			Text spacer = new Text("&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;");

			myTable2.add(mainpage,2,4);
			myTable2.add(spacer,2,4);
			myTable2.add(about,2,4);
			myTable2.add(spacer,2,4);
			myTable2.add(goods,2,4);
			myTable2.add(spacer,2,4);
			myTable2.add(staff,2,4);
			myTable2.add(spacer,2,4);
			myTable2.add(associates,2,4);

			Text loginText = new Text("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+getLeftBoxHeader());
				loginText.setFontSize(1);
				loginText.setBold();

			//Text textText = new Text("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;vörur&nbsp;&&nbsp;þjónusta");
                        //Text textText = new Text(getMainBoxHeader());
                        Text textText = new Text("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+getMainBoxHeader());



                        	textText.setFontSize(1);
				textText.setBold();

			try {
				myTable2.add(loginText,2,2);
				myTable2.addBreak(2,2);
				myTable2.add(content2(),2,2);

				myTable2.add(textText,4,2);
				myTable2.addBreak(4,2);
				myTable2.add(content(),4,2);

			}
			catch (Exception io) {
				System.err.println(io.getMessage());
			}

		}

		return myTable2;
	}

	public Table content()throws Exception {

		if (contentTable == null){

			contentTable = new Table(3,3);
			contentTable.setBorder(0);
			contentTable.setWidth("576");
			contentTable.setHeight("100%");
			contentTable.setCellpadding(0);
			contentTable.setCellspacing(0);
			contentTable.setColor(2,2,"FFFFFF");
			contentTable.setAlignment("center");

			contentTable.setWidth(1,"9");
			contentTable.setWidth(3,"9");
			contentTable.setHeight(3,"20");
			contentTable.setHeight(1,"20");

			contentTable.setVerticalAlignment(2,2,"top");

			contentTable.setBackgroundImage(1,1,new Image("/pics/Box/boxHorntoppV.gif"));
			contentTable.setBackgroundImage(2,1,new Image("/pics/Box/boxTopphlid.gif"));
			contentTable.setBackgroundImage(3,1,new Image("/pics/Box/boxHorntoppH.gif"));

			contentTable.setBackgroundImage(1,2,new Image("/pics/Box/boxVhlid.gif"));
			contentTable.setBackgroundImage(3,2,new Image("/pics/Box/boxHhlid.gif"));

			contentTable.setBackgroundImage(1,3,new Image("/pics/Box/boxHornbotnV.gif"));
			contentTable.setBackgroundImage(2,3,new Image("/pics/Box/boxBotnhlid.gif"));
			contentTable.setBackgroundImage(3,3,new Image("/pics/Box/boxHornbotnH.gif"));

		}

		return contentTable;
	}
/*
	public Table content2()throws Exception {

		if (contentTable2 == null){
			contentTable2 = new Table(3,3);
			contentTable2.setBorder(0);
			contentTable2.setWidth("150");
			//contentTable2.setHeight("100%");
			contentTable2.setCellpadding(0);
			contentTable2.setCellspacing(0);
			contentTable2.setColor(2,2,"FFFFFF");
			contentTable2.setAlignment("center");

			contentTable2.setWidth(1,"9");
			contentTable2.setWidth(3,"9");
			contentTable2.setHeight(3,"20");
			contentTable2.setHeight(1,"20");

			contentTable2.setVerticalAlignment(2,2,"top");

			contentTable2.setBackgroundImage(1,1,new Image("/pics/Box/boxHorntoppV.gif"));
			contentTable2.setBackgroundImage(2,1,new Image("/pics/Box/boxTopphlid.gif"));
			contentTable2.setBackgroundImage(3,1,new Image("/pics/Box/boxHorntoppH.gif"));

			contentTable2.setBackgroundImage(1,2,new Image("/pics/Box/boxVhlid.gif"));
			contentTable2.setBackgroundImage(3,2,new Image("/pics/Box/boxHhlid.gif"));

			contentTable2.setBackgroundImage(1,3,new Image("/pics/Box/boxHornbotnV.gif"));
			contentTable2.setBackgroundImage(2,3,new Image("/pics/Box/boxBotnhlid.gif"));
			contentTable2.setBackgroundImage(3,3,new Image("/pics/Box/boxHornbotnH.gif"));
		}

		return contentTable2;
	}


*/
}  // class HysingTemplate
