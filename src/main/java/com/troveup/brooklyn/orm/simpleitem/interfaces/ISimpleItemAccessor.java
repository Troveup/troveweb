package com.troveup.brooklyn.orm.simpleitem.interfaces;

import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.simpleitem.model.OrderNote;
import com.troveup.brooklyn.orm.simpleitem.model.SimpleItem;

import javax.jdo.PersistenceManager;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 3/25/16.
 */
public interface ISimpleItemAccessor
{
    SimpleItem getSimpleItemByItemId(Long itemId, IEnums.SEEK_MODE mode);
    SimpleItem persistSimpleItem(SimpleItem item);
    String getControlTitleById(Long controlId);
    String getControlOptionLabelById(Long controlOptionId);
    List<SimpleItem> getReferenceOrderedSimpleItemsWithOrderNumbersByInfluencerId(Long influencerId);
    List<SimpleItem> getReferenceSimpleItemsByInfluencerId(Long influencerId, IEnums.SEEK_MODE mode);
    List<SimpleItem> getLatestSimpleItemOrders(Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode);
    SimpleItem getManagedSimpleItem(Long itemId, PersistenceManager pm);
    Boolean updateSimpleItemManufacturerCompletionDate(Long itemId, Date date);
    Boolean updateSimpleItemManufacturerStatus(Long itemId, SimpleItem.MANUFACTURING_STATUS status);
    Boolean updateSimpleItemManufacturerPrice(Long itemId, Float price);
    Boolean addSimpleItemNote(Long itemId, Long userIdCreator, String note);
    List<OrderNote> getAllActiveNotesBySimpleItemId(Long itemId, IEnums.SEEK_MODE mode);
    List<SimpleItem> getOrderedSimpleItemsByInfluencerId(Long influencerId, IEnums.SEEK_MODE mode);
    List<SimpleItem> getPaginatedReadyToWearSimpleItemReferences(Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode);
    List<SimpleItem> getListPersistenceManagedSimpleItems(List<SimpleItem> items, PersistenceManager pm);
}
