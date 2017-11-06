package com.troveup.brooklyn.orm.user.interfaces;

import com.troveup.brooklyn.orm.cart.model.Cart;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.user.model.*;
import com.troveup.brooklyn.util.exception.UsernameAlreadyInUseException;

import javax.jdo.PersistenceManager;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Tim Growney
 *
 * Interface definition to use when creating a User Database Access Object repository for Trove.  Allows for the
 * classes that use this interface to be agnostic of the underlying storage medium, whether that be MySQL or other
 * Google Cloud Datastore.
 */
public interface IUserAccessor {

    /**
     * Persists a new User object.
     *
     * @param user User object containing the data to persist.
     * @return Boolean response indicating the result of the operation.
     * @throws UsernameAlreadyInUseException
     */
    Boolean createUser(User user) throws UsernameAlreadyInUseException;

    /**
     * Retrieves a list of Users based on the given identifiers.
     *
     * @param userIdentifier List of identifying information to use to pull a given set of Users (e.g. list of
     *                       last names).
     * @param identifierType Type of identifying information provided by the userIdentifier list.
     * @param mode Amount of data to pull.  Some persistence frameworks will not pull complex objects that are joined
     *             to the objects that are being pulled unless instructed to do so.  A QUICK seek should provide a
     *             minimal object, while a FULL seek should provide all child objects associated with the base object.
     * @return List of User objects that were found using the supplied identifying information if found, null otherwise.
     */
    List<User> getUser(List<String> userIdentifier, User.SEARCH_BY_TYPE identifierType, IEnums.SEEK_MODE mode);

    /**
     * Retrieves a list of Users based on the given identifier.
     *
     * @param userIdentifier Identifying information to use to pull a given set of users (e.g. a last name).
     * @param identifierType Type of identifying information provided by the userIdentifier.
     * @param mode Amount of data to pull.  Some persistence frameworks will not pull complex objects that are joined
     *             to the objects that are being pulled unless instructed to do so.  A QUICK seek should provide a
     *             minimal object, while a FULL seek should provide all child objects associated with the base object.
     * @return List of User objects that were found using the supplied identifying information if found, null otherwise.
     */
    List<User> getUser(String userIdentifier, User.SEARCH_BY_TYPE identifierType, IEnums.SEEK_MODE mode);

    /**
     * Retrieves a User object by its persisted primary key identifier.
     *
     * @param id Persistent primary key identifier.
     * @param mode Amount of data to pull.  Some persistence frameworks will not pull complex objects that are joined
     *             to the objects that are being pulled unless instructed to do so.  A QUICK seek should provide a
     *             minimal object, while a FULL seek should provide all child objects associated with the base object.
     * @return User object that was found using the supplied identifier if found, null otherwise.
     */
    User getUser(Long id, IEnums.SEEK_MODE mode);

    /**
     * Retrieves a PersistenceManager managed user that is still attached to the persistence layer.  Any changes to the
     * returned user from this user object will be persisted.
     *
     * @param id Primary key user identifier for the user being requested.
     * @param pm PersistenceManager object to which the user should be attached.
     * @return DataNucleus Managed User object.
     */
    User getUserAttached(Long id, PersistenceManager pm);

    /**
     * Retrieves a User object by its e-mail address.
     *
     * @param email Email address with which to search a User object.
     * @param mode Amount of data to pull.  Some persistence frameworks will not pull complex objects that are joined
     *             to the objects that are being pulled unless instructed to do so.  A QUICK seek should provide a
     *             minimal object, while a FULL seek should provide all child objects associated with the base object.
     * @return User object that was found using the supplied email address if found, null otherwise.
     */
    User getUserByEmail(String email, IEnums.SEEK_MODE mode);

