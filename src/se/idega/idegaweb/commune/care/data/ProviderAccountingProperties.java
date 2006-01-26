/**
 * 
 */
package se.idega.idegaweb.commune.care.data;



import com.idega.block.school.data.School;
import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface ProviderAccountingProperties extends IDOEntity {
	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#getSchool
	 */
	public School getSchool();

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#getSchoolId
	 */
	public int getSchoolId();

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#getProviderType
	 */
	public ProviderType getProviderType();

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#getProviderTypeId
	 */
	public int getProviderTypeId();

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#getStatisticsType
	 */
	public String getStatisticsType();

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#getPaymentByInvoice
	 */
	public boolean getPaymentByInvoice();

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#getStateSubsidyGrant
	 */
	public boolean getStateSubsidyGrant();

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#getPostgiro
	 */
	public String getPostgiro();

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#getBankgiro
	 */
	public String getBankgiro();

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#getOwnPosting
	 */
	public String getOwnPosting();

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#getDoublePosting
	 */
	public String getDoublePosting();

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#getGiroText
	 */
	public String getGiroText();

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#getCreateInvoiceRecord
	 */
	public boolean getCreateInvoiceRecord();

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#setSchoolId
	 */
	public void setSchoolId(int id);

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#setProviderTypeId
	 */
	public void setProviderTypeId(int id);

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#setStatisticsType
	 */
	public void setStatisticsType(String type);

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#setPaymentByInvoice
	 */
	public void setPaymentByInvoice(boolean b);

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#setStateSubsidyGrant
	 */
	public void setStateSubsidyGrant(boolean b);

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#setPostgiro
	 */
	public void setPostgiro(String postgiro);

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#setBankgiro
	 */
	public void setBankgiro(String bankgiro);

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#setOwnPosting
	 */
	public void setOwnPosting(String s);

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#setDoublePosting
	 */
	public void setDoublePosting(String s);

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#setGiroText
	 */
	public void setGiroText(String text);

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#setCreateInvoiceRecord
	 */
	public void setCreateInvoiceRecord(boolean createRecord);

}
