package is.idega.block.nationalregister.business;

import com.idega.business.IBOService;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jul 29, 2004
 */
public interface FamilyLogic extends IBOService {
	
	public void setAsChildFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
	 public void setAsCohabitantFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
	 public void setAsCustodianFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
	 public void setAsParentFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
	 public void setAsSiblingFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
	 public void setAsSpouseFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;

}
