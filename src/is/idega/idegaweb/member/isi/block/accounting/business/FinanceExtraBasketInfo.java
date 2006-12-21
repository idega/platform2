/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.business;

import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;

import com.idega.data.IDOPrimaryKey;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * @author palli
 *
 */
public class FinanceExtraBasketInfo {

    private Group club = null;
    private Group division = null;
    private Group group = null;
    private User user = null;
    private String info = null;
    private Double amount = null;
    private double amountPaid = 0.0;
    private IDOPrimaryKey key = null;
    
    public FinanceExtraBasketInfo(FinanceEntry entry, double amountPaid) {
        this.club = entry.getClub();
        this.division = entry.getDivision();
        this.group = entry.getGroup();
        this.user = entry.getUser();
        this.info = entry.getInfo();
        this.amount = entry.getItemPrice();
        this.amountPaid = amountPaid;
        this.key = entry.getItemID();
    }
    /**
     * @return Returns the amount.
     */
    public Double getAmount() {
        return this.amount;
    }
    /**
     * @param amount The amount to set.
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    /**
     * @return Returns the amountPaid.
     */
    public double getAmountPaid() {
        return this.amountPaid;
    }
    /**
     * @param amountPaid The amountPaid to set.
     */
    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }
    /**
     * @return Returns the club.
     */
    public Group getClub() {
        return this.club;
    }
    /**
     * @param club The club to set.
     */
    public void setClub(Group club) {
        this.club = club;
    }
    /**
     * @return Returns the division.
     */
    public Group getDivision() {
        return this.division;
    }
    /**
     * @param division The division to set.
     */
    public void setDivision(Group division) {
        this.division = division;
    }
    /**
     * @return Returns the group.
     */
    public Group getGroup() {
        return this.group;
    }
    /**
     * @param group The group to set.
     */
    public void setGroup(Group group) {
        this.group = group;
    }
    /**
     * @return Returns the info.
     */
    public String getInfo() {
        return this.info;
    }
    /**
     * @param info The info to set.
     */
    public void setInfo(String info) {
        this.info = info;
    }
    /**
     * @return Returns the key.
     */
    public IDOPrimaryKey getKey() {
        return this.key;
    }
    /**
     * @param key The key to set.
     */
    public void setKey(IDOPrimaryKey key) {
        this.key = key;
    }
    /**
     * @return Returns the user.
     */
    public User getUser() {
        return this.user;
    }
    /**
     * @param user The user to set.
     */
    public void setUser(User user) {
        this.user = user;
    }
}