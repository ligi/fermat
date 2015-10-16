package com.bitdubai.sub_app.crypto_broker_identity.common.holders;

import android.view.View;

import com.bitdubai.fermat_android_api.layer.definition.wallet.FermatRoundedImageView;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.holders.FermatViewHolder;
import com.bitdubai.sub_app.crypto_broker_identity.R;

/**
 * Created by nelson on 01/09/15.
 */
public class CryptoBrokerIdentityInfoViewHolder extends FermatViewHolder {
    private FermatRoundedImageView identityImage;
    private FermatTextView identityName;

    public CryptoBrokerIdentityInfoViewHolder(View itemView) {
        super(itemView);

        identityImage = (FermatRoundedImageView) itemView.findViewById(R.id.crypto_broker_identity_image);
        identityName = (FermatTextView) itemView.findViewById(R.id.crypto_broker_identity_alias);
    }

    public FermatRoundedImageView getIdentityImage() {
        return identityImage;
    }

    public FermatTextView getIdentityName() {
        return identityName;
    }
}
