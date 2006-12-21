/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.business;

import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * @author palli
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
class PaymentInfo {
    private User user = null;
    private Group club = null;
    private Group division = null;
    private Group group = null;
    private int amount = 0;
    private String info = null;
    
    public PaymentInfo(User user, Group club, Group division, Group group, int amount, String info) {
        this.user = user;
        this.club = club;
        this.division = division;
        this.group = group;
        this.amount = amount;
        this.info = info;
    }
    
    /**
     * @return Returns the amount.
     */
    public int getAmount() {
        return this.amount;
    }
    /**
     * @param amount The amount to set.
     */
    public void setAmount(int amount) {
        this.amount = amount;
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
}