package is.idegaweb.campus.allocation;

import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.Page;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.jmodule.object.ModuleInfo;
import com.lowagie.text.Font;
import java.util.StringTokenizer;
import com.idega.util.idegaTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class ContractFiler extends JModuleObject {

  public  String prmOneId ="contract_id" ,prmTest="test",prmManyIds="many_ids";
  public  String prmSeperator = "_",prmFileName = "fname";
  public ContractFiler() {
  }

  public void main(ModuleInfo modinfo){

    //ModuleInfo modinfo = getModuleInfo();
    //IWMainApplication iwma = modinfo.getApplication()
    String identifier = "is.idegaweb.campus.contract";
    IWResourceBundle iwrb = modinfo.getApplication().getBundle(identifier).getResourceBundle(modinfo);
    String fileSeperator = System.getProperty("file.separator");
    String filepath = modinfo.getServletContext().getRealPath(fileSeperator+"allocation/files"+fileSeperator);
    String prefFilename = modinfo.getParameter("fname");
    String filename = "contract.pdf";
    String filetest = "test.pdf";
    Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
    Font paraFont = new Font(Font.HELVETICA, 10, Font.BOLD);
    Font nameFont = new Font(Font.HELVETICA, 12, Font.BOLDITALIC);
    Font tagFont = new Font(Font.HELVETICA,9,Font.BOLDITALIC);
    Font textFont = new Font(Font.HELVETICA, 8, Font.NORMAL);

    if(prefFilename != null){
      filename = prefFilename+".pdf";
    }
    String path = filepath;
      Page p = getParentPage();
      //Page p = getPage();
      if(modinfo.getParameter(prmOneId)!=null){
        int id = Integer.parseInt(modinfo.getParameter(prmOneId));
        boolean filewritten = CampusContractWriter.writePDF(id,iwrb,path+filename, nameFont,titleFont, paraFont, tagFont, textFont);

        if(filewritten)
          p.setToRedirect("/servlet/pdf?&dir="+path+filename,1);
        else
          add("failed");

      }
      else if(modinfo.getParameter(prmTest)!=null){

        boolean filewritten = CampusContractWriter.writeTestPDF(iwrb,path+filetest,  nameFont,titleFont, paraFont, tagFont, textFont);
        if(filewritten)
          p.setToRedirect("/servlet/pdf?&dir="+path+filetest,1);
        else
          add("failed");
      }
      else if(modinfo.getParameter(prmManyIds)!=null){
        System.err.println(prmManyIds);
        String values = modinfo.getParameter(prmManyIds);
        StringTokenizer st = new StringTokenizer(values,prmSeperator);
        int[] ids = new int[st.countTokens()];
        for (int i = 0; i < ids.length; i++) {
          String token = st.nextToken();
          ids[i] = Integer.parseInt(token);
        }
        /*
        idegaTimestamp it = idegaTimestamp.RightNow();
        StringBuffer f = new StringBuffer();
        f.append(it.getMonth());
        f.append(it.getDay());
        f.append(it.getHour());
        f.append(it.getMinute());
        f.append(it.getSecond());
        f.append(".pdf");
        String fname = f.toString();
        */

        boolean filewritten = CampusContractWriter.writePDF(ids,iwrb,path+filetest,  nameFont,titleFont, paraFont, tagFont, textFont);
        if(filewritten)
          p.setToRedirect("/servlet/pdf?&dir="+path+filetest,1);
        else
          add("failed");

      }
      else{add("nothing");}
      p.setParentToReload();
  }

}