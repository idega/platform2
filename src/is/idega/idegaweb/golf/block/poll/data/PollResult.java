package is.idega.idegaweb.golf.block.poll.data;


public interface PollResult extends com.idega.data.IDOLegacyEntity
{
 public int getHits();
 public is.idega.idegaweb.golf.block.poll.data.PollOption getOption();
 public int getOptionId();
 public void initializeAttributes();
 public void setHits(int p0);
 public void setOption(int p0);
 public void setOption(is.idega.idegaweb.golf.block.poll.data.PollOption p0);
}
