package is.idega.idegaweb.travel.service.fishing.business;


public interface FishingBusiness extends is.idega.idegaweb.travel.business.TravelStockroomBusiness
{
 public int createFishing(int supplierId, Integer fileId, String name, String number, String description, int[] activeDays, boolean isValid, int discountTypeId) throws Exception;
 public int updateFishing(int serviceId, int supplierId, Integer fileId, String name, String number, String description, int[] activeDays, boolean isValid, int discountTypeId) throws Exception;
}
