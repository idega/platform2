package is.idega.idegaweb.golf.entity;


public class StrokeHomeImpl extends com.idega.data.IDOFactory implements StrokeHome
{
 protected Class getEntityInterfaceClass(){
  return Stroke.class;
 }


 public Stroke create() throws javax.ejb.CreateException{
  return (Stroke) super.createIDO();
 }


 public Stroke createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public Stroke findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Stroke) super.findByPrimaryKeyIDO(pk);
 }


 public Stroke findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Stroke) super.findByPrimaryKeyIDO(id);
 }


 public Stroke findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


public int getCountDifferenceByMember(int p0,int p1,java.lang.String p2)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((StrokeBMPBean)entity).ejbHomeGetCountDifferenceByMember(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOfHolesPlayedByMember(int p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((StrokeBMPBean)entity).ejbHomeGetCountOfHolesPlayedByMember(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getSumOfStrokesByMember(int p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((StrokeBMPBean)entity).ejbHomeGetSumOfStrokesByMember(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}