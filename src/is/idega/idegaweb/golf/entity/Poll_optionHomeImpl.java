package is.idega.idegaweb.golf.entity;


public class Poll_optionHomeImpl extends com.idega.data.IDOFactory implements Poll_optionHome
{
 protected Class getEntityInterfaceClass(){
  return Poll_option.class;
 }

 public Poll_option create() throws javax.ejb.CreateException{
  return (Poll_option) super.idoCreate();
 }

 public Poll_option createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Poll_option findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Poll_option) super.idoFindByPrimaryKey(id);
 }

 public Poll_option findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Poll_option) super.idoFindByPrimaryKey(pk);
 }

 public Poll_option findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}