    /**
     * Retrieves a User object by its e-mail address.
     *
     * @param username Username with which to search a User object.
     * @param mode Amount of data to pull.  Some persistence frameworks will not pull complex objects that are joined
     *             to the objects that are being pulled unless instructed to do so.  A QUICK seek should provide a
     *             minimal object, while a FULL seek should provide all child objects associated with the base object.
     * @return User object that was found using the supplied email address if found, null otherwise.
     */
    User getUserByUsername(String username, IEnums.SEEK_MODE mode);

    /**
     * Creates a verification token and attaches it to the user specified by the userId.
     *
     * @param userId Persistent primary key User identifier.
     * @param token Unique string token to be used for verification.
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean addVerificationLinkage(Long userId, String token);

    /**
     * Retrieves a VerificationToken object based on its internal String token member.
     *
     * @param token Token specifier to use in the search for a given VerificationToken
     * @return VerificationToken object containing the token string parameter if found, null otherwise.
     */
    VerificationToken getVerificationToken(String token);

    /**
     * Marks a VerificationToken as verified so that it cannot be used again.
     *
     * @param token Token value for identifying which token to mark as verified.
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean setVerificationTokenVerified(String token);

    /**
     * Sets the enabled field of a given User to true.
     *
     * @param userId Persistent primary key User identifier.
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean setUserEnabled(Long userId);
    Boolean setUserDisabled(Long userId);

    /**
     * Creates a forgot password PasswordResetToken object and associates it with a specific User.  This can be used
     * for resetting a user's password.
     *
     * @param userId Persistent primary key User identifier.
     * @param token Token String value to set the PasswordResetToken's internal token field to.
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean createForgotPasswordLinkage(Long userId, String token);

    /**
     * Retrieves a PasswordResetToken object, which can be used for resetting a given user's password.
     *
     * @param token Token String value for the PasswordResetToken object that needs to be retrieved.
     * @return PasswordResetToken object if found, null otherwise.
     */
    PasswordResetToken getResetToken(String token);

    /**
     * Marks a request token as unusable by setting its valid field to false.
     *
     * @param token Token String value for the PasswordResetToken object that needs to be marked as invalid.
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean invalidateResetToken(String token);

    /**
     * Updates a User object's password field.
     *
     * @param userId Persistent primary key User identifier.
     * @param password User's encrypted password.
     * @return Boolean response indicating the success state of the operation.
     * @note Password must be encrypted by this point, or the application will not be able to authenticate the user.
     */
    Boolean updateUserPassword(Long userId, String password);

    /**
     * Adds an Item to the user's List of Troved items.
     *
     * @param identifier Unique user's e-mail address
     * @param itemId Unique primary key identifier of the item to add.
     * @param searchByType
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean addItemToTrove(String identifier, Long itemId, User.SEARCH_BY_TYPE searchByType);

    /**
     * Adds an item to the user's Trove.
     * @param userId Unique primary key of the user.
     * @param itemId Unique primary key of the item.
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean addItemToTrove(Long userId, Long itemId);

    /**
     * Adds a Trove Collection to the given user.
     *
     * @param email Unique user's e-mail address
     * @param collection Collection object containing Collection data.
     * @return Boolean response indicating the success state of the operation.
     */
    Long addCollectionToUser(String email, Collection collection);

    Long addCollectionToUser(Long userId, Collection collection);

