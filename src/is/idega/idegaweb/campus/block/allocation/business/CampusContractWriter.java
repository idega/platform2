package is.idega.idegaweb.campus.block.allocation.business;





import com.idega.idegaweb.IWResourceBundle;

import com.idega.block.building.data.*;

import com.idega.block.application.data.Applicant;

import com.idega.util.text.TextSoap;

import com.idega.util.idegaTimestamp;

import com.idega.data.EntityFinder;

import com.idega.block.application.data.*;

import com.idega.core.data.ICFile;



import com.lowagie.text.*;

import com.lowagie.text.pdf.PdfWriter;



import is.idega.idegaweb.campus.block.allocation.data.Contract;

import is.idega.idegaweb.campus.block.allocation.data.ContractText;

/** @todo  */

// get rid of this interconnection

import com.idega.block.finance.data.TariffIndex;

import java.io.FileOutputStream;

import java.util.List;

import java.util.StringTokenizer;

import java.util.Hashtable;

import java.text.DateFormat;

import java.text.NumberFormat;

import java.sql.SQLException;



import com.idega.io.MemoryOutputStream;

import com.idega.io.MemoryInputStream;

import com.idega.io.MemoryFileBuffer;



/**

 * Title:        idegaclasses

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:      idega

 * @author <a href="aron@idega.is">Aron Birkir</a>

 * @version 1.0

 */



public class CampusContractWriter{

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



  public final static String apartment_roomcount = "apartment_roomcount";

  public final static String contract_starts = "contract_starts";

  public final static String contract_ends = "contract_ends";



  public final static String renting_index = "renting_index";

  public final static String today = "today";





  public  static String[] TAGS = {renter_name,renter_address,renter_id,

                            tenant_name,tenant_address,tenant_id,

                            apartment_name,apartment_floor,apartment_address,

                            apartment_campus,apartment_area,

                            apartment_roomcount,apartment_info,

                            apartment_rent,

                            contract_starts,contract_ends,renting_index,today};



  public final static String IS ="IS";

  public final static String EN ="EN";

  public final static String TIIS = "TIS";

  public final static String TIEN = "TEN";



