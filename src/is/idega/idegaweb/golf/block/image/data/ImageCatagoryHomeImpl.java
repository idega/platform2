package is.idega.idegaweb.golf.block.image.data;


public class ImageCatagoryHomeImpl extends com.idega.data.IDOFactory implements ImageCatagoryHome
{
 protected Class getEntityInterfaceClass(){
  return ImageCatagory.class;
 }


 public ImageCatagory create() throws javax.ejb.CreateException{
  return (ImageCatagory) super.createIDO();
 }


 public ImageCatagory createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public ImageCatagory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ImageCatagory) super.findByPrimaryKeyIDO(pk);
 }


 public ImageCatagory findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ImageCatagory) super.findByPrimaryKeyIDO(id);
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