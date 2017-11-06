package com.troveup.brooklyn.orm.user.model;

import com.troveup.brooklyn.orm.cart.model.Cart;
import com.troveup.brooklyn.orm.cart.model.GiftCard;
import com.troveup.brooklyn.orm.cart.model.PromoCode;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.spring.social.model.UserDetails;
import org.springframework.stereotype.Component;

import javax.jdo.annotations.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 4/1/15.
 */
@Component
@PersistenceCapable(table = "user_accounts")
@Uniques({@Unique(name="UUNIQUE", members={"username"}), @Unique(name="EUNIQUE", members={"email"})})
@Cacheable(value = "false")
public class User implements Serializable {

    public enum SEARCH_BY_TYPE {
        IDENTITY_COLUMN_NAME("userId"),
        USER_NAME("username"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        EMAIL("email");


        private final String text;

        SEARCH_BY_TYPE(String type) {
            this.text = type;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    Long userId;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String password;

    private boolean enabled;

    private UserDetails.Role role;

    private List<SocialConnection> socialConnections;

    private List<VerificationToken> registrationToken;

    private List<PasswordResetToken> resetTokens;

    private List<GroupedItem> ownedItems;

    private List<GroupedItem> trovedItems;

    private List<Collection> userCollections;

    private Date created;

    private Address billingAddress;

    private Address shippingAddress;

    private List<com.troveup.brooklyn.orm.order.model.Order> orders;

    private Cart shoppingCart;

    private List<UserAttribute> userAttributes;

    private List<PromoCode> availablePromoCodes;

    private String fullProfileImagePath;
    private String profileImageThumbnailPath;
    private String coverPhotoImagePath;
    private Boolean optOut;

    private Boolean followed;

    private List<Follow> followedUsers;

    private Boolean hasUsedFtue;

    private int bagItemCount;

    private List<PromoCode> usedPromoCodes;

    private Boolean isAdmin;

    //User's code by which they refer people
    private String referralCode;

    //The code that this person used to refer people
    private String referrerCode;

    //Braintree User identifier for the representation of this user within Trove's Braintree Vault
    private String braintreeUserId;

    private BigDecimal storeBalance;

    private Boolean anonymousRegistration;

    private Boolean pseudoUser;
    private User mergedUserDestination;

    private Boolean originFtue;
    private Boolean influencer;
    private Boolean manufacturer;

    private Boolean seller;

    public User() {
        this.enabled = false;
        this.anonymousRegistration = false;
        this.pseudoUser = false;
        this.influencer = false;
        this.isAdmin = false;
        this.seller = false;

        //Last minute decision to only take a "name" for registration.  Make this an empty string.
        lastName = "";
        originFtue = false;
    }

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Persistent
    @Column(name = "first_name", allowsNull = "false")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Persistent
    @Column(name = "last_name", allowsNull = "false")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Persistent(defaultFetchGroup="false")
    @Column(name = "email", allowsNull = "false")
    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    @Persistent
    @Column(name = "role", allowsNull = "false")
    public UserDetails.Role getRole() {
        return role;
    }

    public void setRole(UserDetails.Role role) {
        this.role = role;
    }

    @Persistent(mappedBy="user")
    public List<SocialConnection> getSocialConnections() {
        return socialConnections;
    }

    public void setSocialConnections(List<SocialConnection> socialConnections) {
        this.socialConnections = socialConnections;
    }

    @Persistent(mappedBy = "user")
    public List<VerificationToken> getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(List<VerificationToken> registrationToken) {
        this.registrationToken = registrationToken;
    }

    @Persistent
    @Column(name = "created")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Persistent(defaultFetchGroup="false")
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Persistent
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Persistent(defaultFetchGroup="false")
    @Column(name = "enabled")
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Persistent(mappedBy = "user")
    public List<PasswordResetToken> getResetTokens() {
        return resetTokens;
    }

    public void setResetTokens(List<PasswordResetToken> resetTokens) {
        this.resetTokens = resetTokens;
    }

    @Persistent
    public List<GroupedItem> getOwnedItems() {
        return ownedItems;
    }

    public void setOwnedItems(List<GroupedItem> ownedItems) {
        this.ownedItems = ownedItems;
    }

    @Persistent
    public List<GroupedItem> getTrovedItems() {
        return trovedItems;
    }

    public void setTrovedItems(List<GroupedItem> trovedItems) {
        this.trovedItems = trovedItems;
    }

    @Persistent(mappedBy = "owner")
    public List<Collection> getUserCollections() {
        return userCollections;
    }

    public void setUserCollections(List<Collection> userCollections) {
        this.userCollections = userCollections;
    }

    @Persistent
    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    @Persistent
    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    @Persistent(mappedBy = "cartOwner")
    public Cart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(Cart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    @Persistent
    @Column(name = "ORDERS")
    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Persistent(mappedBy = "attributeOwner")
    public List<UserAttribute> getUserAttributes() {
        return userAttributes;
    }

    public void setUserAttributes(List<UserAttribute> userAttributes) {
        this.userAttributes = userAttributes;
    }

    @Persistent
    @Column(name = "PROMO_CODES")
    public List<PromoCode> getAvailablePromoCodes() {
        return availablePromoCodes;
    }

    public void setAvailablePromoCodes(List<PromoCode> availablePromoCodes) {
        this.availablePromoCodes = availablePromoCodes;
    }

    @Persistent
    @Column(name = "FULL_PROFILE_IMAGE")
    public String getFullProfileImagePath() {
        return fullProfileImagePath;
    }

    public void setFullProfileImagePath(String fullProfileImagePath) {
        this.fullProfileImagePath = fullProfileImagePath;
    }

    @Persistent
    @Column(name = "THUMBNAIL_PROFILE_IMAGE")
    public String getProfileImageThumbnailPath() {
        return profileImageThumbnailPath;
    }

    public void setProfileImageThumbnailPath(String profileImageThumbnailPath) {
        this.profileImageThumbnailPath = profileImageThumbnailPath;
    }

    @Persistent(defaultFetchGroup="false")
    @Column(name = "OPT_OUT")
    public Boolean getOptOut() {
        return optOut;
    }

    public void setOptOut(Boolean optOut) {
        this.optOut = optOut;
    }

    @Persistent
    public List<Follow> getFollowedUsers() {
        return followedUsers;
    }

    public void setFollowedUsers(List<Follow> followedUsers) {
        this.followedUsers = followedUsers;
    }

    @Persistent(defaultFetchGroup="false")
    @Column(name = "USED_FTUE")
    public Boolean getHasUsedFtue() {
        return hasUsedFtue;
    }

    public void setHasUsedFtue(Boolean hasUsedFtue) {
        this.hasUsedFtue = hasUsedFtue;
    }

    @NotPersistent
    public Boolean getFollowed() {
        return followed;
    }

    public void setFollowed(Boolean followed) {
        this.followed = followed;
    }

    @NotPersistent
    public int getBagItemCount() {
        return bagItemCount;
    }

    public void setBagItemCount(int bagItemCount) {
        this.bagItemCount = bagItemCount;
    }


    @Persistent
    @Column(name = "USED_PROMOCODES")
    public List<PromoCode> getUsedPromoCodes() {
        return usedPromoCodes;
    }

    public void setUsedPromoCodes(List<PromoCode> usedPromoCodes) {
        this.usedPromoCodes = usedPromoCodes;
    }

    @Persistent
    @Column(name = "COVER_PHOTO_PATH")
    public String getCoverPhotoImagePath() {
        return coverPhotoImagePath;
    }

    public void setCoverPhotoImagePath(String coverPhotoImagePath) {
        this.coverPhotoImagePath = coverPhotoImagePath;
    }

    @Persistent
    @Column(defaultValue = "0")
    public Boolean getIsAdmin()
    {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Persistent
    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    @Persistent
    public String getReferrerCode() {
        return referrerCode;
    }

    public void setReferrerCode(String referrerCode) {
        this.referrerCode = referrerCode;
    }

    @Persistent(defaultFetchGroup="false")
    public String getBraintreeUserId() {
        return braintreeUserId;
    }

    public void setBraintreeUserId(String braintreeUserId) {
        this.braintreeUserId = braintreeUserId;
    }

    @Persistent(defaultFetchGroup = "false")
    @Column(scale = 2)
    public BigDecimal getStoreBalance() {
        return storeBalance;
    }

    public void setStoreBalance(BigDecimal storeBalance) {
        this.storeBalance = storeBalance;
    }

    @Persistent
    @Column(defaultValue = "0")
    public Boolean getAnonymousRegistration() {
        return anonymousRegistration;
    }

    public void setAnonymousRegistration(Boolean anonymousRegistration) {
        this.anonymousRegistration = anonymousRegistration;
    }

    @Persistent
    @Column(defaultValue = "0")
    public Boolean getOriginFtue() {
        return originFtue;
    }

    public void setOriginFtue(Boolean originFtue) {
        this.originFtue = originFtue;
    }

    @Persistent
    @Column(defaultValue = "0")
    public Boolean getPseudoUser() {
        return pseudoUser;
    }

    @Persistent
    public void setPseudoUser(Boolean pseudoUser) {
        this.pseudoUser = pseudoUser;
    }

    public User getMergedUserDestination() {
        return mergedUserDestination;
    }

    public void setMergedUserDestination(User mergedUserDestination) {
        this.mergedUserDestination = mergedUserDestination;
    }

    @Persistent
    public Boolean getInfluencer() {
        return influencer;
    }

    public void setInfluencer(Boolean influencer) {
        this.influencer = influencer;
    }

    @Persistent
    public Boolean getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Boolean manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Persistent
    public Boolean getSeller() {
        return seller;
    }

    public void setSeller(Boolean seller) {
        this.seller = seller;
    }

    public static List<String> getUserFullFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("socialConnections");
        rval.add("ownedItems");
        rval.add("trovedItems");
        rval.add("registrationToken");
        rval.add("resetTokens");
        rval.add("userCollections");
        rval.add("billingAddress");
        rval.add("shippingAddress");
        rval.add("orders");
        rval.add("shoppingCart");

        return rval;
    }

    public static List<String> getUserQuickCartFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("shoppingCart");

        return rval;
    }

    public static List<String> getUserQuickCartFetchGroupFieldsPII()
    {
        List<String> rval = new ArrayList<>();

        rval.add("shoppingCart");
        rval.add("storeBalance");
        rval.add("email");
        rval.add("braintreeUserId");

        return rval;
    }

    public static List<String> getUserTrovedItemsFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("trovedItems");

        return rval;
    }

    public static List<String> getUserTroveFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("trovedItems");
        rval.add("followedUsers");

        return rval;
    }

