/*
 * $Id: DocumentServiceBean.java,v 1.1 2004/11/04 20:34:48 aron Exp $
 * Created on 15.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.printing.business;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.data.MessageConstants;
import se.idega.idegaweb.commune.message.data.PrintMessage;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessage;
import se.idega.idegaweb.commune.message.data.SystemArchivationMessage;
import se.idega.idegaweb.commune.printing.data.PrintDocuments;
import se.idega.idegaweb.commune.printing.data.PrintDocumentsHome;

import com.idega.block.pdf.business.PrintingContext;
import com.idega.block.pdf.business.PrintingService;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.idegaweb.IWUserContext;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;

/**
 * 
 *  Last modified: $Date: 2004/11/04 20:34:48 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class DocumentServiceBean extends IBOServiceBean  implements DocumentService{
    
    /**
     * Creates a pdf letter from a template which is chosen from the message type.
     * Returns a primaryKey to a file in database
     */
    public Integer createPDF(IWUserContext iwuc,PrintMessage msg, String fileName, boolean flagPrinted)throws ContentCreationException{
        try {
            MemoryFileBuffer buffer = new MemoryFileBuffer();
            OutputStream mos = new MemoryOutputStream(buffer);
            InputStream mis = new MemoryInputStream(buffer);
           
            PrintingContext pcx = getPrintingContext(iwuc,msg);
            pcx.setDocumentStream(mos);
            getPrintingService().printDocument(pcx);
            return storeStreamToICFile(iwuc,getMessageService(),msg,mis,fileName,buffer.length(),flagPrinted);
            
        } catch (Exception e) {
            throw new ContentCreationException(e);
        } 
    }
    
   
    private Integer storeStreamToICFile(IWUserContext iwuc,MessageBusiness msgServ,PrintMessage msg,InputStream is,String fileName,int lengthOfFile,boolean flagPrinted) {
        try {
            ICFile file = createFile(null,fileName,is,lengthOfFile);
            msgServ.setMessageFile(msg,flagPrinted,iwuc.getCurrentUser(),file);
            return (Integer)file.getPrimaryKey();
        } catch (Exception e){
            throw new IBORuntimeException(e);
        }
    }
    
    public Integer createPDF(IWUserContext iwuc,String[] ids, String type, String fileName,boolean flagPrinted){
        try {
            Collection msgs  = getBusiness().getPrintedMessagesByPrimaryKeys(ids,type);
            return createPDF(iwuc,msgs,type,fileName,flagPrinted);
        } catch (Exception e) {
           throw new ContentCreationException(e);
        }
    }
    
    /**
     * Creates a pdf letter from a template which is chosen from the message type.
     * Returns a primaryKey to a file in database
     */
    public Integer createPDF(IWUserContext iwuc,Collection msgs,String type, String fileName,boolean flagPrinted){
        OutputStream outerOs = null;
        InputStream outerIs = null;
        try {
            MemoryFileBuffer outerBuf = new MemoryFileBuffer();
            outerOs = new MemoryOutputStream(outerBuf);
            outerIs = new MemoryInputStream(outerBuf);
            
            //
            // step 1: creation of a document-object
            Document document = new Document();
            // step 2: we create a writer that listens to the document
            PdfCopy writer = new PdfCopy(document, outerOs);
            // step 3: we open the document
            document.open();
            
            ICFile bulkFile = getICFileHome().create();
			bulkFile.store();
            
            PrintingService pserv = getPrintingService();
            
            MessageBusiness msgBuiz = getMessageService();
            int lettersProcessed = 0;
            for (Iterator iter = msgs.iterator(); iter.hasNext();) {
               PrintMessage msg = (PrintMessage) iter.next();
               MemoryFileBuffer buffer = new MemoryFileBuffer();
               OutputStream mos = new MemoryOutputStream(buffer);
               InputStream mis = new MemoryInputStream(buffer);
               
               PrintingContext pcx = getPrintingContext(iwuc,msg);
               pcx.setDocumentStream(mos);
               pserv.printDocument(pcx);
               
               PdfReader reader = new PdfReader(buffer.buffer());
               PdfImportedPage page;
               int n = reader.getNumberOfPages();
               for (int i = 0; i < n; ) {
                   ++i;
                   page = writer.getImportedPage(reader, i);
                   writer.addPage(page);
                   System.out.println("Processed page " + i);
               }
               lettersProcessed++;
               storeStreamToICFile(iwuc,msgBuiz,msg,mis,fileName,buffer.length(),flagPrinted);
               msg.setMessageBulkData(bulkFile);
               msg.store();
            }
            document.close();
            bulkFile = createFile(bulkFile,fileName,outerIs,outerBuf.length());
            
            PrintDocuments pdocs = getPrintDocumentsHome().create();
			pdocs.setDocument(bulkFile);
			pdocs.setNumberOfSubDocuments(lettersProcessed);
			pdocs.setCreator(iwuc.getCurrentUser());
			pdocs.setType(type);
			pdocs.store();
           
            return (Integer)bulkFile.getPrimaryKey();
        } catch (Exception e) {
            throw new ContentCreationException(e);
        } 
        finally{
            try {
				outerOs.close();
				outerIs.close();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
        }
    }
    
    public PrintingContext getPrintingContext(IWUserContext iwuc,PrintMessage msg){  
		if (msg instanceof PrintedLetterMessage) {
			PrintedLetterMessage pmsg = (PrintedLetterMessage) msg;
			if (pmsg.getLetterType().equals(MessageConstants.LETTER_TYPE_PASSWORD))
				return new PasswordLetterContext(iwuc,msg);
			else if (pmsg.getLetterType().equals(MessageConstants.LETTER_TYPE_DEFAULT)) 
			    return new DefaultLetterContext(iwuc,msg);
		}
		else if (msg instanceof SystemArchivationMessage) {
			return new ArchiveLetterContext(iwuc,msg);
		}
        return new MessageLetterContext(iwuc,msg);
    }

    
    private ICFile createFile(ICFile file,String fileName,InputStream is,int length) throws CreateException{
        if(file==null)
            file = getICFileHome().create();
        
        if(!fileName.endsWith(".pdf") &&  !fileName.endsWith(".PDF"))
		    fileName +=".pdf";
        
        createBackup(fileName,is);
        
        file.setFileValue(is);
		file.setMimeType("application/x-pdf");
		
		file.setName(fileName );
		file.setFileSize(length);
		file.store();
		return file;
    }
    
    private void createBackup(String fileName,InputStream mis){
       
		
		try {
            /* *** writing pdf to cachefolder manually*/
            String folder = getIWApplicationContext().getIWMainApplication().getRealPath(getIWApplicationContext().getIWMainApplication().getCacheDirectoryURI()+"/prints");
            java.io.File tfile = com.idega.util.FileUtil.getFileAndCreateIfNotExists(folder,fileName);
            java.io.FileOutputStream fos = new java.io.FileOutputStream(tfile);
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            while (mis.available() > 0) {
            	baos.write(mis.read());
            }
            baos.writeTo(fos);
            baos.flush();
            	baos.close();
            	mis.reset();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    public void rugl(){
    int f = 0;
    String outFile = args[args.length-1];
    Document document = null;
    PdfCopy  writer = null;
    while (f < args.length-1) {
        // we create a reader for a certain document
        PdfReader reader = new PdfReader(args[f]);
        reader.consolidateNamedDestinations();
        // we retrieve the total number of pages
        int n = reader.getNumberOfPages();
        List bookmarks = SimpleBookmark.getBookmark(reader);
        if (bookmarks != null) {
            if (pageOffset != 0)
                SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
            master.addAll(bookmarks);
        }
        pageOffset += n;
        System.out.println("There are " + n + " pages in " + args[f]);
        
        if (f == 0) {
            // step 1: creation of a document-object
            document = new Document(reader.getPageSizeWithRotation(1));
            // step 2: we create a writer that listens to the document
            writer = new PdfCopy(document, new FileOutputStream(outFile));
            // step 3: we open the document
            document.open();
        }
        // step 4: we add content
        PdfImportedPage page;
        for (int i = 0; i < n; ) {
            ++i;
            page = writer.getImportedPage(reader, i);
            writer.addPage(page);
            System.out.println("Processed page " + i);
        }
        PRAcroForm form = reader.getAcroForm();
        if (form != null)
            writer.copyAcroForm(reader);
        f++;
    }
    if (master.size() > 0)
        writer.setOutlines(master);
    // step 5: we close the document
    document.close();
	}
   
    */
    protected ICFileHome getICFileHome() {
		try {
			return (ICFileHome) getIDOHome(ICFile.class);
		}
		catch (Exception e) {
			throw new IBORuntimeException(e);
		}
	}
    
    protected PrintDocumentsHome getPrintDocumentsHome() {
		try {
			return (PrintDocumentsHome) getIDOHome(PrintDocuments.class);
		}
		catch (Exception e) {
			throw new IBORuntimeException(e);
		}
	}
    

    public PrintingService getPrintingService()throws IBOLookupException{
        return (PrintingService)getServiceInstance(PrintingService.class);
    }
    
    private DocumentBusiness getBusiness()throws IBOLookupException{
        return (DocumentBusiness)getServiceInstance(DocumentBusiness.class);
    }
    
    public MessageBusiness getMessageService()throws IBOLookupException {
        return (MessageBusiness) getServiceInstance(MessageBusiness.class);
	}
    
    private CommuneUserBusiness getUserService() throws IBOLookupException{
        return (CommuneUserBusiness) getServiceInstance(CommuneUserBusiness.class);
        
    }
}
