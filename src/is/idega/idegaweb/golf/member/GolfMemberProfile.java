package is.idega.idegaweb.golf.member;

import is.idega.idegaweb.golf.entity.Address;
import is.idega.idegaweb.golf.entity.Card;
import is.idega.idegaweb.golf.entity.CardHome;
import is.idega.idegaweb.golf.entity.Family;
import is.idega.idegaweb.golf.entity.FamilyHome;
import is.idega.idegaweb.golf.entity.Group;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.Phone;
import is.idega.idegaweb.golf.entity.UnionMemberInfo;

import java.sql.SQLException;

import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDOLookup;

public class GolfMemberProfile {

  private Member eMember = null;
  private MemberInfo eMemberInfo = null;
  private UnionMemberInfo[] eUMIs = null;
  private UnionMemberInfo eUMI = null;
  private Address[] eAddresses = null;
  private Phone[] ePhones = null;
  private Family eFamily = null;
  private Group[] eGroups = null;
  private Card eCard = null;
  private Member[] eFamilyMembers = null;
  private int UnionId = 0;
  public boolean bEditMember ,bEditMemberInfo, bEditUMI ,bEditPhones , bEditAddresses,bEditGroups,bEditFamily , bEditCard , bEditFamilyMembers ;
  public boolean bHasMember ,bHasMemberInfo,bHasUMI,bHasUMIs,bHasPhones,bHasAddresses,bHasGroups,bHasFamily ,bHasCard , bHasFamilyMembers ;

  public GolfMemberProfile(Member eMember){
    init();
    setMember(eMember);
    fillProfile();
  }

  public GolfMemberProfile(Member eMember,int iUnionId){
    init();
    setMember(eMember);
    try {
      setUnionMemberInfo(eMember.getUnionMemberInfo(iUnionId));
    }
    catch (Exception ex) {ex.printStackTrace(); }
    fillProfile();
  }
  private void init(){
    bEditMember = false;
    bEditMemberInfo = false;
    bEditUMI = false;
    bEditPhones = false;
    bEditAddresses = false;
    bEditGroups = false;
    bEditFamily = false;
    bEditCard = false;
    bEditFamilyMembers = false;
    bHasMember = false;
    bHasMemberInfo = false;
    bHasUMI = false;
    bHasUMIs = false;
    bHasPhones = false;
    bHasAddresses = false;
    bHasGroups = false;
    bHasFamily = false;
    bHasCard = false;
    bHasFamilyMembers = false;
  }

  private void fillProfile(){
    // UnionMemberInfos
    try {
      eUMIs = (UnionMemberInfo[]) ((UnionMemberInfo) IDOLookup.instanciateEntity(UnionMemberInfo.class)).findAllByColumn(eMember.getIDColumnName(),eMember.getID());
      if(eUMIs != null && eUMIs.length == 0)
        eUMIs = null;
      else
        bHasUMIs = true;
    }
    catch (Exception ex) {
      System.err.println("GolfMemberProfile.eUMIs");
      ex.printStackTrace();
      eUMIs = null;
    }
    // MemberInfo
    try {
      this.eMemberInfo = eMember.getMemberInfo();
      if(eMemberInfo != null )
        bHasMemberInfo = true;
    }
    catch (Exception ex) {}
    // Groups:
    try {
      this.eGroups = eMember.getGroups();
      if(eGroups != null && eGroups.length == 0)
        eGroups = null;
      else
        bHasGroups = true;
    }
    catch (Exception ex) {}
    // Addresses:
    try {
      this.eAddresses = eMember.getAddress();
      if(eAddresses != null && eAddresses.length == 0)
        eAddresses = null;
      else
        bHasAddresses = true;
    }
    catch (Exception ex) {}
    // Phones:
    try {
      this.ePhones = eMember.getPhone();
      if(ePhones != null && ePhones.length == 0)
        ePhones = null;
      else
        bHasPhones = true;
    }
    catch (Exception ex) {}

    if(eUMI != null && eUMI.getFamilyId() > 0){
      // Family:
      try {
        eFamily = ((FamilyHome) IDOLookup.getHomeLegacy(Family.class)).findByPrimaryKey(eUMI.getFamilyId());
        if(eFamily != null)
          this.bHasFamily = true;
      }
      catch (Exception ex) {}
      // Card:
      try {
        int id = eUMI.getCardId();
        if(id > 1)
          eCard = ((CardHome) IDOLookup.getHomeLegacy(Card.class)).findByPrimaryKey(id);
      }
      catch (Exception ex) { }

    }
    // FamilyMembers:
    if(eUMI != null && eUMI.getFamilyId() > 0){
      try {
        this.eFamilyMembers = eMember.getFamilyMembers(eUMI.getFamilyId());
        if(eFamilyMembers != null && eFamilyMembers.length == 0)
          eFamilyMembers = null;
        else
          bHasFamilyMembers = true;
      }
      catch (Exception ex) {ex.printStackTrace();}
    }
  }
  private void saveProfile(){
    //Member
    if(bHasMember && bEditMember){
      try {  saveEntity(eMember);   }
      catch (Exception ex) { }
    }
    // MemberInfo
    if(bHasMemberInfo && bEditMemberInfo){
      try {  saveEntity(eMemberInfo);   }
      catch (Exception ex) { }
    }
    // Card
    if(bHasCard && bEditCard){
      try {  saveEntity(eCard);   }
      catch (Exception ex) { }
    }
    // UnionMemberInfo
    if(bHasUMI && bEditUMI){
      try {  saveEntity(eUMI);   }
      catch (Exception ex) { }
    }
    // Family
    if(bHasFamily && bEditFamily){
      try {  saveEntity(eFamily);   }
      catch (Exception ex) { }
    }
    // Addresses
    if(bHasAddresses && bEditAddresses){
      for (int i = 0; i < eAddresses.length; i++) {
        try{ saveEntity(eAddresses[i]); }
        catch (Exception ex) { }
      }
    }
    // Phones
    if(bHasPhones && bEditPhones){
      for (int i = 0; i < eUMIs.length; i++) {
        try{ saveEntity(ePhones[i]); }
        catch (Exception ex) { }
      }
    }
    // Familymembers
    if(bHasFamilyMembers && bEditFamilyMembers){
      for (int i = 0; i < eFamilyMembers.length; i++) {
        try{ saveEntity(eFamilyMembers[i]); }
        catch (Exception ex) { }
      }
    }
    // Groups
    if(bHasGroups && bEditGroups){
      for (int i = 0; i < eGroups.length; i++) {
        try{ saveEntity(eGroups[i]); }
        catch (Exception ex) { }
      }
    }
  }

