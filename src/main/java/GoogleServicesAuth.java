import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.List;

public class GoogleServicesAuth {

    private static final String APPLICATION_NAME = "Sheets Property Testing";
    private static final String CREDENTIALS_PATH = "credentials.json";
    private static final String TOKEN_DIRECTORY_PATH = "tokens";
    private static final String ACCESS_TYPE = "offline";
    private static final String AUTHORIZE_USER = "user";

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static final List<String> SCOPES = List.of(SheetsScopes.SPREADSHEETS);

    private static final int PORT = 8888;

    private static NetHttpTransport HTTP_TRANSPORT;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    private Credential authenticate() throws IOException {
        File credentials = new File(CREDENTIALS_PATH);
        GoogleClientSecrets secrets = GoogleClientSecrets.load(JSON_FACTORY, new FileReader(credentials));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow
                .Builder(HTTP_TRANSPORT, JSON_FACTORY, secrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKEN_DIRECTORY_PATH)))
                .setAccessType(ACCESS_TYPE)
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver
                .Builder()
                .setPort(PORT)
                .build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize(AUTHORIZE_USER);
    }

    public Sheets.Spreadsheets getSpreadSheetService() throws IOException {
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, authenticate())
                .setApplicationName(APPLICATION_NAME)
                .build()
                .spreadsheets();
    }

}
