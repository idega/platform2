package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.business.MemberUserBusiness;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.LinkContainer;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
/**
 * This window is used to work with a clubs work reports.
 * Copyright : Idega Software 2003
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */


public class WorkReportWindow extends IWAdminWindow {

	private MemberUserBusiness memBiz;
	private GroupBusiness groupBiz;
	private UserBusiness userBiz;

	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	public static final String PARAMETER_CLUB_ID = "iwme_wr_club_id";

	private static final String ACTION = "iwme_wr_act";
	private static final String ACTION_SAVE ="iwme_wr_act_sv";
	
	private static final String ACTION_WORK_ON_REPORT ="iwme_wr_act_b1";
	private static final String ACTION_EDIT_MEMBER_LIST ="iwme_wr_act_b2";
	private static final String ACTION_EDIT_ACCOUNT ="iwme_wr_act_b3";
	private static final String ACTION_EDIT_BOARD ="iwme_wr_act_b4";
	private static final String ACTION_UPLOAD_REPORT ="iwme_wr_act_b5"; //b6 is useless
	private static final String ACTION_IMPORT_MEMBERS ="iwme_wr_act_b7";
	private static final String ACTION_IMPORT_ACCOUNT ="iwme_wr_act_b8";
	private static final String ACTION_IMPORT_BOARD ="iwme_wr_act_b9";
	private static final String ACTION_REPORT_OVERVIEW ="iwme_wr_act_b10";
	private static final String ACTION_CLOSE_REPORT ="iwme_wr_act_b11";
	private static final String ACTION_STATISTICS ="iwme_wr_act_b12";
	
	private IWResourceBundle iwrb;
	private List failedInserts;


	public WorkReportWindow() {
		setHeight(800);
		setWidth(600);	
		setResizable(true);
		
	}
	
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		iwrb = getResourceBundle(iwc);
		groupBiz = getGroupBusiness(iwc);
		userBiz = getUserBusiness(iwc);
		memBiz = getMemberUserBusiness(iwc);
		
		setTitle(iwrb.getLocalizedString("workreportwindow.title", "Work Reports"));

		String action = iwc.getParameter(ACTION);
			
		if (action == null) {
			addForm(iwc);
		}else if (action.equals(ACTION_SAVE)) {
			save(iwc);
		}

	}
	

	private void save(IWContext iwc) {

		try {
			User user = iwc.getCurrentUser();
		
			boolean success = true;
			if(success){
				close();
			}
			else{
				add(iwrb.getLocalizedString("workreportwindow.error_occured", "An error occurred. Please try again or contact your system administrator."));
				add(new CloseButton(iwrb.getLocalizedImageButton("workreportwindow.close", "close")));
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
				
	}


	private void addForm(IWContext iwc) {
		Form form = new Form();
		
		Table table = new Table(2,1);
		//table.mergeCells(1,2,3,1);
		table.setWidthAndHeightToHundredPercent();
		table.setColumnColor(1,this.MENU_COLOR);
		table.setColumnWidth(1,"200");
		
		Table menu = new Table(1,11);
		menu.setWidth(Table.HUNDRED_PERCENT);
		table.setVerticalAlignment(1,1,Table.VERTICAL_ALIGN_TOP);
		table.setVerticalAlignment(2,1,Table.VERTICAL_ALIGN_TOP);
		
		Text operations = formatHeadline(iwrb.getLocalizedString("workreportwindow.operations","Operations"));
		
		LinkContainer createReports = new LinkContainer();
		createReports.add(formatText(iwrb.getLocalizedString("workreportwindow.create_reports","Create reports"),true));
		
		//B.5		
		LinkContainer uploadReport = new LinkContainer();
		uploadReport.add(formatText(iwrb.getLocalizedString("workreportwindow.upload_report","Upload report"),true));
		Lists uploadList = new Lists();
		//B.7
		uploadList.add(formatText(iwrb.getLocalizedString("workreportwindow.import_members","Import member list")));
		//B.8
		uploadList.add(formatText(iwrb.getLocalizedString("workreportwindow.import_account","Import account info")));
		//B.9
		uploadList.add(formatText(iwrb.getLocalizedString("workreportwindow.import_board","Import board info")));
		
		//B.1
		LinkContainer workOnReport = new LinkContainer();
		workOnReport.add(formatText(iwrb.getLocalizedString("workreportwindow.select_report","Select report"),true));
		Lists editList = new Lists();
		//B.2
		editList.add(formatText(iwrb.getLocalizedString("workreportwindow.edit_members","Edit member list")));
		//B.3
		editList.add(formatText(iwrb.getLocalizedString("workreportwindow.edit_account","Edit account info")));
		//B.4
		editList.add(formatText(iwrb.getLocalizedString("workreportwindow.edit_board","Edit board info")));

		//B.10
		LinkContainer reportsOverview = new LinkContainer();
		reportsOverview.add(formatText(iwrb.getLocalizedString("workreportwindow.report_overview","Reports overview"),true));
		
		//B.11
		LinkContainer closeReport = new LinkContainer();
		closeReport.add(formatText(iwrb.getLocalizedString("workreportwindow.close_report","Close report"),true));
		
		//B.12
		Text statistics = formatHeadline(iwrb.getLocalizedString("workreportwindow.statistics","Statistics"));
		
		Lists statsList = new Lists();
		statsList.add(formatText(iwrb.getLocalizedString("workreportwindow.leagues","Leagues")));
		statsList.add(formatText(iwrb.getLocalizedString("workreportwindow.regional_unions","Regional unions")));
		statsList.add(formatText(iwrb.getLocalizedString("workreportwindow.clubs","Clubs")));
		statsList.add(formatText(iwrb.getLocalizedString("workreportwindow.accounts","Accounts")));
		statsList.add(formatText(iwrb.getLocalizedString("workreportwindow.reports_list","Reports list")));
		statsList.add(formatText(iwrb.getLocalizedString("workreportwindow.misc","Misc")));		
		

		//add to window
		menu.add(operations,1,1);
		menu.add(createReports,1,2);
		menu.add(uploadReport,1,3);
		menu.add(uploadList,1,4);
		menu.add(workOnReport,1,5);
		menu.add(editList,1,6);
		menu.add(reportsOverview,1,7);
		menu.add(closeReport,1,8);
		menu.add(statistics,1,10);
		menu.add(statsList,1,11);
		
		form.add(table);
		table.add(menu,1,1);
				
		add(form);
		
	
		/*List groups = null;
		try {
			List list = memBiz.getLeaguesListForUser(iwc.getCurrentUser(),iwc);
			
			
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
	*/
				

		
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
		else menu.addMenuElement(-1,iwrb.getLocalizedString("workreportwindow.no_values", "No values!"));
	

		
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
