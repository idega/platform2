package is.idega.idegaweb.campus.block.allocation.data;


import com.idega.data.IDOEntity;
import com.idega.user.data.User;
import java.sql.Timestamp;
import com.idega.block.finance.data.AccountKey;

public interface ContractTariff extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#getContract
	 */
	public Contract getContract();

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#getPrice
	 */
	public float getPrice();

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#getAccountKey
	 */
	public AccountKey getAccountKey();

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#getUseIndex
	 */
	public boolean getUseIndex();

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#getIndexType
	 */
	public String getIndexType();

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#getIndexUpdated
	 */
	public Timestamp getIndexUpdated();

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#getIsDeleted
	 */
	public boolean getIsDeleted();

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#getDeletedBy
	 */
	public User getDeletedBy();

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#getContractTariffName
	 */
	public ContractTariffName getContractTariffName();

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#getContractTariffNameCopy
	 */
	public ContractTariffName getContractTariffNameCopy();

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#setContract
	 */
	public void setContract(Contract contract);

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#setPrice
	 */
	public void setPrice(float price);

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#setAccountKey
	 */
	public void setAccountKey(AccountKey key);

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#setAccountKey
	 */
	public void setAccountKey(int keyID);

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#setUseIndex
	 */
	public void setUseIndex(boolean useIndex);

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#setIndexType
	 */
	public void setIndexType(String indexType);

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#setIndexUpdated
	 */
	public void setIndexUpdated(Timestamp updated);

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#setIsDeleted
	 */
	public void setIsDeleted(boolean isDeleted);

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#setDeletedBy
	 */
	public void setDeletedBy(User user);

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#setContractTariffName
	 */
	public void setContractTariffName(ContractTariffName name);

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ContractTariffBMPBean#setContractTariffNameCopy
	 */
	public void setContractTariffNameCopy(ContractTariffName name);
}