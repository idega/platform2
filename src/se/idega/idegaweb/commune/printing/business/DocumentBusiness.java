package se.idega.idegaweb.commune.printing.business;


public interface DocumentBusiness extends com.idega.business.IBOService
{
 public void createAddressContent(java.lang.String p0,com.lowagie.text.pdf.PdfWriter p1)throws com.lowagie.text.DocumentException, java.rmi.RemoteException;
 public void createArchiveMessageContent(se.idega.idegaweb.commune.printing.business.DocumentPrintContext p0)throws se.idega.idegaweb.commune.printing.business.ContentCreationException, java.rmi.RemoteException;
 public void createCommuneFooter(com.lowagie.text.pdf.PdfWriter p0)throws java.lang.Exception, java.rmi.RemoteException;
 public int createDefaultLetterHeader(com.lowagie.text.Document p0,java.lang.String p1,com.lowagie.text.pdf.PdfWriter p2)throws java.lang.Exception, java.rmi.RemoteException;
 public void createLogoContent(com.lowagie.text.Document p0)throws com.lowagie.text.BadElementException,java.net.MalformedURLException,java.io.IOException,com.lowagie.text.DocumentException, java.rmi.RemoteException;
 public void createMessageContent(se.idega.idegaweb.commune.printing.business.DocumentPrintContext p0)throws se.idega.idegaweb.commune.printing.business.ContentCreationException, java.rmi.RemoteException;
 public void createNewlinesContent(com.lowagie.text.Document p0)throws com.lowagie.text.DocumentException, java.rmi.RemoteException;
 public java.lang.String getAddressString(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public java.lang.String getDefaultXMLTemplateValue() throws java.rmi.RemoteException;
 public java.util.HashMap getMessageTagMap(se.idega.idegaweb.commune.message.data.PrintMessage p0,java.util.Locale p1) throws java.rmi.RemoteException;
 public java.lang.String[] getPrintMessageTypes() throws java.rmi.RemoteException;
 public java.util.Collection getPrintedDocuments()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getPrintedDocuments(java.lang.String p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getPrintedDocuments(java.lang.String p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.PrintedLetterMessageHome getPrintedLetterMessageHome() throws java.rmi.RemoteException;
 public int getPrintedLettersCountByStatusAndType(java.lang.String p0,java.lang.String p1) throws java.rmi.RemoteException;
 public int getPrintedLettersCountByType(java.lang.String p0) throws java.rmi.RemoteException;
 public java.util.Collection getPrintedMessages(java.lang.String p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getPrintedMessages(java.lang.String p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getPrintedMessagesByPrimaryKeys(java.lang.String[] p0,java.lang.String p1)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.SystemArchivationMessageHome getSystemArchivationMessageHome() throws java.rmi.RemoteException;
 public int getUnPrintedDefaultLettersCount() throws java.rmi.RemoteException;
 public int[] getUnPrintedLettersIDs(java.lang.String p0) throws java.rmi.RemoteException;
 public java.util.Collection getUnPrintedMessages(java.lang.String p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getUnPrintedMessages(java.lang.String p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public int getUnPrintedPasswordLettersCount() throws java.rmi.RemoteException;
 public int getUnprintedLettersCountByType(java.lang.String p0) throws java.rmi.RemoteException;
 public int getUnprintedMessagesCountByType(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getXMLLetterUrl(com.idega.idegaweb.IWBundle p0,java.util.Locale p1,java.lang.String p2) throws java.rmi.RemoteException;
 public java.lang.String getXMLLetterUrl(com.idega.idegaweb.IWBundle p0,java.util.Locale p1,java.lang.String p2,boolean p3) throws java.rmi.RemoteException;
 public boolean isBulkLetterType(java.lang.String p0) throws java.rmi.RemoteException;
 public boolean isTypeSystemArchiveMessage(java.lang.String p0) throws java.rmi.RemoteException;
 public int writeBulkPDF(java.lang.String[] p0,com.idega.user.data.User p1,java.lang.String p2,java.util.Locale p3,java.lang.String p4,boolean p5,boolean p6,boolean p7)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public int writeBulkPDF(java.util.Collection p0,com.idega.user.data.User p1,java.lang.String p2,java.util.Locale p3,java.lang.String p4,boolean p5,boolean p6,boolean p7) throws java.rmi.RemoteException;
 public int writePDF(se.idega.idegaweb.commune.message.data.PrintMessage p0,com.idega.user.data.User p1,java.lang.String p2,java.util.Locale p3,boolean p4)throws java.lang.Exception, java.rmi.RemoteException;
 public float getPointsFromMM(float millimeters) ;
 public void createHeaderDate(com.lowagie.text.Document document,com.lowagie.text.pdf.PdfWriter writer,String dateString)throws com.lowagie.text.DocumentException;
}