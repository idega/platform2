package com.idega.projects.iceconsult.moduleobject;

import java.io.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;

/**
*@author <a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
*@version 1.0
*/
public class IceConsultPage extends Page{


public com.idega.presentation.Image maini = new com.idega.presentation.Image("/pics/iceconsult/Main.gif");
public com.idega.presentation.Image productsi = new com.idega.presentation.Image("/pics/iceconsult/Products.gif");
public com.idega.presentation.Image partnersi = new com.idega.presentation.Image("/pics/iceconsult/Partners.gif");
public com.idega.presentation.Image companyi = new com.idega.presentation.Image("/pics/iceconsult/Company.gif");
public com.idega.presentation.Image newsi = new com.idega.presentation.Image("/pics/iceconsult/News.gif");
public com.idega.presentation.Image contacti = new com.idega.presentation.Image("/pics/iceconsult/Contact-us.gif");
public com.idega.presentation.Image customeri = new com.idega.presentation.Image("/pics/iceconsult/Customer-showcase.gif");

//public com.idega.presentation.Image countryi = new com.idega.presentation.Image("/pics/iceconsult/english.gif","English");

public Link lCountry;


public Table table;
public Table extra;
public Table left = new Table(1,1);
public Table right = new Table(1,1);

private com.idega.presentation.Image leftImage = null;
private com.idega.presentation.Image rightImage = null;
private Text leftText = null;
private Text rightText = null;

public String language = "EN";
private int width_leftside_percent = 50;
private int width_leftside = 0;
private int width_rightside = 0;
private int space = 22;
public int width = 798;


	public IceConsultPage(){
		this("");
                initialize();
	}

	public IceConsultPage(String title){
          super(title);
          extra = new Table(3,2);
          extra.add(left,1,2);
          initialize();
        }

        public void initialize() {
          this.setAlinkColor("#232323");
          this.setVlinkColor("#454545");
          this.setLinkColor("#676767");
        }

        public void calc() {
          width_leftside = ( width_leftside_percent * width) / 100;
          width_rightside = ( (100 - width_leftside_percent) * width) / 100;
        }

