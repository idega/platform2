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
import com.idega.presentation.text.Text;

/**
 * Title:        AlbumCollection
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class CreateAlbum extends IWAdminWindow {

  private Table myTable;

  private DropdownMenu _fieldAlbumType;
  private TextInput _fieldName;
  //private TextInput _field;
  private DateInput _fieldPublishingDay;
  private SelectionBox _fieldAuthors;
  private SelectionBox _fieldPerformers;
  private SelectionBox _fieldCategories;

  public CreateAlbum() {
    myTable = new Table(1,1);
    myTable.setWidth("100%");
    myTable.setHeight("100%");
    myTable.setCellspacing(0);
    this.add(myTable);
  }



  public PresentationObject getElementsOredered(IWContext iwc){
    Table contentTable = new Table();

    //
    Table nameTable = new Table(2,3);
    nameTable.add(new Text(),1,1);
    nameTable.add(this._fieldName,2,1);
    nameTable.add(new Text(),1,1);
    nameTable.add(this._fieldAlbumType,2,2);
    nameTable.add(new Text(),1,1);
    nameTable.add(this._fieldPublishingDay,2,3);

    contentTable.add(nameTable,1,1);

    Table t2 = new Table(2,2);
    t2.add(this._fieldAuthors,1,2);
    t2.add(this._fieldPerformers,2,2);

    contentTable.add(t2,1,2);



    return contentTable;
  }

  public void main(IWContext iwc) throws Exception {
    myTable.empty();
    myTable.add(getElementsOredered(iwc));
  }


}