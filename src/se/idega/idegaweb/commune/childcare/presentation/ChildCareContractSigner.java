/*
 * Created on 21.5.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;



import com.idega.block.contract.business.ContractService;
import com.idega.block.contract.data.Contract;
import com.idega.block.contract.data.ContractCategory;
import com.idega.block.contract.data.ContractCategoryHome;
import com.idega.block.contract.data.ContractHome;
import com.idega.builder.data.IBPage;
import com.idega.business.IBOLookup;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;

import com.idega.presentation.text.Link;

import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;

import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLException;
import com.idega.xml.XMLParser;


/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ChildCareContractSigner extends Block implements Builderaware{
	
	public final static String PAR_CONTRACT_ID = "ChildCareContractSigner_CONTRACT_ID";
	
	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
		
	public ChildCareContractSigner() {
		super();
	}	
		
	public void main(IWContext iwc) {
				
		try{
		add(makeTableOfContracts(iwc));	
//			control(iwc);
	}catch(Exception ex){
		ex.printStackTrace();
		}
	}	


	private Form makeTableOfContracts(IWContext iwc) throws Exception{
		System.out.println("makeTableOfContracts()");			
		ContractService service = (ContractService) IBOLookup.getServiceInstance(iwc,ContractService.class);
		ContractHome home = service.getContractHome();
		ContractCategoryHome catHome = service.getContractCategoryHome();
		
		Collection contracts = home.findAllByUser(iwc.getCurrentUser().getID());
		if (contracts == null){
			return new Form();
		}
		
		Iterator i = contracts.iterator();
		
		Table t = new Table(4, contracts.size());
		t.setCellpadding(2);
		t.setCellspacing(2);	
		t.setBorder(0);	
		
		int row = 1;
		
//		t.add(new Text("Text"), 1, 1);
//		t.add(new Text("Text"), 1, 1);
//		t.add(new Text("Text"), 1, 1);
		IWResourceBundle iwrb = getResourceBundle(iwc);		
		
		while (i.hasNext()){
			Contract contract = (Contract) i.next();

			System.out.println("Contract.id: " +contract.getPrimaryKey().toString());
			
			String text = contract.getText();
			if (text == null){
				text = " ";
			}
			
			ContractCategory cat = catHome.findByPrimaryKey(contract.getCategoryId());
			t.add(getContractIcon(iwc/*, contract*/), 1, row);
			t.add(new Text(cat.getName()), 2, row);
						
			if (contract.isSigned()) {
				t.add(new Text(iwrb.getLocalizedString("ccconsign_signed", "Signed") + contract.getSignedDate()), 4, row);
			}else {

				Link signBtn = new Link(iwrb.getLocalizedString("ccconsign_signcon","Sign Contract"));
				signBtn.setWindowToOpen(ChildCareWindow.class);
				signBtn.addParameter(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_SIGN_CONTRACT);
				signBtn.setParameter(ChildCareAdminWindow.PARAMETER_CONTRACT_ID, contract.getPrimaryKey().toString());
				signBtn.setAsImageButton(true);
				
//				signBtn.setParameter(PAR_CONTRACT_ID, ""+contract.getID());
//				signBtn.setParameter(ACTION, ACTION_FILLOUT);
				t.add(signBtn, 3, row);				
			}
				
