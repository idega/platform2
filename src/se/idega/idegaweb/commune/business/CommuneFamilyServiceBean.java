/*
 * Created on Feb 15, 2004
 *
 */
package se.idega.idegaweb.commune.business;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

import com.idega.user.data.User;

import is.idega.block.family.business.FamilyLogicBean;

/**
 * CommuneFamilyServiceBean
 * @author aron 
 * @version 1.0
 */
public class CommuneFamilyServiceBean extends FamilyLogicBean implements CommuneFamilyService {
	
	
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogic#setAsCohabitantFor(com.idega.user.data.User, com.idega.user.data.User)
	 */
	public void setAsCohabitantFor(User personToSet, User relatedPerson) throws CreateException, RemoteException {
		// We don't allow both cohabitant and spouse relation between same people
		if(!this.isSpouseOf(personToSet,relatedPerson)){
			super.setAsCohabitantFor(personToSet, relatedPerson);
		}
		else
			throw new CreateException("Multiple relations: Cohabitant relation failed, spouse relation already exists");
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogic#setAsSpouseFor(com.idega.user.data.User, com.idega.user.data.User)
	 */
	public void setAsSpouseFor(User personToSet, User relatedPerson) throws CreateException, RemoteException {
		if(!this.isCohabitantOf(personToSet,relatedPerson)){
			super.setAsSpouseFor(personToSet, relatedPerson);
		}
		else
			throw new CreateException("Multiple relations: Spouse relation failed, cohabitant relation already exists");
	}

}
