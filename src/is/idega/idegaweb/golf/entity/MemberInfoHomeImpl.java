package is.idega.idegaweb.golf.entity;


public class MemberInfoHomeImpl extends com.idega.data.IDOFactory implements MemberInfoHome
{
 protected Class getEntityInterfaceClass(){
  return MemberInfo.class;
 }


 public MemberInfo create() throws javax.ejb.CreateException{
  return (MemberInfo) super.createIDO();
 }


 public MemberInfo createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


public MemberInfo findByMember(is.idega.idegaweb.golf.entity.Member p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((MemberInfoBMPBean)entity).ejbFindByMember(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public MemberInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (MemberInfo) super.findByPrimaryKeyIDO(pk);
 }


 public MemberInfo findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (MemberInfo) super.findByPrimaryKeyIDO(id);
 }


 public MemberInfo findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}