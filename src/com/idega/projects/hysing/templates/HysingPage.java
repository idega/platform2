//idega 2001 - Tryggvi Larusson
/*
*Copyright 2001 idega.is All Rights Reserved.
*/
package com.idega.projects.hysing.templates;


import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.poll.moduleobject.*;
import com.idega.idegaweb.template.*;
import java.util.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.0
*/
public class HysingPage extends TemplatePage{


  protected String align;
  protected Table myTable;
  protected Table myTable2;
  protected Table contentTable;
  protected Table contentTable2;
  protected Table contentTable3;
  protected Table contentTable4;
  protected String headerImageURL;
  protected String footerImageURL;
  protected String middleImageURL;
  protected String middleEndImageURL;
  protected String mailImageURL;
  protected String helpImageURL;
  protected String serviceImageURL;
  protected String mailWindowColor="blatt";
  private String mainBoxHeader = "fréttir";
  private String leftBoxHeader = "utan úr heimi";
  private String rightBoxHeader = "tilkynningar";
  private boolean merged = false;

  protected HysingMenu clickablemenu;

  public HysingPage(){
	setLinkColor("000000");
	setVlinkColor("000000");
	setAlinkColor("000000");
        setMarginWidth(0);
        setMarginHeight (0);
        setLeftMargin(0);
        setTopMargin(0);
        clickablemenu = new HysingMenu();
        setClickedMenuItem("main");
  }


  public void main(ModuleInfo modinfo)throws Exception{
      if(headerImageURL==null){
        headerImageURL="/pics/headers/forsida/forsida_topp.jpg";
      }
      if(footerImageURL==null){
        footerImageURL="/pics/footers/BlarFooter.gif";
      }
      if(middleImageURL==null){
        middleImageURL="/pics/headers/forsida/2forsida1a.gif";
      }
      if(mailImageURL==null){
        mailImageURL="/pics/mail/blattMail_icon.gif";
      }
      if(serviceImageURL==null){
        serviceImageURL="/pics/nyicon/TjonustaBlatt.gif";
      }
      if(helpImageURL==null){
        helpImageURL="/pics/nyicon/FyrirspurnBlatt.gif";
      }
      if(middleEndImageURL==null){
        middleEndImageURL="/pics/menubar/menubar2blatt.gif";
      }

      super.add(template());
      super.add(template2());
  }

  public void setMerged(boolean isMerged){
    merged=isMerged;
  }

  public boolean getMerged(){
    return merged;
  }

  public void setMainBoxHeader(String text){
    mainBoxHeader=text;
  }

  public String getMainBoxHeader(){
    return mainBoxHeader;
  }

  public void setLeftBoxHeader(String text){
    leftBoxHeader=text;
  }

  public String getLeftBoxHeader(){
    return leftBoxHeader;
  }

  public void setRightBoxHeader(String text){
    rightBoxHeader=text;
  }

  public String getRightBoxHeader(){
    return rightBoxHeader;
  }

  public String getHeaderImageURL(){
    return headerImageURL;
  }

  public void setHeaderImageURL(String URL){
      headerImageURL=URL;
  }

  public String getFooterImageURL(){
    return footerImageURL;
  }

  public void setFooterImageURL(String URL){
     footerImageURL=URL;
  }

  public String getMiddleImageURL(){
    return middleImageURL;
  }

  public void setMiddleImageURL(String URL){
      middleImageURL=URL;
  }


  public String getMiddleEndImageURL(){
    return middleEndImageURL;
  }

  public void setMiddleEndImageURL(String URL){
      middleEndImageURL=URL;
  }

  public String getMailImageURL(){
    return mailImageURL;
  }

  public void setMailImageURL(String URL){
      mailImageURL=URL;
  }

  public String getHelpImageURL(){
    return helpImageURL;
  }

  public void setHelpImageURL(String URL){
      helpImageURL=URL;
  }

  public String getServiceImageURL(){
    return serviceImageURL;
  }

  public void setServiceImageURL(String URL){
      serviceImageURL=URL;
  }

  public void setMailWindowColor(String color){
    mailWindowColor=color;
  }

  public String getMailWindowColor(){
    return mailWindowColor;
  }


