package is.idega.idegaweb.golf.entity;


public class SubscriptionHomeImpl extends com.idega.data.IDOFactory implements SubscriptionHome
{
 protected Class getEntityInterfaceClass(){
  return Subscription.class;
 }

 public Subscription create() throws javax.ejb.CreateException{
  return (Subscription) super.idoCreate();
 }

 public Subscription createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Subscription findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Subscription) super.idoFindByPrimaryKey(id);
 }

 public Subscription findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Subscription) super.idoFindByPrimaryKey(pk);
 }

 public Subscription findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}