package se.idega.idegaweb.commune.message.data;


public interface PrintedLetterMessageHome extends PrintMessageHome
{
 public java.util.Collection findAllLettersBySchool(int p0,java.lang.String p1,java.lang.String p2,com.idega.util.IWTimestamp p3,com.idega.util.IWTimestamp p4)throws javax.ejb.FinderException;
 public java.util.Collection findAllPrintedLetters()throws javax.ejb.FinderException;
 public java.util.Collection findAllUnPrintedLetters()throws javax.ejb.FinderException;
 public java.util.Collection findByBulkFile(int p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.FinderException;
 public java.util.Collection findLetters(java.lang.String[] p0)throws javax.ejb.FinderException;
 public java.util.Collection findLettersByChildcare(int p0,java.lang.String p1,java.lang.String p2,com.idega.util.IWTimestamp p3,com.idega.util.IWTimestamp p4)throws javax.ejb.FinderException;
 public java.util.Collection findMessages(com.idega.user.data.User p0)throws javax.ejb.FinderException;
 public java.util.Collection findMessagesByStatus(com.idega.user.data.User p0,java.lang.String[] p1)throws javax.ejb.FinderException;
 public java.util.Collection findPrintedDefaultLetters()throws javax.ejb.FinderException;
 public java.util.Collection findPrintedLettersByType(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findPrintedLettersByType(java.lang.String p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException;
 public java.util.Collection findPrintedPasswordLetters()throws javax.ejb.FinderException;
 public java.util.Collection findSingleByTypeAndStatus(java.lang.String p0,java.lang.String p1,com.idega.util.IWTimestamp p2,com.idega.util.IWTimestamp p3)throws javax.ejb.FinderException;
 public java.util.Collection findSinglePrintedLettersByType(java.lang.String p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException;
 public java.util.Collection findSingleUnPrintedLettersByType(java.lang.String p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException;
 public java.util.Collection findUnPrintedDefaultLetters()throws javax.ejb.FinderException;
 public java.util.Collection findUnPrintedLettersByType(java.lang.String p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException;
 public java.util.Collection findUnPrintedLettersByType(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findUnPrintedPasswordLetters()throws javax.ejb.FinderException;
 public java.lang.String[] getLetterTypes();
 public int getNumberOfLettersByStatusAndType(java.lang.String p0,java.lang.String p1);
 public int getNumberOfPrintedDefaultLetters();
 public int getNumberOfPrintedLettersByType(java.lang.String p0);
 public int getNumberOfPrintedPasswordLetters();
 public int getNumberOfUnPrintedDefaultLetters();
 public int getNumberOfUnPrintedPasswordLetters();
 public int getNumberOfUnprintedLettersByType(java.lang.String p0);
 public java.lang.String[] getPrintMessageTypes();

}