package is.idega.idegaweb.travel.block.search.presentation;

import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine;
import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineHome;
import is.idega.idegaweb.travel.business.TravelSessionManager;
import is.idega.idegaweb.travel.presentation.TravelBlock;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import com.idega.business.IBOLookup;
import com.idega.core.builder.data.ICPage;
import com.idega.core.component.data.ICObject;
import com.idega.core.component.data.ICObjectHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.util.text.TextSoap;

/**
 * @author gimmi
 */
public class ServiceSearch extends TravelBlock {

	public static final String IC_OBJECT_TYPE = "iw.travel.search";
	
	public static final String PARAMETER_SERVICE_SEARCH_FORM = "p_ssf";
	
	protected String textFontStyle;
	protected String headerFontStyle;
	protected String linkFontStyle;
	protected String clickedLinkFontStyle;
	protected String errorFontStyle;
	
	protected String headerBackgroundColor;
	protected String linkBackgroundColor;
	protected String backgroundColor;
	
	protected String searchPartTopBorderColor = null;
	protected String searchPartTopBorderWidth = null;
	protected String searchPartBottomBorderColor = null;
	protected String searchPartBottomBorderWidth = null;
	protected String searchPartColor = null;
	protected String interfaceObjectStyle = null;
	protected Image searchImage = null;
	protected Image resetImage = null;
	
	protected String width;
	protected String formInputStyle;
	protected int engineID = -1;
	protected int resultsPerPage = -1;
	
	private Image headerImage;
	private Image headerTiler;
	private Image windowHeaderImage;
	
	private IWResourceBundle iwrb;
	private List searchForms2 = null; 
	private AbstractSearchForm currentSearchForm = null;
	private ICPage targetPage = null;
	private boolean horizontal = false;

	public ServiceSearch() {
		super();
	}
	
	public void main(IWContext iwc) throws Exception {
		if (engineID > 0) {
			init(iwc);
			if (currentSearchForm == null) {
				add("cannot get searchform instance");
			} else {
				add(currentSearchForm);
			}
		} else {
			add("searchform must be associated with an engine");
		}
	}

