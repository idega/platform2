package is.idega.idegaweb.travel.service.carrental.data;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.FinderException;

import is.idega.idegaweb.travel.data.PickupPlaceHome;
import is.idega.idegaweb.travel.data.Service;
import is.idega.idegaweb.travel.data.PickupPlace;

import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;

/**
 * @author gimmi
 */
public class CarRentalBMPBean extends GenericEntity implements CarRental{

	private Collection allPlaces;
	private Collection dropoffPlaces;
	private Collection pickupPlaces;

	public String getEntityName() {
		return "TB_CAR"; 
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName(),"Service_id",true,true,Integer.class,"one-to-one",Service.class);
		addManyToManyRelationShip(PickupPlace.class);
	}

	private void resetPlaces() {
		allPlaces = new Vector();
		pickupPlaces = new Vector();
		dropoffPlaces = new Vector();
	}
		
	private void setAllPlaces() throws IDORelationshipException, IDOLookupException, FinderException {
		System.out.println("[CarRentalBMPBean]setAllPlaces()");
		resetPlaces();
		allPlaces = this.idoGetRelatedEntities(PickupPlace.class);
		
		if (allPlaces != null && !allPlaces.isEmpty()) {
			PickupPlaceHome pHome = (PickupPlaceHome) IDOLookup.getHome(PickupPlace.class);
			PickupPlace pPlace;
			Object obj;
			Iterator iter = allPlaces.iterator();
			while (iter.hasNext()) {
				obj = iter.next();
				pPlace = pHome.findByPrimaryKey(obj);
				if (pPlace.getIsPickup()) {
					pickupPlaces.add(obj);
				}else {
					dropoffPlaces.add(obj);
				}
			}	
		}
	}
	
	public Collection getPickupPlaces() throws IDOLookupException, IDORelationshipException, FinderException {
		if (pickupPlaces == null) {	
			setAllPlaces();
		}		
		return pickupPlaces;
	}	
	
	public Collection getDropoffPlaces() throws IDOLookupException, IDORelationshipException, FinderException {
		if (dropoffPlaces == null) {
			setAllPlaces();	
		}	
		return dropoffPlaces;
	}
	
	public void addPickupPlace(PickupPlace pPlace) throws IDOAddRelationshipException {
		this.idoAddTo(pPlace);
		resetPlaces();
	}
	
	public void addDropoffPlace(PickupPlace pPlace) throws IDOAddRelationshipException {
		this.idoAddTo(pPlace);
		resetPlaces();	
	}
	
	public void removeAllPickupPlaces() throws IDORemoveRelationshipException {
		this.idoRemoveFrom(PickupPlace.class);	
	}
	
	public void removeAllDropoffPlaces() throws IDORemoveRelationshipException  {
		this.idoRemoveFrom(PickupPlace.class);	
	}
	
	public void setPrimaryKey(Object obj) {
		super.setPrimaryKey(obj);	
	}
}
