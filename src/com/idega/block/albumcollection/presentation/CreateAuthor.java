package com.idega.block.albumcollection.presentation;

import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.Table;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.block.albumcollection.business.AlbumCollectionBusiness;

/**
 * Title:        AlbumCollection
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author <a href="mailto:gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
 * @version 1.0
 */

public class CreateAuthor extends IWAdminWindow {

  private Form myForm;

  private TextInput _fieldName;
  private TextInput _fieldDisplayName;

  private static String _fieldNameName = "ac_author_name";
  private static String _fieldNameDisplayName = "ac_author_display_name";

  public CreateAuthor() {
    super();
    this.setHeight(170);
    this.setWidth(350);
    this.setScrollbar(false);
    this.myForm = new Form();
  }

  public void initFields(IWContext iwc){
    this._fieldName = new TextInput(_fieldNameName);
    this._fieldName.keepStatusOnAction();

    this._fieldDisplayName = new TextInput(_fieldNameDisplayName);
    this._fieldDisplayName.keepStatusOnAction();
  }


  public PresentationObject getElementsOredered(IWContext iwc){
    Table contentTable = new Table();
    contentTable.setWidth("100%");
    //
    Table nameTable = new Table(2,2);
    nameTable.add(new Text("Fullt nafn:"),1,1);
    nameTable.add(this._fieldName,2,1);
    nameTable.add(new Text("Nafn � vef:"),1,2);
    nameTable.add(this._fieldDisplayName,2,2);
    // ButtonTable

    contentTable.add(nameTable,1,1);

    Table bTable = new Table(2,1);
    //bTable.setCellpadding(4);
    bTable.add(new SubmitButton("  Save  ","save","true"),1,1);
    bTable.add(new CloseButton("  Close  "),2,1);

    contentTable.add(bTable,1,2);
    contentTable.setAlignment(1,2,"right");

    return contentTable;
  }


  public void saveAuthor(IWContext iwc) throws Exception {
    String acName = iwc.getParameter(_fieldNameName);
    String acDisplayName = iwc.getParameter(_fieldNameDisplayName);

    AlbumCollectionBusiness.addAuthor(acName,acDisplayName);

  }



  public void main(IWContext iwc) throws Exception {
    if(iwc.getParameter("save") == null){
      initFields(iwc);
      this.add(this.myForm);
      this.myForm.empty();
      //updateFieldStatus(iwc);
      this.myForm.add(getElementsOredered(iwc));
    } else {
      this.saveAuthor(iwc);
      this.close();
      this.setParentToReload();
    }
  }


}
