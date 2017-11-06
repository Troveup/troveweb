package com.troveup.brooklyn.sdk.payment.braintree.api;

import com.braintreegateway.*;
import com.troveup.brooklyn.orm.cart.model.Cart;
import com.troveup.brooklyn.orm.cart.model.PaymentDetails;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.payment.braintree.exception.BraintreeCustomerCreationException;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by tim on 5/28/15.
 */
public class BraintreeApi extends BraintreeCommonApi
{
    public BraintreeApi(BraintreePaymentFactory braintreePaymentFactory)
    {
        super(braintreePaymentFactory);
    }

    public String getClientToken()
    {
        BraintreeGateway gateway = braintreePaymentFactory.getBraintreeGatewayInstance();

        String clientToken = gateway.clientToken().generate();

        return clientToken;
    }

    public Boolean persistPaymentNonce(Long trovePersistentUserId, String paymentNonce)
    {
        Boolean rval = false;
        try {
            User user = userAccessor.getUser(trovePersistentUserId, IEnums.SEEK_MODE.USER_CART_QUICK);
            if (user == null)
                throw new NullPointerException("Cannot persist a payment nonce on a null user.");

            //TODO:  Handle scenario where the shopping cart is null.  Shouldn't ever get there, but it's a possiblity.
            Long cartId = user.getShoppingCart().getCartId();

            PaymentDetails details = new PaymentDetails();
            details.setPaymentToken(paymentNonce);
            details.setCreated(new Date());

            if (!cartAccessor.addPaymentDetails(cartId, details))
                throw new NullPointerException("Failed to persist payment details userId " + trovePersistentUserId);

            rval = true;
        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            return rval;
        }
    }

    public Result<Transaction> processTransaction(Long trovePersistentUserId)
    {
        Result<Transaction> rval = null;

        try {
            BraintreeGateway gateway = braintreePaymentFactory.getBraintreeGatewayInstance();
            User user = userAccessor.getUser(trovePersistentUserId, IEnums.SEEK_MODE.USER_CART_PAYMENT_DETAILS);

            if (user == null)
                throw new NullPointerException("Cannot process a payment on a null user.");

            //TODO:  Handle scenario where the shopping cart is null.  Shouldn't ever get there, but it's a possiblity.
            Cart cart = user.getShoppingCart();

            PaymentDetails details = cart.getPaymentDetails();

            if (details == null)
                throw new NullPointerException("Cannot process a payment on a cart with null payment details");

            if (details.getPaymentToken() == null)
                throw new NullPointerException("Cannot process a payment on a cart with null payment token");

            TransactionRequest request = new TransactionRequest()
                    .amount(new BigDecimal(cart.getGrandTotal().toString()))
                    .paymentMethodNonce(cart.getPaymentDetails().getPaymentToken())
                    .options()
                        .submitForSettlement(false)
                    .done();

            rval = gateway.transaction().sale(request);
        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            return rval;
        }
    }

    public Result<Transaction> processTransaction(BigDecimal grandTotal, String paymentToken)
    {
        Result<Transaction> rval = null;

        try {
            BraintreeGateway gateway = braintreePaymentFactory.getBraintreeGatewayInstance();

            if (grandTotal == null)
                throw new NullPointerException("Cannot process a payment on a cart with null payment details");

            if (paymentToken == null || paymentToken.length() == 0)
                throw new NullPointerException("Cannot process a payment on a cart with null payment token");

            TransactionRequest request = new TransactionRequest()
                    .amount(grandTotal)
                    .paymentMethodNonce(paymentToken)
                    .options()
                    .submitForSettlement(true)
                    .done();

            rval = gateway.transaction().sale(request);
        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            return rval;
        }
    }

    public Result<Transaction> processTransaction(BigDecimal grandTotal, String paymentToken, Boolean shouldSubmitForSettlement)
    {
        Result<Transaction> rval = null;

        try {
            BraintreeGateway gateway = braintreePaymentFactory.getBraintreeGatewayInstance();

            if (grandTotal == null)
                throw new NullPointerException("Cannot process a payment on a cart with null payment details");

            if (paymentToken == null || paymentToken.length() == 0)
                throw new NullPointerException("Cannot process a payment on a cart with null payment token");

            TransactionRequest request = new TransactionRequest()
                    .amount(grandTotal)
                    .paymentMethodNonce(paymentToken)
                    .options()
                    .submitForSettlement(shouldSubmitForSettlement)
                    .done();

            rval = gateway.transaction().sale(request);
        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            return rval;
        }
    }

