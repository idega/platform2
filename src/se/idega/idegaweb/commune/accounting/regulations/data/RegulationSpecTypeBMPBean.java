/*
 * $Id: RegulationSpecTypeBMPBean.java,v 1.10 2003/09/02 23:42:55 kjell Exp $
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
 * Regulation spec types ("check", "modersmal", "blabla") etc. Used for the posting.
 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean 
 * Has a relation to Main rules
 * @see se.idega.idegaweb.commune.accounting.regulations.data.MainRuleBMPBean#
 * <p>
 * 
 * $Id: RegulationSpecTypeBMPBean.java,v 1.10 2003/09/02 23:42:55 kjell Exp $
 * 
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.10 $
 */
public class RegulationSpecTypeBMPBean extends GenericEntity implements RegulationSpecType {
	
	private static final String ENTITY_NAME = "cacc_reg_spec_type";
	private static final String COLUMN_REG_SPEC_TYPE = "reg_spec_type";
	private static final String COLUMN_MAIN_RULE_ID = "main_rule_id";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void insertStartData () throws Exception {
		super.insertStartData ();
        
		System.out.println ("¤¤¤ Invoked " + ENTITY_NAME + ".insertStartData ()");

		RegulationSpecTypeHome home
				= (RegulationSpecTypeHome) IDOLookup.getHome(RegulationSpecType.class);
		final String [] data1 = { "check", "modersmal", "svenska_2", "laginkomstskydd", "syskonrabatt"};
		final int [] data2 = { 1, 2, 2, 3, 3};
		for (int i = 0; i < data1.length; i++) {
			RegulationSpecType regSpec = home.create();
			regSpec.setRegSpecType(ENTITY_NAME + "." + data1[i]);
			regSpec.setMainRule(data2[i]);
			regSpec.store();
		}
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_REG_SPEC_TYPE, "Regulation specification type", true, true, String.class);
		addAttribute(COLUMN_MAIN_RULE_ID, "Main rule", true, true, 
						Integer.class, "many-to-one", MainRule.class);
		setAsPrimaryKey (getIDColumnName(), true);
	}

	public void setRegSpecType(String type) { 
		setColumn(COLUMN_REG_SPEC_TYPE, type); 
	}
	
	public String getRegSpecType() {
		return (String) getStringColumnValue(COLUMN_REG_SPEC_TYPE);
	}

	public void setMainRule(int rule) { 
		setColumn(COLUMN_MAIN_RULE_ID, rule); 
	}
	
	public MainRule getMainRule() {
		return (MainRule) getColumnValue(COLUMN_MAIN_RULE_ID);
	}

	public void setLocalizationKey(String type) { 
		setColumn(COLUMN_REG_SPEC_TYPE, type); 
	}
	
	public String getLocalizationKey() {
		return (String) getStringColumnValue(COLUMN_REG_SPEC_TYPE);
	}

	public Collection ejbFindAllRegulationSpecTypes() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		return idoFindPKsBySQL(sql.toString());
	}

	public Object ejbFindRegulationSpecType(int id) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(getIDColumnName(), id);
		return idoFindOnePKByQuery(sql);
	}
}
