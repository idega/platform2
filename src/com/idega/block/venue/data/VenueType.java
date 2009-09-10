package com.idega.block.venue.data;

import com.idega.user.data.User;


public interface VenueType extends com.idega.data.IDOEntity {
	
	public static final String ENTITY_NAME = "ve_venue_type";

	public static final String COLUMN_VENUE_TYPE = "venue_type";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_DELETED = "deleted";
	public static final String COLUMN_DELETED_BY = "deleted_by";
	public static final String COLUMN_LOCALIZED_KEY = "localized_key";

	public String getName();
	public String getDescription();
	public boolean isDeleted();
	public User getDeletedBy(); 
	public String getLocalizedKey();

	public void setName(String name);
	public void setDescription(String description);
	public void setDeleted(boolean deleted);
	public void setDeletedBy(User user);
	public void setLocalizedKey(String localizedKey);
}