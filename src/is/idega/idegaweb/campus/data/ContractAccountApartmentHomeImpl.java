package is.idega.idegaweb.campus.data;


public class ContractAccountApartmentHomeImpl extends com.idega.data.IDOFactory implements ContractAccountApartmentHome
{
 protected Class getEntityInterfaceClass(){
  return ContractAccountApartment.class;
 }

 public ContractAccountApartment create() throws javax.ejb.CreateException{
  return (ContractAccountApartment) super.idoCreate();
 }

 public ContractAccountApartment createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ContractAccountApartment findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ContractAccountApartment) super.idoFindByPrimaryKey(id);
 }

 public ContractAccountApartment findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ContractAccountApartment) super.idoFindByPrimaryKey(pk);
 }

 public ContractAccountApartment findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}