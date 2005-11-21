/**
 * 
 */
package com.idega.block.finance.business;



import com.idega.business.IBOHomeImpl;

/**
 * @author bluebottle
 *
 */
public class AssessmentBusinessHomeImpl extends IBOHomeImpl implements
		AssessmentBusinessHome {
	protected Class getBeanInterfaceClass() {
		return AssessmentBusiness.class;
	}

	public AssessmentBusiness create() throws javax.ejb.CreateException {
		return (AssessmentBusiness) super.createIBO();
	}

}