    //Only fetch group that has a password.  CAREFUL USING THIS, AS IT WILL REVEAL A USER'S HASHED PASSWORD
    //IF IT IS ACCIDENTALLY PASSED TO THE FRONT END
    public static List<String> getUserSocialConnectionsFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("socialConnections");
        rval.add("password");
        rval.add("email");
        rval.add("enabled");

        return rval;
    }

    //This fetchgroup exposes PII.  Use with caution.
    public static List<String> getUserOrdersFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("orders");
        rval.add("email");

        return rval;
    }

    public static List<String> getUserRemadeItemFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("ownedItems");

        return rval;
    }

    public static List<String> getUserSettingsFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("optOut");
        rval.add("storeBalance");

        return rval;
    }

    public static List<String> getUserHasUsedFtueFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("hasUsedFtue");

        return rval;
    }

    public static List<String> getUserWithEmailAndFtueFlag()
    {
        List<String> rval = new ArrayList<>();

        rval.add("email");
        rval.add("hasUsedFtue");

        return rval;
    }

    public static List<String> getUsersThisUserHasFollowedFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("followedUsers");

        return rval;
    }

    public static List<String> getUserAddressFetchGroupFields()
    {

        List<String> rval = new ArrayList<>();

        rval.add("billingAddress");
        rval.add("shippingAddress");

        return rval;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof User)
            return ((User) obj).getUserId().equals(this.userId);
        else
            return false;
    }
}
