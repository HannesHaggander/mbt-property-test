import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Assert;
import org.junit.runner.RunWith;

@RunWith(JUnitQuickcheck.class)
public class test {

    @Property
    public void concat(String s1, String s2){
        Assert.assertEquals(s1.length() + s2.length(), (s1 + s2).length());
    }

    @Property
    public void add(int i1, int i2){
        Assert.assertEquals(i1 + i2, i1 + i2);
    }

}
