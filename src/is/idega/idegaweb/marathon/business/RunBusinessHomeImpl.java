/*
 * Created on 21.8.2004
 */
package is.idega.idegaweb.marathon.business;




import com.idega.business.IBOHomeImpl;


/**
 * @author laddi
 */
public class RunBusinessHomeImpl extends IBOHomeImpl implements RunBusinessHome {

	protected Class getBeanInterfaceClass() {
		return RunBusiness.class;
	}

	public RunBusiness create() throws javax.ejb.CreateException {
		return (RunBusiness) super.createIBO();
	}

}