//			t.add(new Text(""+contract.getID()), 2, row);
//			t.add(new Text(escapeHTML(contract.getXmlSignedData())), 3, row);
			
			row ++;
		}
		setStyle(this, "font-size:10px");		
		setStyle(this, "font-family: sans-serif");
		
		Form f = new Form();
		f.add(t);
		return f;
	}


	public static void main(String[] aqrgs){
		String text = "Dette er en test <dato /> av <xml/> parser";
		
		try {
			XMLParser parser = new XMLParser();
			XMLDocument document = parser.parse(new StringReader("<dummy>" + text + "</dummy>"));
		
			XMLElement root = document.getRootElement();
			Iterator it = root.getContent().iterator();
			while (it.hasNext()){
				Object obj = it.next();
				if (obj instanceof XMLElement) {
					System.out.println(((XMLElement) obj).getName());
				}
//				else if (obj instanceof XMLCDATA) { ignore	}
				else if (obj instanceof String) {
					System.out.println(((String) obj));			
				}
			}	
						
			List fields = root.getChildren();
			Iterator i = fields.iterator();
			System.out.println(root.getText());			
			System.out.println(root.getName());		
			
			while (i.hasNext()){
				XMLElement f = (XMLElement) i.next();
				System.out.println(f.getText());
				System.out.println(f.getName());
			}
		}catch (XMLException ex){
			ex.printStackTrace();
			return;			
			
		}
	}
	/**
	 * @param iwc
	 * @param contract
	 * @return
	 */
	private PresentationObject getContractIcon(IWContext iwc/*, Contract contract*/)
	{
		//TODO Display a PDF link if possible
		Image image = getBundle(iwc).getImage("contracticon.gif");
		return image;
	}

	private static IBPage _page;	

	public void setResponsePage(IBPage page){
		_page = page;
	}
	
	public IBPage getResponsePage(){
		return _page;
	}
	
	public static final String escapeHTML(String s){
		if (s == null){
			return "";
		}
		
	   StringBuffer sb = new StringBuffer();
	   int n = s.length();
	   for (int i = 0; i < n; i++) {
		  char c = s.charAt(i);
		  switch (c) {
			 case '<': sb.append("&lt;"); break;
			 case '>': sb.append("&gt;"); break;
			 case '&': sb.append("&amp;"); break;
			 case '"': sb.append("&quot;"); break;
			 case '�': sb.append("&agrave;");break;
			 case '�': sb.append("&Agrave;");break;
			 case '�': sb.append("&acirc;");break;
			 case '�': sb.append("&Acirc;");break;
			 case '�': sb.append("&auml;");break;
			 case '�': sb.append("&Auml;");break;
			 case '�': sb.append("&aring;");break;
			 case '�': sb.append("&Aring;");break;
			 case '�': sb.append("&aelig;");break;
			 case '�': sb.append("&AElig;");break;
			 case '�': sb.append("&ccedil;");break;
			 case '�': sb.append("&Ccedil;");break;
			 case '�': sb.append("&eacute;");break;
			 case '�': sb.append("&Eacute;");break;
			 case '�': sb.append("&egrave;");break;
			 case '�': sb.append("&Egrave;");break;
			 case '�': sb.append("&ecirc;");break;
			 case '�': sb.append("&Ecirc;");break;
			 case '�': sb.append("&euml;");break;
			 case '�': sb.append("&Euml;");break;
			 case '�': sb.append("&iuml;");break;
			 case '�': sb.append("&Iuml;");break;
			 case '�': sb.append("&ocirc;");break;
			 case '�': sb.append("&Ocirc;");break;
			 case '�': sb.append("&ouml;");break;
			 case '�': sb.append("&Ouml;");break;
			 case '�': sb.append("&oslash;");break;
			 case '�': sb.append("&Oslash;");break;
			 case '�': sb.append("&szlig;");break;
			 case '�': sb.append("&ugrave;");break;
			 case '�': sb.append("&Ugrave;");break;         
			 case '�': sb.append("&ucirc;");break;         
			 case '�': sb.append("&Ucirc;");break;
			 case '�': sb.append("&uuml;");break;
			 case '�': sb.append("&Uuml;");break;
			 case '�': sb.append("&reg;");break;         
			 case '�': sb.append("&copy;");break;   
			 case '�': sb.append("&euro;"); break;
			 // be carefull with this one (non-breaking whitee space)
			 case ' ': sb.append("&nbsp;");break;         
         
			 default:  sb.append(c); break;
		  }
	   }
	   return sb.toString();
	}	
	
}