    /**
     * Removes a Trove Collection from the given user.
     *
     * @param email Unique user's e-mail address.
     * @param collectionId Unique primary key identifier of the collection.
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean removeCollectionFromUser(String email, Long collectionId);

    Boolean removeCollectionFromUser(Long userId, Long collectionId);



    /**
     * Adds an item to a given collection.  Verifies that the collection belongs
     * to the given user's e-mail.
     *
     * @param email Unique user's e-mail address.
     * @param itemId Unique primary key identifier of the given item.
     * @param collectionId Unique primary key identifier of the collection to which the item is being added.
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean addItemToCollection(String email, Long itemId, Long collectionId);

    Boolean addItemToCollection(Long userId, Long itemId, Long collectionId);

    /**
     * Removes an item from a given collection.  Verifies that the collection belongs
     * to the given user's e-mail.
     *
     * @param email Unique user's e-mail address.
     * @param itemId Unique primary key identifier of the given item.
     * @param collectionId Unique primary key identifier of the collection from which the item should be removed.
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean removeItemFromCollection(String email, Long itemId, Long collectionId);

    Boolean removeItemFromCollection(Long userId, Long itemId, Long collectionId);

    /**
     * Updates the collection specified by the collectionId within the passed collection
     * and changes all simple collection fields (name, description, etc) that aren't null within the passed collection
     * to the new values.
     *
     * @param email Unique user's e-mail address.
     * @param collection Collection object containing the collectionId, and simple fields to be updated
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean updateCollection(String email, Collection collection);

    Boolean updateCollection(Long userId, Long collectionId, String collectionName, String collectionDescription,
                             Boolean privateCollection);

    Boolean deleteCollection(Long userId, Long collectionId);

    List<Collection> getPagedCollectionsByUserId(Long userId, Long collectionPage, Long collectionPageSize,
                                                        Long extraResults, Boolean includePrivate, IEnums.SEEK_MODE mode);

    /**
     * Get a persistence managed address for direct manipulation.
     *
     * @param addressId Unique primary key address identifier.
     * @param pm Manager to which this address object is attached.
     * @return Requested address object.
     */
    Address getAddressAttached(Long addressId, PersistenceManager pm);

    /**
     * Sets the user's active shopping cart to this cart, and adds it to their cart history.
     *
     * @param userId Unique primary key user identifier.
     * @param shoppingCart Shopping cart object to set as the user's cart.
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean addShoppingCartToUser(Long userId, Cart shoppingCart);

    /**
     * Adds an order to the user.
     *
     * @param userId Unique primary key user identifier.
     * @param order Order containing all of the pertenent details of a purchase
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean persistNewUserOrder(Long userId, Order order);

    /**
     * Sets the user's cart to null so that a new cart may be made after processing
     * an order.
     * @param userId Unique primary key user identifier.
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean removeCurrentCart(Long userId);

    /**
     * Sets the path for the user's profile and thumbnail images.
     *
     * @param userId Unique primary key user identifier.
     * @param profileImagePath Public URL path of the user's profile image to the storage location on the CDN.
     * @param profileThumbnailPath Public URL path of the user's profile thumbnail to the storage location on the CDN.
     * @return Boolean response indicating the success state of the operation
     */
    Boolean updateUserProfileImage(Long userId, String profileImagePath, String profileThumbnailPath);

    /**
     * Sets a user's opt-out status flag on their persisted user record.
     *
     * @param userId Unique primary key user identifier.
     * @param optOutStatus Boolean opt-out status.
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean updateUserOptOutFlag(Long userId, Boolean optOutStatus);

    /**
     * Sets the firstName and lastName fields of the User object specified by the userId field
     * to the new values provided.
     *
     * @param userId Unique primary key user identifier.
     * @param firstName First name of the user.
     * @param lastName Last name of the user.
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean updateUserFirstLastName(Long userId, String firstName, String lastName);

    /**
     * Retrieves a list of users that the supplied user has followed.
     *
     * @param userId Unique primary key user identifier.
     * @param mode SEEK_MODE specifier indicating the depth of data to pull.
     * @return List of users that the supplied user has followed.
     */
    List<User> getFollowedUsers(Long userId, IEnums.SEEK_MODE mode);

    /**
     * Retrieves a list of users that have followed the specified user.
     * @param userId Unique primary key user identifier.
     * @param mode SEEK_MODE specifier indicating the depth of data to pull.
     * @return List of users that have followed the supplied user.
     */
    List<User> getUsersWhoFollowedUser(Long userId, IEnums.SEEK_MODE mode);

    /**
     * Retrieves a count of the users that the supplied user has followed.
     * @param userId Unique primary key user identifier.
     * @return Integer count of the number of users the supplied user has followed.
     */
    Integer getFollowedUserCount(Long userId);

