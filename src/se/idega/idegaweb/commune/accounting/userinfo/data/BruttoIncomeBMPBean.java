/*
 * Created on Aug 11, 2003
 *
 */
package se.idega.idegaweb.commune.accounting.userinfo.data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.user.data.User;

/**
 * BruttoIncome used to store income info about users.
 * @author aron 
 * @version 1.0
 */

public class BruttoIncomeBMPBean extends GenericEntity implements BruttoIncome {
	
	public final static String ENTITY_NAME = "CACC_BRUTTO_INCOME";
	public final static String COLUMN_USER = "USER_ID";
	public final static String COLUMN_INCOME = "INCOME";
	public final static String COLUMN_VALID_FROM = "VALID_FROM";
	public final static String COLUMN_CREATED = "CREATED";
	public final static String COLUMN_CREATOR = "CREATOR";

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
		addManyToOneRelationship(COLUMN_USER,User.class);
		addAttribute(COLUMN_INCOME, "Income", true, true, java.lang.Float.class);
		addAttribute(COLUMN_VALID_FROM, "Valid from", true, true,Date.class);
		addAttribute(COLUMN_CREATED, "Created", true, true,java.sql.Timestamp.class);
		addManyToOneRelationship(COLUMN_CREATOR,User.class);
	}
	
	public User getUser(){
		return (User)getColumnValue(COLUMN_USER);
	}
	
	public Integer getUserID(){
		return getIntegerColumnValue(COLUMN_USER);
	}
	
	public void setUser(Integer userID){
		setColumn(COLUMN_USER,userID);
	}
	
	public void setUser(int userID){
		setColumn(COLUMN_USER,userID);
	}
	
	public void setUser(User user){
		setColumn(COLUMN_USER,user);
	}
	
	public Float getIncome(){
		return new Float(getFloatColumnValue(COLUMN_INCOME));
	}
	
	public void setIncome(Float income){
		setColumn(COLUMN_INCOME,income);
	}
	
	public void setIncome(float income){
		setColumn(COLUMN_INCOME,income);
	}
	
	public Date getValidFrom(){
		return (Date)getColumnValue(COLUMN_VALID_FROM);
	}
	
	public void setValidFrom(Date from){
		setColumn(COLUMN_VALID_FROM,from);
	}
	
	public Timestamp getCreated(){
		return (Timestamp)getColumnValue(COLUMN_CREATED);
	}
	
	public void setCreated(Timestamp from){
		setColumn(COLUMN_CREATED,from);
	}
	
	public User getCreator(){
		return (User) getColumnValue(COLUMN_CREATOR);
	}
	
	public Integer getCreatorID(){
		return getIntegerColumnValue(COLUMN_CREATOR);
	}
	
	public void setCreator(Integer creator){
		setColumn(COLUMN_CREATOR,creator);
	}
	
	public void setCreator(int creator){
		setColumn(COLUMN_CREATOR,creator);
	}
	
	public void setCreator(User creator){
		setColumn(COLUMN_CREATOR,creator);
	}
	
	public Collection ejbFindByUser(Integer userID) throws FinderException{
		return super.idoFindPKsByQuery(idoQueryGetSelect().appendWhereEquals(COLUMN_USER,userID.intValue()).appendOrderByDescending(COLUMN_VALID_FROM)); 
	}
	
	public Object ejbFindLatestByUser(Integer userID) throws FinderException{
		Collection pks = super.idoFindPKsByQuery(idoQueryGetSelect().appendWhereEquals(COLUMN_USER,userID.intValue()).appendOrderByDescending(COLUMN_VALID_FROM),1);
		if(pks!=null && !pks.isEmpty())
			return pks.iterator().next();
		throw new FinderException("Nothing found");
	}
}
