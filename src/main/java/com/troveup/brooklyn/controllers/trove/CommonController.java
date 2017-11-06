package com.troveup.brooklyn.controllers.trove;

import com.google.appengine.api.utils.SystemProperty;
import com.google.gson.Gson;
import com.troveup.brooklyn.orm.admin.interfaces.IAdminAccessor;
import com.troveup.brooklyn.orm.cart.interfaces.ICartAccessor;
import com.troveup.brooklyn.orm.cart.interfaces.IPromoCodeAccessor;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.countries.interfaces.ICountryAccessor;
import com.troveup.brooklyn.orm.feed.interfaces.IFeedAccessor;
import com.troveup.brooklyn.orm.ftui.impl.FtueAccessor;
import com.troveup.brooklyn.orm.item.interfaces.IItemAccessor;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.item.model.ItemImage;
import com.troveup.brooklyn.orm.item.model.PriceFilter;
import com.troveup.brooklyn.orm.materials.model.Finish;
import com.troveup.brooklyn.orm.materials.model.Material;
import com.troveup.brooklyn.orm.order.interfaces.IOrderAccessor;
import com.troveup.brooklyn.orm.order.interfaces.IPrintOrderAccessor;
import com.troveup.brooklyn.orm.renderqueue.interfaces.IRenderQueueAccessor;
import com.troveup.brooklyn.orm.renderqueue.model.Render;
import com.troveup.brooklyn.orm.simpleitem.interfaces.ISimpleItemAccessor;
import com.troveup.brooklyn.orm.storefront.interfaces.IStoreFrontAccessor;
import com.troveup.brooklyn.orm.urlshortener.interfaces.IShortLinkAccessor;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.orm.user.model.GroupedItem;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.cdn.gcs.GoogleCloudStorage;
import com.troveup.brooklyn.sdk.cdn.interfaces.CloudStore;
import com.troveup.brooklyn.sdk.cdn.model.CloudParam;
import com.troveup.brooklyn.sdk.email.interfaces.IMailProvider;
import com.troveup.brooklyn.sdk.image.interfaces.IImageManipProvider;
import com.troveup.brooklyn.sdk.keyvaluestore.interfaces.IKeyValueStore;
import com.troveup.brooklyn.sdk.mail.business.EasyPostProvider;
import com.troveup.brooklyn.sdk.meshexporter.forge.business.Forge;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.ForgeMeshVolumeResponse;
import com.troveup.brooklyn.sdk.meshexporter.java.business.MeshExport;
import com.troveup.brooklyn.sdk.payment.braintree.api.BraintreeApi;
import com.troveup.brooklyn.sdk.print.interfaces.IPrintSupplier;
import com.troveup.brooklyn.sdk.print.interfaces.ISampleSupplier;
import com.troveup.brooklyn.sdk.realityserver.business.RealityServerManager;
import com.troveup.brooklyn.sdk.tax.interfaces.ITaxHandler;
import com.troveup.brooklyn.spring.rdbms.TroveUserDetailsService;
import com.troveup.brooklyn.spring.social.model.UserDetails;
import com.troveup.brooklyn.util.*;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.StreamHandler;

/**
 * Created by tim on 5/29/15.
 */
public abstract class CommonController {
    @Autowired
    protected IUserAccessor userAccessor;

    @Autowired
    protected IItemAccessor itemAccessor;

    @Autowired
    protected ICartAccessor cartAccessor;

    @Autowired
    protected ICountryAccessor countryAccessor;

    @Autowired
    protected IPrintSupplier printSupplier;

    @Autowired
    protected ITaxHandler taxHandler;

    @Autowired
    protected IPromoCodeAccessor promoCodeAccessor;

    @Autowired
    protected IOrderAccessor orderAccessor;

    @Autowired
    protected BraintreeApi braintreeApi;

    @Autowired
    protected Environment env;

    @Autowired
    protected Gson gson;

    @Autowired
    protected CloudStore storage;

    @Autowired
    protected IMailProvider mailProvider;

    @Autowired
    protected BCryptPasswordEncoder encoder;

    @Autowired
    protected IImageManipProvider imageProvider;

    @Autowired
    protected FtueAccessor ftueAccessor;

    @Autowired
    protected Forge forgeExporter;

    @Autowired
    protected ISampleSupplier sampleSupplier;

    @Autowired
    protected MeshExport meshExporter;

    @Autowired
    protected IRenderQueueAccessor renderQueueAccessor;

    @Autowired
    protected EasyPostProvider easyPostProvider;

    @Autowired
    protected IPrintOrderAccessor printOrderAccessor;

    @Autowired
    protected RealityServerManager realityServerManager;

