/*
 * $Id: FinanceEntryHome.java,v 1.20 2005/05/31 10:00:56 palli Exp $
 * Created on May 19, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.accounting.data;


import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


/**
 * 
 *  Last modified: $Date: 2005/05/31 10:00:56 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.20 $
 */
public interface FinanceEntryHome extends IDOHome {

	public FinanceEntry create() throws javax.ejb.CreateException;

	public FinanceEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllByAssessmentRound
	 */
	public Collection findAllByAssessmentRound(AssessmentRound round) throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllByUser
	 */
	public Collection findAllByUser(User user) throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllByUser
	 */
	public Collection findAllByUser(Group club, Group division, User user) throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllOpenAssessmentByUser
	 */
	public Collection findAllOpenAssessmentByUser(Group club, Group division, User user) throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllAssessmentByUser
	 */
	public Collection findAllAssessmentByUser(Group club, Group division, User user) throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllAssessmentByUser
	 */
	public Collection findAllAssessmentByUser(Group club, Group division, User user, IWTimestamp entriesAfter)
			throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllPaymentsByUser
	 */
	public Collection findAllPaymentsByUser(Group club, Group division, User user) throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllFinanceEntriesByDateIntervalDivisionsAndGroupsOrderedByDivisionGroupAndDate
	 */
	public Collection findAllFinanceEntriesByDateIntervalDivisionsAndGroupsOrderedByDivisionGroupAndDate(Group club,
			String[] types, Date dateFrom, Date dateTo, Collection divisions, Collection groups, String personalID)
			throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllFinanceEntriesByPaymentDateDivisionsAndGroupsOrderedByDivisionGroupAndDate
	 */
	public Collection findAllFinanceEntriesByPaymentDateDivisionsAndGroupsOrderedByDivisionGroupAndDate(Group club,
			String[] types, Collection divisions, Collection groups, String personalID) throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean#ejbFindAllByGroupAndPaymentTypeNotInBatch
	 */
	public Collection findAllByGroupAndPaymentTypeNotInBatch(Group group, PaymentType type, IWTimestamp dateFrom,
			IWTimestamp dateTo) throws FinderException;

}
