package is.idega.idegaweb.golf.entity;


public class CountryHomeImpl extends com.idega.data.IDOFactory implements CountryHome
{
 protected Class getEntityInterfaceClass(){
  return Country.class;
 }


 public Country create() throws javax.ejb.CreateException{
  return (Country) super.createIDO();
 }


 public Country createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


public Country findByAbbreviation(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((CountryBMPBean)entity).ejbFindByAbbreviation(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public Country findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Country) super.findByPrimaryKeyIDO(pk);
 }


 public Country findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Country) super.findByPrimaryKeyIDO(id);
 }


 public Country findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}