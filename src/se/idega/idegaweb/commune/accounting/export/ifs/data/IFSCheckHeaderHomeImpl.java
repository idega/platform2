package se.idega.idegaweb.commune.accounting.export.ifs.data;


public class IFSCheckHeaderHomeImpl extends com.idega.data.IDOFactory implements IFSCheckHeaderHome
{
 protected Class getEntityInterfaceClass(){
  return IFSCheckHeader.class;
 }


 public IFSCheckHeader create() throws javax.ejb.CreateException{
  return (IFSCheckHeader) super.createIDO();
 }


public IFSCheckHeader findBySchoolCategory(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((IFSCheckHeaderBMPBean)entity).ejbFindBySchoolCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public IFSCheckHeader findBySchoolCategory(com.idega.block.school.data.SchoolCategory p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((IFSCheckHeaderBMPBean)entity).ejbFindBySchoolCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public IFSCheckHeader findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (IFSCheckHeader) super.findByPrimaryKeyIDO(pk);
 }



}