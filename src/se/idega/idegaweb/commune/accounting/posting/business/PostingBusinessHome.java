/**
 * 
 */
package se.idega.idegaweb.commune.accounting.posting.business;




import com.idega.business.IBOHome;

/**
 * @author bluebottle
 *
 */
public interface PostingBusinessHome extends IBOHome {
	public PostingBusiness create() throws javax.ejb.CreateException,
			java.rmi.RemoteException;

}
