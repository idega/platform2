/*
 * $Id: VATRuleBMPBean.java,v 1.3 2003/11/11 10:57:45 anders Exp $
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
 * Holds VAT rules ("Momsersättning förskoleklass", "Momsersättning grundskola" etc) 
 * 
 * <p>
 * $Id: VATRuleBMPBean.java,v 1.3 2003/11/11 10:57:45 anders Exp $
 * 
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.3 $
 */
public class VATRuleBMPBean extends GenericEntity implements VATRule {
	
	private static final String ENTITY_NAME = "cacc_vat_rule";
	private static final String COLUMN_VAT_RULE = "vat_rule";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void insertStartData () throws Exception {
		super.insertStartData ();
        
		System.out.println ("¤¤¤ Invoked " + ENTITY_NAME + ".insertStartData ()");

		VATRuleHome home
				= (VATRuleHome) IDOLookup.getHome(VATRule.class);
		final String [] data = { 
			"momsersattning_forskola",
			"momsersattning_familjedaghem",
			"momsersattning_allman_forskola",
			"momsersattning_oppen_forskoleverksamhet",
			"momsersattning_fritidshem",
			"momsersattning_familjefritidshem",
			"momsersattning_oppen_fritidsverksamhet",
			"momsersattning_forskoleklass",
			"momsersattning_grundskola",
			"momsersattning_obligatorisk_sarskola",
			"momsersattning_gymnasieskola",
			"momsersattning_gymnasiesarskola",
			"momsersattning_individresurs_enligt_regelverk",
			"momsersattning_individresurs_for_nyanlanda",
			"momsersattning_individresurs_enligt_avtal",
			"momsersattning_modersmal",
			"momsersattning_svenska_som_andrasprak",
			"momsersattning_mindre_undervisningsgrupper"
		};
		for (int i = 0; i < data.length; i++) {
			VATRule sc = home.create();
			sc.setVATRule(ENTITY_NAME + "." + data[i]);
			sc.store();
		}
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_VAT_RULE, "VAT Rule", true, true, String.class);
		setAsPrimaryKey (getIDColumnName(), true);
	}

	public void setVATRule(String type) { 
		setColumn(COLUMN_VAT_RULE, type); 
	}
	
	public String getVATRule() {
		return getStringColumnValue(COLUMN_VAT_RULE);
	}

	public void setLocalizationKey(String type) { 
		setColumn(COLUMN_VAT_RULE, type); 
	}
	
	public String getLocalizationKey() {
		return getStringColumnValue(COLUMN_VAT_RULE);
	}

	public Collection ejbFindAllVATRules() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		return idoFindPKsBySQL(sql.toString());
	}

	public Object ejbFindVATRule(int id) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(getIDColumnName(), id);
		return idoFindOnePKByQuery(sql);
	}

}
