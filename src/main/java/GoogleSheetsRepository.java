import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GoogleSheetsRepository {

    private static final String DATA_RANGE = "Sheet1!A1:A5";
    private static final String SUM_RANGE = "Sheet1!A6";
    private static final String AVG_RANGE = "Sheet1!A7";

    private final String SPREADSHEET_ID;
    private final Sheets.Spreadsheets spreadsheets;

    public GoogleSheetsRepository(Sheets.Spreadsheets spreadsheets){
        this(spreadsheets, System.getenv("SHEET_ID"));
    }

    public GoogleSheetsRepository(Sheets.Spreadsheets spreadsheets, String sheetId){
        this.spreadsheets = spreadsheets;
        this.SPREADSHEET_ID = sheetId;
    }

    public void setData(List<String> data) throws Exception {
        if(data.size() != 5) throw new Exception("Expected data list of size 5");

        var updateValues = new ValueRange()
                .setValues(data
                        .stream()
                        .map(e -> List.of((Object) e))
                        .collect(Collectors.toList()));

        spreadsheets
                .values()
                .update(SPREADSHEET_ID, DATA_RANGE, updateValues)
                .setValueInputOption("USER_ENTERED")
                .execute();
    }

    public String getAverage() throws IOException {
        return getRange(AVG_RANGE);
    }

    public String getSum() throws IOException {
        return getRange(SUM_RANGE);
    }

    private String getRange(String range) throws IOException {
        return spreadsheets.values()
                .get(SPREADSHEET_ID, range)
                .execute()
                .getValues()
                .stream()
                .flatMap(Collection::stream)
                .map(Object::toString)
                .collect(Collectors.joining(", "));
    }

}
