package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;
import javax.ejb.FinderException;
import com.idega.block.category.business.CategoryService;
import com.idega.block.category.data.ICCategory;
import com.idega.block.trade.stockroom.business.ProductEditorBusiness;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.business.IBOLookup;
import com.idega.core.file.data.ICFile;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Window;


/**
 * @author gimmi
 */
public class ProductCatalogLayoutMetadataList extends AbstractProductCatalogLayout {

	private static final String METADATA = "metadata_";
	
	public PresentationObject getCatalog(ProductCatalog productCatalog, IWContext iwc, List productCategories) throws RemoteException, FinderException {
		Table table = new Table();
		table.setWidth("100%");
		table.setCellspacing(1);
		table.setCellpadding(2);

		Collection products = productCatalog.getProducts(productCategories);
		Iterator iter = products.iterator();
		Iterator catIter = productCategories.iterator();
		

		int row = 1;
		int column = 1;
		int imageColumn = 1;
		
		Hashtable metaDataTypes = new Hashtable();
		
		table.add(productCatalog.getHeader(productCatalog.iwrb.getLocalizedString("product.product", "Product")), column++, row);
		List metadataKeys = new Vector();
		String[] metadata = new String[10+column];


		while (catIter.hasNext()) {
			ICCategory element = (ICCategory) catIter.next();
			metaDataTypes.putAll(getCategoryService(iwc).getInheritedMetaDataTypes(element.getMetaDataTypes(), element));
		}

		if (!metaDataTypes.isEmpty()) {
			Set set = metaDataTypes.keySet();
			Iterator setIter = set.iterator();
			while (setIter.hasNext()) {
				String key = setIter.next().toString();
				if (!metadataKeys.contains(key)) {
					metadataKeys.add(key);
					metadata[column] = key;
					table.add(productCatalog.getHeader(productCatalog.iwrb.getLocalizedString(METADATA + key, key)), column++, row);
				}
			}
		}
		imageColumn = column;
		table.add(productCatalog.getHeader(productCatalog.iwrb.getLocalizedString("product.images", "Images")), column++, row);
		table.setRowColor(row, productCatalog.getHeaderBackgroundColor());
		
		Product product;
		String key;
		String meta;
		Locale locale = iwc.getCurrentLocale();
		while ( iter.hasNext() ) {
			++row;
			product = (Product) iter.next();
			if (productCatalog.hasEditPermission()) {
				table.add(productCatalog.getProductEditorLink(product), 1, row);
			}
			table.add(productCatalog.getText(product.getProductName(iwc.getCurrentLocaleId())) ,1, row);
			for (int i = 0; i < metadata.length; i++) {
				key = metadata[i];
				if (key != null) {
					meta = product.getMetaData(METADATA + key + "_" + locale.toString());
					if (meta != null) {
						table.add(productCatalog.getText(meta), i, row);
					}
				}
			}
			
			Collection coll = getEditorBusiness(iwc).getFiles(product);
			Iterator images = coll.iterator();
			Image image;
			int counter = 0;
			while (images.hasNext()) {
				++counter;
				try {
					image = new Image( new Integer(( (ICFile) images.next()).getPrimaryKey().toString()).intValue() );
					Window window = new Window(image);
					Link link = new Link(productCatalog.getText(Integer.toString(counter)));
					link.setWindow(window);
					
					table.add(link, imageColumn, row);
					table.add(productCatalog.getText(Text.NON_BREAKING_SPACE), imageColumn, row);
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
			table.setRowColor(row, productCatalog.getBackgroundColor());
		}
		
		return table;
	}
	
	protected ProductEditorBusiness getEditorBusiness(IWContext iwc) {
		return ProductEditorBusiness.getInstance();
	}
	
	private CategoryService getCategoryService(IWApplicationContext iwac) throws RemoteException {
		return (CategoryService) IBOLookup.getServiceInstance(iwac, CategoryService.class);
	}
	
}