    /**
     * Retrieves a count of the users that have followed the supplied user.
     * @param userId Unique primary key user identifier.
     * @return Integer count of the number of users that have followed the supplied user.
     */
    Long getUsersWhoFollowedUserCount(Long userId);

    /**
     * Adds an entry into the user's follow list.
     *
     * @param followerId Unique primary key user identifier of the user that is doing the following.
     * @param followeeId Unique primary key user identifier of the user that is being followed.
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean followUser(Long followerId, Long followeeId);

    /**
     * Removes an entry from the user's follow list.
     *
     * @param followerId Unique primary key user identifier of the user that is doing the following.
     * @param followeeId Unique primary key user identifier of the user that is no longer to be followed.
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean unfollowUser(Long followerId, Long followeeId);

    /**
     * Check to see if the follower user is currently following the followee user.
     *
     * @param followerId Unique primary key user identifier of the user whose following list to check.
     * @param followeeId Unique primary key user identifier of the user to check if they're followed.
     * @return Boolean response indicating whether or not the user has been followed.
     */
    Boolean checkUserFollow(Long followerId, Long followeeId);

    Boolean setUserCompletedFtui(Long userId, Boolean hasCompletedFtue);

    Boolean removeItemFromTrove(Long userId, Long itemId);

    Boolean updateUserCoverPhotoImage(Long userId, String coverPhotoImagePath);

    Boolean followCollection(Long userId, Long collectionId);

    Boolean unfollowCollection(Long userId, Long collectionId);

    Boolean incrementItemCollectionTroveCount(Long itemId);
    Boolean incrementItemCollectionRemakeCount(Long itemId);
    Collection getCollectionAttached(Long collectionId, PersistenceManager pm);
    Boolean doesUserOwnCollection(Long collectionId, Long userId);
    User getUserByCollectionId(Long collectionId, IEnums.SEEK_MODE mode);
    List<User> getPagedFollowersByUserId(Long userId, Integer collectionPage, Long collectionPageSize, Long extraResults,
                                         IEnums.SEEK_MODE mode);
    List<User> getPagedFollowedByUserId(Long userId, Integer collectionPage, Long collectionPageSize, Long extraResults,
                                        IEnums.SEEK_MODE mode);

    List<User> getPagedFollowersOfCollection(Long collectionId, Integer collectionPage, Long collectionPageSize,
                                             Long extraResults, IEnums.SEEK_MODE mode);

    Long getCollectionCount(Long userId, Boolean includePrivate);
    Long getCollectionFollowCount(Long collectionId);
    Collection getCollectionByCollectionId(Long collectionId, IEnums.SEEK_MODE mode);

    Boolean checkUserAdmin(Long userId);

    String getUserReferralCode(Long userId);
    Boolean setUserReferrerCode(Long userId, String referrerCode);
    String getUserReferrerCode(Long userId);
    Long getReferralCountByUserId(Long userId);
    User getUserByReferralCode(String referralCode, IEnums.SEEK_MODE mode);

    Boolean updateUserBraintreeVaultId(Long userId, String vaultId);
    BigDecimal getUserStoreCredit(Long userId);
    BigDecimal addUserStoreCredit(Long userId, BigDecimal amount);
    BigDecimal subtractUserStoreCredit(Long userId, BigDecimal amount);
    User getUserByOrderId(Long orderId, IEnums.SEEK_MODE mode);
    Boolean updateUserShippingAddress(Long userId, Address shippingAddress);
    Boolean updateUsername(Long userId, String username);
    Boolean generateAndReplaceUsername(Long userId);
    User mergeUserAccounts(Long sourceUserId, Long destinationUserId);
    User createGenericPseudoUser(String randomEncodedPassword);

    List<User> getAllInfluencers(IEnums.SEEK_MODE mode);

    Signup createSignup(Signup signup);
}
