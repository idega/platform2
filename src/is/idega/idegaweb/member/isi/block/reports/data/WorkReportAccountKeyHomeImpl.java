package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportAccountKeyHomeImpl extends com.idega.data.IDOFactory implements WorkReportAccountKeyHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportAccountKey.class;
 }


 public WorkReportAccountKey create() throws javax.ejb.CreateException{
  return (WorkReportAccountKey) super.createIDO();
 }


 public WorkReportAccountKey findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReportAccountKey) super.findByPrimaryKeyIDO(pk);
 }


public java.lang.String getCreditTypeString(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.String theReturn = ((WorkReportAccountKeyBMPBean)entity).ejbHomeGetCreditTypeString();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.lang.String getDebetTypeString(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.String theReturn = ((WorkReportAccountKeyBMPBean)entity).ejbHomeGetDebetTypeString();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}