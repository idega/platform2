/*
 * $Id: CommuneBelongingTypeBMPBean.java,v 1.7 2003/09/02 23:42:55 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 * 
 */
package se.idega.idegaweb.commune.accounting.regulations.data;
    
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.data.IDOLookup;

/**
 * Holds Commune belonging types ("Nacka", "Ej Nacka", "blabla") etc. Used for the posting.
 * 
 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean
 * <p>
 * $Id: CommuneBelongingTypeBMPBean.java,v 1.7 2003/09/02 23:42:55 kjell Exp $
 * 
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.7 $
 */
public class CommuneBelongingTypeBMPBean extends GenericEntity implements CommuneBelongingType {
	
	private static final String ENTITY_NAME = "cacc_commune_belong_type";
	private static final String COLUMN_COMMUNE_BELONGING_TYPE = "commune_belong_type";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void insertStartData () throws Exception {
		super.insertStartData ();
        
		System.out.println ("¤¤¤ Invoked " + ENTITY_NAME + ".insertStartData ()");

		CommuneBelongingTypeHome home
				= (CommuneBelongingTypeHome) IDOLookup.getHome(CommuneBelongingType.class);
		final String [] data = { "nacka", "ej_nacka" };
		for (int i = 0; i < data.length; i++) {
			CommuneBelongingType cbType = home.create();
			cbType.setCommuneBelongingType(ENTITY_NAME + "." + data[i]);
			cbType.store();
		}
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

	public void setLocalizationKey(String type) { 
		setColumn(COLUMN_COMMUNE_BELONGING_TYPE, type); 
	}
	
	public String getLocalizationKey() {
		return (String) getStringColumnValue(COLUMN_COMMUNE_BELONGING_TYPE);
	}

	public Collection ejbFindAllCommuneBelongingTypes() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		return idoFindPKsBySQL(sql.toString());
	}

	public Object ejbFindCommuneBelongingType(int id) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(getIDColumnName(), id);
		return idoFindOnePKByQuery(sql);
	}

}
