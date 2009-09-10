package com.idega.block.venue.data;

import com.idega.core.location.data.Address;
import com.idega.user.data.Group;
import com.idega.user.data.User;


public interface Venue extends com.idega.data.IDOEntity
{
	public static final String ENTITY_NAME = "ve_venue";

	public static final String COLUMN_VENUE_ID = "venue_id";
	public static final String COLUMN_NAME = "venue_name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_TYPE = "venue_type";
	public static final String COLUMN_DELETED = "deleted";
	public static final String COLUMN_DELETED_BY = "deleted_by";
	public static final String COLUMN_OWNER = "owner_id";
	public static final String COLUMN_ADDRESS = "address";

	public String getName();
	public Group getOwner();
	public String getDescription();
	/**
	 * @uml.property name="venueType"
	 * @uml.associationEnd multiplicity="(0 1)" inverse="venue:com.idega.block.venue.data.VenueType"
	 */
	public VenueType getVenueType();
	public Address getAddress();
	public User getDeletedBy();
	public boolean isDeleted();

	public void setName(String name);
	public void setOwner(Group owner);
	public void setDescription(String description);
	/**
	 * @uml.property name="venueType"
	 */
	public void setVenueType(VenueType venueType);
	public void setAddress(Address address);
	public void setDeletedBy(User user);
	public void setDeleted(boolean deleted);
}
