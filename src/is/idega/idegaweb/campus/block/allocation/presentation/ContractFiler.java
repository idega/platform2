package is.idega.idegaweb.campus.block.allocation.presentation;





import java.util.StringTokenizer;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.lowagie.text.Font;



/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved

 * Company:      idega

  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>

 * @version 1.1

 */



public class ContractFiler extends Block {



  public static String prmOneId ="contract_id" ,prmTest="test",prmManyIds="many_ids";

  public static String prmSeperator = "_",prmFileName = "fname";

  public ContractFiler() {

  }



  public void main(IWContext iwc){



    //IWContext iwc = getIWContext();

    //IWMainApplication iwma = iwc.getApplication()

    String identifier = "is.idega.idegaweb.campus.block.contract";

    IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(identifier).getResourceBundle(iwc);

    String fileSeperator = System.getProperty("file.separator");

    String filepath = iwc.getServletContext().getRealPath(fileSeperator+"allocation/files"+fileSeperator);

    String prefFilename = iwc.getParameter("fname");

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

      if(iwc.getParameter(prmOneId)!=null){

        int id = Integer.parseInt(iwc.getParameter(prmOneId));

        boolean filewritten = false;//CampusContractWriter.writePDF(id,iwrb,path+filename, nameFont,titleFont, paraFont, tagFont, textFont);



        if(filewritten)

          p.setToRedirect("/servlet/pdf?&dir="+path+filename,1);

        else

          add("failed");



      }

      else if(iwc.getParameter(prmTest)!=null){



       boolean filewritten = false;//CampusContractWriter.writeTestPDF(iwrb,path+filetest,  nameFont,titleFont, paraFont, tagFont, textFont);

        if(filewritten)

          p.setToRedirect("/servlet/pdf?&dir="+path+filetest,1);

        else

          add("failed");

      }

      else if(iwc.getParameter(prmManyIds)!=null){

        //System.err.println(prmManyIds);

        String values = iwc.getParameter(prmManyIds);

        StringTokenizer st = new StringTokenizer(values,prmSeperator);

        int[] ids = new int[st.countTokens()];

        for (int i = 0; i < ids.length; i++) {

          String token = st.nextToken();

          ids[i] = Integer.parseInt(token);

        }

        /*

        IWTimestamp it = IWTimestamp.RightNow();

        StringBuffer f = new StringBuffer();

        f.append(it.getMonth());

        f.append(it.getDay());

        f.append(it.getHour());

        f.append(it.getMinute());

        f.append(it.getSecond());

        f.append(".pdf");

        String fname = f.toString();

        */



        boolean filewritten = false;//CampusContractWriter.writePDF(ids,iwrb,path+filetest,  nameFont,titleFont, paraFont, tagFont, textFont);

        if(filewritten)

          p.setToRedirect("/servlet/pdf?&dir="+path+filetest,1);

        else

          add("failed");



      }

      else{add("nothing");}

      p.setParentToReload();

  }



}

