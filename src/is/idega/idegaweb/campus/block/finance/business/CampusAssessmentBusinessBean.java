/*
 * Created on Mar 30, 2004
 *
 */
package is.idega.idegaweb.campus.block.finance.business;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.data.ApartmentAccountEntry;
import is.idega.idegaweb.campus.data.ApartmentAccountEntryBMPBean;
import is.idega.idegaweb.campus.data.ApartmentAccountEntryHome;
import is.idega.idegaweb.campus.data.BatchContractBMPBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.finance.business.AssessmentBusinessBean;
import com.idega.block.finance.business.FinanceService;
import com.idega.block.finance.data.Account;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.AccountEntryBMPBean;
import com.idega.block.finance.data.AccountEntryHome;
import com.idega.block.finance.data.AccountHome;
import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.data.AccountKeyHome;
import com.idega.block.finance.data.AssessmentRound;
import com.idega.block.finance.data.AssessmentRoundHome;
import com.idega.block.finance.data.EntryGroup;
import com.idega.block.finance.data.FinanceAccount;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.IWTimestamp;
import com.idega.util.database.ConnectionBroker;
import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLOutput;

/**
 * CampusAssessmentBusinessBean
 * @author aron 
 * @version 1.0
 */
public class CampusAssessmentBusinessBean extends AssessmentBusinessBean implements CampusAssessmentBusiness{
	
	private static final String INVOICE = "Invoices";

	private static final String HEAD_NUMBER = "HeadNumber";

	private static final String ID = "id";

	private static final String INVOICE_DATE = "InvoiceDate";

	private static final String DUE_DATE = "InvoiceDueDate";

	private static final String CUSTOMER_ID = "Customer";

	private static final String CUSTOMER_NAME = "CName";

	private static final String CUSTOMER_ADDRESS_1 = "CAddress1";

	private static final String CUSTOMER_ADDRESS_2 = "CAddress2";

	private static final String CUSTOMER_ZIP_CODE = "CZipCode";

	private static final String CUSTOMER_SOCIAL_SECURITY_NUMBER = "CSSNumber";

	private static final String CUSTOMER_PHONE = "CPhone";

	private static final String LINE_NUMBER = "LineNumber";

	private static final String ITEM_CODE = "ItemCode";

	private static final String UNIT_PRICE = "UnitPriceWithTax";

	private static final String QUANTITY = "Quantity";

	private static final String ITEM_DESCRIPTION = "ItemDescription";

	private static final String DIVISION = "Dim1";


