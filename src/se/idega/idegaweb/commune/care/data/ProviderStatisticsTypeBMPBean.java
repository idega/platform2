/*
 * $Id: ProviderStatisticsTypeBMPBean.java,v 1.1 2004/10/15 10:41:51 thomas Exp $
 *
 * Copyright (C) 2003 Idega. All Rights Reserved.
 *
 * This software is the proprietary information of Idega.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.care.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOQuery;

/**
 * Entity bean for provider statistic types.
 * <p>
 * Last modified: $Date: 2004/10/15 10:41:51 $ by $Author: thomas $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */ 
public class ProviderStatisticsTypeBMPBean extends GenericEntity implements ProviderStatisticsType {

	private static final String ENTITY_NAME = "cacc_provider_stat_type";

	private static final String COLUMN_LOCALIZATION_KEY = "localization_key";

	private static final String KP = "provider."; // Localization key prefix
	
	private static final String STATISTICS_COMMUNE_PROVIDER_WITHIN_COMMUNE = KP + "statistics_commune_provider_within_commune";
	private static final String STATISTICS_PRIVATE_PROVIDER = KP + "statistics_private_provider";
	private static final String STATISTICS_COMMUNE_PROVIDER_OTHER_COMMUNE = KP + "statistics_commune_provider_other_commune";
	private static final String STATISTICS_SPECIAL_SCHOOL = KP + "statistics_special_school";
	private static final String STATISTICS_COUNTY_COUNCIL_SCHOOL = KP + "statistics_county_council_school";
	private static final String STATISTICS_FOREIGN_PROVIDER = KP + "statistics_foreign_provider";

	/**
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/**
	 * @see com.idega.data.GenericEntity#getPrimaryKeyClass()
	 */
	public Class getPrimaryKeyClass() {
		return String.class;
	}
	
	/**
	 * @see com.idega.data.GenericEntity#getIdColumnName()
	 */
	public String getIDColumnName() {
		return COLUMN_LOCALIZATION_KEY;
	}
	
	/**
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(COLUMN_LOCALIZATION_KEY, "Provider statistics type localization key", String.class, 100);
		setAsPrimaryKey(COLUMN_LOCALIZATION_KEY, true);
	}

	/**
	 * @see com.idega.data.GenericEntity#insertStartData()
	 */
	public void insertStartData () throws Exception {
		super.insertStartData ();
        
		System.out.println ("¤¤¤ Invoked " + ENTITY_NAME + ".insertStartData ()");

		ProviderStatisticsTypeHome home = (ProviderStatisticsTypeHome) IDOLookup.getHome(ProviderStatisticsType.class);
		final String [] data = {
				STATISTICS_COMMUNE_PROVIDER_WITHIN_COMMUNE, 
				STATISTICS_PRIVATE_PROVIDER, 
				STATISTICS_COMMUNE_PROVIDER_OTHER_COMMUNE, 
				STATISTICS_SPECIAL_SCHOOL, 
				STATISTICS_COUNTY_COUNCIL_SCHOOL, 
				STATISTICS_FOREIGN_PROVIDER 
		};
		
		for (int i = 0; i < data.length; i++) {
			ProviderStatisticsType pst = home.create();
			pst.setLocalizationKey(data[i]);
			pst.store();
		}
	}

	public String getLocalizationKey() {
		return getStringColumnValue(COLUMN_LOCALIZATION_KEY);	
	}

	public void setLocalizationKey(String key) { 
		setColumn(COLUMN_LOCALIZATION_KEY, key); 
	}

	/**
	 * Finds all provider statistics types.
	 * @return collection of all provider statistics type objects
	 * @throws FinderException
	 */
	public Collection ejbFindAll() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		return idoFindPKsByQuery(query);
	}
}
