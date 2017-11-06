package com.troveup.brooklyn.sdk.payment.braintree.api;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.troveup.brooklyn.sdk.payment.braintree.interfaces.IPaymentFactory;

/**
 * Created by tim on 6/10/15.
 */
public class BraintreePaymentFactory implements IPaymentFactory
{
    private final Environment environment;
    private final String merchantId;
    private final String publicKey;
    private final String privateKey;

    public BraintreePaymentFactory(Environment environment, String merchantId, String publicKey, String privateKey)
    {
        this.environment = environment;
        this.merchantId = merchantId;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    @Override
    public BraintreeGateway getBraintreeGatewayInstance()
    {
        return new BraintreeGateway(environment, merchantId, publicKey, privateKey);
    }
}
