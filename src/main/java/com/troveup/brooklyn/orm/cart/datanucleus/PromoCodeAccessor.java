package com.troveup.brooklyn.orm.cart.datanucleus;

import com.troveup.brooklyn.orm.cart.interfaces.ICartAccessor;
import com.troveup.brooklyn.orm.cart.interfaces.IPromoCodeAccessor;
import com.troveup.brooklyn.orm.cart.model.Cart;
import com.troveup.brooklyn.orm.cart.model.CartItem;
import com.troveup.brooklyn.orm.cart.model.PromoCode;
import com.troveup.brooklyn.orm.common.datanucleus.ObjectAccessor;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.util.MoneyUtil;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jdo.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by tim on 6/13/15.
 */
public class PromoCodeAccessor extends ObjectAccessor implements IPromoCodeAccessor
{
    @Autowired
    IUserAccessor userAccessor;

    @Autowired
    ICartAccessor cartAccessor;

    public PromoCodeAccessor(PersistenceManagerFactory pmfProxy) {
        super(pmfProxy);
    }

    @Override
    public Boolean addPromoCode(PromoCode promoCode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (promoCode == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            //No promocode specified, assign a dynamic one.
            if (promoCode.getPromoCode() == null || promoCode.getPromoCode().length() == 0)
            {
                String possiblePromo = UUID.randomUUID().toString().substring(0, 7).toUpperCase();

                Boolean uniquenessCheck = doesPromoCodeExist(possiblePromo);

                while(uniquenessCheck)
                {
                    possiblePromo = UUID.randomUUID().toString().substring(0, 7).toUpperCase();
                    uniquenessCheck = doesPromoCodeExist(possiblePromo);
                }

                promoCode.setPromoCode(possiblePromo.toUpperCase());
            }

            //Attach a user object to the promo code
            if (promoCode.getAssociatedUser() != null && promoCode.getAssociatedUser().getUserId() != null)
            {
                promoCode.setAssociatedUser(userAccessor.getUserAttached(promoCode.getAssociatedUser().getUserId(), pm));
            }

            //Return all of the data values back on this code after we commit
            pm.setDetachAllOnCommit(true);
            rval = pm.makePersistent(promoCode) != null;

        } catch (Exception e)
        {
            logError(e);
        } finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<String> bulkAddPromoCode(PromoCode promoCode, Integer numberToGenerate)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<String> rval = new ArrayList<>();

        try
        {
            if (promoCode == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            List<String> promoCodeNumbers = bulkGenerateUniquePromoCodeNumbers(numberToGenerate, promoCode.getCharacterLength());

            List<PromoCode> bulkPromoCodesToPersist = new ArrayList<>();

            User user = null;

            //Attach a user object to the promo code
            if (promoCode.getAssociatedUser() != null && promoCode.getAssociatedUser().getUserId() != null)
            {
                user = userAccessor.getUserAttached(promoCode.getAssociatedUser().getUserId(), pm);
            }

            for (String promoCodeNumber : promoCodeNumbers)
            {
                PromoCode bulkCode = new PromoCode(promoCode);
                bulkCode.setPromoCode(promoCodeNumber);

                if (user != null)
                    bulkCode.setAssociatedUser(user);

                bulkPromoCodesToPersist.add(bulkCode);
            }

            pm.makePersistentAll(bulkPromoCodesToPersist);

            rval = promoCodeNumbers;

        } catch (Exception e)
        {
            logError(e);
        } finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<String> bulkGenerateUniquePromoCodeNumbers(Integer numberOfPromocodes, Integer lengthOfCode)
    {
        List<String> rval = new ArrayList<>();
        Integer MAX_PROMOCODE_STRING_LENGTH = lengthOfCode != null && lengthOfCode > 1 ? lengthOfCode : 7;

        for (int i = 0; i < numberOfPromocodes; ++i)
        {
            rval.add(generateRandomUppercaseString(MAX_PROMOCODE_STRING_LENGTH));
        }

        List<String> existenceCheck = bulkCheckPromoCodeExistence(rval);
        rval.removeAll(existenceCheck);

        while(existenceCheck.size() > 0)
        {
            Integer numberToRegenerate = existenceCheck.size();

            List<String> potentialReplacementCodes = new ArrayList<>();

            for (int i = 0; i < numberToRegenerate; ++i) {
                potentialReplacementCodes.add(generateRandomUppercaseString(MAX_PROMOCODE_STRING_LENGTH));
            }

            existenceCheck = bulkCheckPromoCodeExistence(potentialReplacementCodes);

            potentialReplacementCodes.removeAll(existenceCheck);
            rval.addAll(potentialReplacementCodes);
        }

        return rval;
    }

    @Override
    public List<String> bulkCheckPromoCodeExistence(List<String> promoCodesToCheck)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<String> rval = new ArrayList<>();

        try {
            if (promoCodesToCheck == null || promoCodesToCheck.size() == 0)
                throw new NullPointerException("Argument cannot be null or empty");

            Query query = pm.newQuery(PromoCode.class, ":promoCodeCheckList.contains(promoCode) && controlReference == true");
            List<PromoCode> queryResult = (List<PromoCode>) query.execute(promoCodesToCheck);

            if (queryResult != null && queryResult.size() > 0)
            {
                for (PromoCode code : queryResult)
                {
                    rval.add(code.getPromoCode());
                }
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

    @Override
    public Boolean doesPromoCodeExist(String promoCode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (promoCode == null || promoCode.length() == 0)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(PromoCode.class, "promoCode == :promoCode");
            query.setResult("count(this)");

            Long queryResult = (Long) query.execute(promoCode);

            if (queryResult > 0l)
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

    @Override
    public Boolean addPromoCodesToUsers(List<Long> userIds, List<PromoCode> promoCodes)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (userIds == null || userIds.size() == 0 || promoCodes == null || promoCodes.size() == 0)
                throw new IllegalArgumentException("Arguments cannot be null");

            //Make sure we work out the user level attachments
            //Note that this is a very expensive O(n)^2 operation
            for (Long userId : userIds)
            {
                List<PromoCode> codesToAdd = new ArrayList<>();
                User user = userAccessor.getUserAttached(userId, pm);

                for (PromoCode promoCode : promoCodes)
                {
                    PromoCode codeToAdd = new PromoCode(promoCode);
                    codeToAdd.setUserLevelAttachment(user);
                }

                user.getAvailablePromoCodes().addAll(codesToAdd);
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

    @Override
    public Boolean addPromoCodesToUser(Long userId, List<PromoCode> promoCodes)
    {
        List<Long> container = new ArrayList<>();
        container.add(userId);

        return addPromoCodesToUsers(container, promoCodes);
    }

    @Override
    public Boolean addPromoCodeToUser(Long userId, PromoCode promoCode)
    {
        List<Long> userContainer = new ArrayList<>();
        List<PromoCode> promoCodeContainer = new ArrayList<>();

        userContainer.add(userId);
        promoCodeContainer.add(promoCode);

        return addPromoCodesToUsers(userContainer, promoCodeContainer);
    }

    @Override
    public List<PromoCode> getGlobalPromoCodes()
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<PromoCode> rval = null;

        try
        {
            configureFetchGroups(pm, IEnums.SEEK_MODE.PROMO_FULL);
            Query query = pm.newQuery(PromoCode.class, "global == true");

            rval = (List<PromoCode>) query.execute();

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

    @Override
    public List<PromoCode> getGlobalActivePromoCodes()
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<PromoCode> rval = null;

        try
        {
            configureFetchGroups(pm, IEnums.SEEK_MODE.PROMO_FULL);
            Query query = pm.newQuery(PromoCode.class,
                    "global == true && active == true && (availableUses > 0 || unlimited == true) && expires > :date" +
                            "&& controlReference == true");

            rval = (List<PromoCode>) (pm.detachCopyAll((List<PromoCode>) query.execute(new Date())));

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

    @Override
    public PromoCode getPromoCode(Long promoCodeId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        PromoCode rval = null;

        try
        {
            if (promoCodeId == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            configureFetchGroups(pm, IEnums.SEEK_MODE.PROMO_FULL);
            Query query = pm.newQuery(PromoCode.class, "promoCodeId == :promoCodeId");
            query.setResultClass(PromoCode.class);

            List<PromoCode> queryList = (List<PromoCode>) query.execute(promoCodeId);

            if (queryList != null && queryList.size() > 0)
                rval = pm.detachCopy(queryList.get(0));

        }
        catch (Exception e)
        {
            logError(e);
        } finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public PromoCode getPromoCode(String promoCode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        PromoCode rval = null;

        try
        {
            if (promoCode == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            configureFetchGroups(pm, IEnums.SEEK_MODE.PROMO_FULL);
            Query query = pm.newQuery(PromoCode.class, "promoCode == :promoCode");
            query.setResultClass(PromoCode.class);

            List<PromoCode> queryList = (List<PromoCode>) query.execute(promoCode);

            if (queryList != null && queryList.size() > 0)
                rval = pm.detachCopy(queryList.get(0));

        }
        catch (Exception e)
        {
            logError(e);
        } finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<User> getUsersWithAttachedPromoCode(Long promoCodeId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<User> rval = null;

        try
        {
            if (promoCodeId == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            PromoCode searchCode = new PromoCode();
            searchCode.setPromoCodeId(promoCodeId);

            Query query = pm.newQuery(User.class, "availablePromoCodes.contains(:searchCode)");
            rval = (List<User>) query.execute(searchCode);

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

    @Override
    public List<User> getUsersWithAttachedPromoCode(String promoCode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<User> rval = null;

        try
        {
            if (promoCode == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            PromoCode searchCode = new PromoCode();
            searchCode.setPromoCode(promoCode);

            Query query = pm.newQuery(User.class, "availablePromoCodes.contains(:searchCode)");
            rval = (List<User>) query.execute(searchCode);

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

    @Override
    public List<Order> getOrdersWithAppliedPromoCode(Long promoCodeId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Order> rval = null;

        try {
            if (promoCodeId == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            PromoCode searchCode = new PromoCode();
            searchCode.setPromoCodeId(promoCodeId);

            Query query = pm.newQuery(Order.class, "availablePromoCodes.contains(:searchCode)");
            rval = (List<Order>) query.execute(searchCode);
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

    @Override
    public List<Order> getOrdersWithAppliedPromoCode(String promoCode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Order> rval = null;

        try {
            if (promoCode == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            PromoCode searchCode = new PromoCode();
            searchCode.setPromoCode(promoCode);

            Query query = pm.newQuery(Order.class, "availablePromoCodes.contains(:searchCode)");
            rval = (List<Order>) query.execute(searchCode);
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

    @Override
    public PromoCode validatePromoCode(String promoCode, Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        PromoCode rval = null;

        try
        {
            if (promoCode == null || userId == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            PromoCode compareCode = getPromoCodeAttached(promoCode, pm);

            Query query = pm.newQuery(User.class, "usedPromoCodes.contains(:promoCode)");
            query.setResult("count(this)");

            Long userCount = (Long) query.execute(compareCode);

            if (userCount.equals(0l)) {
                query = pm.newQuery(PromoCode.class, "promoCode.equals(:promoCode) && controlReference == true &&" +
                        "active == true && ((availableUses > 0 && expires > :date) || unlimited == true)");

                List<PromoCode> promoCodes = (List<PromoCode>) query.execute(promoCode, new Date());

                //Find a matching code that is either of global status or is assigned to the user
                if (promoCode != null && promoCodes.size() > 0) {
                    for (PromoCode code : promoCodes) {
                        if (code.getGlobal() ||
                                (code.getUserLevelAttachment() != null &&
                                        code.getUserLevelAttachment().getUserId().equals(userId))) {
                            rval = code;
                            break;
                        }
                    }
                }
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

    @Override
    public PromoCode validatePromoCode(String promoCode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        PromoCode rval = null;

        try {
            List<String> queryList = new ArrayList<>();
            queryList.add(promoCode);
            List<PromoCode> masterCodes = getPromoCodesByCodeAttached(queryList, true, pm);

            PromoCode searchCode = new PromoCode();
            searchCode.setPromoCode(promoCode);

            //Apparently this operation isn't supported on query results??
            //int searchIndex = masterCodes.indexOf(searchCode);

            PromoCode foundCode = null;
            for (PromoCode code : masterCodes)
            {
                if (code.getPromoCode().equals(promoCode))
                    foundCode = code;
            }


            if (foundCode != null && foundCode.isActive() && (foundCode.getUnlimited() ||
                    (foundCode.getExpires().after(new Date()) && foundCode.getAvailableUses() > 0))) {
                rval = foundCode;
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

    @Override
    public List<PromoCode> validatePromoCodes(List<String> promoCodes, Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<PromoCode> rval = null;

        try
        {
            if (promoCodes == null || userId == null || promoCodes.size() == 0)
                throw new IllegalArgumentException("Arguments cannot be null");

            List<String> allCodes = new ArrayList<>(promoCodes);

            //Codes already applied to the cart
            List<String> cartCodes = new ArrayList<>();

            //Codes not yet applied anywhere
            List<String> notAppliedPromoCodes = new ArrayList<>();

            //Codes that have been used with a previous order
            List<String> previouslyUsedCodes = new ArrayList();

            List<String> codesThatAreAttemptingToBeStacked = new ArrayList();

            User user = userAccessor.getUserAttached(userId, pm);

            //Get all of the promocodes that have been used in previous orders so that they can be discarded
            for (PromoCode code : user.getUsedPromoCodes())
            {
                if (promoCodes.contains(code.getPromoCode()))
                    previouslyUsedCodes.add(code.getPromoCode());
            }

            //Get the ones that have already been applied to the cart
            for (PromoCode promoCode : user.getShoppingCart().getAppliedPromoCodes())
            {
                cartCodes.add(promoCode.getPromoCode());
            }

            //Get the other ones that were applied to specific items
            for (CartItem item : user.getShoppingCart().getCartItems())
            {
                if (item.getAppliedPromoCode() != null)
                    cartCodes.add(item.getAppliedPromoCode().getPromoCode());
            }

            allCodes.addAll(cartCodes);

            List<PromoCode> matchingControlCodes = getPromoCodesByCodeAttached(allCodes, true, pm);

            for (int i = 0; i < matchingControlCodes.size(); ++i)
            {
                for (int j = 0; j < matchingControlCodes.size(); ++j)
                {
                    PromoCode code = matchingControlCodes.get(i);
                    PromoCode comparisonCode = matchingControlCodes.get(j);

                    if (i != j && code.getBlanketCategory().equals(comparisonCode.getBlanketCategory()) &&
                            !codesThatAreAttemptingToBeStacked.contains(code.getPromoCode()) &&
                            !codesThatAreAttemptingToBeStacked.contains(comparisonCode.getPromoCode()))
                    {
                        //If there is one in the cart already, keep it, and discard the other
                        if (cartCodes.contains(code.getPromoCode()))
                            codesThatAreAttemptingToBeStacked.add(comparisonCode.getPromoCode());
                            //Check if the inverse of the above is true
                        else if (cartCodes.contains(comparisonCode.getPromoCode()))
                            codesThatAreAttemptingToBeStacked.add(code.getPromoCode());
                            //Check which one is larger out of percentages and discard it with that logic
                        else if (code.getPercentDiscount() > 0d && comparisonCode.getPercentDiscount() > 0d
                                && code.getPercentDiscount() > comparisonCode.getPercentDiscount())
                            codesThatAreAttemptingToBeStacked.add(code.getPromoCode());
                        else if (code.getPercentDiscount() > 0d && comparisonCode.getPercentDiscount() > 0d
                                && code.getPercentDiscount() < comparisonCode.getPercentDiscount())
                            codesThatAreAttemptingToBeStacked.add(comparisonCode.getPromoCode());
                            //We could do this all day, bail out..
                        else
                            codesThatAreAttemptingToBeStacked.add(code.getPromoCode());
                    }
                }
            }

            //Match the promocodes against each other so that they don't stack by category
            /*for (PromoCode code : matchingControlCodes)
            {
                for (PromoCode comparisonCode : matchingControlCodes)
                {
                    //Make sure we're not comparing against the same promo code, then also make sure that it matches
                    //the case where the blanket category is the same
                    if (!code.getPromoCode().equals(comparisonCode.getPromoCode()) &&
                            code.getBlanketCategory().equals(comparisonCode.getBlanketCategory()))
                    {
                        //If there is one in the cart already, keep it, and discard the other
                        if (cartCodes.contains(code.getPromoCode()))
                            codesThatAreAttemptingToBeStacked.add(code.getPromoCode());
                        //Check if the inverse of the above is true
                        else if (cartCodes.contains(comparisonCode.getPromoCode()))
                            codesThatAreAttemptingToBeStacked.add(comparisonCode.getPromoCode());
                        //Check which one is larger out of percentages and discard it with that logic
                        else if (code.getPercentDiscount() > 0d && comparisonCode.getPercentDiscount() > 0d
                                && code.getPercentDiscount() > comparisonCode.getPercentDiscount())
                            codesThatAreAttemptingToBeStacked.add(code.getPromoCode());
                        else if (code.getPercentDiscount() > 0d && comparisonCode.getPercentDiscount() > 0d
                                && code.getPercentDiscount() < comparisonCode.getPercentDiscount())
                            codesThatAreAttemptingToBeStacked.add(comparisonCode.getPromoCode());
                        //We could do this all day, bail out..
                        else
                            codesThatAreAttemptingToBeStacked.add(code.getPromoCode());
                    }
                }
            }*/

            //Anything not falling into the above two categories are good codes that may be applied
            for (String promoCode : promoCodes)
            {
                if (!cartCodes.contains(promoCode) && !previouslyUsedCodes.contains(promoCode) &&
                        !codesThatAreAttemptingToBeStacked.contains(promoCode))
                    notAppliedPromoCodes.add(promoCode);
            }


            Query query = pm.newQuery(PromoCode.class, ":promoCodes.contains(promoCode) && controlReference == true && " +
                    "active == true && ((availableUses > 0 && (!doesExpire || expires > :date)) || unlimited == true)");

            List<PromoCode> queryList = (List<PromoCode>) query.execute(notAppliedPromoCodes, new Date());

            //Sort the different codes into a map of lists, as there could be duplicate codes
            if (queryList != null && queryList.size() > 0)
            {
                HashMap<String, List<PromoCode>> map = new HashMap<>();

                for (PromoCode code : queryList)
                {
                    if (map.containsKey(code.getPromoCode()))
                        map.get(code.getPromoCode()).add(code);
                    else
                    {
                        List<PromoCode> codes = new ArrayList<>();
                        codes.add(code);

                        map.put(code.getPromoCode(), codes);
                    }
                }

                //Find a matching code that is either of global status or is assigned to the user
                rval = new ArrayList<>();

                for(String code : map.keySet())
                {
                    List<PromoCode> codeSet = map.get(code);

                    for (PromoCode validCode : codeSet)
                    {

                        if (validCode.getGlobal() ||
                                (validCode.getUserLevelAttachment() != null &&
                                        validCode.getUserLevelAttachment().getUserId().equals(userId))) {
                            rval.add(validCode);
                            break;
                        }
                    }
                }
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

    @Override
    public Boolean deactivatePromoCode(Long promoCodeId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        PromoCode promoCode = getPromoCodeAttached(promoCodeId, pm);

        promoCode.setActive(false);

        return true;

    }

    @Override
    public Boolean consumePromoCode(String promoCodeId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (promoCodeId == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            List<String> promoCodeContainer = new ArrayList<>();
            promoCodeContainer.add(promoCodeId);
            List<PromoCode> promoCode = getPromoCodesByCodeAttached(promoCodeContainer, true, pm);

            PromoCode foundCode = null;
            for (PromoCode code : promoCode)
            {
                if (code.getPromoCode().equals(promoCodeId))
                    foundCode = code;
            }

            if (foundCode != null && !foundCode.getUnlimited()) {
                foundCode.setAvailableUses(foundCode.getAvailableUses() - 1);
            }

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

    @Override
    public Boolean consumePromoCodes(List<Long> promoCodeIds)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (promoCodeIds == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            Query query = pm.newQuery(PromoCode.class, ":promoCodeIds.contains(promoCodeId)");

            List<PromoCode> attachedPromoCodes = (List<PromoCode>) query.execute(promoCodeIds);

            if (attachedPromoCodes != null)
            {
                for (PromoCode code : attachedPromoCodes)
                {
                    if (!code.getUnlimited()) {
                        code.setAvailableUses(code.getAvailableUses() - 1);
                        rval = true;
                    }
                }
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

    @Override
    public Boolean consumePromoCodes(Long cartId, Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartId == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            Cart cart = cartAccessor.getShoppingCartAttached(cartId, pm);
            User user = userAccessor.getUserAttached(userId, pm);

            List<String> promoCodeIds = new ArrayList<>();

            if (cart.getAppliedPromoCodes() != null && cart.getAppliedPromoCodes().size() > 0) {
                for (PromoCode code : cart.getAppliedPromoCodes()) {
                    promoCodeIds.add(code.getConsumedCode().getPromoCode());
                    user.getUsedPromoCodes().add(code);
                }
            }

            for (CartItem item :cart.getCartItems())
            {
                if (item.getAppliedPromoCode() != null) {
                    promoCodeIds.add(item.getAppliedPromoCode().getPromoCode());
                    user.getUsedPromoCodes().add(item.getAppliedPromoCode());
                }
            }

            if (promoCodeIds.size() > 0)
            {
                Query query = pm.newQuery(PromoCode.class, ":promoCodeIds.contains(promoCode) && controlReference == true");

                List<PromoCode> attachedPromoCodes = (List<PromoCode>) query.execute(promoCodeIds);


                if (attachedPromoCodes != null)
                {
                    for (PromoCode code : attachedPromoCodes)
                    {
                        if (code.getUnlimited() == null || !code.getUnlimited()) {
                            code.setAvailableUses(code.getAvailableUses() - 1);
                        }
                    }
                }
            }

            rval = true;

        } catch (Exception e)
        {
            logError(e);
        }
        finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Cart applyPromoCodesToCart(List<Long> controlCodeIds, Long cartId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Cart rval = null;
        CartAccessor.configureFetchGroups(pm, IEnums.SEEK_MODE.CART_PROMO);

        try
        {
            if (controlCodeIds == null && cartId == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            Cart cart = cartAccessor.getShoppingCartAttached(cartId, pm);
            String currencyUnit = cart.getCurrencyUnit();

            List<PromoCode> promoCodeObjects = getPromoCodesByIdAttached(controlCodeIds, pm);

            List<PromoCode> promoCodePercentages = new ArrayList<>();
            List<PromoCode> promoCodeDollars = new ArrayList<>();

            //Separate percent from flat dollar codes so that they can be sorted in descending order
            for (PromoCode code : promoCodeObjects)
            {
                if (code.getDollarDiscount() != null)
                    promoCodeDollars.add(code);
                else
                    promoCodePercentages.add(code);
            }

            Collections.sort(promoCodePercentages, new Comparator<PromoCode>() {
                @Override
                public int compare(PromoCode o1, PromoCode o2) {
                    if (o1.getPercentDiscount() == o2.getPercentDiscount())
                        return 0;
                    else if (o1.getPercentDiscount() > o2.getPercentDiscount())
                        return 1;
                    else
                        return -1;
                }
            });

            Collections.sort(promoCodeDollars, new Comparator<PromoCode>() {
                @Override
                public int compare(PromoCode o1, PromoCode o2)
                {
                    return o1.getDollarDiscount().compareTo(o2.getDollarDiscount());
                }
            });

            promoCodeObjects = new ArrayList<>();
            promoCodeObjects.addAll(promoCodePercentages);
            promoCodeObjects.addAll(promoCodeDollars);

            List<PromoCode> blanketPromoCodes = new ArrayList<>();
            List<PromoCode> itemLevelPromoCodes = new ArrayList<>();

            for (PromoCode code : promoCodeObjects)
            {
                if (code.getBlanketApplication())
                    blanketPromoCodes.add(code);
                else
                    itemLevelPromoCodes.add(code);
            }

            HashMap<String, List<CartItem>> categoryMap = null;

            List<CartItem> cartItemList = cart.getCartItems();

            //Sort CartItems according to price
            if (itemLevelPromoCodes.size() > 0)
            {
                Collections.sort(cartItemList, new Comparator<CartItem>()
                {
                    @Override
                    public int compare(CartItem o1, CartItem o2)
                    {
                        return o1.getActualPrice().compareTo(o2.getActualPrice());
                    }
                });
            }

            for (PromoCode code : itemLevelPromoCodes)
            {
                if (code.getItemCategory() != null)
                {
                    //Make sure that the category map doesn't get replenished every time the outer for loop runs
                    if (categoryMap == null) {
                        categoryMap = new HashMap<>();
                        //Sort the items into categories
                        for (CartItem cartItem : cart.getCartItems()) {
                            if (categoryMap.containsKey(cartItem.getCartItemReference().getCategory()))
                                categoryMap.get(cartItem.getCartItemReference().getCategory()).add(cartItem);
                            else {
                                List<CartItem> categoryList = new ArrayList<>();
                                categoryList.add(cartItem);
                                categoryMap.put(cartItem.getCartItemReference().getBaseItemReference().getCategory(),
                                        categoryList);
                            }
                        }

                        //Now sort the lists in a ascending pattern by category
                        for (String category : categoryMap.keySet()) {
                            Collections.sort(categoryMap.get(category), new Comparator<CartItem>() {
                                @Override
                                public int compare(CartItem o1, CartItem o2) {
                                    /*if (o2.getActualPrice().isEqual(o1.getActualPrice())) {
                                        return 0;
                                    } else if (o2.getActualPrice().isGreaterThan(o1.getActualPrice())) {
                                        return 1;
                                    } else {
                                        return -1;
                                    }*/

                                    return o2.getActualPrice().compareTo(o1.getActualPrice());
                                }
                            });
                        }
                    }

                    //Promo code match for cart item category, find out where we can apply it
                    if (categoryMap.containsKey(code.getItemCategory()))
                    {
                        List<CartItem> mappedList = categoryMap.get(code.getItemCategory());

                        //Go through all of the items in that category, cheapest to most expensive, and see if we can
                        //match one with the same material ID and finish ID
                        for (CartItem cartItem : mappedList)
                        {
                            if (code.getMaterialId().equals(cartItem.getMaterialId())
                                    && code.getFinishId().equals(cartItem.getFinishId())
                                    && cartItem.getAppliedPromoCode() == null)
                            {
                                applyPromoCodeToCartItem(code, cartItem, currencyUnit);
                                break;
                            }
                        }

                    }
                }
                //Both material and finish were specified, apply the code accordingly
                else if (code.getMaterialId() != null && code.getFinishId() != null)
                {
                    for (CartItem item : cartItemList)
                    {
                        if (item.getAppliedPromoCode() == null && item.getMaterialId().equals(code.getMaterialId()) &&
                                item.getFinishId().equals(code.getFinishId()))
                        {
                            applyPromoCodeToCartItem(code, item, currencyUnit);
                            break;
                        }
                    }
                }
                //Failing both being specified, handle the more general case of only the material having been specified
                else if (code.getMaterialId() != null)
                {
                    for (CartItem item : cartItemList)
                    {
                        if (item.getAppliedPromoCode() == null && item.getMaterialId().equals(code.getMaterialId()))
                        {
                            applyPromoCodeToCartItem(code, item, currencyUnit);
                            break;
                        }
                    }
                }
                //If the material was supplied but the finish wasn't, apply to the first item
                else if (code.getFinishId() != null)
                {
                    //Go through all of the items in that category, cheapest to most expensive, and see if we can
                    //match one with the same material ID and finish ID
                    for (CartItem cartItem : cartItemList) {
                        if (cartItem.getAppliedPromoCode() == null && cartItem.getFinishId().equals(code.getFinishId()))
                        {
                            applyPromoCodeToCartItem(code, cartItem, currencyUnit);
                            break;
                        }
                    }
                }
                //No category was supplied, this means we need to apply this to the cheapest of the cart items
                else
                {
                    //Find the first of the items that this can be applied to, and apply it.
                    for (CartItem item : cartItemList)
                    {
                        if (item.getAppliedPromoCode() == null) {
                            applyPromoCodeToCartItem(code, item, currencyUnit);
                            break;
                        }
                    }
                }

                recalculateSubTotal(cart);
            }

            for (PromoCode code : blanketPromoCodes)
            {
                PromoCode appliedCode = PromoCode.getAssociativeCode(code);
                //Check if this is a shipping code
                if (code.getBlanketCategory().equals(PromoCode.DISCOUNT_CATEGORY.SHIPPING.toString()) &&
                        moneyGreaterThanZeroTest(cart.getShipping()))
                {
                    Money shipping = MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(),cart.getShipping());

                    //If it is, and there's a percent discount to subtract, subtract the percentage
                    if (code.getPercentDiscount() > 0)
                    {
                        Money priceDelta = shipping.multipliedBy(code.getPercentDiscount() / 100,
                                RoundingMode.UP);
                        shipping = shipping.minus(priceDelta);
                        appliedCode.setPriceDelta(priceDelta.getAmount());
                    }
                    //Otherwise, subtract the flat dollar amount
                    else if (code.getDollarDiscount() != null &&
                            moneyGreaterThanZeroTest(code.getDollarDiscount()))
                    {

                        //Account for a flat promo reduction that might push it negative
                        if (shipping.isGreaterThan(MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(),
                                code.getDollarDiscount())))
                        {
                            shipping = shipping.minus(code.getDollarDiscount());
                            appliedCode.setPriceDelta(code.getDollarDiscount());
                        }
                        else
                        {
                            appliedCode.setPriceDelta(shipping.getAmount());
                            shipping = Money.parse(currencyUnit + " 0.00");
                        }
                    }

                    //Set the new dollar amount
                    cart.setShipping(shipping.getAmount());
                }
                //Check if this is a subtotal code
                else if (code.getBlanketCategory().equals(PromoCode.DISCOUNT_CATEGORY.SUB_TOTAL.toString()) &&
                        moneyGreaterThanZeroTest(cart.getSubTotal()))
                {
                    Money subtotal = MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(), cart.getSubTotal());

                    //If it is, and there's a percent discount to subtract, subtract the percentage
                    if (code.getPercentDiscount() > 0)
                    {
                        Money priceDelta = subtotal.multipliedBy(code.getPercentDiscount() / 100,
                                RoundingMode.UP);
                        subtotal = subtotal.minus(priceDelta);
                        appliedCode.setPriceDelta(priceDelta.getAmount());
                    }
                    //Otherwise, subtract the flat dollar amount
                    else if (code.getDollarDiscount() != null &&
                            moneyGreaterThanZeroTest(code.getDollarDiscount()))
                    {
                        //Account for a flat promo reduction that might push it negative
                        if (subtotal.isGreaterThan(MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(),
                                code.getDollarDiscount())))
                        {
                            subtotal = subtotal.minus(code.getDollarDiscount());
                            appliedCode.setPriceDelta(code.getDollarDiscount());
                        }
                        else
                        {
                            appliedCode.setPriceDelta(subtotal.getAmount());
                            subtotal = Money.parse(currencyUnit + " 0.00");
                        }
                    }

                    cart.setSubTotal(subtotal.getAmount());
                }
                else if (code.getBlanketCategory().equals(PromoCode.DISCOUNT_CATEGORY.TAX.toString()) &&
                        moneyGreaterThanZeroTest(cart.getTax())) {
                    Money tax = MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(), cart.getTax());

                    //If it is, and there's a percent discount to subtract, subtract the percentage
                    if (code.getPercentDiscount() > 0)
                    {
                        Money priceDelta = tax.multipliedBy(code.getPercentDiscount() / 100,
                                RoundingMode.UP);
                        tax = tax.minus(priceDelta);
                        appliedCode.setPriceDelta(priceDelta.getAmount());
                    }
                    //Otherwise, subtract the flat dollar amount
                    else if (code.getDollarDiscount() != null &&
                            moneyGreaterThanZeroTest(code.getDollarDiscount()))
                    {
                        //Account for a flat promo reduction that might push it negative
                        if (tax.isGreaterThan(MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(),
                                code.getDollarDiscount())))
                        {
                            tax = tax.minus(code.getDollarDiscount());
                            appliedCode.setPriceDelta(code.getDollarDiscount());
                        }
                        else
                        {
                            appliedCode.setPriceDelta(tax.getAmount());
                            tax = Money.parse(currencyUnit + " 0.00");
                        }
                    }

                    cart.setTax(tax.getAmount());
                }

                //Set the applied code in the cart
                cart.getAppliedPromoCodes().add(appliedCode);
            }

            recalculateCartTotal(cart);

            rval = pm.detachCopy(cart);

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

    @Override
    public Cart removePromoCodesFromCart(List<String> promoCodes, Long cartId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        CartAccessor.configureFetchGroups(pm, IEnums.SEEK_MODE.CART_PROMO);
        Cart rval = null;
        try
        {
            if (promoCodes == null && cartId == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            rval = cartAccessor.getShoppingCartAttached(cartId, pm);

            //Sort these things so that we stay in the O(n) realm, unlike the above method's atrocity.
            //Handle the broad promo codes first
            HashMap<String, Integer> broadPromoMap = new HashMap<>();
            List<Integer> broadPromosToRemove = new ArrayList<>();

            if (rval.getAppliedPromoCodes() != null) {
                for (int i = 0; i < rval.getAppliedPromoCodes().size(); ++i)
                    broadPromoMap.put(rval.getAppliedPromoCodes().get(i).getPromoCode(), i);

                for (String promoCode : promoCodes) {
                    if (broadPromoMap.containsKey(promoCode))
                        broadPromosToRemove.add(broadPromoMap.get(promoCode));
                }

                for (Integer broadPromoIndexToRemove : broadPromosToRemove) {
                    removeBroadPromoCode(broadPromoIndexToRemove, rval);
                }
            }

            //Next, handle the cart item specific promo codes
            HashMap<String, CartItem> specificItemPromoMap = new HashMap<>();

            if (rval.getCartItems() != null) {

                for (CartItem cartItem : rval.getCartItems()) {
                    if (cartItem.getAppliedPromoCode() != null) {
                        specificItemPromoMap.put(cartItem.getAppliedPromoCode().getPromoCode(), cartItem);
                    }
                }

                for (String promoCode : specificItemPromoMap.keySet()) {
                    removePromoCodeFromCartItem(specificItemPromoMap.get(promoCode), rval.getCurrencyUnit());
                }
            }

            rval = pm.detachCopy(recalculateCartTotal(rval));

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

    @Override
    public Boolean removeAllPromoCodesFromCart(Long cartId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartId == null)
                throw new NullPointerException("Argument cannot be null");

            Cart cart = cartAccessor.getShoppingCartAttached(cartId, pm);

            cart.setAppliedPromoCodes(new ArrayList<PromoCode>());

            rval = true;
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

    public static void getManagedPromoCodes(List<PromoCode> promoCodes, PersistenceManager pm,
                                            IPromoCodeAccessor promoCodeAccessor)
    {
        if (promoCodes != null && promoCodes.size() > 0)
        {
            List<PromoCode> managedList = new ArrayList<>();
            for (int i = 0; i < promoCodes.size(); ++i)
            {
                if (promoCodes.get(i).getPromoCodeId() != null)
                {
                    managedList.add(promoCodeAccessor.getPromoCodeAttached(promoCodes.get(i).getPromoCodeId(), pm));
                }

                promoCodes = managedList;
            }
        }
    }

    @Override
    public Boolean updatePromoCode(PromoCode promoCode)
    {
        Boolean rval = false;
        PersistenceManager pm = pmf.getPersistenceManager();

        try
        {
            if (promoCode == null)
                throw new IllegalArgumentException("Arguments cannot be null.");
            if (promoCode.getPromoCodeId() == null)
                throw new NullPointerException("PromoCode's ID cannot be null on update!");

            PromoCode persistedPromoCode = getPromoCodeAttached(promoCode.getPromoCodeId(), pm);

            persistedPromoCode.setActive(promoCode.isActive());
            persistedPromoCode.setPromoCode(promoCode.getPromoCode());
            persistedPromoCode.setAvailableUses(promoCode.getAvailableUses());
            persistedPromoCode.setPercentDiscount(promoCode.getPercentDiscount());
            persistedPromoCode.setDollarDiscount(promoCode.getDollarDiscount());
            persistedPromoCode.setBlanketCategory(promoCode.getBlanketCategory());

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

    @Override
    public PromoCode getPromoCodeAttached(Long promoCodeId, PersistenceManager pm)
    {
        PromoCode rval = null;

        if (promoCodeId == null || pm == null)
            throw new IllegalArgumentException("Arguments cannot be null");

        Query query = pm.newQuery(PromoCode.class, "promoCodeId == :promoCodeId");
        query.setResultClass(PromoCode.class);
        List<PromoCode> queryResults = (List<PromoCode>) query.execute(promoCodeId);

        if (queryResults != null && queryResults.size() > 0)
            rval = queryResults.get(0);

        return rval;
    }

    public List<PromoCode> getPromoCodesByIdAttached(List<Long> promoCodeIds, PersistenceManager pm)
    {
        Query query = pm.newQuery(PromoCode.class, ":promoCodeIds.contains(promoCodeId)");
        query.setResultClass(PromoCode.class);
        return (List<PromoCode>) query.execute(promoCodeIds);
    }

    public List<PromoCode> getPromoCodesByCodeAttached(List<String> promoCodes, Boolean controlCodeReference, PersistenceManager pm)
    {
        Query query = pm.newQuery(PromoCode.class, ":promoCodes.contains(promoCode) && controlReference == :controlReference");
        query.setResultClass(PromoCode.class);
        return (List<PromoCode>) query.execute(promoCodes, controlCodeReference);
    }

    public PromoCode getPromoCodeAttached(String promoCodeId, PersistenceManager pm)
    {
        PromoCode rval = null;

        if (promoCodeId == null || pm == null)
            throw new IllegalArgumentException("Arguments cannot be null");

        Query query = pm.newQuery(PromoCode.class, "promoCode.equals(:promoCodeId)");
        query.setResultClass(PromoCode.class);
        List<PromoCode> queryResults = (List<PromoCode>) query.execute(promoCodeId);

        if (queryResults != null && queryResults.size() > 0)
            rval = queryResults.get(0);

        return rval;
    }

    public static PromoCode getManagedPromoCode(PromoCode promoCode, PersistenceManager pm,
                                                IPromoCodeAccessor promoCodeAccessor)
    {
        if (promoCode != null)
        {
            if (promoCode.getPromoCodeId() != null)
                return promoCodeAccessor.getPromoCodeAttached(promoCode.getPromoCodeId(), pm);
        }

        return promoCode;
    }

    private void configureFetchGroups(PersistenceManager pm, IEnums.SEEK_MODE mode)
    {
        if (mode != IEnums.SEEK_MODE.QUICK)
        {
            if (mode == IEnums.SEEK_MODE.PROMO_FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(PromoCode.class, "fullPromoCode");

                List<String> fetchGroupFields = PromoCode.getPromoCodeFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullPromoCode");
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

    public Cart recalculateCartTotal(Cart cart)
    {
        Money grandTotal = Money.parse(cart.getCurrencyUnit() + " 0.00");
        //cart = recalculateSubTotal(cart);

        grandTotal = grandTotal.plus(cart.getTax());
        grandTotal = grandTotal.plus(cart.getShipping());
        grandTotal = grandTotal.plus(cart.getSubTotal());

        cart.setGrandTotal(grandTotal.getAmount());

        return cart;

    }

    private Cart recalculateSubTotal(Cart cart)
    {
        Money subTotal = Money.parse(cart.getCurrencyUnit() + " 0.00");
        for (CartItem cartItem : cart.getCartItems())
        {
            subTotal = subTotal.plus(cartItem.getActualPrice());
        }

        cart.setSubTotal(subTotal.getAmount());

        return cart;
    }

    private void applyPromoCodeToCartItem(PromoCode code, CartItem item, String currencyUnit)
    {
        Money price = MoneyUtil.bigDecimalToMoney(currencyUnit, item.getActualPrice());

        PromoCode appliedCode = PromoCode.getAssociativeCode(code);

        Money priceDelta;

        //If it is, and there's a percent discount to subtract, subtract the percentage
        if (code.getPercentDiscount() > 0)
        {
            priceDelta = price.multipliedBy(code.getPercentDiscount() / 100,
                    RoundingMode.UP);
            price = price.minus(priceDelta);
            appliedCode.setPriceDelta(priceDelta.getAmount());
        }
        //Otherwise, subtract the flat dollar amount
        else if (code.getDollarDiscount() != null &&
                moneyGreaterThanZeroTest(code.getDollarDiscount()))
        {
            //Account for a flat promo reduction that might push it negative
            if (price.isGreaterThan(MoneyUtil.bigDecimalToMoney(currencyUnit, code.getDollarDiscount())))
            {
                priceDelta = MoneyUtil.bigDecimalToMoney(null, code.getDollarDiscount());
                price = price.minus(priceDelta);
                appliedCode.setPriceDelta(priceDelta.getAmount());
            }
            else
            {
                appliedCode.setPriceDelta(price.getAmount());
                price = Money.parse(currencyUnit + " 0.00");
            }
        }

        item.setActualPrice(price.getAmount());

        item.setAppliedPromoCode(appliedCode);
    }

    private void removePromoCodeFromCartItem(CartItem item, String currencyUnit)
    {
        Money price = MoneyUtil.bigDecimalToMoney(currencyUnit, item.getActualPrice());

        price = price.plus(item.getAppliedPromoCode().getPriceDelta());

        item.setActualPrice(price.getAmount());

        item.setAppliedPromoCode(null);
    }

    private void removeBroadPromoCode(Integer index, Cart cart)
    {
        PromoCode codeToRemove = cart.getAppliedPromoCodes().get(index);

        if(codeToRemove.getBlanketCategory().equals(PromoCode.DISCOUNT_CATEGORY.TAX.toString()))
        {
            Money tax = MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(), cart.getTax());
            tax = tax.plus(MoneyUtil.toProperScale(codeToRemove.getPriceDelta(), null));
            cart.setTax(tax.getAmount());
        }
        else if (codeToRemove.getBlanketCategory().equals(PromoCode.DISCOUNT_CATEGORY.SUB_TOTAL.toString()))
        {
            Money subTotal = MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(), cart.getSubTotal());
            subTotal = subTotal.plus(MoneyUtil.toProperScale(codeToRemove.getPriceDelta(), null));
            cart.setSubTotal(subTotal.getAmount());
        }
        else if (codeToRemove.getBlanketCategory().equals(PromoCode.DISCOUNT_CATEGORY.SHIPPING.toString()))
        {
            Money shipping = MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(), cart.getShipping());
            shipping = shipping.plus(MoneyUtil.toProperScale(codeToRemove.getPriceDelta(), null));
            cart.setShipping(shipping.getAmount());
        }
        else if (codeToRemove.getBlanketCategory().equals(PromoCode.DISCOUNT_CATEGORY.GRAND_TOTAL.toString()))
        {
            Money grandTotal = MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(), cart.getGrandTotal());
            grandTotal = grandTotal.plus(MoneyUtil.toProperScale(codeToRemove.getPriceDelta(), null));
            cart.setGrandTotal(grandTotal.getAmount());
        }

        //This... doesn't work.  Datanucleus.  Fuck.  Why?
        //cart.getAppliedPromoCodes().remove(index);

        List<PromoCode> originalCartCodes = new ArrayList<>();

        for (PromoCode code : cart.getAppliedPromoCodes())
        {
            if (!code.getPromoCode().equals(codeToRemove.getPromoCode()))
                originalCartCodes.add(code);
        }

        cart.setAppliedPromoCodes(originalCartCodes);
    }

    private String generateRandomUppercaseString(Integer stringLength)
    {
        return UUID.randomUUID().toString().substring(0, stringLength).toUpperCase().replace("-", "");
    }
}
