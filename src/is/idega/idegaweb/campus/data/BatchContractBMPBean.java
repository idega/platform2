/*
 * Created on Mar 15, 2004
 *
 */
package is.idega.idegaweb.campus.data;

import is.idega.idegaweb.campus.block.allocation.data.Contract;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.finance.data.AssessmentRound;
import com.idega.data.GenericEntity;

/**
 * ApartmentAccountEntryBMPBean
 * @author aron 
 * @version 1.0
 */
public class BatchContractBMPBean extends GenericEntity implements BatchContract  {
	public final static String ENTITY_NAME = "CAM_BATCH_CONTRACT";
	public final static String COLUMN_BATCH_ID = "BATCH_ID";
	public final static String COLUMN_CONTRACT_ID = "CONTRACT_ID";
	
	
	/* (non-Javadoc)
	 * @see com.idega.data.IDOEntityBean#getPrimaryKeyClass()
	 */
	public Class getPrimaryKeyClass() {
		return BatchContractKey.class;
	}
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
		addManyToOneRelationship(COLUMN_BATCH_ID, AssessmentRound.class);
		addManyToOneRelationship(COLUMN_CONTRACT_ID, Contract.class);
		setAsPrimaryKey(COLUMN_BATCH_ID, true);
		setAsPrimaryKey(COLUMN_CONTRACT_ID, true);
		setNullable(COLUMN_BATCH_ID, false);
		setNullable(COLUMN_CONTRACT_ID, false);
		
	}
	
	public void setBatchID(Integer entryID){
		setColumn(COLUMN_BATCH_ID,entryID);
	}
	
	public Integer getBatchID(){
		return getIntegerColumnValue(COLUMN_BATCH_ID);
	}
	
	public void setContractID(Integer apartmentID){
		setColumn(COLUMN_CONTRACT_ID,apartmentID);
	}
	
	public Integer getContractID(){
		return getIntegerColumnValue(COLUMN_CONTRACT_ID);
	}
	
	public Object ejbFindByPrimaryKey(BatchContractKey primaryKey) throws FinderException {
		return super.ejbFindByPrimaryKey(primaryKey);
	}
	
	public Object ejbCreate(BatchContractKey primaryKey) throws CreateException {
		setPrimaryKey(primaryKey);
		return super.ejbCreate();
	}
	
}
