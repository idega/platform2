/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.raindance.business;




import com.idega.business.IBOHomeImpl;

/**
 * @author bluebottle
 *
 */
public class RaindanceBusinessHomeImpl extends IBOHomeImpl implements
		RaindanceBusinessHome {
	protected Class getBeanInterfaceClass() {
		return RaindanceBusiness.class;
	}

	public RaindanceBusiness create() throws javax.ejb.CreateException {
		return (RaindanceBusiness) super.createIBO();
	}

}
