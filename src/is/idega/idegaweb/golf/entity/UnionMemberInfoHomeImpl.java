package is.idega.idegaweb.golf.entity;


public class UnionMemberInfoHomeImpl extends com.idega.data.IDOFactory implements UnionMemberInfoHome
{
 protected Class getEntityInterfaceClass(){
  return UnionMemberInfo.class;
 }

 public UnionMemberInfo create() throws javax.ejb.CreateException{
  return (UnionMemberInfo) super.idoCreate();
 }

 public UnionMemberInfo createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public UnionMemberInfo findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (UnionMemberInfo) super.idoFindByPrimaryKey(id);
 }

 public UnionMemberInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (UnionMemberInfo) super.idoFindByPrimaryKey(pk);
 }

 public UnionMemberInfo findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}