    @Autowired
    protected IFeedAccessor feedAccessor;

    @Autowired
    protected IKeyValueStore keyValueStore;

    @Autowired
    protected IAdminAccessor adminAccessor;

    @Autowired
    protected IShortLinkAccessor shortLinkAccessor;

    @Autowired
    protected TroveUserDetailsService userDetailsService;

    @Autowired
    protected ISimpleItemAccessor simpleItemAccessor;

    @Autowired
    protected IStoreFrontAccessor storeFrontAccessor;

    //Long story, but the authenticationManager has to be exposed as a bean manually:
    //http://docs.spring.io/spring-security/site/docs/current/apidocs/org/springframework/security/config/annotation/web/configuration/WebSecurityConfigurerAdapter.html#authenticationManager()
    //So, autowire it with the qualifier so that we can get the singleton
    @Qualifier("troveAuthenticationManager")
    @Autowired
    protected AuthenticationManager authenticationManager;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final String REFERRER_SESSION_INDEX = "REFERRER_CODE";
    protected java.util.logging.Logger javaLogger = java.util.logging.Logger.getLogger("GoogleDiagLogger");
    protected final String SESSION_VAR_ANONYMOUS_EMAIL = "ANONYMOUS_EMAIL";
    protected final String SESSION_VAR_ANONYMOUS_NAME = "ANONYMOUS_NAME";
    protected final String SESSION_VAR_PSEUDO_USER_ID = "PSEUDO_USER_ID";
    protected final String SESSION_VAR_ADMIN_BACK_BUTTON_PRESERVATION_SETTINGS = "ADMIN_BACK_BUTTON_PRESERVATION";
    protected final String SESSION_VAR_PAGE_BROWSE_COUNT = "PAGE_BROWSE_COUNT";


    public CommonController() {
        javaLogger.addHandler(new StreamHandler());
    }

    protected void logError(Exception e) {
        ErrorUtils.logError(e, logger);
    }

    /**
     * Gets the authenticated user's User object.  Includes the user's e-mail.  As such, this is unsafe to pass
     * as a full method for AJAX requests, as it will leak an e-mail address.
     *
     * @return User object associated with the authenticated user, or null otherwise.
     */
    protected User getQuickUserDetailsPII() {
        return getQuickUserDetailsPII(null);
    }

    protected User getQuickUserDetailsPII(IEnums.SEEK_MODE mode) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = null;

        if (auth.getPrincipal() instanceof UserDetails) {
            UserDetails details = (UserDetails) auth.getPrincipal();
            name = details.getEmail();
        }

        User user = null;

        if (name != null) {
            if (name.contains("@")) {
                user = userAccessor.getUserByEmail(name, mode == null ? IEnums.SEEK_MODE.USER_BAG_COUNT_EMAIL : mode);
            } else {
                user = userAccessor.getUserByUsername(name, mode == null ? IEnums.SEEK_MODE.USER_BAG_COUNT_EMAIL : mode);
            }
        }

