package is.idega.idegaweb.golf.entity;


public class CardHomeImpl extends com.idega.data.IDOFactory implements CardHome
{
 protected Class getEntityInterfaceClass(){
  return Card.class;
 }

 public Card create() throws javax.ejb.CreateException{
  return (Card) super.idoCreate();
 }

 public Card createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Card findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Card) super.idoFindByPrimaryKey(id);
 }

 public Card findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Card) super.idoFindByPrimaryKey(pk);
 }

 public Card findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}