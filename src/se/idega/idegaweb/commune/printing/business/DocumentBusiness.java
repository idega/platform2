package se.idega.idegaweb.commune.printing.business;

import javax.ejb.*;

public interface DocumentBusiness extends com.idega.business.IBOService
{
 public int getUnPrintedLettersCount()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getPrintedDocuments()throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public int printAllUnPrintedLetters(int p0) throws java.rmi.RemoteException;
 public int writePrintedLetterPDF(int p0,int p1) throws java.rmi.RemoteException;
 public int writePrintedLetterPDF(int[] p0,int p1,java.lang.String p2,com.lowagie.text.Font p3,com.lowagie.text.Font p4,com.lowagie.text.Font p5,com.lowagie.text.Font p6) throws java.rmi.RemoteException;
 public int[] getUnPrintedLettersIDs() throws java.rmi.RemoteException;
 public int writePrintedLetterPDF(int[] p0,int p1) throws java.rmi.RemoteException;
}
