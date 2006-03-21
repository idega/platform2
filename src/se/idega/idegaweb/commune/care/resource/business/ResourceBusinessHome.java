/**
 * 
 */
package se.idega.idegaweb.commune.care.resource.business;

import com.idega.business.IBOHome;


/**
 * <p>
 * TODO Maris_O Describe Type ResourceBusinessHome
 * </p>
 *  Last modified: $Date: 2006/03/21 08:58:42 $ by $Author: mariso $
 * 
 * @author <a href="mailto:Maris_O@idega.com">Maris_O</a>
 * @version $Revision: 1.1.2.1 $
 */
public interface ResourceBusinessHome extends IBOHome
{

    public ResourceBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
