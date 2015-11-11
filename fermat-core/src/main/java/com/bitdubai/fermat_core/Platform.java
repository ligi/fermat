/*
 * @#Platform.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_core;

import com.bitdubai.fermat_api.Addon;
import com.bitdubai.fermat_api.CantInitializePluginsManagerException;
import com.bitdubai.fermat_api.CantReportCriticalStartingProblemException;
import com.bitdubai.fermat_api.CantStartPlatformException;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.Plugin;
import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer.CantStartLayerException;
import com.bitdubai.fermat_api.layer.PlatformLayer;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.CantGetAddonException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.VersionNotFoundException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.AddonVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.developer.DatabaseManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.developer.DealWithDatabaseManagers;
import com.bitdubai.fermat_api.layer.all_definition.developer.DealsWithLogManagers;
import com.bitdubai.fermat_api.layer.all_definition.developer.LogManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Developers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.PlatformComponents;
import com.bitdubai.fermat_api.layer.all_definition.enums.PlatformLayers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.util.ObjectSizeFetcher;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.dmp_identity.designer.interfaces.DealsWithIdentityDesigner;
import com.bitdubai.fermat_api.layer.dmp_identity.designer.interfaces.DesignerIdentityManager;
import com.bitdubai.fermat_api.layer.dmp_identity.translator.interfaces.DealsWithIdentityTranslator;
import com.bitdubai.fermat_api.layer.dmp_identity.translator.interfaces.TranslatorIdentityManager;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.DealsWithPluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PlatformFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.location_system.DealsWithDeviceLocation;
import com.bitdubai.fermat_api.layer.osa_android.location_system.LocationManager;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.DealsWithLogger;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.interfaces.BitcoinNetworkManager;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.interfaces.DealsWithBitcoinNetwork;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.asset_vault.interfaces.AssetVaultManager;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.asset_vault.interfaces.DealsWithAssetVault;
import com.bitdubai.fermat_cbp_api.layer.cbp_identity.crypto_broker.interfaces.CryptoBrokerIdentityManager;
import com.bitdubai.fermat_cbp_api.layer.cbp_identity.crypto_broker.interfaces.DealsWithCryptoBrokerIdentities;
import com.bitdubai.fermat_cbp_api.layer.cbp_identity.crypto_customer.interfaces.CryptoCustomerIdentityManager;
import com.bitdubai.fermat_cbp_api.layer.cbp_identity.crypto_customer.interfaces.DealsWithCryptoCustomerIdentities;
import com.bitdubai.fermat_ccp_api.layer.actor.extra_user.interfaces.DealsWithExtraUsers;
import com.bitdubai.fermat_ccp_api.layer.actor.extra_user.interfaces.ExtraUserManager;
import com.bitdubai.fermat_ccp_api.layer.actor.intra_user.interfaces.DealsWithCCPActorIntraWalletUsers;
import com.bitdubai.fermat_ccp_api.layer.actor.intra_user.interfaces.IntraWalletUserActorManager;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletManager;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.bitcoin_wallet.interfaces.DealsWithBitcoinWallet;
import com.bitdubai.fermat_ccp_api.layer.transaction.outgoing_intra_actor.interfaces.DealsWithOutgoingIntraActor;
import com.bitdubai.fermat_ccp_api.layer.transaction.outgoing_intra_actor.interfaces.OutgoingIntraActorManager;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.crypto_wallet.interfaces.CryptoWalletManager;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.crypto_wallet.interfaces.DealsWithWalletModuleCryptoWallet;
import com.bitdubai.fermat_core.layer.all_definition.DefinitionLayer;
import com.bitdubai.fermat_core.layer.dap_middleware.DAPMiddlewareLayer;
import com.bitdubai.fermat_core.layer.dap_module.DAPModuleLayer;
import com.bitdubai.fermat_core.layer.dap_network_service.DAPNetworkServiceLayer;
import com.bitdubai.fermat_core.layer.dap_sub_app_module.DAPSubAppModuleLayer;
import com.bitdubai.fermat_core.layer.dap_transaction.DAPTransactionLayer;
import com.bitdubai.fermat_core.layer.dmp_agent.AgentLayer;
import com.bitdubai.fermat_core.layer.dmp_identity.IdentityLayer;
import com.bitdubai.fermat_core.layer.dmp_middleware.MiddlewareLayer;
import com.bitdubai.fermat_core.layer.dmp_module.ModuleLayer;
import com.bitdubai.fermat_core.layer.dmp_world.WorldLayer;
import com.bitdubai.fermat_core.layer.p2p_communication.CommunicationLayer;
import com.bitdubai.fermat_core.layer.pip_actor.ActorLayer;
import com.bitdubai.fermat_core.layer.pip_engine.EngineLayer;
import com.bitdubai.fermat_core.layer.wpd.actor.WPDActorLayer;
import com.bitdubai.fermat_core.layer.wpd.engine.WPDEngineLayer;
import com.bitdubai.fermat_core.layer.wpd.identity.WPDIdentityLayer;
import com.bitdubai.fermat_core.layer.wpd.middleware.WPDMiddlewareLayer;
import com.bitdubai.fermat_core.layer.wpd.network_service.WPDNetworkServiceLayer;
import com.bitdubai.fermat_core.layer.wpd.sub_app_module.WPDSubAppModuleLayer;
import com.bitdubai.fermat_cry_api.layer.crypto_module.crypto_address_book.interfaces.CryptoAddressBookManager;
import com.bitdubai.fermat_cry_api.layer.crypto_module.crypto_address_book.interfaces.DealsWithCryptoAddressBook;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.CryptoVaultManager;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.DealsWithCryptoVault;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuerManager;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.DealsWithActorAssetIssuer;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_user.interfaces.ActorAssetUserManager;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_user.interfaces.DealsWithActorAssetUser;
import com.bitdubai.fermat_dap_api.layer.dap_actor.redeem_point.interfaces.ActorAssetRedeemPointManager;
import com.bitdubai.fermat_dap_api.layer.dap_actor.redeem_point.interfaces.DealsWithActorAssetRedeemPoint;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_issuer.interfaces.AssetIssuerActorNetworkServiceManager;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_issuer.interfaces.DealsWithAssetIssuerActorNetworkServiceManager;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_user.interfaces.AssetUserActorNetworkServiceManager;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_user.interfaces.DealsWithAssetUserActorNetworkServiceManager;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.redeem_point.interfaces.AssetRedeemPointActorNetworkServiceManager;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.redeem_point.interfaces.DealsWithAssetRedeemPointActorNetworkServiceManager;
import com.bitdubai.fermat_dap_api.layer.dap_identity.asset_issuer.interfaces.DealsWithIdentityAssetIssuer;
import com.bitdubai.fermat_dap_api.layer.dap_identity.asset_issuer.interfaces.IdentityAssetIssuerManager;
import com.bitdubai.fermat_dap_api.layer.dap_identity.asset_user.interfaces.DealsWithIdentityAssetUser;
import com.bitdubai.fermat_dap_api.layer.dap_identity.asset_user.interfaces.IdentityAssetUserManager;
import com.bitdubai.fermat_dap_api.layer.dap_identity.redeem_point.interfaces.DealsWithIdentityAssetRedeemPoint;
import com.bitdubai.fermat_dap_api.layer.dap_identity.redeem_point.interfaces.RedeemPointIdentityManager;
import com.bitdubai.fermat_dap_api.layer.dap_middleware.dap_asset_factory.interfaces.AssetFactoryManager;
import com.bitdubai.fermat_dap_api.layer.dap_middleware.dap_asset_factory.interfaces.DealsWithAssetFactory;
import com.bitdubai.fermat_dap_api.layer.dap_module.asset_factory.interfaces.AssetFactoryModuleManager;
import com.bitdubai.fermat_dap_api.layer.dap_module.asset_factory.interfaces.DealsWithModuleAseetFactory;
import com.bitdubai.fermat_dap_api.layer.dap_network_services.asset_transmission.interfaces.AssetTransmissionNetworkServiceManager;
import com.bitdubai.fermat_dap_api.layer.dap_network_services.asset_transmission.interfaces.DealsWithAssetTransmissionNetworkServiceManager;
import com.bitdubai.fermat_dap_api.layer.dap_sub_app_module.asset_issuer_community.interfaces.AssetIssuerCommunitySubAppModuleManager;
import com.bitdubai.fermat_dap_api.layer.dap_sub_app_module.asset_issuer_community.interfaces.DealsWithAssetIssuerCommunitySubAppModule;
import com.bitdubai.fermat_dap_api.layer.dap_sub_app_module.asset_user_community.interfaces.AssetUserCommunitySubAppModuleManager;
import com.bitdubai.fermat_dap_api.layer.dap_sub_app_module.asset_user_community.interfaces.DealsWithAssetUserCommunitySubAppModule;
import com.bitdubai.fermat_dap_api.layer.dap_sub_app_module.redeem_point_community.interfaces.DealsWithRedeemPointCommunitySubAppModule;
import com.bitdubai.fermat_dap_api.layer.dap_sub_app_module.redeem_point_community.interfaces.RedeemPointCommunitySubAppModuleManager;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.asset_appropriation.interfaces.AssetAppropriationManager;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.asset_appropriation.interfaces.DealsWithAssetAppropriation;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.asset_distribution.interfaces.AssetDistributionManager;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.asset_distribution.interfaces.DealsWithAssetDistribution;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.asset_issuing.interfaces.AssetIssuingManager;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.asset_issuing.interfaces.DealsWithAssetIssuing;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.asset_reception.interfaces.AssetReceptionManager;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.asset_reception.interfaces.DealsWithAssetReception;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.interfaces.AssetIssuerWalletManager;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.interfaces.DealsWithAssetIssuerWallet;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_redeem_point.interfaces.AssetRedeemPointWalletManager;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_redeem_point.interfaces.DealsWithAssetRedeemPointWallet;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_user_wallet.interfaces.AssetUserWalletManager;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_user_wallet.interfaces.DealsWithAssetUserWallet;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.CommunicationLayerManager;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.DealsWithCommunicationLayerManager;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.DealsWithWsCommunicationsCloudClientManager;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.WsCommunicationsCloudClientManager;
import com.bitdubai.fermat_pip_api.layer.pip_module.developer.interfaces.DealsWithDeveloperModule;
import com.bitdubai.fermat_pip_api.layer.pip_module.developer.interfaces.DeveloperModuleManager;
import com.bitdubai.fermat_pip_api.layer.pip_user.device_user.interfaces.DealsWithDeviceUser;
import com.bitdubai.fermat_pip_api.layer.pip_user.device_user.interfaces.DeviceUserManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.UnexpectedPlatformExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.DealsWithEvents;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.platform_info.interfaces.DealsWithPlatformInfo;
import com.bitdubai.fermat_pip_api.layer.platform_service.platform_info.interfaces.PlatformInfoManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_identity.publisher.interfaces.DealsWithPublisherIdentity;
import com.bitdubai.fermat_wpd_api.layer.wpd_identity.publisher.interfaces.PublisherIdentityManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_factory.interfaces.DealsWithWalletFactory;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_factory.interfaces.WalletFactoryProjectManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_manager.interfaces.DealsWithWalletManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_manager.interfaces.WalletManagerManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_publisher.interfaces.DealsWithWalletPublisherMiddlewarePlugin;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_publisher.interfaces.WalletPublisherMiddlewarePlugin;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_settings.interfaces.DealsWithWalletSettings;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_settings.interfaces.WalletSettingsManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_store.interfaces.DealsWithWalletStoreMiddleware;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_store.interfaces.WalletStoreManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_network_service.wallet_resources.interfaces.DealsWithWalletResources;
import com.bitdubai.fermat_wpd_api.layer.wpd_network_service.wallet_resources.interfaces.WalletResourcesInstalationManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_network_service.wallet_statistics.interfaces.DealsWithWalletStatisticsNetworkService;
import com.bitdubai.fermat_wpd_api.layer.wpd_network_service.wallet_statistics.interfaces.WalletStatisticsManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_network_service.wallet_store.interfaces.DealsWithWalletStoreNetworkService;
import com.bitdubai.fermat_wpd_api.layer.wpd_sub_app_module.wallet_publisher.interfaces.DealsWithWalletPublisherModule;
import com.bitdubai.fermat_wpd_api.layer.wpd_sub_app_module.wallet_publisher.interfaces.WalletPublisherModuleManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_sub_app_module.wallet_store.interfaces.DealsWithWalletStoreModule;
import com.bitdubai.fermat_wpd_api.layer.wpd_sub_app_module.wallet_store.interfaces.WalletStoreModuleManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

;

/**
 * The Class <code>com.bitdubai.fermat_core.Platform</code> start all
 * component of the platform and manage it
 * <p/>
 * <p/>
 * Created by ciencias on 20/01/15.
 * Update by Roberto Requena - (rart3001@gmail.com) on 24/07/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */

