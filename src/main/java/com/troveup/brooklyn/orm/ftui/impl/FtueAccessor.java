package com.troveup.brooklyn.orm.ftui.impl;

import com.troveup.brooklyn.orm.cart.interfaces.IPromoCodeAccessor;
import com.troveup.brooklyn.orm.cart.model.PromoCode;
import com.troveup.brooklyn.orm.common.datanucleus.ObjectAccessor;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.countries.interfaces.ICountryAccessor;
import com.troveup.brooklyn.orm.countries.model.Country;
import com.troveup.brooklyn.orm.countries.model.Subdivision;
import com.troveup.brooklyn.orm.ftui.model.*;
import com.troveup.brooklyn.orm.order.model.PrintOrder;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.util.MoneyUtil;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jdo.*;
import javax.jdo.annotations.PersistenceCapable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by tim on 6/16/15.
 */
public class FtueAccessor extends ObjectAccessor
{
    @Autowired
    ICountryAccessor countryAccessor;

    @Autowired
    IPromoCodeAccessor promoCodeAccessor;

    public FtueAccessor(PersistenceManagerFactory pmfProxy)
    {
        super(pmfProxy);
    }

    public String persistFtueRecord(FtuePersistedRecord record)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        pm.setDetachAllOnCommit(true);
        String rval = "";

