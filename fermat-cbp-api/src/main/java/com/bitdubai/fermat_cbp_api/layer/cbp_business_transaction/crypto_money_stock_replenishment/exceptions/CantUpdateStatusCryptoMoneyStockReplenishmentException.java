package com.bitdubai.fermat_cbp_api.layer.cbp_business_transaction.crypto_money_stock_replenishment.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * Created by Yordin Alayn on 24.09.15.
 */

public class CantUpdateStatusCryptoMoneyStockReplenishmentException extends FermatException {

    public static final String DEFAULT_MESSAGE = "Falled To Update the Status the Business Transaction Crypto Money Stock Replenishment.";
    public CantUpdateStatusCryptoMoneyStockReplenishmentException(String message, Exception cause, String context, String possibleReason) {
        super(message, cause, context, possibleReason);
    }
}
