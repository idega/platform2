/*
 * $Id: AdultEducationChoiceReasonBMPBean.java,v 1.1 2005/05/11 07:16:22 laddi Exp $
 * Created on May 3, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.GenericEntity;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;


public class AdultEducationChoiceReasonBMPBean extends GenericEntity  implements AdultEducationChoiceReason{

	private static final String ENTITY_NAME = "vux_choice_reason";

	private static final String NAME = "reason_name";
	private static final String LOCALIZED_KEY = "localized_key";
	private static final String IS_ACTIVE = "is_active";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		
		addAttribute(NAME, "Name", String.class);
		addAttribute(LOCALIZED_KEY, "Localized key", String.class);
		addAttribute(IS_ACTIVE, "Is active", Boolean.class);
	}

	public void setDefaultValues() {
		setIsActive(true);
	}
	
	//Getters
	public String getName() {
		return getStringColumnValue(NAME);
	}
	
	public String getLocalizedKey() {
		return getStringColumnValue(LOCALIZED_KEY);
	}
	
	public boolean isActive() {
		return getBooleanColumnValue(IS_ACTIVE, true);
	}
	
	//Setters
	public void setName(String name) {
		setColumn(NAME, name);
	}
	
	public void setLocalizedKey(String localizedKey) {
		setColumn(LOCALIZED_KEY, localizedKey);
	}
	
	public void setIsActive(boolean isActive) {
		setColumn(IS_ACTIVE, isActive);
	}
	
	//Finders
	public Collection ejbFindAll() throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, IS_ACTIVE, MatchCriteria.EQUALS, true));
		
		return idoFindPKsByQuery(query);
	}
}
