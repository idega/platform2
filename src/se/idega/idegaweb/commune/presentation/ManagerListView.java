package se.idega.idegaweb.commune.presentation;
import java.util.Collection;
import java.util.Iterator;

import com.idega.builder.data.IBPage;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
/**
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class ManagerListView extends CommuneBlock {
	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	private final static int ACTION_VIEW_MANAGER_LIST = 2;
	private final static int ACTION_VIEW_MANAGER = 3;

	final static String PARAM_MANAGER_ID = ManagerView.PARAM_MANAGER_ID;
	final static String PARAM_MANAGER_GROUP_ID=ManagerGroupListView.PARAM_MANAGER_GROUP_ID;
	
	private Table mainTable = null;
	private int managerPageID = -1;
	private int groupID=-1;
	private boolean addHeader=true;

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	public void main(IWContext iwc)throws Exception {
		try {
			initGroupID(iwc);
			int action = parseAction(iwc);
			//System.out.println("main: action="+action);
			switch (action) {
				case ACTION_VIEW_MANAGER_LIST :
					viewManagerList(iwc);
					break;
				case ACTION_VIEW_MANAGER :
					viewManager();
					break;
				default :
					viewNoGroup();
					break;
			}
			super.add(mainTable);
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}
	/**
	 * Method initGroupID.
	 * @param iwc
	 */
	private void initGroupID(IWContext iwc) {
		if(iwc.isParameterSet(PARAM_MANAGER_GROUP_ID)){
			try{
				int i = Integer.parseInt(iwc.getParameter(PARAM_MANAGER_GROUP_ID));
				groupID=i;
			}
			catch(NumberFormatException ne){
			}
		}
	}
	private int parseAction(IWContext iwc) {
		int action = -1;
		if(groupID!=-1){
			action = ACTION_VIEW_MANAGER_LIST;
		}
		if(iwc.isParameterSet(PARAM_MANAGER_ID)){
		  action = ACTION_VIEW_MANAGER;
		}
		return action;
	}
	
	public Object clone(){
		ManagerListView view = (ManagerListView)super.clone();
		if(mainTable!=null){
			view.mainTable=(Table)mainTable.clone();
		}
		return view;
	}
	
	public void add(PresentationObject po) {
		if (mainTable == null) {
			mainTable = new Table();
			mainTable.setCellpadding(0);
			mainTable.setCellspacing(0);
			//mainTable.setColor(getBackgroundColor());
			mainTable.setWidth("100%");
		}
		mainTable.add(po);
	}

	
	/**
	 * Method viewNoGroups.
	 * @param iwc
	 */
	private void viewNoGroup() {
		add(getSmallText(localize("managerlistview.no_group_set", "No group selected")));
	}

	private void viewManagerList(IWContext iwc) throws Exception {
		//System.out.println("viewManagerList()");
		//add(getLocalizedHeader("managerlistview.managers", "Managers:"));
		Group topGroup = getGroup();
		if(addHeader){
			String header = topGroup.getName();
			add(getHeader(header));
			add(new Break(2));
		}
		
		
		//if (iwc.isLoggedOn()) {
			//Collection users = getCommuneUserBusiness(iwc).getAllCommuneAdministrators();
			
			Collection users = getGroupBusiness(iwc).getUsersDirectlyRelated(topGroup);
			if (users != null & !users.isEmpty()) {
				Table table = new Table();
				table.setCellpadding(getCellpadding());
				table.setCellspacing(getCellspacing());
				int row = 1;
				
				table.add(getSmallHeader(localize("managerlistview.name", "Name")), 1, row);
				table.add(getSmallHeader(localize("managerlistview.description", "Description")), 2, row++);

				User user = null;
				Link userName = null;
				Text description = null;

				if (users != null) {
					Iterator iter = users.iterator();
					while (iter.hasNext()) {
						try {
							user = (User) iter.next();
							userName = getSmallLink(user.getName());
							if(managerPageID!=-1){
								userName.setPage(managerPageID);
							}
							userName.addParameter(PARAM_MANAGER_ID,user.getPrimaryKey().toString());
							table.add(userName, 1, row);
							
							if (user.getDescription() != null) {
								description = getSmallText(user.getDescription());
								table.add(description, 2, row);
							}
						}
						catch (Exception e) {
						}
						table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_TOP);
						row++;
					}
				}
				add(table);
			}
			else {
				add(getSmallText(localize("managerlistview.no_managers", "No managers")));
			}
	}
	
	
	private void viewManager() throws Exception {
		//System.out.println("viewManager()");
		add(new ManagerView());
	}
	
	
	/*
	  private void viewCase(IWContext iwc)throws Exception{
	    Message msg = getMessage(iwc.getParameter(PARAM_MESSAGE_ID),iwc);
	    getMessageBusiness(iwc).markMessageAsRead(msg);
	
	    add(getLocalizedHeader("message.message","Message"));
	    add(new Break(2));
	    add(getLocalizedText("message.from","From"));
	    add(getText(": "));
	    //add(getLink(msg.getSenderName()));
	    add(new Break(2));
	    add(getLocalizedText("message.date","Date"));
	    add(getText(": "+(new IWTimestamp(msg.getCreated())).getLocaleDate(iwc)));
	    add(new Break(2));
	    add(getLocalizedText("message.subject","Subject"));
	    add(getText(": "+msg.getSubject()));
	    add(new Break(2));
	    add(getText(msg.getBody()));
	
	    add(new Break(2));
	    Table t = new Table();
	    t.setWidth("100%");
	    t.setAlignment(1,1,"right");
	    Link l = getLocalizedLink("message.back", "Back");
	    l.addParameter(PARAM_VIEW_MESSAGE_LIST,"true");
	    l.setAsImageButton(true);
	    t.add(l,1,1);
	    add(t);
	  }
	
	  private void showDeleteInfo(IWContext iwc)throws Exception{
	    String[] ids = iwc.getParameterValues(PARAM_MESSAGE_ID);
	    int msgId = 0;
	    int nrOfMessagesToDelete = 0;
	    if(ids!=null){
	      nrOfMessagesToDelete = ids.length;
	      msgId = Integer.parseInt(ids[0]);
	    }
	
	    if(nrOfMessagesToDelete==1){
	      add(getLocalizedHeader("message.delete_message","Delete message"));
	    }else{
	      add(getLocalizedHeader("message.delete_messages","Delete messages"));
	    }
	    add(new Break(2));
	
	    String s = null;
	    if(nrOfMessagesToDelete==0){
	      s = localize("message.no_messages_to_delete","No messages selected. You have to mark the message(s) to delete.");
	    }else if(nrOfMessagesToDelete==1){
	      Message msg = getMessageBusiness(iwc).getUserMessage(msgId);
	      s = localize("message.one_message_to_delete","Do you really want to delete the message with subject: ")+msg.getSubject()+"?";
	    }else{
	      s = localize("message.messages_to_delete","Do you really want to delete the selected messages?");
	    }
	
	    Table t = new Table(1,5);
	    t.setWidth("100%");
	    t.add(getText(s),1,1);
	    t.setAlignment(1,1,"center");
	    if(nrOfMessagesToDelete==0){
	      Link l = getLocalizedLink("message.back","back");
	      l.addParameter(PARAM_VIEW_MESSAGE_LIST,"true");
	      l.setAsImageButton(true);
	      t.add(l,1,4);
	    }else{
	      Link l = getLocalizedLink("message.ok","OK");
	      l.addParameter(PARAM_DELETE_MESSAGE,"true");
	      for(int i=0; i<ids.length; i++){
	        l.addParameter(PARAM_MESSAGE_ID,ids[i]);
	      }
	      l.setAsImageButton(true);
	      t.add(l,1,4);
	      t.add(getText(" "),1,4);
	      l = getLocalizedLink("message.cancel","Cancel");
	      l.addParameter(PARAM_VIEW_MESSAGE_LIST,"true");
	      l.setAsImageButton(true);
	      t.add(l,1,4);
	    }
	    t.setAlignment(1,4,"center");
	    add(t);
	  }
	
	  private void deleteMessage(IWContext iwc)throws Exception{
	    String[] ids = iwc.getParameterValues(PARAM_MESSAGE_ID);
	    for(int i=0; i<ids.length; i++){
	      getMessageBusiness(iwc).deleteUserMessage(Integer.parseInt(ids[i]));
	    }
	  }*/
	
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
	
	/* Commented out since it is never used...
	private CommuneUserBusiness getCommuneUserBusiness(IWContext iwc) throws Exception {
		return (CommuneUserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}*/
	
	
	public void setManagerViewPage(IBPage page) {
		setManagerViewPage(page.getID());
	}
	public void setManagerViewPage(int ib_page_id) {
		managerPageID = ib_page_id;
	}
	public int getManagerViewPage() {
		return managerPageID;
	}
	/**
	 * Returns the groupID.
	 * @return int
	 */
	public int getGroupID() {
		return groupID;
	}

	/**
	 * Sets the groupID.
	 * @param groupID The groupID to set
	 */
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	  	public void setGroup(Group group){
	  		int groupID = ((Integer)group.getPrimaryKey()).intValue();
	  		setGroupID(groupID);
	  	}

	  	/**
	  	 * Returns the top group to display users under.
	  	 * @return Group
	  	 */
	  	private Group getGroup() {
	  		try{
	  			GroupHome gHome = (GroupHome)IDOLookup.getHome(Group.class);
	  			return gHome.findByPrimaryKey(new Integer(getGroupID()));
	  		}
	  		catch(Exception e){
	  			throw new RuntimeException(e.getMessage());
	  		}
	  	}


	protected GroupBusiness getGroupBusiness(IWApplicationContext iwac)throws Exception{
		return (GroupBusiness)com.idega.business.IBOLookup.getServiceInstance(iwac,GroupBusiness.class);
	}

	/**
	 * Returns the addHeader.
	 * @return boolean
	 */
	public boolean getIfAddHeader() {
		return addHeader;
	}

	/**
	 * Sets the addHeader.
	 * @param addHeader The addHeader to set
	 */
	public void setAddHeader(boolean addHeader) {
		this.addHeader = addHeader;
	}

}
