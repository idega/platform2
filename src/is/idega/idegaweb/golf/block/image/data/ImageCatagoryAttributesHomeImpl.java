package is.idega.idegaweb.golf.block.image.data;


public class ImageCatagoryAttributesHomeImpl extends com.idega.data.IDOFactory implements ImageCatagoryAttributesHome
{
 protected Class getEntityInterfaceClass(){
  return ImageCatagoryAttributes.class;
 }


 public ImageCatagoryAttributes create() throws javax.ejb.CreateException{
  return (ImageCatagoryAttributes) super.createIDO();
 }


 public ImageCatagoryAttributes createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public ImageCatagoryAttributes findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ImageCatagoryAttributes) super.findByPrimaryKeyIDO(pk);
 }


 public ImageCatagoryAttributes findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ImageCatagoryAttributes) super.findByPrimaryKeyIDO(id);
 }


 public ImageCatagoryAttributes findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}