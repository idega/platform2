/**
 * 
 */
package se.idega.idegaweb.commune.childcare.business;




import com.idega.business.IBOHomeImpl;

/**
 * @author Dainis
 *
 */
public class AfterSchoolBusinessHomeImpl extends IBOHomeImpl implements
		AfterSchoolBusinessHome {
	protected Class getBeanInterfaceClass() {
		return AfterSchoolBusiness.class;
	}

	public AfterSchoolBusiness create() throws javax.ejb.CreateException {
		return (AfterSchoolBusiness) super.createIBO();
	}

}
