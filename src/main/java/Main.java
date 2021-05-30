import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String args[]){
        try {
            var repo = new GoogleSheetsRepository(new GoogleServicesAuth().getSpreadSheetService());
            repo.setData(List.of("1", "1", "1", "1", "1"));
            System.out.println(repo.getSum());
            System.out.println(repo.getAverage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
