import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Assert;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(JUnitQuickcheck.class)
public class GoogleSheetPropertyTests {

    private final GoogleSheetsRepository repo;

    public GoogleSheetPropertyTests() throws IOException {
        repo = new GoogleSheetsRepository(new GoogleServicesAuth().getSpreadSheetService());
    }

    private String formatTwoDecimals(Double d) {
        return String.format("%.2f", d);
    }

    @Property(trials = 5)
    public void testAverage(double d1, double d2, double d3, double d4, double d5) throws Exception {
        List<Double> data = List.of(d1, d2, d3, d4, d5);
        String expected = formatTwoDecimals(data
            .stream()
            .mapToDouble(d -> d)
            .average()
            .orElse(0.0)
        );
        testSetData(data);
        String actual = formatTwoDecimals(Double.parseDouble(repo.getAverage()));
        Assert.assertEquals(expected, actual);
    }

    @Property(trials = 5)
    public void testSum(double d1, double d2, double d3, double d4, double d5) throws Exception {
        List<Double> data = List.of(d1, d2, d3, d4, d5);
        String expected = formatTwoDecimals(data
                .stream()
                .mapToDouble(d -> d)
                .sum()
        );
        testSetData(data);
        String actual = formatTwoDecimals(Double.parseDouble(repo.getSum()));
        Assert.assertEquals(expected, actual);
    }

    private void testSetData(List<Double> data) throws Exception {
        repo.setData(data.stream().map(Object::toString).collect(Collectors.toList()));
    }
}
