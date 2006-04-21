/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.business;




import com.idega.business.IBOHome;

/**
 * @author bluebottle
 *
 */
public interface AccountingBusinessHome extends IBOHome {
	public AccountingBusiness create() throws javax.ejb.CreateException,
			java.rmi.RemoteException;

}