	public void createDKXMLFile(EntryGroup entryGroup, Locale locale) {
		try {
			IWTimestamp invoiceDate = new IWTimestamp(entryGroup.getFileInvoiceDate());
			IWTimestamp dueDate = new IWTimestamp(entryGroup.getFileDueDate());
			String invoiceDateString = invoiceDate.getDateString("dd.MM.yyyy");
			String dueDateString = dueDate.getDateString("dd.MM.yyyy");
			NumberFormat format = NumberFormat.getInstance(locale);
			format.setMaximumFractionDigits(0);
			format.setGroupingUsed(false);

			Collection entries = ((AccountEntryHome) IDOLookup.getHome(AccountEntry.class)).findByEntryGroup((Integer)entryGroup.getPrimaryKey());
			Map accountMap = new HashMap();
			if (entries != null && !entries.isEmpty()) {
				Iterator it = entries.iterator();
				while (it.hasNext()) {
					AccountEntry entry = (AccountEntry) it.next();
					Integer id = new Integer(entry.getAccountId());
					Map divisionMap = null;
					if (accountMap.containsKey(id)) {
						divisionMap = (Map) accountMap.get(id);
					}
					else {
						divisionMap = new HashMap();
						accountMap.put(id, divisionMap);
					}

					String division = entry.getDivisionForAccounting();
					Map entryMap = null;
					if (divisionMap.containsKey(division)) {
						entryMap = (Map) divisionMap.get(division);
					}
					else {
						entryMap = new HashMap();
						divisionMap.put(division, entryMap);
					}

					// special
					if (entry.getAccountKeyId() == 7) {
						entry.setAccountKeyId(2);
					}
					Integer acc_key = new Integer(entry.getAccountKeyId());
					if (entryMap.containsKey(acc_key)) {
						AccountEntry oldEntry = (AccountEntry) entryMap.get(acc_key);
						oldEntry.setTotal(oldEntry.getTotal() + entry.getTotal());
						entryMap.put(acc_key, oldEntry);
					}
					else {
						entryMap.put(acc_key, entry);
					}
				}

				if (!accountMap.isEmpty()) {
					XMLDocument doc = new XMLDocument(new XMLElement(INVOICE));
					XMLElement rootElement = doc.getRootElement();

					int counter = 0;
					Iterator keys = accountMap.keySet().iterator();
					while (keys.hasNext()) {
						counter++;
						Integer finAccId = (Integer) keys.next();
						Account account = ((AccountHome) IDOLookup.getHome(Account.class)).findByPrimaryKey(finAccId);
						User user = ((UserHome) IDOLookup.getHome(User.class)).findByPrimaryKey(new Integer(
								account.getUserId()));

						is.idega.idegaweb.campus.block.allocation.data.Contract contract = findContractForUser(user);
						
						if (contract == null) {
							System.out.println("!!!!!!!!!!Skipping contract for user " + user.getName() + ", number = " + counter);
						}
						
						if (contract != null) {
							XMLElement header = new XMLElement(HEAD_NUMBER);
							header.setAttribute(ID, Integer.toString(counter));
							header.addContent(INVOICE_DATE, invoiceDateString);
							header.addContent(DUE_DATE, dueDateString);
							header.addContent(CUSTOMER_ID, user.getPersonalID());
							StringBuffer fullname = new StringBuffer(user.getFirstName());
							fullname.append(" ");
							if (user.getMiddleName() != null && !"".equals(user.getMiddleName().trim())) {
								fullname.append(user.getMiddleName());
								fullname.append(" ");
							}
							fullname.append(user.getLastName());
							header.addContent(CUSTOMER_NAME, fullname.toString());
							header.addContent(CUSTOMER_ADDRESS_1,
									contract.getApartment().getFloor().getBuilding().getName());
							header.addContent(CUSTOMER_ADDRESS_2, contract.getApartment().getName());
							String zipCode = contract.getApartment().getFloor().getBuilding().getPostalCode();
							if (zipCode == null || "".equals(zipCode.trim())) {
								zipCode = "101";
							}
							header.addContent(CUSTOMER_ZIP_CODE, zipCode);
							header.addContent(CUSTOMER_SOCIAL_SECURITY_NUMBER, user.getPersonalID());

							rootElement.addContent(header);

							Map divisionMap = (Map) accountMap.get(finAccId);
							int lineCounter = 0;
							Iterator divIt = divisionMap.keySet().iterator();
							while (divIt.hasNext()) {
								String division = (String) divIt.next();
								Map entryMap = (Map) divisionMap.get(division);
								Iterator entryIt = entryMap.keySet().iterator();
								while (entryIt.hasNext()) {
									lineCounter++;
									Integer accountKeyId = (Integer) entryIt.next();
									AccountEntry entry = (AccountEntry) entryMap.get(accountKeyId);

									XMLElement line = new XMLElement(LINE_NUMBER);
									line.setAttribute(ID, Integer.toString(lineCounter));
									AccountKey accKey = ((AccountKeyHome) IDOLookup.getHome(AccountKey.class)).findByPrimaryKey(new Integer(
											entry.getAccountKeyId()));
									line.addContent(ITEM_CODE, accKey.getName());
									line.addContent(DIVISION, entry.getDivisionForAccounting());
									line.addContent(ITEM_DESCRIPTION, entry.getName());
									line.addContent(UNIT_PRICE, format.format(-entry.getTotal()));
									line.addContent(QUANTITY, "1");

									header.addContent(line);
								}
							}
						}
					}

					if (entryGroup.getFileName() == null || "".equals(entryGroup.getFileName())) {
						entryGroup.setFileName(entryGroup.getPrimaryKey().toString());
					}
					
					String fileName = entryGroup.getFileName() + ".xml";
					File file = new File(fileName);
					file.createNewFile();

					FileOutputStream out = new FileOutputStream(file);

					XMLOutput output = new XMLOutput("  ", true);
					output.setLineSeparator(System.getProperty("line.separator"));
					output.setTextNormalize(true);
					output.setEncoding("ISO-8859-1");

					output.output(doc, out);

					out.close();

					ICFile icfile = ((ICFileHome) IDOLookup.getHome(ICFile.class)).create();
					icfile.setFileValue(new FileInputStream(fileName.toString()));
					icfile.setName(fileName.toString());
					icfile.store();
					
					entryGroup.setFile(icfile);
					entryGroup.store();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public is.idega.idegaweb.campus.block.allocation.data.Contract findContractForUser(User user) {
		String statuses[] = { "S", "T", "E", "U" };

		is.idega.idegaweb.campus.block.allocation.data.Contract contract = null;

		contract = findContractForUserByStatus(user, "S", IWTimestamp.RightNow().getDate());
		if (contract != null) {
			return contract;
		}

		contract = findContractForUserByStatus(user, "U", IWTimestamp.RightNow().getDate());
		if (contract != null) {
			return contract;
		}

		contract = findContractForUserByStatus(user, "E", IWTimestamp.RightNow().getDate());
		if (contract != null) {
			return contract;
		}

		contract = findContractForUserByStatus(user, "T", IWTimestamp.RightNow().getDate());
		if (contract != null) {
			return contract;
		}

		IWTimestamp oneYearFromNow = IWTimestamp.RightNow();
		oneYearFromNow.addYears(1);
		
		contract = findContractForUserByStatus(user, "S", oneYearFromNow.getDate());
		if (contract != null) {
			return contract;
		}

		System.out.println("Didn't find any contracts for user " + user.getPersonalID());
		
		return contract;
	}

	private is.idega.idegaweb.campus.block.allocation.data.Contract findContractForUserByStatus(User user, String status, java.sql.Date rentedBefore) {
		is.idega.idegaweb.campus.block.allocation.data.Contract contract = null;
		Collection contracts = null;
		try {
			contracts = ((is.idega.idegaweb.campus.block.allocation.data.ContractHome) IDOLookup.getHome(is.idega.idegaweb.campus.block.allocation.data.Contract.class)).findByUserAndStatusAndRentedBeforeDate(
					(Integer) user.getPrimaryKey(), status, rentedBefore);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (EJBException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		if (contracts != null && !contracts.isEmpty()) {
			Iterator contractIt = contracts.iterator();
			while (contractIt.hasNext()) {
				contract = (is.idega.idegaweb.campus.block.allocation.data.Contract) contractIt.next();
			}
		}

		return contract;
	}


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

		String division = "";
		if (externalID != null && externalID.intValue() > 0) {
			
		}
		else if (accountID != null && accountID.intValue() > 0) {
			try {
				FinanceAccount account = (FinanceAccount)getFinanceService().getAccountHome().findByPrimaryKey(accountID);
				Contract contract = this.findContractForUser(account.getUser());
				division = contract.getApartment().getFloor().getBuilding().getDivision();
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}

		AccountEntry entry =  super.createAccountEntry(accountID, accountKeyID, cashierID, roundID, netto, VAT, total, paydate, Name,
				Info, status, externalID, division);
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
		Connection conn = null;
		Statement stmt = null;
		try {
			t.begin();
			conn = ConnectionBroker.getConnection();
			stmt = conn.createStatement();
			AssessmentRound AR = ((AssessmentRoundHome) IDOLookup.getHome(AssessmentRound.class))
					.findByPrimaryKey(assessmentRoundId);
			stmt.execute(sql3.toString());
			stmt.execute(sql2.toString());
			stmt.execute(sql.toString());
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
		finally {
            if (stmt != null) {
                try {
					stmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
            }
            if (conn != null) {
                ConnectionBroker.freeConnection(conn);
            }
		}
		return false;
	}
	
	public FinanceService getFinanceService() throws RemoteException {
		return (FinanceService) getServiceInstance(FinanceService.class);
	}
}