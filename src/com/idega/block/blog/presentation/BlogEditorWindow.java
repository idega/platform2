package com.idega.block.blog.presentation;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.block.blog.BlogBundle;
import com.idega.block.blog.business.BlogBusiness;
import com.idega.block.blog.business.BlogBusinessOLD;
import com.idega.block.blog.business.BlogFinder;
import com.idega.block.blog.data.BlogEntity;
import com.idega.block.category.presentation.FolderBlockComponentIWAdminWindowLegacy;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.block.text.business.ContentBusiness;
import com.idega.block.text.business.ContentFinder;
import com.idega.block.text.business.ContentHelper;
import com.idega.block.text.data.Content;
import com.idega.block.text.data.LocalizedText;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.file.data.ICFile;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.core.user.data.User;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.texteditor.TextEditor;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TimestampInput;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;

/**
 * Title: Blog 
 * Description: 
 * Copyright: Copyright (c) 2003 
 * Company: idega Software
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @author 2004 - idega team -<br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.5b
 */

public class BlogEditorWindow extends FolderBlockComponentIWAdminWindowLegacy {

	private final static String IW_BUNDLE_IDENTIFIER = BlogBundle.IW_BUNDLE_IDENTIFIER;
	private String Error;
	private boolean isAdmin = false;
	private int iUserId = -1;
	private User eUser = null;
	private int iObjInsId = -1;
	private int defaultPublishDays = 50;
	private int SAVECATEGORY = 1, SAVEBLOG = 2;

	private static String YEARS_AHEAD_PROPERTY = "publish_to_years";

	private static String prmHeadline = "bgep_headline";
	private static String prmTeaser = "bgep_teaser";
	private static String prmAuthor = "bgep_author";
	private static String prmSource = "bgep_source";
	private static String prmDaysshown = "bgep_daysshown";
	private static String prmBody = "bge_body";
	public static String prmCategory = "bgep_category";
	private static String prmLocale = "bgep_locale";
	private static String prmLocalizedTextId = "bgep_loctextid";
	public static String prmObjInstId = prmBlockInstanceID;
	public static String prmObjId = prmBlockObjectID;
	public static String prmAttribute = "bgep_attribute";
	private static String prmUseImage = "insertImage"; //bgep.useimage
	public static String prmDelete = "bgep_txdeleteid";
	private static String prmImageId = "bgep_imageid";
	public static String prmBlogEntityId = "bgep_bgblogid";
	private static String actDelete = "bgea_delete";
	private static String actSave = "bgea_save";
	private static String actClose = "bgea_close";
	private static String modeDelete = "bgem_delete";
	private static String prmFormProcess = "bge_formprocess";
	private static String prmNewCategory = "bgep_newcategory";
	private static String prmEditCategory = "bgep_editcategory";
	private static String prmDeleteFile = "bgep_deletefile";
	private static String prmSaveFile = "bgep_savefile";
	private static String prmCatName = "bgep_categoryname";
	private static String prmCatDesc = "bgep_categorydesc";
	private static String prmPubFrom = "bgep_publishfrom";
	private static String prmPubTo = "bgep_publishto";
	private static String prmBlogDate = "bgep_blogdate";
	private static String prmMoveToCat = "bgep_movtocat";
	public static String prmWorkFolderPK = "bgep_wf";
	public static final String imageAttributeKey = "blogimage";
	private String sBlogId = null;
	private int iCategoryId = -1;

	private String sEditor, sHeadline, sTeaser, sBlog, sBlogDate, sCategory, sAuthor, sSource, sDaysShown, sImage, sLocale, sPublisFrom, sPublisTo;

	private int attributeId = 3;
	private IWBundle iwb, core;
	private IWResourceBundle iwrb;
	private BlogBusiness blogBusiness;

	public BlogEditorWindow() {
		setWidth(570);
//		setHeight(620);
//		setResizable(true);
//		setScrollbar(true);
		setUnMerged();
	}

	private void init() {
		sHeadline = iwrb.getLocalizedString("headline", "Headline");
		sLocale = iwrb.getLocalizedString("locale", "Locale");
		sTeaser = iwrb.getLocalizedString("teaser", "Teaser");
		sBlog = iwrb.getLocalizedString("blog", "Blog");
		sCategory = iwrb.getLocalizedString("category", "Category");
		sAuthor = iwrb.getLocalizedString("author", "Author");
		sSource = iwrb.getLocalizedString("source", "Source");
		sDaysShown = iwrb.getLocalizedString("visible_days", "Number of days visible");
		sImage = iwrb.getLocalizedString("image", "Image");
		sEditor = iwrb.getLocalizedString("blog_editor", "Blog Editor");
		sPublisFrom = iwrb.getLocalizedString("publish_from", "Publish from");
		sPublisTo = iwrb.getLocalizedString("publish_to", "Publish to");
		sBlogDate = iwrb.getLocalizedString("blog_date", "Blog date");
//		setAllMargins(0);
//		setTitle(sEditor);
	}

