/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.economa.business;

import java.util.Collection;
import java.util.Locale;


import se.idega.idegaweb.commune.accounting.export.business.ExportBusiness;
import se.idega.idegaweb.commune.accounting.export.business.MoveFileException;
import se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckHeader;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.school.business.ProviderBusiness;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolCategory;
import com.idega.business.IBOService;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author bluebottle
 *
 */
public interface EconomaBusiness extends IBOService {
	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.business.EconomaBusinessBean#getJournalLog
	 */
	public Collection getJournalLog() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.business.EconomaBusinessBean#getJournalLogBySchoolCategory
	 */
	public Collection getJournalLogBySchoolCategory(String category)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.business.EconomaBusinessBean#getJournalLogBySchoolCategory
	 */
	public Collection getJournalLogBySchoolCategory(SchoolCategory category)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.business.EconomaBusinessBean#getEconomaCheckHeaderBySchoolCategory
	 */
	public EconomaCheckHeader getEconomaCheckHeaderBySchoolCategory(
			String category) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.business.EconomaBusinessBean#getEconomaCheckHeaderBySchoolCategory
	 */
	public EconomaCheckHeader getEconomaCheckHeaderBySchoolCategory(
			SchoolCategory category) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.business.EconomaBusinessBean#getEconomaCheckRecordByHeaderId
	 */
	public Collection getEconomaCheckRecordByHeaderId(int headerId)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.business.EconomaBusinessBean#createFiles
	 */
	public void createFiles(String schoolCategory, IWTimestamp paymentDate,
			String periodText, User user, Locale currentLocale)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.business.EconomaBusinessBean#deleteFiles
	 */
	public void deleteFiles(String schoolCategory, User user)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.business.EconomaBusinessBean#moveFiles
	 */
	public void moveFiles(String schoolCategory) throws MoveFileException,
			java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.business.EconomaBusinessBean#getExportBusiness
	 */
	public ExportBusiness getExportBusiness() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.business.EconomaBusinessBean#getUserBusiness
	 */
	public UserBusiness getUserBusiness() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.business.EconomaBusinessBean#getSchoolBusiness
	 */
	public SchoolBusiness getSchoolBusiness() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.business.EconomaBusinessBean#getPostingBusiness
	 */
	public PostingBusiness getPostingBusiness() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.business.EconomaBusinessBean#getProviderBusiness
	 */
	public ProviderBusiness getProviderBusiness()
			throws java.rmi.RemoteException;

}
