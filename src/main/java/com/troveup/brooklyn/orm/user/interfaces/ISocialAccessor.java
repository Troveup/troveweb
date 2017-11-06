package com.troveup.brooklyn.orm.user.interfaces;

import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.user.model.SocialConnection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionKey;

import java.util.List;
import java.util.Set;

/**
 * @author Tim Growney
 *
 * Interface definition to use when creating a SocialConnection Database Access Object repository for Trove.  Allows for
 * the classes that use this interface to be agnostic of the underlying storage medium, whether that be MySQL or other
 * Google Cloud Datastore.
 */
public interface ISocialAccessor {

    /**
     * Creates a SocialConnection database object out of a ConnectionData object, associates it with a given user,
     * then persists it.
     *
     * @param data ConnectionData object representing the SocialConnection object that needs to be persisted.
     * @param userId Persistent primary key identifier of a given User that should have this ConnectionData object
     *               attached.
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean addUserSocialConnection(ConnectionData data, Long userId);

    /**
     * Updates a SocialConnection.  Optimally retrieves the object to be updated by using the ProviderId and the
     * ProviderUserId within the ConnectionData object as the search key to locate the SocialConnection object to
     * update.  Updates the accessToken, displayName, expireTime, imageUrl, profileUrl, refreshToken, and secret fields
     * of the SocialConnection object to that of the passed ConnectionData object.
     *
     * @param data ConnectionData object containing the data to use to update the persisted SocialConnection.
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean updateUserSocialConnection(ConnectionData data);

    /**
     * Retrieves a SocialConnection, using the ConnectionKey fields ProviderId and ProviderUserId as query parameters.
     *
     * @param key ConnectionKey object used to filter the search.
     * @param mode Amount of data to pull.  Some persistence frameworks will not pull complex objects that are joined
     *             to the objects that are being pulled unless instructed to do so.  A QUICK seek should provide a
     *             minimal object, while a FULL seek should provide all child objects associated with the base object.
     * @return SocialConnection object representing a social connection.
     */
    SocialConnection getSocialConnection(ConnectionKey key, IEnums.SEEK_MODE mode);

    /**
     * Retrieves a SocialConnection, using the ConnectionKey fields ProviderId and ProviderUserId as query parameters.
     * Additionally filters by userId.  If the SocialConnection exists but is not associated with the given primary
     * key userId of a given User, the result is not included in the search.
     *
     * @param key ConnectionKey object used to filter the search.
     * @param userId Persistent primary key User identifier used as an additional search parameter.
     * @param mode Amount of data to pull.  Some persistence frameworks will not pull complex objects that are joined
     *             to the objects that are being pulled unless instructed to do so.  A QUICK seek should provide a
     *             minimal object, while a FULL seek should provide all child objects associated with the base object.
     * @return SocialConnection object representing a social connection.
     */
    SocialConnection getSocialConnection(ConnectionKey key, Long userId, IEnums.SEEK_MODE mode);

    /**
     * Retrieves all SocialConnection objects that match a given providerId and userId filter.
     *
     * @param providerId Social connection provider identifier to use as a filter.
     * @param userId Persistent primary key User identifier to use as a secondary filter.
     * @param mode Amount of data to pull.  Some persistence frameworks will not pull complex objects that are joined
     *             to the objects that are being pulled unless instructed to do so.  A QUICK seek should provide a
     *             minimal object, while a FULL seek should provide all child objects associated with the base object.
     * @return SocialConnection object representing a social connection.
     */
    List<SocialConnection> getSocialConnections(String providerId, Long userId, IEnums.SEEK_MODE mode);

    /**
     * Retrieves all SocialConnection objects associated with a given persisted User represented by the passed userId.
     *
     * @param userId Persistent primary key User identifier.
     * @param mode Amount of data to pull.  Some persistence frameworks will not pull complex objects that are joined
     *             to the objects that are being pulled unless instructed to do so.  A QUICK seek should provide a
     *             minimal object, while a FULL seek should provide all child objects associated with the base object.
     * @return List of SocialConnection objects representing a social connection.
     */
    List<SocialConnection> getSocialConnections(Long userId, IEnums.SEEK_MODE mode);