	private void control(IWContext iwc) throws Exception {
		init();
		//debugParameters(iwc);
		boolean doView = true;
		Locale currentLocale = iwc.getCurrentLocale(), chosenLocale;

		if (iwc.isParameterSet(actClose) || iwc.isParameterSet(actClose + ".x")) {
//			setParentToReload();
//			close();
			quit(iwc);
		} else {

			String sLocaleId = iwc.getParameter(prmLocale);
			String sCategoryId = iwc.getParameter(prmCategory);
			iCategoryId = sCategoryId != null ? Integer.parseInt(sCategoryId) : -1;
			int saveInfo = getSaveInfo(iwc);

			// LocaleHandling
			int iLocaleId = -1;
			if (sLocaleId != null) {
				iLocaleId = Integer.parseInt(sLocaleId);
				chosenLocale = BlogFinder.getLocale(iLocaleId);
			} else {
				chosenLocale = currentLocale;
				iLocaleId = ICLocaleBusiness.getLocaleId(chosenLocale);
			}

			if (isAdmin) {
				// end of LocaleHandling

				// Text initialization
				String sAttribute = null;
				String sLocTextId = iwc.getParameter(prmLocalizedTextId);
				String sObjInstId = iwc.getParameter(prmObjInstId);
				sAttribute = iwc.getParameter(prmAttribute);
				if (sObjInstId != null)
					iObjInsId = Integer.parseInt(sObjInstId);

				// Blog Id Request :
				if (iwc.getParameter(prmBlogEntityId) != null) {
					sBlogId = iwc.getParameter(prmBlogEntityId);
				}
				// Delete Request :
				else if (iwc.getParameter(prmDelete) != null) {
					sBlogId = iwc.getParameter(prmDelete);
					confirmDelete(sBlogId, iObjInsId);
					doView = false;
				}
				// Object Instance Request :
				else if (sObjInstId != null) {
					//doView = false;
//					if (iObjInsId > 0 && saveInfo != SAVECATEGORY)
//						iCategoryId = CategoryFinder.getInstance().getObjectInstanceCategoryId(iObjInsId);
				}
				//add("category id "+iCategoryId);
				//add(" instance id "+iObjInsId);
				// end of Blog initialization

				// Form processing
				if (saveInfo == SAVEBLOG)
					processForm(iwc, sBlogId, sLocTextId, sCategoryId);
				//      else if(saveInfo == SAVECATEGORY)
				//        processCategoryForm(iwc,sCategoryId,iObjInsId);

				/*
				 * old stuff if(iwc.isParameterSet(prmObjInstId)){
				 * addCategoryFields(CategoryFinder.getInstance().getCategory(iCategoryId),iObjInsId ); }
				 */
				//doView = false;

				if (doView)
					doViewBlog(sBlogId, sAttribute, chosenLocale, iLocaleId);
			} else {
				noAccess();
			}
		}
	}

	private int getSaveInfo(IWContext iwc) {
		if (iwc.getParameter(prmFormProcess) != null) {
			if (iwc.getParameter(prmFormProcess).equals("Y"))
				return SAVEBLOG;
			else if (iwc.getParameter(prmFormProcess).equals("C"))
				return SAVECATEGORY;
			//doView = false;
		}
		return 0;
	}

	private Parameter getParameterSaveBlog() {
		return new Parameter(prmFormProcess, "Y");
	}

	private Parameter getParameterSaveCategory() {
		return new Parameter(prmFormProcess, "C");
	}

