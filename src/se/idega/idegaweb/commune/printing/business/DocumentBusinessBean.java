/*
 * $Id: MessageBusinessBean.java,v 1.16 2002/10/08 22:16:29 tryggvil Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.printing.business;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.ejb.FinderException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.business.NoUserAddressException;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.business.MessagePdfHandler;
import se.idega.idegaweb.commune.message.data.MessageHandlerInfo;
import se.idega.idegaweb.commune.message.data.MessageHandlerInfoHome;
import se.idega.idegaweb.commune.message.data.PrintMessage;
import se.idega.idegaweb.commune.message.data.PrintMessageHome;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessage;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessageHome;
import se.idega.idegaweb.commune.message.data.SystemArchivationMessage;
import se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean;
import se.idega.idegaweb.commune.message.data.SystemArchivationMessageHome;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.printing.data.PrintDocuments;
import se.idega.idegaweb.commune.printing.data.PrintDocumentsHome;

import com.idega.block.process.data.Case;
import com.idega.business.IBORuntimeException;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Country;
import com.idega.idegaweb.IWBundle;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.user.data.User;
import com.idega.util.IWColor;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLOutput;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ElementTags;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.markup.MarkupTags;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.xml.SAXmyHandler;
import com.lowagie.text.xml.XmlPeer;

/**
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson </a>
 * @author <a href="mailto:aron@idega.is">Aron Birkir </a>
 * @version 1.0
 * 
 * Scale dimension of the logo and the position of it can be set with bundle
 * properties in the commune bundle. (Use BundlePropertySetter)
 * 
 * The position of the address box can also be set with bundle properties.
 * 
 * The properties concerning these settings are all prefixed with "printing.".
 * 
 * To activate the bundle property changes you need to set the bundle property
 * "printing.dimension_refresh" to "true";
 * 
 * Every measure is in millimeters(mm). X coordinates are measured from the left
 * side of the page(paper). y coordinates are measured from bottom of the page
 * 
 * The address box is specified with its lower left corner coordinates and upper
 * right corner coordinates
 */
public class DocumentBusinessBean extends com.idega.business.IBOServiceBean implements DocumentBusiness {

	//private Font defaultTitleFont;
	private static Font defaultParagraphFont;
	//private Font defaultTagFont;
	private static Font defaultTextFont;

	private static Font defaultAddressTextFont;
	//private boolean oldstyle = false;
	private static final int ADDRESS_ERROR = -5;

	/////////////////////////////////
	// defined in millimeters:
	private static int logoScaleHeight = 15;
	private static int logoScaleWidth = 30;
	private static int logoAbsPosX = 25;
	private static int logoAbsPosY = 267;
	private static int addressLowerLeftX = 20 + 95;
	private static int addressLowerLeftY = 231;
	private static int addressUpperRightX = 193;
	private static int addressUpperRightY = 257;
	private static int newlinesBetween = 8;
	private static boolean initRefresh = true;

	//////////////////////////////

	/**
	 * Method getDefaultTextFont.
	 * 
	 * @return Font
	 */
	private Font getDefaultTextFont() {
		if (defaultTextFont == null) {
			defaultTextFont = new Font(Font.HELVETICA);
			defaultTextFont.setSize(12);
		}
		return defaultTextFont;
	}

	private Font getDefaultAddressTextFont() {
		if (defaultAddressTextFont == null) {
			defaultAddressTextFont = new Font(Font.HELVETICA);
			defaultAddressTextFont.setSize(12);
		}
		return defaultAddressTextFont;
	}

	/*
	 * Commented out since it is never used... private Font getDefaultTagFont() {
	 * if (defaultTagFont == null) { defaultTagFont = new Font(Font.HELVETICA);
	 * defaultTagFont.setSize(12); } return defaultTagFont; }
	 */

	/**
	 * Method getDefaultParagraphFont.
	 * 
	 * @return Font
	 */
	private Font getDefaultParagraphFont() {
		if (defaultParagraphFont == null) {
			defaultParagraphFont = new Font(Font.HELVETICA, 12, Font.BOLD);
		}
		return defaultParagraphFont;
	}

	/*
	 * Commented out since it is never used... private Font getDefaultTitleFont() {
	 * if (defaultTitleFont == null) { defaultTitleFont = new
	 * Font(Font.HELVETICA); defaultTitleFont.setSize(12); } return
	 * defaultTitleFont; }
	 */

	private Font getTextFont() {
		return getDefaultTextFont();
	}

	private Font getAddressFont() {
		return getDefaultAddressTextFont();
	}

	/*
	 * Commented out since it is never used... private Font
	 * getTagFont(PrintedLetterMessage msg) { return getDefaultTagFont(); }
	 */

	/*
	 * Commented out since it is never used... private Font
	 * getParagraphFont(PrintedLetterMessage msg) { return
	 * getDefaultParagraphFont(); }
	 */

	/*
	 * Commented out since it is never used... private Font
	 * getTitleFont(PrintedLetterMessage msg) { return getDefaultTitleFont(); }
	 */

	public String getAddressString(User user) {
		StringBuffer addrString = new StringBuffer();
		try {

			addrString.append(user.getName());
			addrString.append("\n");
			Address addr = getSnailMailAddress(user);
			if (addr != null) {
				addrString.append(addr.getStreetAddress());
				addrString.append("\n");
				addrString.append(addr.getPostalAddress());
				addrString.append("\n");
				if (addr.getCountryId() != -1) {
					Country country = addr.getCountry();
					if (!country.getIsoAbbreviation().equalsIgnoreCase("se")) {
						// see scarab issue nasc58
						addrString.append(addr.getCountry().getName());
						addrString.append("\n");
					}
				}
			}

		}
		catch (Exception e) {
			throw new IBORuntimeException(e);
		}
		return addrString.toString();
	}

