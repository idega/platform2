package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportMemberHomeImpl extends com.idega.data.IDOFactory implements WorkReportMemberHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportMember.class;
 }


 public WorkReportMember create() throws javax.ejb.CreateException{
  return (WorkReportMember) super.createIDO();
 }


public java.util.Collection findAllWorkReportMembersByWorkReportIdAndWorkReportGroup(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WorkReportMemberBMPBean)entity).ejbFindAllWorkReportMembersByWorkReportIdAndWorkReportGroup(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllWorkReportMembersByWorkReportIdOrderedByMemberName(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WorkReportMemberBMPBean)entity).ejbFindAllWorkReportMembersByWorkReportIdOrderedByMemberName(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public WorkReportMember findWorkReportMemberBySocialSecurityNumberAndWorkReportId(java.lang.String p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((WorkReportMemberBMPBean)entity).ejbFindWorkReportMemberBySocialSecurityNumberAndWorkReportId(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public WorkReportMember findWorkReportMemberByUserIdAndWorkReportId(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((WorkReportMemberBMPBean)entity).ejbFindWorkReportMemberByUserIdAndWorkReportId(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public WorkReportMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReportMember) super.findByPrimaryKeyIDO(pk);
 }


public int getCountOfFemaleMembersByWorkReport(is.idega.idegaweb.member.isi.block.reports.data.WorkReport p0){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfFemaleMembersByWorkReport(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfFemaleMembersEqualOrOlderThanAgeByWorkReport(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfFemaleMembersEqualOrOlderThanAgeByWorkReport(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfFemaleMembersOfYoungerAgeByWorkReport(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfFemaleMembersOfYoungerAgeByWorkReport(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfFemalePlayersByWorkReport(is.idega.idegaweb.member.isi.block.reports.data.WorkReport p0){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfFemalePlayersByWorkReport(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfFemalePlayersByWorkReportAndWorkReportGroup(is.idega.idegaweb.member.isi.block.reports.data.WorkReport p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p1){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfFemalePlayersByWorkReportAndWorkReportGroup(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p2){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfFemalePlayersEqualOrOlderThanAgeByWorkReport(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfFemalePlayersEqualOrOlderThanAgeByWorkReport(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p2){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfFemalePlayersOfYoungerAgeByWorkReport(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfFemalePlayersOfYoungerAgeByWorkReport(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfMaleMembersByWorkReport(is.idega.idegaweb.member.isi.block.reports.data.WorkReport p0){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfMaleMembersByWorkReport(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfMaleMembersEqualOrOlderThanAgeByWorkReport(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfMaleMembersEqualOrOlderThanAgeByWorkReport(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfMaleMembersOfYoungerAgeByWorkReport(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfMaleMembersOfYoungerAgeByWorkReport(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfMalePlayersByWorkReport(is.idega.idegaweb.member.isi.block.reports.data.WorkReport p0){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfMalePlayersByWorkReport(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfMalePlayersByWorkReportAndWorkReportGroup(is.idega.idegaweb.member.isi.block.reports.data.WorkReport p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p1){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfMalePlayersByWorkReportAndWorkReportGroup(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p2){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfMalePlayersEqualOrOlderThanAgeByWorkReport(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfMalePlayersEqualOrOlderThanAgeByWorkReport(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p2){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfMalePlayersOfYoungerAgeByWorkReport(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfMalePlayersOfYoungerAgeByWorkReport(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfMembersByWorkReport(is.idega.idegaweb.member.isi.block.reports.data.WorkReport p0){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfMembersByWorkReport(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfMembersEqualOrOlderThanAgeByWorkReport(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfMembersEqualOrOlderThanAgeByWorkReport(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfMembersOfYoungerAgeByWorkReport(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfMembersOfYoungerAgeByWorkReport(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfPlayersByWorkReport(is.idega.idegaweb.member.isi.block.reports.data.WorkReport p0){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfPlayersByWorkReport(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfPlayersByWorkReportAndWorkReportGroup(is.idega.idegaweb.member.isi.block.reports.data.WorkReport p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p1){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfPlayersByWorkReportAndWorkReportGroup(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfPlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p2){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfPlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfPlayersEqualOrOlderThanAgeByWorkReport(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfPlayersEqualOrOlderThanAgeByWorkReport(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfPlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p2){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfPlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfPlayersOfYoungerAgeByWorkReport(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetCountOfPlayersOfYoungerAgeByWorkReport(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.lang.String getFemaleGenderString(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.String theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetFemaleGenderString();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.lang.String getMaleGenderString(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.String theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetMaleGenderString();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}