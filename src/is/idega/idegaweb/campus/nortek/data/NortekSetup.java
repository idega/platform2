package is.idega.idegaweb.campus.nortek.data;


import com.idega.block.category.data.ICCategory;
import com.idega.block.finance.data.TariffGroup;
import com.idega.block.finance.data.AccountKey;
import com.idega.data.IDOEntity;

public interface NortekSetup extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.campus.nortek.data.NortekSetupBMPBean#getFinanceCategory
	 */
	public ICCategory getFinanceCategory();

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.NortekSetupBMPBean#getTariffGroup
	 */
	public TariffGroup getTariffGroup();

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.NortekSetupBMPBean#getAccountKey
	 */
	public AccountKey getAccountKey();

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.NortekSetupBMPBean#setFinanceCategory
	 */
	public void setFinanceCategory(ICCategory entity);

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.NortekSetupBMPBean#setTariffGroup
	 */
	public void setTariffGroup(TariffGroup group);

	/**
	 * @see is.idega.idegaweb.campus.nortek.data.NortekSetupBMPBean#setAccountKey
	 */
	public void setAccountKey(AccountKey key);
}