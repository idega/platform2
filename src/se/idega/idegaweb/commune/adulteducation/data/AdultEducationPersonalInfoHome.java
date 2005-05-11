/*
 * Created on 2005-maj-11
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.adulteducation.data;

import com.idega.data.IDOHome;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface AdultEducationPersonalInfoHome extends IDOHome {
	public AdultEducationPersonalInfo create() throws javax.ejb.CreateException;

	public AdultEducationPersonalInfo findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationPersonalInfoBMPBean#ejbFindByUserId
	 */
	public AdultEducationPersonalInfo findByUserId(Integer userId)
			throws javax.ejb.FinderException;

}
