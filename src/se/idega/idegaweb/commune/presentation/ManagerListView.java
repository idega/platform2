package se.idega.idegaweb.commune.presentation;
import java.text.DateFormat;
import java.util.*;
import javax.ejb.EJBException;
import se.idega.idegaweb.commune.message.data.*;
import se.idega.idegaweb.commune.business.CommuneCaseBusiness;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.message.business.*;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;
import com.idega.builder.data.IBPage;
import com.idega.user.data.User;
import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.user.Converter;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;
/**
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class ManagerListView extends CommuneBlock {
	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	private final static int ACTION_VIEW_MANAGER_LIST = 2;
	private final static int ACTION_VIEW_MANAGER = 3;

	private final static String PARAM_MANAGER_ID = ManagerView.PARAM_MANAGER_ID;
	private Table mainTable = null;
	private int managerPageID = -1;


	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	public void main(IWContext iwc)throws Exception {
		try {
			int action = parseAction(iwc);
			//System.out.println("main: action="+action);
			switch (action) {
				case ACTION_VIEW_MANAGER_LIST :
					viewManagerList(iwc);
					break;
				case ACTION_VIEW_MANAGER :
					viewManager(iwc);
					break;
				default :
					break;
			}
			super.add(mainTable);
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
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
			mainTable.setCellpadding(14);
			mainTable.setCellspacing(0);
			mainTable.setColor(getBackgroundColor());
			mainTable.setWidth(600);
		}
		mainTable.add(po);
	}
	private int parseAction(IWContext iwc) {
		int action = ACTION_VIEW_MANAGER_LIST;
		if(iwc.isParameterSet(PARAM_MANAGER_ID)){
		  action = ACTION_VIEW_MANAGER;
		}
		return action;
	}
	private void viewManagerList(IWContext iwc) throws Exception {
		//System.out.println("viewManagerList()");
		add(getLocalizedHeader("managerlistview.managers", "Managers:"));
		add(new Break(2));
		//if (iwc.isLoggedOn()) {
			Collection users = getCommuneUserBusiness(iwc).getAllCommuneAdministrators();
			if (users != null & !users.isEmpty()) {
				Form f = new Form();
				ColumnList messageList = new ColumnList(3);
				f.add(messageList);
				messageList.setBackroundColor("#e0e0e0");
				messageList.setHeader(localize("managerlistview.name", "Name"), 1);

				PresentationObject userName = null;

				//CheckBox deleteCheck = null;
				boolean isRead = false;
				if (users != null) {
					Iterator iter = users.iterator();
					while (iter.hasNext()) {
						try {
							User user = (User) iter.next();
							Text tUserName = getSmallText(user.getName());
							Link lUserName = new Link(tUserName);
							userName = lUserName;
							if(managerPageID!=-1){
								lUserName.setPage(managerPageID);
							}
							lUserName.addParameter(PARAM_MANAGER_ID,user.getPrimaryKey().toString());
							messageList.add(userName);

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
				add(getSmallText(localize("managerlistview.no_managers", "No managers")));
			}
		//}
	}
	
	
	private void viewManager(IWContext iwc) throws Exception {
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
	private CaseBusiness getCaseBusiness(IWContext iwc) throws Exception {
		return (CaseBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CaseBusiness.class);
	}
	private CommuneCaseBusiness getCommuneCaseBusiness(IWContext iwc) throws Exception {
		return (CommuneCaseBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CommuneCaseBusiness.class);
	}
	private UserBusiness getUserBusiness(IWContext iwc) throws Exception {
		return (UserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, UserBusiness.class);
	}
	
	private CommuneUserBusiness getCommuneUserBusiness(IWContext iwc) throws Exception {
		return (CommuneUserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}	
	
	
	public void setManagerViewPage(IBPage page) {
		setManagerViewPage(page.getID());
	}
	public void setManagerViewPage(int ib_page_id) {
		managerPageID = ib_page_id;
	}
	public int getManagerViewPage() {
		return managerPageID;
	}
}
