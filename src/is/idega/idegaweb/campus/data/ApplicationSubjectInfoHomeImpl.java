package is.idega.idegaweb.campus.data;


public class ApplicationSubjectInfoHomeImpl extends com.idega.data.IDOFactory implements ApplicationSubjectInfoHome
{
 protected Class getEntityInterfaceClass(){
  return ApplicationSubjectInfo.class;
 }


 public ApplicationSubjectInfo create() throws javax.ejb.CreateException{
  return (ApplicationSubjectInfo) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApplicationSubjectInfoBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ApplicationSubjectInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ApplicationSubjectInfo) super.findByPrimaryKeyIDO(pk);
 }



}