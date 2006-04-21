/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.business;




import com.idega.business.IBOHomeImpl;

/**
 * @author bluebottle
 *
 */
public class AccountingBusinessHomeImpl extends IBOHomeImpl implements
		AccountingBusinessHome {
	protected Class getBeanInterfaceClass() {
		return AccountingBusiness.class;
	}

	public AccountingBusiness create() throws javax.ejb.CreateException {
		return (AccountingBusiness) super.createIBO();
	}

}
