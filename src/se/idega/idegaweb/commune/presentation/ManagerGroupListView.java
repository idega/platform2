package se.idega.idegaweb.commune.presentation;
import java.util.Collection;
import java.util.Iterator;

import com.idega.builder.data.IBPage;
import com.idega.data.IDOLookup;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.util.StringHandler;
/**
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class ManagerGroupListView extends CommuneBlock {
	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	private final static int ACTION_VIEW_MANAGER_LIST = 2;
	private final static int ACTION_VIEW_MANAGER_GROUP = 3;

	//final static String PARAM_MANAGER_ID = ManagerView.PARAM_MANAGER_ID;
	final static String PARAM_MANAGER_GROUP_ID="comm_manvw_gr_id";
	
	private Table mainTable = null;
	private int managerListPageID = -1;
	private int topGroupID=-1;
	private boolean addHeader=false;

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	public void main(IWContext iwc)throws Exception {
		try {
			int action = parseAction(iwc);
			initGroupID(iwc);
			//System.out.println("main: action="+action);
			switch (action) {
				case ACTION_VIEW_MANAGER_LIST :
					viewManagerList(iwc);
					break;
				case ACTION_VIEW_MANAGER_GROUP :
					viewManagerGroup(iwc);
					break;
				default :
					viewNoGroups(iwc);
					break;
			}
			super.add(mainTable);
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}
	/**
	 * Method viewNoGroups.
	 * @param iwc
	 */
	private void viewNoGroups(IWContext iwc) {
		add(getSmallText(localize("managergrouplistview.no_management_group_set", "No management group set")));
	}
	/**
	 * Method initGroupID.
	 * @param iwc
	 */
	private void initGroupID(IWContext iwc) {
		if(iwc.isParameterSet(PARAM_MANAGER_GROUP_ID)){
			try{
				int i = Integer.parseInt(iwc.getParameter(PARAM_MANAGER_GROUP_ID));
				topGroupID=i;
			}
			catch(NumberFormatException ne){
			}
		}
	}
	
	public Object clone(){
		ManagerGroupListView view = (ManagerGroupListView)super.clone();
		if(mainTable!=null){
			view.mainTable=(Table)mainTable.clone();
		}
		return view;
	}
	
	public void add(PresentationObject po) {
		if (mainTable == null) {
			mainTable = new Table();
			mainTable.setCellpadding(14);
			mainTable.setCellspacing(0);
			mainTable.setColor(getBackgroundColor());
			mainTable.setWidth(600);
		}
		mainTable.add(po);
	}
	private int parseAction(IWContext iwc) {
		int action = -1;
		if(topGroupID!=-1){
			//if topGroupID==-1 then it cant vew a manager list
			action=ACTION_VIEW_MANAGER_LIST;
		}	
		if(iwc.isParameterSet(PARAM_MANAGER_GROUP_ID)){
		  action = ACTION_VIEW_MANAGER_GROUP;
		}
		return action;
	}
	private void viewManagerList(IWContext iwc) throws Exception {
		//System.out.println("viewManagerList()");
		
		if(addHeader){
			add(getLocalizedHeader("managergrouplistview.managergroups", "Managergroups:"));
			add(new Break(2));
		}

		//if (iwc.isLoggedOn()) {
			//Collection users = getCommuneUserBusiness(iwc).getAllCommuneAdministrators();
			Group topGroup = getTopGroup();
			Collection groups=null;
			groups = getGroupBusiness(iwc).getChildGroups(topGroup);
			//groups = topGroup.getChildGroups();
			if (groups != null & !groups.isEmpty()) {
				Form f = new Form();
				ColumnList messageList = new ColumnList(2);
				f.add(messageList);
				messageList.setBackroundColor("#e0e0e0");
				messageList.setHeader(localize("managergrouplistview.name", "Name"), 1);

				PresentationObject userName = null;

				//CheckBox deleteCheck = null;
				//boolean isRead = false;
				if (groups != null) {
					Iterator iter = groups.iterator();
					while (iter.hasNext()) {
						try {
							Group group = (Group) iter.next();
							Text tUserName = getSmallText(getStringOrDash(group.getName()));
							Link lUserName = new Link(tUserName);
							userName = lUserName;
							if(managerListPageID!=-1){
								lUserName.setPage(managerListPageID);
							}
							lUserName.addParameter(PARAM_MANAGER_GROUP_ID,group.getPrimaryKey().toString());
							messageList.add(userName);
							Text tDesc = getSmallText(getStringOrDash(group.getDescription()));
							messageList.add(tDesc);

						} catch (Exception e) {
							add(e);
							e.printStackTrace();
						}
						//messageList.add(deleteCheck);
					}
				}
				messageList.skip(2);
				add(f);
			} else {
				add(getSmallText(localize("managergrouplistview.no_managers", "No managers")));
			}
		//}
	}
	/**
	 * Returns the top group to display groups under.
	 * @return Group
	 */
	private Group getTopGroup() {
		try{
			GroupHome gHome = (GroupHome)IDOLookup.getHome(Group.class);
			return gHome.findByPrimaryKey(new Integer(getTopGroupID()));
		}
		catch(Exception e){
			throw new RuntimeException(e.getMessage());	
		}
	}
	
	private void viewManagerGroup(IWContext iwc) throws Exception {
		//System.out.println("viewManager()");
		add(new ManagerListView());
	}
	
	
	/* Commented out since it is never used...
	private CaseBusiness getCaseBusiness(IWContext iwc) throws Exception {
		return (CaseBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CaseBusiness.class);
	}*/
	
	/* Commented out since it is never used...
	private CommuneCaseBusiness getCommuneCaseBusiness(IWContext iwc) throws Exception {
		return (CommuneCaseBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CommuneCaseBusiness.class);
	}*/
	
	/* Commented out since it is never used...
	private UserBusiness getUserBusiness(IWContext iwc) throws Exception {
		return (UserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, UserBusiness.class);
	}*/
	
	private GroupBusiness getGroupBusiness(IWContext iwc) throws Exception {
		return (GroupBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
	}
	
	/* Commented out since it is never used...
	private CommuneUserBusiness getCommuneUserBusiness(IWContext iwc) throws Exception {
		return (CommuneUserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}*/
	
	
	public void setManagerListViewPage(IBPage page) {
		setManagerListViewPage(page.getID());
	}
	public void setManagerListViewPage(int ib_page_id) {
		managerListPageID = ib_page_id;
	}
	public int getManagerListViewPage() {
		return managerListPageID;
	}
	/**
	 * Returns the groupID.
	 * @return int
	 */
	public int getTopGroupID() {
		return topGroupID;
	}
	
	
	public void setTopGroup(Group group){
		int groupID = ((Integer)group.getPrimaryKey()).intValue();
		setTopGroupID(groupID);
	}	

	/**
	 * Sets the groupID.
	 * @param groupID The groupID to set
	 */
	public void setTopGroupID(int groupID) {
		this.topGroupID = groupID;
	}

	/**
	 * Returns the addHeader.
	 * @return boolean
	 */
	public boolean isAddHeader() {
		return addHeader;
	}

	/**
	 * Sets the addHeader.
	 * @param addHeader The addHeader to set
	 */
	public void setAddHeader(boolean addHeader) {
		this.addHeader = addHeader;
	}
	
	/**
	 * Returns a string or dash if there is no string
	 * @param str String to check
	 * @return String which is either a dash or a string if 
	 */
	private String getStringOrDash(String str){
		return StringHandler.getStringOrDash(str);
	}

}
