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



    return contentTable;
  }

  public void main(IWContext iwc) throws Exception {
    myTable.empty();
    myTable.add(getElementsOredered(iwc));
  }


}