	public Table template(){

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
			myTable.setBackgroundImage(1,1,new Image(getHeaderImageURL()));

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
				//innerTable.setBackgroundImage(new Image("/pics/headers/forsida/2forsida1a.gif"));
                                innerTable.setBackgroundImage(new Image(getMiddleImageURL()));


				Window gluggi = new Window("Gluggi1",471,404,"/mail.jsp?color="+getMailWindowColor());
					gluggi.setScrollbar(false);

				Image mailImage = new Image(getMailImageURL(),"Fyrirspurn til Hýsingar");
					mailImage.setAttribute("hspace","9");
					mailImage.setAttribute("vspace","3");

				Image helpImage = new Image(getHelpImageURL(),"Hjálp");
					helpImage.setAttribute("hspace","9");
					helpImage.setAttribute("vspace","3");

				Image serviceImage = new Image(getServiceImageURL(),"Þjónustusíður");
					serviceImage.setAttribute("hspace","9");
					serviceImage.setAttribute("vspace","3");

				Link mail = new Link(mailImage,gluggi);
                                Link help = new Link(helpImage,"http://www.hysing.is/hjalp/");
                                Link service = new Link(serviceImage,"/service.jsp");

				innerTable.add(service,1,2);
				innerTable.add(help,1,2);
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

			Image menuStartImage = new Image("/pics/menubar/menubar1.gif");
                        Image menuEndImage = new Image(getMiddleEndImageURL());

                        /*Image menuImageBlue = new Image("/pics/menubar/menubar2blatt.gif");
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
                        Image bla = new Image(

			Image button5 = new Image("Associates","/pics/menubar/Takkar/samstarfs1.gif","/pics/menubar/Takkar/samstarfs2.gif");
			Image button5s = new Image("/pics/menubar/Takkar/samstarfs_valid.gif","Associates");


			Link mainpage = new Link(button1,"/index.jsp");
			Link about = new Link(button2,"/about.jsp");
			Link goods = new Link(button3,"/goods.jsp");
			Link staff = new Link(button4,"/staff.jsp");
			Link associates = new Link(button5,"/associates.jsp");



			menuTable.add(menuImage1,1,1);
			menuTable.add(menuItemBlueL,2,1);
			menuTable.add(button1s,3,1);
			menuTable.add(menuItemBlueR,4,1);
			menuTable.add(about,5,1);
			menuTable.add(menuItem1,6,1);
			menuTable.add(goods,7,1);
			menuTable.add(menuItem1,8,1);
			menuTable.add(staff,9,1);
			menuTable.add(menuItem1,10,1);
			menuTable.add(associates,11,1);
			menuTable.add(menuItem1,12,1);
			menuTable.add(menuImageBlue,13,1);
                        */


			menuTable.add(menuStartImage,1,1);
			//menuTable.add(menuItemBlueL,2,1);
                        menuTable.add(getLeftMenuItem("main"),2,1);
			menuTable.add(getMenuItem("main"),3,1);
                        menuTable.add(getRightMenuItem("main"),4,1);
			//menuTable.add(menuItemBlueR,4,1);
			menuTable.add(getMenuItem("about"),5,1);
                        menuTable.add(getRightMenuItem("about"),6,1);
			//menuTable.add(menuItem1,6,1);
			menuTable.add(getMenuItem("goods"),7,1);
                        menuTable.add(getRightMenuItem("goods"),8,1);
			//menuTable.add(menuItem1,8,1);
			menuTable.add(getMenuItem("staff"),9,1);
                        menuTable.add(getRightMenuItem("staff"),10,1);
			//menuTable.add(menuItem1,10,1);
			menuTable.add(getMenuItem("associates"),11,1);
                        menuTable.add(getRightMenuItem("associates"),12,1);
			//menuTable.add(menuItem1,12,1);
			//menuTable.add(menuImageBlue,13,1);
                        menuTable.add(menuEndImage,13,1);

		return menuTable;

	}


        public void setClickedMenuItem(String menuItemName){
          clickablemenu.setClickedMenuItem(menuItemName);
        }

        public ModuleObject getMenuItem(String menuItemName){
          return clickablemenu.getMenuItem(menuItemName);
        }

        public ModuleObject getLeftMenuItem(String menuItemName){
          return clickablemenu.getLeftMenuItem(menuItemName);
        }

        public ModuleObject getRightMenuItem(String menuItemName){
          return clickablemenu.getRightMenuItem(menuItemName);
        }

