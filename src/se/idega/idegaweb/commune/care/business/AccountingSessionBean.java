/*
 * Created on 9.9.2003
 */
package se.idega.idegaweb.commune.care.business;

import com.idega.business.IBOSessionBean;

/**
 * @author laddi
 */
public class AccountingSessionBean extends IBOSessionBean implements AccountingSession {

	protected static final String PARAMETER_OPERATIONAL_FIELD = "acc_operational_field";

	protected String _operationalField = null;

	/**
	 * Returns the parameter for the operational field.
	 * @return String
	 */
	public String getParameterOperationalField() {
		return PARAMETER_OPERATIONAL_FIELD;
	}

	/**
	 * Returns the operational field stored in session.
	 * @return String
	 */
	public String getOperationalField() {
		return _operationalField;
	}

	/**
	 * Sets the operational field stored in session.
	 * @param operationalField
	 */
	public void setOperationalField(String operationalField) {
		_operationalField = operationalField;
	}
}