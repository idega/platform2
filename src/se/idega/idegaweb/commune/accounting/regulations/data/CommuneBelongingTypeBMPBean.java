/*
 * $Id: CommuneBelongingTypeBMPBean.java,v 1.2 2003/08/18 13:36:49 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 * 
 */
package se.idega.idegaweb.commune.accounting.regulations.data;
    
import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDOQuery;

/**
 * Holds Commune belonging types ("Nacka", "Ej Nacka", "blabla") etc. Used for the posting.
 * 
 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean
 * <p>
 * $Id: CommuneBelongingTypeBMPBean.java,v 1.2 2003/08/18 13:36:49 kjell Exp $
 * 
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.2 $
 */
public class CommuneBelongingTypeBMPBean extends GenericEntity implements CommuneBelongingType {
	
	private static final String ENTITY_NAME = "cacc_commune_belonging_type";
	private static final String COLUMN_COMMUNE_BELONGING_TYPE = "commune_belonging_type";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_COMMUNE_BELONGING_TYPE, "Commune Belonging type", true, true, String.class);
		setAsPrimaryKey (getIDColumnName(), true);
	}

	public void setCommuneBelongingType(String type) { 
		setColumn(COLUMN_COMMUNE_BELONGING_TYPE, type); 
	}
	
	public String getCommuneBelongingType() {
		return (String) getStringColumnValue(COLUMN_COMMUNE_BELONGING_TYPE);
	}

	public Collection ejbFindAllCommuneBelongingTypes() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.append(getEntityName());
		return idoFindPKsBySQL(sql.toString());
	}

	public Object ejbFindCommuneBelongingType(int id) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(getIDColumnName(), id);
		return idoFindOnePKByQuery(sql);
	}

}
