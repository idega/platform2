/*
 * $Id: CampusContractWriter.java,v 1.7 2001/08/16 23:41:57 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.allocation;

import com.idega.block.application.data.*;
import java.util.List;
import com.idega.idegaweb.IWResourceBundle;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import is.idegaweb.campus.entity.ContractText;
import com.idega.data.EntityFinder;
import java.sql.SQLException;
import is.idegaweb.campus.entity.Contract;
import com.idega.block.building.data.*;
import com.idega.block.application.data.Applicant;
import com.idega.util.text.TextSoap;
import com.idega.util.idegaTimestamp;
import is.idegaweb.campus.entity.TariffIndex;
import java.util.StringTokenizer;
import java.util.Hashtable;
import java.text.DateFormat;
import java.text.NumberFormat;
/**
 *
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 *
 */

public class CampusContractWriter{
  public final static String renter_name = "[renter_name]";
  public final static String renter_address = "[renter_address]";
  public final static String renter_id = "[renter_id]";

  public final static String tenant_name = "[tenant_name]";
  public final static String tenant_address = "[tenant_address]";
  public final static String tenant_id = "[tenant_id]";

  public final static String apartment_name = "[apartment_name]";
  public final static String apartment_address = "[apartment_address]";
  public final static String apartment_campus = "[apartment_campus]";
  public final static String apartment_area = "[apartment_area]";
  public final static String apartment_floor = "[apartment_floor]";
  public final static String apartment_info = "[apartment_info]";
  public final static String apartment_rent = "[apartment_rent]";

  public final static String apartment_roomcount = "[apartment_roomcount]";
  public final static String contract_starts = "[contract_starts]";
  public final static String contract_ends = "[contract_ends]";

  public final static String renting_index = "[renting_index]";
  public final static String today = "[today]";


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
  public final static String TIEN = "T EN";

  public static boolean writePDF(int id,IWResourceBundle iwrb,String realpath){
    boolean returner = false;
    boolean bEntity = false;
    if(id > 0){
      bEntity = true;
    }
    try {

        String file = realpath;
        FileOutputStream fos = new FileOutputStream(file);
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, fos);
        document.addAuthor("Idegaweb Campus");
        document.addSubject("");
        document.open();

        Font chapterFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font nameFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        Font tagFont = new Font(Font.COURIER,9,Font.BOLD);
        Font textFont = new Font(Font.HELVETICA, 8, Font.NORMAL);
        Font filledFont = new Font(Font.HELVETICA,8,Font.BOLD);
        filledFont.setStyle("underline");
        ContractText ct = getHeader();
        String title = "";
        if(ct != null)
          title = ct.getText()+" \n\n";
        Paragraph cTitle = new Paragraph(title , chapterFont);
        Chapter chapter = new Chapter(cTitle, 1);
        chapter.setNumberDepth(0);
        Paragraph P;
        List L = listOfTexts();
        Hashtable H = getHashTags(id,iwrb);
        if(L!=null){
          int len = L.size();
          for (int i = 0; i < len; i++) {
            ContractText CT = (ContractText) L.get(i);
            P = new Paragraph(new Phrase(CT.getName()+"\n",nameFont));
            String sText = CT.getText();
            if(bEntity &&CT.getUseTags()){
              Phrase phrase = detagParagraph(H,sText);
              P.add(phrase);
            }
            else{
              Phrase phrase = new Phrase(sText,textFont);
              P.add(phrase);
            }
            chapter.add(P);
          }
        }
        document.add(chapter);
        document.close();
        try {
          fos.close();
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
        returner = true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      returner = false;
    }
    if(bEntity){
      try {
        Contract C = new Contract(id);
        C.setStatusPrinted();
        C.update();
      }
      catch (SQLException ex) {
        returner = false;
      }
    }
    return returner;
  }

  public static boolean writeTestPDF(IWResourceBundle iwrb,String realpath){
    return writePDF(-1,iwrb,realpath);
  }
  private static List listOfTexts(){
    List L = null;

    try {
      ContractText CT = new ContractText();
      L = EntityFinder.findAllByColumnOrdered(CT,CT.getLanguageColumnName(),IS,CT.getOrdinalColumnName());
    }
    catch (SQLException ex) {

    }
    return L;
  }

  private static ContractText getHeader(){
    try {
      ContractText CT = new ContractText();
      List L = EntityFinder.findAllByColumn(CT,CT.getLanguageColumnName(),TIIS);
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
    StringTokenizer ST  = new StringTokenizer(sParagraph);
    while(ST.hasMoreTokens()){
      String token = ST.nextToken();
      if(H.containsKey(token)){
        phrase.add(H.get(token));
      }
      else{
        phrase.add(token);
      }
    }
    return phrase;
  }

  private static Hashtable getHashTags(int contractId,IWResourceBundle iwrb){
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
      H.put(renter_name,"Stúdentagarðar");
      H.put(renter_address,"Hringbraut");
      H.put(renter_id,"00000000");
      H.put(today,dfLong.format(new java.util.Date()));
      H.put(tenant_name,eApplicant.getFullName());
      H.put(tenant_address,eApplicant.getLegalResidence());
      H.put(tenant_id,eApplicant.getSSN());
      H.put(apartment_name,eApartment.getName());
      H.put(apartment_floor, eFloor.getName());
      H.put(apartment_address,eBuilding.getStreet());
      H.put(apartment_campus, eComplex.getName());
      H.put(apartment_area,String.valueOf( eApartmentType.getArea()));
      H.put(apartment_roomcount,String.valueOf(eApartmentType.getRoomCount()));
      H.put(apartment_info,eApartmentType.getExtraInfo());
      H.put(contract_starts,dfLong.format(eContract.getValidFrom()));
      H.put(contract_ends,dfLong.format(eContract.getValidTo()));
      H.put(apartment_rent,nf.format((long)eApartmentType.getRent()));
      H.put(renting_index, String.valueOf( getTariffIndex()));
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
