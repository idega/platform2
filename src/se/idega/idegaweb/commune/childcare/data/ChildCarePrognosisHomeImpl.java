package se.idega.idegaweb.commune.childcare.data;


public class ChildCarePrognosisHomeImpl extends com.idega.data.IDOFactory implements ChildCarePrognosisHome
{
 protected Class getEntityInterfaceClass(){
  return ChildCarePrognosis.class;
 }


 public ChildCarePrognosis create() throws javax.ejb.CreateException{
  return (ChildCarePrognosis) super.createIDO();
 }


public ChildCarePrognosis findPrognosis(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCarePrognosisBMPBean)entity).ejbFindPrognosis(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public ChildCarePrognosis findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ChildCarePrognosis) super.findByPrimaryKeyIDO(pk);
 }



}