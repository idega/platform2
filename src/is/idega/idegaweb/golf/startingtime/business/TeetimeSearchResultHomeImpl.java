package is.idega.idegaweb.golf.startingtime.business;


public class TeetimeSearchResultHomeImpl extends com.idega.business.IBOHomeImpl implements TeetimeSearchResultHome
{
 protected Class getBeanInterfaceClass(){
  return TeetimeSearchResult.class;
 }


 public TeetimeSearchResult create() throws javax.ejb.CreateException{
  return (TeetimeSearchResult) super.createIBO();
 }



}