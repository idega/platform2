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

	public void main(ModuleInfo modinfo) {
    try {
      isAdmin = AccessControl.hasEditPermission(new TextReader(),modinfo);
    }
    catch (SQLException e) {
      isAdmin = false;
    }

    iwb = getBundle(modinfo);
    iwrb = getResourceBundle(modinfo);

		if ( isAdmin ) {
      addTitle(iwrb.getLocalizedString("text_editor","Text Editor"));

			String mode = modinfo.getParameter("mode");
			String action = modinfo.getParameter("action");
				if ( action == null ) { action = "none"; }

			if ( mode == null ) {
				mode = "new";
        try {
  				newText(modinfo);
        }
        catch (Exception e) {
          e.printStackTrace(System.err);
        }
			}
			else if ( mode.equals("update") ) {
				update = true;
        try {
  				newText(modinfo);
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
          saveText(modinfo);
        }
        catch (Exception e) {
          e.printStackTrace(System.err);
        }
			}
			else if ( mode.equals("delete") ) {
        try {
  				confirmDelete(modinfo);
        }
        catch (Exception e) {
          e.printStackTrace(System.err);
        }
			}

			if ( action.equals("delete") ) {
        try {
  				deleteText(modinfo);
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

	public void newText(ModuleInfo modinfo) throws IOException,SQLException {
		TextModule text = TextBusiness.getTextModule(modinfo);

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

	public void confirmDelete(ModuleInfo modinfo) throws IOException,SQLException {
		TextModule text = TextBusiness.getTextModule(modinfo);
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

	public void saveText(ModuleInfo modinfo) throws IOException,SQLException {
    TextBusiness.saveText(modinfo,update);
    setParentToReload();
    close();
	}

	public void deleteText(ModuleInfo modinfo) throws IOException,SQLException {
    TextBusiness.deleteText(modinfo);
    setParentToReload();
    close();
	}

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
}