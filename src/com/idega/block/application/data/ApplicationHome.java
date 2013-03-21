package com.idega.block.application.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ApplicationHome extends IDOHome {
	public Application create() throws CreateException;

	public Application findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;

	public Collection findByApplicantID(Integer ID) throws FinderException;

	public Collection findByApplicantAndStatus(Integer ID, String status)
			throws FinderException;

	public Collection findBySubjectAndStatus(Integer subjectID, String status)
			throws FinderException;

	public Collection findByStatus(String status) throws FinderException;

	public Collection findBySubject(Integer subjectID) throws FinderException;

	public Collection findBySQL(String sql) throws FinderException;
}