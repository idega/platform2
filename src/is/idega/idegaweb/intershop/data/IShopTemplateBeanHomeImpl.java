package is.idega.idegaweb.intershop.data;


public class IShopTemplateBeanHomeImpl extends com.idega.data.IDOFactory implements IShopTemplateBeanHome
{
 protected Class getEntityInterfaceClass(){
  return IShopTemplateBean.class;
 }

 public IShopTemplateBean create() throws javax.ejb.CreateException{
  return (IShopTemplateBean) super.idoCreate();
 }

 public IShopTemplateBean createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public IShopTemplateBean findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (IShopTemplateBean) super.idoFindByPrimaryKey(id);
 }

 public IShopTemplateBean findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (IShopTemplateBean) super.idoFindByPrimaryKey(pk);
 }

 public IShopTemplateBean findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}