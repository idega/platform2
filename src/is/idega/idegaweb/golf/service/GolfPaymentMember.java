package is.idega.idegaweb.golf.service;



import java.lang.String;

import com.idega.util.IWTimestamp;



/**

*@author <a href="mailto:aron@idega.is">Aron Birkir</a>

*@version 1.0

*/



public class GolfPaymentMember {

  private int iMemberId, iFamilyId, iInstallments, iAge,iPriceCatId;

  private String sName, sSocialSecurityNumber,sGender, sFamilyStatus;

  private IWTimestamp RegistrationDate,FirstInstallmentDate;





  public GolfPaymentMember(){

    this(0,0,0,0,0,"","","","",new IWTimestamp(),new IWTimestamp());

  }



  public GolfPaymentMember( int iMemberId, int iFamilyId, int iInstallments, int iAge,int iPriceCatalogueId,

                            String sName, String sSocialSecurityNumber,String sGender,String sFamilyStatus,

                            IWTimestamp RegistrationDate, IWTimestamp FirstInstallmentDate){

    this.iMemberId = iMemberId;

    this.iFamilyId = iFamilyId;

    this.iInstallments = iInstallments;

    this.iAge = iAge;

    this.iPriceCatId = iPriceCatalogueId;

    this.sName = sName;

    this.sSocialSecurityNumber = sSocialSecurityNumber;

    this.sGender = sGender;

    this.sFamilyStatus = sFamilyStatus;

    this.RegistrationDate = RegistrationDate;

    this.FirstInstallmentDate = FirstInstallmentDate;

  }



  public void setMemberId(int iMemberId){

    this.iMemberId = iMemberId;

  }



  public int getMemberId(){

    return this.iMemberId;

  }



  public void setID(int iMemberId){

    this.iMemberId = iMemberId;

  }



  public int getID(){

    return this.iMemberId;

  }



  public void setFamilyId(int iFamilyId){

    this.iFamilyId = iFamilyId;

  }

  public int getFamilyId(){

    return this.iFamilyId;

  }

  public void setInstallments(int iInstallments){

    this.iInstallments = iInstallments;

  }

  public int getInstallments(){

    return this.iInstallments;

  }

  public void setAge(int iAge){

    this.iAge = iAge;

  }

  public int getAge(){

    return this.iAge;

  }

  public void setPriceCatalogueId(int iPriceCatalogueId){

    this.iPriceCatId = iPriceCatalogueId;

  }

  public int getPriceCatalogueId(){

    return this.iPriceCatId;

  }

  public void setName(String sName){

    this.sName = sName;

  }

  public String getName(){

    return this.sName;

  }

  public void setSocialSecurityNumber(String sSocialSecurityNumber){

    this.sSocialSecurityNumber = sSocialSecurityNumber;

  }

  public String getSocialSecurityNumber(){

    return this.sSocialSecurityNumber;

  }

  public void setGender(String sGender){

    this.sGender = sGender;

  }

  public String getGender(){

    return this.sGender;

  }

  public void setFamilyStatus(String sFamilyStatus){

    this.sFamilyStatus = sFamilyStatus;

  }

  public String getFamilyStatus(){

    return this.sFamilyStatus;

  }

  public void setRegistrationDate(IWTimestamp RegistrationDate){

    this.RegistrationDate = RegistrationDate;

  }

  public IWTimestamp getRegistrationDate(){

    return this.RegistrationDate;

  }

  public void setFirstInstallmentDate(IWTimestamp FirstInstallmentDate){

    this.FirstInstallmentDate = FirstInstallmentDate;

  }

  public IWTimestamp getFirstInstallmentDate(){

    return this.FirstInstallmentDate;

  }











}// class GolfPaymentMember

