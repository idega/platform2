package is.idega.idegaweb.golf.entity;


public class PaymentTypeHomeImpl extends com.idega.data.IDOFactory implements PaymentTypeHome
{
 protected Class getEntityInterfaceClass(){
  return PaymentType.class;
 }

 public PaymentType create() throws javax.ejb.CreateException{
  return (PaymentType) super.idoCreate();
 }

 public PaymentType createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public PaymentType findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (PaymentType) super.idoFindByPrimaryKey(id);
 }

 public PaymentType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PaymentType) super.idoFindByPrimaryKey(pk);
 }

 public PaymentType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}