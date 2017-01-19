
import com.larskroll.common.J6;
import java.util.Collection;
import java.util.TreeSet;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Lars Kroll <lkroll@kth.se>
 */
public class J6Test {

//    public J6Test() {
//    }
    @Test
    public void randomCollectionTest() {
        Collection<Integer> cs = new TreeSet<Integer>();
        for (int i = 0; i < 20; i++) {
            cs.add(i);
        }
        for (int i = 0; i < 200000; i++) {
            int rand = J6.randomElement(cs);
            System.out.print(rand);
            Assert.assertTrue(cs.contains(rand));
        }
    }
}
