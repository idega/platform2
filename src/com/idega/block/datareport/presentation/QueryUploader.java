package com.idega.block.datareport.presentation;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.ejb.FinderException;

import com.idega.block.dataquery.business.QueryService;
import com.idega.block.dataquery.data.UserQuery;
import com.idega.block.dataquery.presentation.ReportQueryBuilder;
import com.idega.block.media.business.MediaBusiness;
import com.idega.block.media.presentation.SimpleFileChooser;
import com.idega.business.IBOLookup;
import com.idega.core.file.data.ICFile;
import com.idega.data.EntityRepresentation;
import com.idega.data.IDOStoreException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.UploadFile;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.RadioGroup;
import com.idega.presentation.ui.TextInput;
import com.idega.util.StringAlphabeticalComparator;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Feb 9, 2004
 */
public class QueryUploader extends Block {
	
	private final static String KEY_QUERY_FILE_ID = "key_file_id";
	private final static String KEY_QUERY_NAME = "key_query_name";
	private final static String KEY_PERMISSION = "key_private_query";
	private final static String KEY_CHOSEN_QUERY = "key_chosen_query";
	public final static String KEY_QUERY_UPLOAD_IS_SUBMITTED = "key_query_upload_is_submitted"; 
	
	private final static String PRIVATE = "private";
	private final static String PUBLIC = "public";
	
	private final static String VALUE_DO_NOT_REPLACE_A_QUERY = "-1";
	
	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.dataquery";
	
	private int userQueryId = -1;
	private String layoutFolderId = null;
	
	public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
	
	public void main(IWContext iwc) throws Exception {
		parseAction(iwc);
		IWResourceBundle resourceBundle = getResourceBundle(iwc);
		Form form = new Form();
		addMaintainParametersToForm(form);
		
		Table table = new Table(2, 5);
		int row = 1;
		String queryInfo = resourceBundle.getLocalizedString("query_uploader_replace_query", "Replace query");
		table.add(queryInfo, 1, row);
		PresentationObject queryList = getDropDownOfQueries(resourceBundle, iwc);
		table.add(queryList, 2, row);
		row++;
		String defaultName = resourceBundle.getLocalizedString("query_uploader_default_queryname", "My query");
		TextInput textInput = new TextInput(KEY_QUERY_NAME, defaultName );
		Text info = new Text(resourceBundle.getLocalizedString("query_uploader_query_name", "set query name"));
		table.add(info, 1 ,row);
		table.add(textInput, 2,row);
		row++;
		table.add(getPrivatePublicRadioButtons(resourceBundle),2,row);
		SimpleFileChooser uploader = new SimpleFileChooser(form, KEY_QUERY_FILE_ID);
		row++;
		table.add(uploader, 2,row);
		row++;
		table.add(getGoBackButton(resourceBundle), 1 ,row);
		form.add(table);
		form.addParameter(KEY_QUERY_UPLOAD_IS_SUBMITTED, KEY_QUERY_UPLOAD_IS_SUBMITTED);
		add(form);
	}
	
	private void addMaintainParametersToForm(Form form) {
			form.addParameter(ReportQueryBuilder.PARAM_LAYOUT_FOLDER_ID, layoutFolderId);
	}
	
	private void parseAction(IWContext iwc) throws NumberFormatException, IDOStoreException, IOException, RemoteException {
		if (iwc.isParameterSet(ReportQueryBuilder.PARAM_LAYOUT_FOLDER_ID)) {
			layoutFolderId = iwc.getParameter(ReportQueryBuilder.PARAM_LAYOUT_FOLDER_ID);
		}
		else {
			return;
		}
		if (iwc.isParameterSet(KEY_QUERY_UPLOAD_IS_SUBMITTED)) {
			String queryToBeReplacedId = iwc.getParameter(KEY_CHOSEN_QUERY);
			if (VALUE_DO_NOT_REPLACE_A_QUERY.equals(queryToBeReplacedId)) {
				queryToBeReplacedId = null;
			}
			UploadFile uploadFile = iwc.getUploadedFile();
			ICFile icFile = MediaBusiness.saveMediaToDBUploadFolder(uploadFile,iwc);
			String name = iwc.getParameter(KEY_QUERY_NAME);
			String permission = iwc.getParameter(KEY_PERMISSION);
			boolean isPrivate = PRIVATE.equals(permission);
			QueryService queryService = (QueryService) IBOLookup.getServiceInstance(iwc, QueryService.class);
			UserQuery userQuery = queryService.storeQuery(name, icFile, isPrivate, new Integer(queryToBeReplacedId), iwc);
			userQueryId = ((Integer) userQuery.getPrimaryKey()).intValue();
		}
	}
	
	
	private PresentationObject getGoBackButton(IWResourceBundle resourceBundle)	{
  	String goBackText = resourceBundle.getLocalizedString("ro_back_to_list", "Back to list");
  	Link goBack = new Link(goBackText);
  	goBack.addParameter(ReportQueryBuilder.PARAM_LAYOUT_FOLDER_ID, layoutFolderId);
  	goBack.setAsImageButton(true);
  	return goBack;
	}	

	private PresentationObject getPrivatePublicRadioButtons(IWResourceBundle iwrb) {
		RadioGroup radioGroup = new RadioGroup(KEY_PERMISSION);
		radioGroup.setWidth(1);
		radioGroup.addRadioButton(PRIVATE, new Text(iwrb.getLocalizedString("query_uploader_private_query", "private")), true);
		radioGroup.addRadioButton(PUBLIC, new Text(iwrb.getLocalizedString("query_uploader_public_query", "public")));
		return radioGroup;
	}
		
	private PresentationObject getDropDownOfQueries(IWResourceBundle iwrb, IWContext iwc) throws RemoteException, FinderException {
		SortedMap sortedMap = new TreeMap(new StringAlphabeticalComparator(iwc.getCurrentLocale()));
		DropdownMenu drp = new DropdownMenu(KEY_CHOSEN_QUERY);
		Iterator iterator = ReportQueryOverview.getOwnQueries(iwc).iterator();
		while (iterator.hasNext()) {
			EntityRepresentation userQuery = (EntityRepresentation) iterator.next();
			String name = (String) userQuery.getColumnValue(ReportQueryOverview.NAME_KEY);
			String id = userQuery.getPrimaryKey().toString();
			if (sortedMap.containsKey(name)) {
				//usually the items have different names therefore we implement
				// a very simple solution 
				name += " (1)";
			}
			sortedMap.put(name, id);
		}
		Iterator sortedIterator = sortedMap.entrySet().iterator();
		while (sortedIterator.hasNext()) {
			Map.Entry entry = (Map.Entry) sortedIterator.next();
			String id = (String) entry.getValue();
			String name = (String) entry.getKey();
			drp.addMenuElement(id, name);
		}
		String doNotUseReplaceAQuery = iwrb.getLocalizedString("step_5_do_not_replace_a_query","don't replace a query");
		drp.addMenuElementFirst(VALUE_DO_NOT_REPLACE_A_QUERY, doNotUseReplaceAQuery );
		return drp;
	}
	
	
	
	/**
	 * @return Returns the userQueryId.
	 */
	public int getUserQueryId() {
		return userQueryId;
	}

}
