/**
 * 
 */
package com.idega.block.application.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface ApplicationHome extends IDOHome {
	public Application create() throws javax.ejb.CreateException;

	public Application findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#ejbFindByApplicantID
	 */
	public Collection findByApplicantID(Integer ID) throws FinderException;

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#ejbFindByApplicantAndStatus
	 */
	public Collection findByApplicantAndStatus(Integer ID, String status)
			throws FinderException;

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#ejbFindBySubjectAndStatus
	 */
	public Collection findBySubjectAndStatus(Integer subjectID, String status)
			throws FinderException;

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#ejbFindByStatus
	 */
	public Collection findByStatus(String status) throws FinderException;

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#ejbFindBySubject
	 */
	public Collection findBySubject(Integer subjectID) throws FinderException;

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#ejbFindBySQL
	 */
	public Collection findBySQL(String sql) throws FinderException;

}
