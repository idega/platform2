/*
 * $Id: Member.java,v 1.13 2005/02/08 10:10:38 laddi Exp $
 * Created on 8.2.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.entity;

import is.idega.idegaweb.golf.block.image.data.ImageEntity;

import java.sql.Date;
import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.data.IDOLegacyEntity;
import com.idega.data.UniqueIDCapable;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/02/08 10:10:38 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.13 $
 */
public interface Member extends IDOLegacyEntity, UniqueIDCapable {

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getEmail
	 */
	public String getEmail();

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#setEmail
	 */
	public void setEmail(String email);

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getFirstName
	 */
	public String getFirstName();

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#setFirstName
	 */
	public void setFirstName(String first_name);

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getMiddleName
	 */
	public String getMiddleName();

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#setMiddleName
	 */
	public void setMiddleName(String middle_name);

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getLastName
	 */
	public String getLastName();

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#setLastName
	 */
	public void setLastName(String last_name);

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#setFullName
	 */
	public void setFullName();

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getDateOfBirth
	 */
	public Date getDateOfBirth();

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#setDateOfBirth
	 */
	public void setDateOfBirth(Date dateOfBirth);

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getGender
	 */
	public String getGender();

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#setGender
	 */
	public void setGender(String gender);

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getSocialSecurityNumber
	 */
	public String getSocialSecurityNumber();

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getSSN
	 */
	public String getSSN();

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#setSocialSecurityNumber
	 */
	public void setSocialSecurityNumber(String social_security_number);

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getImageId
	 */
	public int getImageId();

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#setimage_id
	 */
	public void setimage_id(int image_id);

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#setimage_id
	 */
	public void setimage_id(Integer image_id);

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#setImageId
	 */
	public void setImageId(int image_id);

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#setImageId
	 */
	public void setImageId(Integer image_id);

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getImage
	 */
	public ImageEntity getImage();

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getJob
	 */
	public String getJob();

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#setJob
	 */
	public void setJob(String job);

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getWorkPlace
	 */
	public String getWorkPlace();

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#setWorkPlace
	 */
	public void setWorkPlace(String workPlace);

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getMemberInfo
	 */
	public MemberInfo getMemberInfo();

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getUnionMemberInfo
	 */
	public UnionMemberInfo getUnionMemberInfo(String union_id) throws SQLException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getUnionMemberInfo
	 */
	public UnionMemberInfo getUnionMemberInfo(String union_id, String member_id) throws SQLException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getUnionMemberInfo
	 */
	public UnionMemberInfo getUnionMemberInfo(int unionId) throws SQLException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getMainUnionID
	 */
	public int getMainUnionID() throws SQLException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#setMainUnion
	 */
	public void setMainUnion(Union union) throws SQLException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#setMainUnion
	 */
	public void setMainUnion(int iUnionId) throws SQLException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getMainUnion
	 */
	public Union getMainUnion() throws FinderException, SQLException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#setAddress
	 */
	public void setAddress(Address type);

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getAddress
	 */
	public Address[] getAddress() throws SQLException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getCards
	 */
	public Card[] getCards() throws SQLException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getScorecards
	 */
	public Scorecard[] getScorecards() throws SQLException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getHandicap
	 */
	public float getHandicap() throws SQLException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getUnions
	 */
	public Union[] getUnions() throws SQLException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#isMemberIn
	 */
	public boolean isMemberIn(Union union) throws SQLException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#isMemberInUnion
	 */
	public boolean isMemberInUnion(Union union) throws SQLException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#isMemberInUnion
	 */
	public boolean isMemberInUnion() throws SQLException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getPhone
	 */
	public Phone[] getPhone() throws SQLException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getFamilyMembers
	 */
	public Member[] getFamilyMembers(int FamilyId) throws SQLException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getGroups
	 */
	public Group[] getGroups() throws SQLException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getAge
	 */
	public int getAge();

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#setICUser
	 */
	public void setICUser(com.idega.user.data.User user);

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#getICUser
	 */
	public User getICUser();

}
