/*
 * Created on 18.4.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.venue.data;

import com.idega.data.IDOEntity;


import com.idega.user.data.Group;


import com.idega.core.location.data.Address;



/** * @author laddiTODO To change the template for this generated type comment go toWindow - Preferences - Java - Code Generation - Code and Comments */
public interface Venue extends IDOEntity {

	String getName();

	void setName(String name);

	Group getOwner();

	void setOwner(Group owner);

	String getDescription();

	void setDescription(String description);

	/**
	 *  
	 * @uml.property name="venueType"
	 * @uml.associationEnd multiplicity="(0 1)" inverse="venue:com.idega.block.venue.data.VenueType"
	 */
	VenueType getVenueType();

	/**
	 *  
	 * @uml.property name="venueType"
	 */
	void setVenueType(VenueType venueType);

	Address getAddress();

	void setAddress(Address address);

	boolean isDeleted();

	void setDeleted(boolean deleted);

}
