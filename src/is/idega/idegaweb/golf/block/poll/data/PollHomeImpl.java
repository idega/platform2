package is.idega.idegaweb.golf.block.poll.data;


public class PollHomeImpl extends com.idega.data.IDOFactory implements PollHome
{
 protected Class getEntityInterfaceClass(){
  return Poll.class;
 }


 public Poll create() throws javax.ejb.CreateException{
  return (Poll) super.createIDO();
 }


 public Poll createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public Poll findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Poll) super.findByPrimaryKeyIDO(pk);
 }


 public Poll findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Poll) super.findByPrimaryKeyIDO(id);
 }


 public Poll findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}