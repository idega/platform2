/*
 * Created on Feb 15, 2004
 *
 */
package se.idega.idegaweb.commune.user.presentation;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.business.CommuneFamilyService;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;

import is.idega.block.family.business.FamilyLogic;
import is.idega.idegaweb.member.presentation.FamilyRelationConnector;

/**
 * CommuneFamilyRelationConnector
 * @author aron 
 * @version 1.0
 */
public class CommuneFamilyRelationConnector extends FamilyRelationConnector {
	
	
	/**
	 * 
	 */
	public CommuneFamilyRelationConnector() {
		super();
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.presentation.FamilyRelationConnector#getMemberFamilyLogic(com.idega.idegaweb.IWApplicationContext)
	 */
	protected FamilyLogic getMemberFamilyLogic(IWApplicationContext iwac) throws RemoteException {
		return (FamilyLogic)IBOLookup.getServiceInstance(iwac,CommuneFamilyService.class);
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.presentation.UserRelationConnector#isRelationshipLegal(com.idega.presentation.IWContext, com.idega.user.data.User, com.idega.user.data.User, java.lang.String)
	 */
	protected boolean isRelationshipLegal(IWContext iwc, User roleUser, User victimUser, String relationType) {
		try {
			FamilyLogic familyService = getMemberFamilyLogic(iwc);
			if(relationType.equalsIgnoreCase(familyService.getCohabitantRelationType())){
				return !familyService.isSpouseOf(roleUser,victimUser);
			}
			else if(relationType.equalsIgnoreCase(familyService.getSpouseRelationType())){
				return !familyService.isCohabitantOf(roleUser,victimUser);
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		return true;
	}

}
