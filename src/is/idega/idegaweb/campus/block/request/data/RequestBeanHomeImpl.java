package is.idega.idegaweb.campus.block.request.data;


public class RequestBeanHomeImpl extends com.idega.data.IDOFactory implements RequestBeanHome
{
 protected Class getEntityInterfaceClass(){
  return RequestBean.class;
 }

 public RequestBean create() throws javax.ejb.CreateException{
  return (RequestBean) super.idoCreate();
 }

 public RequestBean createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public RequestBean findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (RequestBean) super.idoFindByPrimaryKey(id);
 }

 public RequestBean findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (RequestBean) super.idoFindByPrimaryKey(pk);
 }

 public RequestBean findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}