/*
 * $Id: CitizenAccountHomeImpl.java,v 1.5 2005/04/06 08:26:18 anna Exp $
 * Created on 6.4.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.account.citizen.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.process.data.CaseStatus;
import com.idega.data.IDOException;
import com.idega.data.IDOFactory;


/**
 * <p>
 * TODO anna Describe Type CitizenAccountHomeImpl
 * </p>
 *  Last modified: $Date: 2005/04/06 08:26:18 $ by $Author: anna $
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.5 $
 */
public class CitizenAccountHomeImpl extends IDOFactory implements CitizenAccountHome {

	protected Class getEntityInterfaceClass() {
		return CitizenAccount.class;
	}

	public CitizenAccount create() throws javax.ejb.CreateException {
		return (CitizenAccount) super.createIDO();
	}

	public CitizenAccount findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (CitizenAccount) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllCasesByStatus(CaseStatus caseStatus) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CitizenAccountBMPBean) entity).ejbFindAllCasesByStatus(caseStatus);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllCasesByStatus(String caseStatus) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CitizenAccountBMPBean) entity).ejbFindAllCasesByStatus(caseStatus);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getTotalCount() throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CitizenAccountBMPBean) entity).ejbHomeGetTotalCount();
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getCount(String personalID, String status) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CitizenAccountBMPBean) entity).ejbHomeGetCount(personalID, status);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}
}
