package is.idega.idegaweb.golf.block.poll.data;


public class PollOptionHomeImpl extends com.idega.data.IDOFactory implements PollOptionHome
{
 protected Class getEntityInterfaceClass(){
  return PollOption.class;
 }


 public PollOption create() throws javax.ejb.CreateException{
  return (PollOption) super.createIDO();
 }


 public PollOption createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public PollOption findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PollOption) super.findByPrimaryKeyIDO(pk);
 }


 public PollOption findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (PollOption) super.findByPrimaryKeyIDO(id);
 }


 public PollOption findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}