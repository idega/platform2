/*
 * $Id: PostingParametersBMPBean.java,v 1.2 2003/08/18 12:51:41 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 * 
 */
 
package se.idega.idegaweb.commune.accounting.posting.data;
    
import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDOQuery;

import se.idega.idegaweb.commune.accounting.regulations.data.ActivityType;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.CompanyType;
import se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingType;


/**
 * Holds information about default posting info.
 * @see se.idega.idegaweb.commune.accounting.regulations.data.ActivityType;
 * @see se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
 * @see se.idega.idegaweb.commune.accounting.regulations.data.CompanyType;
 * @see se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingType;
 * @author Kjell Lindman 
 */

public class PostingParametersBMPBean extends GenericEntity implements PostingParameters, IDOLegacyEntity
{
	private static final String ENTITY_NAME = "cacc_posting_parameters";

	private static final String COLUMN_PERIODE_FROM = "periode_from";
	private static final String COLUMN_PERIODE_TO = "periode_to";
	private static final String COLUMN_ACTIVITY = "activity";
	private static final String COLUMN_REG_SPEC_TYPE = "reg_spec_type";
	private static final String COLUMN_COMPANY_TYPE = "company_type";
	private static final String COLUMN_COMMUNE_BELONGING = "commune_belonging";
	private static final String COLUMN_OWN_ENTRY = "own_entry";
	private static final String COLUMN_DOUBLE_ENTRY = "double_entry";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		
		addAttribute(COLUMN_PERIODE_FROM, "Period from", true, true, String.class);
		addAttribute(COLUMN_PERIODE_TO, "Period  tom", true, true, String.class);

		addAttribute(COLUMN_ACTIVITY, "Verksamhet", true, true, 
						Integer.class, "many-to-one", ActivityType.class);
						
		addAttribute(COLUMN_REG_SPEC_TYPE, "Regelspecificationstyp", true, true, 
						Integer.class, "many-to-one", RegulationSpecType.class);
						
		addAttribute(COLUMN_COMPANY_TYPE, "Bolagstyp", true, true, 
						Integer.class, "many-to-one", CompanyType.class);
		
		addAttribute(COLUMN_COMMUNE_BELONGING, "Kommuntillhörighet", true, true, 
						Integer.class, "many-to-one", CommuneBelongingType.class);

		addAttribute(COLUMN_OWN_ENTRY, "Egen kontering", true, true, String.class);
		addAttribute(COLUMN_DOUBLE_ENTRY, "Mot kontering", true, true, String.class);

	}

	public void setPeriodeFrom(String periode) { 
		setColumn(COLUMN_PERIODE_FROM, periode); 
	}
	
	public void setPeriodeTo(String periode) { 
		setColumn(COLUMN_PERIODE_FROM, periode); 
	}
	
	public void setActivity(int data) { 
		setColumn(COLUMN_ACTIVITY, data); 
	}
	
	public void setRegSpecType(int data) { 
		setColumn(COLUMN_REG_SPEC_TYPE, data); 
	}
	
	public void setCompanyType(int data) { 
		setColumn(COLUMN_COMPANY_TYPE, data); 
	}
	
	public void setCommuneBelonging(int data) { 
		setColumn(COLUMN_COMMUNE_BELONGING, data); 
	}
	
	public void setOwnEntry(String data) { 
		setColumn(COLUMN_OWN_ENTRY, data); 
	}
	public void setDoubleEntry(String data) { 
		setColumn(COLUMN_DOUBLE_ENTRY, data); 
	}

	public String getPeriodeFrom() {
		return (String) getStringColumnValue(COLUMN_PERIODE_FROM);
	}

	public String getPeriodeTo() {
		return (String) getStringColumnValue(COLUMN_PERIODE_TO);
	}

	public ActivityType getActivity() {
		return (ActivityType) getColumnValue(COLUMN_ACTIVITY);
	}

	public RegulationSpecType getRegSpecType() {
		return (RegulationSpecType) getColumnValue(COLUMN_REG_SPEC_TYPE);
	}
	
	public CompanyType getCompanyType() {
		return (CompanyType) getColumnValue(COLUMN_COMPANY_TYPE);
	}
	
	public CommuneBelongingType getCommuneBelonging() {
			return (CommuneBelongingType) getColumnValue(COLUMN_COMMUNE_BELONGING);
	}

	public String getOwnEntry() {
		return (String) getStringColumnValue(COLUMN_OWN_ENTRY);
	}

	public String getDoubleEntry() {
		return (String) getStringColumnValue(COLUMN_DOUBLE_ENTRY);
	}
	
	public Collection ejbFindPostingParametersByPeriode(String from, String to) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere(COLUMN_PERIODE_FROM);
		sql.appendLessThanOrEqualsSign().append("'"+from+"'");
		sql.appendAnd().append(COLUMN_PERIODE_TO);
		sql.appendGreaterThanOrEqualsSign().append("'"+to+"'");
		return idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindAllPostingParameters() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.append(getEntityName());
		return idoFindPKsBySQL(sql.toString());
	}

	public Object ejbFindPostingParameter(int act, int reg, int comt, int comb ) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_ACTIVITY, act);
		sql.appendEquals(COLUMN_REG_SPEC_TYPE, reg);
		sql.appendEquals(COLUMN_COMPANY_TYPE, comt);
		sql.appendEquals(COLUMN_COMMUNE_BELONGING, comb);
			
		return (Integer) idoFindOnePKByQuery(sql);
	}

}
