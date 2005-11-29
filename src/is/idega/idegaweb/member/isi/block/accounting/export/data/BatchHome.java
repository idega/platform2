/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.export.data;

import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContract;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.finance.data.BankInfo;
import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface BatchHome extends IDOHome {
	public Batch create() throws javax.ejb.CreateException;

	public Batch findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#ejbFindUnsentByContractAndCreditCardType
	 */
	public Batch findUnsentByContractAndCreditCardType(
			CreditCardContract contract, CreditCardType type)
			throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#ejbFindUnsentByBankInfo
	 */
	public Batch findUnsentByBankInfo(BankInfo info) throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.BatchBMPBean#ejbFindAllNewestFirst
	 */
	public Collection findAllNewestFirst() throws FinderException;

}
