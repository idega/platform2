package is.idega.demo.textviewer.business;


public class TextViewBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements TextViewBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return TextViewBusiness.class;
 }


 public TextViewBusiness create() throws javax.ejb.CreateException{
  return (TextViewBusiness) super.createIBO();
 }



}