/*
 * Created on Mar 29, 2004
 *
 */
package is.idega.idegaweb.campus.data;

import com.idega.data.PrimaryKey;

/**
 * ApartmentAccountEntryKey
 * @author aron 
 * @version 1.0
 */
public class BatchContractKey extends PrimaryKey {
	
	private String COLUMN_BATCH= BatchContractBMPBean.COLUMN_BATCH_ID;
	private String COLUMN_CONTRACT = BatchContractBMPBean.COLUMN_CONTRACT_ID;
	
	/**
	 * @param batchID
	 * @param contractID
	 */
	public BatchContractKey(Object batchID, Object contractID) {
		this();
		setBatch(batchID);
		setContract(contractID);
	}
	
	public BatchContractKey() {
		super();
	}
	
	public void setBatch(Object batchID) {
		setPrimaryKeyValue(COLUMN_BATCH,batchID);
	}

	public Object getBatch() {
		return getPrimaryKeyValue(COLUMN_BATCH);
	}

	public void setContract(Object contractID) {
		setPrimaryKeyValue(COLUMN_CONTRACT, contractID);
	}

	public Object getContract() {
		return getPrimaryKeyValue(COLUMN_CONTRACT);
	}
}
