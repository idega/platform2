package com.idega.block.importer.presentation;
import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.CreateException;
import com.idega.block.importer.business.ImportBusiness;
import com.idega.block.importer.data.ImportFileRecord;
import com.idega.block.importer.data.ImportFileRecordHome;
import com.idega.block.media.business.MediaBusiness;
import com.idega.block.media.business.MediaConstants;
import com.idega.block.media.presentation.MediaChooserWindow;
import com.idega.business.IBOLookup;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileBMPBean;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.io.UploadFile;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.FileInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.StyledButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.Group;
import com.idega.user.presentation.GroupPropertyWindow;
import com.idega.util.IWColor;
import com.idega.util.IWTimestamp;
/**
 * <p>Title: Importer</p>
 * <p>Description: This is a block for managing,importing and keeping track of your import files.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Idega Software</p>
 * @author <a href="mailto:eiki@idega.is">Eirikur Sveinn Hrafnsson</a>
 * @version 1.0
 */
public class Importer extends StyledIWAdminWindow {
	
	private String folderPath;
	private ICFile importFolder;
	private boolean usingLocalFileSystem, selectFiles, importFiles, selectFileSystemFolder , isInApplication= false;
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

	
	public static final String PARAMETER_GROUP_ID = "ic_group_id";
	public static final String PARAMETER_IMPORT_HANDLER = "im_imh";
	public static final String PARAMETER_IMPORT_FILE = "im_imf";
	private static final String PARAMETER_SORT_BY = "im_sb";
	
	public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.importer";
	
	private final static String HELP_TEXT_KEY = "importer";
	
	
	private String importHandler;
	private String importFile;
	public Importer() {
		this.setAllMargins(0);
		setHeight(400);
		setWidth(400);
	}
	/**
	 * Method setFolderPath. Use this set method if you want to select files from the local filesystem.
	 * @param path
	 */
	public void setLocalFolderPath(String path) {
		this.folderPath = path;
		this.usingLocalFileSystem = true;
	}
	public String getLocalFolderPath() {
		return this.folderPath;
	}
	/**
	 * Method setImportFolder. Use this method if you want use the idegaWeb filesystem.
	 * @param folder
	 */
	public void setImportFolder(ICFile folder) {
		this.importFolder = folder;
		this.usingLocalFileSystem = false;
	}
	
	public ICFile getImportFolder() {
		return this.importFolder;
	}
	
	private void parseAction(IWContext iwc) {
		
		if( (getICObjectInstanceID() < 1) && iwc.isParameterSet(PARAMETER_IMPORT_HANDLER) && iwc.isParameterSet(PARAMETER_IMPORT_FILE) ){
			this.isInApplication = true;
		}
		
		if (iwc.isParameterSet(ACTION_PARAMETER)) {
			if (iwc.getParameter(ACTION_PARAMETER).equals(IMPORT_FILES)) {
				this.importFiles = true;
			}
			else if (iwc.getParameter(ACTION_PARAMETER).equals(SELECT_FILES)) {
				/*if( iwc.isParameterSet(NEW_FOLDER_PATH) ){
				  this.setFolderPath(iwc.getParameter(NEW_FOLDER_PATH));
				}*/ //removed this because folders are now set at design time in the Builder not runtime
				this.selectFiles = true;
			}
			/*else if(  iwc.getParameter(ACTION_PARAMETER).equals(SELECT_NEW_FOLDER) ){
			  selectFileSystemFolder = true;
			}*/
		}
		else {
			this.selectFiles = true;
		}
		//if importing into a specific group. Used for compatabilty to idegaWeb Member system
		this.groupId = iwc.getParameter(GroupPropertyWindow.PARAMETERSTRING_GROUP_ID);
		if (this.groupId != null) {
			iwc.setSessionAttribute(PARAMETER_GROUP_ID, this.groupId);
		}

		this.importHandler = iwc.getParameter(PARAMETER_IMPORT_HANDLER);
		this.importFile = iwc.getParameter(PARAMETER_IMPORT_FILE);
		
	}
	
