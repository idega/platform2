package se.idega.idegaweb.commune.printing.business;

import javax.ejb.*;

public interface DocumentBusiness extends com.idega.business.IBOService
{
 public int createContent(com.lowagie.text.Document p0,se.idega.idegaweb.commune.message.data.PrintedLetterMessage p1,com.idega.user.data.User p2,com.lowagie.text.pdf.PdfWriter p3)throws java.lang.Exception, java.rmi.RemoteException;
 public java.util.Collection getPrintedDocuments()throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getPrintedDocuments(java.lang.String p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.PrintedLetterMessageHome getPrintedLetterMessageHome() throws java.rmi.RemoteException;
 public java.lang.String[] getPrintedLetterTypes()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getPrintedLettersCountByStatusAndType(java.lang.String p0,java.lang.String p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getPrintedLettersCountByType(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getUnPrintedDefaultLettersCount()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int[] getUnPrintedLettersIDs(java.lang.String p0) throws java.rmi.RemoteException;
 public int getUnPrintedPasswordLettersCount()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getUnprintedLettersCountByType(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean isBulkLetterType(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int printAllUnPrintedLetters(int p0,java.lang.String p1) throws java.rmi.RemoteException;
 public void writeBulkPDF(java.util.Collection p0,com.idega.user.data.User p1,java.lang.String p2) throws java.rmi.RemoteException;
 public int writePDF(se.idega.idegaweb.commune.message.data.PrintedLetterMessage p0,com.idega.user.data.User p1,java.lang.String p2)throws java.lang.Exception, java.rmi.RemoteException;
 public int writePrintedLetterPDF(int p0,int p1) throws java.rmi.RemoteException;
 public int writePrintedLetterPDF(int[] p0,int p1) throws java.rmi.RemoteException;
 public int writePrintedLetterPDF(int[] p0,int p1,java.lang.String p2,com.lowagie.text.Font p3,com.lowagie.text.Font p4,com.lowagie.text.Font p5,com.lowagie.text.Font p6) throws java.rmi.RemoteException;
}
