package com.idega.block.datareport.presentation;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.ejb.FinderException;
import com.idega.block.dataquery.presentation.ReportQueryBuilder;
import com.idega.block.media.business.MediaBusiness;
import com.idega.block.media.presentation.SimpleFileChooser;
import com.idega.builder.business.FileBusiness;
import com.idega.business.IBOLookup;
import com.idega.core.data.ICTreeNode;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.UploadFile;
import com.idega.io.serialization.Storable;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.StringAlphabeticalComparator;
import com.idega.util.StringHandler;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Feb 10, 2004
 */
public class LayoutUploader extends Block {

	public final static String KEY_LAYOUT_UPLOAD_IS_SUBMITTED = "key_layout_upload_is_submitted"; 
	public final static String KEY_LAYOUT_DOWNLOAD_IS_SUBMITTED = "key_layout_download_is_submitted";
	private final static String KEY_CHOSEN_LAYOUT_FOR_DOWNLOADING = "key_chosen_layout_for_downloading";

	
	private final static String KEY_FILE_ID = "key_file_id";
	private final static String KEY_NAME = "key_query_name";
	private final static String KEY_CHOSEN_LAYOUT = "key_chosen_layout";
	public final static String KEY_DELETE_LAYOUT_IS_SUBMITTED = "key_delete_layout";
	private final static String COUNTER_TOKEN = "_";
	private static final String DEFAULT_NAME = "my layout";
		
	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.dataquery";
	
	private ICFile layoutFolder = null;
	private String layoutFolderId = null;
	private String downloadUrl = null;
	
	public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
	
