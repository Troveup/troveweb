package com.troveup.brooklyn.sdk.payment.braintree.interfaces;

import com.braintreegateway.BraintreeGateway;

/**
 * Created by tim on 6/10/15.
 */
public interface IPaymentFactory
{
    BraintreeGateway getBraintreeGatewayInstance();
}
