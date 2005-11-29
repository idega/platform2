/**
 * 
 */
package is.idega.idegaweb.campus.business;



import com.idega.business.IBOHomeImpl;

/**
 * @author bluebottle
 *
 */
public class CampusServiceHomeImpl extends IBOHomeImpl implements
		CampusServiceHome {
	protected Class getBeanInterfaceClass() {
		return CampusService.class;
	}

	public CampusService create() throws javax.ejb.CreateException {
		return (CampusService) super.createIBO();
	}

}
