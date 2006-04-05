/**
 * 
 */
package se.idega.idegaweb.commune.adulteducation.business;

import com.idega.business.IBOHomeImpl;


/**
 * <p>
 * TODO Dainis Describe Type AdultEducationBusinessHomeImpl
 * </p>
 *  Last modified: $Date: 2006/04/05 15:20:35 $ by $Author: dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.30.2.4 $
 */
public class AdultEducationBusinessHomeImpl extends IBOHomeImpl implements AdultEducationBusinessHome {

	protected Class getBeanInterfaceClass() {
		return AdultEducationBusiness.class;
	}

	public AdultEducationBusiness create() throws javax.ejb.CreateException {
		return (AdultEducationBusiness) super.createIBO();
	}
}
