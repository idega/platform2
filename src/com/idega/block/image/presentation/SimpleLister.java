package com.idega.block.image.presentation;

import java.sql.SQLException;
import java.util.List;

import com.idega.block.image.data.ImageEntity;
import com.idega.core.file.data.ICFileBMPBean;
import com.idega.data.EntityFinder;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.util.IWTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class SimpleLister extends PresentationObjectContainer {

    private String target = "viewer";
    public String prmImageView = "img_view_id";
    public String sessImageParameterName = "im_image_session_name";
    public String sessImageParameter = "image_id";

    public void  main(IWContext iwc){
      //add("block.media");
      getParentPage().setAllMargins(0);
      List L = listOfImages();

      checkParameterName(iwc);

      if(L!= null){
        Table Frame = new Table();
          Frame.setWidth("100%");
        Frame.setCellpadding(0);
        Frame.setCellspacing(0);
        Table T = new Table();
          T.setWidth("100%");
        int len = L.size();
        int row = 1;
        T.add(formatText("Pictures"),1,row++);
        for (int i = 0; i < len; i++) {
          ImageEntity image = (ImageEntity) L.get(i);
          T.add(getImageLink(image,this.target,this.prmImageView),1,row);
          /**@todo: localize
           *
           */
          T.add(formatText(new IWTimestamp(image.getCreationDate() ).getISLDate(".",true)),2,row);
          row++;
        }
        T.setCellpadding(2);
        T.setCellspacing(0);

        T.setHorizontalZebraColored("#CBCFD3","#ECEEF0");
        Frame.add(T,1,1);
        add(Frame);
      }
    }

  public void checkParameterName(IWContext iwc){
     if(iwc.getParameter(this.sessImageParameterName)!=null){
      this.sessImageParameter = iwc.getParameter(this.sessImageParameterName);
      iwc.setSessionAttribute(this.sessImageParameterName,this.sessImageParameter);
    }
    else if(iwc.getSessionAttribute(this.sessImageParameterName)!=null) {
		this.sessImageParameter = (String) iwc.getSessionAttribute(this.sessImageParameterName);
	}
  }

  public Link getImageLink(ImageEntity image,String target,String prm){
    Link L = new Link(formatText(image.getName()),SimpleViewer.class);
    L.setFontSize(1);
    L.setOnClick("top.iImageId = "+image.getPrimaryKey().toString() );
    L.addParameter(this.sessImageParameter,image.getPrimaryKey().toString());
    L.setTarget(target);
    return L;
  }

  public List listOfImages(){
    List L = null;
    try {
      ImageEntity image = ((com.idega.block.image.data.ImageEntityHome)com.idega.data.IDOLookup.getHomeLegacy(ImageEntity.class)).createLegacy();
      StringBuffer sql = new StringBuffer("select f.* ");
      sql.append(" from ic_file f, ic_mime_type m ,ic_file_type t ");
      sql.append(" where f.mime_type = m.mime_type ");
      sql.append(" and m.ic_file_type_id = t.ic_file_type_id ");
      sql.append(" and t.unique_name = 'ic_image' ");
      sql.append(" order by ").append(ICFileBMPBean.getColumnNameCreationDate()).append(" desc ");
      //EntityFinder.debug = true;
      //TODO - ARON - move into ICFileBMPBean
      L = EntityFinder.findAll(image,sql.toString());
      //EntityFinder.debug = false;
    }
    catch (SQLException ex) {
      L = null;
    }
    return L;
  }

  public Text formatText(String s){
    Text T= new Text();
    if(s!=null){
      T= new Text(s);

      T.setFontColor("#000000");
      T.setFontSize(1);
    }
    return T;
  }
  public Text formatText(int i){
    return formatText(String.valueOf(i));
  }
}