public class Platform implements Serializable {

    /**
     * Represent the Logger
     */
    private static Logger LOG = Logger.getGlobal();

    /**
     * Represent the dealsWithDatabaseManagersPlugins
     */
    private Map<Plugins, Plugin> dealsWithDatabaseManagersPlugins;

    /**
     * Represent the dealsWithLogManagersPlugins
     */
    private Map<Plugins, Plugin> dealsWithLogManagersPlugins;

    /**
     * Represent the dealsWithDatabaseManagersAddons
     */
    private Map<Addons, Addon> dealsWithDatabaseManagersAddons;

    /**
     * Represent the dealsWithLogManagersAddons
     */
    private Map<Addons, Addon> dealsWithLogManagersAddons;

    /**
     * Represent the pluginsIdentityManager
     */
    private PluginsIdentityManager pluginsIdentityManager;

    /**
     * Represent the corePlatformContext
     */
    private CorePlatformContext corePlatformContext;

    private FermatSystem fermatSystem;

    public void setFermatSystem(FermatSystem fermatSystem) {
        this.fermatSystem = fermatSystem;
    }

    /**
     * Constructor
     */
    public Platform() {
        corePlatformContext = new CorePlatformContext();

        dealsWithDatabaseManagersPlugins = new ConcurrentHashMap<>();
        dealsWithLogManagersPlugins = new ConcurrentHashMap<>();
        dealsWithDatabaseManagersAddons = new ConcurrentHashMap<>();
        dealsWithLogManagersAddons = new ConcurrentHashMap<>();

    }

    /**
     * Somebody is starting the com.bitdubai.platform. The com.bitdubai.platform is portable. That somebody is OS dependent and has access to
     * the OS. I have to transport a reference to that somebody to the OS subsystem in other to allow it to access
     * the OS through this reference.
     */

    /**
     * Method that start the platform
     *
     * @throws CantStartPlatformException
     * @throws CantReportCriticalStartingProblemException
     */
    public void start() throws CantStartPlatformException, CantReportCriticalStartingProblemException {

        LOG.info("Platform - start()");

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         * ------------------------------------------------------------------------------------------------------------*
         * Layers initialization                                                                                       *
         * ------------------------------------------------------------------------------------------------------------*
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
        initializePlatformLayers();

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         * ------------------------------------------------------------------------------------------------------------*
         * Plugin initialization                                                                                       *
         * ------------------------------------------------------------------------------------------------------------*
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
         initializePlugins();

        /*
         * Check addon for developer interfacesque 
         */
        for (Addons registeredDescriptor : corePlatformContext.getRegisteredAddonskeys()) {
            checkAddonForDeveloperInterfaces(registeredDescriptor);
        }

        /*
         * Check plugin for developer interfaces
         */
        for (Plugins registeredDescriptor : corePlatformContext.getRegisteredPluginskeys()) {
            checkPluginForDeveloperInterfaces(registeredDescriptor);
        }


        /**
         * Set Actor developer like manager
         */

        ((DealWithDatabaseManagers) corePlatformContext.getPlugin(Plugins.BITDUBAI_DEVELOPER_MODULE)).setDatabaseManagers(dealsWithDatabaseManagersPlugins, dealsWithDatabaseManagersAddons);
        ((DealsWithLogManagers) corePlatformContext.getPlugin(Plugins.BITDUBAI_DEVELOPER_MODULE)).setLogManagers(dealsWithLogManagersPlugins, dealsWithLogManagersAddons);

        //TODO: NO LO SIGAN PONIENDO QUE ME TIRA ABAJO LA APP. CUANDO LO SUBAN SAQUENLO COMO YO SACO LA CAPA P2P. Gracias XXOO Mati.
//        System.err.println("Tamaño de Platform en runtime es: " + (RamUsageEstimator.sizeOf(this)/1024/1024) + " MB");
    }

