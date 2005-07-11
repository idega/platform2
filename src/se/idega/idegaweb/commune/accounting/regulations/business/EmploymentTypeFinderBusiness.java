/*
 * Created on Jul 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.idegaweb.commune.accounting.regulations.business;

import java.util.Collection;
import com.idega.business.IBOService;


/**
 * <p>
 * TODO thomas Describe Type EmploymentTypeFinderBusiness
 * </p>
 *  Last modified: $Date: 2005/07/11 16:42:25 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public interface EmploymentTypeFinderBusiness extends IBOService {

	public Collection findAllEmploymentTypes() throws java.rmi.RemoteException;

}
