package is.idega.idegaweb.golf.block.poll.data;


public class PollAttributesHomeImpl extends com.idega.data.IDOFactory implements PollAttributesHome
{
 protected Class getEntityInterfaceClass(){
  return PollAttributes.class;
 }


 public PollAttributes create() throws javax.ejb.CreateException{
  return (PollAttributes) super.createIDO();
 }


 public PollAttributes createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public PollAttributes findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PollAttributes) super.findByPrimaryKeyIDO(pk);
 }


 public PollAttributes findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (PollAttributes) super.findByPrimaryKeyIDO(id);
 }


 public PollAttributes findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}