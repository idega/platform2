/*
 * $Id: RegulationSpecTypeBMPBean.java,v 1.5 2003/08/19 10:17:27 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 * 
 */
package se.idega.idegaweb.commune.accounting.regulations.data;
    
import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDOQuery;
import com.idega.data.IDOLookup;

/**
 * Regulation spec types ("check", "modersmal", "blabla") etc. Used for the posting.
 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean 
 * <p>
 * $Id: RegulationSpecTypeBMPBean.java,v 1.5 2003/08/19 10:17:27 kjell Exp $
 * 
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.5 $
 */
public class RegulationSpecTypeBMPBean extends GenericEntity implements RegulationSpecType {
	
	private static final String ENTITY_NAME = "cacc_reg_spec_type";
	private static final String COLUMN_REG_SPEC_TYPE = "reg_spec_type";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void insertStartData () throws Exception {
		super.insertStartData ();
        
		System.out.println ("¤¤¤ Invoked " + ENTITY_NAME + ".insertStartData ()");

		RegulationSpecTypeHome home
				= (RegulationSpecTypeHome) IDOLookup.getHome(RegulationSpecType.class);
		final String [] data = { "check", "modersmal"};
		for (int i = 0; i < data.length; i++) {
			RegulationSpecType regSpec = home.create();
			regSpec.setRegSpecType(ENTITY_NAME + "." + data[i]);
			regSpec.store();
		}
	}


	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_REG_SPEC_TYPE, "Regulation specification type", true, true, String.class);
		setAsPrimaryKey (getIDColumnName(), true);
	}

	public void setRegSpecType(String type) { 
		setColumn(COLUMN_REG_SPEC_TYPE, type); 
	}
	
	public String getRegSpecType() {
		return (String) getStringColumnValue(COLUMN_REG_SPEC_TYPE);
	}

	public Collection ejbFindAllRegulationSpecTypes() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.append(getEntityName());
		return idoFindPKsBySQL(sql.toString());
	}

	public Object ejbFindRegulationSpecType(int id) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(getIDColumnName(), id);
		return idoFindOnePKByQuery(sql);
	}
}