	protected void init(IWContext iwc) throws Exception {
		iwrb = getTravelSessionManager(iwc).getIWResourceBundle();

		if (searchForms2 != null) {
			currentSearchForm = setCurrentSearchForm(iwc, searchForms2);
		} else {
			List searchForms = new Vector();
			IWBundle bundle = getTravelSessionManager(iwc).getIWBundle();
			Collection ICObjectList = bundle.getICObjectsList(IC_OBJECT_TYPE);
			Iterator iter = ICObjectList.iterator();
			ICObject object;
			ICObjectHome objectHome = (ICObjectHome) IDOLookup.getHome(ICObject.class);
			AbstractSearchForm ss;

			while (iter.hasNext()) {
				object = (ICObject) iter.next();
				try {
					Class tmpClass = Class.forName(object.getClassName());
					//System.out.println(tmpClass.getName());
					ss = (AbstractSearchForm) tmpClass.newInstance();
					searchForms.add( ss );
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			currentSearchForm = setCurrentSearchForm(iwc, searchForms);
		} 

	}

	protected AbstractSearchForm setCurrentSearchForm(IWContext iwc, List searchForms) {
		try {
			String currentSF = iwc.getParameter(PARAMETER_SERVICE_SEARCH_FORM);
			try {
				Integer.parseInt(currentSF);
				currentSF = IWMainApplication.decryptClassName(currentSF);
			} catch (NumberFormatException n) {
				//System.out.println("Cannot decrypt class name = "+currentSF);
			}
			AbstractSearchForm ss = null;
			if (currentSF != null) {
				ss = (AbstractSearchForm) Class.forName(currentSF).newInstance();
			} else if (!searchForms.isEmpty()) {
				ss = (AbstractSearchForm) searchForms.get(0).getClass().newInstance();
			} else {
				ss = (AbstractSearchForm) Class.forName(AbstractSearchForm.class.getName()).newInstance();
			}
			ss.setTextFontStyle(textFontStyle);
			ss.setHeaderFontStyle(headerFontStyle);
			ss.setLinkFontStyle(linkFontStyle);
			ss.setClickedLinkFontStyle(clickedLinkFontStyle);
			ss.setErrorFontStyle(errorFontStyle);
			ss.setHeaderBackgroundColor(headerBackgroundColor);
			ss.setLinksBackgroundColor(linkBackgroundColor);
			ss.setHeaderImage(headerImage);
			ss.setWidth(width);
			ss.setFormInputStyle(formInputStyle);
			ss.setWindowHeaderImage(windowHeaderImage);
			ss.setSearchForms(searchForms);
			ss.setSearchPartBottomBorderColor(searchPartBottomBorderColor);
			ss.setSearchPartBottomBorderWidth(searchPartBottomBorderWidth);
			ss.setSearchPartColor(searchPartColor);
			ss.setSearchPartTopBorderColor(searchPartTopBorderColor);
			ss.setSearchPartTopBorderWidth(searchPartTopBorderWidth);
			ss.setTargetPage(targetPage);
			ss.setSearchImage(searchImage);
			ss.setResetImage(resetImage);
			if (resultsPerPage > 0) {
				ss.setResultsPerPage(resultsPerPage);
			}
			ss.setHorizontal(horizontal);
			ss.setServiceSearchEngine(((ServiceSearchEngineHome) IDOLookup.getHome(ServiceSearchEngine.class)).findByPrimaryKey(new Integer(engineID)));
			return ss;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setTextFontStyle(String fontStyle) {
		this.textFontStyle = fontStyle;
	}
	
	public void setHeaderFontStyle(String fontStyle) {
		this.headerFontStyle = fontStyle;
	}
	
	public void setLinkFontStyle(String fontStyle) {
		this.linkFontStyle = fontStyle;
	}
	
	public void setClickedLinkFontStyle(String fontStyle) {
		this.clickedLinkFontStyle = fontStyle;
	}
	
	public void setErrorTextStyle(String fontStyle) {
		this.errorFontStyle = fontStyle;
	}

	public void setHeaderImage(Image image) {
		this.headerImage = image;
	}
	
	public void setHeaderTilerImage(Image image) {
		this.headerTiler = image;
	}
	
	public void setHeaderBackgroundColor(String color) {
		this.headerBackgroundColor = color;
	}
	
	public void setLinksBackgroundColor(String color) {
		this.linkBackgroundColor = color;
	}
	
	public void setBackgroundColor(String color) {
		this.backgroundColor = color;
	}

	public void setFormInputStyle(String style) {
		this.formInputStyle = style;
	}

	public void setWidth(String width) {
		this.width = width;
	}
	
	public void setWindowHeaderImage(Image image) {
		this.windowHeaderImage = image;
	}
	
	public void setSearchEngine(int engineID) {
		this.engineID = engineID;
	}
	
	public void setSearchPartBottomBorderColor(String color) {
		searchPartBottomBorderColor = color;
	}
	
	public void setSearchPartBottomBorderWidth(String width) {
		searchPartBottomBorderWidth = width;
	}
	
	public void setSearchPartTopBorderColor(String color) {
		searchPartTopBorderColor = color;
	}
	
	public void setSearchPartTopBorderWidth(String width) {
		searchPartTopBorderWidth = width;
	}
	
	public void setSearchPartColor(String color) {
		searchPartColor = color;
	}
	
	public void setTargetPage(ICPage page) {
		this.targetPage = page;
	}
	
	public void setResultsPerPage(int resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}
	
	public void setSearchImage(Image image) {
		this.searchImage = image;
	}
	
	public void setResetImage(Image image) {
		this.resetImage = image;
	}
	
	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
	}

	/**
	 * Set the valid search engines for the Search Engine
	 * 
	 * @param validSearchEngines Comma seperated classNames
	 */
	public void setValidSearchEngines(String validSearchEngines) {
		searchForms2 = new Vector();
		AbstractSearchForm ss;
		validSearchEngines = TextSoap.findAndCut(validSearchEngines, " ");
		StringTokenizer st = new StringTokenizer(validSearchEngines, ",");
		
		while (st.hasMoreTokens()) {
			try {
				Class tmpClass = Class.forName(st.nextToken());
				ss = (AbstractSearchForm) tmpClass.newInstance();
				searchForms2.add( ss );
			} catch (ClassNotFoundException e) {
				System.err.println("ServiceSearch couldnt instanciate class, engineID = "+engineID);
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
	}

	protected TravelSessionManager getTravelSessionManager(IWUserContext iwuc) throws RemoteException {
		return (TravelSessionManager) IBOLookup.getSessionInstance(iwuc, TravelSessionManager.class);
	}
	
}
