/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.data;

import java.sql.Date;

import com.idega.data.GenericEntity;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * @author palli
 */
public class UserCreditCardBMPBean extends GenericEntity implements UserCreditCard {
	protected final static String ENTITY_NAME = "isi_user_creditcard";
	
	protected final static String COLUMN_CLUB = "club_id";
	protected final static String COLUMN_USER = "user_id";
	protected final static String COLUMN_CARD_TYPE = "card_type_id";
	protected final static String COLUMN_CARD_NR = "card_number";
	protected final static String COLUMN_CARD_VALID = "card_valid";

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
		addManyToOneRelationship(COLUMN_CLUB,Group.class);
		addManyToOneRelationship(COLUMN_USER,User.class);
		addManyToOneRelationship(COLUMN_CARD_TYPE,CreditCardType.class);
		addAttribute(COLUMN_CARD_NR,"Card number",true,true,String.class);
		addAttribute(COLUMN_CARD_VALID,"Card valid",true,true,Date.class);
	}

	public void setClubID(int id) {
		setColumn(COLUMN_CLUB,id);
	}
	
	public void setClub(Group club) {
		setColumn(COLUMN_CLUB,club);
	}

	public void setCardOwnerId(int id) {
		setColumn(COLUMN_USER,id);
	}
	
	public void setCardOwner(User user) {
		setColumn(COLUMN_USER,user);
	}
	
	public void setCardTypeId(int id) {
		setColumn(COLUMN_CARD_TYPE,id);
	}
	
	public void setCardType(CreditCardType type) {
		setColumn(COLUMN_CARD_TYPE,type);
	}
	
	public void setCardNumber(String number) {
		setColumn(COLUMN_CARD_NR,number);
	}
	
	public void setCardValidDate(Date date) {
		setColumn(COLUMN_CARD_VALID,date);
	}
	
	public int getClubID() {
		return getIntColumnValue(COLUMN_CLUB);
	}
	
	public Group getClub() {
		return (Group)getColumnValue(COLUMN_CLUB);
	}

	public int getCardOwnerId() {
		return getIntColumnValue(COLUMN_USER);
	}
	
	public User getCardOwner() {
		return (User) getColumnValue(COLUMN_USER);
	}

	public int getCardTypeId() {
		return getIntColumnValue(COLUMN_CARD_TYPE);
	}

	public CreditCardType getCardType() {
		return (CreditCardType) getColumnValue(COLUMN_CARD_TYPE);
	}

	public String getCardNumber() {
		return getStringColumnValue(COLUMN_CARD_NR);
	}
	
	public Date getCardValidDate() {
		return getDateColumnValue(COLUMN_CARD_VALID);
	}
}