package com.idega.block.text.presentation;

import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.block.text.data.*;
import com.idega.data.*;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.util.text.*;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;


public class TextReader extends JModuleObject{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.text";
private String text_id = null;
private boolean isAdmin=false;
private TextModule text;
private Table myTable = new Table(2,2);
private String adminURL = "/text/textadmin.jsp";

private String textBgColor = "#FFFFFF";
private String textColor = "#000000";
private String headlineBgColor = "#FFFFFF";
private String headlineColor = "#000000";
private int textSize = 2;
private int tableTextSize = 1;
private int headlineSize = 3;
private String tableWidth = "";
private boolean displayHeadline=true;
private boolean enableDelete=true;
private String textWidth = "100%";
private String textStyle = "";
private String headlineStyle = "";
private boolean reverse = false;
private boolean crazy = false;
private IWBundle iwb;
private IWResourceBundle iwrb;

public TextReader(){
}

public TextReader(String text_id){
	this.text_id=text_id;
  //eiki
  try{
    text = new TextModule(Integer.parseInt(text_id));
  }
  catch(SQLException e){

  }
}

public TextReader(int text_id){
  this.text_id = String.valueOf(text_id);
  //eiki
  try{
    text = new TextModule(text_id);
  }
  catch(SQLException e){

  }

}

public void main(ModuleInfo modinfo) throws Exception {
  isAdmin = AccessControl.hasEditPermission(this,modinfo);
  iwb = getBundle(modinfo);
  iwrb = getResourceBundle(modinfo);

  String text_id2 = modinfo.getParameter("text_id");
        if( text_id2 != null ) text_id = text_id2;

		if ( this.text_id == null ) {
			noTextID();
			add(myTable);
		}
		else {
//EIKI þetta mætti allt hreinsa til og gera þennan hlut í constructornum
			text = new TextModule(Integer.parseInt(text_id));
			textTable();
			add(myTable);
		}
	}

	public void textTable() throws IOException,SQLException {
			myTable.setCellpadding(3);
			myTable.setCellspacing(3);
			myTable.mergeCells(1,1,2,1);
			myTable.mergeCells(1,2,2,2);
			myTable.setRowColor(1,headlineBgColor);
			myTable.setRowColor(2,textBgColor);
      myTable.setWidth(textWidth);

		Text headline = new Text(text.getTextHeadline());
			headline.setFontSize(headlineSize);
			headline.setFontColor(headlineColor);
			headline.setBold();
      headline.setAttribute("class","headlinetext");
      headline.setFontStyle(headlineStyle);

		String text_body = text.getTextBody();

    if ( reverse ) {
      text_body = textReverse(text_body);
    }
    if ( crazy ) {
      text_body = textCrazy(text_body);
    }

    text_body = formatText(text_body);

		Text body = new Text(text_body);
			body.setFontSize(textSize);
			body.setFontColor(textColor);
      body.setAttribute("class","bodytext");
      body.setFontStyle(textStyle);

		Image body_image;

		if ( text.getIncludeImage().equals("Y") ) {
			body_image = new Image(text.getImageId());
			body_image.setAttribute("align","right");
			body_image.setAttribute("vspace","6");
			body_image.setAttribute("hspace","6");

			if ( displayHeadline ) {
				myTable.add(body_image,1,2);
			}
			else {
				myTable.add(body_image,1,1);
			}
		}

		if ( displayHeadline ) {
      if ( headline.getText() != null ) {
        Anchor headlineAnchor = new Anchor(headline,headline.getText());
        headlineAnchor.setFontColor(headlineColor);
        myTable.add(headlineAnchor ,1,1);
        myTable.add(body,1,2);
      }
		}
		else {
			myTable.mergeCells(1,1,1,2);
			myTable.add(body,1,1);
		}

		if ( isAdmin ) {

			Window adminWindow = new Window("AdminWindow",TextEditor.class,com.idega.jmodule.object.Page.class);
        adminWindow.setWidth(570);
        adminWindow.setHeight(430);

      //Link breyta = new Link(iwrb.getImage("change.gif"),adminWindow);
      Link breyta = new Link(iwrb.getImage("change.gif"));
        breyta.setWindowToOpen(TextEditor.class);
				breyta.addParameter("mode","update");
				breyta.addParameter("text_id",text_id);

      //Link delete = new Link(iwrb.getImage("delete.gif"),adminWindow);
      Link delete = new Link(iwrb.getImage("delete.gif"));
        delete.setWindowToOpen(TextEditor.class);
				delete.addParameter("mode","delete");
				delete.addParameter("text_id",text_id);

			myTable.resize(2,3);
			myTable.setAlignment(2,3,"right");
			myTable.add(breyta,1,3);
			if ( enableDelete ) {
				myTable.add(delete,2,3);
			}

		}
	}

