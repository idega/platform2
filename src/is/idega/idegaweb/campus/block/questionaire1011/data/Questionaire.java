package is.idega.idegaweb.campus.block.questionaire1011.data;


public interface Questionaire extends com.idega.data.IDOEntity
{
 public int getAnswer1();
 public int getAnswer2();
 public int getAnswer3();
 public int getAnswer4();
 public int getAnswer5();
 public int getAnswer6();
 public int getAnswer7();
 public int getAnswer8();
 public int getAnswer9();
 public com.idega.core.user.data.User getUser();
 public int getUserID();
 public void initializeAttributes();
 public void setAnswer1(int p0);
 public void setAnswer2(int p0);
 public void setAnswer3(int p0);
 public void setAnswer4(int p0);
 public void setAnswer5(int p0);
 public void setAnswer6(int p0);
 public void setAnswer7(int p0);
 public void setAnswer8(int p0);
 public void setAnswer9(int p0);
 public void setUser(com.idega.core.user.data.User p0);
 public void setUserID(int p0);
}