    /**
     * Retrieves all SocialConnection objects that match the providerId, fall into the list of providerUserIds, and
     * belong to a given User represented by the passed userId.
     *
     * @param providerId Social connection provider identifier to use as a filter.
     * @param providerUserId List of user identifiers from the social provider.
     * @param userId Persistent primary key User identifier to use as a secondary filter.
     * @param mode Amount of data to pull.  Some persistence frameworks will not pull complex objects that are joined
     *             to the objects that are being pulled unless instructed to do so.  A QUICK seek should provide a
     *             minimal object, while a FULL seek should provide all child objects associated with the base object.
     * @return List of SocialConnection objects representing a social connection.
     */
    List<SocialConnection> getSocialConnections(String providerId, List<String> providerUserId, Long userId, IEnums.SEEK_MODE mode);

    /**
     * Retrieves all SocialConnection objects that match the providerId and fall into the list of providerUserIds.
     *
     * @param providerId Social connection provider identifier to use as a filter.
     * @param providerUserId List of user identifiers from the social provider.
     * @param mode Amount of data to pull.  Some persistence frameworks will not pull complex objects that are joined
     *             to the objects that are being pulled unless instructed to do so.  A QUICK seek should provide a
     *             minimal object, while a FULL seek should provide all child objects associated with the base object.
     * @return List of SocialConnection objects representing a social connection.
     */
    List<SocialConnection> getSocialConnections(String providerId, List<String> providerUserId, IEnums.SEEK_MODE mode);

    /**
     * Removes a given SocialConnection from persistence.
     *
     * @param key ConnectionKey filter containing the providerId and providerUserId for use with finding the
     *            SocialConnection earmarked for deletion.
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean removeConnection(ConnectionKey key);

    /**
     * Removes a given SocialConnection from persistence.  Uses a ConnectionKey and the primary key from a User as a
     * filter.
     *
     * @param key ConnectionKey filter containing the providerId and providerUserId for use with finding the
     *            SocialConnection earmarked for deletion.
     * @param userId Persistent primary key User identifier to use as a filter.
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean removeConnection(ConnectionKey key, Long userId);

    /**
     * Removes a given SocialConnection from persistence.  Uses a providerId and the primary key from a User as a
     * filter.
     *
     * @param providerId providerId of SocialConnection object that needs to be removed.
     * @param userId Primary key from a User that is associated with the given SocialConnection.
     * @return Boolean response indicating the success state of the operation.
     */
    Boolean removeConnection(String providerId, Long userId);

    /**
     * Retrieves a Set of primary key User identifiers, filtered by the providerId and providerUserId found in the
     * passed ConnectionKey.
     *
     * @param key ConnectionKey containing the providerId and providerUserId for filtering the result.
     * @return Set of primary key User identifiers.
     */
    Set<Long> getUserIds(ConnectionKey key);

    /**
     * Retrieves a Set of usernames associated with Users filtered by the providerId and providerUserId found in the
     * passed ConnectionKey.
     *
     * @param key ConnectionKey containing the providerId and providerUserId for filtering the result.
     * @return Set of usernames.
     */
    Set<String> getUsernames(ConnectionKey key);

    /**
     * Retrieves a Set of emails associated with Users filtered by the providerId and providerUserId found in the
     * passed ConnectionKey.
     *
     * @param key ConnectionKey containing the providerId and providerUserId for filtering the result.
     * @return Set of emails.
     */
    Set<String> getEmails(ConnectionKey key);

    /**
     * Retrieves a Set of primary key identifiers associated with Users filtered by the providerId and
     * list of providerUserIds.
     *
     *
     * @param providerId providerId of the SocialConnection object.
     * @param providerUserIds List of providerUserIds of possible various SocialConnection objects.
     * @return Set of primary key identifiers identifying User objects found by the search.
     */
    Set<Long> getUserIds(String providerId, List<String> providerUserIds);
}
