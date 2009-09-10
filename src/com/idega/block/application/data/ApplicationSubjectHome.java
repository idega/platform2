package com.idega.block.application.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface ApplicationSubjectHome extends IDOHome {
	public ApplicationSubject create() throws CreateException;

	public ApplicationSubject findByPrimaryKey(Object pk)
			throws FinderException;

	public Collection findAll() throws FinderException;

	public Collection findNonExpired() throws FinderException;
}