package is.idega.idegaweb.member.isi.block.accounting.data;


import com.idega.block.finance.data.AccountEntry;
import com.idega.user.data.Group;
import is.idega.idegaweb.member.isi.block.accounting.export.data.Batch;
import com.idega.block.basket.data.BasketItem;
import com.idega.data.IDOPrimaryKey;
import java.sql.Date;
import com.idega.user.data.User;
import java.sql.Timestamp;
import com.idega.data.IDOEntity;

public interface FinanceEntry extends IDOEntity, BasketItem {
	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setStatusCreated
	 */
	public void setStatusCreated();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setStatusSent
	 */
	public void setStatusSent();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setTypeAssessment
	 */
	public void setTypeAssessment();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setTypeManual
	 */
	public void setTypeManual();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setTypePayment
	 */
	public void setTypePayment();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getType
	 */
	public String getType();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setPaymentTypeID
	 */
	public void setPaymentTypeID(int payment_type_id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setPaymentType
	 */
	public void setPaymentType(PaymentType type);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getPaymentTypeID
	 */
	public int getPaymentTypeID();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getPaymentType
	 */
	public PaymentType getPaymentType();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getTypeLocalizationKey
	 */
	public String getTypeLocalizationKey();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getAmount
	 */
	public double getAmount();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setAmount
	 */
	public void setAmount(double amount);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getAssessmentRoundID
	 */
	public int getAssessmentRoundID();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setAssessmentRoundID
	 */
	public void setAssessmentRoundID(int id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getAssessmentRound
	 */
	public AssessmentRound getAssessmentRound();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setAssessment
	 */
	public void setAssessment(AssessmentRound assRound);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getClubID
	 */
	public int getClubID();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setClubID
	 */
	public void setClubID(int id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getClub
	 */
	public Group getClub();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setClub
	 */
	public void setClub(Group club);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getDivisionID
	 */
	public int getDivisionID();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setDivisionID
	 */
	public void setDivisionID(int id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getDivision
	 */
	public Group getDivision();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setDivision
	 */
	public void setDivision(Group club);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getDateOfEntry
	 */
	public Timestamp getDateOfEntry();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setDateOfEntry
	 */
	public void setDateOfEntry(Timestamp date);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getGroupID
	 */
	public int getGroupID();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setGroupID
	 */
	public void setGroupID(int id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getGroup
	 */
	public Group getGroup();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setGroup
	 */
	public void setGroup(Group group);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getUserID
	 */
	public int getUserID();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setUserID
	 */
	public void setUserID(int id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setUser
	 */
	public void setUser(User user);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getInfo
	 */
	public String getInfo();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setInfo
	 */
	public void setInfo(String info);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getTariffID
	 */
	public int getTariffID();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setTariffID
	 */
	public void setTariffID(int id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getTariff
	 */
	public ClubTariff getTariff();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setTariff
	 */
	public void setTariff(ClubTariff clubTariff);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getTariffTypeID
	 */
	public int getTariffTypeID();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setTariffTypeID
	 */
	public void setTariffTypeID(int id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getTariffType
	 */
	public ClubTariffType getTariffType();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setTariffType
	 */
	public void setTariffType(ClubTariffType clubTariffType);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getInsertedByUserID
	 */
	public int getInsertedByUserID();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setInsertedByUserID
	 */
	public void setInsertedByUserID(int id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getInsertedByUser
	 */
	public User getInsertedByUser();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setInsertedByUser
	 */
	public void setInsertedByUser(User user);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getAmountEqualized
	 */
	public double getAmountEqualized();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setAmountEqualized
	 */
	public void setAmountEqualized(double amount);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getEntryOpen
	 */
	public boolean getEntryOpen();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setEntryOpen
	 */
	public void setEntryOpen(boolean open);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setDiscountPerc
	 */
	public void setDiscountPerc(double perc);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getDiscountPerc
	 */
	public double getDiscountPerc();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setDiscountAmount
	 */
	public void setDiscountAmount(double amount);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getDiscountAmount
	 */
	public double getDiscountAmount();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setDiscountInfo
	 */
	public void setDiscountInfo(String info);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getDiscountInfo
	 */
	public String getDiscountInfo();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getPaymentDate
	 */
	public Timestamp getPaymentDate();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setPaymentDate
	 */
	public void setPaymentDate(Timestamp date);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getSent
	 */
	public boolean getSent();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setSent
	 */
	public void setSent(boolean sent);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setSent
	 */
	public void setSent(Boolean sent);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setPaymentContract
	 */
	public void setPaymentContract(PaymentContract contract);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setPaymentContractId
	 */
	public void setPaymentContractId(int id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getPaymentContract
	 */
	public PaymentContract getPaymentContract();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getPaymentContractId
	 */
	public int getPaymentContractId();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setISIBatchID
	 */
	public void setISIBatchID(int id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setISIBatch
	 */
	public void setISIBatch(Batch batch);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getISIBatchID
	 */
	public int getISIBatchID();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getISIBatch
	 */
	public Batch getISIBatch();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getPayedByUserID
	 */
	public int getPayedByUserID();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setPayedByUserID
	 */
	public void setPayedByUserID(int id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getPayedByUser
	 */
	public User getPayedByUser();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setPayedByUser
	 */
	public void setPayedByUser(User user);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getDueDate
	 */
	public Date getDueDate();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setDueDate
	 */
	public void setDueDate(Date dueDate);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getFinalDueDate
	 */
	public Date getFinalDueDate();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setFinalDueDate
	 */
	public void setFinalDueDate(Date finalDueDate);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getPayedBySSN
	 */
	public String getPayedBySSN();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setPayedBySSN
	 */
	public void setPayedBySSN(String ssn);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getAccountEntryID
	 */
	public int getAccountEntryID();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getAccountEntry
	 */
	public AccountEntry getAccountEntry();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setAccountEntryID
	 */
	public void setAccountEntryID(int accountEntryID);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setAccountEntry
	 */
	public void setAccountEntry(AccountEntry entry);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getInvoiceReceiver
	 */
	public InvoiceReceiver getInvoiceReceiver();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#setInvoiceReceiver
	 */
	public void setInvoiceReceiver(InvoiceReceiver receiver);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getItemDescription
	 */
	public String getItemDescription();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getItemID
	 */
	public IDOPrimaryKey getItemID();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getItemPrice
	 */
	public Double getItemPrice();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#getItemName
	 */
	public String getItemName();
}