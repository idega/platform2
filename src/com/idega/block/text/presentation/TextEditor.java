package com.idega.block.text.presentation;

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
import com.idega.block.text.data.*;
import com.idega.jmodule.image.presentation.ImageInserter;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.util.text.*;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;

public class TextEditor extends JModuleObject{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.text";
private boolean isAdmin = false;
private boolean update = false;
private boolean save = false;
private Table outerTable;
private Form myForm;

private IWBundle iwb;
private IWResourceBundle iwrb;

public TextEditor(){
  myForm = new Form();
  outerTable = new Table(2,2);
  outerTable.setBorder(0);
  add(myForm);
}

	public void main(ModuleInfo modinfo) throws Exception {
    isAdmin = AccessControl.hasEditPermission(new TextReader(),modinfo);
    iwb = getBundle(modinfo);
    iwrb = getResourceBundle(modinfo);

		if ( isAdmin ) {
      this.getParentPage().setAllMargins(0);
      this.getParentPage().setTitle("Text Editor");

			String mode = modinfo.getParameter("mode");
			String action = modinfo.getParameter("action");
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
		outerTable.add(new CloseButton("Loka"),1,2);

	}

	public void newText(ModuleInfo modinfo) throws IOException,SQLException {

		int text_id = -1;

		if ( update ) {
			String text_id2 = modinfo.getParameter("text_id");
				if ( text_id2 == null ) { text_id2 = "-1"; }

			text_id = Integer.parseInt(text_id2);
		}

		TextModule text = new TextModule();

		if ( text_id != -1 ) {
			text = new TextModule(text_id);
		}

    outerTable.setWidth("100%");
    outerTable.setCellpadding(0);
    outerTable.setCellspacing(0);
    outerTable.mergeCells(1,1,2,1);
    outerTable.setColor(1,1,"#0E2456");
    outerTable.setColor(1,2,"#FFFFFF");
    outerTable.setColor(2,2,"#EFEFEF");
    outerTable.setHeight("100%");
    outerTable.setWidth(1,2,"392");
    outerTable.setWidth(2,2,"178");
    outerTable.setVerticalAlignment(1,2,"top");
    outerTable.setVerticalAlignment(2,2,"top");

		Table topTable = new Table(2,1);
      topTable.setCellpadding(0);
      topTable.setCellspacing(0);
      topTable.setWidth("100%");
      topTable.setAlignment(2,1,"right");

    Table myTable = new Table(1,2);
			myTable.setAlignment("center");
			myTable.setCellpadding(8);

    Table rightTable = new Table(1,2);
			rightTable.setAlignment("center");
			rightTable.setCellpadding(8);
      rightTable.setHeight("100%");
      rightTable.setHeight(1,"100%");
      rightTable.setAlignment(1,2,"center");
      rightTable.setVerticalAlignment(1,1,"top");

    Image idegaweb = iwb.getImage("shared/idegaweb.gif","idegaWeb",90,25);
      topTable.add(idegaweb,1,1);
    Text tEditor = new Text(iwrb.getLocalizedString("text_editor","Text Editor")+"&nbsp;&nbsp;");
      tEditor.setBold();
      tEditor.setFontColor("#FFFFFF");
      tEditor.setFontSize("3");
      topTable.add(tEditor,2,1);

    TextInput text_headline = new TextInput("text_headline");
      text_headline.setLength(40);
      text_headline.setMaxlength(255);
      text_headline.setAttribute("style","font-family:arial; font-size:8pt; color:#000000; text-align: justify; border: 1 solid #000000");


    TextArea text_body = new TextArea("text_body",65,22);
      text_body.setAttribute("style","font-family:arial; font-size:8pt; color:#000000; text-align: justify; border: 1 solid #000000");
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

    SubmitButton vista = new SubmitButton(iwrb.getImage("save.gif"));
    rightTable.add(vista,1,2);

    Text name_text = new Text(iwrb.getLocalizedString("title","Title"));
      name_text.setFontSize(Text.FONT_SIZE_7_HTML_1);
      name_text.setBold();
      name_text.setFontFace(Text.FONT_FACE_VERDANA);
    Text body_text = new Text(iwrb.getLocalizedString("body","Text"));
      body_text.setFontSize(Text.FONT_SIZE_7_HTML_1);
      body_text.setBold();
      body_text.setFontFace(Text.FONT_FACE_VERDANA);
    Text image_text = new Text(iwrb.getLocalizedString("image","Image"));
      image_text.setFontSize(Text.FONT_SIZE_7_HTML_1);
      image_text.setBold();
      image_text.setFontFace(Text.FONT_FACE_VERDANA);

    myTable.add(name_text,1,1);
    myTable.add(Text.getBreak(),1,1);
    myTable.add(text_headline,1,1);

    myTable.add(body_text,1,2);
    myTable.add(Text.getBreak(),1,2);
    myTable.add(text_body,1,2);

    ImageInserter imageInsert = new ImageInserter();
      imageInsert.setSelected(false);
      if ( update ) {
        if ( text.getIncludeImage().equalsIgnoreCase("y") ) {
          imageInsert.setImageId(text.getImageId());
          imageInsert.setSelected(true);
        }
      }
			rightTable.add(image_text,1,1);
			rightTable.add(Text.getBreak(),1,1);
      rightTable.add(imageInsert,1,1);

		outerTable.add(topTable,1,1);
		outerTable.add(myTable,1,2);
		outerTable.add(rightTable,2,2);

    myForm.add(outerTable);
	}

	public void saveText(ModuleInfo modinfo) throws IOException,SQLException {

		boolean check = true;

		modinfo.getSession().removeAttribute("image_id");

		String text_id = modinfo.getParameter("text_id");

		String text_headline = modinfo.getParameter("text_headline");
			if ( text_headline == null ) { check = false; }

		String text_body = modinfo.getParameter("text_body");
			if ( text_body == null ) { check = false; }

		String include_image = modinfo.getParameter("insertImage");
			if ( include_image == null ) { include_image = "N"; }

		String image_id = modinfo.getParameter("image_id");

		idegaTimestamp date = new idegaTimestamp();

		TextModule text = new TextModule();

		if ( update ) { text = new TextModule(Integer.parseInt(text_id)); }

		text.setTextHeadline( text_headline );
		text.setTextBody( text_body );
		text.setIncludeImage(include_image);
		text.setImageId( Integer.parseInt(image_id) );
    text.setTextDate( date.getTimestampRightNow());

		if ( update ) {
      text.update();
    }
		else {
      text.insert();
    }

    this.getParentPage().setParentToReload();
    this.getParentPage().close();
	}

	public void deleteText(ModuleInfo modinfo) throws IOException,SQLException {

		String text_id = modinfo.getParameter("text_id");

		TextModule text = new TextModule(Integer.parseInt(text_id));

		outerTable = new Table(1,2);
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
		String text_id = modinfo.getParameter("text_id");

		TextModule text = new TextModule(Integer.parseInt(text_id));
		text.delete();

    this.getParentPage().setParentToReload();
    this.getParentPage().close();
	}

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
}

