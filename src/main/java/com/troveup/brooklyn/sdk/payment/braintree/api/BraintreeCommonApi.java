package com.troveup.brooklyn.sdk.payment.braintree.api;

import com.braintreegateway.BraintreeGateway;
import com.troveup.brooklyn.orm.cart.interfaces.ICartAccessor;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by tim on 5/28/15.
 */
public abstract class BraintreeCommonApi
{
    public static final String BT_MERCHANT_ID = "kmts4967n92kvs69";
    public static final String BT_PUBLIC_KEY = "348x2kz3trk9q74p";
    public static final String BT_PRIVATE_KEY = "01986614616ae242821f410c5e8e3ef5";
    public static final String BT_ENVIRONMENT = "SANDBOX";

    @Autowired
    IUserAccessor userAccessor;

    @Autowired
    ICartAccessor cartAccessor;

    BraintreePaymentFactory braintreePaymentFactory;

    public BraintreeCommonApi(BraintreePaymentFactory braintreePaymentFactory)
    {
        this.braintreePaymentFactory = braintreePaymentFactory;
    }

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected void logError(Exception e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        logger.error("Stack Trace: " + sw.toString());
    }

}