        try
        {
            if (record == null)
                throw new NullPointerException("Arguments cannot be null");

            List<FtuePersistedRecord> priorRecords =
                    getPersistedRecordsByEmailAttached(record.getRequest().getShipping_info().getEmail(), pm);

            //Before the referral code system was in place, there was nothing.  Iterate through all prior records
            //and see if there was a referral code attached.  If there was, reuse it.  Otherwise, generate a new one
            //and assign it to all matching records with the same e-mail address.  Also make sure that they're not
            //re-using the same e-mail address to generate referrals.
            String referralCode = null;
            Boolean hasUsedPriorReferrerCode = false;
            for (FtuePersistedRecord priorRecord : priorRecords)
            {
                if (priorRecord.getReferralCode() != null && priorRecord.getReferralCode().length() > 0)
                    referralCode = priorRecord.getReferralCode();

                if (priorRecord.getReferrerCode() != null && priorRecord.getReferrerCode().length() > 0)
                    hasUsedPriorReferrerCode = true;
            }

            //Didn't find one, generate it
            if (referralCode == null)
                referralCode = generateUniqueFtueReferralCode();

            //If there are any records missing one, go ahead and add it.
            for (FtuePersistedRecord priorRecord : priorRecords)
            {
                if (priorRecord.getReferralCode() == null)
                    priorRecord.setReferralCode(referralCode);
            }

            //Set the referral code on the new record and persist it
            record.setReferralCode(referralCode);

            //Make sure that anybody with the same e-mail address doesn't count as a referral, otherwise
            //they could refer themselves
            if ((record.getReferrerCode() != null && record.getReferrerCode().equals(referralCode)) || hasUsedPriorReferrerCode)
                record.setReferrerCode(null);

            pm.makePersistent(record);

            rval = referralCode;

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public String persistFtueCheckout(FtueCheckout checkoutSessionObject)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        String rval = "";

        try
        {
            if (checkoutSessionObject == null || checkoutSessionObject.getLandingUuid() == null)
                throw new NullPointerException("Argument cannot be null, and the FtuePersistedRecord cannot be null.");

            FtuePersistedRecord managedPersistedRecord;

            if (checkoutSessionObject.getRecordReference() == null)
                managedPersistedRecord = getPersistedRecordAttached(checkoutSessionObject.getLandingUuid(), pm);
            else
                managedPersistedRecord =
                        getPersistedRecordAttached(checkoutSessionObject.getRecordReference().getFtuePersistedRecordId(), pm);

            Country managedBillingCountry =
                    countryAccessor.getCountryAttached(checkoutSessionObject.getBillingAddress().getCountry().getPrimaryKeyId(), pm);
            Country managedShippingCountry;
            Subdivision managedBillingSubdivision =
                    countryAccessor.getSubdivisionAttached(checkoutSessionObject.getBillingAddress().getSubdivision().getPrimaryKeyId(), pm);
            Subdivision managedShippingSubdivision;

            if (managedBillingCountry.getPrimaryKeyId().equals(checkoutSessionObject.getShippingAddress().getCountry().getPrimaryKeyId()))
                managedShippingCountry = managedBillingCountry;
            else
                managedShippingCountry = countryAccessor.getCountryAttached(checkoutSessionObject.getShippingAddress().getCountry().getPrimaryKeyId(), pm);

            if (managedBillingSubdivision.getPrimaryKeyId().equals(checkoutSessionObject.getShippingAddress().getSubdivision().getPrimaryKeyId()))
                managedShippingSubdivision = managedBillingSubdivision;
            else
                managedShippingSubdivision =
                        countryAccessor.getSubdivisionAttached(checkoutSessionObject.getShippingAddress().getSubdivision().getPrimaryKeyId(), pm);

            checkoutSessionObject.getBillingAddress().setCountry(managedBillingCountry);
            checkoutSessionObject.getBillingAddress().setSubdivision(managedBillingSubdivision);
            checkoutSessionObject.getShippingAddress().setCountry(managedShippingCountry);
            checkoutSessionObject.getBillingAddress().setSubdivision(managedShippingSubdivision);

            checkoutSessionObject.setRecordReference(managedPersistedRecord);

            rval = generateUniqueFtueCheckoutId();

            checkoutSessionObject.setCheckoutSessionId(rval);

            pm.makePersistent(checkoutSessionObject);
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public Boolean updateFtueCheckoutStatus(String checkoutId, FtueCheckout.FTUE_CHECKOUT_STATUS status)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (checkoutId == null || checkoutId.length() == 0 || status == null)
                throw new Exception("Arguments cannot be null or empty");

            FtueCheckout ftueCheckout = getFtueCheckoutRecordAttached(checkoutId, pm);

            ftueCheckout.setStatus(status);

            rval = true;

        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public Boolean updateModelFileName(String checkoutId, String modelFileName)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (checkoutId == null || checkoutId.length() == 0 || modelFileName == null || modelFileName.length() == 0)
                throw new Exception("Arguments cannot be null or empty");

            FtueCheckout ftueCheckout = getFtueCheckoutRecordAttached(checkoutId, pm);

            ftueCheckout.setModelFileName(modelFileName);

            rval = true;

        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public Boolean checkIfCheckoutIdExists(String checkoutId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = true;

        try
        {
            if (checkoutId == null || checkoutId.length() == 0)
                throw new NullPointerException("Argument cannot be null or empty");

            Query query = pm.newQuery(FtueCheckout.class, "checkoutSessionId == :checkoutId");
            query.setResult("count(this)");

            Long queryResult = (Long) query.execute(checkoutId);

            if (queryResult == 0)
                rval = false;
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public Boolean checkIfFtueReferralCodeExists(String referralCode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = true;

        try
        {
            if (referralCode == null || referralCode.length() == 0)
                throw new NullPointerException("Argument cannot be null or empty");

            Query query = pm.newQuery(FtuePersistedRecord.class, "referralCode == :referralCode");
            query.setResult("count(this)");

            Long queryResult = (Long) query.execute(referralCode);

            if (queryResult == 0)
                rval = false;
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public long getAddressCountAddress(FtueRequestAddress address)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        long rval = 0l;

        try
        {
            if (address == null)
                throw new NullPointerException("Arguments cannot be null");

            //Query query = pm.newQuery(FtueRequestAddress.class, "this.equals(:searchAddress)");

            Query query = pm.newQuery(FtuePersistedRecord.class,
            "request.shipping_info.city.toLowerCase().equals(\"" + address.getCity().toLowerCase() + "\") && " +
                //"name.toLowerCase().equals(\"" + address.getName().toLowerCase() + "\") && " +
                "request.shipping_info.zip.toLowerCase().equals(\"" + address.getZip().toLowerCase() + "\") && " +
                "request.shipping_info.street1.toLowerCase().equals(\"" + address.getStreet1().toLowerCase() + "\") && " +
                "request.shipping_info.street2.toLowerCase().equals(\"" + address.getStreet2().toLowerCase() + "\") && " +
                "request.shipping_info.state.toLowerCase().equals(\"" + address.getState().toLowerCase() + "\")");// && " +
                //"email.toLowerCase().equals(\"" + address.getEmail().toLowerCase() + "\")");
            query.setResult("count(this)");

            rval = (Long) query.execute(address);

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public Long getFtueOrderCount()
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Long rval = 0l;

        try
        {
            Query query = pm.newQuery(FtuePersistedRecord.class, "1 == 1");
            query.setResult("count(this)");

            rval = (Long) query.execute();

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public FtuePersistedRecord getPersistedRecord(Long persistedRecordId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        FtuePersistedRecord rval = null;

        try
        {
            if (persistedRecordId == null)
                throw new NullPointerException("Argument cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(FtuePersistedRecord.class, "ftuePersistedRecordId == :persistedRecordId");

            List<FtuePersistedRecord> queryList = (List<FtuePersistedRecord>) query.execute(persistedRecordId);

            if (queryList != null && queryList.size() > 0)
                rval = queryList.get(0);
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }
        return rval;
    }

    public FtuePersistedRecord getPersistedRecord(String landingPageId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        FtuePersistedRecord rval = null;

        try
        {
            if (landingPageId == null || landingPageId.length() == 0)
                throw new NullPointerException("Argument cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(FtuePersistedRecord.class, "landingUuid == :landingPageId");

            List<FtuePersistedRecord> queryList = (List<FtuePersistedRecord>) query.execute(landingPageId);

            if (queryList != null && queryList.size() > 0)
                rval = queryList.get(0);
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }
        return rval;
    }

    public FtueCheckout getPersistedFtueCheckoutRecord(String checkoutId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        FtueCheckout rval = null;

        try
        {
            if (checkoutId == null)
                throw new NullPointerException("Argument cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(FtueCheckout.class, "checkoutSessionId == :checkoutId");

            List<FtueCheckout> queryList = (List<FtueCheckout>) query.execute(checkoutId);

            if (queryList != null && queryList.size() > 0)
                rval = queryList.get(0);

            if (rval != null)
                rval = pm.detachCopy(rval);
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }
        return rval;
    }

    public FtueCheckout getFtueCheckoutRecordAttached(String checkoutId, PersistenceManager pm)
    {
        FtueCheckout rval = null;

        if (checkoutId == null)
            throw new NullPointerException("Argument cannot be null");

        Query query = pm.newQuery(FtueCheckout.class, "checkoutSessionId == :checkoutId");

        List<FtueCheckout> queryList = (List<FtueCheckout>) query.execute(checkoutId);

        if (queryList != null && queryList.size() > 0)
            rval = queryList.get(0);

        return rval;
    }

    public FtueCheckout validateAndApplyPromoCode(String promoCode, FtueCheckout checkoutObject)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        FtueCheckout rval = null;


        try {
            PromoCode persistedGlobalCode = promoCodeAccessor.validatePromoCode(promoCode);
            PromoCode appliedCode = new PromoCode();
            appliedCode.setPromoCode(promoCode);
            appliedCode.setControlReference(false);

            configureFetchGroups(pm, IEnums.SEEK_MODE.FTUECHECKOUT_CART_PRICES);

            //Code is validated, go ahead and apply it
            if (persistedGlobalCode != null) {
                FtueCheckout managedCheckoutObject = getFtueCheckoutRecordAttached(
                        checkoutObject.getCheckoutSessionId(), pm);


                if (persistedGlobalCode.getBlanketCategory().equals(PromoCode.DISCOUNT_CATEGORY.SHIPPING.toString()) &&
                        moneyGreaterThanZeroTest(managedCheckoutObject.getShipping()))
                {
                    Money shipping = MoneyUtil.bigDecimalToMoney(null, checkoutObject.getShipping());

                    //If it is, and there's a percent discount to subtract, subtract the percentage
                    if (persistedGlobalCode.getPercentDiscount() > 0)
                    {
                        Money priceDelta = shipping.multipliedBy(persistedGlobalCode.getPercentDiscount() / 100,
                                RoundingMode.UP);
                        shipping = shipping.minus(priceDelta);
                        appliedCode.setPriceDelta(priceDelta.getAmount());
                    }
                    //Otherwise, subtract the flat dollar amount
                    else if (persistedGlobalCode.getDollarDiscount() != null &&
                            moneyGreaterThanZeroTest(persistedGlobalCode.getDollarDiscount()))
                    {

                        //Account for a flat promo reduction that might push it negative
                        if (shipping.isGreaterThan(MoneyUtil.bigDecimalToMoney(null,
                                persistedGlobalCode.getDollarDiscount())))
                        {
                            shipping = shipping.minus(persistedGlobalCode.getDollarDiscount());
                            appliedCode.setPriceDelta(persistedGlobalCode.getDollarDiscount());
                        }
                        else
                        {
                            appliedCode.setPriceDelta(shipping.getAmount());
                            shipping = MoneyUtil.bigDecimalToMoney(null, BigDecimal.ZERO);
                        }
                    }

                    //Set the new dollar amount
                    managedCheckoutObject.setShipping(shipping.getAmount());
                }
                //Check if this is a subtotal code
                else if (persistedGlobalCode.getBlanketCategory().equals(PromoCode.DISCOUNT_CATEGORY.SUB_TOTAL.toString()) &&
                        moneyGreaterThanZeroTest(checkoutObject.getSubtotal()))
                {
                    Money subtotal = MoneyUtil.bigDecimalToMoney(null, checkoutObject.getSubtotal());

                    //If it is, and there's a percent discount to subtract, subtract the percentage
                    if (persistedGlobalCode.getPercentDiscount() > 0)
                    {
                        Money priceDelta = subtotal.multipliedBy(persistedGlobalCode.getPercentDiscount() / 100,
                                RoundingMode.UP);
                        subtotal = subtotal.minus(priceDelta);
                        appliedCode.setPriceDelta(priceDelta.getAmount());
                    }
                    //Otherwise, subtract the flat dollar amount
                    else if (persistedGlobalCode.getDollarDiscount() != null &&
                            moneyGreaterThanZeroTest(persistedGlobalCode.getDollarDiscount()))
                    {
                        //Account for a flat promo reduction that might push it negative
                        if (subtotal.isGreaterThan(MoneyUtil.bigDecimalToMoney(null,
                                persistedGlobalCode.getDollarDiscount())))
                        {
                            subtotal = subtotal.minus(persistedGlobalCode.getDollarDiscount());
                            appliedCode.setPriceDelta(persistedGlobalCode.getDollarDiscount());
                        }
                        else
                        {
                            appliedCode.setPriceDelta(subtotal.getAmount());
                            subtotal = MoneyUtil.bigDecimalToMoney(null, BigDecimal.ZERO);
                        }
                    }

                    managedCheckoutObject.setSubtotal(subtotal.getAmount());
                }

                managedCheckoutObject.setAppliedCode(appliedCode);

                rval = pm.detachCopy(managedCheckoutObject);
            }
        }
        catch(Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public FtueCheckout recalculateFtueCheckoutGrandTotal(String ftueCheckoutId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        FtueCheckout rval = null;

        try
        {
            if (ftueCheckoutId == null || ftueCheckoutId.length() == 0)
                throw new NullPointerException("Argument cannot be null or empty");

            configureFetchGroups(pm, IEnums.SEEK_MODE.FTUECHECKOUT_CART_PRICES);

            FtueCheckout persistenceManagedFtueCheckout = getFtueCheckoutRecordAttached(ftueCheckoutId, pm);
            BigDecimal grandTotal = BigDecimal.ZERO;
            grandTotal = grandTotal.add(persistenceManagedFtueCheckout.getShipping());
            grandTotal = grandTotal.add(persistenceManagedFtueCheckout.getTax());
            grandTotal = grandTotal.add(persistenceManagedFtueCheckout.getSubtotal());
            persistenceManagedFtueCheckout.setGrandtotal(grandTotal);

            rval = pm.detachCopy(persistenceManagedFtueCheckout);

        } catch(Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public Boolean markFtueCheckoutAsComplete(String ftueCheckoutId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (ftueCheckoutId == null || ftueCheckoutId.length() == 0)
                throw new NullPointerException("Argument cannot be null or empty");

            FtueCheckout checkoutObject = getFtueCheckoutRecordAttached(ftueCheckoutId, pm);
            checkoutObject.setCheckoutDate(new Date());
            checkoutObject.setStatus(FtueCheckout.FTUE_CHECKOUT_STATUS.COMPLETE);

            rval = true;
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public Boolean updateFtueCheckoutTax(String ftueCheckoutId, BigDecimal tax)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (tax == null)
                throw new NullPointerException("Argument cannot be null");

            FtueCheckout checkoutObject = getFtueCheckoutRecordAttached(ftueCheckoutId, pm);

            checkoutObject.setTax(tax);

            rval = true;

        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public List<FtuePersistedRecord> getAllFtuePersistedRecords(IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<FtuePersistedRecord> rval = new ArrayList<>();

        try
        {
            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(FtuePersistedRecord.class, "1==1");

            rval = (List<FtuePersistedRecord>) query.execute();
            rval = (List<FtuePersistedRecord>) pm.detachCopyAll(rval);

        } catch(Exception e)
        {
            logError(e);
        }
        finally {
            pm.close();
        }

        return rval;
    }

    public List<FtuePersistedRecord> getFtuePersistedRecordsByStatus(FtuePersistedRecord.FTUE_STATUS status, IEnums.SEEK_MODE mode, long rangeLimit)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<FtuePersistedRecord> rval = null;

        try
        {
            if (status == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(FtuePersistedRecord.class, "status == :ftueStatus");
            query.setResultClass(FtuePersistedRecord.class);

            if (rangeLimit > 0)
            {
                query.setRange(0l, rangeLimit);
            }

            rval = (List<FtuePersistedRecord>) query.execute(status);

            rval = (List<FtuePersistedRecord>) pm.detachCopyAll(rval);
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public Boolean updateFtueRecordOrderItem(Long persistedRecordId, List<FtueOrderItem> orderItems)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (persistedRecordId == null || orderItems == null || orderItems.size() == 0)
                throw new NullPointerException("Arguments cannot be null or empty");

            FtuePersistedRecord record = getPersistedRecordAttached(persistedRecordId, pm);

            if (record != null) {
                record.getRequest().setOrder_items(orderItems);

                rval = true;
            }
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public Boolean updateFtueRecordResponse(Long persistedRecordId, FtueResponse response)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (persistedRecordId == null || response == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            FtuePersistedRecord record = getPersistedRecordAttached(persistedRecordId, pm);

            if (record != null) {
                record.setResponse(response);
                rval = true;
            }
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public Boolean updateFtuePostCallbackResponse(Long persistedRecordId, FtueResponse response,
                                                  List<FtueOrderItem> orderItems, FtuePersistedRecord.FTUE_STATUS status)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (persistedRecordId == null || response == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            FtuePersistedRecord record = getPersistedRecordAttached(persistedRecordId, pm);

            if (record != null) {
                record.setResponse(response);
                record.getRequest().setOrder_items(orderItems);
                record.setStatus(status);
                rval = true;
            }
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public Boolean updateFtuePrices(List<FtuePersistedRecord> records)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (records == null || records.size() == 0)
                throw new NullPointerException("Argument cannot be null or empty");

            //Populate a list of persistence record identifiers
            List<Long> persistedRecordIds = new ArrayList<>();
            for (FtuePersistedRecord record : records)
            {
                persistedRecordIds.add(record.getFtuePersistedRecordId());
            }

            //Get persistence managed versions of all of our records
            List<FtuePersistedRecord> persistenceManagedRecords = getPersistedRecordsAttached(persistedRecordIds, pm);

            //Create a mapping of all of the persistence managed records so that we keep this to (O)^n
            Map<Long, FtuePersistedRecord> recordMap = new HashMap<>();
            for (FtuePersistedRecord managedRecord : persistenceManagedRecords)
            {
                recordMap.put(managedRecord.getFtuePersistedRecordId(), managedRecord);
            }

            //Persist the material prices from the non-persisted versions to the persisted versions
            for (FtuePersistedRecord record : records)
            {
                recordMap.get(record.getFtuePersistedRecordId()).setMaterialPrices(record.getMaterialPrices());
            }

            rval = true;

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public Boolean bulkUpdateFtueStatuses(List<FtuePersistedRecord> records, FtuePersistedRecord.FTUE_STATUS status)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (records == null || records.size() == 0)
                throw new NullPointerException("Argument cannot be null or empty");

            //Populate a list of persistence record identifiers
            List<Long> persistedRecordIds = new ArrayList<>();
            for (FtuePersistedRecord record : records)
            {
                persistedRecordIds.add(record.getFtuePersistedRecordId());
            }

            //Get persistence managed versions of all of our records
            List<FtuePersistedRecord> persistenceManagedRecords = getPersistedRecordsAttached(persistedRecordIds, pm);

            for (FtuePersistedRecord managedRecord : persistenceManagedRecords)
            {
                managedRecord.setStatus(status);
            }

            rval = true;

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public Boolean bulkUpdateFtueStatusesById(List<Long> records, FtuePersistedRecord.FTUE_STATUS status)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (records == null || records.size() == 0)
                throw new NullPointerException("Argument cannot be null or empty");

            //Get persistence managed versions of all of our records
            List<FtuePersistedRecord> persistenceManagedRecords = getPersistedRecordsAttached(records, pm);

            for (FtuePersistedRecord managedRecord : persistenceManagedRecords)
            {
                managedRecord.setStatus(status);
            }

            rval = true;

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public Boolean updateFtueStatus(Long record, FtuePersistedRecord.FTUE_STATUS status)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (record == null || status == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            //Get persistence managed versions of all of our records
            FtuePersistedRecord persistenceManagedRecord = getPersistedRecordAttached(record, pm);

            persistenceManagedRecord.setStatus(status);

            if (status.equals(FtuePersistedRecord.FTUE_STATUS.EMAIL_SENT))
                persistenceManagedRecord.setEmailedDate(new Date());

            rval = true;

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public Boolean updateLandingIdentifier(Long record, String identifier)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (record == null || identifier == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            //Get persistence managed versions of all of our records
            FtuePersistedRecord persistenceManagedRecord = getPersistedRecordAttached(record, pm);

            persistenceManagedRecord.setLandingUuid(identifier);

            rval = true;

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public FtuePersistedRecord getFtueRecordByLandingUuid(String landingUuid, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        FtuePersistedRecord rval = null;

        try
        {
            if (landingUuid == null || mode == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            Query query = pm.newQuery(FtuePersistedRecord.class, "landingUuid.equals(:uuid)");

            configureFetchGroups(pm, mode);

            List<FtuePersistedRecord> queryList = (List<FtuePersistedRecord>) query.execute(landingUuid);

            if (queryList != null && queryList.size() > 0) {
                rval = queryList.get(0);
                rval = pm.detachCopy(rval);
            }


        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public Long getReferralCount(String referralCode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Long rval = 0l;

        try
        {
            if (referralCode == null || referralCode.length() == 0)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(FtuePersistedRecord.class, "referrerCode == :referralCode");
            query.setResult("count(this)");

            rval = (Long) query.execute(referralCode);

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public FtuePersistedRecord getPersistedRecordAttached(Long persistedRecordId, PersistenceManager pm)
    {
        FtuePersistedRecord rval = null;

        Query query = pm.newQuery(FtuePersistedRecord.class, "ftuePersistedRecordId == :persistedRecordId");

        List<FtuePersistedRecord> queryList = (List<FtuePersistedRecord>) query.execute(persistedRecordId);

        if (queryList != null && queryList.size() > 0)
            rval = queryList.get(0);

        return rval;
    }

    public FtuePersistedRecord getPersistedRecordAttached(String landingUuid, PersistenceManager pm)
    {
        FtuePersistedRecord rval = null;

        Query query = pm.newQuery(FtuePersistedRecord.class, "landingUuid.equals(:uuid)");

        List<FtuePersistedRecord> queryList = (List<FtuePersistedRecord>) query.execute(landingUuid);

        if (queryList != null && queryList.size() > 0)
            rval = queryList.get(0);

        return rval;
    }

    public List<FtuePersistedRecord> getPersistedRecordsByEmailAttached(String email, PersistenceManager pm)
    {
        List<FtuePersistedRecord> rval;

        Query query = pm.newQuery(FtuePersistedRecord.class, "request.shipping_info.email == :email");

        rval = (List<FtuePersistedRecord>)query.execute(email);

        return rval;
    }

    public List<FtuePersistedRecord> getPersistedRecordsAttached(List<Long> persistedRecordIds, PersistenceManager pm)
    {
        Query query = pm.newQuery(FtuePersistedRecord.class, ":persistedRecordIds.contains(ftuePersistedRecordId)");

        return (List<FtuePersistedRecord>) query.execute(persistedRecordIds);
    }

    public Boolean addFtueImages(Long persistedFtueRecord, List<FtueImage> images)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (persistedFtueRecord == null || images == null || images.size() == 0)
                throw new NullPointerException("Arguments cannot be null or empty");

            FtuePersistedRecord record = getPersistedRecordAttached(persistedFtueRecord, pm);
            rval = record.getFtueImages().addAll(images);
        }
        catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    public String getPersistedRecordEmailByReferralCode(String referralCode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        String rval = null;

        try
        {
            if (referralCode == null || referralCode.length() == 0)
                throw new NullPointerException("Arguments cannot be null");

            Query query = pm.newQuery(FtuePersistedRecord.class, "referralCode == :referralCode");
            query.setResult("request.shipping_info.email");

            List<String> queryList = (List<String>) query.execute(referralCode);

            if (queryList != null && queryList.size() > 0)
                rval = queryList.get(0);

        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public FtuePersistedRecord getPersistedRecordByReferralCode(String referralCode, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        FtuePersistedRecord rval = null;

        try
        {
            if (referralCode == null || referralCode.length() == 0 || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(FtuePersistedRecord.class, "referralCode == :referralCode");
            query.setOrdering("creationDate ASC");

            List<FtuePersistedRecord> queryList = (List<FtuePersistedRecord>) query.execute(referralCode);

            if (queryList != null && queryList.size() > 0)
                rval = queryList.get(0);
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public Boolean setFtueRecordModelVolume(Long persistedRecordId, Float volume)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (persistedRecordId == null || volume == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            FtuePersistedRecord record = getPersistedRecordAttached(persistedRecordId, pm);

            record.setFtueModelVolume(volume);

            rval = true;
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally {
            pm.close();
        }

        return rval;
    }

    public Boolean setFtueRecordManufacturerModelUrl(Long persistedRecordId, String modelUrl)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (persistedRecordId == null || modelUrl == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            FtuePersistedRecord record = getPersistedRecordAttached(persistedRecordId, pm);

            record.setManufacturerModelUrl(modelUrl);

            rval = true;
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally {
            pm.close();
        }

        return rval;
    }

    public Boolean checkIfLandingIdExists(String landingId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = true;

        try
        {
            if (landingId == null || landingId.length() == 0)
                throw new NullPointerException("Argument cannot be null or empty");

            Query query = pm.newQuery(FtuePersistedRecord.class, "landingUuid == :landingId");
            query.setResult("count(this)");

            Long queryResult = (Long) query.execute(landingId);

            if (queryResult == 0)
                rval = false;
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public Boolean setCheckoutPrintOrder(String orderNumber, PrintOrder order)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (orderNumber == null || order == null || orderNumber.length() == 0)
                throw new NullPointerException("Arguments cannot be null");

            FtueCheckout checkoutObject = getFtueCheckoutRecordAttached(orderNumber, pm);
            order.setCheckoutReference(checkoutObject);
            order.setOrderSystem(PrintOrder.ORDER_SYSTEM.FTUE);

            checkoutObject.setManufacturerOrder(order);

            rval = true;
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public Boolean setPersistedRecordSampleSupplierOrder(String landingUuid, PrintOrder order)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (order == null || landingUuid == null || landingUuid.length() == 0)
                throw new NullPointerException("Arguments cannot be null");

            FtuePersistedRecord persistedRecordReference = getPersistedRecordAttached(landingUuid, pm);
            order.setPersistedRecordReference(persistedRecordReference);
            order.setOrderSystem(PrintOrder.ORDER_SYSTEM.FTUE_PROTOTYPE);

            persistedRecordReference.setPrintOrder(order);

            rval = true;
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public Boolean setPersistedRecordSampleSupplierOrder(Long persistedRecordId, PrintOrder order)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (order == null || persistedRecordId == null)
                throw new NullPointerException("Arguments cannot be null");

            FtuePersistedRecord persistedRecordReference = getPersistedRecordAttached(persistedRecordId, pm);
            order.setPersistedRecordReference(persistedRecordReference);
            order.setOrderSystem(PrintOrder.ORDER_SYSTEM.FTUE_PROTOTYPE);

            persistedRecordReference.setPrintOrder(order);

            rval = true;
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    private String generateUniqueFtueCheckoutId()
    {
        String rval = UUID.randomUUID().toString().substring(0, 7).toUpperCase();

        Boolean uniquenessCheck = checkIfCheckoutIdExists(rval);

        while(uniquenessCheck)
        {
            rval = UUID.randomUUID().toString().substring(0, 7).toUpperCase();
            uniquenessCheck = checkIfCheckoutIdExists(rval);
        }

        return rval;
    }

    private String generateUniqueFtueReferralCode()
    {
        String rval = UUID.randomUUID().toString().substring(0, 7).toUpperCase();

        Boolean uniquenessCheck = checkIfFtueReferralCodeExists(rval);

        while(uniquenessCheck)
        {
            rval = UUID.randomUUID().toString().substring(0, 7).toUpperCase();
            uniquenessCheck = checkIfFtueReferralCodeExists(rval);
        }

        return rval;
    }

    private void configureFetchGroups(PersistenceManager pm, IEnums.SEEK_MODE mode)
    {
        if (mode != IEnums.SEEK_MODE.QUICK)
        {
            if (mode == IEnums.SEEK_MODE.FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(FtuePersistedRecord.class, "fullFetch");

                //Add the user fetch group fields
                List<String> fetchGroupFields = FtuePersistedRecord.getPersistedRecordFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(FtueRequest.class, "requestFetch");
                fetchGroupFields = FtueRequest.getFtueRequestFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullFetch");
                fetchPlan.addGroup("requestFetch");

                fetchPlan.setMaxFetchDepth(4);
            }
            else if (mode == IEnums.SEEK_MODE.FTUE_IMAGE_SUBMIT)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(FtuePersistedRecord.class, "renderFetch");

                //Add the user fetch group fields
                List<String> fetchGroupFields = FtuePersistedRecord.getPersistedRecordRenderFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("renderFetch");
                fetchPlan.setMaxFetchDepth(2);
            }
            else if (mode == IEnums.SEEK_MODE.FTUE_ADDRESSES)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(FtuePersistedRecord.class, "renderFetch");

                List<String> fetchGroupFields = FtuePersistedRecord.getPersistedRecordAddressFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(FtueRequest.class, "addressFetch");
                fetchGroupFields = FtueRequest.getFtueRequestShippingFetchGroupField();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("renderFetch");
                fetchPlan.addGroup("addressFetch");
                fetchPlan.setMaxFetchDepth(2);
            }
            else if (mode == IEnums.SEEK_MODE.FTUE_LANDING_ACTION)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(FtuePersistedRecord.class, "landingFetch");

                List<String> fetchGroupFields = FtuePersistedRecord.getPersistedRecordLandingFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("landingFetch");
                fetchPlan.setMaxFetchDepth(2);
            }
            else if (mode == IEnums.SEEK_MODE.FTUE_MATERIAL_PRICES)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(FtuePersistedRecord.class, "checkoutFetch");

                List<String> fetchGroupFields = FtuePersistedRecord.getPersistedRecordPricesFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("checkoutFetch");
                fetchPlan.setMaxFetchDepth(2);
            }
            else if (mode == IEnums.SEEK_MODE.FTUECHECKOUT_CART_PRICES)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(FtueCheckout.class, "checkoutFetch");

                List<String> fetchGroupFields = FtueCheckout.getFtueCheckoutPromoCodeFetchGroupFields();
                for (String field : fetchGroupFields)
                {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Address.class, "addressFetch");
                fetchGroupFields = Address.getAddressCartFetchGroupFields();
                for (String field : fetchGroupFields)
                {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("checkoutFetch");
                fetchPlan.addGroup("addressFetch");
                fetchPlan.setMaxFetchDepth(2);

            }
            else if (mode == IEnums.SEEK_MODE.FTUE_POST_PROTOTYPE_EMAIL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(FtuePersistedRecord.class, "renderFetch");

                List<String> fetchGroupFields = FtuePersistedRecord.getPersistedRecordAddressImageFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(FtueRequest.class, "addressFetch");
                fetchGroupFields = FtueRequest.getFtueRequestShippingFetchGroupField();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("renderFetch");
                fetchPlan.addGroup("addressFetch");
                fetchPlan.setMaxFetchDepth(4);
            }
            else if (mode == IEnums.SEEK_MODE.FTUECHECKOUT_FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(FtueCheckout.class, "checkoutFetch");

                List<String> fetchGroupFields = FtueCheckout.getFtueCheckoutFullFetchGroupFields();
                for (String field : fetchGroupFields)
                {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Address.class, "addressFetch");
                fetchGroupFields = Address.getAddressCartFetchGroupFields();
                for (String field : fetchGroupFields)
                {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(FtuePersistedRecord.class, "persistedRecordFetch");
                fetchGroupFields = FtuePersistedRecord.getPersistedRecordFullFetchGroupFields();
                for (String field : fetchGroupFields)
                {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("checkoutFetch");
                fetchPlan.addGroup("addressFetch");
                fetchPlan.addGroup("persistedRecordFetch");
                fetchPlan.setMaxFetchDepth(2);
            }
            else if (mode == IEnums.SEEK_MODE.FTUE_PROTOTYPE)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(FtuePersistedRecord.class, "prototypeFetch");

                List<String> fetchGroupFields = FtuePersistedRecord.getPersistedRecordPrototypeFetchGroupFields();
                for (String field : fetchGroupFields)
                {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(FtueRequest.class, "addressFetch");
                fetchGroupFields = FtueRequest.getFtueRequestShippingFetchGroupField();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("prototypeFetch");
                fetchPlan.addGroup("addressFetch");
                fetchPlan.setMaxFetchDepth(2);
            }
        }
    }

    private Boolean moneyGreaterThanZeroTest(BigDecimal moneyToTest)
    {
        if (moneyToTest.compareTo(BigDecimal.ZERO) == 0)
        {
            return false;
        }

        return true;
    }
}
