package com.idega.block.importer.presentation;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWResourceBundle;
import java.io.File;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.user.business.GroupBusiness;
import com.idega.presentation.text.*;
import com.idega.block.importer.business.*;
import com.idega.block.importer.data.*;
import com.idega.user.data.Group;

/**
 * <p>Title: IdegaWeb classes</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Idega Software</p>
 * @author <a href="mailto:eiki@idega.is"> Eirikur Sveinn Hrafnsson</a>
 * @version 1.0
 */

public class Importer extends Window {
  String folderPath;
  boolean selectFiles,importFiles,selectFolder = false;
  IWResourceBundle iwrb;
      Group group= null;

  private final String ACTION_PARAMETER = "se_im_ac"; //action
  private final String SELECT_FILES = "se_im_sf"; //select files action
  private final String IMPORT_FILES = "se_im_if"; //import files action
  private final String IMPORT_FILE_PATHS = "se_im_fp"; //list of files
  private final String SELECT_NEW_FOLDER = "se_im_snf"; //new folder overrides builder parameter action
  private final String NEW_FOLDER_PATH = "se_im_nfp"; //new folder path
  
    public static final String PARAMETERSTRING_GROUP_ID = "ic_group_id";

  public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.importer";

  public Importer() {
  }

  public void setFolderPath(String path){
    this.folderPath = path;
  }

  public String getFolderPath(){
    return this.folderPath;
  }

  private void parseAction(IWContext iwc){
    if( iwc.isParameterSet(ACTION_PARAMETER) ){
      if( iwc.getParameter(ACTION_PARAMETER).equals(IMPORT_FILES) ){
        importFiles = true;
      }
      else if( iwc.getParameter(ACTION_PARAMETER).equals(SELECT_FILES) ){
        if( iwc.isParameterSet(NEW_FOLDER_PATH) ){
          this.setFolderPath(iwc.getParameter(NEW_FOLDER_PATH));
        }
        selectFiles = true;
      }
      else if(  iwc.getParameter(ACTION_PARAMETER).equals(SELECT_NEW_FOLDER) ){
        selectFolder = true;
      }
    }
    else{
     selectFiles = true;
    }

  }
  
   public GroupBusiness getGroupBusiness(IWContext iwc) throws Exception{
    return (GroupBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc,GroupBusiness.class);
  }

  public void main(IWContext iwc) throws Exception {
    iwrb = this.getResourceBundle(iwc);

    parseAction(iwc);
    
    String groupId = iwc.getParameter(this.PARAMETERSTRING_GROUP_ID);

    if( groupId!=null ){

    	
    	group =  getGroupBusiness(iwc).getGroupHome().findByPrimaryKey(new Integer(groupId));
    	    	iwc.setSessionAttribute("gruppan",group);
    }

    Link selectFolderLink = new Link(iwrb.getLocalizedString("importer.select.folder","Select folder"));
    selectFolderLink.addParameter(ACTION_PARAMETER,SELECT_NEW_FOLDER);
    selectFolderLink.setAsImageButton(true);
    add(selectFolderLink);
    addBreak();
    

    if( selectFiles ){
      if( this.getFolderPath()!=null ){
        File folder = new File(getFolderPath());

        if( folder.isDirectory() ){

          File[] files = folder.listFiles();
          Table fileTable = new Table(2,files.length+2);
          Form form = new Form();
          form.add(fileTable);

          fileTable.add(iwrb.getLocalizedString("importer.select_files","Select files to import."),1,1);
          fileTable.add(new HiddenInput(ACTION_PARAMETER,IMPORT_FILES),2,1);

          for (int i = 0; i < files.length; i++) {
            if( !files[i].isDirectory() ){
             fileTable.add(files[i].getName(),1,i+2);
             fileTable.add(new CheckBox(this.IMPORT_FILE_PATHS,files[i].getAbsolutePath()),2,i+2);
            }
            else{
              fileTable.add(files[i].getName(),1,i+2);
              fileTable.add(iwrb.getLocalizedString("importer.is.a.folder","Folder"),2,i+2);
            }
         }

          fileTable.add(new SubmitButton(),2,files.length+2);

          add(form);

        }
        else{
          add( iwrb.getLocalizedString("importer.nosuchfolder","No such folder.") );
          add( new BackButton(iwrb.getLocalizedString("importer.try.again","Try again")) );
        }


      }

    }
    else if( selectFolder ){
      Form form = new Form();
      form.add(new HiddenInput(this.ACTION_PARAMETER,this.SELECT_FILES));
      String path = getFolderPath();
      if( path == null ){
       path = iwc.getApplication().getApplicationRealPath();
      }
      form.add(new TextInput(NEW_FOLDER_PATH,path) );
      form.add(new SubmitButton());
      add(form);
      add( new BackButton(iwrb.getLocalizedString("importer.try.again","Try again")) );


    }
    else if( importFiles ){
      add(iwrb.getLocalizedString("importer.done.importing","Done importing:"));
     // NackaImportFile file;

      String[] values = iwc.getParameterValues(IMPORT_FILE_PATHS);
      ImportBusiness biz = (ImportBusiness) IBOLookup.getServiceInstance(iwc,ImportBusiness.class);
      //ImportBusinessBean biz = new ImportBusinessBean();

      for (int i = 0; i < values.length; i++) {
      /** @todo make a dropdown of possible importers and support for uploaded files
       *
       */
        //NackaImportFile importFile = new NackaImportFile(new File(values[i]));
        /*
         */
         
         boolean success = false;
         
         group =     	(Group)iwc.getSessionAttribute("gruppan");
        ColumnSeparatedImportFile importFile = new ColumnSeparatedImportFile(new File(values[i]));
        if(group!=null){
        	
        	
        success = biz.importRecords(group,importFile);
       
       
        }
        else{
        	success = biz.importRecords(importFile);
        }
        addBreak();

        String status = (success)? iwrb.getLocalizedString("importer.success","finished!") : iwrb.getLocalizedString("importer.failure","failed!!");

        Text fileStatus = new Text(values[i]+" : "+status);
        fileStatus.setBold();

        add(fileStatus);


        addBreak();
      }





    }

  }

}