package is.idega.block.nationalregister.data;


public interface NationalRegisterHome extends com.idega.data.IDOHome
{
 public NationalRegister create() throws javax.ejb.CreateException;
 public NationalRegister findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findAllBySSN(java.lang.String p0)throws javax.ejb.FinderException,java.rmi.RemoteException;

}