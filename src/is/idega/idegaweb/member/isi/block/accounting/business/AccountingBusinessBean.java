/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 *  
 */
package is.idega.idegaweb.member.isi.block.accounting.business;

import is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRound;
import is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundHome;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariff;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffHome;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffTypeHome;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContract;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractHome;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardTypeHome;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryHome;
import is.idega.idegaweb.member.isi.block.accounting.data.PaymentType;
import is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeHome;
import is.idega.idegaweb.member.isi.block.accounting.data.UserCreditCard;
import is.idega.idegaweb.member.isi.block.accounting.data.UserCreditCardHome;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.idega.block.basket.business.BasketBusiness;
import com.idega.block.basket.data.BasketEntry;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWUserContext;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;

/**
 * A service bean for the accounting part of the isi member system.
 * 
 * @author palli
 */
public class AccountingBusinessBean extends IBOServiceBean implements
        AccountingBusiness {

    /**
     * A method to start the assessment batch. Starts up a thread that executes
     * the batch, creates a log entry and then exits.
     * 
     * @param name
     *            The name of the assessment batch.
     * @param club
     *            The group representing the club the batch is being executed
     *            for.
     * @param division
     *            The group representing the division the batch is being
     *            executed for. Can be null.
     * @param groupId
     *            The id of the top level group the batch is being executed for.
     * @param user
     *            The user executing the batch.
     * @param includeChildren
     *            If true then the batch is executed recursively for the
     *            children of the top level group.
     * @param tariffs
     *            A String array of the tariff types the batch is being executed
     *            for.
     * @param paymentDate
     *            The last payment date to be put in the FinanceEntry.
     * @param runOnDate
     *            The date the tariffs are being fetched on.
     */
    public boolean doAssessment(String name, Group club, Group division,
            String groupId, User user, boolean includeChildren,
            String tariffs[], Timestamp paymentDate, Timestamp runOnDate) {
        Group group = null;
        if (groupId != null) {
            try {
                GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class);
                group = gHome.findByPrimaryKey(new Integer(groupId));
            } catch (IDOLookupException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (FinderException e) {
                e.printStackTrace();
            }
        }

        IWTimestamp now = IWTimestamp.RightNow();
        AssessmentRound round = insertAssessmentRound(name, club, division,
                group, user, now.getTimestamp(), null, includeChildren,
                paymentDate, runOnDate);

        Thread assRoundThread = new AssessmentRoundThread(round,
                getIWApplicationContext(), Arrays.asList(tariffs));
        assRoundThread.start();

        return true;
    }

    public Collection findAllTariffByClub(Group club) {
        try {
            return getClubTariffHome().findAllByClub(club);
        } catch (FinderException e) {
        }

        return null;
    }

    public Collection findAllTariffByClubAndDivision(Group club, Group division) {
        try {
            return getClubTariffHome().findAllByClubAndDivision(club, division);
        } catch (FinderException e) {
        }

        return null;
    }

    public Collection findAllValidTariffByGroup(Group group) {
        try {
            return getClubTariffHome().findAllValidByGroup(group);
        } catch (FinderException e) {
        }

        return null;
    }

    public Collection findAllValidTariffByGroup(String groupId) {
        Group group = null;
        if (groupId != null) {
            try {
                GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class);
                group = gHome.findByPrimaryKey(new Integer(groupId));
            } catch (IDOLookupException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (FinderException e) {
                e.printStackTrace();
            }
        }

        if (group != null) { return findAllValidTariffByGroup(group); }

        return null;
    }

    public boolean insertTariff(Group club, Group division, String groupId,
            String typeId, String text, String amount, Date from, Date to,
            boolean applyToChildren, String skip) {
        Group group = null;
        if (groupId != null) {
            try {
                GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class);
                group = gHome.findByPrimaryKey(new Integer(groupId));
            } catch (IDOLookupException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (FinderException e) {
                e.printStackTrace();
            }
        }

        ClubTariffType type = null;
        if (typeId != null) {
            try {
                type = getClubTariffTypeHome().findByPrimaryKey(
                        new Integer(typeId));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (FinderException e) {
                e.printStackTrace();
            }
        }

        float am = 0;
        try {
            am = Float.parseFloat(amount);
        } catch (Exception e) {
        }

        return insertTariff(club, division, group, type, text, am, from, to,
                applyToChildren, skip, null);
    }

    public boolean insertTariff(Group club, Group division, Group group,
            ClubTariffType type, String text, float amount, Date from, Date to,
            boolean applyToChildren, String skipList, List skip) {
        if (skip == null || skip.isEmpty()) {
            skip = new ArrayList();

            StringTokenizer tok = new StringTokenizer(skipList, ";");
            while (tok.hasMoreElements()) {
                String str = (String) tok.nextElement();
                skip.add(str);
            }
        }

        ClubTariff eTariff;
        try {
            if (!skip.contains(group.getGroupType())) {
                if (division == null) {
                    division = findDivisionForGroup(group);
                }

                eTariff = getClubTariffHome().create();
                eTariff.setClub(club);
                eTariff.setDivision(division);
                eTariff.setGroup(group);
                eTariff.setTariffType(type);
                eTariff.setText(text);
                eTariff.setAmount(amount);
                eTariff.setPeriodFrom(from);
                eTariff.setPeriodTo(to);

                eTariff.store();
            }

            if (applyToChildren) {
                Iterator children = group.getChildren();
                if (children != null) {
                    while (children.hasNext()) {
                        Group child = (Group) children.next();
                        boolean ret = insertTariff(club, division, child, type,
                                text, amount, from, to, applyToChildren,
                                skipList, skip);
                        if (!ret) return ret;
                    }
                }
            }

            return true;
        } catch (CreateException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Group findDivisionForGroup(Group group) {
        if (group == null) { return null; }

        if (group.getGroupType().equals(
                IWMemberConstants.GROUP_TYPE_CLUB_DIVISION)) {
            return group;
        } else if (group.getGroupType().equals(
                IWMemberConstants.GROUP_TYPE_CLUB)) { return null; }

        List parents = group.getParentGroups();
        if (parents != null && !parents.isEmpty()) {
            Iterator it = parents.iterator();
            while (it.hasNext()) {
                Group parent = (Group) it.next();

                Group div = findDivisionForGroup(parent);
                if (div != null) { return div; }
            }
        }

        return null;
    }

    public Group findClubForGroup(Group group) {
        if (group == null) { return null; }

        if (group.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB)) { return group; }

        List parents = group.getParentGroups();
        if (parents != null && !parents.isEmpty()) {
            Iterator it = parents.iterator();
            while (it.hasNext()) {
                Group parent = (Group) it.next();

                Group div = findClubForGroup(parent);
                if (div != null) { return div; }
            }
        }

        return null;
    }

    public boolean deleteTariff(String ids[]) {
        try {
            for (int i = 0; i < ids.length; i++) {
                Integer id = new Integer(ids[i]);
                ClubTariff eTariff = getClubTariffHome().findByPrimaryKey(id);
                eTariff.setDeleted(true);
                eTariff.store();
            }

            return true;
        } catch (FinderException e) {
            e.printStackTrace();
        } catch (EJBException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Collection findAllTariffTypeByClub(Group club) {
        try {
            return getClubTariffTypeHome().findAllByClub(club);
        } catch (FinderException e) {
        }

        return null;
    }

    public boolean insertTariffType(String type, String name, String locKey,
            Group club) {
        try {
            ClubTariffType eType = getClubTariffTypeHome().create();
            eType.setTariffType(type);
            eType.setClub(club);
            eType.setName(name);
            eType.setLocalizedKey(locKey);

            eType.store();

            return true;
        } catch (CreateException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteTariffType(String ids[]) {
        try {
            for (int i = 0; i < ids.length; i++) {
                Integer id = new Integer(ids[i]);
                ClubTariffType eType = getClubTariffTypeHome()
                        .findByPrimaryKey(id);
                eType.setDeleted(true);
                eType.store();
            }

            return true;
        } catch (FinderException e) {
            e.printStackTrace();
        } catch (EJBException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Collection findAllCreditCardType() {
        try {
            return getCreditCardTypeHome().findAll();
        } catch (FinderException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Collection findAllCreditCardContractByClub(Group club) {
        try {
            return getCreditCardContractHome().findAllByClub(club);
        } catch (FinderException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insertCreditCardContract(Group club, String division,
            String group, String contractNumber, String type) {
        Group div = null;
        Group grp = null;
        if (division != null && !division.equals("-1")) {
            try {
                GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class);
                div = gHome.findByPrimaryKey(new Integer(division));
            } catch (IDOLookupException e) {
            } catch (NumberFormatException e) {
            } catch (FinderException e) {
            }
        }

        if (group != null && !group.equals("-1")) {
            try {
                GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class);
                grp = gHome.findByPrimaryKey(new Integer(group));
            } catch (IDOLookupException e) {
            } catch (NumberFormatException e) {
            } catch (FinderException e) {
            }
        }

        CreditCardType cType = null;
        if (type != null) {
            try {
                cType = getCreditCardTypeHome().findByPrimaryKey(
                        new Integer(type));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (FinderException e) {
                e.printStackTrace();
            }
        }

        return insertCreditCardContract(club, div, grp, contractNumber, cType);
    }

    public boolean insertCreditCardContract(Group club, Group division,
            Group group, String contractNumber, CreditCardType type) {
        try {
            CreditCardContract eCont = null;
            Collection contracts = getCreditCardContractHome()
                    .findAllByClubDivisionGroupAndType(club, division, group,
                            type);
            if (contracts != null && !contracts.isEmpty()) {
                Iterator it = contracts.iterator();
                eCont = (CreditCardContract) it.next();
            } else {
                eCont = getCreditCardContractHome().create();
            }

            eCont.setClub(club);
            eCont.setDivision(division);
            eCont.setGroup(group);
            eCont.setContractNumber(contractNumber);
            eCont.setCardType(type);

            eCont.store();

            return true;
        } catch (CreateException e) {
            e.printStackTrace();
        } catch (FinderException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteContract(String ids[]) {
        try {
            for (int i = 0; i < ids.length; i++) {
                Integer id = new Integer(ids[i]);
                CreditCardContract eCont = getCreditCardContractHome()
                        .findByPrimaryKey(id);
                eCont.setDeleted(true);
                eCont.store();
            }

            return true;
        } catch (FinderException e) {
            e.printStackTrace();
        } catch (EJBException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean insertCreditCard(Group club, String division, String type,
            String number, String expMonth, String expYear, User user) {
        Group div = null;
        if (division != null || !division.equals("-1")) {
            try {
                GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class);
                div = gHome.findByPrimaryKey(new Integer(division));
            } catch (IDOLookupException e) {
            } catch (NumberFormatException e) {
            } catch (FinderException e) {
            }
        }

        CreditCardType cType = null;
        if (type != null) {
            try {
                cType = getCreditCardTypeHome().findByPrimaryKey(
                        new Integer(type));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (FinderException e) {
                e.printStackTrace();
            }
        }

        return insertCreditCard(club, div, cType, number, expMonth, expYear,
                user);
    }

    public boolean insertCreditCard(Group club, Group division,
            CreditCardType type, String number, String expMonth,
            String expYear, User user) {
        try {
            UserCreditCard eCard = getUserCreditCardHome().create();
            eCard.setClub(club);
            eCard.setDivision(division);
            eCard.setCardType(type);
            eCard.setCardOwner(user);
            eCard.setCardNumber(number);
            eCard.setExpirationMonth(expMonth);
            eCard.setExpirationYear(expYear);

            eCard.store();

            return true;
        } catch (CreateException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteCreditCards(String ids[]) {
        try {
            for (int i = 0; i < ids.length; i++) {
                Integer id = new Integer(ids[i]);
                UserCreditCard eCard = getUserCreditCardHome()
                        .findByPrimaryKey(id);
                eCard.setDeleted(true);
                eCard.store();
            }

            return true;
        } catch (FinderException e) {
            e.printStackTrace();
        } catch (EJBException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Collection findAllAssessmentRoundByClubAndDivision(Group club,
            Group division) {
        try {
            return getAssessmentRoundHome().findAllByClubAndDivision(club,
                    division);
        } catch (FinderException e) {
            e.printStackTrace();
        }

        return null;
    }

    public AssessmentRound insertAssessmentRound(String name, Group club,
            Group division, Group group, User user, Timestamp start,
            Timestamp end, boolean includeChildren, Timestamp paymentDate,
            Timestamp runOnDate) {
        AssessmentRound round = null;
        try {
            round = getAssessmentRoundHome().create();
            round.setName(name);
            round.setClub(club);
            if (division == null) {
                division = findDivisionForGroup(group);
            }
            if (division != null) round.setDivision(division);
            if (group != null) round.setGroup(group);
            round.setExecutedBy(user);
            round.setStartTime(start);
            if (end != null) round.setEndTime(end);
            round.setIncludeChildren(includeChildren);
            round.setPaymentDate(paymentDate);
            round.setRunOnDate(runOnDate);

            round.store();
        } catch (CreateException e) {
            e.printStackTrace();

            return null;
        }

        return round;
    }

    public boolean deleteAssessmentRound(String ids[]) {
        try {
            for (int i = 0; i < ids.length; i++) {
                Integer id = new Integer(ids[i]);
                AssessmentRound eRound = getAssessmentRoundHome()
                        .findByPrimaryKey(id);
                eRound.setDeleted(true);
                eRound.store();

                //@TODO just get entries I'm allowed to delete!!!!
                Collection rec = getFinanceEntryHome()
                        .findAllByAssessmentRound(eRound);
                if (rec != null && !rec.isEmpty()) {
                    Iterator it = rec.iterator();
                    while (it.hasNext()) {
                        FinanceEntry entry = (FinanceEntry) it.next();
                        if (entry.getAmountEqualized() == 0.0) {
                            try {
                                entry.remove();
                            } catch (RemoveException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }

            return true;
        } catch (FinderException e) {
            e.printStackTrace();
        } catch (EJBException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Collection findAllUsersCreditCards(Group club, User user,
            Group division) {
        try {
            return getUserCreditCardHome().findAllByUser(club, division, user);
        } catch (FinderException e) {
        }

        return null;
    }

    private ClubTariffTypeHome getClubTariffTypeHome() {
        try {
            return (ClubTariffTypeHome) IDOLookup.getHome(ClubTariffType.class);
        } catch (IDOLookupException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insertPayment(String type, String amount, User currentUser,
            Map basket, IWUserContext iwuc) {
        PaymentType eType = null;
        if (type != null) {
            try {
                PaymentTypeHome pHome = (PaymentTypeHome) IDOLookup
                        .getHome(PaymentType.class);
                eType = pHome.findByPrimaryKey(new Integer(type));
            } catch (IDOLookupException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (FinderException e) {
                e.printStackTrace();
            }
        }

        int am = 0;
        try {
            am = Integer.parseInt(amount);
        } catch (Exception e) {
        }

        return insertPayment(eType, am, currentUser, basket, iwuc);
    }

    public boolean insertPayment(PaymentType type, int amount,
            User currentUser, Map basket, IWUserContext iwuc) {

        UserTransaction trans = null;
        try {
            trans = getSessionContext().getUserTransaction();
            trans.begin();

            Map users = equalizeBasket(basket, amount, iwuc);
            Iterator it = users.values().iterator();
            while (it.hasNext()) {
                Map divisions = (HashMap) it.next();

                Iterator it2 = divisions.values().iterator();
                while (it2.hasNext()) {
                    PaymentInfo info = (PaymentInfo) it2.next();

                    FinanceEntry entry = getFinanceEntryHome().create();
                    entry.setUser(info.getUser());
                    entry.setClub(info.getClub());
                    entry.setDivision(info.getDivision());
                    entry.setAmount(info.getAmount());
                    entry.setDateOfEntry(IWTimestamp.getTimestampRightNow());
                    entry.setStatusCreated();
                    entry.setTypePayment();
                    entry.setPaymentType(type);
                    entry.setEntryOpen(false);
                    entry.setInsertedByUser(currentUser);
                    entry.store();
                }
            }
            
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                trans.rollback();
            } catch (IllegalStateException e1) {
                e1.printStackTrace();
            } catch (SecurityException e1) {
                e1.printStackTrace();
            } catch (SystemException e1) {
                e1.printStackTrace();
            }

            return false;
        }

        return true;
    }

    private Map equalizeBasket(Map basket, float amount, IWUserContext iwuc) {
        Map users = new HashMap();
        Map divisions = null;

        if (basket != null && !basket.isEmpty()) {
            List toRemove = new ArrayList();

            BasketBusiness bBiz = getBasketBusiness(iwuc);
            try {
                bBiz.emtpyExtraInfo();
            } catch (RemoteException e2) {
                e2.printStackTrace();
            }

            //Sorting the basket. Putting the oldest items first.
            Comparator comparator = new Comparator() {

                public int compare(Object first, Object second) {
                    BasketEntry firstEntry = (BasketEntry) first;
                    BasketEntry secondEntry = (BasketEntry) second;

                    FinanceEntry firstItem = (FinanceEntry) firstEntry
                            .getItem();
                    FinanceEntry secondItem = (FinanceEntry) secondEntry
                            .getItem();

                    if (firstItem.getDateOfEntry().getTime() < secondItem
                            .getDateOfEntry().getTime()) {
                        return -1;
                    } else if (firstItem.getDateOfEntry().getTime() > secondItem
                            .getDateOfEntry().getTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            };

            Collection values = basket.values();
            List sortable = new ArrayList(values);
            Collections.sort(sortable, comparator);
            Iterator it = sortable.iterator();

            while (it.hasNext() && amount != 0.0f) {
                BasketEntry bEntry = (BasketEntry) it.next();
                FinanceEntry entry = (FinanceEntry) bEntry.getItem();
                if (amount >= entry.getItemPrice().doubleValue()) {
                    FinanceExtraBasketInfo info = new FinanceExtraBasketInfo(
                            entry, entry.getItemPrice().doubleValue());
                    try {
                        bBiz.addExtraInfo(info);
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }

                    if (users.containsKey(entry.getUser())) {
                        divisions = (Map) users.get(entry.getUser());
                    } else {
                        divisions = new HashMap();
                        users.put(entry.getUser(), divisions);
                    }

                    if (divisions.containsKey(entry.getDivision())) {
                        PaymentInfo paymentInfo = (PaymentInfo) divisions
                                .get(entry.getDivision());
                        int am = paymentInfo.getAmount();
                        paymentInfo.setAmount(am
                                + (int) entry.getItemPrice().doubleValue());
                    } else {
                        PaymentInfo paymentInfo = new PaymentInfo(entry
                                .getUser(), entry.getClub(), entry
                                .getDivision(), (int) entry.getItemPrice()
                                .doubleValue());
                        divisions.put(entry.getDivision(), paymentInfo);
                    }

                    amount -= entry.getItemPrice().doubleValue();
                    entry.setAmountEqualized(entry.getAmountEqualized()
                            + entry.getItemPrice().doubleValue());
                    entry.setEntryOpen(false);
                    toRemove.add(entry);
                } else {
                    FinanceExtraBasketInfo info = new FinanceExtraBasketInfo(
                            entry, amount);
                    try {
                        bBiz.addExtraInfo(info);
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }

                    if (users.containsKey(entry.getUser())) {
                        divisions = (Map) users.get(entry.getUser());
                    } else {
                        divisions = new HashMap();
                        users.put(entry.getUser(), divisions);
                    }

                    if (divisions.containsKey(entry.getDivision())) {
                        PaymentInfo paymentInfo = (PaymentInfo) divisions
                                .get(entry.getDivision());
                        int am = paymentInfo.getAmount();
                        paymentInfo.setAmount(am + (int) amount);
                    } else {
                        PaymentInfo paymentInfo = new PaymentInfo(entry
                                .getUser(), entry.getClub(), entry
                                .getDivision(), (int) amount);
                        divisions.put(entry.getDivision(), paymentInfo);
                    }

                    entry.setAmountEqualized(entry.getAmountEqualized()
                            + amount);
                    amount = 0.0f;
                }

                entry.store();
            }

            if (!toRemove.isEmpty()) {
                Iterator it2 = toRemove.iterator();
                while (it2.hasNext()) {
                    FinanceEntry entry = (FinanceEntry) it2.next();
                    try {
                        getBasketBusiness(iwuc).removeItem(entry);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return users;
    }

    private BasketBusiness getBasketBusiness(IWUserContext iwuc) {
        try {
            return (BasketBusiness) getSessionInstance(iwuc,
                    BasketBusiness.class);
        } catch (IBOLookupException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean insertManualAssessment(Group club, Group div, User user,
            String groupId, String tariffId, String amount, String info,
            User currentUser, Timestamp paymentDate) {
        Group group = null;
        if (groupId != null) {
            try {
                GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class);
                group = gHome.findByPrimaryKey(new Integer(groupId));
            } catch (IDOLookupException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (FinderException e) {
                e.printStackTrace();
            }
        }

        ClubTariff tariff = null;
        if (tariffId != null) {
            try {
                tariff = getClubTariffHome().findByPrimaryKey(
                        new Integer(tariffId));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (FinderException e) {
                e.printStackTrace();
            }
        }

        float am = 0;
        try {
            am = Float.parseFloat(amount);
        } catch (Exception e) {
        }

        return insertManualAssessment(club, div, user, group, tariff, am, info,
                currentUser, paymentDate);
    }

    public boolean insertManualAssessment(Group club, Group div, User user,
            Group group, ClubTariff tariff, float amount, String info,
            User currentUser, Timestamp paymentDate) {
        try {
            FinanceEntry entry = getFinanceEntryHome().create();
            entry.setUser(user);
            entry.setClub(club);
            if (div == null) {
                div = findDivisionForGroup(group);
            }
            entry.setDivision(div);
            entry.setGroup(group);
            entry.setAmount(amount);
            entry.setDateOfEntry(IWTimestamp.getTimestampRightNow());
            if (info != null && !"".equals(info))
                entry.setInfo(info);
            else
                entry.setInfo(tariff.getText());
            entry.setTariffID(((Integer) tariff.getPrimaryKey()).intValue());
            entry.setTariffTypeID(tariff.getTariffTypeId());
            entry.setStatusCreated();
            entry.setTypeManual();
            if (amount < 0.0f) {
                entry.setEntryOpen(false);
                entry.setAmountEqualized(amount);
                equalizeEntries(club, div, user, -amount);
            } else {
                entry.setEntryOpen(true);
            }
            entry.setInsertedByUser(currentUser);
            entry.setPaymentDate(paymentDate);
            entry.store();

            return true;
        } catch (CreateException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void equalizeEntries(Group club, Group div, User user, float amount) {
        try {
            Collection entries = getFinanceEntryHome()
                    .findAllOpenAssessmentByUser(club, div, user);
            if (entries != null && !entries.isEmpty()) {
                Iterator it = entries.iterator();
                while (it.hasNext() && amount != 0.0f) {
                    System.out.println("amount = " + amount);
                    FinanceEntry entry = (FinanceEntry) it.next();
                    System.out.println("itemprice = "
                            + entry.getItemPrice().doubleValue());
                    if (amount >= entry.getItemPrice().doubleValue()) {
                        amount -= entry.getItemPrice().doubleValue();
                        entry.setAmountEqualized(entry.getAmountEqualized()
                                + entry.getItemPrice().doubleValue());
                        entry.setEntryOpen(false);
                    } else {
                        entry.setAmountEqualized(entry.getAmountEqualized()
                                + amount);
                        amount = 0.0f;
                    }

                    entry.store();
                }
            }
        } catch (FinderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public Collection findAllOpenAssessmentEntriesByUserGroupAndDivision(
            Group club, Group div, User user) {
        try {
            return getFinanceEntryHome().findAllOpenAssessmentByUser(club, div,
                    user);
        } catch (FinderException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Collection findAllPaymentEntriesByUserGroupAndDivision(Group club,
            Group div, User user) {
        try {
            return getFinanceEntryHome().findAllPaymentsByUser(club, div, user);
        } catch (FinderException e) {
            e.printStackTrace();
        }

        return null;
    }

    public FinanceEntry getFinanceEntryByPrimaryKey(Integer id) {
        try {
            return getFinanceEntryHome().findByPrimaryKey(id);
        } catch (FinderException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Collection findAllPaymentTypes() {
        try {
            return getPaymentTypeHome().findAllPaymentTypes();
        } catch (FinderException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ClubTariffHome getClubTariffHome() {
        try {
            return (ClubTariffHome) IDOLookup.getHome(ClubTariff.class);
        } catch (IDOLookupException e) {
            e.printStackTrace();
        }

        return null;
    }

    private FinanceEntryHome getFinanceEntryHome() {
        try {
            return (FinanceEntryHome) IDOLookup.getHome(FinanceEntry.class);
        } catch (IDOLookupException e) {
            e.printStackTrace();
        }

        return null;
    }

    private CreditCardContractHome getCreditCardContractHome() {
        try {
            return (CreditCardContractHome) IDOLookup
                    .getHome(CreditCardContract.class);
        } catch (IDOLookupException e) {
            e.printStackTrace();
        }

        return null;
    }

    private CreditCardTypeHome getCreditCardTypeHome() {
        try {
            return (CreditCardTypeHome) IDOLookup.getHome(CreditCardType.class);
        } catch (IDOLookupException e) {
            e.printStackTrace();
        }

        return null;
    }

    private AssessmentRoundHome getAssessmentRoundHome() {
        try {
            return (AssessmentRoundHome) IDOLookup
                    .getHome(AssessmentRound.class);
        } catch (IDOLookupException e) {
            e.printStackTrace();
        }

        return null;
    }

    private PaymentTypeHome getPaymentTypeHome() {
        try {
            return (PaymentTypeHome) IDOLookup.getHome(PaymentType.class);
        } catch (IDOLookupException e) {
            e.printStackTrace();
        }

        return null;
    }

    private UserCreditCardHome getUserCreditCardHome() {
        try {
            return (UserCreditCardHome) IDOLookup.getHome(UserCreditCard.class);
        } catch (IDOLookupException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Collection getFinanceEntriesByDateIntervalDivisionsAndGroups(
            Group club, String[] types, java.sql.Date dateFrom,
            java.sql.Date dateTo, Collection divisionsFilter,
            Collection groupsFilter, String personalID) {
        try {
            return getFinanceEntryHome()
                    .findAllFinanceEntriesByDateIntervalDivisionsAndGroupsOrderedByDivisionGroupAndDate(
                            club, types, dateFrom, dateTo, divisionsFilter,
                            groupsFilter, personalID);
        } catch (FinderException e) {
            return ListUtil.getEmptyList();
        }
    }

    public Collection getFinanceEntriesByPaymentDateDivisionsAndGroups(
            Group club, String[] types, Collection divisionsFilter,
            Collection groupsFilter, String personalID) {
        try {
            return getFinanceEntryHome()
                    .findAllFinanceEntriesByPaymentDateDivisionsAndGroupsOrderedByDivisionGroupAndDate(
                            club, types, divisionsFilter, groupsFilter,
                            personalID);
        } catch (FinderException e) {
            return ListUtil.getEmptyList();
        }
    }
}