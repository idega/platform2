/*
 * Created on Aug 1, 2003
 *
 */
package is.idega.idegaweb.member.presentation;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.ibm.icu.text.SearchIterator;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Window;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.GroupRelationHome;
import com.idega.user.data.GroupRelationType;
import com.idega.user.data.GroupRelationTypeHome;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
/**
 * GroupRelationConnector used to add or remove user relations to found/chosen user
 * @author aron 
 * @version 1.0
 */
public class UserRelationConnector extends Window {
	protected String bundleIdentifier = "is.idega.idegaweb.member";
	protected IWBundle iwb;
	protected IWResourceBundle iwrb;
	public static final String PARAM_USER_ID = "ic_user_id";
	//public static final String PARAM_RELATED_USER_ID = UserSearcher.PRM_USER_ID;
	public static final String PARAM_ACTION = "action";
	public static final String PARAM_TYPE = "type";
	public static final String PARAM_REVERSE_TYPE = "rtype";
	public static final int ACTION_ATTACH = 1;
	public static final int ACTION_DETACH = 2;
	public static final int ACTION_SAVE = 3;
	protected int action = -1;
	protected Integer userID = null;
	protected User user = null;
	protected User relatedUser = null;
	protected String type = null,rtype = null;
	private static String searchIdentifer = "fmrels";
	protected String buttonStyle = "color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:normal;border-width:1px;border-style:solid;border-color:#000000;";
	protected String interfaceStyle = "color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:normal;border-width:1px;border-style:solid;border-color:#000000;";

	protected User getUser(IWContext iwc) throws RemoteException {
		userID = Integer.valueOf(iwc.getParameter(PARAM_USER_ID));
		return getUserService(iwc).getUser(userID);
	}

	/**
	 * Gets the requested action method
	 * @param iwc
	 * @return 
	 */
	protected int parseAction(IWContext iwc) {
		try {
			return Integer.parseInt(iwc.getParameter(PARAM_ACTION));
		}
		catch (NumberFormatException nfe) {
			return ACTION_ATTACH;
		}
	}

