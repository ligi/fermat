package com.bitdubai.reference_niche_wallet.fermat_wallet.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.EditText;

import com.bitdubai.android_fermat_ccp_wallet_fermat.R;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.all_definition.settings.exceptions.CantGetSettingsException;
import com.bitdubai.fermat_api.layer.all_definition.settings.exceptions.CantPersistSettingsException;
import com.bitdubai.fermat_api.layer.all_definition.settings.exceptions.SettingsNotFoundException;
import com.bitdubai.fermat_api.layer.modules.interfaces.FermatSettings;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.classes.vault_seed.exceptions.CantLoadExistingVaultSeed;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.fermat_wallet.FermatWalletSettings;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.fermat_wallet.interfaces.FermatWallet;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.fermat_wallet.interfaces.FermatWalletWalletContact;
import com.bitdubai.fermat_wpd_api.layer.wpd_network_service.wallet_resources.interfaces.WalletResourcesProviderManager;
import com.bitdubai.reference_niche_wallet.fermat_wallet.common.popup.SendToLossProtectedWalletDialog;
import com.bitdubai.reference_niche_wallet.fermat_wallet.common.utils.WalletUtils;
import com.bitdubai.reference_niche_wallet.fermat_wallet.session.FermatWalletSession;
import com.mati.fermat_preference_settings.drawer.FermatPreferenceFragment;
import com.mati.fermat_preference_settings.drawer.interfaces.PreferenceSettingsItem;
import com.mati.fermat_preference_settings.drawer.models.PreferenceSettingsLinkText;
import com.mati.fermat_preference_settings.drawer.models.PreferenceSettingsOpenDialogText;
import com.mati.fermat_preference_settings.drawer.models.PreferenceSettingsSwithItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.bitdubai.reference_niche_wallet.fermat_wallet.common.utils.WalletUtils.showMessage;

/**
 * Created by mati on 2016.02.09..
 */
public class ReferenceWalletSettings extends FermatPreferenceFragment<FermatWalletSession,WalletResourcesProviderManager> implements FermatSettings {

    private FermatWalletSession fermatWalletSession;
    private FermatWalletSettings bitcoinWalletSettings = null;
    private String previousSelectedItem = "RegTest";

    private FermatWalletWalletContact cryptoWalletWalletContact;
    private BlockchainNetworkType blockchainNetworkType;
    private FermatWallet cryptoWallet;

