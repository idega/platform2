package is.idega.block.nationalregister.business;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


public interface NationalRegisterBusiness extends com.idega.business.IBOService
{
 public is.idega.block.nationalregister.data.NationalRegister getEntryBySSN(java.lang.String p0) throws java.rmi.RemoteException;
 public void updateUserAddress(User user, UserBusiness userBiz, String address, String po) throws RemoteException, CreateException;
 public boolean updateEntry(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.lang.String p8,java.lang.String p9,java.lang.String p10,java.lang.String p11,java.lang.String p12,java.lang.String p13,java.lang.String p14,java.lang.String p15,java.lang.String p16,java.lang.String p17,java.lang.String p18,java.lang.String p19,java.lang.String p20,java.lang.String p21,java.lang.String p22,java.lang.String p23,java.lang.String p24,java.lang.String p25,java.lang.String p26,java.lang.String p27,java.lang.String p28,java.lang.String p29,java.lang.String p30, Group citizenGroup) throws java.rmi.RemoteException;
 //public boolean updateEntry(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.lang.String p8,java.lang.String p9,java.lang.String p10,java.lang.String p11,java.lang.String p12,java.lang.String p13,java.lang.String p14,java.lang.String p15,java.lang.String p16,java.lang.String p17,java.lang.String p18,java.lang.String p19)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
