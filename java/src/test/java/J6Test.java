/* 
* Copyright 2019 Lars Kroll
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software and 
* associated documentation files (the "Software"), to deal in the Software without restriction, 
* including without limitation the rights to use, copy, modify, merge, publish, distribute, 
* sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is 
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all copies or 
* substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
* INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE 
* AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
* DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import com.lkroll.common.J6;
import java.util.Collection;
import java.util.TreeSet;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Lars Kroll <lkroll@kth.se>
 */
public class J6Test {

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
