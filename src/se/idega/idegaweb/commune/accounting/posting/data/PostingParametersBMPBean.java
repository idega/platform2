/*
 * $Id: PostingParametersBMPBean.java,v 1.5 2003/08/20 13:15:50 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 * 
 */
package se.idega.idegaweb.commune.accounting.posting.data;
        
import java.util.Collection;
import java.sql.Date;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

import se.idega.idegaweb.commune.accounting.regulations.data.ActivityType;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.CompanyType;
import se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingType;

/**
 * PostingParameters Holds information about default posting info.
 * It is used to match a posting and get its posting accounts etc
 * 
 * Other submodules will use this data to search for a match on 
 * Periode, Activity, Regulation sec, Company type and Commune belonging.
 * When you have a hit you can retrive accounting data such as accounts, resources, activity codes 
 * etc. These values are always mirrored in "Own entries" and "Double entries". See Book-Keeping
 * terms.
 *  
 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingString;
 * @see se.idega.idegaweb.commune.accounting.regulations.data.ActivityType;
 * @see se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
 * @see se.idega.idegaweb.commune.accounting.regulations.data.CompanyType;
 * @see se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingType;
 * <p>
 * $Id: PostingParametersBMPBean.java,v 1.5 2003/08/20 13:15:50 kjell Exp $
 * 
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.5 $
 */
public class PostingParametersBMPBean extends GenericEntity implements PostingParameters {
	
	private static final String ENTITY_NAME = "cacc_posting_parameters";

	private static final String COLUMN_PERIODE_FROM = "periode_from";
	private static final String COLUMN_PERIODE_TO = "periode_to";
	private static final String COLUMN_CHANGED_DATE = "changed_date";
	private static final String COLUMN_CHANGED_SIGN = "changed_sign";

	private static final String COLUMN_ACTIVITY_ID = "activity_id";
	private static final String COLUMN_REG_SPEC_TYPE_ID = "reg_spec_type_id";
	private static final String COLUMN_COMPANY_TYPE_ID = "company_type_id";
	private static final String COLUMN_COMMUNE_BELONGING_ID = "commune_belonging_id";
	
	private static final String COLUMN_OWN_ACCOUNT = "own_account";
	private static final String COLUMN_OWN_LIABILITY = "own_liability";
	private static final String COLUMN_OWN_RESOURCE = "own_resource";
	private static final String COLUMN_OWN_ACTIVITY_CODE = "own_activity_code";
	private static final String COLUMN_OWN_DOUBLE_ENTRY = "own_double_entry";
	private static final String COLUMN_OWN_ACTIVITY = "own_activity";
	private static final String COLUMN_OWN_PROJECT = "own_project";
	private static final String COLUMN_OWN_OBJECT = "own_object";

	private static final String COLUMN_DOUBLE_ACCOUNT = "double_account";
	private static final String COLUMN_DOUBLE_LIABILITY = "double_liability";
	private static final String COLUMN_DOUBLE_RESOURCE = "double_resource";
	private static final String COLUMN_DOUBLE_ACTIVITY_CODE = "double_activity_code";
	private static final String COLUMN_DOUBLE_DOUBLE_ENTRY = "double_double_entry";
	private static final String COLUMN_DOUBLE_ACTIVITY = "double_activity";
	private static final String COLUMN_DOUBLE_PROJECT = "double_project";
	private static final String COLUMN_DOUBLE_OBJECT = "double_object";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		
		addAttribute(COLUMN_PERIODE_FROM, "Period from", true, true, Date.class);
		addAttribute(COLUMN_PERIODE_TO, "Period  tom", true, true, Date.class);
		addAttribute(COLUMN_CHANGED_DATE, "Ändrings datum", true, true, Date.class);
		addAttribute(COLUMN_CHANGED_SIGN, "Ändrings sign", true, true, String.class);
		
		addAttribute(COLUMN_ACTIVITY_ID, "Verksamhet", true, true, 
						Integer.class, "many-to-one", ActivityType.class);
						
		addAttribute(COLUMN_REG_SPEC_TYPE_ID, "Regelspecificationstyp", true, true, 
						Integer.class, "many-to-one", RegulationSpecType.class);
						
