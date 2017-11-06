package com.troveup.brooklyn.sdk.payment.braintree.exception;

/**
 * Created by tim on 10/27/15.
 */
public class BraintreeCustomerCreationException extends RuntimeException
{
    public BraintreeCustomerCreationException()
    {

    }

    public BraintreeCustomerCreationException(String message)
    {
        super(message);
    }
}
