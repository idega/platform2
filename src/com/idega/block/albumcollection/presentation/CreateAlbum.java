package com.idega.block.albumcollection.presentation;

import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.Table;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.text.Text;
import com.idega.util.idegaTimestamp;
import com.idega.block.albumcollection.business.AlbumCollectionBusiness;
import com.idega.block.albumcollection.data.*;
import com.idega.util.idegaTimestamp;
import com.idega.block.media.presentation.SimpleChooserWindow;
import com.idega.block.media.presentation.ImageInserter;

import java.util.List;
import java.util.Iterator;

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
  private TextInput _fieldAlbumName;
  //private TextInput _field;
  private DateInput _fieldPublishingDay;
  private TextArea _fieldDescription;
  private SelectionBox _fieldAuthors;
  private SelectionBox _fieldPerformers;
  private SelectionBox _fieldCategories;
  private ImageInserter _imageInsert;

  private static String _fieldNameAlbumType = "ac_album_type";
  private static String _fieldNameAlbumName = "ac_album_name";
  //private static String _fieldName = "ac_";
  private static String _fieldNameFrontCoverId = "ac_front_cover_id";
  private static String _fieldNamePublishingDay = "ac_publishing_day";
  private static String _fieldNameDescription = "ac_album_description";
  private static String _fieldNameAuthors = "ac_authors";
  private static String _fieldNamePerformers = "ac_performers";
  private static String _fieldNameCategories = "ac_categories";


  public CreateAlbum() {
    super();

    myForm = new Form();
    /*myTable = new Table(1,1);
    myTable.setWidth("100%");
    myTable.setHeight("100%");
    myTable.setCellspacing(0);
    myTable.setAlignment(1,1,"center");
    myTable.setVerticalAlignment(1,1,"top");*/
  }


  public void initFields(IWContext iwc) throws Exception{
    _fieldAlbumType = new DropdownMenu(_fieldNameAlbumType);
    List albumTypeList = AlbumCollectionBusiness.getAlbumTypes();
    if(albumTypeList != null){
      Iterator iter = albumTypeList.iterator();
      while (iter.hasNext()) {
        AlbumType item = (AlbumType)iter.next();
        this._fieldAlbumType.addMenuElement(item.getID(),item.getName());
      }
    }
    _fieldAlbumType.keepStatusOnAction();

    _fieldAlbumName = new TextInput(_fieldNameAlbumName);
    _fieldAlbumName.keepStatusOnAction();
    _fieldPublishingDay  = new DateInput(_fieldNamePublishingDay);
    idegaTimestamp time = idegaTimestamp.RightNow();
    _fieldPublishingDay.setYearRange(time.getYear(),time.getYear()-100);
    _fieldPublishingDay.keepStatusOnAction();

    String imageId = iwc.getParameter(_fieldNameFrontCoverId);
    if(imageId != null){
      _imageInsert = new ImageInserter(Integer.parseInt(imageId));
    } else {
      _imageInsert = new ImageInserter();
    }
    _imageInsert.setImSessionImageName(_fieldNameFrontCoverId);
    _imageInsert.setWindowClassToOpen(SimpleChooserWindow.class);
    _imageInsert.setHasUseBox(false);


    _fieldDescription = new TextArea(this._fieldNameDescription);
    _fieldDescription.setHeight(6);
    _fieldDescription.setWidth(22);
    _fieldDescription.keepStatusOnAction();


    _fieldAuthors = new SelectionBox(_fieldNameAuthors);
    List authorList = AlbumCollectionBusiness.getAuthors();
    if(authorList != null){
      Iterator iter = authorList.iterator();
      while (iter.hasNext()) {
        Author item = (Author)iter.next();
        this._fieldAuthors.addMenuElement(item.getID(),item.getDisplayName());
      }
    }
    _fieldAuthors.keepStatusOnAction();

    _fieldPerformers = new SelectionBox(_fieldNamePerformers);
    List perfomerList = AlbumCollectionBusiness.getPerformers();
    if(perfomerList != null){
      Iterator iter = perfomerList.iterator();
      while (iter.hasNext()) {
        Performer item = (Performer)iter.next();
        this._fieldPerformers.addMenuElement(item.getID(),item.getDisplayName());
      }
    }
    _fieldPerformers.keepStatusOnAction();

    //_fieldCategories = new SelectionBox(_fieldNameCategories);
  }


  public PresentationObject getElementsOredered(IWContext iwc){
    Table contentTable = new Table();
    contentTable.setWidth("100%");
    //
    Table nameTable = new Table(2,3);
    nameTable.add(new Text("Nafn:"),1,1);
    nameTable.add(this._fieldAlbumName,2,1);
    nameTable.add(new Text("Gerð:"),1,2);
    nameTable.add(this._fieldAlbumType,2,2);
    //nameTable.add(this._imageInsert,3,1);
    //nameTable.mergeCells(3,1,3,2);
    nameTable.add(new Text("Útgáfudagur:"),1,3);
    nameTable.add(this._fieldPublishingDay,2,3);
    //nameTable.mergeCells(2,3,3,3);


    contentTable.add(nameTable,1,1);

    //Description
    Table descriptionTable = new Table(2,2);
    descriptionTable.setWidth("100%");
    //descriptionTable.setCellpadding(0);
    //descriptionTable.setCellspacing(0);
    //descriptionTable.setHeight(1,"32");
    descriptionTable.add(new Text("Um plötuna"),1,1);
    descriptionTable.add(this._fieldDescription,1,2);
    descriptionTable.add(new Text("Mynd"),2,1);
    descriptionTable.add(this._imageInsert,2,2);


    contentTable.add(descriptionTable,1,2);

    Table t2 = new Table(2,2);
    t2.setWidth("100%");
    t2.add(new Text("Höfundar:"),1,1);
    t2.add(this._fieldAuthors,1,2);
    t2.add(new Text("Flytjendur:"),2,1);
    t2.add(this._fieldPerformers,2,2);

    contentTable.add(t2,1,3);

    // ButtonTable
    Table bTable = new Table(2,1);
    //bTable.setCellpadding(4);
    bTable.add(new SubmitButton("  Save  ","save","true"),1,1);
    bTable.add(new CloseButton("  Close  "),2,1);

    contentTable.add(bTable,1,4);
    contentTable.setAlignment(1,3,"right");

    return contentTable;
  }

  public void saveNewAlbum(IWContext iwc) throws Exception {
    String acType = iwc.getParameter(_fieldNameAlbumType);
    String acName = iwc.getParameter(_fieldNameAlbumName);

    String acDescription = iwc.getParameter(this._fieldNameDescription);

    String acPublishDay = iwc.getParameter(_fieldNamePublishingDay);
    String acFrontCoverId = iwc.getParameter(_fieldNameFrontCoverId);

    String[] acAuthors = iwc.getParameterValues(_fieldNameAuthors);
    String[] acPerformers = iwc.getParameterValues(_fieldNamePerformers);

    System.out.println("front_cover: "+iwc.getParameter(this._fieldNameFrontCoverId));


    idegaTimestamp publishingDay = null;
    if( acPublishDay != null && !acPublishDay.equals("")){
      publishingDay = new idegaTimestamp(acPublishDay);
    }

    int[] authorIDs = null;
    if(acAuthors != null){
      authorIDs = new int[acAuthors.length];
      for (int i = 0; i < acAuthors.length; i++) {
        try {
          authorIDs[i] = Integer.parseInt(acAuthors[i]);
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }

    int[] performerIDs = null;
    if(acPerformers != null){
      performerIDs = new int[acPerformers.length];
      for (int i = 0; i < acPerformers.length; i++) {
        try {
          performerIDs[i] = Integer.parseInt(acPerformers[i]);
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }

    Integer frontCoverId = null;
    if(acFrontCoverId != null && !acFrontCoverId.equals("-1")){
     frontCoverId = new Integer(acFrontCoverId);
    }

    AlbumCollectionBusiness.createAlbum(acName,acDescription,new Integer(acType),publishingDay,authorIDs,performerIDs,null,frontCoverId);

  }

  public void main(IWContext iwc) throws Exception {

    if(iwc.getParameter("save") == null){
      initFields(iwc);
      this.add(myForm);
      myForm.empty();
      //updateFieldStatus(iwc);
      myForm.add(getElementsOredered(iwc));
    } else {
      this.saveNewAlbum(iwc);
      this.close();
      this.setParentToReload();
    }

  }


}