	/**
     * @param user
     * @return
	 * @throws NoUserAddressException
	 * @throws RemoteException
     * @throws RemoteException
     * @throws NoUserAddressException
     */
    private Address getSnailMailAddress(User user) throws RemoteException, NoUserAddressException  {
        return getUserBusiness().getSnailMailAddress(user);
    }

    public Collection getPrintedDocuments() throws FinderException {
		return getPrintDocumentsHome().findAllPrintedLetterDocuments();
	}

	public Collection getPrintedDocuments(String type) throws FinderException {
		return getPrintDocumentsHome().findAllDocumentByType(type);
	}

	public Collection getPrintedDocuments(String type, IWTimestamp from, IWTimestamp to) throws FinderException {
		return getPrintDocumentsHome().findAllDocumentByType(type, from, to);
	}

	public Collection getPrintedMessages(String type) throws FinderException {
		if (isTypeSystemArchiveMessage(type)) return getSystemArchivationMessageHome().findPrintedMessages();
		return getPrintedLetterMessageHome().findPrintedLettersByType(type);
	}

	public Collection getPrintedMessagesByPrimaryKeys(String[] primaryKeys, String type) throws FinderException {
		PrintMessageHome msgHome = null;
		PrintMessage msg;
		ArrayList coll = new ArrayList(primaryKeys.length);
		if (isTypeSystemArchiveMessage(type)) msgHome = getSystemArchivationMessageHome();
		msgHome = getPrintedLetterMessageHome();
		if (msgHome != null) {
			for (int i = 0; i < primaryKeys.length; i++) {
				msg = (PrintMessage) msgHome.findByPrimaryKey(primaryKeys[i]);
				coll.add(msg);
			}
		}
		return coll;
	}

	public Collection getPrintedMessages(String type, IWTimestamp from, IWTimestamp to) throws FinderException {
		if (isTypeSystemArchiveMessage(type)) return getSystemArchivationMessageHome().findPrintedMessages(from, to);
		return getPrintedLetterMessageHome().findPrintedLettersByType(type, from, to);
	}

	public Collection getUnPrintedMessages(String type) throws FinderException {
		if (isTypeSystemArchiveMessage(type)) return getSystemArchivationMessageHome().findUnPrintedMessages();
		return getPrintedLetterMessageHome().findUnPrintedLettersByType(type);
	}

	public Collection getUnPrintedMessages(String type, IWTimestamp from, IWTimestamp to) throws FinderException {
		if (isTypeSystemArchiveMessage(type)) return getSystemArchivationMessageHome().findUnPrintedMessages(from, to);
		return getPrintedLetterMessageHome().findUnPrintedLettersByType(type, from, to);
	}

	public int getUnPrintedDefaultLettersCount() {
		return getPrintedLetterMessageHome().getNumberOfUnPrintedDefaultLetters();
	}

	public int getUnPrintedPasswordLettersCount() {
		return getPrintedLetterMessageHome().getNumberOfUnPrintedPasswordLetters();
	}

	public int getPrintedLettersCountByStatusAndType(String caseStatus, String type) {
		return getPrintedLetterMessageHome().getNumberOfLettersByStatusAndType(caseStatus, type);
	}

	public int getUnprintedLettersCountByType(String type) {
		return getUnprintedMessagesCountByType(type);
	}

	public int getUnprintedMessagesCountByType(String type) {
		if (isTypeSystemArchiveMessage(type)) return getSystemArchivationMessageHome().getNumberOfUnPrintedMessages();
		return getPrintedLetterMessageHome().getNumberOfUnprintedLettersByType(type);
	}

	public int getPrintedLettersCountByType(String type) {
		return getPrintedLetterMessageHome().getNumberOfPrintedLettersByType(type);
	}

	public String[] getPrintMessageTypes() {
		String[] ptypes = getPrintedLetterMessageHome().getPrintMessageTypes();
		String[] atypes = getSystemArchivationMessageHome().getPrintMessageTypes();
		String[] types = new String[ptypes.length + atypes.length];

		System.arraycopy(ptypes, 0, types, 0, ptypes.length);
		System.arraycopy(atypes, 0, types, ptypes.length, atypes.length);
		return types;
	}

	public boolean isTypeSystemArchiveMessage(String type) {
		String[] atypes = getSystemArchivationMessageHome().getPrintMessageTypes();
		for (int i = 0; i < atypes.length; i++) {
			if (type.equals(atypes[i])) return true;
		}
		return false;
	}

	public boolean isBulkLetterType(String type) {
		if (type.equals(PrintedLetterMessageBMPBean.LETTER_TYPE_PASSWORD)) return true;
		if (type.equals(SystemArchivationMessageBMPBean.PRINT_TYPE)) return true;

		return false;
	}

	private boolean addTemplateHeader() {
		return getIWApplicationContext().getApplicationSettings().getDefaultLocale().equals(LocaleUtil.getSwedishLocale());
	}

