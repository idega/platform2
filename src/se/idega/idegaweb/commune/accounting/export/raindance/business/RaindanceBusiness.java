/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.raindance.business;

import java.util.Collection;
import java.util.Locale;


import se.idega.idegaweb.commune.accounting.export.business.ExportBusiness;
import se.idega.idegaweb.commune.accounting.export.business.MoveFileException;
import se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceCheckHeader;
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
public interface RaindanceBusiness extends IBOService {
	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.business.RaindanceBusinessBean#getJournalLog
	 */
	public Collection getJournalLog() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.business.RaindanceBusinessBean#getJournalLogBySchoolCategory
	 */
	public Collection getJournalLogBySchoolCategory(String category)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.business.RaindanceBusinessBean#getJournalLogBySchoolCategory
	 */
	public Collection getJournalLogBySchoolCategory(SchoolCategory category)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.business.RaindanceBusinessBean#getRaindanceCheckHeaderBySchoolCategory
	 */
	public RaindanceCheckHeader getRaindanceCheckHeaderBySchoolCategory(
			String category) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.business.RaindanceBusinessBean#getRaindanceCheckHeaderBySchoolCategory
	 */
	public RaindanceCheckHeader getRaindanceCheckHeaderBySchoolCategory(
			SchoolCategory category) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.business.RaindanceBusinessBean#getRaindanceCheckRecordByHeaderId
	 */
	public Collection getRaindanceCheckRecordByHeaderId(int headerId)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.business.RaindanceBusinessBean#createFiles
	 */
	public void createFiles(String schoolCategory, IWTimestamp paymentDate,
			String periodText, User user, Locale currentLocale)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.business.RaindanceBusinessBean#deleteFiles
	 */
	public void deleteFiles(String schoolCategory, User user)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.business.RaindanceBusinessBean#moveFiles
	 */
	public void moveFiles(String schoolCategory) throws MoveFileException,
			java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.business.RaindanceBusinessBean#getExportBusiness
	 */
	public ExportBusiness getExportBusiness() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.business.RaindanceBusinessBean#getUserBusiness
	 */
	public UserBusiness getUserBusiness() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.business.RaindanceBusinessBean#getSchoolBusiness
	 */
	public SchoolBusiness getSchoolBusiness() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.business.RaindanceBusinessBean#getPostingBusiness
	 */
	public PostingBusiness getPostingBusiness() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.business.RaindanceBusinessBean#getProviderBusiness
	 */
	public ProviderBusiness getProviderBusiness()
			throws java.rmi.RemoteException;

}
