/*
 * $Id: CampusContractWriter.java,v 1.3 2001/08/01 10:31:06 aron Exp $
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
  public final static String apartment_area = "[apartment_area]";
  public final static String apartment_floor = "[apartment_floor]";
  public final static String apartment_info = "[apartment_info]";

  public final static String apartment_roomcount = "[apartment_roomcount]";
  public final static String contract_starts = "[contract_starts]";
  public final static String contract_ends = "[contract_ends]";

  public  static String[] TAGS = {renter_name,renter_address,renter_id,
                            tenant_name,tenant_address,tenant_id,
                            apartment_name,apartment_floor,apartment_address,apartment_area,
                            apartment_roomcount,apartment_info,
                            contract_starts,contract_ends};

  public final static String IS ="IS";
  public final static String EN ="EN";
  public static boolean writePDF(int id,IWResourceBundle iwrb,String realpath){
    boolean returner = false;
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
        Font textFont = new Font(Font.HELVETICA, 8, Font.NORMAL);
        Font filledFont = new Font(Font.HELVETICA,8,Font.BOLD);
        filledFont.setStyle("underline");

        Paragraph cTitle = new Paragraph("Húsaleigusamningur Stúdentagarða " , chapterFont);
        Chapter chapter = new Chapter(cTitle, 1);
        chapter.setNumberDepth(0);
        List L = listOfTexts();
        if(L!=null){
          int len = L.size();
          for (int i = 0; i < len; i++) {
            ContractText CT = (ContractText) L.get(i);
            Paragraph name = new Paragraph(CT.getName(),nameFont);
            name.setAlignment(Element.ALIGN_LEFT);
            String sText = CT.getText();
            if(CT.getUseTags()){
              sText = getDeTaggedParagraph(sText,iwrb,id);
            }
            Paragraph text = new Paragraph(sText,textFont);
            text.setAlignment(Element.ALIGN_LEFT);
            chapter.add(name);
            chapter.add(text);
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
    try {
      Contract C = new Contract(id);
      C.setStatusPrinted();
      C.update();
    }
    catch (SQLException ex) {
      returner = false;
    }
    return returner;
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

  private static String getDeTaggedParagraph(String sParagraph,IWResourceBundle iwrb,int contractId){
    String returnString = sParagraph;
    for (int i = 0; i < TAGS.length; i++) {
      int index = returnString.indexOf(TAGS[i]);
      if(index != -1){
        String head = returnString.substring(0,index);
        String tail = returnString.substring(index+TAGS[i].length());
        String mid =  getTagString(TAGS[i],contractId,iwrb);
        returnString = head+ mid+tail;

      }
      System.err.println("returnString: "+returnString);
    }

    return returnString;
  }

  private static String getTagString(String tag,int contractId,IWResourceBundle iwrb){
     String s = "";
     try {
      Contract eContract = new Contract(contractId);
      Applicant eApplicant = new Applicant(eContract.getApplicantId().intValue());
      Apartment eApartment = new Apartment(eContract.getApartmentId().intValue());
      ApartmentType eApartmentType = new ApartmentType(eApartment.getApartmentTypeId());
      Floor eFloor = new Floor(eApartment.getFloorId());
      Building eBuilding = new Building(eFloor.getBuildingId());
      Complex eComplex = new Complex(eBuilding.getComplexId());
      if(tag.equalsIgnoreCase(renter_name)){

      }
      else if(tag.equalsIgnoreCase(renter_address)){

      }
      else if(tag.equalsIgnoreCase(renter_id)){

      }
      else if(tag.equalsIgnoreCase(tenant_name)){
        s = eApplicant.getFullName();

      }
      else if(tag.equalsIgnoreCase(tenant_address)){
        s = eApplicant.getLegalResidence();
      }
      else if(tag.equalsIgnoreCase(tenant_id)){
        s = eApplicant.getSSN();
      }
      else if(tag.equalsIgnoreCase(apartment_name)){
        s = eApartment.getName();
      }
      else if(tag.equalsIgnoreCase(apartment_floor)){
        s = eFloor.getName();
      }
      else if(tag.equalsIgnoreCase(apartment_address)){
        s = eComplex.getName()+" "+eBuilding.getStreet();
      }
      else if(tag.equalsIgnoreCase(apartment_area)){
        s = String.valueOf(eApartmentType.getArea());
      }
      else if(tag.equalsIgnoreCase(apartment_roomcount)){
        s = String.valueOf(eApartmentType.getRoomCount());
      }
      else if(tag.equalsIgnoreCase(apartment_info)){
        s = (eApartmentType.getExtraInfo());
      }
      else if(tag.equalsIgnoreCase(contract_starts)){
        s = new idegaTimestamp(eContract.getValidFrom()).getISLDate("/",true);
      }
      else if(tag.equalsIgnoreCase(contract_ends)){
        s = new idegaTimestamp(eContract.getValidTo()).getISLDate("/",true);
      }

    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return s;

  }

  public static String[] getTags(){
    return TAGS;
  }

}
