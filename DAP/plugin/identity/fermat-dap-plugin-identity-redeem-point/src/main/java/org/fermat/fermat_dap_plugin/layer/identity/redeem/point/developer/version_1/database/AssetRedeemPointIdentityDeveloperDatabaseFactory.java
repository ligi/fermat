package org.fermat.fermat_dap_plugin.layer.identity.redeem.point.developer.version_1.database;

import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by franklin on 02/11/15.
 */
public class AssetRedeemPointIdentityDeveloperDatabaseFactory {//implements DealsWithPluginDatabaseSystem, DealsWithPluginIdentity {

    /**
     * DealsWithPluginDatabaseSystem Interface member variables.
     */
    PluginDatabaseSystem pluginDatabaseSystem;

    /**
     * DealsWithPluginIdentity Interface member variables.
     */
    UUID pluginId;


    Database database;

    /**
     * Constructor
     *
     * @param pluginDatabaseSystem
     * @param pluginId
     */
    public AssetRedeemPointIdentityDeveloperDatabaseFactory(PluginDatabaseSystem pluginDatabaseSystem, UUID pluginId) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginId = pluginId;
    }

//    @Override
//    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
//        this.pluginDatabaseSystem = pluginDatabaseSystem;
//    }
//
//    @Override
//    public void setPluginId(UUID pluginId) {
//        this.pluginId = pluginId;
//    }

    /**
     * This method open or creates the database i'll be working with
     *
     * @throws org.fermat.fermat_dap_plugin.layer.identity.redeem.point.developer.version_1.exceptions.CantInitializeAssetRedeemPointIdentityDatabaseException
     */
    public void initializeDatabase() throws org.fermat.fermat_dap_plugin.layer.identity.redeem.point.developer.version_1.exceptions.CantInitializeAssetRedeemPointIdentityDatabaseException {
        try {

             /*
              * Open new database connection
              */
            database = this.pluginDatabaseSystem.openDatabase(pluginId, AssetRedeemPointIdentityDatabaseConstants.ASSET_REDEEM_POINT_IDENTITY_DB_NAME);
            database.closeDatabase();

        } catch (CantOpenDatabaseException cantOpenDatabaseException) {

             /*
              * The database exists but cannot be open. I can not handle this situation.
              */
            throw new org.fermat.fermat_dap_plugin.layer.identity.redeem.point.developer.version_1.exceptions.CantInitializeAssetRedeemPointIdentityDatabaseException(cantOpenDatabaseException.getMessage());

        } catch (DatabaseNotFoundException e) {

             /*
              * The database no exist may be the first time the plugin is running on this device,
              * We need to create the new database
              */
            AssetRedeemPointIdentityDatabaseFactory assetIssuerIdentityDatabaseFactory = new AssetRedeemPointIdentityDatabaseFactory(pluginDatabaseSystem);

            try {
                  /*
                   * We create the new database
                   */
                database = assetIssuerIdentityDatabaseFactory.createDatabase(pluginId);
                database.closeDatabase();
            } catch (CantCreateDatabaseException cantCreateDatabaseException) {
                  /*
                   * The database cannot be created. I can not handle this situation.
                   */
                throw new org.fermat.fermat_dap_plugin.layer.identity.redeem.point.developer.version_1.exceptions.CantInitializeAssetRedeemPointIdentityDatabaseException(cantCreateDatabaseException.getMessage());
            }
        } catch (Exception e) {

            throw new org.fermat.fermat_dap_plugin.layer.identity.redeem.point.developer.version_1.exceptions.CantInitializeAssetRedeemPointIdentityDatabaseException(e.getMessage());

        }
    }

    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        /**
         * I only have one database on my plugin. I will return its name.
         */
        List<DeveloperDatabase> databases = new ArrayList<>();
        databases.add(developerObjectFactory.getNewDeveloperDatabase(AssetRedeemPointIdentityDatabaseConstants.ASSET_REDEEM_POINT_IDENTITY_DB_NAME, this.pluginId.toString()));
        return databases;
    }

    public List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory) {
        List<DeveloperDatabaseTable> tables = new ArrayList<DeveloperDatabaseTable>();

        /**
         * Table Asset User columns.
         */
        List<String> AssetUserColumns = new ArrayList<String>();

        AssetUserColumns.add(AssetRedeemPointIdentityDatabaseConstants.ASSET_REDEEM_POINT_IDENTITY_PUBLIC_KEY_COLUMN_NAME);
        AssetUserColumns.add(AssetRedeemPointIdentityDatabaseConstants.ASSET_REDEEM_POINT_IDENTITY_ALIAS_COLUMN_NAME);
        AssetUserColumns.add(AssetRedeemPointIdentityDatabaseConstants.ASSET_REDEEM_POINT_IDENTITY_DEVICE_USER_PUBLIC_KEY_COLUMN_NAME);
        AssetUserColumns.add(AssetRedeemPointIdentityDatabaseConstants.ASSET_REDEEM_POINT_IDENTITY_CONTACT_INFORMATION_COLUMN_NAME);
        AssetUserColumns.add(AssetRedeemPointIdentityDatabaseConstants.ASSET_REDEEM_POINT_IDENTITY_ADDRESS_COUNTRY_NAME_COLUMN_NAME);
        AssetUserColumns.add(AssetRedeemPointIdentityDatabaseConstants.ASSET_REDEEM_POINT_IDENTITY_ADDRESS_PROVINCE_NAME_COLUMN_NAME);
        AssetUserColumns.add(AssetRedeemPointIdentityDatabaseConstants.ASSET_REDEEM_POINT_IDENTITY_ADDRESS_CITY_NAME_COLUMN_NAME);
        AssetUserColumns.add(AssetRedeemPointIdentityDatabaseConstants.ASSET_REDEEM_POINT_IDENTITY_ADDRESS_POSTAL_CODE_COLUMN_NAME);
        AssetUserColumns.add(AssetRedeemPointIdentityDatabaseConstants.ASSET_REDEEM_POINT_IDENTITY_ADDRESS_STREET_NAME_COLUMN_NAME);
        AssetUserColumns.add(AssetRedeemPointIdentityDatabaseConstants.ASSET_REDEEM_POINT_IDENTITY_ADDRESS_HOUSE_NUMBER_COLUMN_NAME);
        /**
         * Table Asset User addition.
         */
        DeveloperDatabaseTable assetUserTable = developerObjectFactory.getNewDeveloperDatabaseTable(AssetRedeemPointIdentityDatabaseConstants.ASSET_REDEEM_POINT_IDENTITY_TABLE_NAME, AssetUserColumns);
        tables.add(assetUserTable);


        return tables;
    }

    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory, DeveloperDatabaseTable developerDatabaseTable) {
        /**
         * Will get the records for the given table
         */
        List<DeveloperDatabaseTableRecord> returnedRecords = new ArrayList<DeveloperDatabaseTableRecord>();
        /**
         * I load the passed table name from the SQLite database.
         */
        DatabaseTable selectedTable = database.getTable(developerDatabaseTable.getName());
        try {
            selectedTable.loadToMemory();
            List<DatabaseTableRecord> records = selectedTable.getRecords();
            for (DatabaseTableRecord row : records) {
                List<String> developerRow = new ArrayList<String>();
                /**
                 * for each row in the table list
                 */
                for (DatabaseRecord field : row.getValues()) {
                    /**
                     * I get each row and save them into a List<String>
                     */
                    developerRow.add(field.getValue());
                }
                /**
                 * I create the Developer Database record
                 */
                returnedRecords.add(developerObjectFactory.getNewDeveloperDatabaseTableRecord(developerRow));
            }
            /**
             * return the list of DeveloperRecords for the passed table.
             */
        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
            /**
             * if there was an error, I will returned an empty list.
             */
            database.closeDatabase();
            return returnedRecords;
        } catch (Exception e) {
            database.closeDatabase();
            return returnedRecords;
        }
        database.closeDatabase();
        return returnedRecords;
    }
}