  private void saveEntity(IDOLegacyEntity entity)throws SQLException{
    if(entity.getID() > 0)
      entity.update();
    else
      entity.insert();
  }

  public Member getMember(){
    return this.eMember;
  }
  public void setMember(Member eMember){
    this.eMember = eMember;
    this.bHasMember = (eMember != null)? true:false;
  }
  public MemberInfo getMemberInfo(){
    return this.eMemberInfo;
  }
  public void setMemberInfo(MemberInfo eMemberInfo){
    this.eMemberInfo = eMemberInfo;
    this.bHasMemberInfo = (eMemberInfo != null)? true:false;
  }
  public UnionMemberInfo getUnionMemberInfo(){
    return this.eUMI;
  }
  public void setUnionMemberInfo(UnionMemberInfo eUMI){
    this.eUMI = eUMI;
    this.bHasUMI = (eUMI != null)? true:false;
  }
  public Family getFamily(){
    return this.eFamily;
  }
  public void setFamily(Family eFamily){
    this.eFamily = eFamily;
    this.bHasFamily = (eFamily != null)? true:false;
  }
  public Card getCard(){
    return this.eCard;
  }
  public void setCard(Card eCard){
    this.eCard = eCard;
    this.bHasCard = (eCard != null)? true:false;
  }
  public Group[] getGroups(){
    return this.eGroups;
  }
  public void setGroups(Group[] eGroups){
    this.eGroups = eGroups;
    this.bHasGroups = (eGroups != null)? true:false;
  }
  public Phone[]  gePhones(){
    return this.ePhones;
  }
  public void setPhones(Phone[] ePhones){
    this.ePhones = ePhones;
    this.bHasPhones = (ePhones != null)? true:false;
  }
  public Address[] getAddresses(){
    return this.eAddresses;
  }
  public void setAddresses(Address[] eAddresses){
    this.eAddresses = eAddresses;
    this.bHasAddresses = (eAddresses != null)? true:false;
  }
  public UnionMemberInfo[] getUnionMemberInfos(){
    return this.eUMIs;
  }
  public void setUnionMemberInfos(UnionMemberInfo[] eUMIs){
    this.eUMIs = eUMIs;
    this.bHasUMIs = (eUMIs != null)? true:false;
  }
  public Member[] getFamilyMembers(){
    return this.eFamilyMembers;
  }
  public void stFamilyMembers(Member[] eMembers){
    this.eFamilyMembers = eMembers;
    this.bHasFamilyMembers = (eFamilyMembers != null)? true:false;
  }
}