        return user;
    }

    /**
     * Safe method to use for quick retrieval of a user object.  Does not include any PII, and can be passed around
     * safely.
     *
     * @return User object associated with the authenticated user, or null otherwise.
     */
    protected User getQuickUserDetailsSafe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = null;

        if (auth.getPrincipal() instanceof UserDetails) {
            UserDetails details = (UserDetails) auth.getPrincipal();
            name = details.getEmail();
        }

        User user = null;

        if (name != null) {
            if (name.contains("@")) {
                user = userAccessor.getUserByEmail(name, IEnums.SEEK_MODE.USER_BAG_COUNT);
            } else {
                user = userAccessor.getUserByUsername(name, IEnums.SEEK_MODE.USER_BAG_COUNT);
            }
        }

        return user;
    }

    /**
     * System for discriminating against cloud based development environments based on configuration file settings
     *
     * @return Whether or not this instance is denoted a development server or not.
     */
    protected Boolean isDevelopmentServer() {
        if (env.getProperty("environment.type") != null && env.getProperty("environment.type").equals("DEVELOPMENT"))
            return true;
        else
            return false;
    }

    /**
     * Method that returns whether or not this running instance is operating on a local dev environment.  @see isDevelopmentServer()
     * for telling the difference between cloud based environments.
     *
     * @return Whether or not this instance is running on a local development environment.
     */
    protected Boolean isLocalDevelopmentServer() {
        return SystemProperty.environment.value() == SystemProperty.Environment.Value.Development;
    }

    protected String getSiteUrl() {
        return env.getProperty("environment.baseurl");
    }

    protected void debug(String debugData) {
        javaLogger.log(Level.INFO, debugData);

        Handler[] handlers = javaLogger.getHandlers();

        for (Handler handler : handlers) {
            handler.flush();
        }
    }

    protected String getCustomizerPath() {
        return env.getProperty("cloudstore.cloudstoreurl") + env.getProperty("cloudstore.jsonbucketname") +
                "/" + env.getProperty("cloudstore.customizerfragmenturl") +
                env.getProperty("cloudstore.customizerversionurlfragment");
    }

    protected Boolean isAuthenticated() {
        SecurityContext context = SecurityContextHolder.getContext();

        return SecurityContextHolder.getContext() != null &&
                SecurityContextHolder.getContext().getAuthentication() != null &&
                !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser");
    }

    /**
     * Iterates through the user's items and makes sure that their default images are set.  Assumes that the user
     * objects are populated with GroupedItems, and those individual GroupedItem objects have Item objects populated
     * with ItemImages.
     *
     * @param users     List of users on which to work.
     * @param materials List of materials used to pull in the correct item material.
     */
    protected void setDefaultUserItemImages(List<User> users, List<Material> materials) {
        //Make sure that the default images have been populated with something.  If they haven't, go ahead and populate
        //them just for this instance, as there is a chance that the server is still rendering the images.
        for (User user : users) {
            for (GroupedItem groupedItem : user.getTrovedItems()) {
                if (groupedItem.getGroupedItem().getDefaultCardImageUrl() == null ||
                        groupedItem.getGroupedItem().getDefaultCardImageUrl().length() == 0) {
                    setDefaultItemImage(groupedItem, materials);
                }
            }
        }
    }

    /**
     * Method for setting the default image of either an Item or a GroupedItem type object.  Will attempt to extract
     * an Item from a GroupedItem in order to perform the default Item Image work on it.
     *
     * @param item      Object that will be either casted to GroupedItem or directly acted upon
     * @param materials Materials available to grab an image from
     */
    protected void setDefaultItemImage(Object item, List<Material> materials) {
        Item workItem = null;

        //Handle the case of either a GroupedItem or Item, as they both need the action done to them
        if (item instanceof GroupedItem) {
            workItem = ((GroupedItem) item).getGroupedItem();
        } else if (item instanceof Item) {
            workItem = (Item) item;
        } else {
            throw new IllegalArgumentException("Cannot do work on anything other than class Item or GroupedItem");
        }

        if (workItem != null && (workItem.getDefaultCardImageUrl() == null ||
                workItem.getDefaultCardImageUrl().length() == 0)) {
            String imageUrl = "";
            Finish itemFinish = Finish.getFinishByFinishId(workItem.getFinishId(),
                    Material.getMaterialByMaterialId(workItem.getMaterialId(),
                            materials).getFinishList());

            for (ItemImage image : workItem.getImages()) {
                if (image.getMaterial() != null && image.getMaterial().equals(itemFinish.getRealityServerMapping())) {
                    imageUrl = image.getLargeImageUrlPath();
                    break;
                }
            }

            //No default images set, grab the first of the images as a backup
            if (imageUrl.length() == 0) {
                if (workItem.getImages() != null && workItem.getImages().size() > 0)
                    imageUrl = workItem.getImages().get(0).getLargeImageUrlPath();

                //That didn't work either, set it to the last-resort backup
                if (imageUrl == null || imageUrl.length() == 0) {
                    imageUrl = "https://storage.googleapis.com/troveup-imagestore/default-image-icon-1.jpg";
                }
            }

            workItem.setDefaultCardImageUrl(imageUrl);
        }
    }

    /**
     * Submits a request to Google's worker queue for this app to have renders placed for this item.
     *
     * @param item The item for which images need to be rendered.
     */
    protected void submitRenderToRealityServer(Item item) {
        Map<String, String> queueWorkMap = new HashMap<>();
        queueWorkMap.put("itemId", item.getItemId().toString());
        queueWorkMap.put("renderType", Render.RENDER_TYPE.ITEM.toString());
        WorkerQueuer.queueWorkForWorker(queueWorkMap, WorkerController.REALITY_WORKER_SUBMIT_URL);
    }

    public Money getItemPrice(Long itemId, String materialId, String finishId, Float volume) {
        return getItemPrice(itemId, materialId, finishId, volume, null);
    }

    public Money getItemPrice(Long itemId, String materialId, String finishId, Float volume, PriceFilter filter) {
        Money markupPrice;
        Money price;

        Item item = itemAccessor.getItemDetached(itemId, IEnums.SEEK_MODE.ITEM_PRICING);

        if (volume != null) {
            price = printSupplier.getInstantPrice(materialId, finishId, (volume / 1000));
            logger.debug("Manufacturer price is " + price.toString());
        } else {
            if (item.getMediumVolume() == null) {
                ForgeMeshVolumeResponse response = forgeExporter.getVolume(item.getOperatorsFromCustomizations(),
                        item.getVisibleMeshes(),
                        SizeMapper.getMediumExportSize(item.getCategory()), item.getCustomizerPath() + item.getCustomizerFilename());

                if (response != null && response.getSuccess() != null && response.getSuccess() &&
                        response.getVolume() != null) {
                    itemAccessor.updateItemMediumVolume(item.getItemId(), Float.parseFloat(response.getVolume()));
                    item.setMediumVolume(Float.parseFloat(response.getVolume()));
                } else {
                    logger.error("Item volume calculation failure for itemId, defaulting to 5000.0f " + itemId);
                    item.setMediumVolume(5000.0f);
                }
            }

            if (materialId != null && finishId != null) {
                price = printSupplier.getInstantPrice(materialId, finishId, (item.getMediumVolume() / 1000));
                logger.debug("Manufacturer price is " + price.toString());
            } else {
                price = printSupplier.getInstantPrice(item.getMaterialId(), item.getFinishId(), (item.getMediumVolume() / 1000));
                logger.debug("Manufacturer price is " + price.toString());
            }
        }

        if (price.isGreaterThan(Money.zero(CurrencyUnit.USD))) {

            if (filter == null) {
                filter = itemAccessor.getApplicablePricefilter(item, materialId, finishId, IEnums.SEEK_MODE.QUICK);
            }

            markupPrice = MoneyUtil.applyMarkup(price, "USD", filter);
        } else
            markupPrice = Money.zero(CurrencyUnit.USD);

        logger.debug("Price after markup: " + markupPrice);

        return markupPrice;
    }

    /**
     * Common ModelAndView object builder that populates ModelAndView objects with objects that are required
     * across views.
     *
     * @param view The view that the ModelAndView object should be prepopulated with.
     * @param req  The HttpRequest Object
     * @return ModelAndView object with common child objects necessary for every view.
     */
    protected ModelAndView getModelAndView(String view, HttpServletRequest req) {
        ModelAndView rval = view != null ? new ModelAndView(view) : new ModelAndView();

        //Get the latest alert banner
        rval.addObject("alertBannerState", adminAccessor.getLatestAlertBannerState());

        User user = getPseudoUserOrAuthUserPII(req, IEnums.SEEK_MODE.USER_BAG_COUNT_EMAIL);

        if (user != null && ((rval.getViewName() != null && !rval.getViewName().contains("redirect")) || rval.getViewName() == null))
            rval.addObject("bagItemCount", user.getBagItemCount());

        if (req.getSession().getAttribute(SESSION_VAR_PAGE_BROWSE_COUNT) == null)
        {
            req.getSession().setAttribute(SESSION_VAR_PAGE_BROWSE_COUNT, "1");
        }
        else if (req.getSession().getAttribute(SESSION_VAR_PAGE_BROWSE_COUNT) != null &&
                ((String) req.getSession().getAttribute(SESSION_VAR_PAGE_BROWSE_COUNT)).length() > 0)
        {
            Integer pageVisitCounter = Integer.parseInt((String) req.getSession().getAttribute(SESSION_VAR_PAGE_BROWSE_COUNT));

            if (pageVisitCounter == 2)
            {
                rval.addObject("showFreeShippingModal", true);
            }

            pageVisitCounter++;
            req.getSession().setAttribute(SESSION_VAR_PAGE_BROWSE_COUNT, pageVisitCounter.toString());

        }

        return rval;
    }

    protected ModelAndView getModelAndView(HttpServletRequest req) {
        return getModelAndView(null, req);
    }

    protected Item checkAndUpdateItemStartingPrice(Item item) {
        if (item != null && item.getStartingPrice() == null) {
            String MATERIAL_PLATED = "3";
            String FINISH_PLATED = "110";
            Money price = getItemPrice(item.getItemId(), MATERIAL_PLATED,
                    FINISH_PLATED, null);

            itemAccessor.updateItemStartingPrice(item.getItemId(), price.getAmount());
            item.setStartingPrice(price.getAmount());
        }

        return item;
    }

    protected String generateOrderNumber() {
        String rval = StringUtils.generateRandomUppercaseString(7);

        Boolean uniquenessCheck = orderAccessor.checkIfOrderNumberExists(rval);

        while (uniquenessCheck) {
            rval = StringUtils.generateRandomUppercaseString(7);
            uniquenessCheck = orderAccessor.checkIfOrderNumberExists(rval);
        }

        return rval;
    }

    protected User populateUserObjectFromSecurityContextPrincipal(UserDetails principal) {
        User user = new User();
        user.setUserId(principal.getId());
        user.setEmail(principal.getEmail());
        user.setFirstName(principal.getFirstName());
        user.setProfileImageThumbnailPath(principal.getThumbnailProfileImage());

        return user;
    }

    protected Long getPseudoUserFromSession(HttpServletRequest req) {
        Long rval = null;

        if (req.getSession().getAttribute(SESSION_VAR_PSEUDO_USER_ID) != null && ((String) req.getSession().getAttribute(SESSION_VAR_PSEUDO_USER_ID)).length() > 0) {
            rval = Long.parseLong((String) req.getSession().getAttribute(SESSION_VAR_PSEUDO_USER_ID));
        }

        return rval;
    }

    protected Long setSessionUnauthedUser(HttpServletRequest req, Long pseudoUserId) {
        req.getSession().setAttribute(SESSION_VAR_PSEUDO_USER_ID, pseudoUserId.toString());

        return pseudoUserId;
    }

    protected void clearSessionUnauthedUser(HttpServletRequest req) {
        req.getSession().setAttribute(SESSION_VAR_PSEUDO_USER_ID, null);
    }

    protected Long getOrCreatePseudoUserIdFromSession(HttpServletRequest req) {
        Long rval = getPseudoUserFromSession(req);

        if (rval == null) {
            User user = userAccessor.createGenericPseudoUser(encoder.encode(StringUtils.generateRandomUppercaseString(10)));
            rval = user.getUserId();
            setSessionUnauthedUser(req, rval);
        }

        return rval;
    }

    protected void checkMergeAndClearPseudoUser(HttpServletRequest req, Long destinationUserId) {
        //Merge pseudo user into permanent user if necessary
        Long pseudoUserId = getPseudoUserFromSession(req);
        if (pseudoUserId != null) {
            userAccessor.mergeUserAccounts(pseudoUserId, destinationUserId);
            clearSessionUnauthedUser(req);
        }
    }

    protected User getOrCreatePseudoUserOrGetAuthUserPII(HttpServletRequest req, IEnums.SEEK_MODE mode) {
        User rval = null;

        if (!isAuthenticated()) {
            Long userId = getOrCreatePseudoUserIdFromSession(req);
            rval = userAccessor.getUser(userId, mode);
        } else {
            rval = getQuickUserDetailsPII(mode);
        }

        return rval;
    }

    protected User getPseudoUserOrAuthUserPII(HttpServletRequest req, IEnums.SEEK_MODE mode) {
        User rval = null;

        if (!isAuthenticated() && getPseudoUserFromSession(req) != null) {
            Long userId = getPseudoUserFromSession(req);
            List<User> userList = userAccessor.getUser(userId.toString(), User.SEARCH_BY_TYPE.IDENTITY_COLUMN_NAME, mode);

            if (userList != null && userList.size() > 0)
                rval = userList.get(0);

        } else {
            rval = getQuickUserDetailsPII(mode);
        }

        return rval;
    }

    protected String sendImageToCloudImageStorage(byte[] imageData, String imageName, String bucketName, String mimeType) {
        String rval = null;

        try {
            if (!isLocalDevelopmentServer()) {
                List<CloudParam> cloudParams = new ArrayList<>();
                cloudParams.add(new CloudParam(GoogleCloudStorage.KEY_BUCKET_NAME, bucketName));
                cloudParams.add(new CloudParam(GoogleCloudStorage.KEY_MIME_TYPE, mimeType));

                if (storage.writeBytes(imageData,
                        imageName,
                        cloudParams)) {
                    rval = imageName;
                }
            } else {
                logger.debug("Skipping upload of image " + imageName + ", we're on a local server.");
                rval = imageName;
            }
        } catch (Exception e) {
            logError(e);
        }

        return rval;
    }

    protected Boolean checkIsAdminOrManufacturer(User user) {
        return user != null && (userAccessor.checkUserAdmin(user.getUserId()) || (user.getManufacturer() != null && user.getManufacturer()));
    }

    protected void sendErrorEmail(String error, String subject) {
        Map<String, String> founders = new HashMap<>();
        founders.put("Tim", "api@troveup.com");

        mailProvider.sendRawEmail(subject, "errors@troveup.com",
                founders, error, false);
    }

    protected String parseImageType(String imageDataString) {
        String rval = "png";

        try {
            rval = imageDataString.split("[/;]")[1];
        } catch (Exception e) {
            logError(e);
        }

        return rval;
    }
}
