/*
 * Created on Aug 5, 2003
 *
 */
package is.idega.idegaweb.member.presentation;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import is.idega.idegaweb.member.business.MemberFamilyLogic;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;

/**
 * FamilyRelationConnector used to create and remove family specific relations between users
 * @author aron 
 * @version 1.0
 */

public class FamilyRelationConnector extends UserRelationConnector {

	private static final String FAMILY_RELATION_CUSTODIAN_AND_PARENT = "fam_rel_cust_par";

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.presentation.GroupRelationConnector#getRelationMenu(com.idega.presentation.IWContext)
	 */
	public PresentationObject getRelationMenu(IWContext iwc) throws RemoteException {
		if (hasSelectedType()) {
			Text tType = new Text(iwrb.getLocalizedString(type, type));
			return tType;
		}
		else {
			MemberFamilyLogic familyService = getMemberFamilyLogic(iwc);
			DropdownMenu menu = new DropdownMenu(PARAM_TYPE);
			IWResourceBundle iwrb = getResourceBundle(iwc);
			try {
				menu.addMenuElement(
					familyService.getChildRelationType(),
					iwrb.getLocalizedString("usr_fam_win_child", "Child"));
				menu.addMenuElement(
					familyService.getParentRelationType(),
					iwrb.getLocalizedString("usr_fam_win_parent", "Parent"));
				menu.addMenuElement(
					familyService.getCustodianRelationType(),
					iwrb.getLocalizedString("usr_fam_win_custodian", "Custodian"));
				menu.addMenuElement(
					FAMILY_RELATION_CUSTODIAN_AND_PARENT,
					iwrb.getLocalizedString("usr_fam_win_custodian_and_parent", "Custodian and parent"));
				menu.addMenuElement(
					familyService.getSiblingRelationType(),
					iwrb.getLocalizedString("usr_fam_win_sibling", "Sibling"));
				menu.addMenuElement(
					familyService.getSpouseRelationType(),
					iwrb.getLocalizedString("usr_fam_win_spouse", "Spouse"));
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			return menu;
		}
	}

	private MemberFamilyLogic getMemberFamilyLogic(IWApplicationContext iwac) throws RemoteException{
		
		return	(MemberFamilyLogic) IBOLookup.getServiceInstance(iwac, MemberFamilyLogic.class);
		
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.presentation.GroupRelationConnector#createRelation(com.idega.presentation.IWContext, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	public void createRelation(IWContext iwc, Integer userID, Integer relatedUserID, String relationType)
		throws RemoteException {
		try {
			MemberFamilyLogic logic = getMemberFamilyLogic(iwc);
			UserHome userHome  = getUserHome();
			User user = userHome.findByPrimaryKey(userID);
			User relatedUser = userHome.findByPrimaryKey(relatedUserID);
			if (relationType.equals(logic.getChildRelationType())) {
				logic.setAsChildFor(user,relatedUser);
			}
			else if (relationType.equals(logic.getParentRelationType())) {
				logic.setAsParentFor(user,relatedUser);
			}
			else if (relationType.equals(logic.getSpouseRelationType())) {
				logic.setAsSpouseFor(user,relatedUser);
			}
			else if (relationType.equals(logic.getSiblingRelationType())) {
				logic.setAsSiblingFor(user,relatedUser);
			}
			else if (relationType.equals(logic.getCustodianRelationType())) {
				logic.setAsCustodianFor(user,relatedUser);
			}
			else if (relationType.equals(FAMILY_RELATION_CUSTODIAN_AND_PARENT)) {
				logic.setAsParentFor(user,relatedUser);
				logic.setAsCustodianFor(user,relatedUser);
			}
			else
				super.createRelation(iwc, userID, relatedUserID, relationType);
		}
		catch (FinderException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
		catch (CreateException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.presentation.GroupRelationConnector#deleteRelation(com.idega.presentation.IWContext, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	public void removeRelation(IWContext iwc, Integer userID, Integer relatedUserID, String relationType)
		throws RemoteException {
		try {
			MemberFamilyLogic logic = getMemberFamilyLogic(iwc); 
			UserHome userHome = getUserHome();
			User user = userHome.findByPrimaryKey(userID);
			User relatedUser = userHome.findByPrimaryKey(relatedUserID);
			if (relationType.equals(logic.getChildRelationType())) {
				logic.removeAsChildFor(user,relatedUser);
			}
			else if (relationType.equals(logic.getParentRelationType())) {
				logic.removeAsParentFor(user,relatedUser);
			}
			else if (relationType.equals(logic.getSpouseRelationType())) {
				logic.removeAsSpouseFor(user,relatedUser);
			}
			else if (relationType.equals(logic.getSiblingRelationType())) {
				logic.removeAsSiblingFor(user,relatedUser);
			}
			else if (relationType.equals(logic.getCustodianRelationType())) {
				logic.removeAsCustodianFor(user,relatedUser);
			}
			else if (relationType.equals(FAMILY_RELATION_CUSTODIAN_AND_PARENT)) {
				logic.removeAsParentFor(user,relatedUser);
				logic.removeAsCustodianFor(user,relatedUser);
			}
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (RemoveException e) {
			e.printStackTrace();
		}
	}

}
