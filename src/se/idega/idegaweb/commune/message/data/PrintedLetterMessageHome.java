package se.idega.idegaweb.commune.message.data;

import com.idega.util.IWTimestamp;


public interface PrintedLetterMessageHome extends MessageHome
{
 //public PrintedLetterMessage create() throws javax.ejb.CreateException;
 //public PrintedLetterMessage findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findPrintedDefaultLetters()throws javax.ejb.FinderException;
 public java.util.Collection findAllUnPrintedLetters()throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findAllPrintedLetters()throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findUnPrintedPasswordLetters()throws javax.ejb.FinderException;
 public java.util.Collection findPrintedPasswordLetters()throws javax.ejb.FinderException;
 public java.util.Collection findMessages(com.idega.user.data.User p0)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findUnPrintedDefaultLetters()throws javax.ejb.FinderException;
 public java.util.Collection findPrintedLettersByType(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findUnPrintedLettersByType(java.lang.String p0)throws javax.ejb.FinderException;
  public java.util.Collection findPrintedLettersByType(java.lang.String p0,IWTimestamp from, IWTimestamp to)throws javax.ejb.FinderException;
 public java.util.Collection findUnPrintedLettersByType(java.lang.String p0,IWTimestamp from, IWTimestamp to)throws javax.ejb.FinderException;
 public int getNumberOfPrintedDefaultLetters();
 public int getNumberOfUnPrintedDefaultLetters();
 public int getNumberOfPrintedPasswordLetters();
 public int getNumberOfUnPrintedPasswordLetters();
 public int getNumberOfLettersByStatusAndType(String caseStatus,String LetterType);
 public int getNumberOfUnprintedLettersByType(String letterType);
 public int getNumberOfPrintedLettersByType(String letterType);
 public String[] getLetterTypes();

}