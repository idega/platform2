package com.idega.block.albumcollection.presentation;

import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.Table;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.text.Text;
import com.idega.util.idegaTimestamp;

/**
 * Title:        AlbumCollection
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class CreateAlbum extends IWAdminWindow {

  private Form myForm;

  private DropdownMenu _fieldAlbumType;
  private TextInput _fieldName;
  //private TextInput _field;
  private DateInput _fieldPublishingDay;
  private SelectionBox _fieldAuthors;
  private SelectionBox _fieldPerformers;
  private SelectionBox _fieldCategories;

  public CreateAlbum() {
    super();

    initFields();
    myForm = new Form();
    /*myTable = new Table(1,1);
    myTable.setWidth("100%");
    myTable.setHeight("100%");
    myTable.setCellspacing(0);
    myTable.setAlignment(1,1,"center");
    myTable.setVerticalAlignment(1,1,"top");*/
  }


  public void initFields(){
    _fieldAlbumType = new DropdownMenu();
    _fieldName = new TextInput();
    _fieldPublishingDay  = new DateInput();
    idegaTimestamp time = idegaTimestamp.RightNow();
    _fieldPublishingDay.setYearRange(time.getYear(),time.getYear()-100);

    _fieldAuthors = new SelectionBox();
    _fieldPerformers = new SelectionBox();
    _fieldCategories = new SelectionBox();
  }


  public PresentationObject getElementsOredered(IWContext iwc){
    Table contentTable = new Table();

    //
    Table nameTable = new Table(2,3);
    nameTable.add(new Text("Nafn:"),1,1);
    nameTable.add(this._fieldName,2,1);
    nameTable.add(new Text("Gerð:"),1,2);
    nameTable.add(this._fieldAlbumType,2,2);
    nameTable.add(new Text("Útgáfudagur:"),1,3);
    nameTable.add(this._fieldPublishingDay,2,3);

    contentTable.add(nameTable,1,1);

    Table t2 = new Table(2,2);
    t2.setWidth("100%");
    t2.add(new Text("Höfundar:"),1,1);
    t2.add(this._fieldAuthors,1,2);
    t2.add(new Text("Flytjendur:"),2,1);
    t2.add(this._fieldPerformers,2,2);

    contentTable.add(t2,1,2);

    // ButtonTable
    Table bTable = new Table(2,1);
    //bTable.setCellpadding(4);
    bTable.add(new SubmitButton("  Save  ","save","true"),1,1);
    bTable.add(new CloseButton("  Close  "),2,1);

    contentTable.add(bTable,1,3);
    contentTable.setAlignment(1,3,"right");



    return contentTable;
  }

  public void main(IWContext iwc) throws Exception {
    this.add(myForm);
    myForm.empty();
    //updateFieldStatus(iwc);
    myForm.add(getElementsOredered(iwc));
  }


}