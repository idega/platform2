/*
 * $Id: IShopCreatePageWindow.java,v 1.7 2004/05/24 14:56:57 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.intershop.presentation;

import is.idega.idegaweb.intershop.business.IShopExportBusiness;
import is.idega.idegaweb.intershop.business.IShopTemplateHome;
import is.idega.idegaweb.intershop.data.IShopTemplate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.idega.builder.business.IBPageHelper;
import com.idega.builder.business.PageTreeNode;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.FileUtil;
import com.strengur.idegaweb.intershop.business.IShopXMLDesc;

/**
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
*/
public class IShopCreatePageWindow extends IWAdminWindow {
  private static final String IS_NAME = "is_name"; //size = 255
  private static final String IS_DESCRIPTION = "is_desc"; //size = 255
  private static final String IS_CLASS = "is_class"; //size = 2
  private static final String IS_LANGNR = "is_langnr"; //int (= 2)
  private static final String IS_ID = "is_id"; //size = 30
  private static final String IW_BUNDLE_IDENTIFIER  = "is.idega.idegaweb.intershop";

  public IShopCreatePageWindow() {
    setWidth(280);
    setHeight(175);
    setScrollbar(false);
  }

  public void main(IWContext iwc) throws Exception {
    IWBundle iwb = getBundle(iwc);
    IWResourceBundle iwrb = iwb.getResourceBundle(iwc);
    IShopXMLDesc desc = new IShopXMLDesc(iwc.getApplicationContext());

    String ut = iwb.getProperty("UT","UT");

    Form form = new Form();

    setTitle(iwrb.getLocalizedString("create_new_ispage","Create Intershop Template"));
    addTitle(iwrb.getLocalizedString("create_new_ispage","Create Intershop Template"),IWConstants.BUILDER_FONT_STYLE_TITLE);

    add(form);
    Table tab = new Table(2,6);
    tab.setColumnAlignment(1,"right");
    tab.setWidth(1,"110");
    tab.setCellspacing(3);
    tab.setAlignment(2,6,"right");
    form.add(tab);

    TextInput id = new TextInput(IS_ID);
    id.setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE);
    id.setMaxlength(30);
    Text idText = new Text(iwrb.getLocalizedString(IS_ID,"ID")+":");
    idText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

    tab.add(idText,1,1);
    tab.add(id,2,1);

    DropdownMenu mnu = new DropdownMenu(IS_CLASS);
    java.util.Enumeration e  = desc.getAvailableTemplateClasses();
    while (e.hasMoreElements()) {
      String className = (String)e.nextElement();
      if (desc.hasCreatePermissions(className))
        mnu.addMenuElement(className,className);
    }

    mnu.setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE);
    mnu.setToSubmit();

    Text classText = new Text(iwrb.getLocalizedString(IS_CLASS,"Class")+":");
    classText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
    tab.add(classText,1,2);
    tab.add(mnu,2,2);

    mnu.setToSubmit();

    TextInput langnr = new TextInput(IS_LANGNR);
    langnr.setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE);
    langnr.setAsIntegers(iwrb.getLocalizedString("is_only_integer","Only integers are allowed in langnr"));
    Text langnrText = new Text(iwrb.getLocalizedString(IS_LANGNR,"Langnr")+":");
    langnrText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

    tab.add(langnrText,1,3);
    tab.add(langnr,2,3);

    TextInput name = new TextInput(IS_NAME);
    name.setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE);
    name.setMaxlength(255);
    Text nameText = new Text(iwrb.getLocalizedString(IS_NAME,"Name")+":");
    nameText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

    tab.add(nameText,1,4);
    tab.add(name,2,4);

    TextInput description = new TextInput(IS_DESCRIPTION);
    description.setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE);
    description.setMaxlength(255);
    Text descriptionText = new Text(iwrb.getLocalizedString(IS_DESCRIPTION,"Description")+":");
    descriptionText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

    tab.add(descriptionText,1,5);
    tab.add(description,2,5);

    SubmitButton button = new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),"submit");
    SubmitButton close = new SubmitButton(iwrb.getLocalizedImageButton("close","Close"),"close");
    tab.add(close,2,6);
    tab.add(Text.getNonBrakingSpace(),2,6);
    tab.add(button,2,6);

    boolean submit = iwc.isParameterSet("submit");
    boolean quit = iwc.isParameterSet("close");

    if (submit) {
      String id_ = iwc.getParameter(IS_ID);
      String className_ = iwc.getParameter(IS_CLASS);
      String langnr_ = iwc.getParameter(IS_LANGNR);
      String name_ = iwc.getParameter(IS_NAME);
      String description_ = iwc.getParameter(IS_DESCRIPTION);

      String parentId = iwb.getProperty(className_ + "_FOLDER");
      if (parentId != null) {
	      Map tree = PageTreeNode.getTree(iwc);
        int pageId = IBPageHelper.getInstance().createNewPage(parentId,name_,com.idega.builder.data.IBPageBMPBean.PAGE,"",tree,iwc,IShopTemplate.SUBTYPE_NAME);

        IShopTemplate temp = IShopTemplateHome.getInstance().getNewElement();
        temp.setIShopClass(className_);
        temp.setIShopDescription(description_);
        temp.setIShopID(id_);
        temp.setIShopLanguageNr(Integer.parseInt(langnr_));
        temp.setIShopName(name_);
        temp.setPageID(pageId);
        IShopTemplateHome.getInstance().insert(temp);

      	iwc.setSessionAttribute("ib_page_id",Integer.toString(pageId));
        setOnUnLoad("window.opener.parent.parent.location.reload()");

        StringBuffer path = new StringBuffer(iwb.getPropertiesRealPath());
        if (!path.toString().endsWith(FileUtil.getFileSeparator()))
          path.append(FileUtil.getFileSeparator());

        path.append(iwb.getProperty("sybaseproperties","sybasedb.properties"));

        Properties props = new Properties();
        try {
          props.load(new FileInputStream(path.toString()));
        }
        catch(FileNotFoundException e2) {
          e2.printStackTrace();
          return;
        }
        catch(IOException e2) {
          e2.printStackTrace();
          return;
        }

        if (className_.equals(ut)) {
          String idString = IShopExportBusiness.getInstance().getMaxIdForUT(props);
          int idInt = Integer.parseInt(idString);
          idInt++;
          temp.setIShopID(Integer.toString(idInt));
          IShopTemplateHome.getInstance().update(temp);
        }

        IShopExportBusiness.getInstance().createPage(temp,props);
      }
    }
    else if (quit) {
      close();
    }
    else {
      String id_ = iwc.getParameter(IS_ID);
      String className_ = iwc.getParameter(IS_CLASS);
      String langnr_ = iwc.getParameter(IS_LANGNR);
      String name_ = iwc.getParameter(IS_NAME);
      String description_ = iwc.getParameter(IS_DESCRIPTION);

      if (className_ != null) {
        if (!className_.equals(ut)) {
          if (id_ != null)
            id.setValue(id_);
          id.setDisabled(false);
        }
        else
          id.setDisabled(true);
        mnu.setSelectedElement(className_);
      }

      if (langnr_ != null)
        langnr.setValue(langnr_);

      if (name_ != null)
        name.setValue(name_);

      if (description_ != null)
        description.setValue(description_);

    }
  }

  /**
   *
   */
  public String getBundleIdentifier() {
    return IW_BUNDLE_IDENTIFIER;
  }
}