    public Result<Transaction> settleTransaction(String transactionId)
    {
        BraintreeGateway gateway = braintreePaymentFactory.getBraintreeGatewayInstance();
        return gateway.transaction().submitForSettlement(transactionId);
    }

    public Result<Transaction> settleTransaction(String transactionId, BigDecimal amount)
    {
        BraintreeGateway gateway = braintreePaymentFactory.getBraintreeGatewayInstance();
        return gateway.transaction().submitForSettlement(transactionId, amount);
    }

    /**
     * Use this if the transaction has already settled
     * @param transactionId The identifier for the settled transaction.
     * @return The result of trying to refund the transaction.
     */
    public Result<Transaction> refundTransaction(String transactionId)
    {
        BraintreeGateway gateway = braintreePaymentFactory.getBraintreeGatewayInstance();
        return gateway.transaction().refund(transactionId);
    }

    public Result<Transaction> settleTransactionFromVault(String paymentToken, BigDecimal amount)
    {
        BraintreeGateway gateway = braintreePaymentFactory.getBraintreeGatewayInstance();

        TransactionRequest request = new TransactionRequest()
                .amount(amount)
                .paymentMethodToken(paymentToken)
                .options()
                    .submitForSettlement(true)
                    .done();

        return gateway.transaction().sale(request);
    }

    /**
     * Voids a transaction that has not yet been settled.
     * @param transactionId The identifier for the settled transaction.
     * @return The result of trying to void the transaction.
     */
    public Result<Transaction> voidTransaction(String transactionId)
    {
        BraintreeGateway gateway = braintreePaymentFactory.getBraintreeGatewayInstance();
        return gateway.transaction().voidTransaction(transactionId);
    }

    public Transaction getTransactionStatus(String transactionId)
    {
        BraintreeGateway gateway = braintreePaymentFactory.getBraintreeGatewayInstance();
        return gateway.transaction().find(transactionId);
    }

    public Result<Customer> createVaultCustomer(User user) throws BraintreeCustomerCreationException
    {
        Result<Customer> rval = null;

        if (user.getBraintreeUserId() == null || user.getBraintreeUserId().length() == 0) {
            BraintreeGateway gateway = braintreePaymentFactory.getBraintreeGatewayInstance();

            String[] nameSplit = user.getFirstName().split(" ");

            CustomerRequest request = new CustomerRequest();
            request.firstName(nameSplit[0]);

            if (nameSplit.length > 1)
                request.lastName(nameSplit[1]);

            request.customField("username", user.getUsername());
            request.customField("userid", user.getUserId().toString());

            rval = gateway.customer().create(request);

            if (rval.isSuccess())
            {
                userAccessor.updateUserBraintreeVaultId(user.getUserId(), rval.getTarget().getId());
            }
            else {

                String exceptionString = "Customer creation error! " +
                        "Attempted to create customer from user " + user.getUserId() + " and received error ";

                if (rval.getErrors().getAllValidationErrors() != null && rval.getErrors().getAllValidationErrors().size() > 0)
                    exceptionString += rval.getErrors().getAllValidationErrors().get(0).getMessage();
                else if (rval.getErrors().getAllDeepValidationErrors() != null && rval.getErrors().getAllDeepValidationErrors().size() > 0)
                    exceptionString += rval.getErrors().getAllDeepValidationErrors().get(0).getMessage();
                else
                    exceptionString += "unknown.";

                throw new BraintreeCustomerCreationException(exceptionString);
            }

        }

        return rval;
    }

    public Result<PaymentMethod> addPaymentMethodToVaultCustomer(String userVaultId, String paymentNonce)
    {
        BraintreeGateway gateway = braintreePaymentFactory.getBraintreeGatewayInstance();

        PaymentMethodRequest request = new PaymentMethodRequest()
                .customerId(userVaultId)
                .paymentMethodNonce(paymentNonce)
                .options()
                    .verifyCard(true)
                .done();

        return (Result<PaymentMethod>) gateway.paymentMethod().create(request);
    }
}