    /**
     * Method tha initialize all platform layer component
     */
    private void initializePlatformLayers() throws CantReportCriticalStartingProblemException {

        LOG.info("Platform - initializing Platform Layers ...");

        /**
         * Here I will be starting all the platforms layers. It is required that none of them fails. That does not mean
         * that a layer will have at least one service to offer. It depends on each layer. If one believes its lack of
         * services prevent the whole platform to start, then it will throw an exception that will effectively prevent
         * the platform to start.
         */
        try {


            /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
             * ------------------------------------------------------------------------------------------------------------*
             * Critical Layer initialization                                                                               *
             * ------------------------------------------------------------------------------------------------------------*
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

            /*
             * Create and star Definition Layer
             */
            PlatformLayer mDefinitionLayer = new DefinitionLayer();
            mDefinitionLayer.start();

            /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
             * ------------------------------------------------------------------------------------------------------------*
             * Other Layer initialization                                                                                  *
             * ------------------------------------------------------------------------------------------------------------*
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

            /*
             * Created and register into the platform context
             */

            corePlatformContext.registerPlatformLayer(new WorldLayer(), PlatformLayers.BITDUBAI_WORLD_LAYER);
            corePlatformContext.registerPlatformLayer(new CommunicationLayer(), PlatformLayers.BITDUBAI_COMMUNICATION_LAYER);
            corePlatformContext.registerPlatformLayer(new MiddlewareLayer(), PlatformLayers.BITDUBAI_MIDDLEWARE_LAYER);
            corePlatformContext.registerPlatformLayer(new ModuleLayer(), PlatformLayers.BITDUBAI_MODULE_LAYER);
            corePlatformContext.registerPlatformLayer(new AgentLayer(), PlatformLayers.BITDUBAI_AGENT_LAYER);
            corePlatformContext.registerPlatformLayer(new ActorLayer(), PlatformLayers.BITDUBAI_PIP_ACTOR_LAYER);
            corePlatformContext.registerPlatformLayer(new com.bitdubai.fermat_core.layer.pip_module.ModuleLayer(), PlatformLayers.BITDUBAI_PIP_MODULE_LAYER);
            corePlatformContext.registerPlatformLayer(new com.bitdubai.fermat_core.layer.pip_network_service.NetworkServiceLayer(), PlatformLayers.BITDUBAI_PIP_NETWORK_SERVICE_LAYER);
            corePlatformContext.registerPlatformLayer(new com.bitdubai.fermat_core.layer.pip_Identity.IdentityLayer(), PlatformLayers.BITDUBAI_PIP_IDENTITY_LAYER);
            corePlatformContext.registerPlatformLayer(new IdentityLayer(), PlatformLayers.BITDUBAI_IDENTITY_LAYER);
            corePlatformContext.registerPlatformLayer(new EngineLayer(), PlatformLayers.BITDUBAI_ENGINE_LAYER);

            // Init CCM Layers


            // Init DAP Layers
            corePlatformContext.registerPlatformLayer(new DAPMiddlewareLayer(), PlatformLayers.BITDUBAI_DAP_MIDDLEWARE_LAYER);
            corePlatformContext.registerPlatformLayer(new DAPModuleLayer(), PlatformLayers.BITDUBAI_DAP_MODULE_LAYER);
            corePlatformContext.registerPlatformLayer(new DAPSubAppModuleLayer(), PlatformLayers.BITDUBAI_DAP_SUB_APP_MODULE_LAYER);
            corePlatformContext.registerPlatformLayer(new DAPNetworkServiceLayer(), PlatformLayers.BITDUBAI_DAP_NETWORK_SERVICE_LAYER);
            corePlatformContext.registerPlatformLayer(new DAPTransactionLayer(), PlatformLayers.BITDUBAI_DAP_TRANSACTION_LAYER);
            // End  DAP Layers

            // Init WPD Layers
            corePlatformContext.registerPlatformLayer(new WPDActorLayer(), PlatformLayers.BITDUBAI_WPD_ACTOR_LAYER);
            corePlatformContext.registerPlatformLayer(new WPDEngineLayer(), PlatformLayers.BITDUBAI_WPD_ENGINE_LAYER);
            corePlatformContext.registerPlatformLayer(new WPDIdentityLayer(), PlatformLayers.BITDUBAI_WPD_IDENTITY_LAYER);
            corePlatformContext.registerPlatformLayer(new WPDMiddlewareLayer(), PlatformLayers.BITDUBAI_WPD_MIDDLEWARE_LAYER);
            corePlatformContext.registerPlatformLayer(new WPDNetworkServiceLayer(), PlatformLayers.BITDUBAI_WPD_NETWORK_SERVICE_LAYER);
            corePlatformContext.registerPlatformLayer(new WPDSubAppModuleLayer(), PlatformLayers.BITDUBAI_WPD_SUB_APP_MODULE_LAYER);
            // End  WPD Layers

            /*
             * Start all other platform layers
             */
            for (PlatformLayers key : corePlatformContext.getRegisteredPlatformLayerskeys()) {

                /*
                 * Get the platformLayer  by key
                 */
                PlatformLayer platformLayer = corePlatformContext.getPlatformLayer(key);

                /*
                 * If not null
                 */
                if (platformLayer != null) {

                    /*
                     * Start the layer
                     */
                    platformLayer.start();
                }
            }

            /*
             * Register the critical layer after the other layer are started
             * to prevent start again the critical layer in the previous code
             */
            corePlatformContext.registerPlatformLayer(mDefinitionLayer, PlatformLayers.BITDUBAI_DEFINITION_LAYER);

        } catch (CantStartLayerException cantStartLayerException) {

            LOG.log(Level.SEVERE, cantStartLayerException.getLocalizedMessage());

            /**
             * At this point the platform not only can not be started but also the problem can not be reported. That is
             * the reason why I am going to throw a special exception in order to alert the situation to whoever is running
             * the GUI. In this way it can alert the end user of what is going on and provide them with some information.
             * * * *
             */
            throw new CantReportCriticalStartingProblemException();
        }

    }

