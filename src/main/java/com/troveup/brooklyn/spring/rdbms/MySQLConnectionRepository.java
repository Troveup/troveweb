package com.troveup.brooklyn.spring.rdbms;

import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.user.model.SocialConnection;
import com.troveup.brooklyn.orm.user.datanucleus.UserAccessor;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

/**
 * Created by tim on 4/16/15.
 */
public class MySQLConnectionRepository implements ConnectionRepository
{

    UserAccessor userAccessor;

    Long userId;
    ConnectionFactoryLocator connectionFactoryLocator;
    TextEncryptor textEncryptor;

    public MySQLConnectionRepository(String userId, ConnectionFactoryLocator connectionFactoryLocator,
                                     TextEncryptor textEncryptor, UserAccessor userAccessor)
    {
        this.userId = Long.getLong(userId);
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.textEncryptor = textEncryptor;
        this.userAccessor = userAccessor;
    }

    /**
     * Find all connections the current user has across all providers.
     *
     * @return The returned map is sorted by providerId and entry values are ordered by rank. Returns an empty map if the user has no connections.
     */
    @Override
    public MultiValueMap<String, Connection<?>> findAllConnections() {
        MultiValueMap<String, Connection<?>> rval = new LinkedMultiValueMap<>();

        Set<String> registeredProviderIds = connectionFactoryLocator.registeredProviderIds();
        for (String registeredProviderId : registeredProviderIds) {
            rval.put(registeredProviderId, Collections.<Connection<?>>emptyList());
        }

        List<SocialConnection> connections = userAccessor.getSocialConnections(userId, IEnums.SEEK_MODE.FULL);

        //TODO:  Find a better way to do this.
        for (String registeredProvider : registeredProviderIds)
        {
            List<Connection<?>> idGrouping = new ArrayList<>();
            ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(registeredProvider);

            for (SocialConnection connection : connections)
            {
                if (connection.getProviderId().equals(registeredProvider)) {
                    connection.setAccessToken(decrypt(connection.getAccessToken()));
                    connection.setSecret(decrypt(connection.getSecret()));
                    connection.setRefreshToken(decrypt(connection.getRefreshToken()));
                    idGrouping.add(connectionFactory.createConnection(SocialConnection.getBuilder(connection).buildConnectionData()));
                }
            }

            rval.put(registeredProvider, idGrouping);
        }

        return rval;
    }

    /**
     *
     * @param providerId
     * @return
     */
    @Override
    public List<Connection<?>> findConnections(String providerId)
    {
        List<Connection<?>> rval = null;
        List<SocialConnection> connections = userAccessor.getSocialConnections(providerId, userId, IEnums.SEEK_MODE.FULL);
        if (connections != null && connections.size() > 0)
        {
            rval = new ArrayList<>();

            ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(providerId);

            for (SocialConnection connection : connections)
            {
                connection.setAccessToken(decrypt(connection.getAccessToken()));
                connection.setSecret(decrypt(connection.getSecret()));
                connection.setRefreshToken(decrypt(connection.getRefreshToken()));
                rval.add(connectionFactory.createConnection(SocialConnection.getBuilder(connection).buildConnectionData()));
            }
        }

        return rval;
    }

    /**
     *
     * @param aClass
     * @param <A>
     * @return
     */
    @Override
    public <A> List<Connection<A>> findConnections(Class<A> aClass)
    {
        ConnectionFactory<A> factory = connectionFactoryLocator.getConnectionFactory(aClass);

        List<Connection<A>> rval = null;
        List<SocialConnection> connections = userAccessor.getSocialConnections(factory.getProviderId(), userId, IEnums.SEEK_MODE.FULL);
        if (connections != null && connections.size() > 0)
        {
            rval = new ArrayList<>();

            ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(factory.getProviderId());

            for (SocialConnection connection : connections)
            {
                connection.setAccessToken(decrypt(connection.getAccessToken()));
                connection.setSecret(decrypt(connection.getSecret()));
                connection.setRefreshToken(decrypt(connection.getRefreshToken()));
                rval.add((Connection<A>)connectionFactory.createConnection(SocialConnection.getBuilder(connection).buildConnectionData()));
            }
        }

        return rval;
    }