	// Form Processing :
	private void processForm(IWContext iwc, String sBlogId, String sLocTextId, String sCategory) {
		// Save :
		if (iwc.getParameter(actSave) != null || iwc.getParameter(actSave + ".x") != null) {
			iwc.getIWMainApplication().getIWCacheManager().invalidateCache(Blog.CACHE_KEY);
			saveBlog(iwc, sBlogId, sLocTextId, sCategory);
		}
		// Delete :
		else if (iwc.getParameter(actDelete) != null || iwc.getParameter(actDelete + ".x") != null) {
			iwc.getIWMainApplication().getIWCacheManager().invalidateCache(Blog.CACHE_KEY);
			try {
				if (iwc.getParameter(modeDelete) != null) {
					int I = Integer.parseInt(iwc.getParameter(modeDelete));
					deleteBlog(I,iwc);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (iwc.getParameter(prmDeleteFile) != null) {
			if (sBlogId != null) {
				String sFileId = iwc.getParameter(prmDeleteFile);
				deleteFile(sBlogId, sFileId);
			}
		} else if (iwc.getParameter(prmSaveFile) != null || iwc.getParameter(prmSaveFile + ".x") != null) {
			if (sBlogId != null) {
				String sFileId = iwc.getParameter(prmImageId);
				saveFile(sBlogId, sFileId);
			}
		}
		// New:
		/** @todo make possible */
		/*
		 * else if(iwc.getParameter( actNew ) != null ||
		 * iwc.getParameter(actNew+".x")!= null){ sBlogId = null; }
		 */
		// end of Form Actions
	}


	private void doViewBlog(String sBlogId, String sAttribute, Locale locale, int iLocaleId) {
		ContentHelper contentHelper = null;
		BlogEntity blog = null;
		if (sBlogId != null) {
			int iBlogId = Integer.parseInt(sBlogId);
			blog = BlogFinder.getBlog(iBlogId);
			if (blog != null && locale != null)
				contentHelper = ContentFinder.getContentHelper(blog.getContentId(), locale);
		}

		addBlogFields(blog, contentHelper, iLocaleId, iObjInsId);

	}

	private void saveBlog(IWContext iwc, String sBlogEntityId, String sLocalizedTextId, String sCategoryId) {

		String sHeadline = iwc.getParameter(prmHeadline);
		String sTeaser = iwc.getParameter(prmTeaser);
		String sBody = iwc.getParameter(prmBody);
		String sImageId = iwc.getParameter(prmImageId);
		String sLocaleId = iwc.getParameter(prmLocale);
		String sAuthor = iwc.getParameter(prmAuthor);
		String sSource = iwc.getParameter(prmSource);
		String sPubFrom = iwc.getParameter(prmPubFrom);
		String sPubTo = iwc.getParameter(prmPubTo);
		String sBlogDate = iwc.getParameter(prmBlogDate);
		//System.err.println("publish from" + sPubFrom);
		//System.err.println("publish to" + sPubTo);
		
		String sWorkFolder = iwc.getParameter(prmWorkFolderPK);
		
		if (sHeadline != null || sBody != null) {
			int iBlogEntityId = sBlogEntityId != null ? Integer.parseInt(sBlogEntityId) : -1;
			int iLocalizedTextId = sLocalizedTextId != null ? Integer.parseInt(sLocalizedTextId) : -1;
			int iLocaleId = sLocaleId != null ? Integer.parseInt(sLocaleId) : -1;
			int iImageId = sImageId != null ? Integer.parseInt(sImageId) : -1;
			int iCategoryId = sCategoryId != null ? Integer.parseInt(sCategoryId) : -1;
			int iWorkFolderId = sWorkFolder != null ? Integer.parseInt(sWorkFolder) : -1;
			IWTimestamp today = IWTimestamp.RightNow();
			IWTimestamp pubFrom = sPubFrom != null ? new IWTimestamp(sPubFrom) : today;
			Timestamp blogDate = sBlogDate != null ? new IWTimestamp(sBlogDate).getTimestamp() : null;
			today.addDays(defaultPublishDays);
			IWTimestamp pubTo = sPubTo != null ? new IWTimestamp(sPubTo) : today;
			Vector V = null;
			ICFile F = null;
			if (iImageId > 0) {
				try {
					/** @todo use finder */
					F = ((com.idega.core.file.data.ICFileHome) com.idega.data.IDOLookup.getHome(ICFile.class)).findByPrimaryKey(new Integer(iImageId));
					V = new Vector(1);
					V.add(F);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			//System.err.println(pubFrom.toSQLString());
			//System.err.println(pubTo.toString());
			BlogEntity blog=null;
			try {
				blog = ((BlogBusiness)getBlockBusinessInstance(iwc)).saveBlog(iBlogEntityId, iLocalizedTextId, iWorkFolderId, iCategoryId,sHeadline, sTeaser, sAuthor, sSource, sBody, iLocaleId, iUserId, iObjInsId, pubFrom.getTimestamp(), pubTo.getTimestamp(), V, blogDate);
			} catch (IDOLookupException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CreateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IBOLookupException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (blog != null)
				sBlogId = String.valueOf(blog.getID());
		}
	}
	
	public Class getBlockBusinessClass(){
		return BlogBusiness.class;
	}

	private void saveFile(String sBlogId, String sFileId) {
		BlogEntity bg = BlogFinder.getBlog(Integer.parseInt(sBlogId));
		ContentBusiness.addFileToContent(bg.getContentId(), Integer.parseInt(sFileId));
	}

	private void deleteFile(String sBlogId, String sFileId) {
		BlogEntity bg = BlogFinder.getBlog(Integer.parseInt(sBlogId));
		ContentBusiness.removeFileFromContent(bg.getContentId(), Integer.parseInt(sFileId));
	}

	private void deleteBlog(int iBlogId,IWContext iwc) {
		BlogBusinessOLD.deleteBlog(iBlogId);
//		setParentToReload();
//		close();
		quit(iwc);
	}

	public Text getHeaderText(String s) {
		Text textTemplate = new Text(s);
		textTemplate.setFontSize(Text.FONT_SIZE_7_HTML_1);
		textTemplate.setBold();
		textTemplate.setFontFace(Text.FONT_FACE_VERDANA);
		return textTemplate;
	}


	private void addBlogFields(BlogEntity bgBlog, ContentHelper contentHelper, int iLocaleId, int iObjInsId) {
		LocalizedText locText = null;
		boolean hasContent = (contentHelper != null) ? true : false;
		if (hasContent)
			locText = contentHelper.getLocalizedText(ICLocaleBusiness.getLocaleReturnIcelandicLocaleIfNotFound(iLocaleId));
		boolean hasBlogEntity = (bgBlog != null) ? true : false;
		boolean hasLocalizedText = (locText != null) ? true : false;

		TextInput tiHeadline = new TextInput(prmHeadline);
		tiHeadline.setLength(40);
		tiHeadline.setMaxlength(255);

		IWTimestamp now = IWTimestamp.RightNow();
		TimestampInput publishFrom = new TimestampInput(prmPubFrom, true);
		publishFrom.setTimestamp(now.getTimestamp());

		TimestampInput blogDate = new TimestampInput(prmBlogDate, true);
		blogDate.setTimestamp(now.getTimestamp());
		blogDate.setYearRange(now.getYear() - 4, now.getYear() + 2);

		// add default publishing days:
		int addYears = 0;
		try {
			addYears = Integer.parseInt(iwb.getProperty(YEARS_AHEAD_PROPERTY, "0"));
		} catch (NullPointerException ne) {
			addYears = 0;
		} catch (NumberFormatException nfe) {
			addYears = 0;
		}

		now.addYears(addYears);
		TimestampInput publishTo = new TimestampInput(prmPubTo, true);
		publishTo.setTimestamp(now.getTimestamp());

		DropdownMenu LocaleDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(prmLocale);
		LocaleDrop.setToSubmit();
		LocaleDrop.setSelectedElement(Integer.toString(iLocaleId));

		//TextArea taBody = new TextArea(prmBody,65,18);
		TextEditor taBody = new TextEditor();
		taBody.setInputName(prmBody);

		TextArea taTeaser = new TextArea(prmTeaser, 65, 2);

		Collection cats = ListUtil.getEmptyList();
		try {
			cats = this.getBlockBusinessInstance(this.getIWApplicationContext()).getAvailableCategories(this.getBlockObjectID(),this.getWorkFolder().getID()); // CategoryFinder.getInstance().listOfCategoryForObjectInstanceId(iObjInsId);
		} catch (IBOLookupException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		
		DropdownMenu catDrop = new DropdownMenu(cats, prmCategory);
		//catDrop.addMenuElementFirst("-1",sCategory);

		TextInput tiAuthor = new TextInput(prmAuthor);
		tiAuthor.setLength(22);
		tiAuthor.setMaxlength(255);

		TextInput tiSource = new TextInput(prmSource);
		tiAuthor.setLength(22);
		tiAuthor.setMaxlength(255);

		//DropdownMenu drpDaysShown = counterDropdown(prmDaysshown, 1, 30);
		//drpDaysShown.addMenuElementFirst("-1",
		// iwrb.getLocalizedString("undetermined","Undetermined") );
		/*
		 * ImageInserter imageInsert = new ImageInserter();
		 * imageInsert.setImSessionImageName(prmImageId); Link propslink =
		 * null;
		 */

		// Fill or not Fill
		if (hasLocalizedText) {
			if (locText.getHeadline() != null) {
				tiHeadline.setContent(locText.getHeadline());
			}
			if (locText.getTitle() != null) {
				taTeaser.setContent(locText.getTitle());
			}
			if (locText.getBody() != null) {
				taBody.setContent(locText.getBody());
			}
			addHiddenInput(new HiddenInput(prmLocalizedTextId, String.valueOf(locText.getID())));
		}
		if (hasBlogEntity) {
			if ("".equals(bgBlog.getAuthor()) && eUser != null)
				tiAuthor.setContent(eUser.getFirstName());
			else
				tiAuthor.setContent(bgBlog.getAuthor());
			tiSource.setContent(bgBlog.getSource());
			//drpCategories.setSelectedElement(String.valueOf(bgBlog.getBlogCategoryId()));

			if (hasContent) {
				/*
				 * List files = contentHelper.getFiles(); if(files != null){
				 * ICFile file1 = (ICFile) files.get(0);
				 * imageInsert.setImageId(file1.getID()); Text properties = new
				 * Text("properties"); propslink =
				 * com.idega.block.media.presentation.ImageAttributeSetter.getLink(properties,file1.getID(),imageAttributeKey); }
				 */
				Content content = contentHelper.getContent();
				if (content.getPublishFrom() != null) {
					publishFrom.setTimestamp(content.getPublishFrom());
				}
				if (content.getPublishTo() != null) {
					publishTo.setTimestamp(content.getPublishTo());
				}
				if (content.getLastUpdated() != null) {
					blogDate.setTimestamp(content.getLastUpdated());
				}
			}
			catDrop.setSelectedElement(String.valueOf(bgBlog.getBlogCategoryId()));
			addHiddenInput(new HiddenInput(prmBlogEntityId, Integer.toString(bgBlog.getID())));
			//addHiddenInput(new HiddenInput(prmCategory
			// ,String.valueOf(bgBlog.getBlogCategoryId())));
		} else {
			if (eUser != null) {
				tiAuthor.setContent(eUser.getFirstName());
			}
			IWTimestamp today = IWTimestamp.RightNow();
			publishFrom.setTimestamp(today.getTimestamp());

			if (addYears > 0)
				today.addYears(addYears);
			else
				today.addDays(defaultPublishDays);
			publishTo.setTimestamp(today.getTimestamp());
			addHiddenInput(new HiddenInput(prmCategory, String.valueOf(iCategoryId)));
		}
		addHiddenInput(new HiddenInput(prmObjInstId, String.valueOf(iObjInsId)));

		SubmitButton addButton = new SubmitButton(core.getImage("/shared/create.gif", "Add to blog"), prmSaveFile);
		//SubmitButton leftButton = new
		// SubmitButton(core.getImage("/shared/frew.gif","Insert
		// image"),prmSaveFile);
		ImageInserter imageInsert = new ImageInserter();
		imageInsert.setImSessionImageName(prmImageId);
		imageInsert.setUseBoxParameterName(prmUseImage);
		imageInsert.setMaxImageWidth(130);
		imageInsert.setHasUseBox(false);
		imageInsert.setSelected(false);
		Table imageTable = new Table();
		int row = 1;
		//imageTable.mergeCells(1,row,3,row);
		//imageTable.add(formatText(iwrb.getLocalizedString("image","Chosen
		// image :")),1,row++);
		imageTable.mergeCells(1, row, 3, row);
		imageTable.add(imageInsert, 1, row++);
		imageTable.mergeCells(1, row, 3, row);
		//imageTable.add(leftButton,1,row);
		imageTable.add(addButton, 1, row++);

		if (hasContent) {
			List files = contentHelper.getFiles();
			if (files != null && !files.isEmpty()) {
				imageTable.mergeCells(1, row, 3, row);
				imageTable.add(formatText(iwrb.getLocalizedString("blogimages", "Blog images :")), 1, row++);
				ICFile file1 = (ICFile) files.get(0);
				imageInsert.setImageId(((Integer) file1.getPrimaryKey()).intValue());

				Iterator I = files.iterator();
				while (I.hasNext()) {
					try {

						ICFile f = (ICFile) I.next();
						Image immi = new Image(((Integer) f.getPrimaryKey()).intValue());
						immi.setMaxImageWidth(50);

						imageTable.add(immi, 1, row);
						//Link edit = new
						// Link(iwb.getImage("/shared/edit.gif"));
						Link edit = com.idega.block.image.presentation.ImageAttributeSetter.getLink(iwb.getImage("/shared/edit.gif"), ((Integer) file1.getPrimaryKey()).intValue(), imageAttributeKey);
						Link delete = new Link(core.getImage("/shared/delete.gif"));
						delete.addParameter(prmDeleteFile, f.getPrimaryKey().toString());
						delete.addParameter(prmBlogEntityId, bgBlog.getID());
						delete.addParameter(getParameterSaveBlog());
						imageTable.add(edit, 2, row);
						imageTable.add(delete, 3, row);
						row++;
					} catch (Exception ex) {

					}
				}
			}
		}

		addLeft(sHeadline, tiHeadline, true);
		addLeft(sLocale, LocaleDrop, true);
		addLeft(sTeaser, taTeaser, true);
		addLeft(sBlog, taBody, true);
		addLeft(sBlogDate, blogDate, true);
		addLeft(sPublisFrom, publishFrom, true);
		addLeft(sPublisTo, publishTo, true);

		addRight(sCategory, catDrop, true);
		addRight(sAuthor, tiAuthor, true);
		addRight(sSource, tiSource, true);
		//addRight(iwrb.getLocalizedString("image","Image"),imageInsert,true);
		//if(addButton!=null){
		//addRight("",addButton,true,false);
		//}
		addRight(iwrb.getLocalizedString("images", "Images"), imageTable, true, false);

		/*
		 * addRight(sImage,imageInsert,true); if(propslink != null)
		 * addRight("props",propslink,true);
		 */

		SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save", "Save"), actSave);
		SubmitButton close = new SubmitButton(iwrb.getLocalizedImageButton("close", "Close"), actClose);
		addSubmitButton(save);
		addSubmitButton(close);

		addHiddenInput(new HiddenInput(prmFormProcess, "Y"));
	}

	private void deleteCat(int iCatId) {

	}

	private void confirmDelete(String sBlogId, int iObjInsId) throws IOException, SQLException {
		int iBlogId = Integer.parseInt(sBlogId);
		BlogEntity bgBlog = BlogFinder.getBlog(iBlogId);

		if (bgBlog != null) {
			addLeft(iwrb.getLocalizedString("blog_to_delete", "Blog to delete"));
			addLeft(iwrb.getLocalizedString("confirm_delete", "Are you sure?"));

			//addSubmitButton(new
			// SubmitButton(iwrb.getImage("delete.gif"),actDelete));
			addSubmitButton(new SubmitButton(iwrb.getLocalizedImageButton("delete", "Delete"), actDelete));

			addHiddenInput(new HiddenInput(modeDelete, String.valueOf(bgBlog.getID())));
			addHiddenInput(new HiddenInput(prmFormProcess, "Y"));
		} else {
			addLeft(iwrb.getLocalizedString("not_exists", "Blog already deleted or not available."));
			//addSubmitButton(new CloseButton(iwrb.getImage("close.gif")));
			addSubmitButton(new CloseButton());
		}
	}

	private void noAccess() throws IOException, SQLException {
		addLeft(iwrb.getLocalizedString("no_access", "Login first!"));
		this.addSubmitButton(new CloseButton());
	}

	public DropdownMenu counterDropdown(String dropdownName, int countFrom, int countTo) {
		DropdownMenu myDropdown = new DropdownMenu(dropdownName);

		for (; countFrom <= countTo; countFrom++) {
			myDropdown.addMenuElement(Integer.toString(countFrom), Integer.toString(countFrom));
		}
		myDropdown.keepStatusOnAction();

		return myDropdown;
	}


	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		isAdmin = true;
		eUser = com.idega.core.accesscontrol.business.LoginBusinessBean.getUser(iwc);
		iUserId = eUser != null ? eUser.getID() : -1;
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		core = iwc.getIWMainApplication().getBundle(Blog.IW_CORE_BUNDLE_IDENTIFIER);
		addTitle(iwrb.getLocalizedString("blog_editor", "Blog Editor"));
		blogBusiness = (BlogBusiness) IBOLookup.getServiceInstance(iwc, BlogBusiness.class);
		this.getUnderlyingForm().maintainParameter(prmWorkFolderPK);
		this.getUnderlyingForm().maintainParameter(Blog.PRM_MODE);
		control(iwc);
	
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	


	
}