	public UserBusiness getUserService(IWApplicationContext iwc) throws RemoteException {
		return (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
	}

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return bundleIdentifier;
	}
	/**
	 * @param string
	 */
	public void setBundleIdentifier(String string) {
		bundleIdentifier = string;
	}
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		debugParameters(iwc);
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		//iwc.getApplication().getLog().info("Who is your daddy ?");
		process(iwc);
		System.err.println("user is null ? " + (user == null));
		presentate(iwc);

	}

	private void presentate(IWContext iwc) throws RemoteException{
		Table mainTable = new Table();
		int row = 1;

		UserSearcher searcher = new UserSearcher();
		searcher.setShowMiddleNameInSearch(false);
		searcher.maintainParameter(new Parameter(PARAM_USER_ID, userID.toString()));
		searcher.maintainParameter(new Parameter(PARAM_TYPE, type));
		searcher.setUniqueIdentifier(searchIdentifer);
		searcher.setOwnFormContainer(false);

		if (relatedUser != null) {
			searcher.setUser(relatedUser);
		}
		else if (relatedUser == null) {
			try {
				searcher.process(iwc);
				relatedUser = searcher.getUser();
			}
			catch (RemoteException e1) {
				e1.printStackTrace();
			}
			catch (FinderException e1) {
				e1.printStackTrace();
			}
		}

		mainTable.add((searcher), 1, row++);
		mainTable.add(Text.getBreak(), 1, row++);

		Text tCurrentUser = new Text(iwrb.getLocalizedString("current_user", "Current user") );
		tCurrentUser.setBold();
		mainTable.add(tCurrentUser, 1, row++);
		Text tCurrentUserName = new Text(user.getName());
		Text tCurrentUserPersonalID = new Text(user.getPersonalID());
		mainTable.add(tCurrentUserPersonalID, 1, row);
		mainTable.add(Text.getNonBrakingSpace(), 1, row);
		mainTable.add(Text.getNonBrakingSpace(), 1, row);
		mainTable.add(tCurrentUserName, 1, row);

		row++;

		Text tRelatedUser = new Text(iwrb.getLocalizedString("relation_user", "Relation user") );
		tRelatedUser.setBold();
		mainTable.add(tRelatedUser, 1, row++);
		if (relatedUser != null) {
			Text tRelatedUserName = new Text(relatedUser.getName());
			Text tRelatedUserPersonalID = new Text(relatedUser.getPersonalID());
			mainTable.add(tRelatedUserPersonalID, 1, row);
			mainTable.add(Text.getNonBrakingSpace(), 1, row);
			mainTable.add(Text.getNonBrakingSpace(), 1, row);
			mainTable.add(tRelatedUserName, 1, row);
			row++;
			
			Text tRelationtype = new Text(iwrb.getLocalizedString("relation_type","Relation type"));
			tRelationtype.setBold();
			mainTable.add(tRelationtype,1,row++);
			mainTable.add(Text.getNonBrakingSpace(),1,row);
			mainTable.add(Text.getNonBrakingSpace(),1,row);
			mainTable.add(getRelationMenu(iwc),1,row);
			row++;
			
			mainTable.add(Text.getNonBrakingSpace(),1,row);
			mainTable.add(getActionButton(relatedUser,user,type),1,row);
			
			mainTable.add(searcher.getUniqueUserParameter((Integer)relatedUser.getPrimaryKey()));
		}

		Form form = new Form();
		//if (user != null)
		//form.addParameter(PARAM_USER_ID, user.getPrimaryKey().toString());
		form.maintainParameter(PARAM_USER_ID);
		form.maintainParameter(UserSearcher.getUniqueUserParameterName(searchIdentifer));
		form.maintainParameter(PARAM_TYPE);
		form.add(mainTable);
		add(form);

	}

	private PresentationObject getActionButton(User roleUser,User victimUser,String type) {
		// if we have a relation we offer a remove action
		if(victimUser.hasRelationTo(  ( (Integer) roleUser.getUserGroup().getPrimaryKey()).intValue() ,type) ){
			String detachWarning = iwrb.getLocalizedString("warning_detach_relation","Are you shure you want to remove this relation ?");
			SubmitButton detach = getActionButton(iwrb.getLocalizedString("detach","Detach"),ACTION_DETACH,detachWarning);
			return detach;
		}else{
			String attachWarning = iwrb.getLocalizedString("warning_attach_relation","Are you shure you want to relate these two people ?");
			SubmitButton attach = getActionButton(iwrb.getLocalizedString("attach","Attach"),ACTION_ATTACH,attachWarning);
			return attach;
		}
	}
	
	protected SubmitButton getActionButton(String display,int action,String warningMsg){
		SubmitButton btnAction = new SubmitButton(display,PARAM_ACTION,Integer.toString(action));
		btnAction.setOnClickConfirm(warningMsg);
		return btnAction;
	}
	
	public static String getRelatedUserParameterName(){
		return UserSearcher.getUniqueUserParameterName(searchIdentifer);
	}

	public void process(IWContext iwc) throws RemoteException {
		user = getUser(iwc);
		type = iwc.getParameter(PARAM_TYPE);
		rtype = iwc.getParameter(PARAM_REVERSE_TYPE);
		if(iwc.isParameterSet(PARAM_ACTION)){
			int action = Integer.parseInt(iwc.getParameter(PARAM_ACTION));
			Integer relatedUserID = Integer.valueOf(iwc.getParameter(UserSearcher.getUniqueUserParameterName(searchIdentifer)));
			switch (action) {
				case ACTION_ATTACH :
					System.out.println("createrelation: "+user.getPrimaryKey().toString()+","+relatedUserID.toString()+","+type);
					createRelation(iwc,(Integer)user.getPrimaryKey(),relatedUserID,type,rtype);
				break;

				case ACTION_DETACH:
					removeRelation(iwc,(Integer)user.getPrimaryKey(),relatedUserID,type,rtype);
				break;
			}
			setParentToReload();
			close();
		}
		
	}

	/**
	 * Returns true if a specific relation type has been requested
	 * @return
	 */
	public boolean hasSelectedType(){
		return type!=null;
	}
	
	/**
	 * Displays the selected relationtype or the selectable menu with available relation types
	 * 
	 */
	public PresentationObject getRelationMenu(IWContext iwc) throws RemoteException{
		if (hasSelectedType()) {
			Text tType = new Text(iwrb.getLocalizedString(type, type));
			return tType;
		}
		else {
			return getAllRelationTypesMenu(iwc);
		}

	}

	private DropdownMenu getAllRelationTypesMenu(IWContext iwc) {
		DropdownMenu menu = new DropdownMenu(PARAM_TYPE);
		try {
			Collection types = getRelationTypeHome().findAll();

			for (Iterator iter = types.iterator(); iter.hasNext();) {
				GroupRelationType relType = (GroupRelationType) iter.next();
				menu.addMenuElement(relType.getType(), iwrb.getLocalizedString(relType.getType(), relType.getType()));
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return menu;
	}
	
	/**
	 * Called when the detach action is requested
	 * @param iwc
	 * @param userID
	 * @param relatedUserID
	 * @param relationType
	 * @throws RemoteException
	 */
	public void removeRelation(IWContext iwc,Integer userID, Integer relatedUserID, String relationType,String reverseRelationType) throws RemoteException{
		try {
			UserHome userHome = getUserHome();
			User currentUser = userHome.findByPrimaryKey(userID);
			User relatedUser = userHome.findByPrimaryKey(relatedUserID);
			currentUser.removeRelation(relatedUser,relationType);
		}
		
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (RemoveException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Called when the attach action is requested
	 * @param iwc
	 * @param userID
	 * @param relatedUserID
	 * @param relationType
	 * @throws RemoteException
	 */
	public void createRelation(IWContext iwc,Integer userID, Integer relatedUserID, String relationType,String reverseRelationType) throws RemoteException {
		
		try {
			UserHome userHome = getUserHome();
			User currentUser = userHome.findByPrimaryKey(userID);
			User relatedUser = userHome.findByPrimaryKey(relatedUserID);
			currentUser.addUniqueRelation(relatedUser,relationType);
			
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
	}

	public void addInputForm(IWContext iwc) {
		
	}

	public void addConfirmationForm(IWContext iwc) {

	}
	
	public GroupRelationHome getRelationHome() throws RemoteException {
		return (GroupRelationHome) IDOLookup.getHome(GroupRelation.class);
	}

	public GroupRelationTypeHome getRelationTypeHome() throws RemoteException {
		return (GroupRelationTypeHome) IDOLookup.getHome(GroupRelationType.class);
	}
	
	public UserHome getUserHome() throws RemoteException {
			return (UserHome) IDOLookup.getHome(User.class);
	}


}
