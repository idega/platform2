package se.idega.idegaweb.commune.message.data;

import java.util.Collection;
import javax.ejb.FinderException;


public interface PrintedLetterMessageHome extends com.idega.data.IDOHome,MessageHome
{
  public Collection findAllUnPrintedLetters()throws FinderException,java.rmi.RemoteException;
  public Collection findAllPrintedLetters() throws FinderException,java.rmi.RemoteException;
  public int getNumberOfPrintedLetters() throws java.rmi.RemoteException;
  public int getNumberOfUnPrintedLetters() throws java.rmi.RemoteException;
}