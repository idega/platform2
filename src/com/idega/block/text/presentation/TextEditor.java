package com.idega.block.text.presentation;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.block.text.data.TextModule;
import com.idega.block.text.business.TextBusiness;
import com.idega.jmodule.image.presentation.ImageInserter;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.util.text.*;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;

public class TextEditor extends IWAdminWindow{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.text";
private boolean isAdmin = false;
private boolean update = false;
private boolean save = false;

private IWBundle iwb;
private IWResourceBundle iwrb;

public TextEditor(){
  setWidth(570);
  setHeight(430);
  setUnMerged();
}

  public void main(IWContext iwc) throws Exception{

    isAdmin = iwc.hasEditPermission(new TextReader());


    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);

		if ( isAdmin ) {
      addTitle(iwrb.getLocalizedString("text_editor","Text Editor"));

			String mode = iwc.getParameter("mode");
			String action = iwc.getParameter("action");
				if ( action == null ) { action = "none"; }

			if ( mode == null ) {
				mode = "new";
        try {
  				newText(iwc);
        }
        catch (Exception e) {
          e.printStackTrace(System.err);
        }
			}
			else if ( mode.equals("update") ) {
				update = true;
        try {
  				newText(iwc);
        }
        catch (Exception e) {
          e.printStackTrace(System.err);
        }
			}
			else if ( mode.equals("save") ) {
				save = true;
				if ( action.equals("update") ) {
          update = true;
        }
        try {
          saveText(iwc);
        }
        catch (Exception e) {
          e.printStackTrace(System.err);
        }
			}
			else if ( mode.equals("delete") ) {
        try {
  				confirmDelete(iwc);
        }
        catch (Exception e) {
          e.printStackTrace(System.err);
        }
			}

			if ( action.equals("delete") ) {
        try {
  				deleteText(iwc);
        }
        catch (Exception e) {
          e.printStackTrace(System.err);
        }
			}
		}
		else {
      try {
  			noAccess();
      }
      catch (Exception e) {
        e.printStackTrace(System.err);
      }
		}
	}

	public void noAccess() throws IOException,SQLException {
		addLeft(iwrb.getLocalizedString("no_access","Login first!"));
		this.addSubmitButton(new CloseButton("Loka"));
	}

	public void newText(IWContext iwc) throws IOException,SQLException {
		TextModule text = TextBusiness.getTextModule(iwc);

    TextInput text_headline = new TextInput("text_headline");
      text_headline.setLength(40);
      text_headline.setMaxlength(255);

    TextArea text_body = new TextArea("text_body",65,22);
      if ( update ) {
        if ( text.getTextHeadline() != null ) {
          text_headline.setContent(text.getTextHeadline());
        }
        if ( text.getTextBody() != null ) {
          text_body.setContent(text.getTextBody());
        }
        addHiddenInput(new HiddenInput("action","update"));
        addHiddenInput(new HiddenInput("text_id",Integer.toString(text.getID())));
      }

    SubmitButton vista = new SubmitButton(iwrb.getImage("save.gif"));

    ImageInserter imageInsert = new ImageInserter();
      imageInsert.setSelected(false);
      if ( update ) {
        if ( text.getIncludeImage().equalsIgnoreCase("y") ) {
          imageInsert.setImageId(text.getImageId());
          imageInsert.setSelected(true);
        }
      }

    addLeft(iwrb.getLocalizedString("title","Title"),text_headline,true);
    addLeft(iwrb.getLocalizedString("body","Text"),text_body,true);
    addRight(iwrb.getLocalizedString("image","Image"),imageInsert,true);
    addSubmitButton(vista);
    addHiddenInput(new HiddenInput("mode","save"));
	}

	public void confirmDelete(IWContext iwc) throws IOException,SQLException {
		TextModule text = TextBusiness.getTextModule(iwc);
		String textHeadline = text.getTextHeadline();

    if ( textHeadline != null ) {
      addLeft(iwrb.getLocalizedString("text_to_delete","Text to delete")+": "+textHeadline);
      addLeft(iwrb.getLocalizedString("confirm_delete","Are you sure?"));
      addHiddenInput(new HiddenInput("text_id",Integer.toString(text.getID())));
      addHiddenInput(new HiddenInput("action","delete"));
      addHiddenInput(new HiddenInput("mode","delete"));
      addSubmitButton(new SubmitButton(iwrb.getImage("delete.gif")));
    }
    else {
      addLeft(iwrb.getLocalizedString("not_exists","Text already deleted or not available."));
      addSubmitButton(new CloseButton(iwrb.getImage("close.gif")));
    }
	}

	public void saveText(IWContext iwc) throws IOException,SQLException {
    TextBusiness.saveText(iwc,update);
    setParentToReload();
    close();
	}

	public void deleteText(IWContext iwc) throws IOException,SQLException {
    TextBusiness.deleteText(iwc);
    setParentToReload();
    close();
	}

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
}
