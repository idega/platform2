package se.idega.idegaweb.commune.account.citizen.data;


public class CitizenApplicantPutChildrenHomeImpl extends com.idega.data.IDOFactory implements CitizenApplicantPutChildrenHome
{
 protected Class getEntityInterfaceClass(){
  return CitizenApplicantPutChildren.class;
 }


 public CitizenApplicantPutChildren create() throws javax.ejb.CreateException{
  return (CitizenApplicantPutChildren) super.createIDO();
 }


public CitizenApplicantPutChildren findByApplicationId(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((CitizenApplicantPutChildrenBMPBean)entity).ejbFindByApplicationId(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public CitizenApplicantPutChildren findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CitizenApplicantPutChildren) super.findByPrimaryKeyIDO(pk);
 }



}