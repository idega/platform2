package se.idega.idegaweb.commune.childcare.export.data;


public class ChildCareExportTimeHomeImpl extends com.idega.data.IDOFactory implements ChildCareExportTimeHome
{
 protected Class getEntityInterfaceClass(){
  return ChildCareExportTime.class;
 }


 public ChildCareExportTime create() throws javax.ejb.CreateException{
  return (ChildCareExportTime) super.createIDO();
 }


public ChildCareExportTime findLatest(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareExportTimeBMPBean)entity).ejbFindLatest(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public ChildCareExportTime findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ChildCareExportTime) super.findByPrimaryKeyIDO(pk);
 }



}