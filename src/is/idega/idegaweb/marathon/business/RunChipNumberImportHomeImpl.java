/*
 * Created on 19.8.2004
 */
package is.idega.idegaweb.marathon.business;

import com.idega.business.IBOHomeImpl;


/**
 * @author laddi
 */
public class RunChipNumberImportHomeImpl extends IBOHomeImpl implements RunChipNumberImportHome {

	protected Class getBeanInterfaceClass() {
		return RunChipNumberImport.class;
	}

	public RunChipNumberImport create() throws javax.ejb.CreateException {
		return (RunChipNumberImport) super.createIBO();
	}

}
