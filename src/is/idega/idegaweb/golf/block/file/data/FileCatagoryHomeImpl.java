package is.idega.idegaweb.golf.block.file.data;


public class FileCatagoryHomeImpl extends com.idega.data.IDOFactory implements FileCatagoryHome
{
 protected Class getEntityInterfaceClass(){
  return FileCatagory.class;
 }


 public FileCatagory create() throws javax.ejb.CreateException{
  return (FileCatagory) super.createIDO();
 }


 public FileCatagory createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public FileCatagory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (FileCatagory) super.findByPrimaryKeyIDO(pk);
 }


 public FileCatagory findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (FileCatagory) super.findByPrimaryKeyIDO(id);
 }


 public FileCatagory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}