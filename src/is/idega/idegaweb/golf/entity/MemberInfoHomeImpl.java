package is.idega.idegaweb.golf.entity;


public class MemberInfoHomeImpl extends com.idega.data.IDOFactory implements MemberInfoHome
{
 protected Class getEntityInterfaceClass(){
  return MemberInfo.class;
 }

 public MemberInfo create() throws javax.ejb.CreateException{
  return (MemberInfo) super.idoCreate();
 }

 public MemberInfo createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public MemberInfo findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (MemberInfo) super.idoFindByPrimaryKey(id);
 }

 public MemberInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (MemberInfo) super.idoFindByPrimaryKey(pk);
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