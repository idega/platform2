/*
 * $Id: CareTimeBMPBean.java,v 1.4 2005/01/19 15:23:35 anders Exp $
 * Created on 11.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;


/**
 * Last modified: 11.11.2004 08:54:09 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.4 $
 */
public class CareTimeBMPBean extends GenericEntity implements CareTime {

	public static final String ENTITY_NAME = "comm_care_time";
	
	public static final String COLUMN_NAME_CODE = "code";
	public static final String COLUMN_NAME_LOCALIZED_KEY = "localized_key";
	public static final String COLUMN_NAME_HOURS = "hours";
	
	public static final String CODE_FSKHEL = "FSKHEL";
	public static final String CODE_FSKHEL4_5 = "FSKHEL4-5";
	public static final String CODE_FSKDEL = "FSKDEL";
	public static final String CODE_FSKDEL4_5 = "FSKDEL4-5A";
	
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

  public String getIDColumnName(){
    return COLUMN_NAME_CODE;
  }

  public Class getPrimaryKeyClass(){
    return String.class;
  }

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#insertStartData()
	 */
	public void insertStartData() throws Exception {
		CareTimeHome home = (CareTimeHome) IDOLookup.getHome(CareTime.class);
		
		String[] codes = {CODE_FSKHEL, CODE_FSKHEL4_5, CODE_FSKDEL, CODE_FSKDEL4_5};
		String[] localizedKeys = { "care_time.FSKHEL", "care_time.FSKHEL4-5", "care_time.FSKDEL", "care_time.FSKDEL4-5A"};
		
		for (int a = 0; a < codes.length; a++) {
			CareTime time = home.create();
			time.setCode(codes[a]);
			time.setLocalizedKey(localizedKeys[a]);
			time.store();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(COLUMN_NAME_CODE, "Code representing the care time", String.class, 30);
		setPrimaryKey(COLUMN_NAME_CODE);
		
		addAttribute(COLUMN_NAME_LOCALIZED_KEY, "The localized key", String.class);
		addAttribute(COLUMN_NAME_HOURS, "The hours of care", Integer.class);
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.care.data.CareTime#getCode()
	 */
	public String getCode() {
		return getStringColumnValue(COLUMN_NAME_CODE);
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.care.data.CareTime#getLocalizedKey()
	 */
	public String getLocalizedKey() {
		return getStringColumnValue(COLUMN_NAME_LOCALIZED_KEY);
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.care.data.CareTime#getHours()
	 */
	public int getHours() {
		return getIntColumnValue(COLUMN_NAME_HOURS);
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.care.data.CareTime#setCode(java.lang.String)
	 */
	public void setCode(String code) {
		setColumn(COLUMN_NAME_CODE, code);
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.care.data.CareTime#setLocalizedKey(java.lang.String)
	 */
	public void setLocalizedKey(String localizedKey) {
		setColumn(COLUMN_NAME_LOCALIZED_KEY, localizedKey);
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.care.data.CareTime#setHours(int)
	 */
	public void setHours(int hours) {
		setColumn(COLUMN_NAME_HOURS, hours);
	}
	
	public Collection ejbFindAll() throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		
		return idoFindPKsByQuery(query);
	}
}