		addAttribute(COLUMN_COMPANY_TYPE_ID, "Bolagstyp", true, true, 
						Integer.class, "many-to-one", CompanyType.class);
		
		addAttribute(COLUMN_COMMUNE_BELONGING_ID, "Kommuntillhörighet", true, true, 
						Integer.class, "many-to-one", CommuneBelongingType.class);

		addAttribute(COLUMN_OWN_ACCOUNT, "Eget konto", true, true, String.class);
		addAttribute(COLUMN_OWN_LIABILITY, "Eget answer", true, true, String.class);
		addAttribute(COLUMN_OWN_RESOURCE, "Egen resurs", true, true, String.class);
		addAttribute(COLUMN_OWN_ACTIVITY_CODE, "Egen Verksamhetskod", true, true, String.class);
		addAttribute(COLUMN_OWN_DOUBLE_ENTRY, "Egen motpart", true, true, String.class);
		addAttribute(COLUMN_OWN_ACTIVITY, "Egen aktivitet", true, true, String.class);
		addAttribute(COLUMN_OWN_PROJECT, "Egen project", true, true, String.class);
		addAttribute(COLUMN_OWN_OBJECT, "Egen objekt", true, true, String.class);

		addAttribute(COLUMN_DOUBLE_ACCOUNT, "Mot konto", true, true, String.class);
		addAttribute(COLUMN_DOUBLE_LIABILITY, "Mot answer", true, true, String.class);
		addAttribute(COLUMN_DOUBLE_RESOURCE, "Mot resurs", true, true, String.class);
		addAttribute(COLUMN_DOUBLE_ACTIVITY_CODE, "Mot verksamhetskod", true, true, String.class);
		addAttribute(COLUMN_DOUBLE_DOUBLE_ENTRY, "Motpart", true, true, String.class);
		addAttribute(COLUMN_DOUBLE_ACTIVITY, "Mot aktivitet", true, true, String.class);
		addAttribute(COLUMN_DOUBLE_PROJECT, "Mot project", true, true, String.class);
		addAttribute(COLUMN_DOUBLE_OBJECT, "Mot objekt", true, true, String.class);

	}
	
	public String getPostingAccount() {return (String) getStringColumnValue(COLUMN_OWN_ACCOUNT);}
	public String getPostingLiability() {return (String) getStringColumnValue(COLUMN_OWN_LIABILITY);}
	public String getPostingResource() {return (String) getStringColumnValue(COLUMN_OWN_RESOURCE);}
	public String getPostingActivityCode() {return (String) getStringColumnValue(COLUMN_OWN_ACTIVITY_CODE);}
	public String getPostingDoubleEntry() {return (String) getStringColumnValue(COLUMN_OWN_DOUBLE_ENTRY);}
	public String getPostingActivity() {return (String) getStringColumnValue(COLUMN_OWN_ACTIVITY);}
	public String getPostingProject() {return (String) getStringColumnValue(COLUMN_OWN_PROJECT);}
	public String getPostingObject() {return (String) getStringColumnValue(COLUMN_OWN_OBJECT);}

	public String getDoublePostingAccount() {return (String) getStringColumnValue(COLUMN_DOUBLE_ACCOUNT);}
	public String getDoublePostingLiability() {return (String) getStringColumnValue(COLUMN_DOUBLE_LIABILITY);}
	public String getDoublePostingResource() {return (String) getStringColumnValue(COLUMN_DOUBLE_RESOURCE);}
	public String getDoublePostingActivityCode() {return (String) getStringColumnValue(COLUMN_DOUBLE_ACTIVITY_CODE);}
	public String getDoublePostingDoubleEntry() {return (String) getStringColumnValue(COLUMN_DOUBLE_DOUBLE_ENTRY);}
	public String getDoublePostingActivity() {return (String) getStringColumnValue(COLUMN_DOUBLE_ACTIVITY);}
	public String getDoublePostingProject() {return (String) getStringColumnValue(COLUMN_DOUBLE_PROJECT);}
	public String getDoublePostingObject() {return (String) getStringColumnValue(COLUMN_DOUBLE_OBJECT);}

	public void setPostingAccount(String data) {setColumn(COLUMN_OWN_ACCOUNT, data); }
	public void setPostingLiability(String data) {setColumn(COLUMN_OWN_LIABILITY, data); }
	public void setPostingResource(String data) {setColumn(COLUMN_OWN_RESOURCE, data); }
	public void setPostingActivityCode(String data) {setColumn(COLUMN_OWN_ACTIVITY_CODE, data); }
	public void setPostingDoubleEntry(String data) {setColumn(COLUMN_OWN_DOUBLE_ENTRY, data); }
	public void setPostingActivity(String data) {setColumn(COLUMN_OWN_ACTIVITY, data); }
	public void setPostingProject(String data) {setColumn(COLUMN_OWN_PROJECT, data); }
	public void setPostingObject(String data) {setColumn(COLUMN_OWN_OBJECT, data); }

	public void setDoublePostingAccount(String data) {setColumn(COLUMN_DOUBLE_ACCOUNT, data); }
	public void setDoublePostingLiability(String data) {setColumn(COLUMN_DOUBLE_LIABILITY, data); }
	public void setDoublePostingResource(String data) {setColumn(COLUMN_DOUBLE_RESOURCE, data); }
	public void setDoublePostingActivityCode(String data) {setColumn(COLUMN_DOUBLE_ACTIVITY_CODE, data); }
	public void setDoublePostingDoubleEntry(String data) {setColumn(COLUMN_DOUBLE_DOUBLE_ENTRY, data); }
	public void setDoublePostingActivity(String data) {setColumn(COLUMN_DOUBLE_ACTIVITY, data); }
	public void setDoublePostingProject(String data) {setColumn(COLUMN_DOUBLE_PROJECT, data); }
	public void setDoublePostingObject(String data) {setColumn(COLUMN_DOUBLE_OBJECT, data); }


	public Date getChangedDate() {
		return (Date)getColumnValue(COLUMN_CHANGED_DATE);
	}

	public void setUpdatedDate(Date changedDate) {
		setColumn(COLUMN_CHANGED_DATE, changedDate);
	}

	public String getChangedSign() {
		return (String) getColumnValue(COLUMN_CHANGED_SIGN);
	}

	public void setChangedSign(String sign) {
		setColumn(COLUMN_CHANGED_SIGN, sign);
	}

	public void setPeriodeFrom(Date periode) { 
		setColumn(COLUMN_PERIODE_FROM, periode); 
	}
	
	public void setPeriodeTo(Date periode) { 
		setColumn(COLUMN_PERIODE_TO, periode); 
	}
	
	public void setActivity(int id) { 
		setColumn(COLUMN_ACTIVITY_ID, id); 
	}
	
	public void setRegSpecType(int id) { 
		setColumn(COLUMN_REG_SPEC_TYPE_ID, id); 
	}
	
	public void setCompanyType(int id) { 
		setColumn(COLUMN_COMPANY_TYPE_ID, id); 
	}
	
	public void setCommuneBelonging(int id) { 
		setColumn(COLUMN_COMMUNE_BELONGING_ID, id); 
	}
	
	public Date getPeriodeFrom() {
		return (Date) getColumnValue(COLUMN_PERIODE_FROM);
	}

	public Date getPeriodeTo() {
		return (Date) getColumnValue(COLUMN_PERIODE_TO);
	}

	public ActivityType getActivity() {
		return (ActivityType) getColumnValue(COLUMN_ACTIVITY_ID);
	}

	public RegulationSpecType getRegSpecType() {
		return (RegulationSpecType) getColumnValue(COLUMN_REG_SPEC_TYPE_ID);
	}
	
	public CompanyType getCompanyType() {
		return (CompanyType) getColumnValue(COLUMN_COMPANY_TYPE_ID);
	}

	public CommuneBelongingType getCommuneBelonging() {
			return (CommuneBelongingType) getColumnValue(COLUMN_COMMUNE_BELONGING_ID);
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
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_ACTIVITY_ID, act);
		sql.appendEquals(COLUMN_REG_SPEC_TYPE_ID, reg);
		sql.appendEquals(COLUMN_COMPANY_TYPE_ID, comt);
		sql.appendEquals(COLUMN_COMMUNE_BELONGING_ID, comb);
		return idoFindOnePKByQuery(sql);
	}

	public Object ejbFindPostingParameter(int id) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(getIDColumnName(), id);
		return idoFindOnePKByQuery(sql);
	}


}
