package com.idega.block.news.presentation;

import com.idega.block.IWBlock;
import com.idega.block.news.presentation.NewsTable;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.business.*;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.block.news.business.*;
import com.idega.core.user.data.User;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.text.DateFormat;
import com.idega.util.*;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.block.news.data.*;
import com.idega.block.text.data.Content;
import com.idega.data.*;
import com.idega.util.text.*;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.core.data.ICFile;
import java.text.DateFormat;
import com.idega.util.text.TextStyler;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class NewsReader extends Block implements IWBlock{

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.news";
  private boolean isAdmin=false;
  private int iCategoryId = -1;
  private String attributeName = null;
  private int attributeId = -1;
  private User eUser = null;

  private boolean showNewsCollectionButton=true;
  private int categoryId = 0;


  private Table outerTable = new Table(1,1);

  private int numberOfLetters = 273;
	private int numberOfHeadlineLetters = -1;
  private int numberOfDisplayedNews = 5;
  private int numberOfExpandedNews = 3;
	private int numberOfCollectionNews = 5;
  private int iSpaceBetween = 1;
	private int cellPadding = 0;
  private int cellSpacing = 0;
	private int viewPageId = -1;
	private int textSize = 2;
  private boolean backbutton = false;
  private boolean showAll = false;
  private boolean showImages = true;
  private boolean showOnlyDates = false;
	private boolean showTime = true;
	private boolean showTimeFirst = false;
  private boolean headlineAsLink = false;
  private boolean showHeadlineImage = false;
  private boolean showMoreButton = true;
  private boolean alignWithHeadline = false;
  private boolean limitNumberOfNews = false;
  private boolean enableDelete=true;
	private boolean viewNews = true;
	private boolean newobjinst = false;
	private String outerTableWidth = "100%";
	private String sObjectAlign = "center";
  private String headlineImageURL;
	private String firstTableColor = null;
	private String secondTableColor = null;
	private String dateAlign = "left";
	private Image headlineImage = null;

  private Hashtable objectsBetween = null;
	private Text textProxy = new Text();
  private Text headlineProxy  = new Text();
  private Text informationProxy  = new Text();


  private static String prmDelete = "nwr_delete";
  private static String prmEdit = "nwr_edit";
  private static String prmNew = "nwr_new";
  private static String prmMore = "nwr_more";
	private static String prmCollection = "nwr_collection";
	private static String prmObjIns = "nwr_instance_id";

  public static String prmListCategory = "nwr_newscategoryid";
  public static String prmNewsCategoryId = "nwr_listcategory";

  private IWBundle iwb;
  private IWResourceBundle iwrb ;

  public static final int SINGLE_FILE_LAYOUT = NewsLayoutHandler.SINGLE_FILE_LAYOUT;
  public static final int NEWS_SITE_LAYOUT = NewsLayoutHandler.NEWS_SITE_LAYOUT;
  public static final int NEWS_PAPER_LAYOUT = NewsLayoutHandler.NEWS_PAPER_LAYOUT;
	public static final int SINGLE_LINE_LAYOUT = NewsLayoutHandler.SINGLE_LINE_LAYOUT;
  public static final int COLLECTION_LAYOUT = NewsLayoutHandler.COLLECTION_LAYOUT;

  private int iLayout =SINGLE_FILE_LAYOUT;

  public NewsReader(){
    init();
    showAll = true;
  }

  public NewsReader(int iCategoryId){
    this();
    this.iCategoryId=iCategoryId;
    this.showAll = false;
  }
/*
  public NewsViewer(String date){
    this.date=date;
    this.showAll=true;
  }

  public NewsViewer(idegaTimestamp timestamp){
    this.showAll=true;
    this.date = timestamp.toSQLString();
  }

  public NewsViewer(int iCategoryId, String date){
    this.iCategoryId=iCategoryId;
    this.date=date;
    this.showAll = false;
  }

  public NewsViewer(int iCategoryId, idegaTimestamp timestamp){
    this.showAll=false;
    this.iCategoryId=iCategoryId;
    this.date = timestamp.toSQLString();
  }
*/
  private void init(){
		//headlineProxy.setFontStyle(TextStyler.getStyle(null,Text.FONT_FACE_STYLE_BOLD,null,"10",null));
		//informationProxy.setFontStyle(TextStyler.getStyle(null,null,"666666",null,null));
		//textProxy.setFontStyle(TextStyler.getDefaultStyle());
    headlineProxy.setBold();
    informationProxy.setFontColor("#666666");
    textProxy.setFontSize(1);
    informationProxy.setFontSize(1);

  }

  private void checkCategories(){

  }

  private void control(IWContext iwc){
    Locale locale = iwc.getCurrentLocale();
		 String sNewsId = null;
		if(viewNews)
      sNewsId = iwc.getParameter(prmMore+getICObjectInstanceID());
    NewsCategory newsCategory = null;
		String prm = prmListCategory+getICObjectInstanceID();
		boolean info = false;
		if(iwc.isParameterSet(prm)){
		  if(iwc.getParameter(prm).equalsIgnoreCase("true"))
				info = true;
			else
				info = false;
		}

    if(iCategoryId <= 0){
      String sCategoryId = iwc.getParameter(prmNewsCategoryId );
      if(sCategoryId != null)
        iCategoryId = Integer.parseInt(sCategoryId);
      else if(getICObjectInstanceID() > 0){
        iCategoryId = NewsFinder.getObjectInstanceCategoryId(getICObjectInstanceID(),true);
        if(iCategoryId <= 0 ){
          newobjinst = true;
        }
      }
    }
    Table T = new Table(1,1);
    T.setCellpadding(0);
    T.setCellpadding(0);
    T.setWidth( "100%");
    if(isAdmin){
      T.add(getAdminPart(iCategoryId,false,newobjinst,info,iwc),1,1);
    }
    if(iCategoryId >0){
      newsCategory = NewsFinder.getNewsCategory(iCategoryId);
      if(newsCategory != null){
        if(sNewsId != null){
          int id = Integer.parseInt(sNewsId);
          NewsHelper nh = NewsFinder.getNewsHelper(id);
          T.add(getNewsTable(nh,newsCategory,locale,true,false,iwc),1,1);
        }
        else if(info){
          T.add(getCategoryList(newsCategory,locale,iwc),1,1);
        }
        else
          T.add(publishNews(iwc,newsCategory,locale,iwc.isParameterSet(prmCollection+getICObjectInstanceID())),1,1);
      }
    }
    else{
      T.add(new Text(iwrb.getLocalizedString("no_news_category","No news category")));
    }
    super.add(T);
  }

  private PresentationObject getAdminPart(int iCategoryId,boolean enableDelete,boolean newObjInst,boolean info,IWContext iwc){
    Table T = new Table(3,1);
    T.setCellpadding(2);
    T.setCellspacing(2);
    T.setBorder(0);

		IWBundle core = iwc.getApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
    if(iCategoryId > 0){
      Link ne = new Link(core.getImage("/shared/create.gif"));
      ne.setWindowToOpen(NewsEditorWindow.class);
      ne.addParameter(NewsEditorWindow.prmCategory,iCategoryId);
      T.add(ne,1,1);
		  T.add(T.getTransparentCell(iwc),1,1);
      Link list = new Link(iwb.getImage("/shared/info.gif"));
			list.addParameter(prmObjIns,getICObjectInstanceID());
      if(!info)
        list.addParameter(prmListCategory+getICObjectInstanceID(),"true");
			else
				list.addParameter(prmListCategory+getICObjectInstanceID(),"false");
      T.add(list,1,1);
		  T.add(T.getTransparentCell(iwc),1,1);
      Link change = new Link(core.getImage("/shared/edit.gif"));
      change.setWindowToOpen(NewsEditorWindow.class);
      change.addParameter(NewsEditorWindow.prmCategory,iCategoryId);
      change.addParameter(NewsEditorWindow.prmObjInstId,getICObjectInstanceID());
      T.add(change,1,1);

      if ( enableDelete ) {
				T.add(T.getTransparentCell(iwc),1,1);
        Link delete = new Link(core.getImage("/shared/delete.gif"));
        delete.setWindowToOpen(NewsEditorWindow.class);
        delete.addParameter(NewsEditorWindow.prmDelete,iCategoryId);
        T.add(delete,3,1);
      }
    }
    if(newObjInst){
      Link newLink = new Link(core.getImage("/shared/create.gif"));
      newLink.setWindowToOpen(NewsEditorWindow.class);
      if(newObjInst)
        newLink.addParameter(NewsEditorWindow.prmObjInstId,getICObjectInstanceID());

      T.add(newLink,2,1);
    }
    T.setWidth("100%");
    return T;
  }



  private PresentationObject getCategoryList(NewsCategory newsCategory,Locale locale,IWContext iwc){
    List L = NewsFinder.listOfAllNewsHelpersInCategory(newsCategory.getID(),50,locale);
    Table T = new Table();
    int row = 1;
    if(L != null){
      Iterator I = L.iterator();
      NewsHelper newsHelper;
      while(I.hasNext()){
        newsHelper = (NewsHelper) I.next();
        T.add(getNewsOverViewTable(newsHelper,newsCategory,locale,iwc),1,row++);
      }
    }
    else{
      T.add(new Text(iwrb.getLocalizedString("no_news","No News")));
    }

    return T;
  }

  private PresentationObject getNewsOverViewTable(NewsHelper newsHelper,NewsCategory newsCategory, Locale locale,IWContext iwc){
    Table T = new Table(1,4);
    T.setCellpadding(0);
    T.setCellspacing(0);
    T.setBorder(0);
    T.setWidth("100%");
    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,locale);
    ContentHelper contentHelper = newsHelper.getContentHelper();
    NwNews news = newsHelper.getNwNews();
    LocalizedText locText = contentHelper.getLocalizedText(locale);
    Text newsInfo = getInfoText(news,newsHelper.getContentHelper().getContent(), newsCategory.getName(),locale,showOnlyDates,showTime,showTimeFirst);

    String sNewsBody = "";
    String sHeadline = "";

    if(locText!=null){
      sHeadline = locText.getHeadline();
      sNewsBody =  locText.getBody();
    }

    int letterCount = sNewsBody.length();
    int fileCount = 0;

    List files = newsHelper.getContentHelper().getFiles();
    if(files!=null){
      fileCount = files.size();
    }

    Text hLetters = formatText(iwrb.getLocalizedString("letters","Letters")+" : ",true);
    Text hFiles = formatText(iwrb.getLocalizedString("files","Files")+" : ",true);
    Text hFrom = formatText( iwrb.getLocalizedString("publish_from","Publish from")+" : ",true);
    Text hTo = formatText(iwrb.getLocalizedString("publish_to","Publish to")+" : ",true);
    Text tLetters = formatText(String.valueOf(letterCount ),false);
    Text tFiles = formatText(String.valueOf(fileCount ),false);

    idegaTimestamp now = idegaTimestamp.RightNow();
    idegaTimestamp from = new idegaTimestamp(newsHelper.getContentHelper().getContent().getPublishFrom());
    idegaTimestamp to = new idegaTimestamp(newsHelper.getContentHelper().getContent().getPublishTo());

    Text tFrom = formatText(df.format((java.util.Date)from.getTimestamp()),false);
    Text tTo = formatText(df.format((java.util.Date)to.getTimestamp()),false);
		tFrom.setBold();
		tTo.setBold();
		// Unpublished
    if(from.isLaterThan(now)){
      tFrom.setFontColor("#FFDE00");
      tTo.setFontColor("#FFDE00");
    }
		// Published
    else if(now.isLaterThan(to)){
      tFrom.setFontColor("#CC3300");
      tTo.setFontColor("#CC3300");
    }
		// Publishing
    else if(now.isLaterThan(from) && to.isLaterThan(now)){
      tFrom.setFontColor("#333399");
      tTo.setFontColor("#333399");
    }

    Text headLine = new Text(sHeadline);
    newsInfo = setInformationAttributes(newsInfo);
    headLine = setHeadlineAttributes(headLine);


    Table infoTable = new Table();
      infoTable.add(hLetters,1,1);
      infoTable.add(tLetters ,2,1);
      infoTable.add(hFiles ,1,2);
      infoTable.add(tFiles ,2,2);
      infoTable.add(hFrom,3,1);
      infoTable.add(tFrom,4,1);
      infoTable.add(hTo,3,2);
      infoTable.add(tTo,4,2);


    T.add(newsInfo,1,1);
    T.add(headLine,1,2);
    T.add(infoTable,1,3);

    Link moreLink = new Link(iwrb.getImage("more.gif"));
    moreLink.addParameter(prmMore+getICObjectInstanceID(),news.getID());
    T.add(moreLink, 1, 4);
    if(isAdmin){
      T.add(getNewsAdminPart(news,iwc),1,4);
    }
    return T;
  }

  private Text formatText(String text,boolean bold){
    Text T = new Text(text);
    T.setFontSize(2);
    T.setBold(bold);
    return T;
  }

  private PresentationObject publishNews(IWContext iwc ,NewsCategory newsCategory,Locale locale,boolean collection){
		List L = null;
		if(iLayout == COLLECTION_LAYOUT || collection){
		  L = NewsFinder.listOfAllNewsHelpersInCategory(newsCategory.getID(),numberOfCollectionNews,locale);
		}
		else{
      L = NewsFinder.listOfNewsHelpersInCategory(newsCategory.getID(),numberOfDisplayedNews,locale );
		}
    NewsTable T = new NewsTable(NewsTable.NEWS_SITE_LAYOUT ,cellPadding,cellSpacing,firstTableColor,secondTableColor);
		int count = NewsFinder.countNewsInCategory(newsCategory.getID());
    boolean useDividedTable = iLayout == NEWS_SITE_LAYOUT ? true:false;
    if(L!=null){
      int len = L.size();
      Integer I;
      NewsHelper newsHelper;
      for (int i = 0; i < len; i++) {
				if (numberOfExpandedNews == i)
					collection = true;
        newsHelper = (NewsHelper) L.get(i);
        I = new Integer(i);
        if(objectsBetween != null && objectsBetween.containsKey(I)){
          Table t = new Table(1,1);
          t.setCellpadding(4);
          t.add((PresentationObject)objectsBetween.get(I));
          T.add(t,sObjectAlign );
          objectsBetween.remove(I);
        }
        T.add(getNewsTable(newsHelper,newsCategory,locale ,false,collection,iwc),useDividedTable,"left");
      }
			// news collection
			if(showNewsCollectionButton){
			  if(len < count && !collection){
				  Link collectionLink = new Link(iwrb.getImage("collection.gif"));
				  collectionLink.addParameter(prmNewsCategoryId,newsCategory.getID());
				  collectionLink.addParameter(prmCollection+getICObjectInstanceID(),"true");
				  T.add(collectionLink);
			  }
			  else if(collection){
			    T.add(new BackButton(iwrb.getImage("back.gif")));
				}
			}
			// Finish objectsbetween
      if(objectsBetween != null && objectsBetween.size() > 0){
        Vector V = new Vector(objectsBetween.values());
        Collections.reverse(V);
        Iterator iter = V.iterator();
        while(iter.hasNext()){
          T.add((PresentationObject)iter.next(),sObjectAlign );
        }
      }

    }
    else{
      T.add(new Text(iwrb.getLocalizedString("no_news","No News")));
    }
   return(T);
  }

  // Make a table around each news
  private PresentationObject getNewsTable(NewsHelper newsHelper,NewsCategory newsCategory, Locale locale,boolean showAll,boolean collection,IWContext iwc){
    Table T = new Table();
    T.setCellpadding(0);
    T.setCellspacing(0);
    T.setBorder(0);
    T.setWidth("100%");

    ContentHelper contentHelper = newsHelper.getContentHelper();
    NwNews news = newsHelper.getNwNews();
    LocalizedText locText = contentHelper.getLocalizedText(locale);

		if(iLayout == SINGLE_LINE_LAYOUT)
			showOnlyDates = true;

		String sNewsBody = "";
    String sHeadline = "";

		if(locText!=null){
      sHeadline = locText.getHeadline();
    }

		// shortening headlinestext
		if(!showAll && numberOfHeadlineLetters > -1 && sHeadline.length() >= numberOfHeadlineLetters ){
			sHeadline=sHeadline.substring(0,numberOfHeadlineLetters)+"...";
		}

		Text headLine = new Text(sHeadline);

    Text newsInfo = getInfoText(news,newsHelper.getContentHelper().getContent(), newsCategory.getName(),locale,showOnlyDates,showTime,showTimeFirst);
		newsInfo = setInformationAttributes(newsInfo);
		headLine = setHeadlineAttributes(headLine);


		// Check if using single_line_layout
		if(iLayout != SINGLE_LINE_LAYOUT){

			T.add(newsInfo,1,1);

			if(locText!=null && !collection){
        sNewsBody =  locText.getBody();

				// shortening newstext
				if(!showAll && sNewsBody.length() >= numberOfLetters){
					sNewsBody=sNewsBody.substring(0,numberOfLetters)+"...";
				}

				sNewsBody = NewsFormatter.formatNews(sNewsBody,String.valueOf(textSize));

				Text newsBody = new Text(sNewsBody);
				newsBody = setTextAttributes(newsBody);

				if(showImages){
					//if (news.getImageId()!= -1 && showImages && news.getIncludeImage()){
					List files = newsHelper.getContentHelper().getFiles();
					if(files!=null){
						try{
						Table imageTable = new Table(1, 2);
						ICFile imagefile = (ICFile)files.get(0);
						int imid = imagefile.getID();
						String att = imagefile.getMetaData(NewsEditorWindow.imageAttributeKey);

						Image newsImage = new Image(imid);
						if(att != null)
							newsImage.setAttributes(getAttributeMap(att));

						  T.add(newsImage,1,3);
						}
						catch(SQLException ex){
							ex.printStackTrace();
						}
					}
				}

			  T.add(newsBody,1,3);
			}
			if( showAll ) {
				T.add(new BackButton(iwrb.getImage("back.gif")), 1, 4);
			}
			//  add news
			if(!showAll && showMoreButton){
				Link moreLink = new Link(iwrb.getImage("more.gif"));
				moreLink.addParameter(prmMore+getICObjectInstanceID(),news.getID());
				if(viewPageId > 0)
					moreLink.setPage(viewPageId);
				T.add(moreLink, 1, 4);
			}

			if ( alignWithHeadline ){
				if(headlineImage !=null)
					T.add(headlineImage, 1, 2);
				if(headlineImageURL!=null)
					T.add(iwb.getImage(headlineImageURL), 1, 2);
      }


			if ( headlineAsLink ) {
				Link headlineLink = new Link(headLine);
				headlineLink.addParameter(prmMore+getICObjectInstanceID(),news.getID());
				if(viewPageId > 0)
					headlineLink.setPage(viewPageId);
				T.add(headlineLink, 1, 2);
			}
			else {
				T.add(headLine, 1, 2);
			}
			if(isAdmin){
				T.add(getNewsAdminPart(news,iwc),1,4);
			}
		}
		// if single line view
		else{
			int headlineCol = 3;
			int dateCol = 1;
			if(dateAlign.toLowerCase().equals("right")){
			  headlineCol = 1;
				dateCol = 3;
			}

			T.add(newsInfo,dateCol,1);
			T.setAlignment(headlineCol,1,"left");
			T.setAlignment(4,1,"right");
			T.setWidth(headlineCol,1,"100%");
			T.setWidth(dateCol,1,"45");
			T.add("&nbsp;&nbsp",2,1);
		  if ( headlineAsLink ) {
				Link headlineLink = new Link(headLine);
				headlineLink.addParameter(prmMore+getICObjectInstanceID(),news.getID());
				if(viewPageId > 0)
					headlineLink.setPage(viewPageId);
				T.add(headlineLink, headlineCol, 1);
			}
			else {
				T.add(headLine, headlineCol, 1);
			}
			if(isAdmin){
			  T.add(getNewsAdminPart(news,iwc),4,1);
			}
		}
    return T;
  }

  private PresentationObject getNewsAdminPart(NwNews news,IWContext iwc){
    Table links = new Table(3,1);
      Link newsEdit = new Link(iwb.getImage("/shared/edit.gif"));
      newsEdit.setWindowToOpen(NewsEditorWindow.class);
      newsEdit.addParameter(NewsEditorWindow.prmNwNewsId,news.getID());

      Link newsDelete = new Link(iwb.getImage("/shared/delete.gif"));
      newsDelete.setWindowToOpen(NewsEditorWindow.class);
      newsDelete.addParameter(NewsEditorWindow.prmDelete,news.getID());

      //links.setAlignment(1,1,"left");
      //links.setAlignment(2,1,"right");
      links.setCellpadding(0);
			links.setCellspacing(0);
      links.add(newsEdit,1,1);
			links.add(links.getTransparentCell(iwc),2,1);
      links.add(newsDelete,3,1);
    return links;
  }

  private Text getInfoText(NwNews nwNews,Content content ,String sCategoryName,Locale locale, boolean ifUseOnlyDates,boolean ifShowTime,boolean ifShowTimeFirst){
    return new Text(NewsFormatter.getInfoText(nwNews,content,sCategoryName,locale,ifUseOnlyDates,ifShowTime,ifShowTimeFirst) );
  }

  public void main(IWContext iwc)throws Exception{
    try {
      //isAdmin = AccessControl.isAdmin(iwc);
      /** @todo  */
      isAdmin = iwc.hasEditPermission(this);
    }
    catch (Exception ex) {
      isAdmin = false;
    }

    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
    control(iwc);
  }

  public boolean deleteBlock(int instanceid){
    return NewsBusiness.disconnectBlock(instanceid);
  }

  public void setConnectionAttributes(String attributeName, int attributeId) {
    this.attributeName = attributeName;
    this.attributeId = attributeId;
  }

  public void setConnectionAttributes(String attributeName, String attributeId) {
    this.attributeName = attributeName;
    this.attributeId = Integer.parseInt(attributeId);
  }

  /*
  ** This method uses static layouts from this class
  **
  */
  public void setLayout(int LAYOUT){
    this.iLayout = LAYOUT;
  }

  /**
  *
  * return a proxy for the main text. Use the standard
  * set methods on this object such as .setFontSize(1) etc.
  * and it will set the property for all texts.
  */
  public Text getTextProxy(){
    return textProxy;
  }
  public Text getHeadlineProxy(){
    return headlineProxy;
  }
  public Text getInformationProxy(){
    return informationProxy;
  }
  public void setTextProxy(Text textProxy){
    this.textProxy = textProxy;
  }
  public void setHeadlineProxy(Text headlineProxy){
    this.headlineProxy = headlineProxy;
  }
  public void setInformationProxy(Text informationProxy){
    this.informationProxy = informationProxy;
  }
  private Text setTextAttributes( Text realText ){
    Text tempText = (Text) textProxy.clone();
    tempText.setText( realText.getText() );
  return tempText;
  }
  private Text setHeadlineAttributes( Text realText ){
    Text tempText = (Text) headlineProxy.clone();
    tempText.setText( realText.getText() );
    return tempText;
  }
  private Text setInformationAttributes( Text realText ){
    Text tempText = (Text) informationProxy.clone();
    tempText.setText( realText.getText() );
    return tempText;
  }

	public void setInformationFontSize(int size){
	  getInformationProxy().setFontSize(size);
	}

	public void setHeadlineFontSize(int size){
	  getHeadlineProxy().setFontSize(size);
	}

	public void setTextFontSize(int size){
	  getTextProxy().setFontSize(size);
	}

	public void setInformationFontColor(String color){
	  getInformationProxy().setFontColor(color);
	}

	public void setHeadlineFontColor(String color){
		getHeadlineProxy().setFontColor(color);
	}

	public void setTextFontColor(String color){
	  getTextProxy().setFontColor(color);
	}

	public void setInformationFontFace(String face){
	  getInformationProxy().setFontFace(face);
	}

	public void setHeadlineFontFace(String face){
		getHeadlineProxy().setFontFace(face);
	}

	public void setTextFontFontFace(String face){
	  getTextProxy().setFontFace(face);
	}


	public void setInformationFontStyle(String style){
	  getInformationProxy().setFontStyle(style);
	}

	public void setHeadlineFontStyle(String face){
		getHeadlineProxy().setFontStyle(face);
	}

	public void setTextFontFontStyle(String face){
	  getTextProxy().setFontStyle(face);
	}

  public void setNumberOfLetters(int numberOfLetters){
    this.numberOfLetters = Math.abs(numberOfLetters);
  }

	public void setNumberOfHeadlineLetters(int numberOfLetters){
    this.numberOfHeadlineLetters = Math.abs(numberOfLetters);
  }
  //debug this changes the number of news displayed..that is the date alone is failing
  public void setNumberOfDisplayedNews(int numberOfDisplayedNews){
    this.limitNumberOfNews = true;
    this.numberOfDisplayedNews = Math.abs(numberOfDisplayedNews);
  }

	public void setNumberOfCollectionNews(int numberOfCollectionNews){
    this.limitNumberOfNews = true;
    this.numberOfCollectionNews = Math.abs(numberOfCollectionNews);
  }

	public void setToViewNews(boolean viewNews){
	  this.viewNews = viewNews;
	}

  public void setAdmin(boolean isAdmin){
    this.isAdmin=isAdmin;
  }
  public void setWidth(int width){
    setWidth(Integer.toString(width));
  }
  public void setWidth(String width){
    this.outerTableWidth = width;
  }

	public void setBackgroundColor(String color){
	  firstTableColor = color;
	}

	public void setZebraColored(String firstColor,String secondColor){
	  firstTableColor = firstColor;
		secondTableColor = secondColor ;
	}
	public void setCellPadding(int cellpad){
	  this.cellPadding = cellpad;
	}
	public void setCellSpacing(int cellspace){
	  this.cellSpacing = cellspace;
	}
  public void showNewsCollectionButton(boolean showNewsCollectionButton){
    this.showNewsCollectionButton = showNewsCollectionButton;
  }
  public void setNumberOfExpandedNews(int numberOfExpandedNews){
    this.numberOfExpandedNews = Math.abs(numberOfExpandedNews);
  }
  public void setShowImages(boolean showImages) {
    this.showImages=showImages;
  }
  public void setShowMoreButton(boolean showMoreButton) {
    this.showMoreButton=showMoreButton;
  }
  public void setShowHeadlineImage(boolean showHeadlineImage) {
    this.showHeadlineImage=showHeadlineImage;
  }
  public void alignImageWithHeadline() {
    this.alignWithHeadline=true;
  }
  public void setHeadlineAsLink(boolean headlineAsLink) {
    this.headlineAsLink=headlineAsLink;
    this.showHeadlineImage=true;
    this.showMoreButton=false;
  }
	public void setHeadlineImage(Image image) {
    this.headlineImage=image;
		this.alignWithHeadline=true;
  }
  public void setHeadlineImageURL(String headlineImageURL) {
    this.headlineImageURL=headlineImageURL;
		this.alignWithHeadline=true;
  }
  public void setShowOnlyDates(boolean showOnlyDates) {
    this.showOnlyDates=showOnlyDates;
  }
	public void setDateAlign(String alignment) {
    this.dateAlign=alignment;
  }

	public void setViewPage(com.idega.builder.data.IBPage page){
		viewPageId = page.getID();
	}

	public void setShowTime(boolean showTime) {
    this.showTime=showOnlyDates;
  }

	public void setShowTimeFirst(boolean showTimeFirst) {
    this.showTimeFirst=showTimeFirst;
  }
  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
  public String getObjectAlignment(){
    return sObjectAlign ;
  }
  public void setObjectAligment(String sAlign){
    sObjectAlign = sAlign;
  }
  public void addObjectBetween(PresentationObject object,int spaceNumber){
    if(objectsBetween == null)
      objectsBetween = new Hashtable();
    objectsBetween.put(new Integer(spaceNumber),object);
  }
  // overriding super class method
  public void add(PresentationObject MO){
    addObjectBetween(MO,iSpaceBetween);
    if(iLayout == NEWS_SITE_LAYOUT){
      iSpaceBetween+=2;
    }
    else
      iSpaceBetween++;
  }

	public synchronized Object clone() {
    NewsReader obj = null;
    try {
      obj = (NewsReader)super.clone();

			// integers :
			obj.numberOfLetters = numberOfLetters;
			obj.numberOfHeadlineLetters = numberOfHeadlineLetters;
			obj.numberOfDisplayedNews = numberOfDisplayedNews;
			obj.numberOfExpandedNews = numberOfExpandedNews;
			obj.numberOfCollectionNews = numberOfCollectionNews;
			obj.iSpaceBetween = iSpaceBetween;
			obj.cellPadding = cellPadding;
			obj.cellSpacing = cellSpacing;
			obj.viewPageId = viewPageId;
			obj.textSize = textSize;

			// booleans:
			obj.backbutton = backbutton;
			obj.showAll = showAll;
			obj.showImages = showImages;
			obj.showOnlyDates = showOnlyDates;
			obj.showTime = showTime;
			obj.showTimeFirst = showTimeFirst;
			obj.headlineAsLink = headlineAsLink;
			obj.showHeadlineImage = showHeadlineImage;
			obj.showMoreButton = showMoreButton;
			obj.alignWithHeadline = alignWithHeadline;
			obj.limitNumberOfNews = limitNumberOfNews;
			obj.enableDelete=enableDelete;
			obj.viewNews = viewNews;
			obj.newobjinst = newobjinst;
			// Strings :
			obj.outerTableWidth = outerTableWidth;
			obj.sObjectAlign = sObjectAlign;
			obj.headlineImageURL = headlineImageURL;
			obj.dateAlign = dateAlign;

			obj.headlineImage = headlineImage;

			// Nullable :
			if(firstTableColor !=null)
				obj.firstTableColor = firstTableColor;
			if(secondTableColor != null)
				obj.secondTableColor = secondTableColor;
			if(objectsBetween != null)
				obj.objectsBetween = objectsBetween;

			// Text proxies :
			obj.textProxy = textProxy;
			obj.headlineProxy  = headlineProxy;
			obj.informationProxy  = informationProxy;

    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }
}
