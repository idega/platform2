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
	

	public Banner() {

	}

	public Banner(int bannerID) {

		this();

		_bannerID = bannerID;

	}

	public Banner(String attribute) {

		this();

		_attribute = attribute;

	}

	public void main(IWContext iwc) throws Exception {

		_iwrb = getResourceBundle(iwc);

		_iwb = getBundle(iwc);

		_isAdmin = iwc.hasEditPermission(this);

		_iLocaleID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());

		BannerEntity banner = null;

		_myTable = new Table(1, 2);

		_myTable.setCellpadding(0);

		_myTable.setCellspacing(0);

		_myTable.setBorder(0);

		if (_bannerID <= 0) {

			String sBannerID = iwc.getParameter(BannerBusiness.PARAMETER_BANNER_ID);

			if (sBannerID != null)

				_bannerID = Integer.parseInt(sBannerID);

			else if (getICObjectInstanceID() > 0) {

				_bannerID = BannerFinder.getRelatedEntityId(getICObjectInstance());

				if (_bannerID <= 0) {

					BannerBusiness.saveBanner(_bannerID, getICObjectInstanceID(), null);

					_newObjInst = true;

				}

			}

		}

		if (_newObjInst) {

			_bannerID = BannerFinder.getRelatedEntityId(((com.idega.core.component.data.ICObjectInstanceHome) com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(getICObjectInstanceID()));

		}

		if (_bannerID > 0) {

			banner = BannerFinder.getBanner(_bannerID);

		}

		else if (_attribute != null) {

			banner = BannerFinder.getBanner(_attribute);

			if (banner != null) {

				_bannerID = banner.getID();

			}

			else {

				BannerBusiness.saveBanner(-1, -1, _attribute);

			}

			_newWithAttribute = true;

		}

		if (_newWithAttribute) {

			_bannerID = BannerFinder.getBanner(_attribute).getID();

		}

		int row = 1;

		if (_isAdmin) {

			_myTable.add(getAdminPart(iwc), 1, row);

			row++;

		}

		Link link = getBanner(iwc, banner);

		if (link != null)

		_myTable.add(link, 1, row);

		add(_myTable);

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
				
				if(_maxWidth>0) {
					image.setMaxImageWidth(_maxWidth);
				}


				bannerLink = new Link(getStyleObject(image, IMAGE_STYLE));

				if (_target != null) {

					bannerLink.setTarget(_target);

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

		if (bannerLink != null)

		return bannerLink;

		return null;

	}

	private Link getAdminPart(IWContext iwc) {

		Image createImage = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER).getImage("shared/create.gif");

		Link createLink = new Link(createImage);

		createLink.setWindowToOpen(BannerEditorWindow.class);

		createLink.addParameter(BannerBusiness.PARAMETER_BANNER_ID, _bannerID);

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

		_target = target;

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
		_maxWidth = width;
	}

}