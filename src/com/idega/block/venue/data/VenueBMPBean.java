/*
 * Created on 14.7.2004
 */
package com.idega.block.venue.data;

import com.idega.core.location.data.Address;
import com.idega.data.GenericEntity;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * @author laddi
 */
public class VenueBMPBean extends GenericEntity implements Venue {

	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	protected boolean doInsertInCreate() {
		return true;
	}
	
	public void initializeAttributes() {
		addAttribute(COLUMN_VENUE_ID);
		setAsPrimaryKey(COLUMN_VENUE_ID, true);
		
		addAttribute(COLUMN_NAME, "Name", String.class, 255);
		addAttribute(COLUMN_DESCRIPTION, "Description", String.class, 1000);
		addAttribute(COLUMN_DELETED, "Is deleted", Boolean.class);
		addManyToOneRelationship(COLUMN_OWNER, Group.class);
		addManyToOneRelationship(COLUMN_ADDRESS, Address.class);
		addManyToOneRelationship(COLUMN_TYPE, VenueType.class);
	}

	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}
	
	public Group getOwner() {
		return (Group) this.getColumnValue(COLUMN_OWNER);
	}

	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);
	}

	public VenueType getVenueType() {
		return (VenueType) getColumnValue(COLUMN_TYPE);
	}

	public Address getAddress() {
		return (Address) getColumnValue(COLUMN_ADDRESS);
	}
	
	public User getDeletedBy() {
		return (User) getColumnValue(COLUMN_DELETED_BY);
	}

	public boolean isDeleted() {
		return getBooleanColumnValue(COLUMN_DELETED, false);
	}

	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}
	
	public void setOwner(Group owner) {
		setColumn(COLUMN_OWNER, owner);
	}

	public void setDescription(String description) {
		setColumn(COLUMN_DESCRIPTION, description);
	}

	public void setVenueType(VenueType venueType) {
		setColumn(COLUMN_TYPE, venueType);
	}

	public void setAddress(Address address) {
		setColumn(COLUMN_ADDRESS, address);
	}
	
	public void setDeletedBy(User user) {
		setColumn(COLUMN_DELETED_BY, user);
	}

	public void setDeleted(boolean deleted) {
		setColumn(COLUMN_DELETED, deleted);
	}
}