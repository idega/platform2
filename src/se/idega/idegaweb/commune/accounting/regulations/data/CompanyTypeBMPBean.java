/*
 * $Id: CompanyTypeBMPBean.java,v 1.6 2003/08/27 07:38:49 kjell Exp $
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
 * Holds Company types ("Kommun", "Stiftelse", "AB") etc. 
 * Used for the posting but should be used for any translation of Company Types code 
 * 
 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean 
 * <p>
 * $Id: CompanyTypeBMPBean.java,v 1.6 2003/08/27 07:38:49 kjell Exp $
 * 
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.6 $
 */
public class CompanyTypeBMPBean extends GenericEntity implements CompanyType {
	
	private static final String ENTITY_NAME = "cacc_company_type";
	private static final String COLUMN_COMPANY_TYPE = "company_type";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void insertStartData () throws Exception {
		super.insertStartData ();
        
		System.out.println ("¤¤¤ Invoked " + ENTITY_NAME + ".insertStartData ()");

		CompanyTypeHome home
				= (CompanyTypeHome) IDOLookup.getHome(CompanyType.class);
		final String [] data = { "blank", "kommun", "stiftelse", "ab", "ovr_foretag" };
		for (int i = 0; i < data.length; i++) {
			CompanyType ct = home.create();
			ct.setCompanyType(ENTITY_NAME + "." + data[i]);
			ct.store();
		}
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_COMPANY_TYPE, "Companytype", true, true, String.class);
		setAsPrimaryKey (getIDColumnName(), true);
	}

	public void setCompanyType(String type) { 
		setColumn(COLUMN_COMPANY_TYPE, type); 
	}
	
	public String getCompanyType() {
		return (String) getStringColumnValue(COLUMN_COMPANY_TYPE);
	}

	public void setTextKey(String type) { 
		setColumn(COLUMN_COMPANY_TYPE, type); 
	}
	
	public String getTextKey() {
		return (String) getStringColumnValue(COLUMN_COMPANY_TYPE);
	}


	public Collection ejbFindAllCompanyTypes() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		return idoFindPKsBySQL(sql.toString());
	}

	public Object ejbFindCompanyType(int id) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(getIDColumnName(), id);
		return idoFindOnePKByQuery(sql);
	}

}
