package is.idega.idegaweb.golf.entity;


public class PaymentRoundHomeImpl extends com.idega.data.IDOFactory implements PaymentRoundHome
{
 protected Class getEntityInterfaceClass(){
  return PaymentRound.class;
 }

 public PaymentRound create() throws javax.ejb.CreateException{
  return (PaymentRound) super.idoCreate();
 }

 public PaymentRound createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public PaymentRound findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (PaymentRound) super.idoFindByPrimaryKey(id);
 }

 public PaymentRound findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PaymentRound) super.idoFindByPrimaryKey(pk);
 }

 public PaymentRound findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}