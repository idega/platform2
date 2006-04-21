/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOQuery;

/**
 * @author palli
 */
public class CreditCardTypeBMPBean extends GenericEntity implements CreditCardType {
	protected final static String ENTITY_NAME = "isi_creditcard_type";
	
	protected final static String COLUMN_CREDITCARD_TYPE = "card_type";
	protected final static String COLUMN_NAME = "card_type_name";
	protected final static String COLUMN_LOCALIZED_KEY = "localized_key";

	protected final static String TYPE_VISA = "VISA";
	protected final static String TYPE_MASTERCARD = "MASTERCARD";
	protected final static String TYPE_UNDEFINED = "UNDEFINED";
	protected final static String TYPE_OTHER = "OTHER";

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
		addAttribute(COLUMN_CREDITCARD_TYPE,"Creditcard type",true,true,java.lang.String.class,255);
		addAttribute(COLUMN_NAME,"Creditcard name",true,true,java.lang.String.class,255);
		addAttribute(COLUMN_LOCALIZED_KEY,"Creditcard localized key",true,true,java.lang.String.class,255);
	}

	public void insertStartData() throws Exception {

		String types[] = {TYPE_VISA, TYPE_MASTERCARD, TYPE_UNDEFINED, TYPE_OTHER};
		
		String names[] = {"Visa", "Mastercard"};
		
		CreditCardTypeHome typeHome = (CreditCardTypeHome) IDOLookup.getHome(CreditCardType.class);
		CreditCardType type;
		
		for (int i = 0; i < types.length; i++) {
			type = typeHome.create();
			type.setCreditCardType(types[i]);
			type.setName(names[i]);
			StringBuffer b = new StringBuffer(ENTITY_NAME);
			b.append(".");
			b.append(types[i]);
			type.setLocalizedKey(b.toString());
			type.store();
		}
	}
	
	public void setCreditCardType(String type) {
		setColumn(COLUMN_CREDITCARD_TYPE,type);
	}
	
	public void setName(String name) {
		setColumn(COLUMN_NAME,name);
	}
	
	public void setLocalizedKey(String key) {
		setColumn(COLUMN_LOCALIZED_KEY,key);
	}
	
	public String getCreditCardType() {
		return getStringColumnValue(COLUMN_CREDITCARD_TYPE);
	}
	
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}
	
	public String getLocalizedKey() {
		return getStringColumnValue(COLUMN_LOCALIZED_KEY);
	}
	
	public Collection ejbFindAll() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);

		return idoFindPKsBySQL(sql.toString());
	}	
	
	public Object ejbFindTypeVisa() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEqualsQuoted(COLUMN_CREDITCARD_TYPE, TYPE_VISA);

		System.out.println("sql = " + sql.toString());

		return idoFindOnePKByQuery(sql);
	}
}