	public void main(IWContext iwc) throws Exception {
		this.iwrb = this.getResourceBundle(iwc);
		setTitle(this.iwrb.getLocalizedString("Importer.title", "Importer"));
		addTitle(this.iwrb.getLocalizedString("Importer.title", "Importer"), TITLE_STYLECLASS);
		parseAction(iwc);
		if (this.selectFiles) {
			if (this.usingLocalFileSystem) {
				showLocalFileSystemSelection(iwc);
			}
			else if (this.importFolder != null) {
				showIWFileSystemSelection(iwc);
			}
			else if (this.isInApplication) {
				showFileUploader(iwc);
			}
			else {
				if( getICObjectInstanceID() < 1 ) {
					add(this.iwrb.getLocalizedString("importer.no.handler.or.importfile", "No ImportFileHandler or ImportFile specified."));
				}
				else {
					add(this.iwrb.getLocalizedString("importer.no.folder.selected", "No folder is selected. Open the properties window and select a folder."));
				}
			}
		}
		else if (this.importFiles) {
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
	
	private void showFileUploader(IWContext iwc) {
		Form form = new Form();
		form.setMultiPart();
		//SimpleFileChooser chooser = new SimpleFileChooser(form, IMPORT_FILE_IDS);

		Table mainTable = new Table();
		mainTable.setWidth(Table.HUNDRED_PERCENT);
		mainTable.setHeight(100);
		mainTable.setCellpadding(0);
		mainTable.setCellspacing(0);

		Table table = new Table(2,3);
		table.setStyleClass(MAIN_STYLECLASS);
		table.setCellpaddingAndCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(60);
		table.setCellpadding(0);
		table.setCellspacing(5);
		FileInput chooser = new FileInput();
		Help help = getHelp(HELP_TEXT_KEY);
		SubmitButton confirm = new SubmitButton(this.iwrb.getLocalizedString("confirm","Confirm"));
		StyledButton styledConfirm = new StyledButton(confirm);
		CloseButton close = new CloseButton(this.iwrb.getLocalizedString("close", "Close"));
		StyledButton styledClose = new StyledButton(close);
		
		table.add(this.iwrb.getLocalizedString("importer.select_file", "Select a file to import"+":"),1,1);
		table.add(new HiddenInput(ACTION_PARAMETER, IMPORT_FILES),1,2);
		table.add(new HiddenInput(PARAMETER_IMPORT_HANDLER, this.importHandler),1,2);
		table.add(new HiddenInput(PARAMETER_IMPORT_FILE, this.importFile),1,2);
		table.add(chooser,1,2);
		
		Table buttonTable = new Table();
		buttonTable.add(styledConfirm,1,1);
		buttonTable.add(styledClose,2,1);

		Table bottomTable = new Table();
		bottomTable.setCellpadding(0);
		bottomTable.setCellspacing(5);
		bottomTable.setWidth(Table.HUNDRED_PERCENT);
		bottomTable.setStyleClass(MAIN_STYLECLASS);
		bottomTable.add(help,1,1);
		bottomTable.setAlignment(2,1,Table.HORIZONTAL_ALIGN_RIGHT);
		bottomTable.add(buttonTable,2,1);
		
		mainTable.setVerticalAlignment(1,1,Table.VERTICAL_ALIGN_TOP);
		mainTable.setVerticalAlignment(1,3,Table.VERTICAL_ALIGN_TOP);
		mainTable.add(table,1,1);
		mainTable.setHeight(2, 5);
		mainTable.add(bottomTable,1,3);
		
		form.add(mainTable);
		add(form,iwc);
	}
	/**
	 * Method importFiles.
	 * @param iwc
	 */
	private void importFiles(IWContext iwc) throws Exception {
		Form form = new Form();
		Table mainTable = new Table();
		mainTable.setCellspacing(0);
		mainTable.setCellpadding(0);
		mainTable.setWidth(Table.HUNDRED_PERCENT);
		mainTable.setHeight(2, 5);
		Table headerTable = getFrameTable();
		headerTable.setCellpadding(0);
		headerTable.setCellspacing(5);
		Table contentTable = getFrameTable();
		contentTable.setCellpadding(0);
		contentTable.setCellspacing(5);
		Table bottomTable = getFrameTable();
		bottomTable.setCellpadding(0);
		bottomTable.setCellspacing(5);
		bottomTable.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_LEFT);
		bottomTable.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		Table buttonTable = new Table();
		buttonTable.setCellpadding(0);
		buttonTable.setCellspacing(0);
		buttonTable.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		
		int row = 1;
		Text done = new Text(this.iwrb.getLocalizedString("importer.done.importing", "Done importing:"));
		headerTable.add(done, 1, row++);
		String[] values = null;
		if (this.usingLocalFileSystem){
			values = iwc.getParameterValues(IMPORT_FILE_PATHS); //for local file importing
		}
		else if(this.isInApplication){
			if(iwc.isUploadedFileSet()) {
				UploadFile file = iwc.getUploadedFile();
				String[] temp = {file.getAbsolutePath()};
				values = temp;
			}
		}
		else{
			values = iwc.getParameterValues(IMPORT_FILE_IDS);
		}
		String groupIDFromSession = (String) iwc.getSessionAttribute(this.PARAMETER_GROUP_ID);
		String handler = iwc.getParameter(this.PARAMETER_IMPORT_HANDLER);
		String fileClass = iwc.getParameter(this.PARAMETER_IMPORT_FILE);
		if (values != null) {
			// for each file to import
			for (int i = 0; i < values.length; i++) {
				boolean success = false;
				String path;
				if (this.usingLocalFileSystem || this.isInApplication ) {
					path = values[i];
				}
				else { /**@todo read directly from the database or at least do this in the business class**/
					path = MediaBusiness.getCachedFileInfo(Integer.parseInt(values[i]), iwc.getIWMainApplication()).getRealPathToFile();
				}
				//todo get failed records and associate with the import
				//handler. add import methods that take in the file id!
				List failedRecords = new ArrayList();
				if (groupIDFromSession != null) {
					success = getImportBusiness(iwc).importRecords(handler, fileClass, path, new Integer(groupIDFromSession), iwc, failedRecords);
				}
				else {
					success = getImportBusiness(iwc).importRecords(handler, fileClass, path, iwc);
				}
				String status = null;
				if (!success) {
					status = this.iwrb.getLocalizedString("importer.failure", "Failed");
				} else if (failedRecords.size() != 0) {
					status = this.iwrb.getLocalizedString("importer.not_all imported", "Not all records imported");
				} else {
					status = this.iwrb.getLocalizedString("importer.success", "Success");
				}
				Text fileStatus = new Text(path.substring(path.lastIndexOf(com.idega.util.FileUtil.getFileSeparator())+1,path.length()) + ": " + status);
				if (success && !this.usingLocalFileSystem &&!this.isInApplication) {
					//@todo move to a business method
					Integer id = new Integer(values[i]);
					ImportFileRecord record =
						(ImportFileRecord) ((ImportFileRecordHome) com.idega.data.IDOLookup.getHome(ImportFileRecord.class)).findByPrimaryKey(id);
					record.setModificationDate(IWTimestamp.getTimestampRightNow());
					record.setAsImported();
					record.store();
					//getImportBusiness(iwc).updateImportRecord(getImportBusiness(iwc));
				}
				headerTable.add(fileStatus, 1, row++);
				row = 1;
				if (failedRecords.size() != 0) {
				    Text failed = new Text(this.iwrb.getLocalizedString("importer.number_of_failed_records", "Number of failed record was:")+" "+String.valueOf(failedRecords.size()));
					contentTable.add(failed, 1, row++);
					for (int j=0;j<failedRecords.size();j++) {
					    failed = new Text(String.valueOf(failedRecords.get(j)));
					    failed.setBold(false);
					    contentTable.add(failed, 1, row++);
					}
				}
			}
			if(this.isInApplication){
				buttonTable.add(new StyledButton(new CloseButton(this.iwrb.getLocalizedString("importer.close", "close"))), 1, 1); 
				setParentToReload();
			}
			else {
				buttonTable.add(new StyledButton(new BackButton(this.iwrb.getLocalizedString("importer.back", "back"))), 1, 1);
			}
			
			mainTable.add(contentTable, 1, 3);
			mainTable.setHeight(4, 5);
		}
		else {
			headerTable.add(new Text(this.iwrb.getLocalizedString("importer.failure", "Failed")+": "+this.iwrb.getLocalizedString("importer.no.file.selected", "No file selected!")), 1, row++);
			buttonTable.add(new StyledButton(new BackButton(this.iwrb.getLocalizedString("importer.back", "back"))), 1, 1);
		}
		Help help = getHelp(HELP_TEXT_KEY);
		bottomTable.add(help, 1, 1);
		bottomTable.add(buttonTable,2,1);
		mainTable.add(headerTable, 1, 1);
		mainTable.setHeight(2, 5);
		mainTable.add(bottomTable, 1, 5);
		form.add(mainTable);
		add(form,iwc);
	}
	/**
	 * Method showIWFileSystemSelection.
	 * @param iwc
	 */
	private void showIWFileSystemSelection(IWContext iwc) throws Exception {
		Table fileTable = getFrameTable();
		if (MediaBusiness.isFolder(this.importFolder)) {
			MediaBusiness.removeMediaIdFromSession(iwc);
			//do I have to do this?
			ImportFileRecord folder = changeICFileToImportFileRecord(this.importFolder);
			int fileCount = folder.getChildCount();
			if (fileCount > 0) {
				Iterator files;
				String sortBy = iwc.getParameter(PARAMETER_SORT_BY);
				if (sortBy == null || sortBy.equals("")) {
					files = folder.getChildrenIterator();
				} else {
					files = folder.getChildrenIterator(sortBy);
				}
				Form form = new Form();
				//name,size,creationdate(uploaddate),modificationdata(importdate),
				//imported(status),reportlink,checkbox
				fileTable.resize(7, fileCount + 3);
				form.add(fileTable);
				//header
				Text heading = new Text(this.iwrb.getLocalizedString("importer.select.files.to.import", "Importer : Select files to import"));
				heading.setBold();
				fileTable.add(new HiddenInput(ACTION_PARAMETER, IMPORT_FILES), 1, 1);
				fileTable.add(heading, 1, 1);
				Text name = new Text(this.iwrb.getLocalizedString("importer.filename", "File name"));
				name.setBold();
				Text size = new Text(this.iwrb.getLocalizedString("importer.filesize", "File size"));
				size.setBold();
				Text uploaded = new Text(this.iwrb.getLocalizedString("importer.creationdate", "Uploaded"));
				uploaded.setBold();
				Text imported = new Text(this.iwrb.getLocalizedString("importer.modificationdate", "Imported"));
				imported.setBold();
				Text status = new Text(this.iwrb.getLocalizedString("importer.status", "Status"));
				status.setBold();
				Text report = new Text(this.iwrb.getLocalizedString("importer.report", "Report"));
				report.setBold();
				Link sortName = new Link(name);
				if (!ICFileBMPBean.getColumnNameName().equals(sortBy)) {
					sortName.addParameter(PARAMETER_SORT_BY, ICFileBMPBean.getColumnNameName());
				}
				Link sortUploaded = new Link(uploaded);
				if (!ICFileBMPBean.getColumnNameCreationDate().equals(sortBy)) {
					sortUploaded.addParameter(PARAMETER_SORT_BY, ICFileBMPBean.getColumnNameCreationDate());
				}
				Link sortImported = new Link(imported);
				if (!ICFileBMPBean.getColumnNameModificationDate().equals(sortBy)) {
					sortImported.addParameter(PARAMETER_SORT_BY, ICFileBMPBean.getColumnNameModificationDate());
				}
				
				
				fileTable.add(sortName, 1, 2);
				fileTable.add(size, 2, 2);
				fileTable.add(sortUploaded, 3, 2);
				fileTable.add(sortImported, 4, 2);
				fileTable.add(status, 5, 2);
				fileTable.add(report, 6, 2);
				//footer
				Text importHandler = new Text(this.iwrb.getLocalizedString("importer.import.handler", "Import handler : "));
				importHandler.setBold();
				fileTable.add(importHandler, 1, fileCount + 3);
				fileTable.add(getImportBusiness(iwc).getImportHandlers(iwc,PARAMETER_IMPORT_HANDLER), 2, fileCount + 3);
				Text fileType = new Text(this.iwrb.getLocalizedString("importer.import.filetype", "File type : "));
				fileType.setBold();
				fileTable.add(fileType, 3, fileCount + 3);
				fileTable.add(getImportBusiness(iwc).getImportFileClasses(iwc,this.PARAMETER_IMPORT_FILE), 4, fileCount + 3);
				Link upload = new Link(this.iwrb.getLocalizedString("importer.upload", "Upload"));
				upload.setWindowToOpen(MediaChooserWindow.class);
				upload.setAsImageButton(true);
				upload.addParameter(MediaConstants.MEDIA_ACTION_RELOAD, "TRUE");
				upload.addParameter(MediaBusiness.getMediaParameterNameInSession(iwc), ((Integer) folder.getPrimaryKey()).intValue());
				SubmitButton importIt = new SubmitButton(this.iwrb.getLocalizedString("importer.import", "Import"));
				importIt.setAsImageButton(true);
				fileTable.add(upload, 6, fileCount + 3);
				fileTable.add(importIt, 7, fileCount + 3);
				//data	            
				int row = 3;
				while (files.hasNext()) {
					ImportFileRecord file = (ImportFileRecord) files.next();
					if (MediaBusiness.isFolder(file)) { //add support for folders within this?
						fileTable.add(file.getName() + this.iwrb.getLocalizedString("importer.folder", " (Folder)"), 1, row);
					}
					else { //is a file
						boolean wasImported = file.hasBeenImported();
						fileTable.add(new Text(file.getName()), 1, row);
						fileTable.add(new Text(file.getFileSize().toString()), 2, row);
						fileTable.add(new Text(file.getCreationDate().toString()), 3, row);
						if (wasImported) {
							fileTable.add(new Text(file.getModificationDate().toString()), 4, row);
							fileTable.add(new Text(this.iwrb.getLocalizedString("importer.imported", "Imported")), 5, row);
						}
						else {
							fileTable.add(new Text(this.iwrb.getLocalizedString("importer.not.imported", "Not imported")), 5, row);
						}
						addReportInfo(iwc, file.getName(), fileTable, 6, row);

						fileTable.add(new CheckBox(IMPORT_FILE_IDS, ((Integer)file.getPrimaryKey()).toString() ),7,row);
					}
					row++;
				}
				add(form); //add the form
			}
			else {
				Text header = new Text(this.iwrb.getLocalizedString("importer", "Importer"));
				header.setBold();
				fileTable.add(header, 1, 1);
				fileTable.add(this.iwrb.getLocalizedString("no.files", "No files to import, please upload files first"), 1, 2);
				Link upload = new Link(this.iwrb.getLocalizedString("importer.upload", "Upload"));
				upload.setWindowToOpen(MediaChooserWindow.class);
				upload.setAsImageButton(true);
				upload.addParameter(MediaConstants.MEDIA_ACTION_RELOAD, "TRUE");
				upload.addParameter(MediaBusiness.getMediaParameterNameInSession(iwc), ((Integer) folder.getPrimaryKey()).intValue());
				fileTable.add(upload, 7, 3);
				add(fileTable);
			}
		}
		else {
			Text header = new Text(this.iwrb.getLocalizedString("importer", "Importer"));
			header.setBold();
			fileTable.add(header, 1, 1);
			fileTable.add(this.iwrb.getLocalizedString("importer.not.a.folder", "Selected import folder is a file! Please select a folder."), 1, 2);
			add(fileTable);
		}
	}
	/**
	 * Method showLocalFileSystemSelection.
	 * @param iwc
	 */
	private void showLocalFileSystemSelection(IWContext iwc) throws Exception {
		File folder = new File(getLocalFolderPath());
		Table fileTable = getFrameTable();
		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			fileTable.resize(3, files.length + 4);
			Form form = new Form();
			form.add(fileTable);
			Text headline = new Text(this.iwrb.getLocalizedString("importer.select_files", "Select files to import."));
			headline.setBold();
			fileTable.add(headline, 1, 1);
			fileTable.add(new HiddenInput(ACTION_PARAMETER, IMPORT_FILES), 1, 1);
			for (int i = 0; i < files.length; i++) {
				if (!files[i].isDirectory()) {
					fileTable.add(files[i].getName(), 1, i + 2);
					fileTable.add(new CheckBox(this.IMPORT_FILE_PATHS, files[i].getAbsolutePath()), 2, i + 2);
					addReportInfo(iwc, files[i].getName(), fileTable, 3, i + 2);
				}
				else {
					fileTable.add(files[i].getName(), 1, i + 2);
					fileTable.add(this.iwrb.getLocalizedString("importer.is.a.folder", "Folder"), 2, i + 2);
				}
			}
			fileTable.add(this.iwrb.getLocalizedString("importer.select.import.handler", "Select import handler"), 1, files.length + 2);
			fileTable.add(getImportBusiness(iwc).getImportHandlers(iwc,PARAMETER_IMPORT_HANDLER), 2, files.length + 2);
			fileTable.add(this.iwrb.getLocalizedString("importer.select.import.file.type", "Select file type"), 1, files.length + 3);
			fileTable.add(getImportBusiness(iwc).getImportFileClasses(iwc,this.PARAMETER_IMPORT_FILE), 2, files.length + 3);
			fileTable.add(new SubmitButton(), 2, files.length + 4);
			add(form);
		}
		else {
			Text header = new Text(this.iwrb.getLocalizedString("importer", "Importer"));
			header.setBold();
			fileTable.add(header, 1, 1);
			fileTable.add(this.iwrb.getLocalizedString("importer.nosuchfolder", "No such folder."), 1, 2);
			fileTable.add(new BackButton(this.iwrb.getLocalizedString("importer.try.again", "Try again")), 7, 3);
			add(fileTable);
		}
	}