	/**
	 * Returns an empty array if nothing is found.
	 */
	public int[] getUnPrintedLettersIDs(String type) {
		try {
			Collection coll = getPrintedLetterMessageHome().findUnPrintedLettersByType(type);
			if (coll != null && coll.size() > 0) {
				int[] theReturn = new int[coll.size()];
				Iterator iter = coll.iterator();
				int counter = 0;
				while (iter.hasNext()) {
					PrintedLetterMessage pMessage = (PrintedLetterMessage) iter.next();
					theReturn[counter++] = ((Integer) pMessage.getPrimaryKey()).intValue();
				}
				return theReturn;
			}
			else {
				int[] theReturn = new int[0];
				return theReturn;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new IBORuntimeException(e, this);
		}
	}

	/**
	 * Gets the point equal to given millimetercount
	 */
	public float getPointsFromMM(float millimeters) {
		float pointPerMM = 72 / 25.4f;
		return millimeters * pointPerMM;
	}

	protected Document getLetterDocumentTemplate() {
		//Margins defined in millimeters:
		float headFootMarginsMM = 9.0f;
		float leftRightMarginsMM = 30.0f;

		//Margins defined in points:
		float headFootMargins = getPointsFromMM(headFootMarginsMM);
		float leftRightMargins = getPointsFromMM(leftRightMarginsMM);
		Document document = new Document(PageSize.A4, leftRightMargins, leftRightMargins, headFootMargins, headFootMargins);
		//document.setMargins(leftRightMargins,leftRightMargins,headFootMargins,headFootMargins);
		document.addAuthor("IdegaWeb");
		document.addSubject("PrintedLetter");
		//document.open();
		return document;
	}

	protected PrintDocumentsHome getPrintDocumentsHome() {
		try {
			return (PrintDocumentsHome) getIDOHome(PrintDocuments.class);
		}
		catch (Exception e) {
			throw new IBORuntimeException(e);
		}
	}

	public PrintedLetterMessageHome getPrintedLetterMessageHome() {
		try {
			return (PrintedLetterMessageHome) getIDOHome(PrintedLetterMessage.class);
		}
		catch (Exception e) {
			throw new IBORuntimeException(e);
		}
	}

	public SystemArchivationMessageHome getSystemArchivationMessageHome() {
		try {
			return (SystemArchivationMessageHome) getIDOHome(SystemArchivationMessage.class);
		}
		catch (Exception e) {
			throw new IBORuntimeException(e);
		}
	}

	protected ICFileHome getICFileHome() {
		try {
			return (ICFileHome) getIDOHome(ICFile.class);
		}
		catch (Exception e) {
			throw new IBORuntimeException(e);
		}
	}

	protected MessageBusiness getMessageBusiness() {
		try {
			return (MessageBusiness) getServiceInstance(MessageBusiness.class);
		}
		catch (Exception e) {
			throw new IBORuntimeException(e);
		}
	}

	protected CommuneUserBusiness getUserBusiness() {
		try {
			return (CommuneUserBusiness) getServiceInstance(CommuneUserBusiness.class);
		}
		catch (Exception e) {
			throw new IBORuntimeException(e);
		}
	}

	public int writeBulkPDF(String[] messageIDs, User performer, String fileName, Locale locale, String type, boolean isAddressMessages, boolean flagMessages, boolean registerBulkData) throws FinderException {
		int fileId = -1;
		if (messageIDs.length > 0) {
			fileId = writeBulkPDF(getPrintedMessagesByPrimaryKeys(messageIDs, type), performer, fileName, locale, type, isAddressMessages, flagMessages, registerBulkData);
		}
		return fileId;
	}

	public int writeBulkPDF(Collection messages, User performer, String fileName, Locale locale, String type, boolean areAddressMessages, boolean flagMessages, boolean registerBulkData) {
		int fileId = -1;
		try {
			Iterator iter = messages.iterator();
			PrintMessage msg = null;
			PdfTemplate template = null;
			if (registerBulkData) {
				MemoryFileBuffer outerBuf = new MemoryFileBuffer();
				OutputStream outerDocOS = new MemoryOutputStream(outerBuf);
				InputStream outerDocIS = new MemoryInputStream(outerBuf);
				Document outerDocument = getLetterDocumentTemplate();
				PdfWriter writer = PdfWriter.getInstance(outerDocument, outerDocOS);
				outerDocument.open();

				ICFile bulkFile = getICFileHome().create();
				bulkFile.store();

				if (!areAddressMessages) {
					template = getLetterTemplate(type, writer);
				}
				int contentReturn = 0;
				int lettersProcessed = 0;
				DocumentPrintContext dpc = new DocumentPrintContext();
				dpc.setLocale(locale);
				dpc.setDocument(outerDocument);
				dpc.setPdfWriter(writer);
				dpc.setIWApplicationContext(getIWApplicationContext());
				while (iter.hasNext()) {
					msg = (PrintMessage) iter.next();
					dpc.setMessage(msg);
					if (!areAddressMessages) addTemplateToPage(template, writer, type);
					//contentReturn = createContent(outerDocument, msg,
					// performer,writer,locale);
					//contentReturn = createContent(dpc);
					createHandlerContent(dpc);
					//System.err.println("letter content returns : "+contentReturn+" for
					// msg: "+msg.getPrimaryKey().toString());
					if (contentReturn != ADDRESS_ERROR) {
						outerDocument.newPage();
						try {
							//System.err.println("bulk id =
							// "+bulkFile.getPrimaryKey().toString());
							msg.setMessageBulkData(bulkFile);

							if (areAddressMessages) {
								writePDF(msg, performer, fileName, locale, flagMessages);
							}
							else if (flagMessages) {
								getMessageBusiness().flagMessageAsPrinted(performer, msg);
							}
							else {
								msg.store();
							}
						}
						catch (Exception ex) {
							ex.printStackTrace();
						}
						lettersProcessed++;
					}
				}

				outerDocument.close();
				if (lettersProcessed > 0) {
					if (registerBulkData) {
						bulkFile.setFileValue(outerDocIS);
						bulkFile.setMimeType("application/x-pdf");
						bulkFile.setName(fileName + ".pdf");
						bulkFile.setFileSize(outerBuf.length());
						bulkFile.store();
						PrintDocuments pdocs = getPrintDocumentsHome().create();
						pdocs.setDocument(bulkFile);
						pdocs.setNumberOfSubDocuments(lettersProcessed);
						pdocs.setCreator(performer);
						pdocs.setType(type);
						pdocs.store();
						fileId = pdocs.getDocumentFileID();
					}
				}
				try {
					outerDocOS.close();
					outerDocIS.close();
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			else {
				while (iter.hasNext()) {
					msg = (PrintMessage) iter.next();
					writePDF(msg, performer, fileName, locale, flagMessages);
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return fileId;
	}

	public int writePDF(PrintMessage msg, User performer, String fileName, Locale locale, boolean flagPrinted) throws Exception {
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		OutputStream mos = new MemoryOutputStream(buffer);
		InputStream mis = new MemoryInputStream(buffer);
		Document document = getLetterDocumentTemplate();

		PdfWriter writer = PdfWriter.getInstance(document, mos);

		document.open();
		document.newPage();

		DocumentPrintContext dpc = new DocumentPrintContext();
		dpc.setDocument(document);
		dpc.setPdfWriter(writer);
		dpc.setUser(performer);
		dpc.setLocale(locale);
		dpc.setMessage(msg);
		dpc.setIWApplicationContext(getIWApplicationContext());

		//int contentReturn = createContent(document, msg,
		// performer,writer,locale);

		//int contentReturn = createContent(dpc);
		createHandlerContent(dpc);

		/*
		 * if(contentReturn==ADDRESS_ERROR){ document.close(); try { mos.close();
		 * mis.close(); } catch (Exception ex) {
		 *  } return -1; }
		 */
		document.close();
		ICFile file = getICFileHome().create();
		/* *** writing pdf to cachefolder manually
		if(!fileName.endsWith(".pdf") &&  !fileName.endsWith(".PDF"))
		    fileName +=".pdf";
		String folder = getIWApplicationContext().getIWMainApplication().getRealPath(getIWApplicationContext().getIWMainApplication().getCacheDirectoryURI()+"/prints");
		File tfile = FileUtil.getFileAndCreateIfNotExists(folder,fileName);
		FileOutputStream fos = new FileOutputStream(tfile);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while (mis.available() > 0) {
			baos.write(mis.read());
		}
		baos.writeTo(fos);
		baos.flush();
    		baos.close();
    		mis.reset();
    		*/
		
		file.setFileValue(mis);
		file.setMimeType("application/x-pdf");
		file.setName(fileName );
		file.setFileSize(buffer.length());
		file.store();
		msg.setMessageData(file);
		try {
			if (flagPrinted) {
				getMessageBusiness().flagMessageAsPrinted(performer, msg);
			}
			else {
				msg.store();
			}
			return msg.getMessageDataFileID();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			mos.close();
			mis.close();
		}
		catch (Exception ex) {
		}
		return -1;

	}

	private void createHandlerContent(DocumentPrintContext dpc) throws ContentCreationException {
		MessagePdfHandler handler = getMessagePdfHandler(dpc.getMessage());
		if (handler != null)
			handler.createMessageContent(dpc);
		else {
			createMessageContent(dpc);
		}
	}

	//private int createContent(Document doc,PrintMessage msg,User
	// performer,PdfWriter writer,Locale locale)throws Exception {
	private int createContent(DocumentPrintContext dpc) throws ContentCreationException {
		PrintMessage msg = dpc.getMessage();
		if (msg instanceof PrintedLetterMessage) {
			PrintedLetterMessage pmsg = (PrintedLetterMessage) msg;
			if (pmsg.getLetterType().equals(PrintedLetterMessageBMPBean.LETTER_TYPE_PASSWORD))
				return createPasswordLetterContent(dpc);
			else if (pmsg.getLetterType().equals(PrintedLetterMessageBMPBean.LETTER_TYPE_DEFAULT)) return createDefaultLetterContent(dpc);
		}
		else if (msg instanceof SystemArchivationMessage) {
			createArchiveMessageContent(dpc);
		}
		return 0;

	}

	private void addTemplateToPage(PdfTemplate template, PdfWriter writer, String type) throws Exception {
		if (template != null) {

			if (type.equals(PrintedLetterMessageBMPBean.LETTER_TYPE_PASSWORD)) {
				PdfContentByte cb = writer.getDirectContent();
				cb.addTemplate(template, getPointsFromMM(15f), getPointsFromMM(297 - 22));
			}
		}

	}

	private PdfTemplate getLetterTemplate(String type, PdfWriter writer) throws Exception {
		if (type.equals(PrintedLetterMessageBMPBean.LETTER_TYPE_PASSWORD)) { return createPasswordLetterTemplate(writer); }
		return null;
	}

	private PdfTemplate createPasswordLetterTemplate(PdfWriter writer) throws Exception {
		IWBundle iwb = getIWApplicationContext().getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER);
		PdfContentByte cb = writer.getDirectContent();

		float tempLength = 511f;
		float tempHeight = getPointsFromMM(40);
		PdfTemplate template = cb.createTemplate(tempLength, tempHeight);

		if (addTemplateHeader()) {
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			String mail_zip = iwb.getProperty("commune.mail_zip");
			String mail_name = iwb.getProperty("commune.mail_name");

			float convLengt = 100f;
			float convHeight = 60f;
			template.rectangle(0f, 0f, convLengt, convHeight);
			template.moveTo(0f, 0f);
			template.lineTo(convLengt, convHeight);
			template.moveTo(convLengt, 0f);
			template.lineTo(0f, convHeight);
			template.stroke();
			template.beginText();
			template.setFontAndSize(bf, 11f);
			template.setTextMatrix(5f, 40f);
			template.showText(mail_name);

			template.endText();
			template.beginText();
			template.setFontAndSize(bf, 11f);
			template.setTextMatrix(5f, 25f);
			template.showText(mail_zip);
			template.endText();

			Image porto = Image.getInstance(iwb.getResourcesRealPath() + "/shared/porto_betalt.jpg");
			porto.scaleAbsolute(60f, 60f);

			//Image portoA =Image.getInstance(iwb.getResourcesRealPath()+
			// "/shared/porto_a_logo.jpg");
			//float Awidth = 2.3f*60f;
			//portoA.scaleToFit(Awidth,60f);

			float portoXPos = tempLength - 90f;
			//float portoAXPos = portoXPos-Awidth-5f;
			template.addImage(porto, 60f, 0f, 0f, 60f, portoXPos, 0);
			//template.addImage(portoA,Awidth,0f,0f,60f,portoAXPos,0);
		}

		return template;
	}

	//private int createPasswordLetterContent(Document
	// document,PrintedLetterMessage msg,PdfWriter writer,Locale locale) throws
	// Exception{
	private int createPasswordLetterContent(DocumentPrintContext dpc) throws ContentCreationException {
		IWBundle iwb = getIWApplicationContext().getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER);
		System.out.println("Default locale: " + getIWApplicationContext().getApplicationSettings().getDefaultLocale().toString());
		String sAddrString = "";
		PdfContentByte cb = dpc.getPdfWriter().getDirectContent();
		Document document = dpc.getDocument();
		Locale locale = dpc.getLocale();
		PrintMessage msg = dpc.getMessage();
		//String mail_zip = iwb.getProperty("commune.mail_zip");
		//String mail_name = iwb.getProperty("commune.mail_name");

		try {
			sAddrString = getAddressString(dpc.getMessage().getOwner());
		}
		catch (Exception nouser) {
			handleNoAddressUser();
			System.err.println(nouser.getMessage());
			//nouser.printStackTrace();
			return ADDRESS_ERROR;
		}
		try {
			if (addTemplateHeader()) {
				ColumnText ct = new ColumnText(cb);
				float margin = getPointsFromMM(14);
				float lly = getPointsFromMM(297 - 22);
				float ury = lly + 60f;
				float urx = 595f - margin - 60f - 5f;
				float llx = 110f + margin;
				Phrase Ph0 = new Phrase(sAddrString, new Font(Font.HELVETICA, 12, Font.BOLD));
				ct.setSimpleColumn(Ph0, llx, lly, urx, ury, 15, Element.ALIGN_LEFT);
				ct.go();

				document.add(new Paragraph("\n\n\n\n\n\n\n"));
			}

			{

				User owner = msg.getOwner();
				HashMap tagmap = new CommuneUserTagMap(getIWApplicationContext(), owner);
				tagmap.putAll(getMessageTagMap(msg, locale));

				XmlPeer date = new XmlPeer(ElementTags.CHUNK, "date");
				date.setContent(new IWTimestamp().getDateString("dd.MM.yyyy"));
				tagmap.put(date.getAlias(), date);
				System.out.println("Date tag: " + date.getTag());

				String letterUrl = getXMLLetterUrl(iwb, locale, "password_letter.xml");
				if (msg.getBody().indexOf("|") > 0) {
					StringTokenizer tokenizer = new StringTokenizer(msg.getBody(), "|");
					XmlPeer peer = new XmlPeer(ElementTags.ITEXT, "letter");
					tagmap.put(peer.getAlias(), peer);

					if (tokenizer.hasMoreTokens()) {
						peer = new XmlPeer(ElementTags.CHUNK, "username");
						peer.setContent(tokenizer.nextToken());
						tagmap.put(peer.getAlias(), peer);
					}
					if (tokenizer.hasMoreTokens()) {
						peer = new XmlPeer(ElementTags.CHUNK, "password");
						peer.setContent(tokenizer.nextToken());
						tagmap.put(peer.getAlias(), peer);
					}

				}
				javax.xml.parsers.SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
				SAXmyHandler handler = new SAXmyHandler(document, tagmap);
				handler.setControlOpenClose(false);

				parser.parse(letterUrl, handler);
			}
		}
		catch (Exception e) {
			throw new ContentCreationException(e);
		}

		return 0;
	}

	private void handleNoAddressUser() {
		/*
		 * try{ String status =
		 * getMessageBusiness().getCaseStatusError().getStatus();
		 * getMessageBusiness().flagMessageWithStatus(performer,msg,status); }
		 * catch(Exception ex){ ex.printStackTrace(); }
		 */
	}

	/**
	 *  
	 */
	public int createDefaultLetterHeader(Document document, String addressString, PdfWriter writer) throws Exception {
		createLogoContent(document);
		if (addressString != null) {
			createAddressContent(addressString, writer);
		}
		createNewlinesContent(document);
		return 0;
	}

	public void createNewlinesContent(Document document) throws DocumentException {
		StringBuffer newlines = new StringBuffer(newlinesBetween);
		for (int i = 0; i < newlinesBetween; i++) {
			newlines.append('\n');
		}
		document.add(new Paragraph(newlines.toString()));
	}

	public void createLogoContent(Document document) throws BadElementException, MalformedURLException, IOException, DocumentException {
		IWBundle iwb = getIWApplicationContext().getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER);
		checkBundleDimensions(iwb);
		Image image = Image.getInstance(iwb.getResourcesRealPath() + "/shared/commune_logo.png");
		image.scaleToFit(getPointsFromMM(logoScaleWidth), getPointsFromMM(logoScaleHeight));
		image.setAbsolutePosition(getPointsFromMM(logoAbsPosX), getPointsFromMM(logoAbsPosY));
		document.add(image);

	}

	public void createHeaderDate(Document document, PdfWriter writer, String dateString) throws DocumentException {
		/*
		 * final PdfPTable header = new PdfPTable(new float[]{1});
		 * header.setWidthPercentage(100f); final PdfPCell defaultCell =
		 * header.getDefaultCell(); defaultCell.setBorder(0);
		 * //defaultCell.setFixedHeight(getPointsFromMM(40));
		 * defaultCell.setPadding(0); defaultCell.setNoWrap(true);
		 * defaultCell.setVerticalAlignment(align); header.addCell(new Phrase(new
		 * Chunk(dateString, getDefaultParagraphFont()))); document.add(header);
		 */
		PdfContentByte cb = writer.getDirectContent();
		cb.beginText();
		Font font = getDefaultParagraphFont();

		try {
			BaseFont bf = BaseFont.createFont(font.getFamilyname(), BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			cb.setFontAndSize(bf, font.size());
			// we show some text starting on some absolute position with a given
			// alignment
			cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, dateString, getPointsFromMM(210 - 20), getPointsFromMM(297 - 20), 0);
			cb.endText();
		}

		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createAddressContent(String addressString, PdfWriter writer) throws DocumentException {
		IWBundle iwb = getIWApplicationContext().getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER);
		checkBundleDimensions(iwb);
		Phrase Ph0 = new Phrase(addressString, getAddressFont());
		ColumnText ct = new ColumnText(writer.getDirectContent());
		/*
		 * public void setSimpleColumn(Phrase phrase, float llx, float lly, float
		 * urx, float ury, float leading, int alignment) Parameters: phrase - a
		 * Phrase llx - the lower left x corner lly - the lower left y corner urx -
		 * the upper right x corner ury - the upper right y corner leading - the
		 * leading alignment - the column alignment
		 *  
		 */
		float llx = getPointsFromMM(addressLowerLeftX);//getPointsFromMM((20f+95f));
		float lly = getPointsFromMM(addressLowerLeftY);//655f;
		float urx = getPointsFromMM(addressUpperRightX);//getPointsFromMM((193f));
		float ury = getPointsFromMM(addressUpperRightY);//getPointsFromMM(257f);
		//ct.setSimpleColumn(Ph0,getPointsFromMM((20f+95f)), 655f,
		// getPointsFromMM((193f)), getPointsFromMM(257f), 15, Element.ALIGN_LEFT);
		//ct.setSimpleColumn(Ph0,getPointsFromMM(new
		// Float(addressLowerLeftX).floatValue()), new
		// Float(addressLowerLeftY).floatValue(), new
		// Float(addressUpperRightX).floatValue(), new
		// Float(addressUpperRightY).floatValue(), 15, Element.ALIGN_LEFT);
		ct.setSimpleColumn(Ph0, llx, lly, urx, ury, 15, Element.ALIGN_LEFT);
		ct.go();
	}

	//private int createDefaultLetterContent( Document
	// document,PrintedLetterMessage msg, PdfWriter writer,Locale locale)throws
	// Exception{
	private int createDefaultLetterContent(DocumentPrintContext dpc) throws ContentCreationException {
		try {
			IWBundle iwb = getIWApplicationContext().getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER);
			PdfWriter writer = dpc.getPdfWriter();
			Document document = dpc.getDocument();
			Locale locale = dpc.getLocale();
			PrintMessage msg = dpc.getMessage();

			createLogoContent(document);
			String addressString = getAddressString(msg.getOwner());
			createAddressContent(addressString, writer);

			document.add(new Paragraph("\n\n\n\n\n\n\n\n"));

			{
				User owner = msg.getOwner();
				HashMap tagmap = new CommuneUserTagMap(getIWApplicationContext(), owner);
				tagmap.putAll(getMessageTagMap(msg, locale));

				String letterUrl = getXMLLetterUrl(iwb, locale, "default_letter.xml", true);

				SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
				SAXmyHandler handler = new SAXmyHandler(document, tagmap);
				handler.setControlOpenClose(false);

				parser.parse(letterUrl, handler);

			}

			createCommuneFooter(writer);
		}
		catch (NoUserAddressException nouser) {
			handleNoAddressUser();
			System.err.println(nouser.getMessage());
			//nouser.printStackTrace();
			return ADDRESS_ERROR;
		}
		catch (Exception e) {
			throw new ContentCreationException(e);
		}

		return 0;
	}

	public HashMap getMessageTagMap(PrintMessage msg, Locale locale) {
		HashMap tagmap = new HashMap();
		DateFormat df;
		if (locale != null) {
			df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		}
		else {
			df = DateFormat.getDateInstance(DateFormat.SHORT);
		}

		XmlPeer peer = new XmlPeer(ElementTags.CHUNK, "msgsubject");
		peer.setContent(msg.getSubject());
		tagmap.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "msgbody");
		peer.setContent(msg.getBody());
		tagmap.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "msgcreated");
		peer.setContent(df.format(msg.getCreated()));
		tagmap.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "msgid");
		peer.setContent(msg.getPrimaryKey().toString());
		tagmap.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "msgstatus");
		peer.setContent(msg.getCaseStatus().toString());
		tagmap.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "msgcode");
		peer.setContent(msg.getCaseCode().toString());
		tagmap.put(peer.getAlias(), peer);

		return tagmap;
	}

	public void createCommuneFooter(PdfWriter writer) throws Exception {

		PdfContentByte cb = writer.getDirectContent();
		Font nameFont = getDefaultParagraphFont();
		nameFont.setSize(9);
		Font textFont = getDefaultTextFont();
		textFont.setSize(9);

		PdfPTable table = new PdfPTable(4);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table.getDefaultCell().setNoWrap(true);

		IWBundle iwb = getIWApplicationContext().getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER);

		table.addCell(new Phrase(iwb.getProperty("commune.name_mailaddr", "Mailaddress"), nameFont));
		table.addCell(new Phrase(iwb.getProperty("commune.name_visitaddr", "Visitaddress"), nameFont));
		table.addCell(new Phrase(iwb.getProperty("commune.name_contact", "Contact"), nameFont));
		table.addCell(new Phrase(iwb.getProperty("commune.name_org_nr", "Organizationsnr"), nameFont));

		table.addCell(new Phrase(iwb.getProperty("commune.mail_name", "Mail name"), getTextFont()));
		table.addCell(new Phrase(iwb.getProperty("commune.visit_name", "Visit name"), textFont));
		table.addCell(new Phrase(iwb.getProperty("commune.website", "www.some-place.com"), textFont));
		table.addCell(new Phrase(iwb.getProperty("commune.org_number", "XXXXXX-XXXX"), textFont));

		table.addCell(new Phrase(iwb.getProperty("commune.mail_zip", "Zip code"), textFont));
		table.addCell(new Phrase(iwb.getProperty("commune.visit_streetaddr", "Street and number,"), textFont));
		table.addCell(new Phrase(iwb.getProperty("commune.support_email", "email@someplace.com"), textFont));
		table.addCell(new Phrase(" ", textFont));

		table.addCell(new Phrase(" ", textFont));
		table.addCell(new Phrase(iwb.getProperty("commune.visit_zip", "Visit zip"), textFont));
		table.addCell(new Phrase(iwb.getProperty("commune.office_phone", "office phone"), textFont));
		table.addCell(new Phrase(" ", textFont));

		int distFromBottomMM = 30;
		int[] widths = { 20, 20, 30, 20};
		table.setWidths(widths);
		table.setTotalWidth(getPointsFromMM(210 - 25 - 20));
		table.writeSelectedRows(0, -1, getPointsFromMM(25), getPointsFromMM(distFromBottomMM), cb);

		PdfContentByte linebyte = new PdfContentByte(writer);
		// we add some crosses to visualize the destinations

		linebyte.moveTo(getPointsFromMM(25), getPointsFromMM(distFromBottomMM + 2));
		linebyte.lineTo(getPointsFromMM(210 - 25), getPointsFromMM(distFromBottomMM + 2));

		linebyte.stroke();

		// we add the template on different positions
		cb.add(linebyte);
	}

	//public void createArchiveMessageContent(Document
	// document,SystemArchivationMessage msg, User performer,PdfWriter
	// writer,Locale locale)throws Exception{
	public void createArchiveMessageContent(DocumentPrintContext dpc) throws ContentCreationException {
		try {
			IWBundle iwb = getIWApplicationContext().getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER);
			//IWResourceBundle iwrb = iwb.getResourceBundle(locale);

			//DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,locale);
			PrintMessage msg = dpc.getMessage();
			Locale locale = dpc.getLocale();
			User owner = msg.getOwner();
			HashMap tagmap = new CommuneUserTagMap(getIWApplicationContext(), owner);
			tagmap.putAll(getMessageTagMap(msg, locale));

			String letterUrl = getXMLLetterUrl(iwb, locale, "archive_letter.xml");

			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			SAXmyHandler handler = new SAXmyHandler(dpc.getDocument(), tagmap);
			handler.setControlOpenClose(false);

			parser.parse(letterUrl, handler);
		}
		catch (Exception e) {
			throw new ContentCreationException(e);
		}
	}

	// set the refresh property to true if new values entered for dimensions
	private void checkBundleDimensions(IWBundle iwb) {
		String refreshValue = iwb.getProperty("printing.dimension_refresh", Boolean.toString(false));
		boolean refresh = refreshValue != null && refreshValue.equalsIgnoreCase(Boolean.toString(true));
		if (initRefresh || refresh) {
			try {
				newlinesBetween = Integer.parseInt(iwb.getProperty("printing.newlines_between_header_and_content", String.valueOf(newlinesBetween)));
				logoScaleHeight = Integer.parseInt(iwb.getProperty("printing.logo_scale_height_(mm)", String.valueOf(logoScaleHeight)));
				logoScaleWidth = Integer.parseInt(iwb.getProperty("printing.logo_scale_width_(mm)", String.valueOf(logoScaleWidth)));
				logoAbsPosX = Integer.parseInt(iwb.getProperty("printing.logo_abs_x_pos_(mm from left)", String.valueOf(logoAbsPosX)));
				logoAbsPosY = Integer.parseInt(iwb.getProperty("printing.logo_abs_y_pos_(mm from bottom)", String.valueOf(logoAbsPosY)));
				addressLowerLeftX = Integer.parseInt(iwb.getProperty("printing.address_lowerleft_x_(mm from left)", String.valueOf(addressLowerLeftX)));
				addressLowerLeftY = Integer.parseInt(iwb.getProperty("printing.address_lowerleft_y_(mm from bottom)", String.valueOf(addressLowerLeftY)));
				addressUpperRightX = Integer.parseInt(iwb.getProperty("printing.address_upperright_x_(mm from left)", String.valueOf(addressUpperRightX)));
				addressUpperRightY = Integer.parseInt(iwb.getProperty("printing.address_upperright_y_(mm from bottom)", String.valueOf(addressUpperRightY)));
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
			if (initRefresh) initRefresh = false;
			try {
				defaultParagraphFont = decodeFont(iwb.getProperty("printing.font_header", encodeFont(getDefaultParagraphFont())), getDefaultParagraphFont());
				defaultTextFont = decodeFont(iwb.getProperty("printing.font_text", encodeFont(getDefaultTextFont())), getDefaultTextFont());
				defaultAddressTextFont = decodeFont(iwb.getProperty("printing.font_address", encodeFont(getDefaultAddressTextFont())), getDefaultAddressTextFont());
			}
			catch (RuntimeException e1) {
				e1.printStackTrace();
			}
			iwb.setProperty("printing.dimension_refresh", Boolean.toString(false));

		}

	}

	private String encodeFont(Font font) {
		StringBuffer fontBuffer = new StringBuffer();
		fontBuffer.append(font.getFamilyname()).append("-");
		fontBuffer.append(font.size()).append("-");
		fontBuffer.append(encodeFontStyle(font)).append("-");
		if (font.color() != null) fontBuffer.append(IWColor.getHexColorString(font.color()));
		return fontBuffer.toString();
	}

	private String encodeFontStyle(Font font) {
		StringBuffer s = new StringBuffer();
		if (font.isBold()) {
			s.append(MarkupTags.CSS_BOLD).append(",");
		}
		if (font.isItalic()) {
			s.append(MarkupTags.CSS_ITALIC).append(",");
		}
		if (font.isUnderlined()) {
			s.append(MarkupTags.CSS_UNDERLINE).append(",");
		}
		if (font.isStrikethru()) {
			s.append(MarkupTags.CSS_LINETHROUGH);
		}

		return s.toString();
	}

	private Font decodeFont(String fontstring, Font returnFont) {
		if (fontstring != null) {
			int family = -1;
			StringTokenizer tokener = new StringTokenizer(fontstring, "-");
			// family part
			if (tokener.hasMoreTokens()) {
				family = Font.getFamilyIndex(tokener.nextToken());
			}
			if (family > -1) {
				Font font = new Font(family);
				// size part
				if (tokener.hasMoreTokens()) {
					float size = Float.parseFloat(tokener.nextToken());
					font.setSize(size);
				}
				//style part
				if (tokener.hasMoreTokens()) {
					font.setStyle(tokener.nextToken());
				}
				// color part
				if (tokener.hasMoreTokens()) {
					font.setColor(IWColor.getAWTColorFromHex(tokener.nextToken()));
				}
				return font;
			}
		}
		return returnFont;
	}

	public String getXMLLetterUrl(IWBundle iwb, Locale locale, String name) {
		return "file://" + iwb.getResourcesRealPath(locale) + "/" + name;
	}

	public String getXMLLetterUrl(IWBundle iwb, Locale locale, String name, boolean createIfNotExists) {
		String url = getXMLLetterUrl(iwb, locale, name);
		if (url != null) {
			try {
				String protocol = "file://";
				int indexOfProtocol = url.indexOf(protocol);
				String path = url;
				if (indexOfProtocol != -1) path = url.substring(protocol.length());
				java.io.File file = new java.io.File(path);
				if (!file.exists()) {
					file.createNewFile();
					FileOutputStream fos = new FileOutputStream(file);
					XMLElement root = new XMLElement("userletter");
					XMLOutput xmlOutput = new XMLOutput("  ", true);
					xmlOutput.setLineSeparator(System.getProperty("line.separator"));
					xmlOutput.setTextNormalize(true);
					xmlOutput.setEncoding("iso-8859-1");
					XMLDocument doc = new XMLDocument(root);

					XMLElement subject = new XMLElement("msgsubject");
					subject.setAttribute("font", "Helvetica");
					subject.setAttribute("fontstyle", "bold");
					subject.setAttribute("size", "12.0");
					XMLElement body = new XMLElement("msgbody");
					subject.setAttribute("font", "Time");
					subject.setAttribute("fontstyle", "bold");
					subject.setAttribute("size", "12.0");
					root.addContent(subject);
					root.addContent(new XMLElement("newline"));
					root.addContent(new XMLElement("newline"));
					root.addContent(new XMLElement("newline"));
					root.addContent(body);

					xmlOutput.output(doc, fos);
					fos.close();
				}

			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return url;
	}

	public String getDefaultXMLTemplateValue() {
		StringBuffer content = new StringBuffer();
		content.append("<userletter>\n");
		content.append("<msgcreated/>\n");
		content.append("<newline />\n");
		content.append("<newline />\n");
		content.append("<msgsubject font=\"Helvetica\" size=\"12.0\" fontstyle=\"bold\"/>\n");
		content.append("<newline />\n");
		content.append("<newline />\n");
		content.append("<newline />\n");
		content.append("<newline />\n");
		content.append("<msgbody font=\"Time\" size=\"12.0\"/>\n");
		content.append("<newline />\n");
		content.append("</userletter>");
		return content.toString();
	}


	private MessagePdfHandler getMessagePdfHandler(PrintMessage message) {
		Case parentCase = message.getParentCase();

		if (parentCase != null) {

			try {
				String code = parentCase.getCaseCode().toString();
				if (code != null) {
					MessageHandlerInfo handlerInfo = ((MessageHandlerInfoHome) getIDOHome(MessageHandlerInfo.class)).findByPrimaryKey(code);
					return handlerInfo.getHandler();
				}
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				//e.printStackTrace();
			}

		}
		logWarning("No handler found for message: " + message);
		return null;

	}

	/*
	 * Commented out since it is never used... private class
	 * NoLetterTemplateException extends Exception{
	 * NoLetterTemplateException(String type){ super("Letter template for case
	 * type "+type +" not found."); } }
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see se.idega.idegaweb.commune.message.business.MessagePdfHandler#createMessageContent(se.idega.idegaweb.commune.printing.business.DocumentPrintContext)
	 */
	public void createMessageContent(DocumentPrintContext dpc) throws ContentCreationException {
		createContent(dpc);
	}

}