package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.MemberUserBusiness;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.GroupComparator;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.presentation.UserChooserBrowser;
import com.idega.util.IWTimestamp;
/**
 * This window is used to change a members club on a specific date.
 * Copyright : Idega Software 2003
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */


public class ClubMemberExchangeWindow extends StyledIWAdminWindow { //changed from extends IWAdminWindow - birna

	private MemberUserBusiness memBiz;
	private GroupBusiness groupBiz;
	private UserBusiness userBiz;

	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	
	public static final String PARAMETER_USER_ID = "iwme_club_ex_user_id";
	public static final String PARAMETER_FROM_CLUB_DIVISION_ID = "iwme_club_ex_f_club_dv_id";
	public static final String PARAMETER_TO_CLUB_DIVISION_ID = "iwme_club_ex_to_club_dv_id";
	public static final String PARAMETER_INIT_DATE= "iwme_club_ex_init_date";
	public static final String PARAMETER_TERM_DATE= "iwme_club_ex_term_date";
	
	private static final String HELP_TEXT_KEY = "club_member_exchange_window";
	
	
	private String ACTION = "iwme_club_ex_act";
	private String ACTION_SAVE ="iwme_club_ex_act_sv";

	private IWResourceBundle iwrb;
	private List failedInserts;
	
//	private String mainStyleClass = "main";


	public ClubMemberExchangeWindow() {
		setHeight(300);
		setWidth(700);	
		setResizable(true);
	}
	
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		iwrb = getResourceBundle(iwc);
		groupBiz = getGroupBusiness(iwc);
		userBiz = getUserBusiness(iwc);
		memBiz = getMemberUserBusiness(iwc);
		
		setTitle(iwrb.getLocalizedString("clubexchangewindow.title", "Club exchange"));
		addTitle(iwrb.getLocalizedString("clubexchangewindow.title", "Club exchange"), IWConstants.BUILDER_FONT_STYLE_TITLE);

		String action = iwc.getParameter(ACTION);
			