        public void main(IWContext iwc) throws Exception{

          double left_side = ( width_leftside_percent * width) / 100;
          double right_side = ( (100 - width_leftside_percent) * width) / 100;

          width_leftside = (new Double(left_side)).intValue();
          width_rightside = (new Double(right_side)).intValue();

          table = new Table();
            table.setAlignment("center");
            table.setCellpadding(0);
            table.setCellspacing(0);
            table.setBorder(0);
            table.setWidth(798);
            table.mergeCells(1,1,1,2);


            com.idega.presentation.Image spacer = new com.idega.presentation.Image("/pics/iceconsult/spacer.gif");

            com.idega.presentation.Image logo = new com.idega.presentation.Image("/pics/iceconsult/iceconlogo.jpg");
              logo.setWidth(259);
              logo.setHeight(62);
            com.idega.presentation.Image himinn = new com.idega.presentation.Image("/pics/iceconsult/himinn-.jpg");
              himinn.setWidth(541);
              himinn.setHeight(44);
            com.idega.presentation.Image bar1 = new com.idega.presentation.Image("/pics/iceconsult/barlina1.gif");
            com.idega.presentation.Image lina = new com.idega.presentation.Image("/pics/iceconsult/millilinahvit.gif");
            com.idega.presentation.Image bar2 = new com.idega.presentation.Image("/pics/iceconsult/barlina2.gif");
            com.idega.presentation.Image bar3 = new com.idega.presentation.Image("/pics/iceconsult/barlina3.gif");
            com.idega.presentation.Image bar4 = new com.idega.presentation.Image("/pics/iceconsult/barlina4.gif");
            com.idega.presentation.Image bar5 = new com.idega.presentation.Image("/pics/iceconsult/barlina5.gif");
            com.idega.presentation.Image bar6 = new com.idega.presentation.Image("/pics/iceconsult/barlina6.gif");
            com.idega.presentation.Image millibar = new com.idega.presentation.Image("/pics/iceconsult/millibar.gif");
            com.idega.presentation.Image midjulinur_tiler = new com.idega.presentation.Image("/pics/iceconsult/midjubarlinur_tiler.gif");
            com.idega.presentation.Image midjulinur = new com.idega.presentation.Image("/pics/iceconsult/midjubarlinur1.gif");
            com.idega.presentation.Image endalina = new com.idega.presentation.Image("/pics/iceconsult/endalina.gif");


            Link main = new Link(maini,"/index.jsp");
            Link products = new Link(productsi,"/products.jsp?text_id=29");
            Link partners = new Link(partnersi,"/partners.jsp?text_id=30");
            Link company = new Link(companyi,"/company.jsp?text_id=31");
            Link news = new Link(newsi,"/news.jsp");
            Link contact = new Link(contacti,"/contact.jsp?text_id=32");
            Link customer = new Link(customeri,"/customer.jsp?text_id=33");


            com.idega.presentation.Image bogi1 = new com.idega.presentation.Image("/pics/iceconsult/bogi1.gif");
            com.idega.presentation.Image bogiv = new com.idega.presentation.Image("/pics/iceconsult/barvinstri.gif");
            com.idega.presentation.Image bogih = new com.idega.presentation.Image("/pics/iceconsult/barhaegri.gif");
            com.idega.presentation.Image ekkertbil = new com.idega.presentation.Image("/pics/iceconsult/ekkertbil_24.gif");


            table.setBackgroundImage(1,1, logo);
            table.setHeight(1,1,"63");
            table.setWidth(1,1,"257");

            table.setBackgroundImage(2,1,himinn);
            table.setHeight(2,1,"43");
            table.setWidth(2,1,"541");

            table.setBackgroundImage(2,2,bar1);

            table.setVerticalAlignment(2,2,"top");

            maini.setAttribute("align","top") ;
            productsi.setAttribute("align","top") ;
            partnersi.setAttribute("align","top") ;
            companyi.setAttribute("align","top") ;
            newsi.setAttribute("align","top") ;
            maini.setAttribute("align","top") ;
            contacti.setAttribute("align","top") ;
            customeri.setAttribute("align","top") ;

            table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);
            table.add(main,2,2);
            table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);
            table.add(products,2,2);
            table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);
            table.add(partners,2,2);
            table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);
            table.add(company,2,2);
            table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);table.add(spacer,2,2);
            table.add(news,2,2);



            table.setBackgroundImage(1,3,bar2);
            table.mergeCells(1,3,2,3);

            Table tempTable = new Table(2,1);
              tempTable.setHeight(18);
              tempTable.setCellpadding(0);
              tempTable.setCellspacing(0);
              tempTable.setBorder(0);
              tempTable.setWidth("100%");


            table.setHeight(1,3,"18");
            tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);
            tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);
            tempTable.add(contact,1,1);
            tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);
            tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);tempTable.add(bar2,1,1);
            tempTable.add(customer,1,1);
            tempTable.add(bogi1,1,1);
            tempTable.setBackgroundImage(1,1,bar3);
            tempTable.setBackgroundImage(2,1,bar3);

            if (this.lCountry != null) {
              tempTable.add(ekkertbil,2,1);
              tempTable.add(lCountry,2,1);
              tempTable.setAlignment(2,1,"right");
            }

            table.add(tempTable,1,3);


            table.setBackgroundImage(1,4,bar4);
            table.mergeCells(1,4,2,4);
            table.setHeight(1,4,"20");


            table.mergeCells(1,5,4,5);
              table.setRowColor(5,"#FFFFFF");

              table.add(extra,1,5);
              extra.setWidth("100%");
              extra.setCellpadding(0);
              extra.setCellspacing(0);
              extra.setBorder(0);
//              extra.setHeight();
              extra.setBackgroundImage(1,1,bar5);
              extra.setAlignment(1,1,"right");
              extra.setVerticalAlignment(1,2,"top");
              extra.add(bogiv,1,1);
              if (leftImage != null) {
                leftImage.setAttribute("align","top");
                extra.add(leftImage,1,1);
              }
              if (leftText != null) {
                extra.add(leftText,1,1);
              }
              extra.setVerticalAlignment(1,1,"top");


              if (width_leftside_percent != 100) {
                extra.setBackgroundImage(2,1,midjulinur_tiler);
                extra.add(millibar,2,1);
                extra.add(midjulinur,2,2);
                extra.setVerticalAlignment(2,2,"top");
                extra.setWidth(2,space+"");
                extra.setBackgroundImage(3,1,bar5);
                extra.setVerticalAlignment(2,1,"top");
                extra.setVerticalAlignment(3,1,"top");
                if (rightImage != null) {
                  rightImage.setAttribute("align","top");
                  extra.add(rightImage,3,1);
                }
                if (rightText != null) {
                  extra.add(rightText,3,1);
                }
                extra.add(bogih,3,1);
                extra.setVerticalAlignment(3,2,"top");
               extra.add(right,3,2);

              }
              else {
                extra.mergeCells(2,1,3,1);
                extra.setWidth(1,1,"398");
                extra.setWidth(2,1,Integer.toString(width - 398));
                extra.setBackgroundImage(2,1,bar6);
                extra.mergeCells(1,2,3,2);
              }

