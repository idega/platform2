package com.idega.block.importer.presentation;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.importer.business.ImportBusiness;
import com.idega.block.importer.data.ImportFileClass;
import com.idega.block.importer.data.ImportFileRecord;
import com.idega.block.importer.data.ImportFileRecordHome;
import com.idega.block.importer.data.ImportHandler;
import com.idega.block.media.business.MediaBusiness;
import com.idega.business.IBOLookup;
import com.idega.core.data.ICFile;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.Group;
import com.idega.util.IWColor;

/**
 * <p>Title: Importer</p>
 * <p>Description: This is a block for managing,importing and keeping track of your import files.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Idega Software</p>
 * @author <a href="mailto:eiki@idega.is">Eirikur Sveinn Hrafnsson</a>
 * @version 1.0
 */

public class Importer extends Block {
	
  private String folderPath;
  private ICFile importFolder;  
  private boolean usingLocalFileSystem,selectFiles,importFiles,selectFileSystemFolder = false;
  private IWResourceBundle iwrb;
  private Group group = null;
  private String groupId = null;
  private Table frameTable = null;
  private IWColor headerColor = IWColor.getIWColorFromHex("#A0A0A0");
  
  private static final String ACTION_PARAMETER = "im_ac"; //action
  private static final String SELECT_FILES = "im_sf"; //select files action
  private static final String IMPORT_FILES = "im_if"; //import files action
  private static final String IMPORT_FILE_PATHS = "im_fp"; //list of local files
  private static final String IMPORT_FILE_IDS = "im_f_ids"; //list of files in database
    
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
  public void setLocalFolderPath(String path){
    this.folderPath = path;
    usingLocalFileSystem = true;
  }

