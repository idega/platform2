/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.data;


import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 */
public class FinanceEntryHomeImpl extends IDOFactory implements
        FinanceEntryHome {
    protected Class getEntityInterfaceClass() {
        return FinanceEntry.class;
    }

    public FinanceEntry create() throws javax.ejb.CreateException {
        return (FinanceEntry) super.createIDO();
    }

    public FinanceEntry findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException {
        return (FinanceEntry) super.findByPrimaryKeyIDO(pk);
    }

    public Collection findAllByAssessmentRound(AssessmentRound round)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((FinanceEntryBMPBean) entity)
                .ejbFindAllByAssessmentRound(round);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllByUser(User user) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((FinanceEntryBMPBean) entity)
                .ejbFindAllByUser(user);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllByUser(Group club, Group division, User user)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((FinanceEntryBMPBean) entity)
                .ejbFindAllByUser(club, division, user);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllOpenAssessmentByUser(Group club, Group division,
            User user) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((FinanceEntryBMPBean) entity)
                .ejbFindAllOpenAssessmentByUser(club, division, user);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllAssessmentByUser(Group club, Group division,
            User user) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((FinanceEntryBMPBean) entity)
                .ejbFindAllAssessmentByUser(club, division, user);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllAssessmentByUser(Group club, Group division,
            User user, IWTimestamp entriesAfter) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((FinanceEntryBMPBean) entity)
                .ejbFindAllAssessmentByUser(club, division, user, entriesAfter);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllPaymentsByUser(Group club, Group division,
            User user) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((FinanceEntryBMPBean) entity)
                .ejbFindAllPaymentsByUser(club, division, user);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllFinanceEntriesByDateIntervalDivisionsAndGroupsOrderedByDivisionGroupAndDate(
            Group club, String[] types, Date dateFrom, Date dateTo,
            Collection divisions, Collection groups, String personalID)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((FinanceEntryBMPBean) entity)
                .ejbFindAllFinanceEntriesByDateIntervalDivisionsAndGroupsOrderedByDivisionGroupAndDate(
                        club, types, dateFrom, dateTo, divisions, groups,
                        personalID);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllFinanceEntriesByPaymentDateDivisionsAndGroupsOrderedByDivisionGroupAndDate(
            Group club, String[] types, Collection divisions,
            Collection groups, String personalID) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((FinanceEntryBMPBean) entity)
                .ejbFindAllFinanceEntriesByPaymentDateDivisionsAndGroupsOrderedByDivisionGroupAndDate(
                        club, types, divisions, groups, personalID);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllByGroupAndPaymentTypeNotInBatch(Group group,
            PaymentType type, IWTimestamp dateFrom, IWTimestamp dateTo)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((FinanceEntryBMPBean) entity)
                .ejbFindAllByGroupAndPaymentTypeNotInBatch(group, type,
                        dateFrom, dateTo);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

}