		if (action == null) {
			addForm(iwc);
		}else if (action.equals(ACTION_SAVE)) {
			save(iwc);
		}

	}
	

	private void save(IWContext iwc) {
		String userId = iwc.getParameter(PARAMETER_USER_ID);
		String initDate = iwc.getParameter(PARAMETER_INIT_DATE);
		String termDate = iwc.getParameter(PARAMETER_TERM_DATE);
		String fromDivisionId = iwc.getParameter(PARAMETER_FROM_CLUB_DIVISION_ID);
		String toDivisionId = iwc.getParameter(PARAMETER_TO_CLUB_DIVISION_ID);

		try {
			User user = userBiz.getUser(new Integer(userId));
			IWTimestamp init = new IWTimestamp(initDate);
			IWTimestamp term = new IWTimestamp(termDate);
			Group fromDiv = groupBiz.getGroupByGroupID(Integer.parseInt(fromDivisionId));
			Group toDiv = groupBiz.getGroupByGroupID(Integer.parseInt(toDivisionId));
			
			boolean success = memBiz.moveUserBetweenDivisions(user,fromDiv,toDiv,term,init,iwc);
			if(success){
				close();
			}
			else{
				add(iwrb.getLocalizedString("clubexchangewindow.error_occured", "An error occurred when trying to transfer the player. Please try again or contact your system administrator."));
				add(new CloseButton(iwrb.getLocalizedImageButton("clubexchangewindow.close", "close")));
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
				
	}


	private void addForm(IWContext iwc) {
		Form form = new Form();
		
		Table mainTable = new Table();
		mainTable.setWidth(660);
		mainTable.setHeight(200);
		mainTable.setCellpadding(0);
		mainTable.setCellspacing(0);
		
		Table table = new Table(3,5);
		form.add(mainTable);
		table.setStyleClass(MAIN_STYLECLASS);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(160);
		table.mergeCells(1,1,3,1);
		table.mergeCells(2,5,3,5);
//		table.setWidthAndHeightToHundredPercent();
		table.setAlignment(2,5,Table.HORIZONTAL_ALIGN_RIGHT);
		
		List groups = null;
		try {
			List list = memBiz.getLeaguesListForUserFromTopNodes(iwc.getCurrentUser(),iwc);
			
			
			if(list!=null && !list.isEmpty()){
				groups = new Vector();
				Iterator iter = list.iterator();
				while (iter.hasNext()) {
					Group group = (Group) iter.next();
					groups.addAll(memBiz.getAllClubDivisionsForLeague(group));
				}
				
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if(groups!=null){
			GroupComparator groupComparator = new GroupComparator(iwc.getCurrentLocale());
			groupComparator.setGroupBusiness(this.getGroupBusiness(iwc));
			Collections.sort(groups, groupComparator);//sort alphabetically
		}else{
			add(iwrb.getLocalizedString("clubexchangewindow.no_club_division_to_manage","You do not have any club divisions to manage!" ));
			return;
		}
		
		DropdownMenu divisionFrom = getGroupDropDown(groups,PARAMETER_FROM_CLUB_DIVISION_ID);
		DropdownMenu divisionTo = getGroupDropDown(groups,PARAMETER_TO_CLUB_DIVISION_ID);
		
		DateInput initDate = new DateInput(this.PARAMETER_INIT_DATE);
		//initDate.setToCurrentDate();
		initDate.setAsNotEmpty(iwrb.getLocalizedString("clubexchangewindow.must_set_init_date","You must select an initilization date for the transfer."));
		
		DateInput termDate = new DateInput(this.PARAMETER_TERM_DATE);
		termDate.setAsNotEmpty(iwrb.getLocalizedString("clubexchangewindow.must_set_term_date","You must select a termination date for the transfer."));
		//termDate.setToCurrentDate();
		
		Help help = getHelp(HELP_TEXT_KEY);
		
		CloseButton close = new CloseButton(iwrb.getLocalizedImageButton("clubexchangewindow.close", "Close"));
		SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("clubexchangewindow.save", "Save"), ACTION, ACTION_SAVE);
		save.setSubmitConfirm(iwrb.getLocalizedString("clubexchangewindow.confirm_message", "Are you sure you want to transfer the user at the given dates? An email message will be sent to all parties involved."));
		UserChooserBrowser chooser = new UserChooserBrowser(PARAMETER_USER_ID);
		
		table.add(new Text(iwrb.getLocalizedString("clubexchangewindow.text","Please select the member to transfer and the dates for the transfer to take place.")), 1,1 );
		
		table.add(new Text(iwrb.getLocalizedString("clubexchangewindow.member","Member : ")), 1,2 );
		table.add(chooser,2,2);

		table.add(new Text(iwrb.getLocalizedString("clubexchangewindow.coming_from","Coming from : ")), 1,3 );
		table.add(divisionFrom,2,3);
		table.add(termDate,3,3);

		table.add(new Text(iwrb.getLocalizedString("clubexchangewindow.going_to","Going to : ")), 1,4 );
		table.add(divisionTo,2,4);
		table.add(initDate,3,4);
		
		Table bottomTable = new Table();
		bottomTable.setCellpadding(0);
		bottomTable.setCellspacing(5);
		bottomTable.setWidth(Table.HUNDRED_PERCENT);
		bottomTable.setHeight(39);
		bottomTable.setStyleClass(MAIN_STYLECLASS);
		bottomTable.add(help,1,1);
		bottomTable.setAlignment(2,1,Table.HORIZONTAL_ALIGN_RIGHT);
		bottomTable.add(save,2,1);
		bottomTable.add(Text.getNonBrakingSpace(),2,1);
		bottomTable.add(close,2,1);
		
		mainTable.setVerticalAlignment(1,1,Table.VERTICAL_ALIGN_TOP);
		mainTable.setVerticalAlignment(1,3,Table.VERTICAL_ALIGN_TOP);
		mainTable.add(table,1,1);
		mainTable.add(bottomTable,1,3);

		
		
		//changed from add(form) - birna
		add(form, iwc);
		
	}



	/**
	 * @param groups
	 * @param name
	 * @return DropdownMenu
	 */
	private DropdownMenu getGroupDropDown(Collection groups, String name) {
		DropdownMenu menu = new DropdownMenu(name);
		
		if( groups!=null && !groups.isEmpty()){
			Iterator iter = groups.iterator();
			while (iter.hasNext()) {
				Group group = (Group) iter.next();
				menu.addMenuElement(group.getPrimaryKey().toString(),groupBiz.getNameOfGroupWithParentName(group));
			}
			
		}
		else menu.addMenuElement(-1,iwrb.getLocalizedString("clubexchangewindow.no_values", "No values!"));
	

		
		return menu;
	}


	public UserBusiness getUserBusiness(IWApplicationContext iwc) {
		if (userBiz == null) {
			try {
				userBiz = (UserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, UserBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return userBiz;
	}
	
	public GroupBusiness getGroupBusiness(IWApplicationContext iwc) {
		if (groupBiz == null) {
			try {
				groupBiz = (GroupBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return groupBiz;
	}
	
	public MemberUserBusiness getMemberUserBusiness(IWApplicationContext iwc) {
		if (memBiz == null) {
			try {
				memBiz = (MemberUserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, MemberUserBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return memBiz;
	}
	
	public String getBundleIdentifier(){
		return this.IW_BUNDLE_IDENTIFIER;
	}
	

	
}
