/**
 * 
 */
package se.idega.idegaweb.commune.childcare.business;

import com.idega.business.IBOHomeImpl;


/**
 * <p>
 * TODO Dainis Describe Type ChildCareBusinessHomeImpl
 * </p>
 *  Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: Dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.1 $
 */
public class ChildCareBusinessHomeImpl extends IBOHomeImpl implements ChildCareBusinessHome {

	protected Class getBeanInterfaceClass() {
		return ChildCareBusiness.class;
	}

	public ChildCareBusiness create() throws javax.ejb.CreateException {
		return (ChildCareBusiness) super.createIBO();
	}
}
