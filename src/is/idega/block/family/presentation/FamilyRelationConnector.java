/*
 * Created on Aug 5, 2003
 *
 */
package is.idega.block.family.presentation;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import is.idega.block.family.business.FamilyLogic;

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
		else if(hasSelectedReverseType()){
			Text tReverseType = new Text(iwrb.getLocalizedString(rtype,rtype));
			return tReverseType;
		}
		else {
			FamilyLogic familyService = getMemberFamilyLogic(iwc);
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
				menu.addMenuElement(
					familyService.getCohabitantRelationType(),
					iwrb.getLocalizedString("usr_fam_win_cohabitant", "Cohabitant"));
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			return menu;
		}
	}

	protected FamilyLogic getMemberFamilyLogic(IWApplicationContext iwac) throws RemoteException{
		
		return	(FamilyLogic) IBOLookup.getServiceInstance(iwac, FamilyLogic.class);
		
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.presentation.GroupRelationConnector#createRelation(com.idega.presentation.IWContext, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	public void createRelation(IWContext iwc, Integer userID, Integer relatedUserID, String relationType,String reverseRelationType)
		throws RemoteException {
			debug("adding relations to "+userID+" with "+relatedUserID+" type "+relationType+" reverse-type "+reverseRelationType);
		try {
			FamilyLogic logic = getMemberFamilyLogic(iwc);
			UserHome userHome  = getUserHome();
			User user = userHome.findByPrimaryKey(userID);
			User relatedUser = userHome.findByPrimaryKey(relatedUserID);
			if(relationType==null && reverseRelationType!=null){
				//swapping of relations and users
				String tmprel = relationType;
				relationType = reverseRelationType;
				reverseRelationType = tmprel;
				User temp = relatedUser;
				relatedUser = user;
				user = temp;
			}			
			
			if (relationType.equals(logic.getChildRelationType())) {
				if(reverseRelationType!=null){
					// if parential child
					if(reverseRelationType.equalsIgnoreCase(logic.getParentRelationType())){
						logic.setAsParentFor(relatedUser,user);
					}
					else if(reverseRelationType.equalsIgnoreCase(logic.getCustodianRelationType())){
						logic.setAsCustodianFor(relatedUser,user);
					}
				}
				else
					logic.setAsChildFor(user,relatedUser);
			}
			else if (relationType.equals(logic.getParentRelationType())) {
				logic.setAsParentFor(user,relatedUser);
			}
			else if (relationType.equals(logic.getSpouseRelationType())) {
				logic.setAsSpouseFor(user,relatedUser);
			}
			else if (relationType.equals(logic.getCohabitantRelationType())) {
				logic.setAsCohabitantFor(user,relatedUser);
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
				super.createRelation(iwc, userID, relatedUserID, relationType,reverseRelationType);
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
	public void removeRelation(IWContext iwc, Integer userID, Integer relatedUserID, String relationType,String reverseRelationType)
		throws RemoteException {
			debug("removing relations to "+userID+" with "+relatedUserID+" type "+relationType+" reverse-type "+reverseRelationType);
		try {
			FamilyLogic logic = getMemberFamilyLogic(iwc); 
			UserHome userHome = getUserHome();
			User user = userHome.findByPrimaryKey(userID);
			User relatedUser = userHome.findByPrimaryKey(relatedUserID);
			if(relationType==null && reverseRelationType!=null){
				relationType = reverseRelationType;
				User temp = relatedUser;
				relatedUser = user;
				user = temp;
			}
			if (relationType.equals(logic.getChildRelationType())) {
				logic.removeAsChildFor(user,relatedUser);
			}
			else if (relationType.equals(logic.getParentRelationType())) {
				logic.removeAsParentFor(user,relatedUser);
			}
			else if (relationType.equals(logic.getSpouseRelationType())) {
				logic.removeAsSpouseFor(user,relatedUser);
			}
			else if (relationType.equals(logic.getCohabitantRelationType())) {
				logic.removeAsCohabitantFor(user,relatedUser);
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
			else{
				super.removeRelation(iwc,userID,relatedUserID,relationType,reverseRelationType);
			}
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (RemoveException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.presentation.UserRelationConnector#hasActiveRelations(com.idega.user.data.User, com.idega.user.data.User, java.lang.String, java.lang.String)
	 */
	protected boolean hasActiveRelations(IWContext iwc,User roleUser, User victimUser, String mainType, String reverseType) {
		try {
			FamilyLogic familyService = getMemberFamilyLogic(iwc);
			String childType = familyService.getChildRelationType();
			if(mainType!=null && reverseType!=null){
				int roleUserID = ((Integer)roleUser.getUserGroup().getPrimaryKey()).intValue();
				int victimUserID = ((Integer)victimUser.getUserGroup().getPrimaryKey()).intValue();
				if(childType.equalsIgnoreCase(mainType)){
					//if(reverseType.equalsIgnoreCase(familyService.getParentRelationType()))
						return victimUser.hasRelationTo(roleUserID,reverseType);
					//else if (reverseType.equalsIgnoreCase(familyService.getCustodianRelationType()))
					//	return victimUser.hasRelationTo(roleUserID,reverseType);
				}
				else if(childType.equalsIgnoreCase(reverseType)){
					return roleUser.hasRelationTo(victimUserID,mainType);
				}
			}
			else return super.hasActiveRelations(iwc,roleUser,victimUser,mainType,reverseType);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (EJBException e) {
			e.printStackTrace();
		}
		return super.hasActiveRelations(iwc,roleUser,victimUser,mainType,reverseType);
			
		
			
		
	}

}