	public void noTextID() throws IOException,SQLException {

		TextModule[] texts = (TextModule[]) (new TextModule()).findAll();

		myTable = new Table(2,texts.length+1);
			myTable.mergeCells(1,1,2,1);

//		Text text_heading = new Text("Texts in this database:");
                // Breytt af gimmi 13.03.2001
		Text text_heading = new Text("");
			text_heading.setFontSize(3);
			text_heading.setBold();

		for ( int a = 0; a < texts.length; a++ ) {

			Link textLink = new Link(texts[a].getTextHeadline(),"");
				textLink.addParameter("text_id",texts[a].getID());

			myTable.addText(texts[a].getID()+".",1,a+2);
			myTable.add(textLink,2,a+2);
		}

		if ( texts.length == 0 ) {
			myTable.resize(2,2);
			myTable.addText("",2,2);
		}

		myTable.add(text_heading,1,1);

	}

	public String formatText(String text_body) {

		//Búa til töflu
		if (text_body==null || text_body.equals("")) text_body = "";

		Vector tableVector = createTextTable(text_body);

		for ( int a = 0; a < tableVector.size(); a++ ) {

			String tableRow = tableVector.elementAt(a).toString();

			if ( a == 0 ) {
				tableRow = TextSoap.findAndReplace(tableRow,"|","</font></td><td valign=\"top\"><font size=\""+(tableTextSize+1)+"\">");
			}

			else {
				tableRow = TextSoap.findAndReplace(tableRow,"|","</font></td><td valign=\"top\"><font size=\""+tableTextSize+"\">");
			}

			if ( a == 0 || a == tableVector.size()-1) {
				if ( a == 0 ) {
					tableRow = "<table border=\"0\" cellpadding=\"3\" cellspacing=\"0\" width=\""+tableWidth+"\"><tr bgcolor=\"#FFFFFF\"><td valign=\"top\"><font size=\""+(tableTextSize+1)+"\">"+tableRow+"</font></td></tr>";
				}

				if ( a == tableVector.size()-1 ) {
				tableRow = "<tr bgcolor=\"#FFFFFF\"><td valign=\"top\"><font size=\""+tableTextSize+"\">"+tableRow+"</font></td></tr></table>";
				}
			}
			else {
				tableRow = "<tr bgcolor=\"#FFFFFF\"><td valign=\"top\"><font size=\""+tableTextSize+"\">"+tableRow+"</font></td></tr>";
			}

			text_body = TextSoap.findAndReplace(text_body,tableVector.elementAt(a).toString(),tableRow);
		}

		text_body = TextSoap.findAndReplace(text_body,"|\r\n","");
		text_body = TextSoap.findAndReplace(text_body,"|","");
		//Töflugerð lokið


		//Búa til töflu 2
		if (text_body==null || text_body.equals("")) text_body = "";
		tableVector = createTextTableNoBanner(text_body);

		for ( int a = 0; a < tableVector.size(); a++ ) {

			String tableRow = tableVector.elementAt(a).toString();

			if ( a == 0 ) {
				tableRow = TextSoap.findAndReplace(tableRow,"?","</font></td><td><font size=\""+(tableTextSize+1)+"\">");
			}

			else {
				tableRow = TextSoap.findAndReplace(tableRow,"?","</font></td><td><font size=\""+tableTextSize+"\">");
			}

			if ( a == 0 || a == tableVector.size()-1) {
				if ( a == 0 ) {
					tableRow = "<table border=\"0\" cellpadding=\"3\" cellspacing=\"0\" width=\""+tableWidth+"\"><tr bgcolor=\"#FFFFFF\"><td><font size=\""+(tableTextSize+1)+"\">"+tableRow+"</font></td></tr>";
				}

				if ( a == tableVector.size()-1 ) {
				tableRow = "<tr bgcolor=\"#FFFFFF\"><td><font size=\""+tableTextSize+"\">"+tableRow+"</font></td></tr></table>";
				}
			}
			else {
				tableRow = "<tr bgcolor=\"#FFFFFF\"><td><font size=\""+tableTextSize+"\">"+tableRow+"</font></td></tr>";
			}

			text_body = TextSoap.findAndReplace(text_body,tableVector.elementAt(a).toString(),tableRow);
		}

		text_body = TextSoap.findAndReplace(text_body,"?\r\n","");
		text_body = TextSoap.findAndReplace(text_body,"?","");
		//Töflugerð lokið


		//Búa til tengla
		Vector linkVector = createTextLink(text_body);

		for ( int a = 0; a < linkVector.size(); a++ ) {
			String link = linkVector.elementAt(a).toString();
				int comma = link.indexOf(",");

			link = "<a href=\""+link.substring(comma+1,link.length())+"\" target=\"_blank\">"+link.substring(0,comma)+"</a>";

			text_body = TextSoap.findAndReplace(text_body,"Link("+linkVector.elementAt(a).toString()+")",link);
		}

		//Almenn hreinsun
		text_body = TextSoap.findAndReplace(text_body,"*","<li>");
		text_body = TextSoap.findAndReplace(text_body,".\r\n",".<br><br>");
		text_body = TextSoap.findAndReplace(text_body,"?\r\n","?<br><br>");
		text_body = TextSoap.findAndReplace(text_body,"!\r\n","!<br><br>");

		//Búa til headline
		/*Vector testVector = testText(text_body);

		while ( text_body.indexOf("\r\n\r\n\r\n") != -1 ) {
			text_body = TextSoap.findAndReplace(text_body,"\r\n\r\n\r\n","\r\n\r\n");
		}

		int head_size = textSize + 1;

		for ( int a = 0; a < testVector.size(); a++ ) {
			text_body = TextSoap.findAndReplace(text_body,"\r\n\r\n"+testVector.elementAt(a).toString(),"temp");
			text_body = TextSoap.findAndReplace(text_body,"temp","<font size=\""+head_size+"\"><b>"+testVector.elementAt(a).toString()+"</b></font>");
		}*/
		//Headlinegerð búin

		text_body = TextSoap.findAndReplace(text_body,"\r\n","<br>");
		text_body = TextSoap.findAndReplace(text_body,"\t","&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

		return text_body;
	}

	public Vector testText(String text_body) {

		Vector testVector = TextSoap.FindAllBetween(text_body,"\r\n\r\n","\r\n");

		return testVector;
	}

	public Vector createTextTable(String text_body) {

		Vector tableVector = TextSoap.FindAllBetween(text_body,"|","|\r\n");

		return tableVector;
	}

	public Vector createTextTableNoBanner(String text_body) {

		Vector tableVector = TextSoap.FindAllBetween(text_body,"?","?\r\n");

		return tableVector;
	}

	public Vector createTextLink(String text_body) {

		Vector linkVector = TextSoap.FindAllBetween(text_body,"Link(",")");

		return linkVector;
	}

        public String textReverse(String strengur) {

          StringBuffer buffer = new StringBuffer(strengur);

          String reverse = buffer.reverse().toString();

          return reverse;

        }

        public String textCrazy(String strengur) {

          String crazy = "";

          StringTokenizer token = new StringTokenizer(strengur);

          while ( token.hasMoreTokens() ) {
            StringBuffer buffer = new StringBuffer(token.nextToken());
            crazy += buffer.reverse().toString();
            if ( token.hasMoreTokens() ) {
              crazy += " ";
            }
          }

          return crazy;

        }

	public void setAdmin(boolean isAdmin){
		this.isAdmin=isAdmin;
	}

	public void setTextBgColor(String textBgColor) {
		this.textBgColor=textBgColor;
	}

	public void setTextColor(String textColor) {
		this.textColor=textColor;
	}

	public void setHeadlineBgColor(String headlineBgColor) {
		this.headlineBgColor=headlineBgColor;
	}

	public void setHeadlineColor(String headlineColor) {
		this.headlineColor=headlineColor;
	}

	public void setTextSize(int textSize) {
		this.textSize=textSize;
	}

	public void setTableTextSize(int tableTextSize) {
		this.tableTextSize=tableTextSize;
	}

	public void setTableWidth(String tableWidth) {
		this.tableWidth=tableWidth;
	}

	public void setHeadlineSize(int headlineSize) {
		this.headlineSize=headlineSize;
	}

        public void setTextStyle(String textStyle) {
                this.textStyle=textStyle;
        }

        public void setHeadlineStyle(String headlineStyle) {
                this.headlineStyle=headlineStyle;
        }

	public void displayHeadline(boolean displayHeadline) {
		this.displayHeadline=displayHeadline;
	}

	public void setEnableDelete(boolean enableDelete) {
		this.enableDelete=enableDelete;
	}

  /**
   * Sets alignment for the table around the text - added by gimmi@idega.is
   */
  public void setAlignment(String alignment) {
          this.myTable.setAlignment(alignment);
  }

  public void setWidth(String textWidth) {
          this.textWidth=textWidth;
  }

  public void setReverse() {
          this.reverse=true;
  }

  public void setCrazy() {
          this.crazy=true;
  }

  public String getAnchorName(){
    return text.getTextHeadline();
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }


}
