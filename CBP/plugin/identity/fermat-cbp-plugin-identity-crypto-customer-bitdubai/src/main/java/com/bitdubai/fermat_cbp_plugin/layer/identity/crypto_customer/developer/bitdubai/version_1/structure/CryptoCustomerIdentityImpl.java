package com.bitdubai.fermat_cbp_plugin.layer.identity.crypto_customer.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.AsymmetricCryptography;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.interfaces.KeyPair;
import com.bitdubai.fermat_cbp_api.all_definition.exceptions.CantCreateMessageSignatureException;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_broker.ExposureLevel;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_customer.interfaces.CryptoCustomerIdentity;

/**
 * Created by jorge on 28-09-2015.
 */
public class CryptoCustomerIdentityImpl implements CryptoCustomerIdentity {

    private static final int HASH_PRIME_NUMBER_PRODUCT = 4259;
    private static final int HASH_PRIME_NUMBER_ADD = 3089;

    private final String alias;
    private final String privateKey;
    private final String publicKey;
    private byte[] profileImage;
    private final boolean published;

    public CryptoCustomerIdentityImpl(final String alias, final KeyPair keyPair, final byte[] profileImage, final boolean published){
        this.alias = alias;
        this.privateKey = keyPair.getPrivateKey();
        this.publicKey = keyPair.getPublicKey();
        this.profileImage = profileImage;
        this.published = published;
    }

    public CryptoCustomerIdentityImpl(final String alias,
                                      final String privateKey,
                                      final String publicKey,
                                      final byte[] profileImage,
                                      final boolean published){
        this.alias = alias;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.profileImage = profileImage;
        this.published = published;
    }

    @Override
    public String getAlias() {
        return alias;
    }


    @Override
    public String getPublicKey() {
        return publicKey;
    }

    @Override
    public byte[] getProfileImage() {
        return profileImage;
    }


    @Override
    public void setNewProfileImage(byte[] imageBytes) {
        this.profileImage = imageBytes;
    }

    @Override
    public boolean isPublished(){ return this.published; }

    @Override
    public ExposureLevel getExposureLevel() {
        return ExposureLevel.PUBLISH;
    }

    @Override
    public String createMessageSignature(String message) throws CantCreateMessageSignatureException{
        try{
            return AsymmetricCryptography.createMessageSignature(message, privateKey);

        } catch(Exception ex){
            throw new CantCreateMessageSignatureException(CantCreateMessageSignatureException.DEFAULT_MESSAGE, ex, "Message: "+ message, "The message could be invalid");
        }
    }


    public boolean equals(Object o){
        if(!(o instanceof CryptoCustomerIdentity))
            return false;
        CryptoCustomerIdentity compare = (CryptoCustomerIdentity) o;
        return alias.equals(compare.getAlias()) && publicKey.equals(compare.getPublicKey());
    }

    @Override
    public int hashCode(){
        int c = 0;
        c += alias.hashCode();
        c += privateKey.hashCode();
        c += publicKey.hashCode();
        return 	HASH_PRIME_NUMBER_PRODUCT * HASH_PRIME_NUMBER_ADD + c;
    }
}
