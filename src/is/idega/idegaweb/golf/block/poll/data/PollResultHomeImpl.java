package is.idega.idegaweb.golf.block.poll.data;


public class PollResultHomeImpl extends com.idega.data.IDOFactory implements PollResultHome
{
 protected Class getEntityInterfaceClass(){
  return PollResult.class;
 }


 public PollResult create() throws javax.ejb.CreateException{
  return (PollResult) super.createIDO();
 }


 public PollResult createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public PollResult findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PollResult) super.findByPrimaryKeyIDO(pk);
 }


 public PollResult findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (PollResult) super.findByPrimaryKeyIDO(id);
 }


 public PollResult findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}