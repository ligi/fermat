package com.bitdubai.smartwallet.platform.layer._2_os.android.developer.bitdubai.version_1.file_system;

import android.content.Context;
import com.bitdubai.smartwallet.platform.layer._2_os.*;

/**
 * Created by ciencias on 20.01.15.
 */
public class AndroidFileSystem implements FileSystem {

    Context mContext;

    @Override
    public PlatformFile getFile(String fileName) throws FileNotFoundException{

        throw new FileNotFoundException();
        //return null;
    }

    @Override
    public PlatformFile createFile(String fileName, FilePrivacy privacyLevel, FileLifeSpan lifeSpan) {

        return new AndroidFile(mContext, fileName, privacyLevel, lifeSpan);
    }

    @Override
    public void setContext(Object context) {
        mContext = (Context) context;
    }
}
