/**
 * 
 */
package com.idega.block.school.business;

import com.idega.business.IBOHome;


/**
 * <p>
 * TODO Dainis Describe Type SchoolUserBusinessHome
 * </p>
 *  Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: Dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.1 $
 */
public interface SchoolUserBusinessHome extends IBOHome {

	public SchoolUserBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
