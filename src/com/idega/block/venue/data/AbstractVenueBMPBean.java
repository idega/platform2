/*
 * Created on 14.7.2004
 */
package com.idega.block.venue.data;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.core.location.data.Address;
import com.idega.data.GenericEntity;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORuntimeException;
import com.idega.data.IDOStoreException;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * @author laddi
 */
public abstract class AbstractVenueBMPBean extends GenericEntity implements Venue {

	private Venue iVenue;
	
	protected abstract String getTypeName();
	protected abstract String getTypeDescription();
	
	protected VenueHome getVenueHome() {
		try {
			return (VenueHome) com.idega.data.IDOLookup.getHome(Venue.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e.getMessage());
		}
	}

	protected VenueTypeHome getVenueTypeHome() {
		try {
			return (VenueTypeHome) com.idega.data.IDOLookup.getHome(VenueType.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e.getMessage());
		}
	}

	protected boolean doInsertInCreate() {
		return true;
	}

	private Venue getGeneralVenue() {
		if (iVenue == null) {
			try {
				iVenue = getVenueHome().findByPrimaryKey(this.getPrimaryKey());
			}
			catch (FinderException fe) {
				fe.printStackTrace();
				throw new EJBException(fe.getMessage());
			}
		}
		return iVenue;
	}
	
	public void addGeneralVenueRelation() {
		this.addManyToOneRelationship(getIDColumnName(), "Venue ID", Venue.class);
		this.getAttribute(getIDColumnName()).setAsPrimaryKey(true);
	}
	
	public Object ejbCreate() throws CreateException {
		iVenue = this.getVenueHome().create();
		this.setPrimaryKey(iVenue.getPrimaryKey());
		try {
			VenueType type = getVenueTypeHome().findByPrimaryKey(getTypeName());
			this.setVenueType(type);
		}
		catch (FinderException fe) {
			log(fe);
		}

		return super.ejbCreate();
	}

	public void insertStartData() {
		try {
			VenueType type = getVenueTypeHome().create();
			type.setName(getTypeName());
			type.setDescription(getTypeDescription());
			type.setLocalizedKey("venue_type." + getTypeName().toLowerCase());
			type.store();
		}
		catch (CreateException ce) {
			log(ce);
		}
	}

	public void remove() throws RemoveException {
		super.remove();
		getGeneralVenue().remove();
	}

	public void store() throws IDOStoreException {
		getGeneralVenue().store();
		super.store();
	}

	public String getName() {
		return getGeneralVenue().getName();
	}
	
	public Group getOwner() {
		return getGeneralVenue().getOwner();
	}

	public String getDescription() {
		return getGeneralVenue().getDescription();
	}

	public VenueType getVenueType() {
		return getGeneralVenue().getVenueType();
	}

	public Address getAddress() {
		return getGeneralVenue().getAddress();
	}

	public boolean isDeleted() {
		return getGeneralVenue().isDeleted();
	}

	public User getDeletedBy() {
		return getGeneralVenue().getDeletedBy();
	}
	
	public void setName(String name) {
		getGeneralVenue().setName(name);
	}

	public void setOwner(Group owner) {
		getGeneralVenue().setOwner(owner);
	}

	public void setDescription(String description) {
		getGeneralVenue().setDescription(description);
	}

	public void setVenueType(VenueType venueType) {
		getGeneralVenue().setVenueType(venueType);
	}

	public void setAddress(Address address) {
		getGeneralVenue().setAddress(address);
	}

	public void setDeleted(boolean deleted) {
		getGeneralVenue().setDeleted(deleted);
	}

	public void setDeletedBy(User user) {
		getGeneralVenue().setDeletedBy(user);
	}
}