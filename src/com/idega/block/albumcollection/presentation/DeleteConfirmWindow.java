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
 * @author <a href="gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
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
    this.myForm = new Form();

    this.question = Text.getBreak();
    this.confirm = new SubmitButton(_fieldNameConfirm,"   Yes   ");
    this.close = new CloseButton("  Cancel ");
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
    this.myForm.empty();
    initialze(iwc);

    String identifier = iwc.getParameter(_PRM_ID);
    String type = iwc.getParameter(AlbumCollectionBusiness._PRM_DELETE);

    if(identifier != null || type != null){
      this.myForm.add(new HiddenInput(_fieldNameIndentifier,identifier));
      this.myForm.add(new HiddenInput(_fieldNameType,type));
    }else{
      this.myForm.maintainParameter(_fieldNameIndentifier);
      this.myForm.maintainParameter(_fieldNameType);
    }

    if(iwc.getParameter(_fieldNameConfirm) == null){
      this.add(this.myForm);
      //updateFieldStatus(iwc);
      this.myForm.add(getElementsOredered(iwc));
    } else {
      this.delete(iwc);
      this.close();
    }
  }


    public PresentationObject getElementsOredered(IWContext iwc){
      this.myTable = new Table(2,2);
      this.myTable.setWidth("100%");
      this.myTable.setHeight("100%");
      this.myTable.setCellpadding(5);
      this.myTable.setCellspacing(5);
      //myTable.setBorder(1);


      this.myTable.mergeCells(1,1,2,1);

      this.myTable.add(this.question,1,1);

      this.myTable.add(this.confirm,1,2);

      this.myTable.add(this.close,2,2);

      this.myTable.setAlignment(1,1,"center");
//      myTable.setAlignment(2,1,"center");
      this.myTable.setAlignment(1,2,"right");
      this.myTable.setAlignment(2,2,"left");

      this.myTable.setVerticalAlignment(1,1,"middle");
      this.myTable.setVerticalAlignment(1,2,"middle");
      this.myTable.setVerticalAlignment(2,2,"middle");

      this.myTable.setHeight(2,"30%");

      return this.myTable;

    }

    public void setQuestion(Text Question){
      this.question = Question;
    }


    public void initialze(IWContext iwc){
      this.setQuestion(new Text("Are you sure you want to delete?"));
    }



}
