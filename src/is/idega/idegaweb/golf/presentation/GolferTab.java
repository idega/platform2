package is.idega.idegaweb.golf.presentation;

import is.idega.idegaweb.golf.util.GolfConstants;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.idega.data.IDOEntity;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.SelectionBox;
import com.idega.user.data.User;
import com.idega.user.presentation.UserConstants;
import com.idega.user.presentation.UserTab;

/**
 * A tab for displaying and editing a user golf specific data, such as his main golf club and sub golf club lists.
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class GolferTab extends UserTab {
	protected static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.golf";
	protected static final String TAB_NAME = "golfer_info_tab";
	protected static final String DEFAULT_TAB_NAME = "Golfer Info";
	protected static final String HELP_TEXT_KEY = "golfer_info_tab";
	protected IDOEntity entity;
	protected Map inputs = null;
	protected Map titles = null;
	
	public GolferTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
	}

	public void initializeFieldNames() {}
	public void initializeFieldValues() {}

	public void updateFieldsDisplayStatus() {
		if(inputs==null){
			inputs = new HashMap();
			titles = new HashMap();
		}
//		get the values and update all the inputs
//		main club
		User user = getUser();
		String mainClubAbbreviation = user.getMetaData(GolfConstants.MAIN_CLUB_META_DATA_KEY);
		//sub clubs
		String subClubAbbreviations = user.getMetaData(GolfConstants.SUB_CLUBS_META_DATA_KEY);
		
		InterfaceObject mainClubInput = (InterfaceObject)inputs.get(GolfConstants.MAIN_CLUB_META_DATA_KEY);
		if(mainClubAbbreviation!=null){
			mainClubInput.setContent(mainClubAbbreviation);
		}
		
		InterfaceObject subClubsInput = (InterfaceObject)inputs.get(GolfConstants.SUB_CLUBS_META_DATA_KEY);
		if(subClubAbbreviations!=null){
			subClubsInput.setContent(mainClubAbbreviation);
		}
	}

	public void initializeFields() {
		//create all the inputs
		if(inputs==null){
			inputs = new HashMap();
			titles = new HashMap();
		}
		
		InterfaceObject mainClubInput = getMainClubDropDown();
		inputs.put(GolfConstants.MAIN_CLUB_META_DATA_KEY, mainClubInput);
		
		InterfaceObject subClubsInput = getSubClubsSelectionBox();
		inputs.put(GolfConstants.SUB_CLUBS_META_DATA_KEY, subClubsInput);
		
	}

	/**
	 * @return
	 */
	private InterfaceObject getSubClubsSelectionBox() {
		InterfaceObject subClubsInput = new DropdownMenu(GolfConstants.SUB_CLUBS_META_DATA_KEY);
		return subClubsInput;
	}

	/**
	 * @return
	 */
	private InterfaceObject getMainClubDropDown() {
		InterfaceObject mainClubInput = new SelectionBox(GolfConstants.MAIN_CLUB_META_DATA_KEY);
		return mainClubInput;
	}

	public void initializeTexts() {
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
		IWResourceBundle iwrb = this.getResourceBundle(iwc);
		
//		table.add(new Text(iwrb.getLocalizedString("GenericMetaDataTab.Key","Key"),true,false,true),1,1);
//		table.add(new Text(iwrb.getLocalizedString("GenericMetaDataTab.Value","Value"),true,false,true),2,1);
		table.setRowVerticalAlignment(1,Table.VERTICAL_ALIGN_TOP);
		
		this.add(table, 1, 1);
		
		if(!inputs.isEmpty()){
			int row = 1;
			Iterator iter = inputs.keySet().iterator();
			while(iter.hasNext()){
				String key = (String) iter.next();
				table.add((PresentationObject)titles.get(key), 1, row);
				table.add((PresentationObject)inputs.get(key), 2, row);
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
			User user = getUser();
			String mainClubAbbr = iwc.getParameter(GolfConstants.MAIN_CLUB_META_DATA_KEY);
			user.setMetaData(GolfConstants.MAIN_CLUB_META_DATA_KEY, mainClubAbbr);
			String subClubsAbbr = iwc.getParameter(GolfConstants.SUB_CLUBS_META_DATA_KEY);
			user.setMetaData(GolfConstants.SUB_CLUBS_META_DATA_KEY, subClubsAbbr);
				
//			if(subUnions!=null && mainUnion!=null){
//				StringTokenizer tokens = new StringTokenizer(subUnions,",");
//				while (tokens.hasMoreTokens()) {
//					String subAbbr = tokens.nextToken();

			this.updateFieldsDisplayStatus();

			return true;
		}
		return false;
	}

	
	public boolean store(IWContext iwc) {
		
		getUser().store();
		
		return true;
	}

	public void initFieldContents() {

		try {
			
			this.updateFieldsDisplayStatus();

		} catch (Exception e) {
			System.err.println("GolferTab error initFieldContents, userId : " + getUserId());
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

} // Class UserPhoneTab
