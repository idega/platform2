package is.idega.idegaweb.golf.block.text.presentation;

import is.idega.idegaweb.golf.block.text.data.TextModuleBMPBean;
import is.idega.idegaweb.golf.presentation.GolfImage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.Vector;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Anchor;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Window;
import com.idega.util.text.TextSoap;


public class TextReader extends Block{

private String text_id = null;
private boolean isAdmin=false;
private TextModuleBMPBean text;
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

public TextReader(){
}

public TextReader(String text_id){
	this.text_id=text_id;
  //eiki
  try{
    text = new TextModuleBMPBean(Integer.parseInt(text_id));
  }
  catch(SQLException e){

  }
}

public TextReader(int text_id){
  this.text_id = String.valueOf(text_id);
  //eiki
  try{
    text = new TextModuleBMPBean(text_id);
  }
  catch(SQLException e){

  }

}
/**
 * @deprecated replaced with the default constructor
 */
public TextReader(boolean isAdmin){
	this.isAdmin=isAdmin;
}

/**
 * @deprecated replaced with the String text_id constructor
 */
public TextReader(String text_id, boolean isAdmin){
	this.text_id=text_id;
	this.isAdmin=isAdmin;
}

public void main(IWContext modinfo) throws Exception {

  String text_id2 = modinfo.getRequest().getParameter("text_id");
        if( text_id2 != null ) text_id = text_id2;

        isAdmin=isAdministrator(modinfo);


		if ( isAdmin ) {

			add(adminForm());
		}

		if ( this.text_id == null ) {

			noTextID();
			add(myTable);
		}

		else {
//EIKI þetta mætti allt hreinsa til og gera þennan hlut í constructornum
			text = new TextModuleBMPBean(Integer.parseInt(text_id));

			textTable();
			add(myTable);
		}
	}

	public Form adminForm() throws IOException {

		Window adminWindow = new Window("AdminWindow",550,550,adminURL);

                Form myForm = new Form(adminWindow);

			myForm.add(new SubmitButton(new Image("/pics/Textastjori.gif")));

		return myForm;
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
                        headline.setMarkupAttribute("class","headlinetext");
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
                        body.setMarkupAttribute("class","bodytext");
                        body.setFontStyle(textStyle);

		Image body_image;

		if ( text.getIncludeImage().equals("Y") ) {

			body_image = new GolfImage(text.getImageId());
			body_image.setMarkupAttribute("align","right");
			body_image.setMarkupAttribute("vspace","6");
			body_image.setMarkupAttribute("hspace","6");

			if ( displayHeadline ) {
				myTable.add(body_image,1,2);
			}
			else {
				myTable.add(body_image,1,1);
			}
		}

		if ( displayHeadline ) {
                //added haffi
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

			Window adminWindow = new Window("AdminWindow",550,550,adminURL);

                        Form breyta = new Form(adminWindow);
				breyta.add(new HiddenInput("mode","update"));
				breyta.add(new HiddenInput("text_id",text_id));
				breyta.add(new SubmitButton(new Image("/pics/breyta_medium.gif")));

			Form delete = new Form(adminWindow);
				delete.add(new HiddenInput("mode","delete"));
				delete.add(new HiddenInput("text_id",text_id));
				delete.add(new SubmitButton(new Image("/pics/eyda_medium.gif")));

			myTable.resize(2,3);
			myTable.setAlignment(2,3,"right");
			myTable.add(breyta,1,3);
			if ( enableDelete ) {
				myTable.add(delete,2,3);
			}

		}
	}

	public void noTextID() throws IOException,SQLException {

		TextModuleBMPBean[] texts = (TextModuleBMPBean[]) (new TextModuleBMPBean()).findAll();

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

			link = "<a href=\""+link.substring(comma+1,link.length())+"\">"+link.substring(0,comma)+"</a>";

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

}