    /**
     * Method tha initialize all addons component
     */
    private void initializePlugins() throws CantStartPlatformException {

        LOG.info("Platform - initializing Plugins ...");

        try {

            /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
             * ------------------------------------------------------------------------------------------------------------*
             * Critical Plugins initialization                                                                             *
             * ------------------------------------------------------------------------------------------------------------*
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

            /*
             * PluginsIdentityManager
             * -----------------------
             *
             * Initialize the Plugin Identity Manager, the one who assigns identities to each plug in.
             */

            try {

                pluginsIdentityManager = new PluginsIdentityManager((PlatformFileSystem)fermatSystem.getAddon(ref(Platforms.OPERATIVE_SYSTEM_API, Layers.SYSTEM, Addons.PLATFORM_FILE_SYSTEM)));
            } catch (CantGetAddonException | VersionNotFoundException e ) {
                System.out.println("No sepué asiná el plato file sitem loco");
                System.out.println(e);
            }

             /* flag temporal para desactivar plugins que tarde demasiado en inicializar,
            y asi poder trabajar en otras partes del sistema de forma relativamente rapida */
            boolean BCH = true;
            boolean BNK = true;
            boolean BNP = true;
            boolean CBP = true;
            boolean CCM = false;
            boolean CCP = true;
            boolean CRY = true;
            boolean CSH = true;
            boolean DAP = true; /* DAP no da errores al iniciar, si la desactivas enviar mensaje a Rodrigo por favor*/
            boolean DMP = true;//DOBLEMENTE TEMPORAL
            boolean MKT = true;
            boolean OSA = true;
            boolean P2P = true;
            boolean PIP = true;
            boolean SHP = true;
            boolean WPD = true;

            //TODO: Esto va acá porque es necesario para que se le pase la instancia a otros plugins

            // addons initializing

            List<AddonVersionReference> addonsToInstantiate = new ArrayList<>();
            Map<PluginVersionReference, Plugins> pluginsToInstantiate = new HashMap<>();

            addonsToInstantiate.add(ref(Platforms.PLUG_INS_PLATFORM   , Layers.PLATFORM_SERVICE, Addons.ERROR_MANAGER));
            addonsToInstantiate.add(ref(Platforms.PLUG_INS_PLATFORM   , Layers.PLATFORM_SERVICE, Addons.EVENT_MANAGER));
            addonsToInstantiate.add(ref(Platforms.PLUG_INS_PLATFORM   , Layers.USER            , Addons.DEVICE_USER));
            addonsToInstantiate.add(ref(Platforms.PLUG_INS_PLATFORM   , Layers.PLATFORM_SERVICE, Addons.PLATFORM_INFO));
            addonsToInstantiate.add(ref(Platforms.OPERATIVE_SYSTEM_API, Layers.SYSTEM          , Addons.PLATFORM_DATABASE_SYSTEM));
            addonsToInstantiate.add(ref(Platforms.OPERATIVE_SYSTEM_API, Layers.SYSTEM          , Addons.PLUGIN_DATABASE_SYSTEM));
            addonsToInstantiate.add(ref(Platforms.OPERATIVE_SYSTEM_API, Layers.SYSTEM          , Addons.PLATFORM_FILE_SYSTEM));
            addonsToInstantiate.add(ref(Platforms.OPERATIVE_SYSTEM_API, Layers.SYSTEM          , Addons.PLUGIN_FILE_SYSTEM));
            addonsToInstantiate.add(ref(Platforms.OPERATIVE_SYSTEM_API, Layers.SYSTEM          , Addons.DEVICE_LOCATION));
            addonsToInstantiate.add(ref(Platforms.OPERATIVE_SYSTEM_API, Layers.SYSTEM          , Addons.LOG_MANAGER));

            pluginsToInstantiate.put(ref(Platforms.BLOCKCHAINS, Layers.CRYPTO_MODULE, Plugins.CRYPTO_ADDRESS_BOOK), Plugins.BITDUBAI_CRYPTO_ADDRESS_BOOK);

            if (PIP) {
           /*
            * Plugin Actor Developer
            * -----------------------------
            */
                Plugin actorDeveloper = ((ActorLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_PIP_ACTOR_LAYER)).getActorDeveloper();
                injectPluginReferencesAndStart(actorDeveloper, Plugins.BITDUBAI_ACTOR_DEVELOPER);

           /*
            * Plugin Developer Module
            * -----------------------------
            */
                Plugin developerModule = ((com.bitdubai.fermat_core.layer.pip_module.ModuleLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_PIP_MODULE_LAYER)).getmDeveloperModule();
                injectPluginReferencesAndStart(developerModule, Plugins.BITDUBAI_DEVELOPER_MODULE);




           /*
            * Plugin Intra User PIP
            * -------------------------------
            */
                Plugin intraUser = ((ActorLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_PIP_ACTOR_LAYER)).getActorDeveloper();
                injectPluginReferencesAndStart(intraUser, Plugins.BITDUBAI_USER_INTRA_USER);

           /*
            * Plugin Desktop runtime PIP
            * -----------------------------
            */
                Plugin desktopRuntimePlugin = ((EngineLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_ENGINE_LAYER)).getmDesktopRuntimePlugin();
                injectPluginReferencesAndStart(desktopRuntimePlugin, Plugins.BITDUBAI_DESKTOP_RUNTIME);

            /*
             * Plugin Wallet Runtime
             * -----------------------------
             */
                Plugin subAppResourcesNetworkService = ((com.bitdubai.fermat_core.layer.pip_network_service.NetworkServiceLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_PIP_NETWORK_SERVICE_LAYER)).getSubAppResources();
                injectPluginReferencesAndStart(subAppResourcesNetworkService, Plugins.BITDUBAI_SUBAPP_RESOURCES_NETWORK_SERVICE);

            /*
             * Plugin Developer Identity
             * -----------------------------
             */
                Plugin developerIdentity = ((com.bitdubai.fermat_core.layer.pip_Identity.IdentityLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_PIP_IDENTITY_LAYER)).getDeveloperIdentity();
                injectPluginReferencesAndStart(developerIdentity, Plugins.BITDUBAI_DEVELOPER_IDENTITY);

            }



            if (CRY) {

                // disabled since we are migrating back to the new crypto network plugin

                pluginsToInstantiate.put(ref(Platforms.BLOCKCHAINS, Layers.CRYPTO_NETWORK, Plugins.BITDUBAI_BITCOIN_CRYPTO_NETWORK), Plugins.BITDUBAI_BITCOIN_CRYPTO_NETWORK);

                pluginsToInstantiate.put(ref(Platforms.BLOCKCHAINS, Layers.CRYPTO_NETWORK, Plugins.BITCOIN_NETWORK), Plugins.BITDUBAI_BITCOIN_CRYPTO_NETWORK2);

                pluginsToInstantiate.put(ref(Platforms.BLOCKCHAINS, Layers.CRYPTO_VAULT, Plugins.BITCOIN_VAULT), Plugins.BITDUBAI_BITCOIN_CRYPTO_VAULT);

                pluginsToInstantiate.put(ref(Platforms.BLOCKCHAINS, Layers.CRYPTO_VAULT, Plugins.BITCOIN_ASSET_VAULT), Plugins.BITDUBAI_ASSETS_CRYPTO_VAULT);
            }
            /*
            * Plugin Wallet Manager Middleware
            * ----------------------------------
            */

            pluginsToInstantiate.put(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.MIDDLEWARE, Plugins.WALLET_MANAGER), Plugins.BITDUBAI_WPD_WALLET_MANAGER_MIDDLEWARE);

            pluginsToInstantiate.put(ref(Platforms.BLOCKCHAINS, Layers.CRYPTO_ROUTER, Plugins.INCOMING_CRYPTO), Plugins.BITDUBAI_INCOMING_CRYPTO_TRANSACTION);

            if (CCP) {

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.BASIC_WALLET   , Plugins.BITCOIN_WALLET        ), Plugins.BITDUBAI_BITCOIN_WALLET_BASIC_WALLET);

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.NETWORK_SERVICE, Plugins.CRYPTO_PAYMENT_REQUEST), Plugins.BITDUBAI_CCP_CRYPTO_PAYMENT_REQUEST_NETWORK_SERVICE);

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.NETWORK_SERVICE, Plugins.INTRA_WALLET_USER     ), Plugins.BITDUBAI_INTRAUSER_NETWORK_SERVICE);

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.NETWORK_SERVICE, Plugins.CRYPTO_TRANSMISSION   ), Plugins.BITDUBAI_CCP_CRYPTO_CRYPTO_TRANSMISSION_NETWORK_SERVICE);

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.NETWORK_SERVICE, Plugins.CRYPTO_ADDRESSES      ), Plugins.BITDUBAI_CCP_CRYPTO_ADDRESSES_NETWORK_SERVICE);

                pluginsToInstantiate.put(ref(Platforms.BLOCKCHAINS             , Layers.MIDDLEWARE     , Plugins.CRYPTO_ADDRESSES      ), Plugins.CRYPTO_ADDRESSES__MIDDLEWARE_TEMP);

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.MIDDLEWARE     , Plugins.WALLET_CONTACTS       ), Plugins.BITDUBAI_CCP_WALLET_CONTACTS_MIDDLEWARE);

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.REQUEST        , Plugins.CRYPTO_PAYMENT_REQUEST), Plugins.BITDUBAI_CCP_CRYPTO_PAYMENT_REQUEST);

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.IDENTITY       , Plugins.INTRA_WALLET_USER     ), Plugins.BITDUBAI_CCP_INTRA_USER_IDENTITY);

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.TRANSACTION    , Plugins.OUTGOING_INTRA_ACTOR  ), Plugins.BITDUBAI_CCP_OUTGOING_INTRA_ACTOR_TRANSACTION);

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.TRANSACTION    , Plugins.OUTGOING_EXTRA_USER   ), Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION);

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.TRANSACTION    , Plugins.INCOMING_EXTRA_USER   ), Plugins.BITDUBAI_INCOMING_EXTRA_USER_TRANSACTION);

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.TRANSACTION    , Plugins.INCOMING_INTRA_USER   ), Plugins.BITDUBAI_INCOMING_INTRA_USER_TRANSACTION);

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.ACTOR          , Plugins.EXTRA_WALLET_USER     ), Plugins.BITDUBAI_ACTOR_EXTRA_USER);

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.ACTOR          , Plugins.INTRA_WALLET_USER     ), Plugins.BITDUBAI_CCM_INTRA_WALLET_USER_ACTOR);

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.SUB_APP_MODULE , Plugins.INTRA_WALLET_USER     ), Plugins.BITDUBAI_INTRA_USER_FACTORY_MODULE);

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.WALLET_MODULE  , Plugins.CRYPTO_WALLET         ), Plugins.BITDUBAI_CRYPTO_WALLET_WALLET_MODULE);

            }

            pluginsToInstantiate.put(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.DESKTOP_MODULE, Plugins.WALLET_MANAGER), Plugins.BITDUBAI_WPD_WALLET_MANAGER_DESKTOP_MODULE);

                if (WPD) {

           /*
            * Plugin Publisher Identity
            * -----------------------------
            */
                    Plugin publisherIdentity = ((WPDIdentityLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_WPD_IDENTITY_LAYER)).getPublisherIdentity();
                    injectPluginReferencesAndStart(publisherIdentity, Plugins.BITDUBAI_WPD_PUBLISHER_IDENTITY);


           /*
            * Plugin Wallet Factory Middleware
            * ----------------------------------
            */
                    Plugin walletFactoryMiddleware = ((WPDMiddlewareLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_WPD_MIDDLEWARE_LAYER)).getWalletFactoryPlugin();
                    injectPluginReferencesAndStart(walletFactoryMiddleware, Plugins.BITDUBAI_WPD_WALLET_FACTORY_MIDDLEWARE);



           /*
            * Plugin Wallet Publisher Middleware
            * ----------------------------------
            */
                    Plugin walletPublisherMiddleware = ((WPDMiddlewareLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_WPD_MIDDLEWARE_LAYER)).getWalletPublisherPlugin();
                    injectPluginReferencesAndStart(walletPublisherMiddleware, Plugins.BITDUBAI_WPD_WALLET_PUBLISHER_MIDDLEWARE);

           /*
            * Plugin Wallet Store Middleware
            * ----------------------------------
            */
                    Plugin walletStoreMiddleware = ((WPDMiddlewareLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_WPD_MIDDLEWARE_LAYER)).getWalletStorePlugin();
                    injectPluginReferencesAndStart(walletStoreMiddleware, Plugins.BITDUBAI_WPD_WALLET_STORE_MIDDLEWARE);

           /*
            * Plugin Wallet Store Module
            * ----------------------------------
            */
                    Plugin walletStoreModule = ((WPDSubAppModuleLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_WPD_SUB_APP_MODULE_LAYER)).getWalletStore();
                    injectPluginReferencesAndStart(walletStoreModule, Plugins.BITDUBAI_WPD_WALLET_STORE_SUB_APP_MODULE);

           /*
            * Plugin Wallet factory
            * -----------------------------
            */
                    Plugin walletFactoryModule = ((WPDSubAppModuleLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_WPD_SUB_APP_MODULE_LAYER)).getWalletFactory();
                    injectPluginReferencesAndStart(walletFactoryModule, Plugins.BITDUBAI_WPD_WALLET_FACTORY_SUB_APP_MODULE);

           /*
            * Plugin Wallet publisher Module
            * ----------------------------------
            */
                    Plugin walletPublisherModule = ((WPDSubAppModuleLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_WPD_SUB_APP_MODULE_LAYER)).getWalletPublisher();
                    injectPluginReferencesAndStart(walletPublisherModule, Plugins.BITDUBAI_WPD_WALLET_PUBLISHER_SUB_APP_MODULE);

           /*
            * Plugin Wallet settings Middleware
            * ----------------------------------
            */
                    Plugin walletSettingsMiddleware = ((WPDMiddlewareLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_WPD_MIDDLEWARE_LAYER)).getWalletSettingsPlugin();
                    injectPluginReferencesAndStart(walletSettingsMiddleware, Plugins.BITDUBAI_WPD_WALLET_SETTINGS_MIDDLEWARE);

           /*
            * Plugin Wallet Resources Network Service
            * -----------------------------
            */
                    Plugin walletResourcesNetworkService = ((WPDNetworkServiceLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_WPD_NETWORK_SERVICE_LAYER)).getWalletResources();
                    injectPluginReferencesAndStart(walletResourcesNetworkService, Plugins.BITDUBAI_WALLET_RESOURCES_NETWORK_SERVICE);

           /*
            * Plugin Wallet Community Network Service
            * -----------------------------
            */
                    Plugin walletCommunityNetworkService = ((WPDNetworkServiceLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_WPD_NETWORK_SERVICE_LAYER)).getWalletCommunity();
                    injectPluginReferencesAndStart(walletCommunityNetworkService, Plugins.BITDUBAI_WALLET_COMMUNITY_NETWORK_SERVICE);

           /*
            * Plugin Wallet Store Network Service
            * -----------------------------
            */
                    Plugin walletStoreNetworkService = ((WPDNetworkServiceLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_WPD_NETWORK_SERVICE_LAYER)).getWalletStore();
                    injectLayerReferences(walletStoreNetworkService);
                    injectPluginReferencesAndStart(walletStoreNetworkService, Plugins.BITDUBAI_WALLET_STORE_NETWORK_SERVICE);

           /*
            * Plugin Wallet Statistics Network Service
            * -----------------------------
            */
                    Plugin walletStatisticsNetworkService = ((WPDNetworkServiceLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_WPD_NETWORK_SERVICE_LAYER)).getWalletStatistics();
                    injectPluginReferencesAndStart(walletStatisticsNetworkService, Plugins.BITDUBAI_WALLET_STATISTICS_NETWORK_SERVICE);

                }


            if (DMP) {



           /*
            * Plugin Translator Identity
            * -----------------------------
            */
                Plugin translatorIdentity = ((IdentityLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_IDENTITY_LAYER)).getTranslatorIdentity();
                injectPluginReferencesAndStart(translatorIdentity, Plugins.BITDUBAI_TRANSLATOR_IDENTITY);

           /*
            * Plugin Designer Identity
            * -----------------------------
            */
                Plugin designerIdentity = ((IdentityLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_IDENTITY_LAYER)).getDesignerIdentity();
                injectPluginReferencesAndStart(designerIdentity, Plugins.BITDUBAI_DESIGNER_IDENTITY);


           /*
            * Plugin Wallet Runtime
            * -----------------------------
            */
                Plugin walletRuntime = ((ModuleLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_MODULE_LAYER)).getWalletRuntime();
                injectPluginReferencesAndStart(walletRuntime, Plugins.BITDUBAI_WALLET_RUNTIME_MODULE);

           /*
            * Plugin notification
            *---------------------
            */
                Plugin notification = ((ModuleLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_MODULE_LAYER)).getNotification();
                injectPluginReferencesAndStart(notification, Plugins.BITDUBAI_MIDDLEWARE_NOTIFICATION);

           /*
            * Plugin Wallet Navigation Structure Middleware
            * ----------------------------------
            */
                Plugin walletNavigationStructureMiddleware = ((MiddlewareLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_MIDDLEWARE_LAYER)).getWalletNavigationStructurePlugin();
                injectPluginReferencesAndStart(walletNavigationStructureMiddleware, Plugins.BITDUBAI_WALLET_NAVIGATION_STRUCTURE_MIDDLEWARE);

           /*
            * Plugin App Runtime Middleware
            * -------------------------------
            */
                Plugin appRuntimeMiddleware = ((MiddlewareLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_MIDDLEWARE_LAYER)).getAppRuntimePlugin();
                injectPluginReferencesAndStart(appRuntimeMiddleware, Plugins.BITDUBAI_APP_RUNTIME_MIDDLEWARE);

            }



            if (DAP) {

                pluginsToInstantiate.put(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.ACTOR_NETWORK_SERVICE, Plugins.ASSET_ISSUER), Plugins.BITDUBAI_DAP_ASSET_ISSUER_ACTOR_NETWORK_SERVICE      );
                pluginsToInstantiate.put(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.ACTOR_NETWORK_SERVICE, Plugins.ASSET_USER  ), Plugins.BITDUBAI_DAP_ASSET_USER_ACTOR_NETWORK_SERVICE        );
                pluginsToInstantiate.put(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.ACTOR_NETWORK_SERVICE, Plugins.REDEEM_POINT), Plugins.BITDUBAI_DAP_ASSET_REDEEM_POINT_ACTOR_NETWORK_SERVICE);

           /*
            * Plugin Asset Transmission Network Service
            * ----------------------------------------assetIssuerActorNetworkService
            */
                Plugin assetTransmissionNetworkService = ((DAPNetworkServiceLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_DAP_NETWORK_SERVICE_LAYER)).getAssetTransmissionNetworService();
                injectLayerReferences(assetTransmissionNetworkService);
                injectPluginReferencesAndStart(assetTransmissionNetworkService, Plugins.BITDUBAI_DAP_ASSET_TRANSMISSION_NETWORK_SERVICE);

                pluginsToInstantiate.put(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.WALLET, Plugins.ASSET_ISSUER), Plugins.BITDUBAI_ASSET_WALLET_ISSUER);
                pluginsToInstantiate.put(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.WALLET, Plugins.ASSET_USER  ), Plugins.BITDUBAI_DAP_ASSET_USER_WALLET  );
                pluginsToInstantiate.put(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.WALLET, Plugins.REDEEM_POINT), Plugins.BITDUBAI_DAP_ASSET_REDEEM_POINT_WALLET);



                pluginsToInstantiate.put(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.ACTOR, Plugins.ASSET_ISSUER), Plugins.BITDUBAI_DAP_ASSET_ISSUER_ACTOR);
                pluginsToInstantiate.put(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.ACTOR, Plugins.ASSET_USER  ), Plugins.BITDUBAI_DAP_ASSET_USER_ACTOR  );
                pluginsToInstantiate.put(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.ACTOR, Plugins.REDEEM_POINT), Plugins.BITDUBAI_DAP_REDEEM_POINT_ACTOR);


           /*
            * Plugin Asset Issuing TransactionassetWalletRedeemPoint
            * -----------------------------
            */
                Plugin assetIssuingTransaction = ((DAPTransactionLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_DAP_TRANSACTION_LAYER)).getAssetIssuingPlugin();
                injectPluginReferencesAndStart(assetIssuingTransaction, Plugins.BITDUBAI_ASSET_ISSUING_TRANSACTION);

           /*
            * Plugin Asset Distribution Transaction
            * -----------------------------
            */
                Plugin assetDistributionTransaction = ((DAPTransactionLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_DAP_TRANSACTION_LAYER)).getAssetDistributionPlugin();
                injectPluginReferencesAndStart(assetDistributionTransaction, Plugins.BITDUBAI_ASSET_DISTRIBUTION_TRANSACTION);

           /*
            * Plugin Asset Appropriation Transaction
            * -----------------------------
            */
                Plugin assetAppropriationTransaction = ((DAPTransactionLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_DAP_TRANSACTION_LAYER)).getAssetAppropriationPlugin();
                injectPluginReferencesAndStart(assetAppropriationTransaction, Plugins.BITDUBAI_ASSET_APPROPRIATION_TRANSACTION);

           /*
            * Plugin Asset Reception Transaction
            * -----------------------------
            */
                Plugin assetReceptionTransaction = ((DAPTransactionLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_DAP_TRANSACTION_LAYER)).getAssetReceptionPlugin();
                injectPluginReferencesAndStart(assetReceptionTransaction, Plugins.BITDUBAI_ASSET_RECEPTION_TRANSACTION);

           /*
            * Plugin Redeem Point Redemption Transaction.
            * -----------------------------
            */
                Plugin redeemPointRedemptionTransaction = ((DAPTransactionLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_DAP_TRANSACTION_LAYER)).getAssetRedeemPointRedemptionPlugin();
                injectPluginReferencesAndStart(redeemPointRedemptionTransaction, Plugins.BITDUBAI_REDEEM_POINT_REDEMPTION_TRANSACTION);

                pluginsToInstantiate.put(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.IDENTITY, Plugins.ASSET_ISSUER), Plugins.BITDUBAI_DAP_ASSET_ISSUER_IDENTITY);
                pluginsToInstantiate.put(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.IDENTITY, Plugins.ASSET_USER  ), Plugins.BITDUBAI_DAP_ASSET_USER_IDENTITY  );
                pluginsToInstantiate.put(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.IDENTITY, Plugins.REDEEM_POINT), Plugins.BITDUBAI_DAP_REDEEM_POINT_IDENTITY);


           /*
            * Plugin Asset Factory Middleware
            * -----------------------------
            */
                Plugin assetFactortMiddleware = ((DAPMiddlewareLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_DAP_MIDDLEWARE_LAYER)).getPluginAssetFactory();
                injectPluginReferencesAndStart(assetFactortMiddleware, Plugins.BITDUBAI_ASSET_FACTORY);

           /*
            * Plugin Asset Factory Module
            * -----------------------------
            */
                Plugin assetFactoryModlue = ((DAPModuleLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_DAP_MODULE_LAYER)).getPluginAssetFactoryModule();
                injectPluginReferencesAndStart(assetFactoryModlue, Plugins.BITDUBAI_ASSET_FACTORY_MODULE);

                pluginsToInstantiate.put(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.WALLET_MODULE, Plugins.ASSET_ISSUER), Plugins.BITDUBAI_DAP_ASSET_ISSUER_WALLET_MODULE);
                pluginsToInstantiate.put(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.WALLET_MODULE, Plugins.ASSET_USER), Plugins.BITDUBAI_DAP_ASSET_USER_WALLET_MODULE);
                pluginsToInstantiate.put(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.WALLET_MODULE, Plugins.REDEEM_POINT), Plugins.BITDUBAI_DAP_ASSET_REDEEM_POINT_WALLET_MODULE);

           /*
            * Plugin Asset Issuer Community Sub App Module
            * -----------------------------
            */
                Plugin assetIssuerSubAppModuleCommunity = ((DAPSubAppModuleLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_DAP_SUB_APP_MODULE_LAYER)).getAssetIssuerCommunity();
                injectPluginReferencesAndStart(assetIssuerSubAppModuleCommunity, Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE);

           /*
            * Plugin Asset User Community Sub App Module
            * -----------------------------
            */
                Plugin assetUserSubAppModuleCommunity = ((DAPSubAppModuleLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_DAP_SUB_APP_MODULE_LAYER)).getAssetUserCommunity();
                injectPluginReferencesAndStart(assetUserSubAppModuleCommunity, Plugins.BITDUBAI_DAP_ASSET_USER_COMMUNITY_SUB_APP_MODULE);

           /*
            * Plugin Redeem Point Community Sub App Module
            * -----------------------------
            */
                Plugin redeemPointSubAppModuleCommunity = ((DAPSubAppModuleLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_DAP_SUB_APP_MODULE_LAYER)).getRedeemPointCommunity();
                injectPluginReferencesAndStart(redeemPointSubAppModuleCommunity, Plugins.BITDUBAI_DAP_REDEEM_POINT_COMMUNITY_SUB_APP_MODULE);

            }

