/*
 * $Id: CoursePackageBMPBean.java,v 1.1 2005/07/07 08:41:42 laddi Exp $
 * Created on Jul 4, 2005
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
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;


/**
 * Last modified: $Date: 2005/07/07 08:41:42 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class CoursePackageBMPBean extends GenericEntity  implements CoursePackage{

	private static final String ENTITY_NAME = "vux_package";
	
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_LOCALIZED_KEY = "localized_key";
	
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		
		addAttribute(COLUMN_NAME, "Name", String.class);
		addAttribute(COLUMN_LOCALIZED_KEY, "Localized key", String.class);
	}
	
	//Getters
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}
	
	public String getLocalizedKey() {
		return getStringColumnValue(COLUMN_LOCALIZED_KEY);
	}
	
	//Setters
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}
	
	public void setLocalizedKey(String key) {
		setColumn(COLUMN_LOCALIZED_KEY, key);
	}
	
	//Finders
	public Collection ejbFindAll() throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		
		return idoFindPKsByQuery(query);
	}
}