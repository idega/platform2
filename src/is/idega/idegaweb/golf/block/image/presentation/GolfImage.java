/*
 * Created on 21.4.2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.golf.block.image.presentation;

import java.sql.SQLException;

import is.idega.idegaweb.golf.block.image.data.ImageEntity;
import is.idega.idegaweb.golf.block.image.data.ImageEntityHome;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWCacheManager;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.util.caching.Cache;

/**
 * @author laddi
 */
public class GolfImage extends Image {

	/**
	 * @param imageId
	 * @throws SQLException
	 */
	public GolfImage(int imageId) throws SQLException {
		super(imageId);
	}

	protected void setImageURL(IWContext iwc) throws Exception {
		IWMainApplication iwma = iwc.getIWMainApplication();
		ImageEntity image = null;
		//**@todo: remove this when no longer needed

		if (useCaching) {
			Cache cachedImage = (Cache) IWCacheManager.getInstance(iwma).getCachedBlobObject("is.idega.idegaweb.golf.block.image.data.ImageEntity", imageId, iwma);
			if (cachedImage != null) {
				image = (ImageEntity) cachedImage.getEntity();
				setURL(cachedImage.getVirtualPathToFile());
			}
		}

		if (image == null) {
			image = ((ImageEntityHome) IDOLookup.getHomeLegacy(ImageEntity.class)).findByPrimaryKeyLegacy(imageId);
			StringBuffer URIBuffer;
			URIBuffer = new StringBuffer("/servlet/GolfImageServlet/");
			URIBuffer.append(imageId);
			URIBuffer.append("image?");
			URIBuffer.append("image_id");
			URIBuffer.append("=");
			URIBuffer.append(imageId);
			setURL(URIBuffer.toString());
		}
	}
}