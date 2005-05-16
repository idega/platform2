/*
 * $Id: AdultEducationChoice.java,v 1.3 2005/05/16 13:42:54 laddi Exp $
 * Created on May 16, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.data;

import java.sql.Date;
import java.util.Collection;
import com.idega.block.process.data.Case;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/05/16 13:42:54 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public interface AdultEducationChoice extends IDOEntity, Case {

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#getCaseCodeKey
	 */
	public String getCaseCodeKey();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#getCaseCodeDescription
	 */
	public String getCaseCodeDescription();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#getUserPK
	 */
	public Object getUserPK();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#getCourse
	 */
	public AdultEducationCourse getCourse();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#getCoursePK
	 */
	public Object getCoursePK();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#getChoiceDate
	 */
	public Date getChoiceDate();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#getComment
	 */
	public String getComment();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#getChoiceOrder
	 */
	public int getChoiceOrder();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#hasGrantedRule1
	 */
	public boolean hasGrantedRule1();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#hasGrantedRule2
	 */
	public boolean hasGrantedRule2();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#hasGrantedRule3
	 */
	public boolean hasGrantedRule3();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#hasAllGranted
	 */
	public boolean hasAllGranted();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#getPriority
	 */
	public int getPriority();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#isPlacementMessageSent
	 */
	public boolean isPlacementMessageSent();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#isConfirmationMessageSent
	 */
	public boolean isConfirmationMessageSent();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#getRejectionComment
	 */
	public String getRejectionComment();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#getReasons
	 */
	public Collection getReasons() throws IDORelationshipException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#getOtherReason
	 */
	public String getOtherReason();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#setUser
	 */
	public void setUser(User user);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#setUser
	 */
	public void setUser(Object userPK);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#setCourse
	 */
	public void setCourse(AdultEducationCourse course);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#setCourse
	 */
	public void setCourse(Object coursePK);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#setChoiceDate
	 */
	public void setChoiceDate(Date date);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#setComment
	 */
	public void setComment(String comment);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#setChoiceOrder
	 */
	public void setChoiceOrder(int order);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#setGrantedRule1
	 */
	public void setGrantedRule1(boolean granted);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#setGrantedRule2
	 */
	public void setGrantedRule2(boolean granted);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#setGrantedRule3
	 */
	public void setGrantedRule3(boolean granted);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#setAllGranted
	 */
	public void setAllGranted(boolean granted);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#setPriority
	 */
	public void setPriority(int priority);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#setPlacementMessageSent
	 */
	public void setPlacementMessageSent(boolean sent);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#setConfirmationMessageSent
	 */
	public void setConfirmationMessageSent(boolean sent);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#setRejectionComment
	 */
	public void setRejectionComment(String comment);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#addReason
	 */
	public void addReason(AdultEducationChoiceReason reason) throws IDOAddRelationshipException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#addReason
	 */
	public void addReason(Object reasonPK) throws IDOAddRelationshipException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#removeReason
	 */
	public void removeReason(AdultEducationChoiceReason reason) throws IDORemoveRelationshipException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#removeAllReasons
	 */
	public void removeAllReasons() throws IDORemoveRelationshipException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#setOtherReason
	 */
	public void setOtherReason(String otherReason);
}
