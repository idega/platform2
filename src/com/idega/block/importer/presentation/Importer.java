package com.idega.block.importer.presentation;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.importer.business.ImportBusiness;
import com.idega.block.importer.data.ImportFile;
import com.idega.block.importer.data.ImportFileClass;
import com.idega.block.importer.data.ImportHandler;
import com.idega.block.media.business.MediaBusiness;
import com.idega.business.IBOLookup;
import com.idega.core.data.ICFile;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;
import com.idega.user.business.GroupBusiness;
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
  private String folderPath;
  private ICFile importFolder;  
  private boolean usingLocalFileSystem,selectFiles,importFiles,selectFileSystemFolder = false;
  private IWResourceBundle iwrb;
  private Group group = null;

  private static final String ACTION_PARAMETER = "im_ac"; //action
  private static final String SELECT_FILES = "im_sf"; //select files action
  private static final String IMPORT_FILES = "im_if"; //import files action
  private static final String IMPORT_FILE_PATHS = "im_fp"; //list of files
  private static final String SELECT_NEW_FOLDER = "im_snf"; //new folder overrides builder parameter action
  private static final String NEW_FOLDER_PATH = "im_nfp"; //new folder path
  
  private static final String PARAMETER_GROUP_ID = "ic_group_id";
  private static final String PARAMETER_IMPORT_HANDLER = "im_imh";
  private static final String PARAMETER_IMPORT_FILE = "im_imf";

  public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.importer";



  public Importer() {
  }


/**
 * Method setFolderPath. Use this set method if you want to select files from the local filesystem.
 * @param path
 */
  public void setFolderPath(String path){
    this.folderPath = path;
    usingLocalFileSystem = true;
  }

  public String getFolderPath(){
    return this.folderPath;
  }
  
/**
 * Method setImportFolder. Use this method if you want use the idegaWeb filesystem.
 * @param folder
 */
  public void setImportFolder(ICFile folder){
  	this.importFolder = folder;	
  	usingLocalFileSystem = false;
  	    
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
        selectFileSystemFolder = true;
      }
    }
    else{
     selectFiles = true;
    }

  }
  


  public void main(IWContext iwc) throws Exception {
    iwrb = this.getResourceBundle(iwc);

    parseAction(iwc);
    
    String groupId = iwc.getParameter(this.PARAMETER_GROUP_ID);

    if( groupId!=null ){
    	iwc.setSessionAttribute(this.PARAMETER_GROUP_ID,groupId);
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
          Table fileTable = new Table(2,files.length+4);
          
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

		  fileTable.add(iwrb.getLocalizedString("importer.select.import.handler","Select import handler") ,1,files.length+2);
		  fileTable.add(this.getImportHandlers(iwc),2,files.length+2);

		  fileTable.add(iwrb.getLocalizedString("importer.select.import.file.type","Select file type") ,1,files.length+3);
		  fileTable.add(this.getImportFileClasses(iwc),2,files.length+3);


          fileTable.add(new SubmitButton(),2,files.length+4);
          
          

          add(form);

        }
        else{
          add( iwrb.getLocalizedString("importer.nosuchfolder","No such folder.") );
          add( new BackButton(iwrb.getLocalizedString("importer.try.again","Try again")) );
        }


      }

    }
    else if( selectFileSystemFolder ){
      Form form = new Form();
      form.add(new HiddenInput(this.ACTION_PARAMETER,this.SELECT_FILES));
      String path = getFolderPath();
      if( path == null ){
       path = iwc.getApplication().getApplicationRealPath()+"import";
      }
      form.add(new TextInput(NEW_FOLDER_PATH,path) );
      form.add(new SubmitButton());
      add(form);
      add( new BackButton(iwrb.getLocalizedString("importer.try.again","Try again")) ); 


    }
    else if( importFiles ){
      add(iwrb.getLocalizedString("importer.done.importing","Done importing:"));

      String[] values = iwc.getParameterValues(IMPORT_FILE_PATHS);
      String groupIDFromSession = (String)iwc.getSessionAttribute(this.PARAMETER_GROUP_ID);
      String handler = iwc.getParameter(this.PARAMETER_IMPORT_HANDLER);
      String fileClass = iwc.getParameter(this.PARAMETER_IMPORT_FILE);
      
     
	  // for each file to import
      for (int i = 0; i < values.length; i++) {
      /** @todo make support for uploaded files
       */
   
        boolean success = false;
        
        if(groupIDFromSession!=null){
        	success = getImportBusiness(iwc).importRecords(handler,fileClass,values[i],new Integer(groupIDFromSession));       
        }
        else{
        	success = getImportBusiness(iwc).importRecords(handler,fileClass,values[i]);
        }
        
        //MediaBusiness.getCachedFileInfo(icfileid,iwc)
        
        String status = (success)? iwrb.getLocalizedString("importer.success","finished!") : iwrb.getLocalizedString("importer.failure","failed!!");
        Text fileStatus = new Text(values[i]+" : "+status);
        fileStatus.setBold();

		addBreak();
        add(fileStatus);
        addBreak();
        
      }





    }

  }
  
  	public DropdownMenu getImportHandlers(IWContext iwc) throws RemoteException{
  		DropdownMenu menu = new DropdownMenu(this.PARAMETER_IMPORT_HANDLER);
	
    	Collection col = getImportBusiness(iwc).getImportHandlers();
  		Iterator iter = col.iterator();
  		// should the business class to this for me?
  		while (iter.hasNext()) {
			ImportHandler element = (ImportHandler) iter.next();
			menu.addMenuElement( element.getClassName(), element.getName() );
		}
  		return menu;
  	}
  	
  	public DropdownMenu getImportFileClasses(IWContext iwc) throws RemoteException{
  		DropdownMenu menu = new DropdownMenu(this.PARAMETER_IMPORT_FILE);
  		
  		Collection col = getImportBusiness(iwc).getImportFileTypes();
  		Iterator iter = col.iterator();
  		// should the business class to this for me?
  		while (iter.hasNext()) {
			ImportFileClass element = (ImportFileClass) iter.next();
			menu.addMenuElement( element.getClassName() , element.getName() );
		}
		
		return menu;
  		
  	}
  	
  	public ImportBusiness getImportBusiness(IWContext iwc) throws RemoteException{
  		return (ImportBusiness) IBOLookup.getServiceInstance(iwc,ImportBusiness.class);	
  		
  	}

}