    public static ReferenceWalletSettings newInstance() {
        return new ReferenceWalletSettings();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fermatWalletSession = appSession;
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            //noinspection unchecked
        } catch (Exception e) {
            fermatWalletSession.getErrorManager().reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
            showMessage(getActivity(), "CantGetCryptoWalletException- " + e.getMessage());
        }
    }

    @Override
    protected boolean hasMenu() {
        return false;
    }

    @Override
    protected List<PreferenceSettingsItem> setSettingsItems() {
        BlockchainNetworkType blockchainNetworkType = null;
        List<PreferenceSettingsItem> list = new ArrayList<>();

        //noinspection TryWithIdenticalCatches
        try {
            bitcoinWalletSettings = fermatWalletSession.getModuleManager().loadAndGetSettings(fermatWalletSession.getAppPublicKey());
            cryptoWallet = fermatWalletSession.getModuleManager();

            list.add(new PreferenceSettingsSwithItem(1, "Enabled Notifications", bitcoinWalletSettings.getNotificationEnabled()));

            if (bitcoinWalletSettings.getBlockchainNetworkType() != null) {
                blockchainNetworkType = bitcoinWalletSettings.getBlockchainNetworkType();

                switch (blockchainNetworkType) {
                    case PRODUCTION:
                        previousSelectedItem = "MainNet";
                        break;
                    case REG_TEST:
                        previousSelectedItem = "RegTest";
                        break;
                    case TEST_NET:
                        previousSelectedItem = "TestNet";
                        break;
                }

            }


            final Bundle dataDialog = new Bundle();
            dataDialog.putInt("items", R.array.items);
            dataDialog.putString("positive_button_text", getResources().getString(R.string.ok_label));
            dataDialog.putString("negative_button_text", getResources().getString(R.string.cancel_label));
            dataDialog.putString("title", getResources().getString(R.string.title_label));
            dataDialog.putString("mode", "single_option");
            dataDialog.putString("previous_selected_item", previousSelectedItem);
            list.add(new PreferenceSettingsOpenDialogText(5, "Select Network", dataDialog));


            list.add(new PreferenceSettingsLinkText(9, "Send Error Report", "",15,Color.GRAY));

            list.add(new PreferenceSettingsLinkText(10, "Export Private key ", "",15,Color.GRAY));

            list.add(new PreferenceSettingsLinkText(11, "Send Bitcoins To Loss Protected Wallet", "",15,Color.GRAY));

            list.add(new PreferenceSettingsLinkText(12, "Import Mnemonic code", "",15,Color.GRAY));


        } catch (CantGetSettingsException e) {
            e.printStackTrace();
        } catch (SettingsNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Cuando se elige algun settings va a pasar por acá
     *
     * @param preferenceSettingsItem
     * @param position
     */
    @Override
    public void onSettingsTouched(PreferenceSettingsItem preferenceSettingsItem, int position) {

        try {

            try {
                bitcoinWalletSettings = fermatWalletSession.getModuleManager().loadAndGetSettings(fermatWalletSession.getAppPublicKey());
            } catch (CantGetSettingsException e) {
                e.printStackTrace();
            } catch (SettingsNotFoundException e) {
                e.printStackTrace();
            }
            bitcoinWalletSettings.setIsPresentationHelpEnabled(false);


            if (preferenceSettingsItem.getId() == 10) {
                //export key show fragment

                changeActivity(Activities.CCP_BITCOIN_WALLET_MNEMONIC_ACTIVITY, fermatWalletSession.getAppPublicKey());

            } else if (preferenceSettingsItem.getId() == 9) {
                //export key show fragment

                changeActivity(Activities.CCP_BITCOIN_WALLET_OPEN_SEND_ERROR_REPORT, fermatWalletSession.getAppPublicKey());

            }else if (preferenceSettingsItem.getId() == 11){
                //send btc to loss protected
                SendToLossProtectedWalletDialog sendToLossProtectedWalletDialog = new SendToLossProtectedWalletDialog(
                        getActivity(),
                        cryptoWallet,
                        fermatWalletSession,
                        blockchainNetworkType);
                sendToLossProtectedWalletDialog.show();

            } else if(preferenceSettingsItem.getId() == 12){

                openImportMnemonicScreen();

            }



            try {
                fermatWalletSession.getModuleManager().persistSettings(fermatWalletSession.getAppPublicKey(), bitcoinWalletSettings);
            } catch (CantPersistSettingsException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
        }

    }

    String m_Text;

    private void openImportMnemonicScreen() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Title");

// Set up the input
        final EditText input = new EditText(getActivity());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                final List<String> mnemonicCodePlusDate = Arrays.asList(m_Text.split(" "));
                final long date = Long.parseLong(mnemonicCodePlusDate.get(mnemonicCodePlusDate.size()-1));
                ArrayList<String> mnemonicCode = new ArrayList<String>(mnemonicCodePlusDate);
                mnemonicCode.remove(mnemonicCode.size() - 1);
                final List<String> tempList = new ArrayList<String>(mnemonicCode);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            cryptoWallet.importMnemonicCode(tempList,date,BlockchainNetworkType.getDefaultBlockchainNetworkType());
                        } catch (CantLoadExistingVaultSeed cantLoadExistingVaultSeed) {
                            cantLoadExistingVaultSeed.printStackTrace();
                        }
                    }
                }).start();


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void sendCrypto() {
        try {
            if (cryptoWalletWalletContact.getReceivedCryptoAddress().get(blockchainNetworkType) != null) {
                CryptoAddress validAddress = WalletUtils.validateAddress(cryptoWalletWalletContact.getReceivedCryptoAddress().get(blockchainNetworkType).getAddress(),cryptoWallet);
                if (validAddress != null) {

                }
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onSettingsTouched(String item, int position) {

    }

    @Override
    public void onSettingsChanged(PreferenceSettingsItem preferenceSettingsItem, int position, boolean isChecked) {

        try {

            try {
                bitcoinWalletSettings = fermatWalletSession.getModuleManager().loadAndGetSettings(fermatWalletSession.getAppPublicKey());
            } catch (CantGetSettingsException e) {
                e.printStackTrace();
            } catch (SettingsNotFoundException e) {
                e.printStackTrace();
            }


            if (preferenceSettingsItem.getId() == 1) {
                //enable notifications settings
                bitcoinWalletSettings.setNotificationEnabled(isChecked);
            }


            try {
                fermatWalletSession.getModuleManager().persistSettings(fermatWalletSession.getAppPublicKey(), bitcoinWalletSettings);
            } catch (CantPersistSettingsException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
        }

    }


    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public int getBackgroundAlpha() {
        return 95;
    }

    @Override
    public void optionSelected(PreferenceSettingsItem preferenceSettingsItem, int position)
    {

    }

    @Override
    public void dialogOptionSelected(String item, int position) {

        BlockchainNetworkType blockchainNetworkType;

        switch (item) {

            case "MainNet":
                blockchainNetworkType = BlockchainNetworkType.PRODUCTION;

                break;

            case "TestNet":
                blockchainNetworkType = BlockchainNetworkType.TEST_NET;
                break;

            case "RegTest":
                blockchainNetworkType = BlockchainNetworkType.REG_TEST;
                break;

            default:
                blockchainNetworkType = BlockchainNetworkType.getDefaultBlockchainNetworkType();
                break;

        }

        System.out.println("NETWORK TYPE TO BE SAVED IS  " + blockchainNetworkType.getCode());

        if (blockchainNetworkType == null) {
            if (bitcoinWalletSettings.getBlockchainNetworkType() != null) {
                blockchainNetworkType = bitcoinWalletSettings.getBlockchainNetworkType();
            } else {
                blockchainNetworkType = BlockchainNetworkType.getDefaultBlockchainNetworkType();
            }
        }

        bitcoinWalletSettings.setBlockchainNetworkType(blockchainNetworkType);


        try {
            fermatWalletSession.getModuleManager().persistSettings(fermatWalletSession.getAppPublicKey(), bitcoinWalletSettings);
        } catch (CantPersistSettingsException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setIsPresentationHelpEnabled(boolean b) {

    }
}
