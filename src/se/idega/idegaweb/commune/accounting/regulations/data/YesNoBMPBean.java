/*
 * $Id: YesNoBMPBean.java,v 1.1 2003/09/11 15:54:25 kjell Exp $
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
 * Holds localizeable Yes / No data for the regulation framework 
 * <p>
 * $Id: YesNoBMPBean.java,v 1.1 2003/09/11 15:54:25 kjell Exp $
 * 
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.1 $
 */
public class YesNoBMPBean extends GenericEntity implements YesNo {
	
	private static final String ENTITY_NAME = "cacc_regulation_yesno";
	private static final String COLUMN_YESNO = "yesno";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void insertStartData () throws Exception {
		super.insertStartData ();
        
		System.out.println ("¤¤¤ Invoked " + ENTITY_NAME + ".insertStartData ()");

		YesNoHome home = (YesNoHome) IDOLookup.getHome(YesNo.class);
		final String [] data = { "yes", "no" };
		for (int i = 0; i < data.length; i++) {
			YesNo yn = home.create();
			yn.setYesNo(ENTITY_NAME + "." + data[i]);
			yn.store();
		}
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_YESNO, "Yes/No value", true, true, String.class);
		setAsPrimaryKey (getIDColumnName(), true);
	}

	public void setYesNo(String type) { 
		setColumn(COLUMN_YESNO, type); 
	}
	
	public String getYeNo() {
		return getStringColumnValue(COLUMN_YESNO);
	}

	public void setLocalizationKey(String key) { 
		setColumn(COLUMN_YESNO, key); 
	}
	
	public String getLocalizationKey() {
		return getStringColumnValue(COLUMN_YESNO);
	}

	public Collection ejbFindAllYesNoValues() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		return idoFindPKsBySQL(sql.toString());
	}

	public Object ejbFindYesNoValue(int id) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(getIDColumnName(), id);
		return idoFindOnePKByQuery(sql);
	}

}