//            left.setBorder(1);


              left.setCellpadding(0);
              left.setCellspacing(0);
//             right.setBorder(1);
              right.setCellpadding(0);
              right.setCellspacing(0);

              Image topLine = new Image("/pics/iceconsult/800x1top.gif");

              Table everything = new Table(3,2);
                everything.setBorder(0);
                everything.mergeCells(1,1,3,1);
                everything.add(topLine,1,1);
                everything.setAlignment(1,1,"center");
                everything.setVerticalAlignment(1,1,"bottom");
                everything.setAlignment("center");
                everything.setHeight(1,"10");
                everything.setVerticalAlignment(1,2,"top");
                everything.setVerticalAlignment(2,2,"top");
                everything.setVerticalAlignment(3,2,"top");
                everything.setCellpadding(0);
                everything.setCellspacing(0);
                everything.setAlignment(1,2,"right");
                everything.add(endalina,1,2);
                everything.add(table,2,2);
                everything.add(endalina,3,2);

              super.add(everything);
	}

        public void setWidthLeftsidePercent(int width_leftside_percent){
         this.width_leftside_percent = width_leftside_percent;
        }

        public void setSpace(int space) {
            this.space = space;
       }

        public void setLeftImage(com.idega.presentation.Image mynd) {
        	leftImage = mynd;
        }

        public void setRightImage(com.idega.presentation.Image mynd) {
        	rightImage = mynd;
        }

        public void setLeftText(Text text) {
        	leftText = text;
        }

        public void setRightText(Text text) {
        	rightText = text;
        }

        public void setCountryLink(Link link) {
            this.lCountry = link;
        }


	public void add(PresentationObject objectToAdd){
//		table.add(Text.getBreak(),2,1);
	  left.add(objectToAdd,1,1);
	}

        public void addLeft(PresentationObject objectToAdd){
	  left.add(objectToAdd,1,1);
	}

        public void addLeft(String string){
	  left.add(string,1,1);
	}

        public void addRight(PresentationObject objectToAdd){
	  right.add(objectToAdd,1,1);
	}

        public void addRight2(PresentationObject objectToAdd){
	  extra.add(objectToAdd,4,2);
	}

        public void addRight(String string){
	  right.add(string,1,1);
	}

	public void setLocation(String loc) {
                calc();

		if (loc==null) {
			loc = "";
		}

		if (loc.equals("Main")) {
                        left.setWidth(width_leftside+30);
                        right.setWidth(width_rightside-space-32);
			maini.setSrc("/pics/iceconsult/Main-over.gif");
		}
		else if (loc.equals("Products")) {
                        left.setWidth(width_leftside+30);
                        right.setWidth(width_rightside-space-32);
			productsi.setSrc("/pics/iceconsult/Products-over.gif");
		}
		else if (loc.equals("Partners")) {
                        left.setWidth(width_leftside);
			partnersi.setSrc("/pics/iceconsult/Partners-over.gif");
		}
		else if (loc.equals("Company")) {
                        left.setWidth(width_leftside);
                        right.setWidth(width_rightside-space-50);
			companyi.setSrc("/pics/iceconsult/Company-over.gif");
		}
		else if (loc.equals("News")) {
                        left.setWidth(width_leftside);
			newsi.setSrc("/pics/iceconsult/News-over.gif");
		}
		else if (loc.equals("Contact")) {
                        left.setWidth(width_leftside);
			contacti.setSrc("/pics/iceconsult/Contact-us-over.gif");
		}
		else if (loc.equals("Customer")) {
                        left.setWidth(width_leftside+30);
                        right.setWidth(width_rightside-space-32);
			customeri.setSrc("/pics/iceconsult/Customer-showcase-over.gif");
		}
                else if (loc.equals("Other_1")) {
                        left.setWidth(width_leftside);
                }


	}
}