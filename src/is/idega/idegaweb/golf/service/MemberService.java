package is.idega.idegaweb.golf.service;

import java.sql.*;
import com.idega.data.*;
import is.idega.idegaweb.golf.entity.*;
import com.idega.util.*;
import com.idega.presentation.ui.*;
import java.io.*;

/**
 * Title:        Golf<p>
 * Description:  <p>
 * Copyright:    Copyright (c) idega 2000 <p>
 * Company:      idega margmiðlun<p>
 * @author idega 2000 - Ægir L. Traustason
 * @version 1.0
 */


public class MemberService
{
  MemberService(){
    super();

    memberId = 0;
    firstName = "";
    middleName = "";
    lastName = "";
    name = "";
    dateOfBirth = null;
    gender = "";
    activeMember = "";
    socialSecurityNumber = "";
    memberNumber = 0;
    familyId = 0;
    addressId = 0;
    street = "";
    streetNumber = "";
    season = "";
    zipCodeId = 0;
    addressType = "";
    addressExtraInfo = "";
    addressCountryId = 0;
    postalCote = "";
    city = "";
    phoneId = 0;
    phoneCountryId = 0;
    phoneNumber = "";
    phoneType = "";
    cardId = 0;
    cardNumber = "";
    expireDate = null;
    cardCompany = "";
    cardType = "";
    handicap = 0;
    handicapFirst = 0;
    unionId = 0;
    familyName = "";
    imageId = 0;
    contentType = "";
    imageValue = null;
    imageName = "";
    dateAdded = null;
    fromFile = false;
  }
  //member table eiginleikar
  private int memberId;
  private String firstName;
  private String middleName;
  private String lastName;
  private String name;
  private Date dateOfBirth;
  private String gender;
  private String activeMember;
  private String socialSecurityNumber;
  private int memberNumber;
  private int familyId;

  // Address table eiginleikar
  private int addressId;
  private String street;
  private String streetNumber;
  private String season;
  private int zipCodeId;
  private String addressType;
  private String addressExtraInfo;
  private int addressCountryId;

  //zipcode table eiginleikar
  private String postalCote;
  private String city;

  //phone table eiginleikar
  private int phoneId;
  private int phoneCountryId;
  private String phoneNumber;
  private String phoneType;

  //card table eiginleikar
  private int cardId;
  private String cardNumber;
  private Date expireDate;
  private String cardCompany;
  private String cardType;

  //member_info eiginleikar
  private float handicap;
  private float handicapFirst;

  //union table eiginleikar
  private int unionId;

  //family table eiginleikar
  private String familyName;

  //image table eiginleikar
  private int imageId;
  private String contentType;
  private Blob imageValue;
  private String imageName;
  private Date dateAdded;
  private boolean fromFile;

  public void add(Member member){
    this.memberId = member.getID();
    this.firstName = member.getFirstName();
    this.middleName = member.getMiddleName();
    this.name = member.getName();
    this.lastName = member.getLastName();
    this.dateOfBirth = member.getDateOfBirth();
    this.gender = member.getGender();
    this.socialSecurityNumber = member.getSocialSecurityNumber();
  }

  public void add(Address address){
    this.addressId = address.getID();
    this.street = address.getStreet();
    this.streetNumber = address.getStreetNumber();
    this.season = address.getSeason();
    this.zipCodeId = address.getZipcodeId();
    this.addressType = address.getAddressType();
    this.addressExtraInfo = address.getExtraInfo();
    this.addressCountryId = address.getCountryId();
  }

  public void add(Phone phone){
    this.phoneId = phone.getID();
    this.phoneCountryId = phone.getCountryId();
    this.phoneNumber = phone.getNumber();
    this.phoneType = phone.getPhoneType();
  }

  public void add(Card card){
    this.cardId = card.getID();
    this.cardNumber = card.getCardNumber();
    this.expireDate = card.getExpireDate();
    this.cardCompany = card.getCardCompany();
    this.cardType = card.getCardType();
  }

  public void add(MemberInfo handicap){
    this.handicap = handicap.getHandicap();
    this.handicapFirst = handicap.getFirstHandicap();
  }

  public void add(ImageEntity image){
    this.imageId = image.getID();
    this.contentType = image.getContentType();
    //this.imageValue = image.getImageValue();
    this.imageName = image.getName();
    //this.dateAdded = image.getDateAdded();
    //this.fromFile = image.getFromFile();
  }

  public void add(Family family){
    this.familyName = family.getName();
    this.familyId = family.getID();
  }

  public void setUnionId(int unionId){
    this.unionId = unionId;
  }

  public int getUnionId(){
    return this.unionId;
  }

  public void setFamilyId(int familyId){
    this.familyId = familyId;
  }

  public int getFamilyId(){
    return this.familyId;
  }

  public String getAddressName(){
    return this.street;
  }

  public String getMemberName(){
    return this.name;
  }
}







