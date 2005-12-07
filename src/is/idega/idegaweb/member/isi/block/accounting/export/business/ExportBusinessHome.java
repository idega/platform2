/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.export.business;




import com.idega.business.IBOHome;

/**
 * @author bluebottle
 *
 */
public interface ExportBusinessHome extends IBOHome {
	public ExportBusiness create() throws javax.ejb.CreateException,
			java.rmi.RemoteException;

}
