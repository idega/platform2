package com.idega.block.banner.presentation;

import java.util.HashMap;
import java.util.Map;

import com.idega.block.banner.business.BannerBusiness;
import com.idega.block.banner.business.BannerFinder;
import com.idega.block.banner.business.BannerListener;
import com.idega.block.banner.data.AdEntity;
import com.idega.block.banner.data.BannerEntity;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;

public class Banner extends Block implements Builderaware {

	private static final String IMAGE_STYLE = "ImageStyle";

	private static final String DEFAULT_IMAGE_STYLE = "";

	private int _bannerID = -1;

	private boolean _isAdmin = false;

	private String _attribute;

	private int _iLocaleID;

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.banner";

	private final static String IW_CORE_BUNDLE_IDENTIFIER = "com.idega.core";

	protected IWResourceBundle _iwrb;

	protected IWBundle _iwb;

	private Table _myTable;

	private boolean _newObjInst = false;

	private boolean _newWithAttribute = false;

	private String _target;
	
	private int _maxWidth = -1;
	
	public static String CACHE_KEY = "banner_cache";
	

	public Banner() {
		super.setCacheable(getCacheKey(), 999999999);
	}

	public Banner(int bannerID) {

		this();

		this._bannerID = bannerID;

	}

	public Banner(String attribute) {

		this();

		this._attribute = attribute;

	}

	public String getCacheKey() {
		return CACHE_KEY;
	}
	
	protected String getCacheState(IWContext iwc, String cacheStatePrefix) {
    return  cacheStatePrefix+this._attribute+this._bannerID;
	}
	public void main(IWContext iwc) throws Exception {

		this._iwrb = getResourceBundle(iwc);

		this._iwb = getBundle(iwc);

		this._isAdmin = iwc.hasEditPermission(this);

		this._iLocaleID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());

		BannerEntity banner = null;

		this._myTable = new Table(1, 2);

		this._myTable.setCellpadding(0);

		this._myTable.setCellspacing(0);

		this._myTable.setBorder(0);

		if (this._bannerID <= 0) {

			String sBannerID = iwc.getParameter(BannerBusiness.PARAMETER_BANNER_ID);

			if (sBannerID != null) {
				this._bannerID = Integer.parseInt(sBannerID);
			}
			else if (getICObjectInstanceID() > 0) {

				this._bannerID = BannerFinder.getRelatedEntityId(getICObjectInstance());

				if (this._bannerID <= 0) {

					BannerBusiness.saveBanner(this._bannerID, getICObjectInstanceID(), null);

					this._newObjInst = true;

				}

			}

		}

		if (this._newObjInst) {

			this._bannerID = BannerFinder.getRelatedEntityId(((com.idega.core.component.data.ICObjectInstanceHome) com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(getICObjectInstanceID()));

		}

		if (this._bannerID > 0) {

			banner = BannerFinder.getBanner(this._bannerID);

		}

		else if (this._attribute != null) {

			banner = BannerFinder.getBanner(this._attribute);

			if (banner != null) {

				this._bannerID = banner.getID();

			}

			else {

				BannerBusiness.saveBanner(-1, -1, this._attribute);

			}

			this._newWithAttribute = true;

		}

		if (this._newWithAttribute) {

			this._bannerID = BannerFinder.getBanner(this._attribute).getID();

		}

		int row = 1;

		if (this._isAdmin) {

			this._myTable.add(getAdminPart(iwc), 1, row);

			row++;

		}

		Link link = getBanner(iwc, banner);

		if (link != null) {
			this._myTable.add(link, 1, row);
		}

		add(this._myTable);

	}

	private Link getBanner(IWContext iwc, BannerEntity banner) {

		Link bannerLink = null;

		AdEntity ad = null;

		Image image = null;

		if (banner != null) {

			ad = BannerBusiness.getCurrentAd(banner);

		}

		if (ad != null) {

			int imageID = BannerBusiness.getImageID(ad);

			if (imageID != -1) {

				image = BannerBusiness.getImage(imageID);

			}

			if (image != null) {
				
				if(this._maxWidth>0) {
					image.setMaxImageWidth(this._maxWidth);
				}


				bannerLink = new Link(getStyleObject(image, IMAGE_STYLE));

				if (this._target != null) {

					bannerLink.setTarget(this._target);

				}

				else {

					bannerLink.setTarget(Link.TARGET_NEW_WINDOW);

				}

				BannerBusiness.updateImpressions(iwc, ad);

				bannerLink.addParameter(BannerBusiness.PARAMETER_MODE, BannerBusiness.PARAMETER_CLICKED);

				bannerLink.addParameter(BannerBusiness.PARAMETER_AD_ID, ad.getID());

				bannerLink.setEventListener(BannerListener.class);

			}

		}

		if (bannerLink != null) {
			return bannerLink;
		}

		return null;

	}

	private Link getAdminPart(IWContext iwc) {

		Image createImage = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER).getImage("shared/create.gif");

		Link createLink = new Link(createImage);

		createLink.setWindowToOpen(BannerEditorWindow.class);

		createLink.addParameter(BannerBusiness.PARAMETER_BANNER_ID, this._bannerID);

		return createLink;

	}

	public boolean deleteBlock(int ICObjectInstanceID) {

		BannerEntity banner = BannerFinder.getObjectInstanceFromID(ICObjectInstanceID);

		if (banner != null) {

		return BannerBusiness.deleteBanner(banner);

		}

		return false;

	}

	public void setTarget(String target) {

		this._target = target;

	}

	public String getBundleIdentifier() {

		return IW_BUNDLE_IDENTIFIER;

	}

	public Object clone() {

		Banner obj = null;

		try {

			obj = (Banner) super.clone();

			if (this._myTable != null) {

				obj._myTable = (Table) this._myTable.clone();

			}

		}

		catch (Exception ex) {

			ex.printStackTrace(System.err);

		}

		return obj;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.presentation.Block#getStyleNames()
	 */
	public Map getStyleNames() {
		Map map = new HashMap();
		map.put(IMAGE_STYLE, DEFAULT_IMAGE_STYLE);
		return map;
	}
	/**
	 * @param width The _fixedWidth to set.
	 */
	public void setMaxWidth(int width) {
		this._maxWidth = width;
	}

}