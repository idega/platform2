package com.idega.block.datareport.presentation;

import java.rmi.RemoteException;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.dataquery.presentation.ReportQueryBuilder;
import com.idega.block.media.business.MediaBusiness;
import com.idega.block.media.presentation.SimpleFileChooser;
import com.idega.core.data.ICTreeNode;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOStoreException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.UploadFile;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.TextInput;
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

	public final static String  KEY_LAYOUT_UPLOAD_IS_SUBMITTED = "key_layout_upload_is_submitted"; 
	
	private final static String KEY_FILE_ID = "key_file_id";
	private final static String KEY_NAME = "key_query_name";
	private final static String COUNTER_TOKEN = "_";
	private static final String DEFAULT_NAME = "my layout";
		
	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.dataquery";
	
	private String layoutFolderId = null;
	
	public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
	
	public void main(IWContext iwc) throws Exception {
		parseAction(iwc);
		IWResourceBundle resourceBundle = getResourceBundle(iwc);
		Form form = new Form();
		addMaintainParametersToForm(form);
		Table table = new Table(2, 3);
		String defaultName = resourceBundle.getLocalizedString("layout_uploader_default_layout_name", "my layout");
		TextInput textInput = new TextInput(KEY_NAME, defaultName );
		Text info = new Text(resourceBundle.getLocalizedString("layout_uploader_set_name", "set layout name"));
		table.add(info, 1 ,1);
		table.add(textInput, 2,1);
		SimpleFileChooser uploader = new SimpleFileChooser(form, KEY_FILE_ID);
		table.add(uploader, 2,2);
		table.add(getGoBackButton(resourceBundle), 1 ,3);
		form.add(table);
		form.addParameter(KEY_LAYOUT_UPLOAD_IS_SUBMITTED, KEY_LAYOUT_UPLOAD_IS_SUBMITTED);
		add(form);
	}
	
	private void addMaintainParametersToForm(Form form) {
		form.addParameter(ReportQueryBuilder.PARAM_LAYOUT_FOLDER_ID, layoutFolderId);
	}
	
	private void parseAction(IWContext iwc) throws NumberFormatException, IDOStoreException, FinderException {
		if (iwc.isParameterSet(ReportQueryBuilder.PARAM_LAYOUT_FOLDER_ID)) {
			layoutFolderId = iwc.getParameter(ReportQueryBuilder.PARAM_LAYOUT_FOLDER_ID);
		}
		else {
			return;
		}
		if (iwc.isParameterSet(KEY_LAYOUT_UPLOAD_IS_SUBMITTED)) {
			int folderId = Integer.parseInt(layoutFolderId);
			ICFile designFolder = getFile(folderId);
			UploadFile uploadFile = iwc.getUploadedFile();
			String name = iwc.getParameter(KEY_NAME);
			name = checkName(designFolder, name);
			uploadFile.setName(name);
			MediaBusiness.saveMediaToDB(uploadFile, folderId, iwc);
		}
	}
		
	private PresentationObject getGoBackButton(IWResourceBundle resourceBundle)	{
  	String goBackText = resourceBundle.getLocalizedString("ro_back_to_list", "Back to list");
  	Link goBack = new Link(goBackText);
  	goBack.addParameter(ReportQueryBuilder.PARAM_LAYOUT_FOLDER_ID, layoutFolderId);
  	goBack.setAsImageButton(true);
  	return goBack;
	}

	private String checkName(ICFile designFolder, String name) {
		if (name == null || name.length() == 0) {
			name = DEFAULT_NAME;
		}
		if(designFolder!=null){
			Iterator iterator = designFolder.getChildren();
			while (iterator.hasNext())	{
				ICTreeNode node = (ICTreeNode) iterator.next();
				String existingName = node.getNodeName();
				if (name.equals(existingName)) {
					return StringHandler.addOrIncreaseCounter(name, COUNTER_TOKEN);
				}
			}
		}
		return name;
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
