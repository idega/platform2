/*
 * $Id: ProviderTypeBMPBean.java,v 1.10 2003/11/24 17:53:00 joakim Exp $
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
 * Entity bean for the provider type (childcare, school, e t c).
 * <p>
 * Last modified: $Date: 2003/11/24 17:53:00 $ by $Author: joakim $
 *
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $Revision: 1.10 $
 */
public class ProviderTypeBMPBean  extends GenericEntity implements ProviderType {

	private static final String ENTITY_NAME = "cacc_provider_type";

	private static final String COLUMN_PROVIDER_TYPE_ID = "provider_type_id";
	private static final String COLUMN_LOCALIZATION_KEY = "localization_key";

	private static final String KEY_PREFIX = ENTITY_NAME + ".";
	
	private static final String KEY_COMMUNE = KEY_PREFIX + "commune";
	private static final String KEY_PRIVATE = KEY_PREFIX + "private";

	/**
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	/**
	 * @see com.idega.data.GenericEntity#getIdColumnName()
	 */
	public String getIDColumnName() {
		return COLUMN_PROVIDER_TYPE_ID;
	}

	/**
	 * @see com.idega.data.GenericEntity#insertStartData()
	 */
	public void insertStartData () throws Exception {
		super.insertStartData ();
        
		System.out.println ("¤¤¤ Invoked " + ENTITY_NAME + ".insertStartData ()");

		ProviderTypeHome home = (ProviderTypeHome) IDOLookup.getHome(ProviderType.class);
		final String [] data = {
				KEY_COMMUNE, 
				KEY_PRIVATE
		};
		
		for (int i = 0; i < data.length; i++) {
			ProviderType pt = home.create();
			pt.setLocalizationKey(data[i]);
			pt.store();
		}
	}

	/**
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_LOCALIZATION_KEY, "Localization key for this type", true, true, String.class);
		setAsPrimaryKey(getIDColumnName(), true);
	}
	
	public String getLocalizationKey() {
		return getStringColumnValue(COLUMN_LOCALIZATION_KEY);
	}

	public void setLocalizationKey(String textKey) { 
		setColumn(COLUMN_LOCALIZATION_KEY, textKey); 
	}

	/**
	 * Finds all provider types.
	 * @return collection of all provider types found
	 * @throws FinderException
	 */
	public Collection ejbFindAll() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		return idoFindPKsBySQL(sql.toString());
	}

	public Integer ejbFindPrivateType() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_LOCALIZATION_KEY, KEY_PRIVATE);
		return (Integer) idoFindOnePKByQuery(query);
	}
	
	public Integer ejbFindCommuneType() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_LOCALIZATION_KEY, KEY_COMMUNE);
		return (Integer) idoFindOnePKByQuery(query);
	}
	
}
