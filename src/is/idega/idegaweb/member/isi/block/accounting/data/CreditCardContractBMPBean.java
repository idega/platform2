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
import com.idega.data.IDOQuery;
import com.idega.user.data.Group;

/**
 * @author palli
 */
public class CreditCardContractBMPBean extends GenericEntity implements CreditCardContract {
	protected final static String ENTITY_NAME = "isi_credit_contract";
	
	protected final static String COLUMN_CLUB = "club_id";
	protected final static String COLUMN_DIVISION = "div_id";
	protected final static String COLUMN_CONTRACT_NR = "contract_number";
	protected final static String COLUMN_CARD_TYPE = "card_type_id";

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
		addManyToOneRelationship(COLUMN_DIVISION,Group.class);
		addAttribute(COLUMN_CONTRACT_NR,"Contract number",true,true,String.class);
		addManyToOneRelationship(COLUMN_CARD_TYPE,CreditCardType.class);
	}

	public void setClubID(int id) {
		setColumn(COLUMN_CLUB,id);
	}
	
	public void setClub(Group club) {
		setColumn(COLUMN_CLUB,club);
	}

	public void setDivisionId(int id) {
		setColumn(COLUMN_DIVISION,id);
	}
	
	public void setDivision(Group division) {
		setColumn(COLUMN_DIVISION,division);
	}

	public void setContractNumber(String number) {
		setColumn(COLUMN_CONTRACT_NR,number);
	}
	
	public void setCardTypeId(int id) {
		setColumn(COLUMN_CARD_TYPE,id);
	}
	
	public void setCardType(CreditCardType type) {
		setColumn(COLUMN_CARD_TYPE,type);
	}
	
	public int getClubID() {
		return getIntColumnValue(COLUMN_CLUB);
	}
	
	public Group getClub() {
		return (Group)getColumnValue(COLUMN_CLUB);
	}

	public int getDivisionId() {
		return getIntColumnValue(COLUMN_DIVISION);
	}
	
	
	public Group getDivision() {
		return (Group)getColumnValue(COLUMN_DIVISION);
	}

	public String getContractNumber() {
		return getStringColumnValue(COLUMN_CONTRACT_NR);
	}
	
	public int getCardTypeId() {
		return getIntColumnValue(COLUMN_CARD_TYPE);
	}

	public CreditCardType getCardType() {
		return (CreditCardType) getColumnValue(COLUMN_CARD_TYPE);
	}
	
	public Collection ejbFindAllByClub(Group club) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_CLUB, ((Integer)club.getPrimaryKey()).intValue());
		
		return idoFindPKsByQuery(sql);
	}	
}