package is.idega.idegaweb.golf.block.file.data;


public class FileEntityHomeImpl extends com.idega.data.IDOFactory implements FileEntityHome
{
 protected Class getEntityInterfaceClass(){
  return FileEntity.class;
 }


 public FileEntity create() throws javax.ejb.CreateException{
  return (FileEntity) super.createIDO();
 }


 public FileEntity createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public FileEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (FileEntity) super.findByPrimaryKeyIDO(pk);
 }


 public FileEntity findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (FileEntity) super.findByPrimaryKeyIDO(id);
 }


 public FileEntity findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}