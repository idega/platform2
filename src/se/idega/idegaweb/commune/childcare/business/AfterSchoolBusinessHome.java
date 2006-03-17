/**
 * 
 */
package se.idega.idegaweb.commune.childcare.business;

import com.idega.business.IBOHome;


/**
 * <p>
 * TODO is Describe Type AfterSchoolBusinessHome
 * </p>
 *  Last modified: $Date: 2006/03/17 16:43:03 $ by $Author: igors $
 * 
 * @author <a href="mailto:is@idega.com">is</a>
 * @version $Revision: 1.3.2.5 $
 */
public interface AfterSchoolBusinessHome extends IBOHome {

	public AfterSchoolBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