//            /*
//            * Plugin Asset Transmission Network Service
//            * ----------------------------------------
//            */
//
//            Plugin assetTransmissionNetworkService = ((DAPNetworkServiceLayer) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_DAP_NETWORK_SERVICE_LAYER)).getAssetTransmissionNetworService();
//            injectLayerReferences(assetTransmissionNetworkService);
//            injectPluginReferencesAndStart(assetTransmissionNetworkService, Plugins.BITDUBAI_DAP_ASSET_TRANSMISSION_NETWORK_SERVICE);

            if(CBP){

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_BROKER_PLATFORM, Layers.IDENTITY      , Plugins.CRYPTO_BROKER           ), Plugins.BITDUBAI_CBP_CRYPTO_BROKER_IDENTITY);

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_BROKER_PLATFORM, Layers.IDENTITY      , Plugins.CRYPTO_CUSTOMER         ), Plugins.BITDUBAI_CBP_CRYPTO_CUSTOMER_IDENTITY);

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_BROKER_PLATFORM, Layers.SUB_APP_MODULE, Plugins.CRYPTO_BROKER_IDENTITY  ), Plugins.BITDUBAI_CBP_CRYPTO_BROKER_IDENTITY_SUB_APP_MODULE);

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_BROKER_PLATFORM, Layers.SUB_APP_MODULE, Plugins.CRYPTO_CUSTOMER_IDENTITY), Plugins.BITDUBAI_CBP_CRYPTO_CUSTOMER_IDENTITY_SUB_APP_MODULE);

                pluginsToInstantiate.put(ref(Platforms.CRYPTO_BROKER_PLATFORM, Layers.WALLET_MODULE , Plugins.CRYPTO_BROKER           ), Plugins.BITDUBAI_CBP_CRYPTO_BROKER_WALLET_MODULE);

            }

            for (AddonVersionReference avr :addonsToInstantiate)
                startEachAddon(avr);

            for (Map.Entry<PluginVersionReference, Plugins> pvr : pluginsToInstantiate.entrySet())
                startEachPlugin(pvr.getKey(), pvr.getValue());

        } catch (CantInitializePluginsManagerException cantInitializePluginsManagerException) {
            LOG.log(Level.SEVERE, cantInitializePluginsManagerException.getLocalizedMessage());
            throw new CantStartPlatformException(CantStartPlatformException.DEFAULT_MESSAGE,cantInitializePluginsManagerException, "","");
        }
        List<Map.Entry<Plugins, Long>> list = new LinkedList<>(pluginsStartUpTime.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Plugins, Long>>() {
            public int compare(Map.Entry<Plugins, Long> o1, Map.Entry<Plugins, Long> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        System.out.println("************************************************************************");
        System.out.println("---- Lista de Tiempos de Start-Up de Plugins order by Start-Up time ----");
        System.out.println("************************************************************************");
        for (Map.Entry<Plugins, Long> entry : list) {
            System.out.println(entry.getKey().toString() + " - Start-Up time: " + entry.getValue() / 1000 + " seconds.");
        }
        System.out.println("************************************************************************");
        System.out.println("************************************************************************");

        System.out.println("--------------- Lista de Tamaños en Start-Up de Plugins ---------------");
        System.out.println("************************************************************************");
        for (Map.Entry<Plugins, String> entry : pluginsSizeReport.entrySet()) {
            System.out.println(entry.getKey().toString() + " - Start-Up Size: " + entry.getValue() +".");
        }
        System.out.println("************************************************************************");

    }

    private void startEachAddon(final AddonVersionReference addonVersionReference) {

        try {

            Addon addon = fermatSystem.getAddon(addonVersionReference);

            corePlatformContext.registerAddon(
                    addon,
                    addonVersionReference.getAddonDeveloperReference().getAddonReference().getAddon()
            );

        } catch (Exception e) {

            reportUnexpectedError(UnexpectedPlatformExceptionSeverity.DISABLES_ONE_PLUGIN, e);
        }
    }

    private void startEachPlugin(final PluginVersionReference pluginVersionReference,
                                 final Plugins                descriptor            ) {

        try {

            Plugin plugin = fermatSystem.startAndGetPluginVersion(pluginVersionReference);
            corePlatformContext.registerPlugin(plugin, descriptor);

        } catch (Exception e) {

            reportUnexpectedError(UnexpectedPlatformExceptionSeverity.DISABLES_ONE_PLUGIN, e);
        }
    }

    private AddonVersionReference ref(Platforms platform, Layers layer, Addons fermatAddonsEnum) {

        return new AddonVersionReference(
                platform,
                layer,
                fermatAddonsEnum,
                Developers.BITDUBAI,
                new Version()
        );
    }

    private PluginVersionReference ref(Platforms platform, Layers layer, Plugins fermatPluginsEnum) {

        return new PluginVersionReference(
                platform,
                layer,
                fermatPluginsEnum,
                Developers.BITDUBAI,
                new Version()
        );
    }

    Map<Plugins, Long> pluginsStartUpTime = new HashMap<>();
    Map<Plugins, String> pluginsSizeReport = new HashMap<>();

    /**
     * Method that inject all references to another plugin that required a
     * plugin to work
     *
     * @param plugin     whom I'll start
     * @param descriptor descriptor of this plugin
     */
    private void injectPluginReferencesAndStart(Plugin plugin, Plugins descriptor) {

        long init = System.currentTimeMillis();

        /*
         * Get the error manager instance
         */



        try {

            if (plugin instanceof DealsWithBitcoinWallet) {
                ((DealsWithBitcoinWallet) plugin).setBitcoinWalletManager((BitcoinWalletManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.BASIC_WALLET, Plugins.BITCOIN_WALLET)));
            }

            if (plugin instanceof DealsWithBitcoinNetwork) {
                ((DealsWithBitcoinNetwork) plugin).setBitcoinNetworkManager((BitcoinNetworkManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.BLOCKCHAINS, Layers.CRYPTO_NETWORK, Plugins.BITCOIN_NETWORK)));
            }

            if (plugin instanceof DealsWithCryptoVault) {
                ((DealsWithCryptoVault) plugin).setCryptoVaultManager((CryptoVaultManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.BLOCKCHAINS, Layers.CRYPTO_VAULT, Plugins.BITCOIN_VAULT)));
            }

            if (plugin instanceof DealsWithAssetVault) {
                ((DealsWithAssetVault) plugin).setAssetVaultManager((AssetVaultManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.BLOCKCHAINS, Layers.CRYPTO_VAULT, Plugins.BITCOIN_ASSET_VAULT)));
            }

            if (plugin instanceof DealsWithDeveloperModule) {
                ((DealsWithDeveloperModule) plugin).setDeveloperModuleManager((DeveloperModuleManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_DEVELOPER_MODULE));
            }

            if (plugin instanceof DealsWithErrors) {
                ((DealsWithErrors) plugin).setErrorManager((ErrorManager) fermatSystem.getAddon(ref(Platforms.PLUG_INS_PLATFORM, Layers.PLATFORM_SERVICE, Addons.ERROR_MANAGER)));
            }

            if (plugin instanceof DealsWithEvents) {
                ((DealsWithEvents) plugin).setEventManager((EventManager) fermatSystem.getAddon(ref(Platforms.PLUG_INS_PLATFORM, Layers.PLATFORM_SERVICE, Addons.EVENT_MANAGER)));
            }

            if (plugin instanceof DealsWithExtraUsers) {
                ((DealsWithExtraUsers) plugin).setExtraUserManager((ExtraUserManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.ACTOR, Plugins.EXTRA_WALLET_USER)));
            }

            if (plugin instanceof DealsWithLogger) {
                ((DealsWithLogger) plugin).setLogManager((LogManager) fermatSystem.getAddon(ref(Platforms.OPERATIVE_SYSTEM_API, Layers.SYSTEM, Addons.LOG_MANAGER)));
            }

            if (plugin instanceof DealsWithDeviceLocation) {
                ((DealsWithDeviceLocation) plugin).setLocationManager((LocationManager) fermatSystem.getAddon(ref(Platforms.OPERATIVE_SYSTEM_API, Layers.SYSTEM, Addons.DEVICE_LOCATION)));
            }

            if (plugin instanceof DealsWithWalletModuleCryptoWallet) {
                ((DealsWithWalletModuleCryptoWallet) plugin).setWalletModuleCryptoWalletManager((CryptoWalletManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.WALLET_MODULE, Plugins.CRYPTO_WALLET)));
            }

            if (plugin instanceof DealsWithPluginFileSystem) {
                ((DealsWithPluginFileSystem) plugin).setPluginFileSystem((PluginFileSystem) fermatSystem.getAddon(ref(Platforms.OPERATIVE_SYSTEM_API, Layers.SYSTEM, Addons.PLUGIN_FILE_SYSTEM)));
            }

            if (plugin instanceof DealsWithPluginDatabaseSystem) {
                ((DealsWithPluginDatabaseSystem) plugin).setPluginDatabaseSystem((PluginDatabaseSystem) fermatSystem.getAddon(ref(Platforms.OPERATIVE_SYSTEM_API, Layers.SYSTEM, Addons.PLUGIN_DATABASE_SYSTEM)));
            }

            if (plugin instanceof DealsWithCryptoAddressBook) {
                ((DealsWithCryptoAddressBook) plugin).setCryptoAddressBookManager((CryptoAddressBookManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.BLOCKCHAINS, Layers.CRYPTO_MODULE, Plugins.CRYPTO_ADDRESS_BOOK)));
            }

            if (plugin instanceof DealsWithWalletFactory) {
                ((DealsWithWalletFactory) plugin).setWalletFactoryProjectManager((WalletFactoryProjectManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_WPD_WALLET_FACTORY_MIDDLEWARE));
            }

            if (plugin instanceof DealsWithWalletManager) {
                ((DealsWithWalletManager) plugin).setWalletManagerManager((WalletManagerManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.MIDDLEWARE, Plugins.WALLET_MANAGER)));
            }

            if (plugin instanceof DealsWithWalletPublisherModule) {
                ((DealsWithWalletPublisherModule) plugin).setWalletPublisherManager((WalletPublisherModuleManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_WPD_WALLET_PUBLISHER_SUB_APP_MODULE));
            }

            if (plugin instanceof DealsWithWalletResources) {
                ((DealsWithWalletResources) plugin).setWalletResourcesManager((WalletResourcesInstalationManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_WALLET_RESOURCES_NETWORK_SERVICE));
            }

            if (plugin instanceof DealsWithWalletStatisticsNetworkService) {
                ((DealsWithWalletStatisticsNetworkService) plugin).setWalletStatisticsManager((WalletStatisticsManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_WALLET_STATISTICS_NETWORK_SERVICE));
            }

            if (plugin instanceof DealsWithWalletStoreMiddleware) {
                ((DealsWithWalletStoreMiddleware) plugin).setWalletStoreManager((WalletStoreManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_WPD_WALLET_STORE_MIDDLEWARE));
            }

            if (plugin instanceof DealsWithWalletStoreNetworkService) {
                ((DealsWithWalletStoreNetworkService) plugin).setWalletStoreManager((com.bitdubai.fermat_wpd_api.layer.wpd_network_service.wallet_store.interfaces.WalletStoreManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_WALLET_STORE_NETWORK_SERVICE));
            }

            if (plugin instanceof DealsWithDeviceUser) {
                ((DealsWithDeviceUser) plugin).setDeviceUserManager((DeviceUserManager) fermatSystem.getAddon(ref(Platforms.PLUG_INS_PLATFORM, Layers.USER, Addons.DEVICE_USER)));
            }
            if (plugin instanceof DealsWithWalletStoreModule) {
                ((DealsWithWalletStoreModule) plugin).setWalletStoreModuleManager((WalletStoreModuleManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_WPD_WALLET_STORE_SUB_APP_MODULE));
            }

            if (plugin instanceof DealsWithPlatformInfo) {
                ((DealsWithPlatformInfo) plugin).setPlatformInfoManager((PlatformInfoManager) fermatSystem.getAddon(ref(Platforms.PLUG_INS_PLATFORM, Layers.PLATFORM_SERVICE, Addons.PLATFORM_INFO)));
            }

            if (plugin instanceof DealsWithWalletPublisherMiddlewarePlugin) {
                ((DealsWithWalletPublisherMiddlewarePlugin) plugin).setWalletPublisherMiddlewarePlugin((WalletPublisherMiddlewarePlugin) corePlatformContext.getPlugin(Plugins.BITDUBAI_WPD_WALLET_PUBLISHER_MIDDLEWARE));
            }

            if (plugin instanceof DealsWithCCPActorIntraWalletUsers) {
                ((DealsWithCCPActorIntraWalletUsers) plugin).setIntraWalletUserIdentityManager((IntraWalletUserActorManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.ACTOR, Plugins.INTRA_WALLET_USER)));
            }

            if (plugin instanceof DealsWithIdentityDesigner) {
                ((DealsWithIdentityDesigner) plugin).setDesignerIdentityManager((DesignerIdentityManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_DESIGNER_IDENTITY));
            }

            if (plugin instanceof DealsWithPublisherIdentity) {
                ((DealsWithPublisherIdentity) plugin).setPublisherIdentityManager((PublisherIdentityManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_WPD_PUBLISHER_IDENTITY));
            }

            if (plugin instanceof DealsWithIdentityTranslator) {
                ((DealsWithIdentityTranslator) plugin).setTranslatorIdentityManager((TranslatorIdentityManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_TRANSLATOR_IDENTITY));
            }

            if (plugin instanceof DealsWithWalletSettings) {
                ((DealsWithWalletSettings) plugin).setWalletSettingsManager((WalletSettingsManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_WPD_WALLET_SETTINGS_MIDDLEWARE));
            }

            if (plugin instanceof DealsWithAssetIssuing) {
                ((DealsWithAssetIssuing) plugin).setAssetIssuingManager((AssetIssuingManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_ASSET_ISSUING_TRANSACTION));
            }

            if (plugin instanceof DealsWithAssetDistribution) {
                ((DealsWithAssetDistribution) plugin).setAssetDistributionManager((AssetDistributionManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_ASSET_DISTRIBUTION_TRANSACTION));
            }

            if (plugin instanceof DealsWithAssetReception) {
                ((DealsWithAssetReception) plugin).setAssetReceptionManager((AssetReceptionManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_ASSET_RECEPTION_TRANSACTION));
            }

            if (plugin instanceof DealsWithAssetAppropriation){
                ((DealsWithAssetAppropriation) plugin).setAssetAppropriationManager((AssetAppropriationManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_ASSET_APPROPRIATION_TRANSACTION));
            }

            if (plugin instanceof DealsWithActorAssetIssuer) {
                ((DealsWithActorAssetIssuer) plugin).setActorAssetIssuerManager((ActorAssetIssuerManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.ACTOR, Plugins.ASSET_ISSUER)));
            }

            if (plugin instanceof DealsWithActorAssetUser) {
                ((DealsWithActorAssetUser) plugin).setActorAssetUserManager((ActorAssetUserManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.ACTOR, Plugins.ASSET_USER)));
            }

            if (plugin instanceof DealsWithActorAssetRedeemPoint) {
                ((DealsWithActorAssetRedeemPoint) plugin).setActorAssetRedeemPointManager((ActorAssetRedeemPointManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.ACTOR, Plugins.REDEEM_POINT)));
            }

            if (plugin instanceof DealsWithIdentityAssetIssuer) {
                ((DealsWithIdentityAssetIssuer) plugin).setIdentityAssetIssuerManager((IdentityAssetIssuerManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.IDENTITY, Plugins.ASSET_ISSUER)));
            }

            if (plugin instanceof DealsWithIdentityAssetUser) {
                ((DealsWithIdentityAssetUser) plugin).setIdentityAssetUserManager((IdentityAssetUserManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.IDENTITY, Plugins.ASSET_USER)));
            }

            if (plugin instanceof DealsWithAssetIssuerActorNetworkServiceManager) {
                ((DealsWithAssetIssuerActorNetworkServiceManager) plugin).setAssetIssuerActorNetworkServiceManager((AssetIssuerActorNetworkServiceManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.ACTOR_NETWORK_SERVICE, Plugins.ASSET_ISSUER)));
            }

            if (plugin instanceof DealsWithAssetUserActorNetworkServiceManager) {
                ((DealsWithAssetUserActorNetworkServiceManager) plugin).setAssetUserActorNetworkServiceManager((AssetUserActorNetworkServiceManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.ACTOR_NETWORK_SERVICE, Plugins.ASSET_USER)));
            }

            if (plugin instanceof DealsWithAssetRedeemPointActorNetworkServiceManager) {
                ((DealsWithAssetRedeemPointActorNetworkServiceManager) plugin).setAssetRedeemPointActorNetworkServiceManager((AssetRedeemPointActorNetworkServiceManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.ACTOR_NETWORK_SERVICE, Plugins.REDEEM_POINT)));
            }

            if (plugin instanceof DealsWithAssetTransmissionNetworkServiceManager) {
                ((DealsWithAssetTransmissionNetworkServiceManager) plugin).setAssetTransmissionNetworkServiceManager((AssetTransmissionNetworkServiceManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_DAP_ASSET_TRANSMISSION_NETWORK_SERVICE));
            }

            if (plugin instanceof DealsWithIdentityAssetRedeemPoint) {
                ((DealsWithIdentityAssetRedeemPoint) plugin).setRedeemPointIdentityManager((RedeemPointIdentityManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.IDENTITY, Plugins.REDEEM_POINT)));
            }

            if (plugin instanceof DealsWithAssetFactory) {
                ((DealsWithAssetFactory) plugin).setAssetFactoryManager((AssetFactoryManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_ASSET_FACTORY));
            }
            if (plugin instanceof DealsWithModuleAseetFactory) {
                ((DealsWithModuleAseetFactory) plugin).setAssetFactoryModuleManager((AssetFactoryModuleManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_ASSET_FACTORY_MODULE));
            }

            if (plugin instanceof DealsWithAssetIssuerCommunitySubAppModule) {
                ((DealsWithAssetIssuerCommunitySubAppModule) plugin).setAssetIssuerCommunitySubAppModuleManager((AssetIssuerCommunitySubAppModuleManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_DAP_ASSET_ISSUER_COMMUNITY_SUB_APP_MODULE));
            }

            if (plugin instanceof DealsWithAssetUserCommunitySubAppModule) {
                ((DealsWithAssetUserCommunitySubAppModule) plugin).setAssetIssuerCommunitySubAppModuleManager((AssetUserCommunitySubAppModuleManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_DAP_ASSET_USER_COMMUNITY_SUB_APP_MODULE));
            }

            if (plugin instanceof DealsWithRedeemPointCommunitySubAppModule) {
                ((DealsWithRedeemPointCommunitySubAppModule) plugin).setAssetIssuerCommunitySubAppModuleManager((RedeemPointCommunitySubAppModuleManager) corePlatformContext.getPlugin(Plugins.BITDUBAI_DAP_REDEEM_POINT_COMMUNITY_SUB_APP_MODULE));
            }

            if (plugin instanceof DealsWithAssetIssuerWallet) {
                ((DealsWithAssetIssuerWallet) plugin).setAssetIssuerManager((AssetIssuerWalletManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.WALLET, Plugins.ASSET_ISSUER)));
            }
            if (plugin instanceof DealsWithAssetUserWallet) {
                ((DealsWithAssetUserWallet) plugin).setAssetUserManager((AssetUserWalletManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.WALLET, Plugins.ASSET_USER)));
            }
            if (plugin instanceof DealsWithAssetRedeemPointWallet) {
                ((DealsWithAssetRedeemPointWallet) plugin).setAssetReddemPointManager((AssetRedeemPointWalletManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.DIGITAL_ASSET_PLATFORM, Layers.WALLET, Plugins.REDEEM_POINT)));
            }

            if (plugin instanceof DealsWithWsCommunicationsCloudClientManager) //////////////////////////////////////////////////////////////////////////////////////////////////////
                ((DealsWithWsCommunicationsCloudClientManager) plugin).setWsCommunicationsCloudClientConnectionManager((WsCommunicationsCloudClientManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.COMMUNICATION_PLATFORM, Layers.COMMUNICATION, Plugins.WS_CLOUD_CLIENT)));

            if (plugin instanceof DealsWithOutgoingIntraActor) {
                ((DealsWithOutgoingIntraActor) plugin).setOutgoingIntraActorManager((OutgoingIntraActorManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.CRYPTO_CURRENCY_PLATFORM, Layers.TRANSACTION, Plugins.OUTGOING_INTRA_ACTOR)));
            }

            if (plugin instanceof DealsWithCryptoBrokerIdentities){
                ((DealsWithCryptoBrokerIdentities) plugin).setCryptoBrokerIdentityManager((CryptoBrokerIdentityManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.CRYPTO_BROKER_PLATFORM, Layers.IDENTITY, Plugins.CRYPTO_BROKER)));
            }

            if (plugin instanceof DealsWithCryptoCustomerIdentities){
                ((DealsWithCryptoCustomerIdentities) plugin).setCryptoCustomerIdentityManager((CryptoCustomerIdentityManager) fermatSystem.startAndGetPluginVersion(ref(Platforms.CRYPTO_BROKER_PLATFORM, Layers.IDENTITY, Plugins.CRYPTO_CUSTOMER)));
            }

            /*
             * Register the plugin into the platform context
             */
            corePlatformContext.registerPlugin(plugin, descriptor);

            /*
             * As any other plugin, this one will need its identity in order to access the data it persisted before.
             */
            plugin.setId(pluginsIdentityManager.getPluginId(descriptor));

            /*
             * Start the plugin service
             */
            ((Service) plugin).start();


        } catch (CantGetAddonException | VersionNotFoundException e) {
            System.out.println("can't get error manager");
            System.out.println(e);
        } catch (Exception e) {

            /**
             * This plugin wont disable the whole platform, so I will allow the Platform to start even if this one
             * will be disabled. In the future, I will re-try the start of plugins that are not starting at once.
             */
            reportUnexpectedError(UnexpectedPlatformExceptionSeverity.DISABLES_ONE_PLUGIN, e);
        }

        long end = System.currentTimeMillis();

        pluginsStartUpTime.put(descriptor, end - init);
        pluginsSizeReport.put(descriptor, ObjectSizeFetcher.sizeOf(plugin));
    }

    private void reportUnexpectedError(UnexpectedPlatformExceptionSeverity severity, Exception e) {
        try {
            ErrorManager errorManager = (ErrorManager) fermatSystem.getAddon(ref(Platforms.PLUG_INS_PLATFORM, Layers.PLATFORM_SERVICE, Addons.ERROR_MANAGER));
            errorManager.reportUnexpectedPlatformException(PlatformComponents.PLATFORM, severity, e);
        } catch (CantGetAddonException | VersionNotFoundException z) {
            System.out.println("can't get error manager");
            System.out.println(z);
        } catch (Exception z) {
            System.out.println("unhandled exception");
            System.out.println(z.toString());
        }
    }

    /**
     * This method is responsible to inject PlatformLayer referent object, since in special cases some plugin interact
     * directly with a layer instance. For example in the case of Network Services
     * <p/>
     * NOTE: This method should always call before @see Platform#injectPluginReferencesAndStart(Plugin, Plugins)
     * always and when it is required by the plugin
     *
     * @param plugin
     */
    private void injectLayerReferences(Plugin plugin) throws CantStartPlatformException {

        try {

            /*
             * Validate if this plugin required the Communication Layer instance
             */
            if (plugin instanceof DealsWithCommunicationLayerManager) {

                /*
                 * Inject the instance
                 */
                ((DealsWithCommunicationLayerManager) plugin).setCommunicationLayerManager((CommunicationLayerManager) corePlatformContext.getPlatformLayer(PlatformLayers.BITDUBAI_COMMUNICATION_LAYER));

            }


        } catch (Exception exception) {
            LOG.log(Level.SEVERE, exception.getLocalizedMessage());
            throw new CantStartPlatformException(CantStartPlatformException.DEFAULT_MESSAGE, FermatException.wrapException(exception), "", "");
        }
    }

    /**
     * Method that check addons for developer interfaces
     *
     * @param descriptor
     */
    private void checkAddonForDeveloperInterfaces(final Addons descriptor) {

        /*
         * Get the addon by description
         */
        Addon addon = corePlatformContext.getAddon(descriptor);

        /*
         * Validate is not null
         */
        if (addon == null) {
            return;
        }

        /*
         * Validate if is instance of DatabaseManagerForDevelopers
         */
        if (addon instanceof DatabaseManagerForDevelopers) {

            /*
             * Put into dealsWithDatabaseManagersAddons
             */
            dealsWithDatabaseManagersAddons.put(descriptor, addon);
        }

        /*
         * Validate if is instance of LogManagerForDevelopers
         */
        if (addon instanceof LogManagerForDevelopers) {

            /*
             * Put into dealsWithLogManagersAddons
             */
            dealsWithLogManagersAddons.put(descriptor, addon);
        }
    }

    /**
     * Method that check plugin for developer interfaces
     *
     * @param descriptor
     */
    private void checkPluginForDeveloperInterfaces(final Plugins descriptor) {

        /*
         * Get the plugin by description
         */
        Plugin plugin = corePlatformContext.getPlugin(descriptor);

        /*
         * Validate is not null
         */
        if (plugin == null) {
            return;
        }

        /*
         * Validate if is instance of DatabaseManagerForDevelopers
         */
        if (plugin instanceof DatabaseManagerForDevelopers) {

            /*
             * Put into dealsWithDatabaseManagersPlugins
             */
            dealsWithDatabaseManagersPlugins.put(descriptor, plugin);
        }

        /*
         * Validate if is instance of LogManagerForDevelopers
         */
        if (plugin instanceof LogManagerForDevelopers) {

            /*
             * Put into dealsWithDatabaseManagersPlugins
             */
            dealsWithLogManagersPlugins.put(descriptor, plugin);
        }

    }

    private void getPlugin() {

    }

    /**
     * Get the CorePlatformContext
     *
     * @return CorePlatformContext
     */
    public CorePlatformContext getCorePlatformContext() {
        return corePlatformContext;
        // Luis: TODO: Este metodo debe ser removido y lo que se debe devolver es un context con referencias a los plugins que la interfaz grafica puede acceder, no a todos los que existen como esta ahora mismo.
    }

    /**
     * Get the plugin reference from the context
     *
     * @param key
     * @return Plugin
     */
    public Plugin getPlugin(Plugins key) {
        return corePlatformContext.getPlugin(key);
    }
}
