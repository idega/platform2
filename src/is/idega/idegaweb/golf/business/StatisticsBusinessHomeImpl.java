package is.idega.idegaweb.golf.business;


public class StatisticsBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements StatisticsBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return StatisticsBusiness.class;
 }


 public StatisticsBusiness create() throws javax.ejb.CreateException{
  return (StatisticsBusiness) super.createIBO();
 }



}