  public String getLocalFolderPath(){
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
  
  
  public ICFile getImportFolder(){
    return this.importFolder;
  }

  private void parseAction(IWContext iwc){
  	
    if( iwc.isParameterSet(ACTION_PARAMETER) ){
      if( iwc.getParameter(ACTION_PARAMETER).equals(IMPORT_FILES) ){
        importFiles = true;
      }
      else if( iwc.getParameter(ACTION_PARAMETER).equals(SELECT_FILES) ){
        /*if( iwc.isParameterSet(NEW_FOLDER_PATH) ){
          this.setFolderPath(iwc.getParameter(NEW_FOLDER_PATH));
        }*/ //removed this because folders are now set at design time in the Builder not runtime
        selectFiles = true;
      }
      /*else if(  iwc.getParameter(ACTION_PARAMETER).equals(SELECT_NEW_FOLDER) ){
        selectFileSystemFolder = true;
      }*/
    }
    else{
     selectFiles = true;
    }
    
    //if importing into a specific group. Used for compatabilty to idegaWeb Member system
    groupId = iwc.getParameter(this.PARAMETER_GROUP_ID);

    if( groupId!=null ){
    	iwc.setSessionAttribute(this.PARAMETER_GROUP_ID,groupId);
    }
    
  }
  


  public void main(IWContext iwc) throws Exception {
    iwrb = this.getResourceBundle(iwc);

    parseAction(iwc);

    if( selectFiles ){
    	if( usingLocalFileSystem ){
			showLocalFileSystemSelection(iwc);
      	}
      	else if( importFolder!=null ){
      		showIWFileSystemSelection(iwc);      	
      	}
      	else{
      		add(iwrb.getLocalizedString("importer.no.folder.selected","No folder is selected. Open the properties window and select a folder."));	
      	}

    }
    else if( importFiles ){
		importFiles(iwc);		   
    }
    
    //removed this because folders are now set at design time in the Builder not runtime  
    /*
    Link selectFolderLink = new Link(iwrb.getLocalizedString("importer.select.folder","Select folder"));
    selectFolderLink.addParameter(ACTION_PARAMETER,SELECT_NEW_FOLDER);
    selectFolderLink.setAsImageButton(true);
    add(selectFolderLink);
    addBreak();*/
    
        //removed this because folders are now set at design time in the Builder not runtime
   /* else if( selectFileSystemFolder ){
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
    }*/
    

  }

	/**
	 * Method importFiles.
	 * @param iwc
	 */
	private void importFiles(IWContext iwc) throws Exception{
		 
		 
		 Table table = getFrameTable();
		 
		 Text header = new Text(iwrb.getLocalizedString("importer.importing","Importer : Importing"));
		 header.setBold();
		 table.add(header,1,1);
		 
		 Text done = new Text(iwrb.getLocalizedString("importer.done.importing","Done importing:"));
		 table.add(done,1,2);
		 
		 table.mergeCells(1,2,7,2);
		
	     String[] values = null;
	     if(usingLocalFileSystem) values = iwc.getParameterValues(IMPORT_FILE_PATHS);//for local file importing
	     else values = iwc.getParameterValues(IMPORT_FILE_IDS);
	     
	     String groupIDFromSession = (String)iwc.getSessionAttribute(this.PARAMETER_GROUP_ID);
	     String handler = iwc.getParameter(this.PARAMETER_IMPORT_HANDLER);
	     String fileClass = iwc.getParameter(this.PARAMETER_IMPORT_FILE);
	     
	     if(values!=null){
		      // for each file to import
		     for (int i = 0; i < values.length; i++) {
		        boolean success = false;
		        String path;
		        if(usingLocalFileSystem){
		        	path = values[i];
		        }
		        else{/**@todo read directly from the database or at least do this in the business class**/
		        	path = MediaBusiness.getCachedFileInfo(Integer.parseInt(values[i]),iwc.getApplication()).getRealPathToFile();
		        }
		        
		        if(groupIDFromSession!=null){
		        	success = getImportBusiness(iwc).importRecords(handler,fileClass,path,new Integer(groupIDFromSession));       
		        }
		        else{
		        	success = getImportBusiness(iwc).importRecords(handler,fileClass,path);
		        }		        
		        
		        
		        String status = (success)? iwrb.getLocalizedString("importer.success","finished!") : iwrb.getLocalizedString("importer.failure","failed!!");
		        Text fileStatus = new Text(path+" : "+status);
		        fileStatus.setBold();
		        
		        /*if(status){
		        	getImportBusiness(iwc).updateImportRecord(getImportBusiness(iwc));
		        	
		        }*/
		
		        table.addBreak(1,2);
		        table.add(fileStatus,1,2);
		     }
		     
		
		}
		else{
			table.add(new Text(iwrb.getLocalizedString("importer.no.file.selected","No file selected!")),1,2);
			addBreak();
			table.add(new BackButton(iwrb.getLocalizedString("importer.back","back")),7,3);
		}
		
		add(table);
	}


	/**
	 * Method showIWFileSystemSelection.
	 * @param iwc
	 */
	private void showIWFileSystemSelection(IWContext iwc) throws Exception{
		Table fileTable = getFrameTable();
        if( MediaBusiness.isFolder(importFolder) ){
        	
        	//do I have to do this?
        	ImportFileRecord folder = changeICFileToImportFileRecord(importFolder);
        	int fileCount = folder.getChildCount();
          	
          
        	if(fileCount>0){
	          	Iterator files = folder.getChildren();
	          	Form form = new Form();
	          	//name,size,creationdate(uploaddate),modificationdata(importdate),
	          	//imported(status),reportlink,checkbox
	          	
	          	fileTable.resize(7,fileCount+3);
	          	
	          	form.add(fileTable);
	          	
	          	
	          	//header
	          	Text heading = new Text(iwrb.getLocalizedString("importer.select.files.to.import","Importer : Select files to import"));
	          	heading.setBold();
	          	
	          	fileTable.add(new HiddenInput(ACTION_PARAMETER,IMPORT_FILES),1,1); 
	            fileTable.add(heading,1,1);
	            
	            Text name = new Text(iwrb.getLocalizedString("importer.filename","File name"));
	            name.setBold();
	            Text size = new Text(iwrb.getLocalizedString("importer.filesize","File size"));
	            size.setBold();
	            Text uploaded = new Text(iwrb.getLocalizedString("importer.creationdate","Uploaded"));
	            uploaded.setBold();
	            Text imported = new Text(iwrb.getLocalizedString("importer.modificationdate","Imported"));
	            imported.setBold();
	            Text status = new Text(iwrb.getLocalizedString("importer.status","Status"));
	            status.setBold();
	            Text report = new Text(iwrb.getLocalizedString("importer.report","Report"));
	            report.setBold();
	            	            
	            fileTable.add(name,1,2);
	            fileTable.add(size,2,2);
	            fileTable.add(uploaded,3,2);
	            fileTable.add(imported,4,2);
	            fileTable.add(status,5,2);
	            fileTable.add(report,6,2);
	            
	            //footer
	            Text importHandler = new Text(iwrb.getLocalizedString("importer.import.handler","Import handler : "));
	            importHandler.setBold();
	            
	            fileTable.add(importHandler, 1, fileCount+3);
	            fileTable.add(this.getImportHandlers(iwc), 2, fileCount+3);
	            
	            Text fileType = new Text(iwrb.getLocalizedString("importer.import.filetype","File type : "));
	            fileType.setBold();
	            
	            fileTable.add(fileType, 3, fileCount+3);
	            fileTable.add(this.getImportFileClasses(iwc), 4, fileCount+3);
	            
	            fileTable.add(new SubmitButton(iwrb.getLocalizedString("importer.import","Import")), 7, fileCount+3);
	            
	            
	            
	            //data	            
	            int row = 3;
	            while (files.hasNext()) {
					ImportFileRecord file = (ImportFileRecord) files.next();

					if( MediaBusiness.isFolder(file) ){//add support for folders within this?
						fileTable.add(file.getName()+iwrb.getLocalizedString("importer.folder"," (Folder)"),1,row);
					}
					else{//is a file
						boolean wasImported = file.hasBeenImported();
						fileTable.add(new Text( file.getName() ) ,1,row);
						fileTable.add(new Text( file.getFileSize().toString() ) ,2,row);
						fileTable.add(new Text( file.getCreationDate().toString() ) ,3,row);
						if(wasImported){
							fileTable.add(new Text( file.getModificationDate().toString() ) ,4,row);
							fileTable.add(new Text(iwrb.getLocalizedString("importer.imported","Imported")),5,row);
						}
						else{
							fileTable.add(new Text(iwrb.getLocalizedString("importer.not.imported","Not imported")),5,row);
						}
						
						if( !file.isLeaf() ){
							fileTable.add(new Text("Temp: has report!"),6,row);
						}
						
						fileTable.add(new CheckBox(IMPORT_FILE_IDS, ((Integer)file.getPrimaryKey()).toString() ),7,row);
					  
					}
					
					row++;
	            }


          add(form);//add the form
          	
          }
          else{
          	Text header = new Text(iwrb.getLocalizedString("importer","Importer"));
          	header.setBold();
          	
          	fileTable.add(header,1,1);
          	fileTable.add(iwrb.getLocalizedString("no.files","No files to import, please upload files first"),1,2);	
          	add(fileTable);
          }
          
          
        }
        else{
          Text header = new Text(iwrb.getLocalizedString("importer","Importer"));
          header.setBold();
          	
          fileTable.add(header,1,1);
          fileTable.add( iwrb.getLocalizedString("importer.not.a.folder","Selected import folder is a file! Please select a folder."),1,2 );
          add(fileTable);
        }		
	}


	/**
	 * Method showLocalFileSystemSelection.
	 * @param iwc
	 */
	private void showLocalFileSystemSelection(IWContext iwc) throws Exception{
	    File folder = new File(getLocalFolderPath());
	    Table fileTable = getFrameTable();

        if( folder.isDirectory() ){

          File[] files = folder.listFiles();
          
          fileTable.resize(2,files.length+4);
          
          Form form = new Form();
          form.add(fileTable);

		  Text headline = new Text(iwrb.getLocalizedString("importer.select_files","Select files to import."));
		  headline.setBold();
		  		  
          fileTable.add(headline,1,1);
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
          Text header = new Text(iwrb.getLocalizedString("importer","Importer"));
          header.setBold();
          	
          fileTable.add(header,1,1);
          
          
          fileTable.add( iwrb.getLocalizedString("importer.nosuchfolder","No such folder."),1,2 );
          fileTable.add( new BackButton(iwrb.getLocalizedString("importer.try.again","Try again")),7,3 );
          add(fileTable);
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
  	
  	private ImportFileRecord changeICFileToImportFileRecord(ICFile folder) throws Exception{
  		return (ImportFileRecord) ((ImportFileRecordHome)com.idega.data.IDOLookup.getHome(ImportFileRecord.class)).findByPrimaryKey(folder.getPrimaryKey());
  	}
  	
  	
  	private Table getFrameTable(){
  		if(frameTable==null){
  			frameTable = new Table(7,3);
          	frameTable.setWidth(Table.HUNDRED_PERCENT);
          	frameTable.mergeCells(1,1,7,1);//merge the header
          	//header color
          	frameTable.setRowColor(1,headerColor.getHexColorString());
  		}
  		
  		return (Table) frameTable.clone();
  		
  	}
  	
  	public void setHeaderColor(IWColor color){
  		headerColor = color;	
  		
  	}

}