/*
 * $Id: ScorecardHome.java,v 1.7 2005/02/07 11:20:28 laddi Exp $
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
import com.idega.data.IDOHome;


/**
 * Last modified: $Date: 2005/02/07 11:20:28 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.7 $
 */
public interface ScorecardHome extends IDOHome {

	public Scorecard create() throws javax.ejb.CreateException;

	public Scorecard findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	public Scorecard findByPrimaryKey(int id) throws javax.ejb.FinderException;

	public Scorecard findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

	/**
	 * @see is.idega.idegaweb.golf.entity.ScorecardBMPBean#ejbHomeGetCountRoundsPlayedByMember
	 */
	public int getCountRoundsPlayedByMember(int member) throws IDOException;

	/**
	 * @see is.idega.idegaweb.golf.entity.ScorecardBMPBean#ejbHomeGetSumPointsByMember
	 */
	public int getSumPointsByMember(int member) throws IDOException;

	/**
	 * @see is.idega.idegaweb.golf.entity.ScorecardBMPBean#ejbHomeGetNumberOfRoundsAfterDateByMember
	 */
	public int getNumberOfRoundsAfterDateByMember(int member, Date scorecardDate) throws IDOException;

	/**
	 * @see is.idega.idegaweb.golf.entity.ScorecardBMPBean#ejbFindBestRoundAfterDateByMember
	 */
	public Scorecard findBestRoundAfterDateByMember(int member, Date scorecardDate) throws FinderException;

	/**
	 * @see is.idega.idegaweb.golf.entity.ScorecardBMPBean#ejbFindLastPlayedRoundByMember
	 */
	public Scorecard findLastPlayedRoundByMember(int member) throws FinderException;

	/**
	 * @see is.idega.idegaweb.golf.entity.ScorecardBMPBean#ejbFindAllByGolfer
	 */
	public Collection findAllByGolfer(int memberID, Date dateFrom, Date dateTo) throws FinderException;

}
