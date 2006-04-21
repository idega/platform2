/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.data;



import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface CreditCardType extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardTypeBMPBean#setCreditCardType
	 */
	public void setCreditCardType(String type);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardTypeBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardTypeBMPBean#setLocalizedKey
	 */
	public void setLocalizedKey(String key);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardTypeBMPBean#getCreditCardType
	 */
	public String getCreditCardType();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardTypeBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardTypeBMPBean#getLocalizedKey
	 */
	public String getLocalizedKey();

}
