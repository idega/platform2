/*
 * Created on Dec 26, 2003
 *
 */
package is.idega.idegaweb.campus.block.application.data;

import com.idega.block.process.data.AbstractCaseBMPBean;

/**
 * ApartmentApplication
 * @author aron 
 * @version 1.0
 */
public class ApartmentApplication extends AbstractCaseBMPBean {
	
	final static public String APARTMENT_APPLICATION = "CAM_APRT_APPL";
	public final static String CASECODE = "APRTAPPL";
	/* (non-Javadoc)
	 * @see com.idega.block.process.data.AbstractCaseBMPBean#getCaseCodeKey()
	 */
	public String getCaseCodeKey() {
		return CASECODE;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.process.data.AbstractCaseBMPBean#getCaseCodeDescription()
	 */
	public String getCaseCodeDescription() {
		return "Apartment application";
	}
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return APARTMENT_APPLICATION;
	}
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		// TODO Auto-generated method stub
	}
}
