/*	
 * $Id: MainRuleBMPBean.java,v 1.3 2003/09/02 23:48:05 kjell Exp $
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
 * Holds Main rulez types ("Check", "Resurs", "Subvention") etc. Used for the posting 
 * and many other things as well
 * 
 * @see se.idega.idegaweb.commune.accounting.regulation.data.RegulationSpecTypeBMPBean# 
 * <p>
 * $Id: MainRuleBMPBean.java,v 1.3 2003/09/02 23:48:05 kjell Exp $
 * 
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.3 $
 */
public class MainRuleBMPBean extends GenericEntity implements MainRule {
	
	private static final String ENTITY_NAME = "cacc_main_rule";
	private static final String COLUMN_MAIN_RULE = "main_rule";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void insertStartData () throws Exception {
		super.insertStartData ();
        
		System.out.println ("¤¤¤ Invoked " + ENTITY_NAME + ".insertStartData ()");

		MainRuleHome home
				= (MainRuleHome) IDOLookup.getHome(MainRule.class);
		final String [] data = { "check", "resurs", "subvention" };
		for (int i = 0; i < data.length; i++) {
			MainRule mainrule = home.create();
			mainrule.setMainRule(ENTITY_NAME + "." + data[i]);
			mainrule.store();
		}
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_MAIN_RULE, "Main rule", true, true, String.class);
		setAsPrimaryKey (getIDColumnName(), true);
	}

	public void setMainRule(String rule) { 
		setColumn(COLUMN_MAIN_RULE, rule); 
	}
	
	public String getMainRule() {
		return (String) getStringColumnValue(COLUMN_MAIN_RULE);
	}

	public void setLocalizationKey(String key) { 
		setColumn(COLUMN_MAIN_RULE, key); 
	}
	
	public String getLocalizationKey() {
		return (String) getStringColumnValue(COLUMN_MAIN_RULE);
	}

	public Collection ejbFindAllMainRules() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		return idoFindPKsBySQL(sql.toString());
	}

	public Object ejbFindMainRule(int id) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(getIDColumnName(), id);
		return idoFindOnePKByQuery(sql);
	}

}