	private void addReportInfo(IWContext iwc, String fileName, Table fileTable, int column, int row) throws RemoteException, CreateException {
		ICFile reportFile = getImportBusiness(iwc).getReportFolder(fileName, false);
		if (reportFile != null) {
			Iterator reports = reportFile.getChildrenIterator();
			ICFile report;
			if (reports != null && reports.hasNext()) {
				while (reports.hasNext()) {
					report = (ICFile) reports.next();
					Link link = new Link( ((Integer) report.getPrimaryKey()).intValue());
					link.setText(report.getName());
					link.setOutgoing(true);
					
					fileTable.add(link,column,row);
				}
			} else if (reports != null) {
				// Backwards thingy
				if (reportFile != null) {
					fileTable.add(new Text(reportFile.getName() + " available"),column,row);
				}
			}
		}
	}

	public ImportBusiness getImportBusiness(IWContext iwc) throws RemoteException {
		return (ImportBusiness) IBOLookup.getServiceInstance(iwc, ImportBusiness.class);
	}
	private ImportFileRecord changeICFileToImportFileRecord(ICFile folder) throws Exception {
		return (ImportFileRecord) ((ImportFileRecordHome) com.idega.data.IDOLookup.getHome(ImportFileRecord.class)).findByPrimaryKey(
			folder.getPrimaryKey());
	}
	private Table getFrameTable() {
		if (this.frameTable == null) {
			this.frameTable = new Table();
			this.frameTable.setStyleClass(MAIN_STYLECLASS);
			this.frameTable.setWidth(Table.HUNDRED_PERCENT);
		}
		return (Table) this.frameTable.clone();
	}
	public void setHeaderColor(IWColor color) {
		this.headerColor = color;
	}
	/**
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}