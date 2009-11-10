package com.idega.block.application.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ApplicantHome extends IDOHome {
	public Applicant create() throws CreateException;

	public Applicant findByPrimaryKey(Object pk) throws FinderException;

	public Collection findBySSN(String SSN) throws FinderException;

	public Collection findByApplicationStatusOrderedBy(String status,
			String order) throws FinderException;

	public Collection findBySQL(String sql) throws FinderException;
}