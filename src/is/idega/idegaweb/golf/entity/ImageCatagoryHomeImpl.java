package is.idega.idegaweb.golf.entity;


public class ImageCatagoryHomeImpl extends com.idega.data.IDOFactory implements ImageCatagoryHome
{
 protected Class getEntityInterfaceClass(){
  return ImageCatagory.class;
 }

 public ImageCatagory create() throws javax.ejb.CreateException{
  return (ImageCatagory) super.idoCreate();
 }

 public ImageCatagory createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ImageCatagory findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ImageCatagory) super.idoFindByPrimaryKey(id);
 }

 public ImageCatagory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ImageCatagory) super.idoFindByPrimaryKey(pk);
 }

 public ImageCatagory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}