/**
 * 
 */
package is.idega.idegaweb.campus.block.finance.business;




import com.idega.business.IBOHome;

/**
 * @author bluebottle
 *
 */
public interface CampusAssessmentBusinessHome extends IBOHome {
	public CampusAssessmentBusiness create() throws javax.ejb.CreateException,
			java.rmi.RemoteException;

}
