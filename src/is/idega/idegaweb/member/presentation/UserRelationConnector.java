/*
 * Created on Aug 1, 2003
 *
 */
package is.idega.idegaweb.member.presentation;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.builder.business.BuilderLogic;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
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
import com.idega.user.presentation.UserSearcher;
import com.idega.util.IWTimestamp;
import com.idega.util.URLUtil;
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
	public static final String PARAM_RELOAD_PAGE_ID = "rldpage";
	public static final String PARAM_RELOAD_USER_PRM_NAME ="rlduidprmnm";
	public static final int ACTION_ATTACH = 1;
	public static final int ACTION_DETACH = 2;
	public static final int PARAM_ATTACH = 4;
	public static final int PARAM_DETACH = 5;
	public static final int ACTION_SAVE = 3;
	protected int action = -1;
	protected Integer userID = null;
	protected Integer parentPageID = null;
	protected String userIDParameterName = null;
	protected User user = null;
	protected User relatedUser = null;
	protected String type = null, rtype = null;
	private static String searchIdentifer = "fmrels";
	protected String buttonStyle =
		"color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:normal;border-width:1px;border-style:solid;border-color:#000000;";
	protected String interfaceStyle =
		"color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:normal;border-width:1px;border-style:solid;border-color:#000000;";
	protected String processExceptionMessage =null;
	protected boolean showCurrentRelations = true;
		
	/**
		 * 
		 */
		public UserRelationConnector() {
			this.setWidth(550);
			this.setHeight(400);
			this.setScrollbar(true);
			this.setResizable(true);
		    this.setAllMargins(0);
		}

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
		//debugParameters(iwc);
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		
		try {
			//iwc.getApplication().getLog().info("Who is your daddy ?");
			process(iwc);
		}
		catch (RemoteException e) {
			processExceptionMessage = e.getMessage();
		}
		//System.err.println("user is null ? " + (user == null));
		
		presentate(iwc);

	}

	private void presentate(IWContext iwc) throws RemoteException {
		Table mainTable = new Table();
		int row = 1;

		UserSearcher searcher = new UserSearcher();
		searcher.setPersonalIDLength(15);
		searcher.setFirstNameLength(25);
		searcher.setLastNameLength(25);
		searcher.setShowMiddleNameInSearch(false);
		searcher.maintainParameter(new Parameter(PARAM_USER_ID, userID.toString()));
		//searcher.maintainParameter(new Parameter(PARAM_ACTION,String.valueOf( action)));
		if (type != null)
			searcher.maintainParameter(new Parameter(PARAM_TYPE, type));
		if (rtype != null)
			searcher.maintainParameter(new Parameter(PARAM_REVERSE_TYPE, rtype));
		if(parentPageID!=null)
			searcher.maintainParameter(new Parameter(PARAM_RELOAD_PAGE_ID,parentPageID.toString()));
		if(userIDParameterName!=null)
			searcher.maintainParameter(new Parameter(PARAM_RELOAD_USER_PRM_NAME,userIDParameterName));
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

		Text tCurrentUser = new Text(iwrb.getLocalizedString("current_user", "Current user"));
		tCurrentUser.setBold();
		mainTable.add(tCurrentUser, 1, row++);
		Text tCurrentUserName = new Text(user.getName());
		Text tCurrentUserPersonalID = new Text(user.getPersonalID());
		mainTable.add(tCurrentUserPersonalID, 1, row);
		mainTable.add(Text.getNonBrakingSpace(), 1, row);
		mainTable.add(Text.getNonBrakingSpace(), 1, row);
		mainTable.add(tCurrentUserName, 1, row);

		row++;

		Text tRelatedUser = new Text(iwrb.getLocalizedString("relation_user", "Relation user"));
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
			mainTable.add(Text.getBreak(), 1, row);
			row++;
			
			if(showCurrentRelations){
			try {
				
				Collection relations = getRelationHome().findGroupsRelationshipsContainingUniDirectional(((Integer)user.getPrimaryKey()).intValue(),((Integer)relatedUser.getPrimaryKey()).intValue());
			
				int trow = 1;
				if(relations!=null &&!relations.isEmpty()){
					Table relationsTable = new Table();
					Text tCurrentRelations = new Text(iwrb.getLocalizedString("current_relations_to", "Current Relations to ")+relatedUser.getFirstName());
					tCurrentRelations.setBold();
					mainTable.add(tCurrentRelations,1,row++);
					relationsTable.add(iwrb.getLocalizedString("relation_type","Relation type"),1,trow);
					relationsTable.add(iwrb.getLocalizedString("created","Created"),2,trow);
					trow++;
					for (Iterator iter = relations.iterator(); iter.hasNext();) {
						GroupRelation element = (GroupRelation) iter.next();
						relationsTable.add(iwrb.getLocalizedString(element.getRelationshipType(),element.getRelationshipType()),1,trow);
						relationsTable.add(new IWTimestamp(element.getInitiationDate()).getLocaleDateAndTime(iwc.getCurrentLocale()),2,trow);
						//relationsTable.add(element.getStatus(),3,trow);
						
						trow++;
					}
				mainTable.add(relationsTable,1,row++);
				mainTable.add(Text.getBreak(), 1, row++);
				}
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (EJBException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
			}
			
			Text tManAct = new Text(iwrb.getLocalizedString("managing_action", "Managing action"));
			tManAct.setBold();
			mainTable.add(tManAct,1,row++);
			String sUserFirstName = (user.getFirstName());
			String sRelatedUserFirstName = (relatedUser.getFirstName());
			String sWillBeRelatedAs = (iwrb.getLocalizedString("related_as","related as"));
			String sTo = iwrb.getLocalizedString("to","to");
			String reltype = null;
			if(hasSelectedType() || hasSelectedReverseType()){
				if(hasSelectedType()){
					reltype = type;
					String sRelationtype = (iwrb.getLocalizedString(type, type));
					String sentence = sUserFirstName+" "+sWillBeRelatedAs+" "+sRelationtype+" "+sTo+" "+sRelatedUserFirstName;
					mainTable.add(new Text(sentence),1,row++);
				}
				
				if (hasSelectedReverseType()){
					reltype = rtype;
					String sReverseRelationtype = (iwrb.getLocalizedString(rtype,rtype));
					String sentence = sRelatedUserFirstName+" "+sWillBeRelatedAs+" "+sReverseRelationtype+" "+sTo+" "+sUserFirstName;
					mainTable.add(new Text(sentence),1,row++);
				}
			}
			else{
				
				mainTable.add(getRelationMenu(iwc), 1, row);
			}
			
			row++;

			mainTable.add(Text.getBreak(), 1, row);
			// show button when the planned relationship is legal
			// or when asking for a delete
			if(reltype!=null && (isDetachRequest() ||  isRelationshipLegal(iwc,user,relatedUser,  reltype)) ){
				mainTable.add(getActionButton(iwc,user,relatedUser,  type,rtype), 1, row++);
				
			}
			else if(processExceptionMessage!=null){
				Text errorText = new Text(processExceptionMessage);
				errorText.setBold(true);
				errorText.setFontColor("#FF0000");
				mainTable.add(errorText,1,row++);
			}
			else{
				String errorString ="";
				if(hasSelectedType())
					errorString = getIllegalRelationMessage(iwc,user, relatedUser,reltype);
				else
					errorString = getIllegalRelationMessage(iwc,relatedUser, user,reltype);
					
				Text errorText = new Text(errorString);
				errorText.setBold(true);
				errorText.setFontColor("#FF0000");
				mainTable.add(errorText,1,row++);
			}
			
			//  show replacement type if specified
			String replacementType = getReplacementRelationType(reltype);
			if(replacementType!=null){
				// create question and link
				
			}			
			mainTable.add(searcher.getUniqueUserParameter((Integer) relatedUser.getPrimaryKey()));
		}
		
		CloseButton cancelButton = new CloseButton(iwrb.getLocalizedString("cancel","Cancel"));
		cancelButton.setStyleAttribute(buttonStyle);
		
		mainTable.add(cancelButton,1,row);

		Form form = new Form();
		//if (user != null)
		//form.addParameter(PARAM_USER_ID, user.getPrimaryKey().toString());
		form.maintainParameter(PARAM_USER_ID);
		form.maintainParameter(UserSearcher.getUniqueUserParameterName(searchIdentifer));
		if (type != null)
			form.maintainParameter(PARAM_TYPE);
		if (rtype != null)
			form.maintainParameter(PARAM_REVERSE_TYPE);
		if(parentPageID!=null)
			form.maintainParameter(PARAM_RELOAD_PAGE_ID);
		if(userIDParameterName!=null)
			form.maintainParameter(PARAM_RELOAD_USER_PRM_NAME);
		form.add(mainTable);
		add(form);

	}
	
	protected boolean  isRelationshipLegal(IWContext iwc,User roleUser, User victimUser, String relationType) {
		return true;
	}
	
	protected String  getIllegalRelationMessage(IWContext iwc,User roleUser, User victimUser, String relationType) {
		String pattern = (iwrb.getLocalizedString("illegal_relationship_msg","Illegal to add relation of type {0} between  {1} and {2}"));
		String[] objs = {relationType,roleUser.getFirstName(),victimUser.getFirstName()};
		return MessageFormat.format(pattern,objs);
	}
	
	
	/**
	 * Returns the replacement relation type 
	 * when the specified type is illegal.
	 * The specified type relation will be removed
	 * and the other put type relation created instead
	 * @return the replacement type else null
	 */
	protected String getReplacementRelationType(String relationType){
		return null;
	}

	private PresentationObject getActionButton(IWContext iwc,User roleUser, User victimUser, String mainType,String reverseType) {
		// if we have a relation we offer a remove action
		//System.err.println("type "+mainType+" reversetype "+reverseType);
		if (hasActiveRelations(iwc,roleUser,victimUser,mainType,reverseType)) {
			String detachWarning = 
			iwrb.getLocalizedString("warning_detach_relation", "Are you shure you want to remove this relation ?");
			SubmitButton detach =
			getActionButton(iwrb.getLocalizedString("detach", "Detach"), ACTION_DETACH, detachWarning);
			return detach;
		}
		else {
			String attachWarning =
				iwrb.getLocalizedString(
					"warning_attach_relation",
					"Are you shure you want to relate these two people ?");
			SubmitButton attach =
				getActionButton(iwrb.getLocalizedString("attach", "Attach"), ACTION_ATTACH, attachWarning);
			return attach;
		}
}

protected boolean hasActiveRelations(IWContext iwc,User roleUser, User victimUser, String mainType,String reverseType){
	boolean returner = true , reltypes = false;
	
	if(mainType!=null){
		returner &=( roleUser.hasRelationTo(((Integer) victimUser.getUserGroup().getPrimaryKey()).intValue(), mainType));
		reltypes = true;
	}
	if(reverseType!=null)	{
 		returner &=(victimUser.hasRelationTo(((Integer) roleUser.getUserGroup().getPrimaryKey()).intValue(), reverseType));
		reltypes = true;
	} 
 	returner &=reltypes;
 	return returner;
}

protected SubmitButton getActionButton(String display, int action, String warningMsg) {
	SubmitButton btnAction = new SubmitButton(display, PARAM_ACTION, Integer.toString(action));
	btnAction.setOnClickConfirm(warningMsg);
	btnAction.setStyleAttribute(buttonStyle);
	return btnAction;
}

public static String getRelatedUserParameterName() {
	return UserSearcher.getUniqueUserParameterName(searchIdentifer);
}

public void process(IWContext iwc) throws RemoteException {
	user = getUser(iwc);
	type = iwc.getParameter(PARAM_TYPE);
	rtype = iwc.getParameter(PARAM_REVERSE_TYPE);
	if(iwc.isParameterSet(PARAM_RELOAD_PAGE_ID))
		parentPageID = Integer.valueOf(iwc.getParameter(PARAM_RELOAD_PAGE_ID));
	if(iwc.isParameterSet(PARAM_RELOAD_USER_PRM_NAME))
		userIDParameterName =iwc.getParameter(PARAM_RELOAD_USER_PRM_NAME);
	if (iwc.isParameterSet(PARAM_ACTION)) {
		boolean Continue = false;
		action = Integer.parseInt(iwc.getParameter(PARAM_ACTION));
		String relUID =iwc.getParameter(UserSearcher.getUniqueUserParameterName(searchIdentifer));
		if(relUID!=null){
		Integer relatedUserID =	Integer.valueOf(relUID);
		switch (action) {
			case ACTION_ATTACH :
				createRelation(iwc, (Integer) user.getPrimaryKey(), relatedUserID, type, rtype);
				Continue = true;
				break;

			case ACTION_DETACH :
				removeRelation(iwc, (Integer) user.getPrimaryKey(), relatedUserID, type, rtype);
				Continue = true;
				break;
			case PARAM_ATTACH:
				Continue = false;
				
			break;
			
			case PARAM_DETACH:
				Continue = false;
			break;
			 default: Continue = false;
		}}
		if(Continue){
		if(parentPageID!=null && userIDParameterName!=null){
			URLUtil URL = new URLUtil(BuilderLogic.getInstance().getIBPageURL(iwc, parentPageID.intValue()));
			URL.addParameter(userIDParameterName, userID.toString());
			setParentToRedirect(URL.toString());
			close();
		}
		else{
			setParentToReload();
			close();
		}}
	}

}

private boolean isAttachRequest(){
	return this.action==PARAM_ATTACH;
}
private boolean isDetachRequest(){
	return this.action==PARAM_DETACH;
}

/**
 * Returns true if a specific relation type has been requested
 * @return
 */
public boolean hasSelectedType() {
	return type != null;
}

/**
 * Returns true if a specific relation type has been requested
 * @return
 */
public boolean hasSelectedReverseType() {
	return rtype != null;
}

/**
 * Displays the selected relationtype or the selectable menu with available relation types
 * 
 */
public PresentationObject getRelationMenu(IWContext iwc) throws RemoteException {
	if (hasSelectedType()) {
		Text tType = new Text(iwrb.getLocalizedString(type, type));
		return tType;
	}
	else if (hasSelectedReverseType()) {
		Text tReverseType = new Text(iwrb.getLocalizedString(rtype, rtype));
		return tReverseType;
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
public void removeRelation(
	IWContext iwc,
	Integer userID,
	Integer relatedUserID,
	String relationType,
	String reverseRelationType)
	throws RemoteException {
	try {
		UserHome userHome = getUserHome();
		User currentUser = userHome.findByPrimaryKey(userID);
		User relatedUser = userHome.findByPrimaryKey(relatedUserID);
		currentUser.removeRelation(relatedUser, relationType);
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
public void createRelation(
	IWContext iwc,
	Integer userID,
	Integer relatedUserID,
	String relationType,
	String reverseRelationType)
	throws RemoteException {

	try {
		UserHome userHome = getUserHome();
		User currentUser = userHome.findByPrimaryKey(userID);
		User relatedUser = userHome.findByPrimaryKey(relatedUserID);
		currentUser.addRelation(relatedUser, relationType);

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
