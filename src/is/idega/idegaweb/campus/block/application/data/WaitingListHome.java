package is.idega.idegaweb.campus.block.application.data;


public interface WaitingListHome extends com.idega.data.IDOHome
{
 public WaitingList create() throws javax.ejb.CreateException;
 public WaitingList findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByApartmentTypeAndComplex(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findByApartmentTypeAndComplexForApplicationType(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findByApartmentTypeAndComplexForTransferType(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findByApplicantID(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySQL(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findNextForTransferByApartmentTypeAndComplex(int p0,int p1,int p2)throws javax.ejb.FinderException;

}