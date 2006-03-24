/**
 * 
 */
package se.idega.idegaweb.commune.childcare.business;

import com.idega.business.IBOHomeImpl;


/**
 * <p>
 * TODO Dainis Describe Type AfterSchoolBusinessHomeImpl
 * </p>
 *  Last modified: $Date: 2006/03/24 13:19:43 $ by $Author: dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.3.2.6 $
 */
public class AfterSchoolBusinessHomeImpl extends IBOHomeImpl implements AfterSchoolBusinessHome {

	protected Class getBeanInterfaceClass() {
		return AfterSchoolBusiness.class;
	}

	public AfterSchoolBusiness create() throws javax.ejb.CreateException {
		return (AfterSchoolBusiness) super.createIBO();
	}
}
