package is.idega.idegaweb.golf.entity;


public class UnionHomeImpl extends com.idega.data.IDOFactory implements UnionHome
{
 protected Class getEntityInterfaceClass(){
  return Union.class;
 }


 public Union create() throws javax.ejb.CreateException{
  return (Union) super.createIDO();
 }


 public Union createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


public java.util.Collection findAllUnions()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((UnionBMPBean)entity).ejbFindAllUnions();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Union findByAbbreviation(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((UnionBMPBean)entity).ejbFindByAbbreviation(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public Union findUnionByIWMemberSystemGroup(com.idega.user.data.Group p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((UnionBMPBean)entity).ejbFindUnionByIWMemberSystemGroup(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public Union findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Union) super.findByPrimaryKeyIDO(pk);
 }


 public Union findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Union) super.findByPrimaryKeyIDO(id);
 }


 public Union findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}