	public Table template2(){

		if (myTable2 == null){
			myTable2 = new Table(6,6);
			myTable2.setCellpadding(0);
			myTable2.setCellspacing(0);
			myTable2.setBorder(0);
			myTable2.setWidth(1,"6");
			//myTable2.setWidth(2,"170");
			myTable2.setWidth(3,"11");
			myTable2.setWidth(4,"415");
			myTable2.setWidth(5,"11");
			myTable2.setWidth(6,"170");
			myTable2.setHeight(1,"30");
			myTable2.setHeight(4,"50");
			myTable2.setHeight(6,"20");
			myTable2.setWidth("783");
			myTable2.setHeight("100%");
			myTable2.setHeight(5,"38");

			myTable2.setAlignment("center");

			myTable2.mergeCells(1,1,1,4);
			myTable2.mergeCells(2,4,6,4);
			myTable2.mergeCells(2,2,2,3);
			myTable2.mergeCells(4,2,4,3);
			myTable2.mergeCells(3,2,3,3);
			myTable2.mergeCells(5,2,5,3);
			myTable2.mergeCells(1,5,6,5);
			myTable2.mergeCells(1,6,6,6);

			myTable2.setColor(1,6,"FFFFFF");

			myTable2.setVerticalAlignment(2,2,"top");
			myTable2.setVerticalAlignment(4,2,"top");
			myTable2.setVerticalAlignment(6,2,"top");
			myTable2.setVerticalAlignment(6,3,"top");
                        myTable2.setVerticalAlignment(1,6,"bottom");
                        myTable2.setAlignment(1,6,"right");

			myTable2.setRowVerticalAlignment(4,"middle");
			myTable2.setRowAlignment(4,"center");

                        if ( merged ) {
                            myTable2 = new Table(2,6);
                            myTable2.setCellpadding(0);
                            myTable2.setCellspacing(0);
                            myTable2.setBorder(0);
                            myTable2.setWidth(1,"6");
                            myTable2.setHeight(1,"30");
                            myTable2.setHeight(4,"50");
                            myTable2.setHeight(6,"20");
                            myTable2.setWidth("783");
                            myTable2.setHeight("100%");
                            myTable2.setHeight(5,"38");

                            myTable2.setAlignment("center");

                            myTable2.mergeCells(1,1,1,4);

                            myTable2.mergeCells(1,5,2,5);
                            myTable2.mergeCells(1,6,2,6);

                            myTable2.setColor(1,6,"FFFFFF");

                            myTable2.setVerticalAlignment(2,2,"top");

                            myTable2.setRowVerticalAlignment(4,"middle");
                            myTable2.setRowAlignment(4,"center");
                        }

			myTable2.setBackgroundImage(new Image("/pics/BoxBackground/grunntiler2.gif"));
			myTable2.setBackgroundImage(1,1,new Image("/pics/BoxBackground/grunntiler1.gif"));
			myTable2.setBackgroundImage(1,5,new Image(footerImageURL));
			if ( !merged ) {
                          myTable2.setBackgroundImage(3,2,new Image("/pics/BoxBackground/divider.gif"));
                          myTable2.setBackgroundImage(5,2,new Image("/pics/BoxBackground/divider.gif"));
			}

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

			Text leftText = new Text("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+getLeftBoxHeader());
				leftText.setFontSize(1);
				leftText.setBold();

			Text middleText = new Text("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+getMainBoxHeader());
				middleText.setFontSize(1);
				middleText.setBold();

			Text rightText = new Text("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+getRightBoxHeader());
				rightText.setFontSize(1);
				rightText.setBold();

			try {
                          if ( merged ) {
                            myTable2.add(middleText,2,2);
                            myTable2.addBreak(2,2);
                            myTable2.add(content(),2,2);
                          }

                          else {
                            myTable2.add(leftText,2,2);
                            myTable2.addBreak(2,2);
                            myTable2.add(content2(),2,2);

                            myTable2.add(middleText,4,2);
                            myTable2.addBreak(4,2);
                            myTable2.add(content(),4,2);

                            myTable2.add(rightText,6,2);
                            myTable2.addBreak(6,2);
                            myTable2.add(content3(),6,2);
                          }
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
                        if ( merged ) {
                          contentTable.setWidth("757");
                        }
                        else {
			  contentTable.setWidth("395");
                        }
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

	public Table content3()throws Exception {

		if (contentTable3 == null){
			contentTable3 = new Table(3,3);
			contentTable3.setBorder(0);
			contentTable3.setWidth("150");
			//contentTable3.setHeight("100%");
			contentTable3.setCellpadding(0);
			contentTable3.setCellspacing(0);
			contentTable3.setColor(2,2,"FFFFFF");
			contentTable3.setAlignment("center");

			contentTable3.setWidth(1,"9");
			contentTable3.setWidth(3,"9");
			contentTable3.setHeight(3,"20");
			contentTable3.setHeight(1,"20");

			contentTable3.setVerticalAlignment(2,2,"top");

			contentTable3.setBackgroundImage(1,1,new Image("/pics/Box/boxHorntoppV.gif"));
			contentTable3.setBackgroundImage(2,1,new Image("/pics/Box/boxTopphlid.gif"));
			contentTable3.setBackgroundImage(3,1,new Image("/pics/Box/boxHorntoppH.gif"));

			contentTable3.setBackgroundImage(1,2,new Image("/pics/Box/boxVhlid.gif"));
			contentTable3.setBackgroundImage(3,2,new Image("/pics/Box/boxHhlid.gif"));

			contentTable3.setBackgroundImage(1,3,new Image("/pics/Box/boxHornbotnV.gif"));
			contentTable3.setBackgroundImage(2,3,new Image("/pics/Box/boxBotnhlid.gif"));
			contentTable3.setBackgroundImage(3,3,new Image("/pics/Box/boxHornbotnH.gif"));
		}

		return contentTable3;
	}

// ###########  Public - Föll
	public void add(ModuleObject objectToAdd){
		try{
			content().add(objectToAdd,2,2);
		}
		catch(Exception ex){
			System.err.println(ex.getMessage());
		}
	}

	public void add2(ModuleObject objectToAdd){
		try{
			content2().add(objectToAdd,2,2);
		}
		catch(Exception ex){
			System.err.println(ex.getMessage());
		}
	}

	public void add3(ModuleObject objectToAdd){
		try{
			content3().add(objectToAdd,2,2);
		}
		catch(Exception ex){
			System.err.println(ex.getMessage());
		}
	}


}  // class HysingPage
