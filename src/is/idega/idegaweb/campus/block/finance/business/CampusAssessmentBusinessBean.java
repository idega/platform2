/*
 * Created on Mar 30, 2004
 *
 */
package is.idega.idegaweb.campus.block.finance.business;

import is.idega.idegaweb.campus.data.ApartmentAccountEntry;
import is.idega.idegaweb.campus.data.ApartmentAccountEntryBMPBean;
import is.idega.idegaweb.campus.data.ApartmentAccountEntryHome;
import is.idega.idegaweb.campus.data.BatchContractBMPBean;

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.CreateException;

import com.idega.block.finance.business.AssessmentBusinessBean;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.AccountEntryBMPBean;
import com.idega.block.finance.data.AssessmentRound;
import com.idega.block.finance.data.AssessmentRoundHome;
import com.idega.data.IDOLookup;
import com.idega.data.SimpleQuerier;

/**
 * CampusAssessmentBusinessBean
 * @author aron 
 * @version 1.0
 */
public class CampusAssessmentBusinessBean extends AssessmentBusinessBean implements CampusAssessmentBusiness{
	

	public ApartmentAccountEntry createApartmentAccountEntry(Integer accountEntryID,Integer apartmentID)throws RemoteException,CreateException{
		ApartmentAccountEntry aprtEntry = ((ApartmentAccountEntryHome) getIDOHome(ApartmentAccountEntry.class)).create();
		aprtEntry.setAccountEntryID(accountEntryID);
		aprtEntry.setApartmentID(apartmentID);
		aprtEntry.store();
		return aprtEntry;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.AssessmentBusiness#createAccountEntry(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, float, float, float, java.util.Date, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)
	 */
	public AccountEntry createAccountEntry(Integer accountID, Integer accountKeyID, Integer cashierID, Integer roundID,
			float netto, float VAT, float total, Date paydate, String Name, String Info, String status,
			Integer externalID) throws RemoteException, CreateException {
		// TODO Auto-generated method stub
		AccountEntry entry =  super.createAccountEntry(accountID, accountKeyID, cashierID, roundID, netto, VAT, total, paydate, Name,
				Info, status, externalID);
		createApartmentAccountEntry((Integer)entry.getPrimaryKey(),externalID);
		return entry;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.business.AssessmentBusiness#rollBackAssessment(int)
	 */
	public boolean rollBackAssessment(Integer assessmentRoundId) {
		StringBuffer sql = new StringBuffer("delete from ");
		sql.append(AccountEntryBMPBean.getEntityTableName());
		sql.append(" where ").append(com.idega.block.finance.data.AccountEntryBMPBean.getRoundIdColumnName());
		sql.append(" = ").append(assessmentRoundId);
		
		StringBuffer sql2 = new StringBuffer("delete from ").append( ApartmentAccountEntryBMPBean.ENTITY_NAME)
		.append(" where ").append(ApartmentAccountEntryBMPBean.COLUMN_ENTRY_ID)
		.append(" in (select e.fin_acc_entry_id from  FIN_ACC_ENTRY e where FIN_ASSESSMENT_ROUND_ID =").append(assessmentRoundId).append(" )");
		
		StringBuffer sql3 = new StringBuffer("delete from ");
		sql3.append(BatchContractBMPBean.ENTITY_NAME);
		sql3.append(" where ").append(BatchContractBMPBean.COLUMN_BATCH_ID);
		sql3.append(" = ").append(assessmentRoundId);
		
		System.err.println(sql.toString());
		System.err.println(sql2.toString());
		System.err.println(sql3.toString());
		javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
		try {
			t.begin();
			AssessmentRound AR = ((AssessmentRoundHome) IDOLookup.getHome(AssessmentRound.class))
					.findByPrimaryKey(assessmentRoundId);
			SimpleQuerier.execute(sql3.toString());
			SimpleQuerier.execute(sql2.toString());
			SimpleQuerier.execute(sql.toString());
			AR.remove();
			t.commit();
			return true;
		} // Try block
		catch (Exception e) {
			try {
				t.rollback();
			} catch (javax.transaction.SystemException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
		return false;
	}
}