  public static int writePDF(int[] ids,IWResourceBundle iwrb,Font nameFont ,Font titleFont,Font paragraphFont, Font tagFont,Font textFont){

    int returner = -1;

    boolean bEntity = false;

    if(ids != null &&  ids.length > 0){

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



        HeaderFooter  footer = new HeaderFooter(new Phrase("",textFont),true);

        footer.setBorder(0);

        footer.setAlignment(Element.ALIGN_CENTER);

        document.setFooter(footer);



        ContractText ct = getHeader();

        List L = listOfTexts();

        String title = "";

        if(ct != null)

          title = ct.getText()+" \n\n";

        Paragraph cTitle = new Paragraph(title , titleFont);

        // for each contract id

        for (int j = 0; j < ids.length; j++) {

          document.setPageCount(1);

          bEntity = ids[j] > 0 ? true:false;

          Chapter chapter = new Chapter(cTitle, 1);

          chapter.setNumberDepth(0);

          Paragraph P,P2;

          Chapter subChapter;

          Section subSection;

          Phrase phrase;

          //System.err.println("inside chapter : "+ids[j]);

          Hashtable H = getHashTags(ids[j],iwrb,nameFont,tagFont,textFont);

          if(L!=null){

            int len = L.size();

            for (int i = 0; i < len; i++) {

              ContractText CT = (ContractText) L.get(i);

              P = new Paragraph(new Phrase(CT.getName(),paragraphFont));

              subSection = chapter.addSection(P,0);



              String sText = CT.getText();

              if(bEntity &&CT.getUseTags()){

                phrase = detagParagraph(H,sText);

              }

              else{

                phrase = new Phrase(sText,textFont);

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

				if(bEntity ){

          try {

						//System.err.println("instanciating Contract "+ids[0]);

            eContract = new Contract(ids[0]);

						Applicant A = new Applicant(eContract.getApplicantId().intValue());

						fileName = A.getSSN();

						if(!"".equals(fileName))

							fileName = A.getFirstName();

          }

          catch (SQLException ex) {

            ex.printStackTrace();

          }

        }



				try {

					pdfFile = new ICFile();

					//System.err.println("available "+mis.available());

					pdfFile.setFileValue(mis);

					pdfFile.setMimeType("application/pdf");

					pdfFile.setName(fileName+".pdf");

					pdfFile.setFileSize(buffer.length());

					pdfFile.insert();

					returner = pdfFile.getID();

				}

				catch (SQLException ex) {

				  ex.printStackTrace();

				}



				if(eContract !=null && returner > 0){

          try {

							//System.err.println("updating Contract ");

						boolean update = false;

            if(eContract.getStatus().equalsIgnoreCase(Contract.statusCreated)){

							eContract.setStatusPrinted();

							update = true;

						}

						if(returner > 0){

							eContract.setFileId(returner);

							update = true;

						}

						if(update)

            eContract.update();

          }

          catch (SQLException ex) {

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



  public static int writeTestPDF(IWResourceBundle iwrb,Font nameFont, Font titleFont,Font paragraphFont, Font tagFont,Font textFont){

    return writePDF(new int[0],iwrb,nameFont, titleFont, paragraphFont, tagFont, textFont);

  }

  public static int writePDF(int id,IWResourceBundle iwrb,Font nameFont, Font titleFont,Font paragraphFont, Font tagFont,Font textFont){

      int[] ids = {id};

    return writePDF(ids,iwrb,nameFont, titleFont, paragraphFont, tagFont, textFont);

  }



  private static List listOfTexts(){

    List L = null;



    try {

      ContractText CT = new ContractText();

      L = EntityFinder.findAllByColumnOrdered(CT,ContractText.getLanguageColumnName(),IS,ContractText.getOrdinalColumnName());

    }

    catch (SQLException ex) {



    }

    return L;

  }



  private static ContractText getHeader(){

    try {

      ContractText CT = new ContractText();

      List L = EntityFinder.findAllByColumn(CT,ContractText.getLanguageColumnName(),TIIS);

      if(L!= null){

        return (ContractText)L.get(0);

      }

      else{

        return null;

      }

    }

    catch (SQLException ex) {

      return null;

    }

  }



  private static float getTariffIndex(){

    TariffIndex ti = new TariffIndex();

    try {

      List L = EntityFinder.findAllDescendingOrdered(ti,TariffIndex.getColumnNameDate());

      if(L!= null){

        ti = (TariffIndex)L.get(0);

        return ti.getIndex();

      }

      else{

        return 1;

      }

    }

    catch (Exception ex) {

      return 1;

    }

  }



  private static Phrase detagParagraph(Hashtable H,String sParagraph){

    Phrase phrase = new Phrase();

    StringTokenizer ST  = new StringTokenizer(sParagraph,"[]");

    while(ST.hasMoreTokens()){

      String token = ST.nextToken();

      if(H.containsKey(token)){

        phrase.add(H.get(token));

      }

      else{

        phrase.add(new Chunk(token,new Font(Font.HELVETICA, 8, Font.NORMAL)));

      }

    }

    return phrase;

  }



  private static Hashtable getHashTags(int contractId,IWResourceBundle iwrb,Font nameFont,Font tagFont,Font textFont){

    try{

      Contract eContract = new Contract(contractId);

      Applicant eApplicant = new Applicant(eContract.getApplicantId().intValue());

      Apartment eApartment = new Apartment(eContract.getApartmentId().intValue());

      ApartmentType eApartmentType = new ApartmentType(eApartment.getApartmentTypeId());

      Floor eFloor = new Floor(eApartment.getFloorId());

      Building eBuilding = new Building(eFloor.getBuildingId());

      Complex eComplex = new Complex(eBuilding.getComplexId());

      Hashtable H = new Hashtable(TAGS.length);

      DateFormat dfLong = DateFormat.getDateInstance(DateFormat.LONG,iwrb.getLocale());

      NumberFormat nf = NumberFormat.getCurrencyInstance(iwrb.getLocale());

      H.put(renter_name,new Chunk("Félagsstofnun Stúdenta",tagFont));

      H.put(renter_address,new Chunk("v/Hringbraut",tagFont));

      H.put(renter_id,new Chunk("540169-6249",tagFont));

      H.put(today,new Chunk(dfLong.format(new java.util.Date()),tagFont));

      H.put(tenant_name,new Chunk(eApplicant.getFullName(),nameFont));

      H.put(tenant_address,new Chunk(eApplicant.getLegalResidence(),nameFont));

      H.put(tenant_id,new Chunk(eApplicant.getSSN(),nameFont));

      String aname = iwrb.getLocalizedString("apartment","Apartment")+" "+ eApartment.getName();



      H.put(apartment_name,new Chunk(aname,nameFont));

      H.put(apartment_floor, new Chunk(eFloor.getName(),nameFont));

      H.put(apartment_address,new Chunk(eBuilding.getStreet(),nameFont));

      H.put(apartment_campus, new Chunk(eComplex.getName(),nameFont));

      H.put(apartment_area, new Chunk(String.valueOf(eApartmentType.getArea()),tagFont));

      H.put(apartment_roomcount,new Chunk(String.valueOf(eApartmentType.getRoomCount()),tagFont));

      H.put(apartment_info,new Chunk(eApartmentType.getExtraInfo()!= null?eApartmentType.getExtraInfo():"",textFont));

      H.put(contract_starts,new Chunk(dfLong.format(eContract.getValidFrom()),tagFont));

      H.put(contract_ends,new Chunk(dfLong.format(eContract.getValidTo()),tagFont));

      H.put(apartment_rent,new Chunk(nf.format((long)eApartmentType.getRent()),tagFont));

      /** @todo fixa: */

      H.put(renting_index,new Chunk( "214.9",tagFont));

      return H;

    }

    catch(SQLException ex){

      ex.printStackTrace();

      return new Hashtable();

    }



  }



  public static String[] getTags(){

    return TAGS;

  }



}