package is.idega.idegaweb.golf.entity;


public class MemberOverViewHomeImpl extends com.idega.data.IDOFactory implements MemberOverViewHome
{
 protected Class getEntityInterfaceClass(){
  return MemberOverView.class;
 }

 public MemberOverView create() throws javax.ejb.CreateException{
  return (MemberOverView) super.idoCreate();
 }

 public MemberOverView createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public MemberOverView findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (MemberOverView) super.idoFindByPrimaryKey(id);
 }

 public MemberOverView findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (MemberOverView) super.idoFindByPrimaryKey(pk);
 }

 public MemberOverView findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}