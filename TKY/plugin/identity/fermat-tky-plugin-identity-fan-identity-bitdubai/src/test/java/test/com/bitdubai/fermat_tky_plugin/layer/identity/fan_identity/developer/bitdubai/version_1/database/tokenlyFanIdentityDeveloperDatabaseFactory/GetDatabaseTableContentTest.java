package test.com.bitdubai.fermat_tky_plugin.layer.identity.fan_identity.developer.bitdubai.version_1.database.tokenlyFanIdentityDeveloperDatabaseFactory;

import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_tky_plugin.layer.identity.fan_identity.developer.bitdubai.version_1.database.TokenlyFanIdentityDeveloperDatabaseFactory;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by gianco on 06/05/16.
 */
public class GetDatabaseTableContentTest {
    @Mock
    DeveloperObjectFactory developerObjectFactory;
    @Mock
    DeveloperDatabaseTable developerDatabaseTable;
    @Mock
    List<DeveloperDatabaseTableRecord> developerDatabaseTableRecord;
    @Test
    public void getDatabaseTableContentTest() {
        TokenlyFanIdentityDeveloperDatabaseFactory tokenlyFanIdentityDeveloperDatabaseFactory = Mockito.mock(TokenlyFanIdentityDeveloperDatabaseFactory.class);

        when(tokenlyFanIdentityDeveloperDatabaseFactory.getDatabaseTableContent(developerObjectFactory,
                                                                                developerDatabaseTable)).thenReturn(developerDatabaseTableRecord);

    }
}