    /**
     *
     * @param multiValueMap
     * @return
     */
    @Override
    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> multiValueMap)
    {
        MultiValueMap<String, Connection<?>> rval = new LinkedMultiValueMap<>();

        //TODO:  Very inefficient.  Find a better way to do this.
        for (String providerId : multiValueMap.keySet())
        {
            ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(providerId);

            List<String> providerUserIds = multiValueMap.get(providerId);

            List<SocialConnection> connections = userAccessor.getSocialConnections(providerId,
                    providerUserIds, userId, IEnums.SEEK_MODE.FULL);

            decryptList(connections);

            List<Connection<?>> orderedConnections = new ArrayList<>();

            for (String orderedProviderUserId : providerUserIds)
            {
                Connection<?> convertedConnection = null;

                for (SocialConnection targetConnection : connections)
                {
                    if (targetConnection.getProviderUserId().equals(orderedProviderUserId))
                        convertedConnection = connectionFactory.createConnection(SocialConnection.getBuilder(targetConnection).buildConnectionData());
                }

                orderedConnections.add(convertedConnection);

            }

            rval.put(providerId, orderedConnections);
        }

        return rval;
    }

    /**
     *
     * @param connectionKey
     * @return
     */
    @Override
    public Connection<?> getConnection(ConnectionKey connectionKey)
    {
        SocialConnection socialConnection = userAccessor.getSocialConnection(connectionKey, userId, IEnums.SEEK_MODE.FULL);

        if (socialConnection == null)
            throw new NoSuchConnectionException(connectionKey);

        socialConnection.setAccessToken(decrypt(socialConnection.getAccessToken()));
        socialConnection.setSecret(decrypt(socialConnection.getSecret()));
        socialConnection.setRefreshToken(decrypt(socialConnection.getRefreshToken()));

        ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(socialConnection.getProviderId());
        return connectionFactory.createConnection(SocialConnection.getBuilder(socialConnection).buildConnectionData());
    }

    /**
     *
     * @param aClass
     * @param providerUserId
     * @param <A>
     * @return
     */
    @Override
    public <A> Connection<A> getConnection(Class<A> aClass, String providerUserId)
    {
        ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(aClass);
        String providerId = connectionFactory.getProviderId();

        ConnectionKey key = new ConnectionKey(providerId, providerUserId);

        SocialConnection connection = userAccessor.getSocialConnection(key, userId, IEnums.SEEK_MODE.FULL);

        if (connection == null)
            throw new NoSuchConnectionException(key);

        return (Connection<A>) connectionFactory.createConnection(SocialConnection.getBuilder(connection).buildConnectionData());
    }

    /**
     *
     * @param aClass
     * @param <A>
     * @return
     */
    @Override
    public <A> Connection<A> getPrimaryConnection(Class<A> aClass)
    {
        ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(aClass);

        List<Connection<?>> connectionsList = findConnections(connectionFactory.getProviderId());
        if (connectionsList == null || connectionsList.size() == 0)
            throw new NotConnectedException(connectionFactory.getProviderId());

        return (Connection<A>) connectionsList.get(0);
    }

    /**
     *
     * @param aClass
     * @param <A>
     * @return
     */
    @Override
    public <A> Connection<A> findPrimaryConnection(Class<A> aClass)
    {
        Connection<A> rval = null;

        ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(aClass);

        List<Connection<?>> connectionsList = findConnections(connectionFactory.getProviderId());

        if (connectionsList != null && connectionsList.size() > 0)
            rval = (Connection<A>) connectionsList.get(0);

        return rval;

    }

    /**
     *
     * @param connection
     */
    @Override
    public void addConnection(Connection<?> connection)
    {
        userAccessor.addUserSocialConnection(connection.createData(), userId);
    }

    /**
     *
     * @param connection
     */
    @Override
    public void updateConnection(Connection<?> connection)
    {
        userAccessor.updateUserSocialConnection(connection.createData());
    }

    /**
     *
     * @param providerId
     */
    @Override
    public void removeConnections(String providerId)
    {
        userAccessor.removeConnection(providerId, userId);
    }

    /**
     *
     * @param connectionKey
     */
    @Override
    public void removeConnection(ConnectionKey connectionKey)
    {
        userAccessor.removeConnection(connectionKey, userId);
    }

    /**
     *
     * @param encryptedText
     * @return
     */
    private String decrypt(String encryptedText)
    {
        return encryptedText != null ? textEncryptor.decrypt(encryptedText) : encryptedText;
    }

    /**
     *
     * @param connections
     */
    private void decryptList(List<SocialConnection> connections)
    {
        for (SocialConnection connection : connections)
        {
            connection.setAccessToken(decrypt(connection.getAccessToken()));
            connection.setSecret(decrypt(connection.getSecret()));
            connection.setRefreshToken(decrypt(connection.getRefreshToken()));
        }
    }
}
