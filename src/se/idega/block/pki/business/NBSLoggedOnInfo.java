/*
 * Created on 2.6.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.block.pki.business;

import se.nexus.nbs.sdk.NBSAuthResult;

import com.idega.core.accesscontrol.business.LoggedOnInfo;

/**
 * @author gummi
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class NBSLoggedOnInfo extends LoggedOnInfo {
	private NBSAuthResult _nbsAuthResult = null;
	
	
	public final static String KEY_PERSONAL_ID = "serialNumber";
	private final static String _nameKey = "CN";
	private final static String _organizationKey = "O";
	private final static String _countryKey = "C";
	private final static String givenNameKey = "givenName";
	private final static String SurNameKey = "SN";
	
	public String getNBSPersonalID(){
		return _nbsAuthResult.getSubjectAttributeValue(KEY_PERSONAL_ID);
	}
	
	public String getNBSCommonName(){
		return _nbsAuthResult.getSubjectAttributeValue(_nameKey);
	}
	
	public String getNBSCountry(){
		return _nbsAuthResult.getSubjectAttributeValue(_organizationKey);
	}
	
	public String getNBSOrganization(){
		return _nbsAuthResult.getSubjectAttributeValue(_countryKey);
	}
	
	public String getGivenName(){
		return _nbsAuthResult.getSubjectAttributeValue(givenNameKey);
	}
	
	public String getSurName(){
		return _nbsAuthResult.getSubjectAttributeValue(SurNameKey);
	}
	
	
	
	/**
	 * @return
	 */
	public NBSAuthResult getNbsAuthResult() {
		return _nbsAuthResult;
	}

	/**
	 * @param result
	 */
	public void setNbsAuthResult(NBSAuthResult result) {
		_nbsAuthResult = result;
	}

}
