package is.idega.idegaweb.golf.presentation;

import is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusiness;
import is.idega.idegaweb.golf.util.GolfConstants;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.idega.block.login.presentation.LoginByUUIDLink;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.data.IDOEntity;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.GenericSelect;
import com.idega.presentation.ui.SelectDropdown;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.SelectPanel;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.presentation.UserConstants;
import com.idega.user.presentation.UserTab;
import com.idega.util.ListUtil;

/**
 * A tab for displaying and editing a user golf specific data, such as his main golf club and sub golf club lists.
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class GolferTab extends UserTab {
	
	private static final String NO_MAIN_CLUB = "   ";
	private static final String NO_MAIN_CLUB_KEY = "no_m_cl";

	protected static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.golf";
	protected static final String TAB_NAME = "golfer_info_tab";
	protected static final String DEFAULT_TAB_NAME = "Golfer Info";
	protected static final String HELP_TEXT_KEY = "golfer_info_tab";
	protected IDOEntity entity;
	protected Map titles;
	protected String uuid;
	private GolfUserPluginBusiness golfBiz;
	private String mainClubAbbrFromRequest;
	private String[] subClubsAbbrFromRequest;
	private SelectOption noMainClub = null;
	
	public GolferTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
	}
	
	public void initializeFieldNames() {}
	public void initializeFieldValues() {}

	public void updateFieldsDisplayStatus() {
		initializeFields();
//		get the values and update all the fieldValues
//		main club
		User user = getUser();
		uuid = user.getUniqueId();
		String mainClubAbbreviation = (mainClubAbbrFromRequest!=null)?mainClubAbbrFromRequest : user.getMetaData(GolfConstants.MAIN_CLUB_META_DATA_KEY);
		List mainSelected = new ArrayList();
		if(mainClubAbbreviation!=null && !"".equals(mainClubAbbreviation)){
			mainSelected.add(mainClubAbbreviation);
		}
		
		List subClubSelected = new ArrayList();
		//sub clubs
		if(subClubsAbbrFromRequest==null){
			String subClubAbbreviations = user.getMetaData(GolfConstants.SUB_CLUBS_META_DATA_KEY);
			if(subClubAbbreviations!=null && !"".equals(subClubAbbreviations)){
				subClubSelected = getListFromSubClubsString(subClubAbbreviations);
				subClubSelected.add(subClubAbbreviations);
			}
		}
		else{
			subClubSelected = ListUtil.convertStringArrayToList(subClubsAbbrFromRequest);
		}
		
		getFilledGenericSelect(GolfConstants.MAIN_CLUB_META_DATA_KEY,mainSelected,true);

		getFilledGenericSelect(GolfConstants.SUB_CLUBS_META_DATA_KEY,subClubSelected,false);
		
	}

	/**
	 * @param subClubAbbreviations
	 * @return
	 */
	private List getListFromSubClubsString(String subClubAbbreviations) {
		return ListUtil.convertCommaSeparatedStringToList(subClubAbbreviations);
	}

	public void initializeFields() {
		//create all the fieldValues
		if(fieldValues.isEmpty()){
			fieldValues = new HashMap();
			
			GenericSelect mainClubInput = getMainClubDropDown();
			fieldValues.put(GolfConstants.MAIN_CLUB_META_DATA_KEY, mainClubInput);
			
			GenericSelect subClubsInput = getSubClubsSelectionBox();
			fieldValues.put(GolfConstants.SUB_CLUBS_META_DATA_KEY, subClubsInput);
		}
	}

	/**
	 * @return
	 */
	protected GenericSelect getSubClubsSelectionBox() {
		GenericSelect subClubsInput = new SelectPanel(GolfConstants.SUB_CLUBS_META_DATA_KEY);
		return subClubsInput;
	}


	/**
	 * Gets the input from fieldValues map and fills it with available golf clubs
	 * @param key
	 * @param selectedValues
	 * @return
	 */
	protected GenericSelect getFilledGenericSelect(String key, List selectedValues, boolean addEmptyValue) {
		GenericSelect keySelect = (GenericSelect) fieldValues.get(key);
		
		try {
			keySelect.removeElements();
			
			Collection clubs = getGolfUserPluginBusiness().getGolfClubs();
			if(!clubs.isEmpty()){
				if(addEmptyValue){
					noMainClub = new SelectOption(NO_MAIN_CLUB,NO_MAIN_CLUB_KEY);
					noMainClub.setSelected(true);
					keySelect.addOption(noMainClub);
				}
				
				Iterator iter = clubs.iterator();
				while (iter.hasNext()) {
					Group group = (Group) iter.next();
					String abbr = group.getAbbrevation();
					String abbrAndName = abbr+" - "+group.getName();
					if(abbr!=null){
						SelectOption option = new SelectOption(abbrAndName,abbr);
						keySelect.addOption(option);
						if(selectedValues.contains(abbr)){
							keySelect.setSelectedOption(abbr);
						}
					}
				}
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	
		return keySelect;
	}

	/**
	 * @return
	 */
	protected GenericSelect getMainClubDropDown() {
		SelectDropdown mainClubInput = new SelectDropdown(GolfConstants.MAIN_CLUB_META_DATA_KEY);	
		return mainClubInput;
	}

	public void initializeTexts() {
		
		if(titles==null){
			titles = new HashMap();
			IWContext iwc = IWContext.getInstance();
			IWResourceBundle iwrb = getResourceBundle(iwc);
				
			//add main and sub (extra) club titles
			//bold text
			Text title = new Text(iwrb.getLocalizedString(GolfConstants.MAIN_CLUB_META_DATA_KEY,"Main club:"),true,false,false);
			//title.setFontStyle("font-size:8px");
			titles.put(GolfConstants.MAIN_CLUB_META_DATA_KEY,title);
			
			Text title2 = new Text(iwrb.getLocalizedString(GolfConstants.SUB_CLUBS_META_DATA_KEY,"Extra clubs:"),true,false,false);
			//title.setFontStyle("font-size:8px");
			titles.put(GolfConstants.SUB_CLUBS_META_DATA_KEY,title2);
		}
	}

	public Help getHelpButton() {
		IWContext iwc = IWContext.getInstance();
		IWBundle iwb = getBundle(iwc);
		Help help = new Help();
		Image helpImage = iwb.getImage("help.gif");
		help.setHelpTextBundle(UserConstants.HELP_BUNDLE_IDENTFIER);
		help.setHelpTextKey(HELP_TEXT_KEY);
		help.setImage(helpImage);
		return help;
	}

	public void lineUpFields() {
		this.resize(1, 1);
		
		Table table = new Table();
		table.setWidth(450);
		table.setHeight("100%");
		table.setColumns(2);
		table.setCellpadding(5);
		table.setCellspacing(0);
		table.setBorder(0);
		IWContext iwc = IWContext.getInstance();
		String currentUserUUID = iwc.getCurrentUser().getUniqueId();
		IWResourceBundle iwrb = this.getResourceBundle(iwc);
		
//		table.add(new Text(iwrb.getLocalizedString("GenericMetaDataTab.Key","Key"),true,false,true),1,1);
//		table.add(new Text(iwrb.getLocalizedString("GenericMetaDataTab.Value","Value"),true,false,true),2,1);
		table.setRowVerticalAlignment(1,Table.VERTICAL_ALIGN_TOP);
		
		this.add(table, 1, 1);
		
		int row = 1;
		if(currentUserUUID!=null){
			row++;
			LoginByUUIDLink loginByUUIDLink = new LoginByUUIDLink();
			//hardcoded hack
			loginByUUIDLink.setText(iwrb.getLocalizedString(TAB_NAME+".handicapinfo_on_golf.is","Open handicap info on golf.is"));
			loginByUUIDLink.setURL("http://www.golf.is/index.jsp?ib_page=36");
			loginByUUIDLink.setUUID(currentUserUUID);
			loginByUUIDLink.setTarget(Link.TARGET_NEW_WINDOW);
			
			if(uuid!=null){
				loginByUUIDLink.addParameter(GolfConstants.MEMBER_UUID, uuid);
			}
			table.add(loginByUUIDLink,1,row);
		}
		
		if(!fieldValues.isEmpty()){
			
			Iterator iter = titles.keySet().iterator();
			while(iter.hasNext()){
				String key = (String) iter.next();
				table.add((PresentationObject)titles.get(key), 1, row);
				table.add((PresentationObject)fieldValues.get(key), 2, row);
				table.setRowVerticalAlignment(row,Table.VERTICAL_ALIGN_TOP);
				row++;
			}
		}


	}

	public void main(IWContext iwc) {
		getPanel().addHelpButton(getHelpButton());
	}

	public boolean collect(IWContext iwc) {
		if (iwc != null) {
			mainClubAbbrFromRequest = iwc.getParameter(GolfConstants.MAIN_CLUB_META_DATA_KEY);
			if(NO_MAIN_CLUB_KEY.equals(mainClubAbbrFromRequest)){
				mainClubAbbrFromRequest = null;
			}
			subClubsAbbrFromRequest = iwc.getParameterValues(GolfConstants.SUB_CLUBS_META_DATA_KEY);
			
			this.updateFieldsDisplayStatus();
			
			return true;
		}
		return false;
	}

	
	public boolean store(IWContext iwc) {
		User user = getUser();
		
		user.setMetaData(GolfConstants.MAIN_CLUB_META_DATA_KEY, mainClubAbbrFromRequest);
		
		if(subClubsAbbrFromRequest!=null){
			List abbrList = ListUtil.convertStringArrayToList(subClubsAbbrFromRequest);
			
			if(mainClubAbbrFromRequest!=null && abbrList.contains(mainClubAbbrFromRequest)){
				abbrList.remove(mainClubAbbrFromRequest);
			}
			
			if(abbrList.isEmpty()){
				//not to null because when we replicate this field it then removes the value on the other server if it is ""
				user.setMetaData(GolfConstants.SUB_CLUBS_META_DATA_KEY, "");
			}
			else{
				String commaSeparated = ListUtil.convertListOfStringsToCommaseparatedString(abbrList);
				user.setMetaData(GolfConstants.SUB_CLUBS_META_DATA_KEY, commaSeparated);
			}
		}else{
//			not to null because when we replicate this field it then removes the value on the other server if it is ""
			user.setMetaData(GolfConstants.SUB_CLUBS_META_DATA_KEY,"");
		}
		
		user.store();
		mainClubAbbrFromRequest = null;
		subClubsAbbrFromRequest = null;
		
		this.updateFieldsDisplayStatus();

		return true;
	}
	
	public void initFieldContents() {

		try {
			
			this.updateFieldsDisplayStatus();

		} catch (Exception e) {
			System.err.println("GolferTab error initFieldContents, userId : " + getUserId());
			e.printStackTrace();
		}
	}

//	private void setClubMetaData(User user, String clubAbbr, String membership_status) {
//		if(membership_status.equalsIgnoreCase(MAIN_CLUB_TYPE)){
//			String abbr = user.getMetaData(MetadataConstants.MAIN_CLUB_GOLF_META_DATA_KEY);
//			if(abbr!=null && !abbr.equals(clubAbbr)){
//				//move the main club to a sub club
//				addToSubClubs(abbr,user);
//				user.setMetaData(MetadataConstants.MAIN_CLUB_GOLF_META_DATA_KEY,clubAbbr);
//			}else{
//				user.setMetaData(MetadataConstants.MAIN_CLUB_GOLF_META_DATA_KEY,clubAbbr);
//			}
//		}else{
//			addToSubClubs(clubAbbr,user);
//		}
//	}
//
//	/**
//	 * Adds the club abbreviation string to a list of comma separeted values and stores the new value if needed in metadata 
//	 * @param abbr
//	 */
//	private void addToSubClubs(String abbr,User user) {
//		String subClubs = user.getMetaData(MetadataConstants.SUB_CLUBS_GOLF_META_DATA_KEY);
//		if(subClubs==null){
//			subClubs = abbr+",";
//			
//		}
//		else{
//			if(subClubs.indexOf(abbr+",")<0){
//				subClubs+=abbr+",";
//			}
//		}
//		
//		user.setMetaData(MetadataConstants.SUB_CLUBS_GOLF_META_DATA_KEY,subClubs);
//	}
	
	
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	
	public GolfUserPluginBusiness getGolfUserPluginBusiness(){
		if(golfBiz == null){
			try {
				golfBiz = (GolfUserPluginBusiness)IBOLookup.getServiceInstance(IWMainApplication.getDefaultIWApplicationContext(),GolfUserPluginBusiness.class);
			}
			catch (IBOLookupException e) {
				e.printStackTrace();
			}
		}
		return golfBiz;
	}

}
