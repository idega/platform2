/**
 * 
 */
package com.idega.block.finance.business;



import com.idega.business.IBOHome;

/**
 * @author bluebottle
 *
 */
public interface AssessmentBusinessHome extends IBOHome {
	public AssessmentBusiness create() throws javax.ejb.CreateException,
			java.rmi.RemoteException;

}