	public void main(IWContext iwc) throws Exception {
		String error = parseAction(iwc);
		if (error != null) {
			// showing nice error text when SQLException occurred - should not happen
			Text errorText = new Text(error);
			add(errorText);
		}
		IWResourceBundle resourceBundle = getResourceBundle(iwc);
		
		// delete form
		Text deleteText = new Text(resourceBundle.getLocalizedString("layout_uploader_delete_layout_headline", "Delete Layout"));
		deleteText.setBold();
		add(deleteText);
		Form form = new Form();
		addMaintainParametersToForm(form);
		int row = 1;
		Table table = new Table(2, 1);
		table.add(getDropDownOfLayouts(KEY_CHOSEN_LAYOUT, layoutFolder, iwc), 1,row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(getDeleteButton(resourceBundle), 2, row);
		form.add(table);
		add(form);
		
		// upload form
		add(Text.getBreak());
		Text uploadText = new Text(resourceBundle.getLocalizedString("layout_uploader_upload_layout_headline", "Upload Layout"));
		uploadText.setBold();
		add(uploadText);
		Table uploadTable = new Table(2,2);
		row = 1;
		Form uploadForm = new Form();
		addMaintainParametersToForm(uploadForm);
		String defaultName = resourceBundle.getLocalizedString("layout_uploader_default_layout_name", "my layout");
		TextInput textInput = new TextInput(KEY_NAME, defaultName );
		Text info = new Text(resourceBundle.getLocalizedString("layout_uploader_layout_name", "layout name"));
		uploadTable.add(info, 1 ,row);
		uploadTable.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		uploadTable.add(textInput, 2, row);
		uploadTable.setAlignment(2, row, Table.HORIZONTAL_ALIGN_LEFT);
		row++;
		SimpleFileChooser uploader = new SimpleFileChooser(uploadForm, KEY_FILE_ID);
		uploadTable.add(uploader, 2, row);
		uploadTable.setAlignment(2, row, Table.HORIZONTAL_ALIGN_LEFT);
		row++;
		uploadForm.add(uploadTable);
		uploadForm.addParameter(KEY_LAYOUT_UPLOAD_IS_SUBMITTED, KEY_LAYOUT_UPLOAD_IS_SUBMITTED);
		add(uploadForm);
		
		// downloading
		add(Text.getBreak());
		Text downloadingText = new Text(resourceBundle.getLocalizedString("layout_uploader_download_layout_headline", "Download Layout"));
		downloadingText.setBold();
		add(downloadingText);
		Form downloadForm = new Form();
		row = 1;
		addMaintainParametersToForm(downloadForm);
		Table downloadTable = new Table(2, 3);
		PresentationObject downloadQueryList = getDropDownOfLayouts(KEY_CHOSEN_LAYOUT_FOR_DOWNLOADING, layoutFolder, iwc);
		downloadTable.add(downloadQueryList, 1, row);
		downloadTable.add(getDownloadButton(resourceBundle), 2, row++);
		if (downloadUrl != null) {
			String downloadText = resourceBundle.getLocalizedString("layout_uploader_download_query", "Download");
	  		downloadTable.add(new Link(downloadText, downloadUrl), 1, row++);
		}
		downloadTable.add(getGoBackButton(resourceBundle), 1 ,row);
		downloadForm.add(downloadTable);
		add(downloadForm);
	}
	
	private void addMaintainParametersToForm(Form form) {
		form.addParameter(ReportQueryBuilder.PARAM_LAYOUT_FOLDER_ID, layoutFolderId);
	}
	
	private String parseAction(IWContext iwc) throws NumberFormatException, FinderException, RemoteException, IOException {
		int folderId;
		if (iwc.isParameterSet(ReportQueryBuilder.PARAM_LAYOUT_FOLDER_ID)) {
			layoutFolderId = iwc.getParameter(ReportQueryBuilder.PARAM_LAYOUT_FOLDER_ID);
			folderId = Integer.parseInt(layoutFolderId);
			layoutFolder = getFile(folderId);
		}
		else {
			return null;
		}
		// do not change the order, check first the delete key
		if (iwc.isParameterSet(KEY_DELETE_LAYOUT_IS_SUBMITTED)) {
			String layoutId = iwc.getParameter(KEY_CHOSEN_LAYOUT);
			int id = Integer.parseInt(layoutId);
			ICFile layout = getFile(id);
			try {
				layout.delete();
			} catch (SQLException e) {
				return "Could not delete file";
			}
		}
		else if (iwc.isParameterSet(KEY_LAYOUT_UPLOAD_IS_SUBMITTED)) {
			UploadFile uploadFile = iwc.getUploadedFile();
			String name = iwc.getParameter(KEY_NAME);
			name = checkName(layoutFolder, name);
			uploadFile.setName(name);
			MediaBusiness.saveMediaToDB(uploadFile, folderId, iwc);
		}
		else if (iwc.isParameterSet(KEY_LAYOUT_DOWNLOAD_IS_SUBMITTED)) {
			Object layoutToBeDownloadedId = iwc.getParameter(KEY_CHOSEN_LAYOUT_FOR_DOWNLOADING);
			Integer layoutToBeDownloaded = new Integer((String) layoutToBeDownloadedId); 
			ICFileHome fileHome = (ICFileHome)IDOLookup.getHome(ICFile.class);
			ICFile layout = fileHome.findByPrimaryKey(layoutToBeDownloaded);
			FileBusiness fileBusiness = (FileBusiness) IBOLookup.getServiceInstance(iwc, FileBusiness.class);
			downloadUrl = fileBusiness.getURLForOfferingDownload((Storable) layout, iwc);
		}
		return null;
	}
	
	private PresentationObject getDropDownOfLayouts(String key, ICFile designFolder, IWContext iwc) {
		SortedMap sortedMap = new TreeMap(new StringAlphabeticalComparator(iwc.getCurrentLocale()));
		DropdownMenu drp = new DropdownMenu(key);
		Iterator iterator = designFolder.getChildren();
		if (iterator != null) {
			while (iterator.hasNext()) {
				ICTreeNode node = (ICTreeNode) iterator.next();
				String name = node.getNodeName();
				int id = node.getNodeID();
				String idAsString = Integer.toString(id);
				if (sortedMap.containsKey(name)) {
					// usually all items have different names therefore 
					// we implement a very simple solution
					name += " (1)";
				}
				sortedMap.put(name, idAsString);
			}
			Iterator sortedIterator = sortedMap.entrySet().iterator();
			while (sortedIterator.hasNext()) {
				Map.Entry entry = (Map.Entry) sortedIterator.next();
				String id = (String) entry.getValue();
				String name = (String) entry.getKey();
				drp.addMenuElement(id, name);
			}
		}
		return drp;
	}
			
	private PresentationObject getDeleteButton(IWResourceBundle resourceBundle) {
		String deleteInfo = resourceBundle.getLocalizedString("layout_uploader_delete", "Delete layout");
		SubmitButton deleteButton = new SubmitButton(deleteInfo, KEY_DELETE_LAYOUT_IS_SUBMITTED, "true");
		deleteButton.setAsImageButton(true);
		return deleteButton;
	}
		
		
		
	private PresentationObject getGoBackButton(IWResourceBundle resourceBundle)	{
  	String goBackText = resourceBundle.getLocalizedString("ro_back_to_list", "Back to list");
  	Link goBack = new Link(goBackText);
  	goBack.addParameter(ReportQueryBuilder.PARAM_LAYOUT_FOLDER_ID, layoutFolderId);
  	goBack.setAsImageButton(true);
  	return goBack;
	}
	
	private PresentationObject getDownloadButton(IWResourceBundle resourceBundle) {
	  	SubmitButton downloadButton = new SubmitButton(resourceBundle.getLocalizedString("ro_download","Download..."), KEY_LAYOUT_DOWNLOAD_IS_SUBMITTED, "true");
	  	downloadButton.setAsImageButton(true);
	  	return downloadButton;
		}	


	private String checkName(ICFile designFolder, String name) {
		if (name == null || name.length() == 0) {
			name = DEFAULT_NAME;
		}
		Collection existingNames = null;
		if(designFolder!=null){
			existingNames = new ArrayList();
			Iterator iterator = designFolder.getChildren();	
			if (iterator != null) {
				while (iterator.hasNext())	{
					ICTreeNode node = (ICTreeNode) iterator.next();
					existingNames.add(node.getNodeName());
				}
			}
		}
		return StringHandler.addOrIncreaseCounterIfNecessary(name, COUNTER_TOKEN, existingNames);
	}

  private ICFile getFile(int fileId) throws FinderException {
    try {
      ICFileHome home = (ICFileHome) IDOLookup.getHome(ICFile.class);
      ICFile file = home.findByPrimaryKey(new Integer(fileId));
      return file;
    }
    catch(RemoteException ex){
      throw new RuntimeException("[ReportBusiness]: Message was: " + ex.getMessage());
    }
  }     
	
	
	
}
