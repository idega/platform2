/*
 * $Id: ScorecardHomeImpl.java,v 1.7 2005/02/07 11:20:28 laddi Exp $
 * Created on 7.2.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.entity;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.data.IDOFactory;


/**
 * Last modified: $Date: 2005/02/07 11:20:28 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.7 $
 */
public class ScorecardHomeImpl extends IDOFactory implements ScorecardHome {

	protected Class getEntityInterfaceClass() {
		return Scorecard.class;
	}

	public Scorecard create() throws javax.ejb.CreateException {
		return (Scorecard) super.createIDO();
	}

	public Scorecard findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (Scorecard) super.findByPrimaryKeyIDO(pk);
	}

	public Scorecard createLegacy() {
		try {
			return create();
		}
		catch (javax.ejb.CreateException ce) {
			throw new RuntimeException("CreateException:" + ce.getMessage());
		}
	}

	public Scorecard findByPrimaryKey(int id) throws javax.ejb.FinderException {
		return (Scorecard) super.findByPrimaryKeyIDO(id);
	}

	public Scorecard findByPrimaryKeyLegacy(int id) throws java.sql.SQLException {
		try {
			return findByPrimaryKey(id);
		}
		catch (javax.ejb.FinderException fe) {
			throw new java.sql.SQLException("FinderException:" + fe.getMessage());
		}
	}

	public int getCountRoundsPlayedByMember(int member) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((ScorecardBMPBean) entity).ejbHomeGetCountRoundsPlayedByMember(member);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getSumPointsByMember(int member) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((ScorecardBMPBean) entity).ejbHomeGetSumPointsByMember(member);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getNumberOfRoundsAfterDateByMember(int member, Date scorecardDate) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((ScorecardBMPBean) entity).ejbHomeGetNumberOfRoundsAfterDateByMember(member, scorecardDate);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Scorecard findBestRoundAfterDateByMember(int member, Date scorecardDate) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ScorecardBMPBean) entity).ejbFindBestRoundAfterDateByMember(member, scorecardDate);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Scorecard findLastPlayedRoundByMember(int member) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ScorecardBMPBean) entity).ejbFindLastPlayedRoundByMember(member);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllByGolfer(int memberID, Date dateFrom, Date dateTo) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ScorecardBMPBean) entity).ejbFindAllByGolfer(memberID, dateFrom, dateTo);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
