package com.idega.block.albumcollection.presentation;

import java.util.Iterator;
import java.util.List;

import com.idega.block.albumcollection.business.AlbumCollectionBusiness;
import com.idega.block.albumcollection.data.Album;
import com.idega.block.albumcollection.data.AlbumType;
import com.idega.block.albumcollection.data.Author;
import com.idega.block.albumcollection.data.Performer;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.data.GenericEntity;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

/**
 * Title:        AlbumCollection
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author <a href="mailto:gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
 * @version 1.0
 */

public class CreateAlbum extends IWAdminWindow {

  private Form myForm;

  private HiddenInput _fieldAlbumId;
  private DropdownMenu _fieldAlbumType;
  private TextInput _fieldAlbumName;
  //private TextInput _field;
  private DateInput _fieldPublishingDay;
  private TextArea _fieldDescription;
  private SelectionBox _fieldAuthors;
  private SelectionBox _fieldPerformers;
  private SelectionBox _fieldCategories;
  private ImageInserter _imageInsert;

  private static String _fieldNameAlbumId = AlbumCollectionBusiness._PRM_ALBUM_ID;
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
    this.setHeight(445);
    this.setScrollbar(false);

    this.myForm = new Form();
  }


  public void initFields(IWContext iwc) throws Exception{
    String str = iwc.getParameter(AlbumCollectionBusiness._PRM_ALBUM_ID);
    Album album = null;
    if(str != null){
      //update
      if(str != null && !str.equals("")){
        this._fieldAlbumId = new HiddenInput(CreateAlbum._fieldNameAlbumId,str);
        if(iwc.getParameter(AlbumCollectionBusiness._PRM_UPDATE) != null){
          album = ((com.idega.block.albumcollection.data.AlbumHome)com.idega.data.IDOLookup.getHomeLegacy(Album.class)).findByPrimaryKeyLegacy(Integer.parseInt(str));
        }
      }
      this._fieldAlbumId.keepStatusOnAction();
    }

    this._fieldAlbumType = new DropdownMenu(_fieldNameAlbumType);
    List albumTypeList = AlbumCollectionBusiness.getAlbumTypes();
    if(albumTypeList != null){
      Iterator iter = albumTypeList.iterator();
      while (iter.hasNext()) {
        AlbumType item = (AlbumType)iter.next();
        this._fieldAlbumType.addMenuElement(item.getID(),item.getName());
      }
    }
    if(album != null){
      this._fieldAlbumType.setSelectedElement(Integer.toString(album.getAlbumTypeId()));
    }
    this._fieldAlbumType.keepStatusOnAction();

    this._fieldAlbumName = new TextInput(_fieldNameAlbumName);
    this._fieldAlbumName.keepStatusOnAction();
    if(album != null){
      String sName = album.getName();
      if(sName != null){
        this._fieldAlbumName.setContent(sName);
      }
    }
    this._fieldPublishingDay  = new DateInput(_fieldNamePublishingDay);
    IWTimestamp time = IWTimestamp.RightNow();
    this._fieldPublishingDay.setYearRange(time.getYear(),time.getYear()-100);
    if(album != null){
      if(album.getPublishingDay() != null){
        this._fieldPublishingDay.setDate(album.getPublishingDay());
      }
    }
    this._fieldPublishingDay.keepStatusOnAction();

    String imageId = iwc.getParameter(_fieldNameFrontCoverId);
    if(imageId != null){
      this._imageInsert = new ImageInserter(Integer.parseInt(imageId));
    } else {
      if(album != null && album.getFrontCoverFileId() > 0){
        this._imageInsert = new ImageInserter(album.getFrontCoverFileId());
      } else {
        this._imageInsert = new ImageInserter();
      }
    }
    this._imageInsert.setImSessionImageName(_fieldNameFrontCoverId);
    this._imageInsert.setImageHeight(100);
    this._imageInsert.setHasUseBox(false);


    this._fieldDescription = new TextArea(CreateAlbum._fieldNameDescription);
    this._fieldDescription.setHeight(6);
    this._fieldDescription.setWidth(22);
    if(album != null){
      String sDescription = album.getDescription();
      if(sDescription != null){
        this._fieldDescription.setContent(sDescription);
      }
    }
    this._fieldDescription.keepStatusOnAction();


    this._fieldAuthors = new SelectionBox(_fieldNameAuthors);
    this._fieldAuthors.setHeight(6);
    List authorList = AlbumCollectionBusiness.getAuthors();
    if(authorList != null){
      Iterator iter = authorList.iterator();
      while (iter.hasNext()) {
        Author item = (Author)iter.next();
        this._fieldAuthors.addMenuElement(item.getID(),item.getDisplayName());
      }
    }
    if(album != null){
      int[] IDs = album.findRelatedIDs(GenericEntity.getStaticInstance(Author.class));
      for (int i = 0; i < IDs.length; i++) {
        this._fieldAuthors.setSelectedElement(Integer.toString(IDs[i]));
      }
    }
    this._fieldAuthors.keepStatusOnAction();

    this._fieldPerformers = new SelectionBox(_fieldNamePerformers);
    this._fieldPerformers.setHeight(6);
    List perfomerList = AlbumCollectionBusiness.getPerformers();
    if(perfomerList != null){
      Iterator iter = perfomerList.iterator();
      while (iter.hasNext()) {
        Performer item = (Performer)iter.next();
        this._fieldPerformers.addMenuElement(item.getID(),item.getDisplayName());
      }
    }
    if(album != null){
      int[] IDs = album.findRelatedIDs(GenericEntity.getStaticInstance(Performer.class));
      for (int i = 0; i < IDs.length; i++) {
        this._fieldPerformers.setSelectedElement(Integer.toString(IDs[i]));
      }
    }
    this._fieldPerformers.keepStatusOnAction();

    //_fieldCategories = new SelectionBox(_fieldNameCategories);
  }


  public PresentationObject getElementsOredered(IWContext iwc){
    Table contentTable = new Table();
    contentTable.setWidth("100%");
    //
    Table nameTable = new Table(2,3);
    nameTable.add(new Text("Nafn:"),1,1);
    nameTable.add(this._fieldAlbumName,2,1);
    nameTable.add(new Text("Ger�:"),1,2);
    nameTable.add(this._fieldAlbumType,2,2);
    nameTable.add(this._fieldAlbumId,2,2);
    //nameTable.add(this._imageInsert,3,1);
    //nameTable.mergeCells(3,1,3,2);
    nameTable.add(new Text("�tg�fudagur:"),1,3);
    nameTable.add(this._fieldPublishingDay,2,3);
    //nameTable.mergeCells(2,3,3,3);


    contentTable.add(nameTable,1,1);

    //Description
    Table descriptionTable = new Table(2,2);
    descriptionTable.setWidth("100%");
    //descriptionTable.setCellpadding(0);
    //descriptionTable.setCellspacing(0);
    //descriptionTable.setHeight(1,"32");
    descriptionTable.add(new Text("Um pl�tuna"),1,1);
    descriptionTable.add(this._fieldDescription,1,2);
    descriptionTable.add(new Text("Mynd"),2,1);
    descriptionTable.add(this._imageInsert,2,2);


    contentTable.add(descriptionTable,1,2);

    Table t2 = new Table(2,3);
    t2.setWidth("100%");
    t2.add(new Text("H�fundar:"),1,1);
    t2.add(this._fieldAuthors,1,2);
    t2.add(new Text("Flytjendur:"),2,1);
    t2.add(this._fieldPerformers,2,2);
    Link addAuthor = new Link("add");
    addAuthor.setWindowToOpen(CreateAuthor.class);
    t2.add(addAuthor,1,3);
    Link addPerformer = new Link("add");
    addPerformer.setWindowToOpen(CreatePerformer.class);
    t2.add(addPerformer,2,3);


    contentTable.add(t2,1,3);

    // ButtonTable
    Table bTable = new Table(2,1);
    //bTable.setCellpadding(4);
    bTable.add(new SubmitButton("  Save  ","save","true"),1,1);
    bTable.add(new CloseButton("  Close  "),2,1);

    contentTable.add(bTable,1,4);
    contentTable.setAlignment(1,4,"right");

    return contentTable;
  }

  public void saveNewAlbum(IWContext iwc) throws Exception {

    String acAlbumId = iwc.getParameter(_fieldNameAlbumId);
    int albumId = -1;
    if(acAlbumId != null && !"".equals(acAlbumId)){
      albumId = Integer.parseInt(acAlbumId);
    }

    String acType = iwc.getParameter(_fieldNameAlbumType);
    String acName = iwc.getParameter(_fieldNameAlbumName);

    String acDescription = iwc.getParameter(CreateAlbum._fieldNameDescription);

    String acPublishDay = iwc.getParameter(_fieldNamePublishingDay);
    String acFrontCoverId = iwc.getParameter(_fieldNameFrontCoverId);

    String[] acAuthors = iwc.getParameterValues(_fieldNameAuthors);
    String[] acPerformers = iwc.getParameterValues(_fieldNamePerformers);

    System.out.println("front_cover: "+iwc.getParameter(CreateAlbum._fieldNameFrontCoverId));


    IWTimestamp publishingDay = null;
    if( acPublishDay != null && !acPublishDay.equals("")){
      publishingDay = new IWTimestamp(acPublishDay);
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
    if(albumId > -1){
      AlbumCollectionBusiness.updateAlbum(albumId, acName,acDescription,new Integer(acType),publishingDay,authorIDs,performerIDs,null,frontCoverId);
    }else{
      AlbumCollectionBusiness.createAlbum(acName,acDescription,new Integer(acType),publishingDay,authorIDs,performerIDs,null,frontCoverId);
    }
  }

  public void main(IWContext iwc) throws Exception {

    if(iwc.getParameter("save") == null){
      initFields(iwc);
      this.add(this.myForm);
      this.myForm.empty();
      //updateFieldStatus(iwc);
      this.myForm.add(getElementsOredered(iwc));
    } else {
      this.saveNewAlbum(iwc);
      this.close();
      this.setParentToReload();
    }

  }


}
