// idega - Gimmi & Eiki

package is.idega.idegaweb.golf.templates.page;





import is.idega.idegaweb.golf.moduleobject.MenuBar;
import java.io.*;

import javax.servlet.*;

import javax.servlet.http.*;

import javax.servlet.jsp.*;

import com.idega.presentation.ui.*;

import com.idega.jmodule.*;

import com.idega.presentation.*;

import com.idega.presentation.text.*;

import com.idega.idegaweb.IWBundle;

import com.idega.idegaweb.IWResourceBundle;





public class JmoduleWindowModuleWindow extends Window{



private Table tafla;

//8ab490

public String header_color ="#F2BC00";

public String color = "#F2BCFF";



private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";

protected IWResourceBundle iwrb;

protected IWBundle iwb;



private MenuBar Menu;

public String MenuAlignment = "&nbsp;&nbsp;&nbsp;&nbsp;";



public boolean initialized = false;







    public JmoduleWindowModuleWindow(){

      super();

      this.setMarginHeight(0);

      this.setMarginWidth(0);

      this.setLeftMargin(0);

      this.setTopMargin(0);

      this.setAlinkColor("black");

      this.setVlinkColor("black");

      this.setLinkColor("black");

      initialized = false;

      tafla = new Table(3,2);



    }





    private void initTable(IWContext iwc) {

	  tafla.setWidth("100%");

	  tafla.setHeight("100%");

	  tafla.setHeight(1,1,"58");

	  tafla.setBorder(0);

	  tafla.setCellpadding(0);

	  tafla.setCellspacing(0);

	  tafla.mergeCells(1,2,3,2);

          tafla.setVerticalAlignment(1,2,"top");

          Image topLeft = new Image("/pics/jmodules/images/golf/idegaweb_standard.gif");

            topLeft.setHeight(58);

            topLeft.setWidth(158);

          Image topTiler = new Image("/pics/jmodules/images/golf/idegawebTiler.gif");

          Image topRight = new Image("/pics/jmodules/images/golf/idegaweb_Golf.gif");

            topRight.setHeight(58);

            topRight.setWidth(154);

            topRight.setAttribute("align","right");

          Image back = new Image("/pics/jmodules/images/golf/idegaweb_Background.gif");



	  tafla.add(topLeft,1,1);

	  tafla.setBackgroundImage(1,1,topTiler);

	  tafla.setBackgroundImage(2,1,topTiler);

	  tafla.setBackgroundImage(3,1,topTiler);

	  tafla.add(topRight,3,1);



          tafla.setBackgroundImage(1,2,back);

          super.add(tafla);

          initialized = true;

	}





	public void add(PresentationObject objectToAdd){

          tafla.add(objectToAdd,1,2);

	}





        private void MenuBar(){



          Menu.setPosition(0,39);

          Menu.setSizes(1,1,0);

          Menu.setColors("#444444", "#FFFFFF", "#BDBDBD" , "#444444" , "#F2BC00", "#444444" , "#BDBDBD" , "#F2BC00", "#444444");

          Menu.setFonts("Arial", "Helvetica" , "sans-serif", "normal", "normal", 8,"Arial", "Helvetica", "sans-serif", "normal", "normal", 8);

          Menu.scaleNavBar();



          Menu.addMenu("file", 80, 120);

          Menu.addMenu("addons", 80, 120);

          Menu.addMenu("tools", 80, 120);

          Menu.addMenu("options", 80, 120);

          Menu.addMenu("help", 80, 120);



          Menu.addItem("file", MenuAlignment+"File");

          Menu.addItem("addons", MenuAlignment+"Add-ons");

          Menu.addItem("tools", MenuAlignment+"Tools");

          Menu.addItem("options", MenuAlignment+"Options");

          Menu.addItem("help", MenuAlignment+"Help");



          this.addToOptionsMenu("Themes", "");

          this.addToOptionsMenu("Language","");

          this.addToHelpMenu("Help", "");



        }





        public void addToFileMenu(String ItemName, String Url){

          Menu.addItem("file", MenuAlignment+ItemName, Url);

        }





        public void addToAddOnsMenu(String ItemName, String Url){

          Menu.addItem("addons", MenuAlignment+ItemName, Url);

        }



        public void addToToolsMenu(String ItemName, String Url){

          Menu.addItem("tools", MenuAlignment+ItemName, Url);

        }



        public void addToOptionsMenu(String ItemName, String Url){

          Menu.addItem("options", MenuAlignment+ItemName, Url);

        }



        public void addToHelpMenu(String ItemName, String Url){

          Menu.addItem("help", MenuAlignment+ItemName, Url);

        }





        public MenuBar getMenu(){

          return Menu;

        }





        public String getBundleIdentifier(){

          return IW_BUNDLE_IDENTIFIER;

        }





        public void main(IWContext iwc) throws Exception {



          iwrb = getResourceBundle(iwc);

          iwb = getBundle(iwc);

          if(!initialized ){

            initTable(iwc);

          }



        }

}

