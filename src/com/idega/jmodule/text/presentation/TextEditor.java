package com.idega.jmodule.text.presentation;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.text.data.*;
import com.idega.jmodule.image.presentation.ImageInserter;
import com.idega.util.text.*;
public class TextEditor extends JModuleObject{

private boolean isAdmin = false;
private boolean update = false;
private boolean save = false;
private Table outerTable;
private String color = "DDDDDD";
private String textColor = "000000";
private String headlineColor = "000000";
private String headlineBgColor = "FFFFFF";

public TextEditor(){
  outerTable = new Table(1,2);
  outerTable.setBorder(0);
  add(outerTable);

}

/**
 * @deprecated replaced with the default constructor
 */
public TextEditor(boolean isAdmin){
        this();
	this.isAdmin=isAdmin;
}

	public void main(ModuleInfo modinfo) throws Exception {

                isAdmin=com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo);

		if ( isAdmin ) {

			String mode = modinfo.getRequest().getParameter("mode");
			String action = modinfo.getRequest().getParameter("action");
				if ( action == null ) { action = "none"; }

			if ( mode == null ) {
				mode = "new";
				newText(modinfo);
			}

			else if ( mode.equals("update") ) {
				update = true;
				newText(modinfo);
			}

			else if ( mode.equals("Vista") ) {
				save = true;

				if ( action.equals("update") ) { update = true; }

				saveText(modinfo);
			}

			else if ( mode.equals("delete") ) {
				deleteText(modinfo);
			}

			if ( action.equals("delete") ) {
				confirmDelete(modinfo);
			}

		}

		else {
			noAccess();
		}

	}

	public void noAccess() throws IOException,SQLException {

		//outerTable = new Table(1,2);

		Text noDice = new Text("Þú hefur ekki réttindi til að skoða þessa síðu!");

		outerTable.add(noDice,1,1);
		outerTable.add(new BackButton("Til baka"),1,2);

	}

	public void newText(ModuleInfo modinfo) throws IOException,SQLException {

		int text_id = -1;

		if ( update ) {
			String text_id2 = modinfo.getRequest().getParameter("text_id");
				if ( text_id2 == null ) { text_id2 = "-1"; }

			text_id = Integer.parseInt(text_id2);
		}

		TextModule text = new TextModule();

		if ( text_id != -1 ) {
			text = new TextModule(text_id);
		}

		HttpSession Session = modinfo.getSession();

		//outerTable = new Table(1,2);
			outerTable.setCellpadding(2);
			outerTable.setCellspacing(2);
			outerTable.setColor(color);

		Form myForm = new Form();

		Table myTable = new Table(2,5);
			myTable.setBorder(0);
			myTable.setWidth("100%");
			myTable.setCellpadding(6);
			myTable.setCellspacing(6);
			myTable.mergeCells(1,2,1,5);
			myTable.setAlignment(2,5,"right");
			myTable.setVerticalAlignment(2,5,"bottom");

			TextInput text_headline = new TextInput("text_headline");
				text_headline.setLength(40);
				text_headline.setMaxlength(255);


			TextArea text_body = new TextArea("text_body",40,22);
				if ( update ) {

					if ( text.getTextHeadline() != null ) {
						text_headline.setContent(text.getTextHeadline());
					}
					if ( text.getTextBody() != null ) {
						text_body.setContent(text.getTextBody());
					}

					myForm.add(new HiddenInput("action","update"));
					myForm.add(new HiddenInput("text_id",String.valueOf(text_id)));
				}

			SubmitButton vista = new SubmitButton("mode","Vista");

			Text name_text = new Text("Titill:");
				name_text.setFontColor(textColor);
			Text body_text = new Text("Innihald:");
				body_text.setFontColor(textColor);

			myTable.add(name_text,1,1);
			myTable.addBreak(1,1);
			myTable.add(text_headline,1,1);

			myTable.add(body_text,1,2);
			myTable.addBreak(1,2);
			myTable.add(text_body,1,2);

			myTable.add(vista,2,5);

                        ImageInserter imageInsert = new ImageInserter();
                        if ( update ) {
                          imageInsert.setImageId(text.getImageId());
                        }

                        myTable.add(imageInsert,2,4);

		myForm.add(myTable);

		outerTable.setRowColor(1,headlineBgColor);

		Text headline_text = new Text("Textastjórinn");
			headline_text.setBold();
			headline_text.setFontSize(3);
			headline_text.setFontColor(headlineColor);

		outerTable.add(headline_text,1,1);
		outerTable.add(myForm,1,2);

	}

	public void saveText(ModuleInfo modinfo) throws IOException,SQLException {

		boolean check = true;

		HttpServletRequest request = modinfo.getRequest();
		HttpSession Session = modinfo.getSession();

			Session.removeAttribute("image_id");

		String text_id = modinfo.getRequest().getParameter("text_id");

		String text_headline = request.getParameter("text_headline");
			if ( text_headline == null ) { check = false; }

		String text_body = request.getParameter("text_body");
			if ( text_body == null ) { check = false; }

		String include_image = request.getParameter("insertImage");
			if ( include_image == null ) { include_image = "N"; }

		String image_id = request.getParameter("image_id");

		idegaTimestamp date = new idegaTimestamp();

		TextModule text = new TextModule();

		if ( update ) { text = new TextModule(Integer.parseInt(text_id)); }

		text.setTextHeadline( text_headline );
		text.setTextBody( text_body );
		text.setIncludeImage(include_image);
		text.setImageId( Integer.parseInt(image_id) );
		//eiki
                text.setTextDate( date.getTimestampRightNow());
                //text.setTextDate( date.RightNow().getSQLDate());

		if ( update ) { text.update(); }
		else { text.insert(); }

                //setParentToReload();
                //close();
                outerTable.addText("Textinn hefur verið vistaður",1,1);
                outerTable.add(new CloseButton("Loka"),1,2);
	}

	public void deleteText(ModuleInfo modinfo) throws IOException,SQLException {

		String text_id = modinfo.getRequest().getParameter("text_id");

		TextModule text = new TextModule(Integer.parseInt(text_id));

		//outerTable = new Table(1,2);
			outerTable.setCellpadding(3);
			outerTable.setCellspacing(3);

		outerTable.addText("Viltu örugglega eyða þessum texta? - ",1,1);

		Text text_headline = new Text("- "+text.getTextHeadline());
			text_headline.setFontSize(3);
			text_headline.setBold();

		outerTable.add(text_headline,1,1);

		Form myForm = new Form("/text/textadmin.jsp");
			myForm.add(new HiddenInput("text_id",text_id));
			myForm.add(new HiddenInput("action","delete"));
			myForm.add(new HiddenInput("mode","delete"));
			myForm.add(new SubmitButton("Eyða texta"));

		outerTable.add(myForm,1,2);

	}

	public void confirmDelete(ModuleInfo modinfo) throws IOException,SQLException {

		String text_id = modinfo.getRequest().getParameter("text_id");

		TextModule text = new TextModule(Integer.parseInt(text_id));

		text.delete();

                //setParentToReload();
                //close();
                outerTable.addText("Textanum hefur verið eytt",1,1);
                outerTable.add(new CloseButton("Loka"),1,2);
	}

	public void setColor(String color){
		this.color=color;
	}

	public void setTextColor(String color){
		this.textColor=color;
	}

	public void setHeadlineColor(String headlineColor){
		this.headlineColor=headlineColor;
	}

	public void setHeadlineBgColor(String headlineBgColor){
		this.headlineBgColor=headlineBgColor;
	}

	public void setAdmin(boolean isAdmin){
		this.isAdmin=isAdmin;
	}
}

