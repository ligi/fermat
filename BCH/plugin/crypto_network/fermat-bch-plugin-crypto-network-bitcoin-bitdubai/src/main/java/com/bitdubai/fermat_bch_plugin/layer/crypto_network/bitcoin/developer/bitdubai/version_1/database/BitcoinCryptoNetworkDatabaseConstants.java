package com.bitdubai.fermat_bch_plugin.layer.crypto_network.bitcoin.developer.bitdubai.version_1.database;

/**
* The Class <code>com.bitdubai.fermat_bch_plugin.layer.cryptonetwork.bitcoin.developer.bitdubai.version_1.database.BitcoinCryptoNetworkDatabaseConstants</code>
* keeps constants the column names of the database.<p/>
* <p/>
*
* Created by Rodrigo Acosta - (acosta_rodrigo@hotmail.com) on 09/10/15.
*
* @version 1.0
* @since Java JDK 1.7
*/
class BitcoinCryptoNetworkDatabaseConstants {

    /**
     * Incoming_Transactions database table definition.
     */
    static final String INCOMING_TRANSACTIONS_TABLE_NAME = "incoming_transactions";

    static final String INCOMING_TRANSACTIONS_TRX_ID_COLUMN_NAME = "trx_id";
    static final String INCOMING_TRANSACTIONS_HASH_COLUMN_NAME = "hash";
    static final String INCOMING_TRANSACTIONS_CRYPTO_STATUS_COLUMN_NAME = "crypto_status";
    static final String INCOMING_TRANSACTIONS_BLOCK_DEPTH_COLUMN_NAME = "block_depth";
    static final String INCOMING_TRANSACTIONS_ADDRESS_TO_COLUMN_NAME = "address_to";
    static final String INCOMING_TRANSACTIONS_ADDRESS_FROM_COLUMN_NAME = "address_from";
    static final String INCOMING_TRANSACTIONS_VALUE_COLUMN_NAME = "value";
    static final String INCOMING_TRANSACTIONS_FEE_COLUMN_NAME = "fee";
    static final String INCOMING_TRANSACTIONS_TRANSMISSION_STATUS_COLUMN_NAME = "transmission_status";
    static final String INCOMING_TRANSACTIONS_LAST_UPDATE_COLUMN_NAME = "last_update";

    static final String INCOMING_TRANSACTIONS_FIRST_KEY_COLUMN = "trx_id";

    /**
     * Outgoing_Transactions database table definition.
     */
    static final String OUTGOING_TRANSACTIONS_TABLE_NAME = "outgoing_transactions";

    static final String OUTGOING_TRANSACTIONS_TRX_ID_COLUMN_NAME = "trx_id";
    static final String OUTGOING_TRANSACTIONS_HASH_COLUMN_NAME = "hash";
    static final String OUTGOING_TRANSACTIONS_CRYPTO_STATUS_COLUMN_NAME = "crypto_status";
    static final String OUTGOING_TRANSACTIONS_BLOCK_DEPTH_COLUMN_NAME = "block_depth";
    static final String OUTGOING_TRANSACTIONS_ADDRESS_TO_COLUMN_NAME = "address_to";
    static final String OUTGOING_TRANSACTIONS_ADDRESS_FROM_COLUMN_NAME = "address_from";
    static final String OUTGOING_TRANSACTIONS_VALUE_COLUMN_NAME = "value";
    static final String OUTGOING_TRANSACTIONS_FEE_COLUMN_NAME = "fee";
    static final String OUTGOING_TRANSACTIONS_TRANSMISSION_STATUS_COLUMN_NAME = "transmission_status";
    static final String OUTGOING_TRANSACTIONS_LAST_UPDATE_COLUMN_NAME = "last_update";

    static final String OUTGOING_TRANSACTIONS_FIRST_KEY_COLUMN = "trx_id";

    /**
     * CryptoVaults_Stats database table definition.
     */
    static final String CRYPTOVAULTS_STATS_TABLE_NAME = "cryptovaults_stats";

    static final String CRYPTOVAULTS_STATS_CRYPTO_VAULT_COLUMN_NAME = "crypto_vault";
    static final String CRYPTOVAULTS_STATS_LAST_CONNECTION_REQUEST_COLUMN_NAME = "last_connection_request";
    static final String CRYPTOVAULTS_STATS_MONITORED_PUBLICKEYS_COLUMN_NAME = "monitored_publickeys";

    static final String CRYPTOVAULTS_STATS_FIRST_KEY_COLUMN = "crypto_vault";

    /**
     * EventAgent_Stats database table definition.
     */
    static final String EVENTAGENT_STATS_TABLE_NAME = "eventagent_stats";

    static final String EVENTAGENT_STATS_EXECUTION_NUMBER_COLUMN_NAME = "execution_number";
    static final String EVENTAGENT_STATS_LAST_EXECUTION_DATE_COLUMN_NAME = "last_execution_date";
    static final String EVENTAGENT_STATS_PENDING_INCOMING_TRANSACTIONS_COLUMN_NAME = "pending_incoming_transactions";
    static final String EVENTAGENT_STATS_PENDING_OUTGOING_TRANSACTIONS_COLUMN_NAME = "pending_outgoing_transactions";

    static final String EVENTAGENT_STATS_FIRST_KEY_COLUMN = "execution_number";

}
