package com.idega.block.albumcollection.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.Table;
import com.idega.block.albumcollection.business.AlbumCollectionBusiness;
import com.idega.block.albumcollection.data.Album;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.util.IWTimestamp;
import com.idega.block.albumcollection.data.Performer;
import com.idega.data.EntityFinder;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;

import java.util.List;
import java.util.Map;
import java.util.Iterator;

/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class AlbumList extends Block {

  public Link albumLinkTemplate;
  public Link updateAlbumLinkTemplate;
  public Link deleteAlbumLinkTemplate;
  public AlbumList.DefaultLayout layout;

  private final static String IW_BUNDLE_IDENTIFIER = AlbumCollection.IW_BUNDLE_IDENTIFIER;


  public AlbumList() {
    layout = new AlbumList.DefaultLayout();
    albumLinkTemplate = AlbumCollectionBusiness.getMainLinkClone();
    //albumLinkTemplate.setFontColor("#FFFFFF");
    updateAlbumLinkTemplate = AlbumCollectionBusiness.getMainLinkClone("Breyta");
    updateAlbumLinkTemplate.setWindowToOpen(com.idega.block.albumcollection.presentation.CreateAlbum.class);
    updateAlbumLinkTemplate.addParameter(AlbumCollectionBusiness._PRM_UPDATE,"true");

    deleteAlbumLinkTemplate = AlbumCollectionBusiness.getMainLinkClone("Eyða");
    deleteAlbumLinkTemplate.setWindowToOpen(DeleteConfirmWindow.class);
    deleteAlbumLinkTemplate.addParameter(AlbumCollectionBusiness._PRM_DELETE,AlbumCollectionBusiness._CONST_ALBUM);
  }


  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }


  public void main(IWContext iwc) throws Exception {
    List albumList = AlbumCollectionBusiness.getAlbums();
    this.empty();

    IWBundle core = iwc.getApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
    IWResourceBundle iwrb = this.getResourceBundle(iwc);

    Table contentTable = null;

    if(albumList != null){
      contentTable = new Table(1,albumList.size()*2+1);
      contentTable.setAlignment("center");
      contentTable.setHeight(1,"30");
      Map albumTypes = AlbumCollectionBusiness.getAlbumTypeNames();
      int index = 2;
      Iterator iter = albumList.iterator();
      while (iter.hasNext()) {
        Album item = (Album)iter.next();
        Link link = (Link)albumLinkTemplate.clone();
        //link.setText("Nánar");
        link.setObject(iwrb.getImage("more.gif"));
        link.addParameter(AlbumCollectionBusiness._PRM_ALBUM_ID,item.getID());
        link.addParameter(AlbumCollection._PRM_STATE,AlbumCollection._STATE_ALBUMINFO);
        AlbumList.DefaultLayout myLayout = (AlbumList.DefaultLayout)layout.clone();

        myLayout.setMoreLink(link);
        myLayout.setAlbumName(item.getName());
        if(item.getFrontCoverFileId() > 0){
          myLayout.setAlbumImage(new Image(item.getFrontCoverFileId()));
        }
        List performers = EntityFinder.findRelated(item,com.idega.block.albumcollection.data.PerformerBMPBean.getStaticInstance(Performer.class));
        if(performers != null){
          Iterator iter2 = performers.iterator();
          boolean f = false;
          String name = "";
          while (iter2.hasNext()) {
            Performer performer = (Performer)iter2.next();
            if(f){
              name += ", ";
            }
            name += performer.getDisplayName();
            f=true;
          }
          myLayout.setAlbumPerformers(name);
        }
        if(item.getPublishingDay() != null){
          myLayout.setAlbumPublishingDay(Integer.toString(new IWTimestamp(item.getPublishingDay()).getYear()));
        }

        if(item.getAlbumTypeId() > 0){
          String type = (String)albumTypes.get(new Integer(item.getAlbumTypeId()));
          if(type != null){
            myLayout.setAlbumType(type);
          }
        }

        if(hasEditPermission()){
          Link update = (Link)updateAlbumLinkTemplate.clone();
          update.setObject(core.getSharedImage("edit.gif","edit album"));
          update.addParameter(AlbumCollectionBusiness._PRM_ALBUM_ID,item.getID());
          myLayout.setEditLink(update);
          //contentTable.add(update,2,index);

          Link delete = (Link)deleteAlbumLinkTemplate.clone();
          delete.setObject(core.getSharedImage("delete.gif","delete album"));
          delete.addParameter(DeleteConfirmWindow._PRM_ID,item.getID());
          myLayout.setDeleteLink(delete);
          //contentTable.add(delete,3,index);
        }

        contentTable.add(myLayout,1,index);
        contentTable.setHeight(index+1,"20");

        index += 2;
      }
      
/*
      this.add(Text.getBreak());
      this.add(Text.getBreak());
      Table tbl = new Table(1,9);
      tbl.setAlignment("center");
      tbl.setHeight(1,2,"40");
      tbl.setHeight(1,4,"40");
      tbl.setHeight(1,6,"40");
      tbl.setHeight(1,8,"40");
      tbl.add((Table)layout.clone(),1,1);
      tbl.add((Table)layout.clone(),1,3);
      tbl.add((Table)layout.clone(),1,5);
      tbl.add((Table)layout.clone(),1,7);
      tbl.add((Table)layout.clone(),1,9);


      this.add(tbl);

*/



    }

    if(hasEditPermission()){
      Link createAlbumLink = AlbumCollectionBusiness.getMainLinkClone();
      createAlbumLink.setObject(core.getSharedImage("create.gif", "create album"));
      //createAlbumLink.setFontColor("#EEEEEE");
      createAlbumLink.setBold();
      createAlbumLink.setWindowToOpen(com.idega.block.albumcollection.presentation.CreateAlbum.class);
      if(contentTable == null){
        contentTable = new Table();
      }
      contentTable.add(createAlbumLink,1,1);
    }
    
	this.add(contentTable);

  }


  private class DefaultLayout extends Table{

    private Table tb;
    private Table tb2;
    private Table tb3;
    private Table tb4;

    public DefaultLayout(){

      super(1,1);
      this.setCellpadding(0);
      this.setCellspacing(1);
      this.setColor(AlbumCollection._COLOR_BRIGHTEST);
      this.setColor(1,1,AlbumCollection._COLOR_BRIGHT);
      this.setWidth(550);
      this.setHeight(110);
      this.setAlignment("center");

      tb = new Table(2,1);
      tb.setAlignment(1,1,"right");
      tb.setVerticalAlignment(1,1,"middle");
      //tb.setAlignment(2,1,"center");
      tb.setVerticalAlignment(2,1,"bottom");
      tb.setHeight(110);
      tb.setWidth(1,1,"450");
      tb.setWidth(2,1,"100");


      tb2 = new Table(2,1);
      tb2.setCellpadding(0);
      tb2.setCellspacing(0);
      tb2.setHeight(90);
      tb2.setWidth(1,1,"90");
      tb2.setWidth(2,1,"340");
      tb2.setColor(1,1,AlbumCollection._COLOR_BRIGHTEST);


      tb3 = new Table(2,4);
      tb3.setCellpadding(0);
      tb3.setCellspacing(3);
      tb3.setWidth(340);
      tb3.setWidth(1,1,"100");
      tb3.setWidth(2,1,"240");
      tb3.setHeight(90);
      tb3.setColumnAlignment(1,"right");
      tb3.setColumnAlignment(2,"left");
      tb3.setColumnColor(2,AlbumCollection._COLOR_DARK);

      tb4 = new Table(1,8);
      //tb4.setHeight(110);
      tb4.setHeight(8,"6");
      tb4.setHeight(6,"6");
      tb4.setHeight(4,"6");
      tb4.setHeight(2,"4");
      tb4.setWidth(100);
      tb4.setColumnAlignment(1,"center");

      initTexts();

    }

    private void initTexts(){
      tb3.add(AlbumCollectionBusiness.getMainTextClone("titill:"),1,1);
      tb3.add(AlbumCollectionBusiness.getMainTextClone("flytjendur:"),1,2);
      tb3.add(AlbumCollectionBusiness.getMainTextClone("útgáfuár:"),1,3);
      tb3.add(AlbumCollectionBusiness.getMainTextClone("tegund:"),1,4);

      for (int i = 0; i < 1; i++) {
        tb3.add(Text.getNonBrakingSpace(),2,1);
        tb3.add(Text.getNonBrakingSpace(),2,2);
        tb3.add(Text.getNonBrakingSpace(),2,3);
        tb3.add(Text.getNonBrakingSpace(),2,4);
      }

    }

    public void setAlbumImage(Image image){
      image.setWidth(90);
      image.setHeight(90);
      tb2.add(image,1,1);
    }

    public void setAlbumName(String name){
      tb3.add(AlbumCollectionBusiness.getMainTextBoldClone(name),2,1);
    }

    public void setAlbumPerformers(String performers){
      tb3.add(AlbumCollectionBusiness.getMainTextBoldClone(performers),2,2);
    }

    public void setAlbumPublishingDay(String publishingDay){
      tb3.add(AlbumCollectionBusiness.getMainTextBoldClone(publishingDay),2,3);
    }

    public void setAlbumType(String type){
      tb3.add(AlbumCollectionBusiness.getMainTextBoldClone(type),2,4);
    }

    public void setMoreLink(Link link){
      tb4.add(link,1,7);
    }

    public void setEditLink(Link link){
      tb4.add(link,1,5);
    }

    public void setDeleteLink(Link link){
      tb4.add(link,1,5);
    }


    public synchronized Object clone(){
      DefaultLayout l = (DefaultLayout)super.clone();
      l.tb = (Table)this.tb.clone();
      l.tb2 = (Table)this.tb2.clone();
      l.tb3 = (Table)this.tb3.clone();
      l.tb4 = (Table)this.tb4.clone();
      return l;
    }


    public void main(IWContext iwc) throws Exception {
      this.empty();
      this.add(tb,1,1);
      tb.emptyCell(1,1);
      tb.add(tb2,1,1);
      tb2.emptyCell(2,1);
      tb2.add(tb3,2,1);
      tb.emptyCell(2,1);
      tb.add(tb4,2,1);
    }

  } // innerclass ends




}
