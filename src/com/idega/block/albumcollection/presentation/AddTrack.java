package com.idega.block.albumcollection.presentation;

import java.util.Iterator;
import java.util.List;

import com.idega.block.albumcollection.business.AlbumCollectionBusiness;
import com.idega.block.albumcollection.data.Author;
import com.idega.block.albumcollection.data.Performer;
import com.idega.block.albumcollection.data.Track;
import com.idega.block.media.presentation.FileChooser;
import com.idega.core.data.ICFile;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.IntegerInput;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;


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
  private HiddenInput _fieldTrackId;
  private TextInput _fieldTrackName;
  private IntegerInput _fieldTrackNumber;
  private IntegerInput _fieldTrackLengthMin;
  private IntegerInput _fieldTrackLengthSek;
  private TextArea _fieldDescription;
  private SelectionBox _fieldAuthors;
  private SelectionBox _fieldPerformers;
  private SelectionBox _fieldCategories;
  private FileChooser _fieldAudioID;
  private CheckBox _fieldAudioIsHidden;

  private static String _fieldNameAlbumId = AlbumCollectionBusiness._PRM_ALBUM_ID;
  private static String _fieldNameTrackId = AlbumCollectionBusiness._PRM_TRACK_ID;
  private static String _fieldNameTrackName = "ac_track_name";
  private static String _fieldNameTrackNumber = "ac_track_number";
  private static String _fieldNameTrackLengthMin = "ac_track_length_min";
  private static String _fieldNameTrackLengthSek = "ac_track_length_sek";
  private static String _fieldNameDescription = "ac_track_description";
  private static String _fieldNameAuthors = "ac_authors";
  private static String _fieldNamePerformers = "ac_performers";
  private static String _fieldNameCategories = "ac_categories";
  private static String _fieldNameAudioID = "ac_audio_id";
  private static String _fieldNameAudioIsHidden = "ac_audio_is_hidden";


  public AddTrack() {
    super();
    this.setHeight(460);
    this.setScrollbar(false);

    myForm = new Form();
  }


  public void initFields(IWContext iwc) throws Exception{

    String trId = iwc.getParameter(AlbumCollectionBusiness._PRM_TRACK_ID);
    Track track = null;
    if(trId != null){
      //update
      if(trId != null && !trId.equals("")){
        _fieldTrackId = new HiddenInput(_fieldNameTrackId,trId);
        if(iwc.getParameter(AlbumCollectionBusiness._PRM_UPDATE) != null){
          track = ((com.idega.block.albumcollection.data.TrackHome)com.idega.data.IDOLookup.getHomeLegacy(Track.class)).findByPrimaryKeyLegacy(Integer.parseInt(trId));
        }
      }
      _fieldTrackId.keepStatusOnAction();
      myForm.add(_fieldTrackId);
    }

    String albumId = iwc.getParameter(_fieldNameAlbumId);
    if(albumId != null && !albumId.equals("")){
      _fieldAlbumId = new HiddenInput(_fieldNameAlbumId,albumId);
    }else{
      _fieldAlbumId = new HiddenInput(_fieldNameAlbumId);
      _fieldAlbumId.setContent("");
    }
    _fieldAlbumId.keepStatusOnAction();


    _fieldTrackName = new TextInput(_fieldNameTrackName);
	_fieldTrackName.setWidth("275");
    if(track != null){
      _fieldTrackName.setContent(track.getName());
    }
    _fieldTrackName.keepStatusOnAction();

    _fieldTrackNumber = new IntegerInput(_fieldNameTrackNumber);
    _fieldTrackNumber.setLength(2);
    _fieldTrackNumber.setMaxlength(2);
    if(track != null){
      _fieldTrackNumber.setContent(Integer.toString(track.getNumber()));
    }

	
	int lengthMin = 0;
    if(track != null){
	  lengthMin = track.getLength();
		lengthMin=((lengthMin<0)?0:lengthMin);
    }
	_fieldTrackLengthMin = new IntegerInput(_fieldNameTrackLengthMin);
	_fieldTrackLengthMin.setLength(2);
	_fieldTrackLengthMin.setMaxlength(2);
    _fieldTrackLengthMin.setContent(Integer.toString(lengthMin/60));
    

    _fieldTrackLengthSek = new IntegerInput(_fieldNameTrackLengthSek);
    _fieldTrackLengthSek.setLength(2);
    _fieldTrackLengthSek.setMaxlength(2);
    _fieldTrackLengthSek.setContent(Integer.toString(lengthMin%60));



	_fieldAudioID = new FileChooser(_fieldNameAudioID);
	ICFile audioTrack = track.getTrack();
	if(audioTrack != null){
		_fieldAudioID.setSelectedFile(audioTrack);
	}
	
	
	_fieldAudioIsHidden = new CheckBox(_fieldNameAudioIsHidden,"hide");
	if(track != null){
		_fieldAudioIsHidden.setChecked(track.isAudoTrackHidden());
	}

    _fieldDescription = new TextArea(_fieldNameDescription);
    _fieldDescription.setHeight(6);
    _fieldDescription.setWidth(42);
    if(track != null){
      _fieldDescription.setContent(track.getDescription());
    }
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
    if(track != null){
      int[] IDs = track.findRelatedIDs(com.idega.block.albumcollection.data.AuthorBMPBean.getStaticInstance(Author.class));
      for (int i = 0; i < IDs.length; i++) {
        _fieldAuthors.setSelectedElement(Integer.toString(IDs[i]));
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
    if(track != null){
      int[] IDs = track.findRelatedIDs(com.idega.block.albumcollection.data.PerformerBMPBean.getStaticInstance(Performer.class));
      for (int i = 0; i < IDs.length; i++) {
        _fieldPerformers.setSelectedElement(Integer.toString(IDs[i]));
      }
    }
    _fieldPerformers.keepStatusOnAction();

    //_fieldCategories = new SelectionBox(_fieldNameCategories);
  }


  public PresentationObject getElementsOredered(IWContext iwc){
    Table contentTable = new Table();
    //contentTable.setWidth("100%");
	//contentTable.setBorder(1);
    //
    Table nameTable = new Table(4,4);
    //nameTable.setBorder(1);
    
    nameTable.setColumnWidth(1,"90");
	nameTable.setColumnWidth(2,"50");
	//nameTable.setColumnWidth(3,"70");
	nameTable.setColumnWidth(4,"170");

    
    nameTable.mergeCells(2,1,4,1);
	nameTable.mergeCells(2,3,4,3);
    nameTable.add(new Text("Nafn:"),1,1);
    nameTable.add(this._fieldTrackName,2,1);
    nameTable.add(this._fieldAlbumId,2,1);
    nameTable.add(new Text("Númer lags:"),1,2);
    nameTable.add(this._fieldTrackNumber,2,2);
    nameTable.add(new Text("Lengd:"),3,2);
    
    //lenth input table
	    Table lTable = new Table(3,1);
	    lTable.setCellpadding(0);
	    lTable.setCellspacing(0);
	    lTable.add(this._fieldTrackLengthMin,1,1);
	    lTable.add(new Text(" : "),2,1);
	    lTable.add(this._fieldTrackLengthSek,2,1);
    nameTable.add(lTable,4,2);
	
	nameTable.add(new Text("Lagið:"),1,3);
	
	nameTable.add(_fieldAudioID,2,3);

	nameTable.add(new Text("Fela hljóðskrá:"),1,4);
	
	nameTable.add(_fieldAudioIsHidden,2,4);

    contentTable.add(nameTable,1,1);
	contentTable.setRowAlignment(1,Table.HORIZONTAL_ALIGN_CENTER);

    
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

    contentTable.add(t2,1,2);
	contentTable.setRowAlignment(2,Table.HORIZONTAL_ALIGN_CENTER);
    
    
	//Description
	Table descriptionTable = new Table(1,2);
	descriptionTable.setWidth("100%");
	//descriptionTable.setCellpadding(0);
	//descriptionTable.setCellspacing(0);
	//descriptionTable.setHeight(1,"32");
	descriptionTable.add(new Text("Um lagið"),1,1);
	descriptionTable.add(this._fieldDescription,1,2);
	
	contentTable.add(descriptionTable,1,3);
	contentTable.setRowAlignment(3,Table.HORIZONTAL_ALIGN_CENTER);
    
    

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

    String acTrackId = iwc.getParameter(_fieldNameTrackId);
    int trackId = -1;
    if(acTrackId != null && !"".equals(acTrackId)){
      trackId = Integer.parseInt(acTrackId);
    }

    String acAlbumId = iwc.getParameter(_fieldNameAlbumId);
    String acName = iwc.getParameter(_fieldNameTrackName);

    String acTrackNumber = iwc.getParameter(_fieldNameTrackNumber);

    String acDescription = iwc.getParameter(_fieldNameDescription);

    String acLengthMin = iwc.getParameter(_fieldNameTrackLengthMin);
    String acLengthSek = iwc.getParameter(_fieldNameTrackLengthSek);
    
	String acAudioID = iwc.getParameter(_fieldNameAudioID);
	String acAudioIsHidden = iwc.getParameter(_fieldNameAudioIsHidden);

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
    
    
    Integer audioID = null;
    if(acAudioID!=null&& !acAudioID.equals("")){
    	try{
			audioID = new Integer(acAudioID);
    	} catch(NumberFormatException nfe){
    		nfe.printStackTrace();
    	}
    }
    
	boolean audioIsHidden = false;
	if(acAudioIsHidden!=null && !acAudioIsHidden.equals("")){
		audioIsHidden = true;
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


    if(trackId > -1){
      AlbumCollectionBusiness.updateTrack(trackId,acName,acDescription,TrackNumber,null,null,trackLength,audioID,audioIsHidden,authorIDs,performerIDs,null);
    }else{
      AlbumCollectionBusiness.addTrack(acName,acDescription,TrackNumber,AlbumId,null,trackLength,audioID,audioIsHidden,authorIDs,performerIDs,null);
    }

  }

  public void main(IWContext iwc) throws Exception {

    if(iwc.getParameter("save") == null){
      myForm.empty();
      initFields(iwc);
      this.add(myForm);
      //updateFieldStatus(iwc);
      myForm.add(getElementsOredered(iwc));
    } else {
      this.saveTrack(iwc);
      this.close();
      this.setParentToReload();
    }

  }

}
