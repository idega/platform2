package is.idega.idegaweb.golf.entity;


public class PaymentHomeImpl extends com.idega.data.IDOFactory implements PaymentHome
{
 protected Class getEntityInterfaceClass(){
  return Payment.class;
 }

 public Payment create() throws javax.ejb.CreateException{
  return (Payment) super.idoCreate();
 }

 public Payment createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Payment findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Payment) super.idoFindByPrimaryKey(id);
 }

 public Payment findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Payment) super.idoFindByPrimaryKey(pk);
 }

 public Payment findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}