package se.idega.idegaweb.commune.accounting.invoice.data;


public class BatchRunHomeImpl extends com.idega.data.IDOFactory implements BatchRunHome
{
 protected Class getEntityInterfaceClass(){
  return BatchRun.class;
 }


 public BatchRun create() throws javax.ejb.CreateException{
  return (BatchRun) super.createIDO();
 }


public BatchRun findBySchoolCategory(com.idega.block.school.data.SchoolCategory p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((BatchRunBMPBean)entity).ejbFindBySchoolCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public BatchRun findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (BatchRun) super.findByPrimaryKeyIDO(pk);
 }



}