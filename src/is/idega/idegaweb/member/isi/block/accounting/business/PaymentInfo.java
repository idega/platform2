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
    private int amount = 0;
    
    public PaymentInfo(User user, Group club, Group division, int amount) {
        this.user = user;
        this.club = club;
        this.division = division;
        this.amount = amount;
    }
    
    /**
     * @return Returns the amount.
     */
    public int getAmount() {
        return amount;
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
        return club;
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
        return division;
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
        return user;
    }
    /**
     * @param user The user to set.
     */
    public void setUser(User user) {
        this.user = user;
    }
}