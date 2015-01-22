package com.bitdubai.smartwallet.platform.layer._2_os;

/**
 * Created by ciencias on 20.01.15.
 */
public interface FileSystem {

    public PlatformFile getFile (String fileName) throws FileNotFoundException;

    public PlatformFile createFile (String fileName, FilePrivacy privacyLevel, FileLifeSpan lifeSpan );

    void setContext (Object context);

}
