package com.idega.block.albumcollection.presentation;

import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.block.albumcollection.business.AlbumCollectionBusiness;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Text;

/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class DeleteConfirmWindow extends IWAdminWindow {

  private static String _fieldNameType = "ac_file_type";
  private static String _fieldNameIndentifier = "ac_file_identifier";
  private static String _fieldNameConfirm = "ac_confirm";

  public static final String _PRM_ID = "ac_file_id";

  private static HiddenInput _fieldType;
  private static HiddenInput _fieldIndentifier;

  private Form myForm;

  public Text question;

  public SubmitButton confirm;
  public CloseButton close;
  public Table myTable = null;

  public DeleteConfirmWindow() {
    super();
    this.setHeight(150);
    this.setWidth(300);
    this.setScrollbar(false);
    this.setAllMargins(0);
    myForm = new Form();

    question = Text.getBreak();
    confirm = new SubmitButton(_fieldNameConfirm,"   Yes   ");
    close = new CloseButton("  Cancel ");
  }

  private void delete(IWContext iwc){
    String identifier = iwc.getParameter(_fieldNameIndentifier);
    String type = iwc.getParameter(_fieldNameType);

    int typeConst = -1;
    if(type != null){
      typeConst = Integer.parseInt(type);
    }

    int id = -1;
    if(identifier != null){
      id = Integer.parseInt(identifier);
    }

    switch (typeConst) {
      case AlbumCollectionBusiness._CONST_ALBUM:
        AlbumCollectionBusiness.deleteAlbum(id);
        this.setParentToReload();
        break;
      case AlbumCollectionBusiness._CONST_TRACK:
        AlbumCollectionBusiness.deleteTrack(id);
        this.setParentToReload();
        break;
      case AlbumCollectionBusiness._CONST_LYRIC:
        AlbumCollectionBusiness.deleteLyric(id);
        this.setOnUnLoad("window.opener.history.go(-1).reload()");
        break;
      default:
        System.err.println("item not found id : " + id + "and type: "+typeConst);
        break;
    }


  }

  public void main(IWContext iwc) throws Exception {
    myForm.empty();
    initialze(iwc);

    String identifier = iwc.getParameter(_PRM_ID);
    String type = iwc.getParameter(AlbumCollectionBusiness._PRM_DELETE);

    if(identifier != null || type != null){
      myForm.add(new HiddenInput(_fieldNameIndentifier,identifier));
      myForm.add(new HiddenInput(_fieldNameType,type));
    }else{
      myForm.maintainParameter(_fieldNameIndentifier);
      myForm.maintainParameter(_fieldNameType);
    }

    if(iwc.getParameter(_fieldNameConfirm) == null){
      this.add(myForm);
      //updateFieldStatus(iwc);
      myForm.add(getElementsOredered(iwc));
    } else {
      this.delete(iwc);
      this.close();
    }
  }


    public PresentationObject getElementsOredered(IWContext iwc){
      myTable = new Table(2,2);
      myTable.setWidth("100%");
      myTable.setHeight("100%");
      myTable.setCellpadding(5);
      myTable.setCellspacing(5);
      //myTable.setBorder(1);


      myTable.mergeCells(1,1,2,1);

      myTable.add(question,1,1);

      myTable.add(confirm,1,2);

      myTable.add(close,2,2);

      myTable.setAlignment(1,1,"center");
//      myTable.setAlignment(2,1,"center");
      myTable.setAlignment(1,2,"right");
      myTable.setAlignment(2,2,"left");

      myTable.setVerticalAlignment(1,1,"middle");
      myTable.setVerticalAlignment(1,2,"middle");
      myTable.setVerticalAlignment(2,2,"middle");

      myTable.setHeight(2,"30%");

      return myTable;

    }

    public void setQuestion(Text Question){
      question = Question;
    }


    public void initialze(IWContext iwc){
      this.setQuestion(new Text("Are you sure you want to delete?"));
    }



}
