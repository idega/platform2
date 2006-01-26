/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.economa.business;




import com.idega.business.IBOHomeImpl;

/**
 * @author bluebottle
 *
 */
public class EconomaBusinessHomeImpl extends IBOHomeImpl implements
		EconomaBusinessHome {
	protected Class getBeanInterfaceClass() {
		return EconomaBusiness.class;
	}

	public EconomaBusiness create() throws javax.ejb.CreateException {
		return (EconomaBusiness) super.createIBO();
	}

}
