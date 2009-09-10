/*
 * Created on 14.7.2004
 */
package com.idega.block.venue.data;

import com.idega.data.GenericEntity;
import com.idega.user.data.User;


/**
 * @author laddi
 */
public class VenueTypeBMPBean extends GenericEntity implements VenueType {

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public Class getPrimaryKeyClass() {
		return String.class;
	}
	
	public String getIDColumnName() {
		return COLUMN_VENUE_TYPE;
	}
	
	public void setDefaultValues() {
		setDeleted(false);
	}
	
	public void initializeAttributes() {
		addAttribute(COLUMN_VENUE_TYPE, "Venue type", String.class, 32);
		setAsPrimaryKey(COLUMN_VENUE_TYPE, true);
		
		addAttribute(COLUMN_DESCRIPTION, "Description", String.class, 1000);
		addAttribute(COLUMN_DELETED, "Is deleted", Boolean.class);
		addAttribute(COLUMN_LOCALIZED_KEY, "Localized key", String.class, 255);
		addManyToOneRelationship(COLUMN_DELETED_BY, User.class);
	}
	
	public String getName() {
		return getStringColumnValue(COLUMN_VENUE_TYPE);
	}

	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);
	}

	public boolean isDeleted() {
		return getBooleanColumnValue(COLUMN_DELETED, false);
	}

	public User getDeletedBy() {
		return (User) getColumnValue(COLUMN_DELETED_BY);
	}
	
	public String getLocalizedKey() {
		return getStringColumnValue(COLUMN_LOCALIZED_KEY);
	}
	
	public void setName(String name) {
		setColumn(COLUMN_VENUE_TYPE, name);
	}

	public void setDescription(String description) {
		setColumn(COLUMN_DESCRIPTION, description);
	}

	public void setDeleted(boolean deleted) {
		setColumn(COLUMN_DELETED, deleted);
	}

	public void setDeletedBy(User user) {
		setColumn(COLUMN_DELETED_BY, user);
	}
	
	public void setLocalizedKey(String localizedKey) {
		setColumn(COLUMN_LOCALIZED_KEY, localizedKey);
	}
}