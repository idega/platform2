package is.idega.idegaweb.campus.block.allocation.presentation;
import is.idega.idegaweb.campus.block.allocation.business.CampusContractWriter;

import java.util.StringTokenizer;

import com.idega.block.media.business.MediaBusiness;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.ui.Window;
import com.lowagie.text.Font;

public class ContractFilerWindow extends Window {
	public static String prmOneId = "contract_id", prmTest = "test", prmManyIds = "many_ids";
	public static String prmSeperator = "_", prmFileName = "fname";

	public ContractFilerWindow() {
		setResizable(true);
		setMenubar(true);
		keepFocus();
	}
	

  public void main(IWContext iwc){

		//IWContext iwc = getIWContext();

		//IWMainApplication iwma = iwc.getApplication()
		String identifier = "is.idega.idegaweb.campus";
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(identifier).getResourceBundle(iwc);
		Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
		Font paraFont = new Font(Font.HELVETICA, 10, Font.BOLD);
		Font nameFont = new Font(Font.HELVETICA, 12, Font.BOLDITALIC);
		Font tagFont = new Font(Font.HELVETICA, 9, Font.BOLDITALIC);
		Font textFont = new Font(Font.HELVETICA, 8, Font.NORMAL);
		int fileId = -1;
		Page p = getParentPage();
		//Page p = getPage();
		if (iwc.getParameter(prmOneId) != null) {
			int id = Integer.parseInt(iwc.getParameter(prmOneId));
			fileId = CampusContractWriter.writePDF(id, iwrb, nameFont, titleFont, paraFont, tagFont, textFont);
		}
		else if (iwc.getParameter(prmTest) != null) {
			fileId = CampusContractWriter.writeTestPDF(iwrb, nameFont, titleFont, paraFont, tagFont, textFont);
		}
		else if (iwc.getParameter(prmManyIds) != null) {
			//System.err.println(prmManyIds);
			String values = iwc.getParameter(prmManyIds);
			StringTokenizer st = new StringTokenizer(values, prmSeperator);
			int[] ids = new int[st.countTokens()];
			for (int i = 0; i < ids.length; i++) {
				String token = st.nextToken();
				ids[i] = Integer.parseInt(token);
			}

			fileId = CampusContractWriter.writePDF(ids, iwrb, nameFont, titleFont, paraFont, tagFont, textFont);
		}
		else {
			add("nothing");
		}
		if (fileId > 0) {
			String url = MediaBusiness.getMediaURL(fileId, iwc.getIWMainApplication());
			p.setToRedirect(url, 1);
			p.setParentToReload();
		}
		else {
			add("Could not create contract file");
		}
	}

}
