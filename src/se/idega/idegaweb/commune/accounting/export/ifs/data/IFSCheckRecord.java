package se.idega.idegaweb.commune.accounting.export.ifs.data;


public interface IFSCheckRecord extends com.idega.data.IDOEntity
{
 public java.lang.String getError();
 public java.lang.String getErrorAmountMissing();
 public java.lang.String getErrorConcerns();
 public se.idega.idegaweb.commune.accounting.export.ifs.data.IFSCheckHeader getHeader();
 public int getHeaderId();
 public void initializeAttributes();
 public void setError(java.lang.String p0);
 public void setErrorAmountMissing();
 public void setErrorConcerns(java.lang.String p0);
 public void setHeader(se.idega.idegaweb.commune.accounting.export.ifs.data.IFSCheckHeader p0);
 public void setHeaderId(int p0);
}
