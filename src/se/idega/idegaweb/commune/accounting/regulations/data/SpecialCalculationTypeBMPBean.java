/*
 * $Id: SpecialCalculationTypeBMPBean.java,v 1.5 2003/10/15 12:11:55 kjell Exp $
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
 * Holds special calculation types ("Subventionerad", "Syskon", "Maxtaxa", "L�ginkomst". "Allm�n f�rskola") 
 * 
 * <p>
 * $Id: SpecialCalculationTypeBMPBean.java,v 1.5 2003/10/15 12:11:55 kjell Exp $
 * 
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.5 $
 */
public class SpecialCalculationTypeBMPBean extends GenericEntity implements SpecialCalculationType {
	
	private static final String ENTITY_NAME = "cacc_sp_calc_type";
	private static final String COLUMN_SPECIAL_CALCULATION_TYPE = "special_calculation_type";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void insertStartData () throws Exception {
		super.insertStartData ();
        
		System.out.println ("��� Invoked " + ENTITY_NAME + ".insertStartData ()");

		SpecialCalculationTypeHome home
				= (SpecialCalculationTypeHome) IDOLookup.getHome(SpecialCalculationType.class);
		final String [] data = { "blank", "subv", "syskon", "maxtaxa", "laginkomst", "allm_forskola" };
		for (int i = 0; i < data.length; i++) {
			SpecialCalculationType sc = home.create();
			sc.setSpecialCalculationType(ENTITY_NAME + "." + data[i]);
			sc.store();
		}
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_SPECIAL_CALCULATION_TYPE, "Special calculation type", true, true, String.class);
		setAsPrimaryKey (getIDColumnName(), true);
	}

	public void setSpecialCalculationType(String type) { 
		setColumn(COLUMN_SPECIAL_CALCULATION_TYPE, type); 
	}
	
	public String getSpecialCalculationType() {
		return getStringColumnValue(COLUMN_SPECIAL_CALCULATION_TYPE);
	}

	public void setLocalizationKey(String type) { 
		setColumn(COLUMN_SPECIAL_CALCULATION_TYPE, type); 
	}
	
	public String getLocalizationKey() {
		return getStringColumnValue(COLUMN_SPECIAL_CALCULATION_TYPE);
	}

	public Collection ejbFindAllSpecialCalculationTypes() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendOrderBy(COLUMN_SPECIAL_CALCULATION_TYPE);
		return idoFindPKsBySQL(sql.toString());
	}

	public Object ejbFindSpecialCalculationType(int id) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(getIDColumnName(), id);
		return idoFindOnePKByQuery(sql);
	}

}
