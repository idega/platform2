package is.idega.idegaweb.campus.data;


public class ContractAccountsHomeImpl extends com.idega.data.IDOFactory implements ContractAccountsHome
{
 protected Class getEntityInterfaceClass(){
  return ContractAccounts.class;
 }

 public ContractAccounts create() throws javax.ejb.CreateException{
  return (ContractAccounts) super.idoCreate();
 }

 public ContractAccounts createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ContractAccounts findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ContractAccounts) super.idoFindByPrimaryKey(id);
 }

 public ContractAccounts findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ContractAccounts) super.idoFindByPrimaryKey(pk);
 }

 public ContractAccounts findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}