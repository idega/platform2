/*
 * Created on 31.3.2004
 */
package se.idega.idegaweb.commune.accounting.update.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOAddRelationshipException;


/**
 * @author laddi
 */
public class SchoolSubGroupPlacementsBusinessBean extends IBOServiceBean implements SchoolSubGroupPlacementsBusiness {

	private SchoolBusiness getSchoolBusiness() {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	public void changeSubGroupPlacements() {
		try {
			Collection placements = getSchoolBusiness().getSchoolClassMemberHome().findSubGroupPlacements();
			Iterator iter = placements.iterator();
			while (iter.hasNext()) {
				SchoolClassMember placement = (SchoolClassMember) iter.next();
				SchoolClass group = placement.getSchoolClass();
				
				try {
					SchoolClassMember mainPlacement = getSchoolBusiness().getSchoolClassMemberHome().findByUserAndSchoolAndSeason(placement.getClassMemberId(), group.getSchoolId(), group.getSchoolSeasonId());
					mainPlacement.addToGroup(group);
					placement.remove();
				}
				catch (FinderException fe) {
					log("No main group placement found for: " + placement.getClassMemberId());
				}
				catch (IDOAddRelationshipException e) {
					log("Could not add " + placement.getClassMemberId() + " to sub group " + group.getPrimaryKey());
				}
				catch (RemoveException e) {
					log("Could not remove " + placement.getClassMemberId() + " from group " + group.getPrimaryKey());
				}
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
		catch (FinderException fe) {
			log("No sub group placements found.");
		}
	}

}
