package is.idega.idegaweb.campus.block.allocation.business;


import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.block.allocation.data.ContractText;
import is.idega.idegaweb.campus.block.allocation.data.ContractTextHome;
import is.idega.idegaweb.campus.block.building.data.ApartmentTypeRent;
import is.idega.idegaweb.campus.block.building.data.ApartmentTypeRentHome;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentHome;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.ApartmentTypeHome;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.BuildingHome;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.ComplexHome;
import com.idega.block.building.data.Floor;
import com.idega.block.building.data.FloorHome;
import com.idega.block.finance.data.TariffIndex;
import com.idega.block.finance.data.TariffIndexBMPBean;
import com.idega.business.IBOLookup;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.lowagie.text.Chapter;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class CampusContractWriter {
	public final static String renter_name = "renter_name";
	public final static String renter_address = "renter_address";
	public final static String renter_id = "renter_id";
	public final static String tenant_name = "tenant_name";
	public final static String tenant_address = "tenant_address";
	public final static String tenant_id = "tenant_id";
	public final static String apartment_name = "apartment_name";
	public final static String apartment_address = "apartment_address";
	public final static String apartment_campus = "apartment_campus";
	public final static String apartment_area = "apartment_area";
	public final static String apartment_floor = "apartment_floor";
	public final static String apartment_info = "apartment_info";
	public final static String apartment_rent = "apartment_rent";
	public final static String apartment_category = "apartment_category";
	public final static String apartment_roomcount = "apartment_roomcount";
	public final static String contract_starts = "contract_starts";
	public final static String contract_ends = "contract_ends";
	public final static String renting_index = "renting_index";
	public final static String today = "today";
	public static String[] TAGS =
		{
			renter_name,
			renter_address,
			renter_id,
			tenant_name,
			tenant_address,
			tenant_id,
			apartment_name,
			apartment_floor,
			apartment_address,
			apartment_campus,
			apartment_area,
			apartment_roomcount,
			apartment_info,
			apartment_rent,
			apartment_category,
			contract_starts,
			contract_ends,
			renting_index,
			today };
	public final static String IS = "IS";
	public final static String EN = "EN";
	public final static String TIIS = "TIS";
	public final static String TIEN = "TEN";
	public static int writePDF(
		int[] ids,
		IWResourceBundle iwrb,
		Font nameFont,
		Font titleFont,
		Font paragraphFont,
		Font tagFont,
		Font textFont) {
		int returner = -1;
		boolean bEntity = false;
		if (ids != null && ids.length > 0) {
			bEntity = true;
		}
		try {
			MemoryFileBuffer buffer = new MemoryFileBuffer();
			MemoryOutputStream mos = new MemoryOutputStream(buffer);
			MemoryInputStream mis = new MemoryInputStream(buffer);
			//FileOutputStream fos = new FileOutputStream(file);
			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
			PdfWriter.getInstance(document, mos);
			document.addAuthor("Idegaweb Campus");
			document.addSubject("");
			document.open();
			document.newPage();
			HeaderFooter footer = new HeaderFooter(new Phrase("", textFont), true);
			footer.setBorder(0);
			footer.setAlignment(Element.ALIGN_CENTER);
			document.setFooter(footer);
			ContractText header = getHeader();
			Collection texts = getTexts();
			String title = "";
			if (header != null)
				title = header.getText() + " \n\n";
			Paragraph cTitle = new Paragraph(title, titleFont);
			// for each contract id
			for (int j = 0; j < ids.length; j++) {
				document.setPageCount(1);
				bEntity = ids[j] > 0 ? true : false;
				Chapter chapter = new Chapter(cTitle, 1);
				chapter.setNumberDepth(0);
				Paragraph P, P2;
				Section subSection;
				Phrase phrase;
				//System.err.println("inside chapter : "+ids[j]);
				Map map = getTagMap(ids[j], iwrb, nameFont, tagFont, textFont);
				if (texts != null) {
					for (Iterator iter = texts.iterator(); iter.hasNext();) {
						ContractText CT = (ContractText) iter.next();
						P = new Paragraph(new Phrase(CT.getName(), paragraphFont));
						subSection = chapter.addSection(P, 0);
						String sText = CT.getText();
						if (bEntity && CT.getUseTags()) {
							phrase = detagParagraph(map, sText);

						}
						else {
							phrase = new Phrase(sText, textFont);
						}
						P2 = new Paragraph(phrase);
						subSection.add(P2);
					}
				}
				document.add(chapter);
				document.newPage();
			}
			document.close();
			ICFile pdfFile = null;
			Contract eContract = null;
			String fileName = "test";
			if (bEntity) {
				try {
					//System.err.println("instanciating Contract "+ids[0]);
					ContractHome cHome = (ContractHome) IDOLookup.getHome(Contract.class);
					eContract = cHome.findByPrimaryKey(new Integer(ids[0]));
					ApplicantHome aHome = (ApplicantHome) IDOLookup.getHome(Applicant.class);
					Applicant A = aHome.findByPrimaryKey(eContract.getApplicantId());
					fileName = A.getSSN();
					if (!"".equals(fileName))
						fileName = A.getFirstName();
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			pdfFile = ((ICFileHome) com.idega.data.IDOLookup.getHome(ICFile.class)).create();
			//System.err.println("available "+mis.available());
			pdfFile.setFileValue(mis);
			pdfFile.setMimeType("application/pdf");
			pdfFile.setName(fileName + ".pdf");
			pdfFile.setFileSize(buffer.length());
			pdfFile.store();
			returner = ((Integer) pdfFile.getPrimaryKey()).intValue();
			if (eContract != null && returner > 0) {
				try {
					//System.err.println("updating Contract ");
					boolean update = false;
					if (eContract
						.getStatus()
						.equalsIgnoreCase(
							is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean.statusCreated)) {
						eContract.setStatusPrinted();
						update = true;
					}
					if (returner > 0) {
						eContract.setFileId(returner);
						update = true;
					}
					if (update)
						eContract.store();
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			try {
				mos.close();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			returner = -1;
		}
		return returner;
	}
	public static int writeTestPDF(
		IWResourceBundle iwrb,
		Font nameFont,
		Font titleFont,
		Font paragraphFont,
		Font tagFont,
		Font textFont) {
		return writePDF(new int[1], iwrb, nameFont, titleFont, paragraphFont, tagFont, textFont);
	}
	public static int writePDF(
		int id,
		IWResourceBundle iwrb,
		Font nameFont,
		Font titleFont,
		Font paragraphFont,
		Font tagFont,
		Font textFont) {
		int[] ids = { id };
		return writePDF(ids, iwrb, nameFont, titleFont, paragraphFont, tagFont, textFont);
	}
	private static Collection getTexts() {
		Collection texts = null;
		try {
			ContractTextHome tHome = (ContractTextHome) IDOLookup.getHome(ContractText.class);
			texts = tHome.findByLanguage(IS);
		}
		catch (Exception ex) {
		}
		return texts;
	}
	private static ContractText getHeader() {
		try {
			ContractTextHome tHome = (ContractTextHome) IDOLookup.getHome(ContractText.class);
			Collection titles = tHome.findByLanguage(TIIS);
			if (titles != null && !titles.isEmpty()) {
				return (ContractText) titles.iterator().next();
			}
			else {
				return null;
			}
		}
		catch (Exception ex) {
			return null;
		}
	}
	private static float getTariffIndex() {
		try {
			List L =
				EntityFinder.getInstance().findAllDescendingOrdered(
					TariffIndex.class,
					TariffIndexBMPBean.getColumnNameDate());
			if (L != null) {
				TariffIndex ti = (TariffIndex) L.get(0);
				return ti.getIndex();
			}
			else {
				return 1;
			}
		}
		catch (Exception ex) {
			return 1;
		}
	}
	private static Phrase detagParagraph(Map map, String sParagraph) {
		Phrase phrase = new Phrase();
		StringTokenizer ST = new StringTokenizer(sParagraph, "[]");
		while (ST.hasMoreTokens()) {
			String token = ST.nextToken();
			if (map.containsKey(token)) {
				phrase.add(map.get(token));
			}
			else {
				phrase.add(new Chunk(token, new Font(Font.HELVETICA, 8, Font.NORMAL)));
			}
		}
		return phrase;
	}
	private static Map getTagMap(
		int contractId,
		IWResourceBundle iwrb,
		Font nameFont,
		Font tagFont,
		Font textFont) {
		if (contractId > 0) {
			try {
				IWBundle iwb = iwrb.getIWBundleParent();
				Contract eContract =
					((ContractHome) IDOLookup.getHome(Contract.class)).findByPrimaryKey(new Integer(contractId));
				Applicant eApplicant =
					((ApplicantHome) IDOLookup.getHome(Applicant.class)).findByPrimaryKey(eContract.getApplicantId());
				Apartment eApartment =
					((ApartmentHome) IDOLookup.getHome(Apartment.class)).findByPrimaryKey(eContract.getApartmentId());
				ApartmentType eApartmentType =
					((ApartmentTypeHome) IDOLookup.getHome(ApartmentType.class)).findByPrimaryKey(
						new Integer(eApartment.getApartmentTypeId()));
				// ApartmentCategory eApartmentCategory = ((com.idega.block.building.data.ApartmentCategoryHome)IDOLookup.getHomeLegacy(ApartmentCategory.class)).findByPrimaryKey(eApartmentType.getApartmentCategoryId());
				String aprtTypeName = eApartmentType.getName();
				Floor eFloor =
					((FloorHome) IDOLookup.getHome(Floor.class)).findByPrimaryKey(new Integer(eApartment.getFloorId()));
				Building eBuilding =
					((BuildingHome) IDOLookup.getHome(Building.class)).findByPrimaryKey(
						new Integer(eFloor.getBuildingId()));
				Complex eComplex =
					((ComplexHome) IDOLookup.getHome(Complex.class)).findByPrimaryKey(
						new Integer(eBuilding.getComplexId()));
				ApartmentTypeRent rent = null;
				try {
					rent =
						((ApartmentTypeRentHome) IDOLookup.getHome(ApartmentTypeRent.class)).findByTypeAndValidity(
							((Integer) eApartmentType.getPrimaryKey()).intValue(),
							eContract.getValidFrom());
				}
				catch (Exception e) {
					rent = null;
				}
				Hashtable H = new Hashtable(TAGS.length);
				DateFormat dfLong = DateFormat.getDateInstance(DateFormat.LONG, iwrb.getLocale());
				NumberFormat nf = NumberFormat.getCurrencyInstance(iwrb.getLocale());
				H.put(
					renter_name,
					new Chunk(iwb.getProperty("contract_campus_name", "F???lagsstofnun St???denta"), tagFont));
				H.put(renter_address, new Chunk(iwb.getProperty("contract_campus_address", "v/Hringbraut"), tagFont));
				H.put(renter_id, new Chunk(iwb.getProperty("contract_campus_id", "540169-6249"), tagFont));
				H.put(today, new Chunk(dfLong.format(new java.util.Date()), tagFont));
				H.put(tenant_name, new Chunk(eApplicant.getFullName(), nameFont));
				H.put(tenant_address, new Chunk(eApplicant.getLegalResidence(), nameFont));
				H.put(tenant_id, new Chunk(eApplicant.getSSN(), nameFont));
				String aname = iwrb.getLocalizedString("apartment", "Apartment") + " " + eApartment.getName();
				H.put(apartment_name, new Chunk(aname, nameFont));
				H.put(apartment_floor, new Chunk(eFloor.getName(), nameFont));
				H.put(apartment_address, new Chunk(eBuilding.getStreet(), nameFont));
				H.put(apartment_campus, new Chunk(eComplex.getName(), nameFont));
				H.put(apartment_area, new Chunk(String.valueOf(eApartmentType.getArea()), tagFont));
				H.put(apartment_roomcount, new Chunk(String.valueOf(eApartmentType.getRoomCount()), tagFont));
				H.put(
					apartment_info,
					new Chunk(eApartmentType.getExtraInfo() != null ? eApartmentType.getExtraInfo() : "", textFont));
				H.put(contract_starts, new Chunk(dfLong.format(eContract.getValidFrom()), tagFont));
				H.put(contract_ends, new Chunk(dfLong.format(eContract.getValidTo()), tagFont));
				if (rent != null && rent.getRent() > 0)
					H.put(apartment_rent, new Chunk(nf.format((double) rent.getRent()), tagFont));
				else
					H.put(apartment_rent, new Chunk(nf.format(eApartmentType.getRent()), tagFont));
				//      H.put(apartment_category,new Chunk(eApartmentCategory.getName(),tagFont));
				String aprtTypeNameAbbr = null;
				if (aprtTypeName != null) {
					StringTokenizer tok = new StringTokenizer(aprtTypeName, " ");
					if (tok.hasMoreTokens())
						aprtTypeNameAbbr = tok.nextToken();
				}
				if (aprtTypeNameAbbr != null)
					H.put(apartment_category, new Chunk(aprtTypeNameAbbr, nameFont));
				else
					H.put(apartment_category, new Chunk("", nameFont));
				H.put(renting_index, new Chunk(iwb.getProperty("contract_campus_index", "100"), tagFont));
				return H;
			}
			
			catch (Exception e) {
				e.printStackTrace();
				return new Hashtable();
			}
		}
		else
			return new Hashtable();
	}
	public static String[] getTags() {
		return TAGS;
	}
	public ContractService getContractService(IWApplicationContext iwac) throws RemoteException {
		return (ContractService) IBOLookup.getServiceInstance(iwac, ContractService.class);
	}
}
