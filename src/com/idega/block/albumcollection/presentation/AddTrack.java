package com.idega.block.albumcollection.presentation;

import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.Table;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.IntegerInput;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.text.Text;
import com.idega.presentation.text.Link;
import com.idega.presentation.PresentationObject;
import com.idega.block.albumcollection.data.Author;
import com.idega.block.albumcollection.data.Performer;
import com.idega.block.albumcollection.business.AlbumCollectionBusiness;


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

public class AddTrack extends IWAdminWindow {

  private Form myForm;

  private HiddenInput _fieldAlbumId;
  private TextInput _fieldTrackName;
  private IntegerInput _fieldTrackNumber;
  private IntegerInput _fieldTrackLengthMin;
  private IntegerInput _fieldTrackLengthSek;
  private TextArea _fieldDescription;
  private SelectionBox _fieldAuthors;
  private SelectionBox _fieldPerformers;
  private SelectionBox _fieldCategories;

  private static String _fieldNameAlbumId = AlbumCollectionBusiness._PRM_ALBUM_ID;
  private static String _fieldNameTrackName = "ac_track_name";
  private static String _fieldNameTrackNumber = "ac_track_number";
  private static String _fieldNameTrackLengthMin = "ac_track_length_min";
  private static String _fieldNameTrackLengthSek = "ac_track_length_sek";
  private static String _fieldNameDescription = "ac_track_description";
  private static String _fieldNameAuthors = "ac_authors";
  private static String _fieldNamePerformers = "ac_performers";
  private static String _fieldNameCategories = "ac_categories";


  public AddTrack() {
    super();
    this.setHeight(440);
    this.setScrollbar(false);

    myForm = new Form();
  }


  public void initFields(IWContext iwc) throws Exception{
    String str = iwc.getParameter(_fieldNameAlbumId);
    if(str != null && !str.equals("")){
      _fieldAlbumId = new HiddenInput(this._fieldNameAlbumId,str);
    }else{
      _fieldAlbumId = new HiddenInput(this._fieldNameAlbumId);
    }
    _fieldAlbumId.keepStatusOnAction();


    _fieldTrackName = new TextInput(_fieldNameTrackName);
    _fieldTrackName.keepStatusOnAction();

    _fieldTrackNumber = new IntegerInput(this._fieldNameTrackNumber);
    _fieldTrackNumber.setLength(2);
    _fieldTrackNumber.setMaxlength(2);

    _fieldTrackLengthMin = new IntegerInput(this._fieldNameTrackLengthMin);
    _fieldTrackLengthMin.setLength(2);
    _fieldTrackLengthMin.setMaxlength(2);

    _fieldTrackLengthSek = new IntegerInput(this._fieldNameTrackLengthSek);
    _fieldTrackLengthSek.setLength(2);
    _fieldTrackLengthSek.setMaxlength(2);


    _fieldDescription = new TextArea(this._fieldNameDescription);
    _fieldDescription.setHeight(6);
    _fieldDescription.setWidth(42);
    _fieldDescription.keepStatusOnAction();



    _fieldAuthors = new SelectionBox(_fieldNameAuthors);
    _fieldAuthors.setHeight(6);
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
    _fieldPerformers.setHeight(6);
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
    nameTable.add(this._fieldTrackName,2,1);
    nameTable.add(this._fieldAlbumId,2,1);
    nameTable.add(new Text("Númer lags:"),1,2);
    nameTable.add(this._fieldTrackNumber,2,2);
    nameTable.add(new Text("Lengd:"),1,3);
    Table lTable = new Table(3,1);
    lTable.setCellpadding(0);
    lTable.setCellspacing(0);
    lTable.add(this._fieldTrackLengthMin,1,1);
    lTable.add(new Text(" : "),2,1);
    lTable.add(this._fieldTrackLengthSek,2,1);
    nameTable.add(lTable,2,3);


    contentTable.add(nameTable,1,1);

    //Description
    Table descriptionTable = new Table(1,2);
    descriptionTable.setWidth("100%");
    //descriptionTable.setCellpadding(0);
    //descriptionTable.setCellspacing(0);
    //descriptionTable.setHeight(1,"32");
    descriptionTable.add(new Text("Um lagið"),1,1);
    descriptionTable.add(this._fieldDescription,1,2);

    contentTable.add(descriptionTable,1,2);

    Table t2 = new Table(2,3);
    t2.setWidth("100%");
    t2.add(new Text("Höfundar:"),1,1);
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

  public void saveTrack(IWContext iwc) throws Exception {
    String acAlbumId = iwc.getParameter(_fieldNameAlbumId);
    String acName = iwc.getParameter(_fieldNameTrackName);

    String acTrackNumber = iwc.getParameter(_fieldNameTrackNumber);

    String acDescription = iwc.getParameter(_fieldNameDescription);

    String acLengthMin = iwc.getParameter(_fieldNameTrackLengthMin);
    String acLengthSek = iwc.getParameter(_fieldNameTrackLengthSek);

    String[] acAuthors = iwc.getParameterValues(_fieldNameAuthors);
    String[] acPerformers = iwc.getParameterValues(_fieldNamePerformers);

    Integer TrackNumber = null;
    if(acTrackNumber != null && !"".equals(acTrackNumber)){
      TrackNumber = new Integer(acTrackNumber);
    }

    Integer AlbumId = null;
    if( acAlbumId != null && !acAlbumId.equals("")){
      AlbumId = new Integer(acAlbumId);
    }

    Integer trackLength = null;
    int length = 0;
    if(acLengthMin != null && !acLengthMin.equals("")){
      length += (Integer.parseInt(acLengthMin)*60);
    }
    if(acLengthSek != null && !acLengthSek.equals("")){
      length += Integer.parseInt(acLengthSek);
    }
    if(length != 0){
      trackLength = new Integer(length);
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



    AlbumCollectionBusiness.addTrack(acName,acDescription,TrackNumber,AlbumId,null,trackLength,authorIDs,performerIDs,null);

  }

  public void main(IWContext iwc) throws Exception {

    if(iwc.getParameter("save") == null){
      initFields(iwc);
      this.add(myForm);
      myForm.empty();
      //updateFieldStatus(iwc);
      myForm.add(getElementsOredered(iwc));
    } else {
      this.saveTrack(iwc);
      this.close();
      this.setParentToReload();
    }

  }

}