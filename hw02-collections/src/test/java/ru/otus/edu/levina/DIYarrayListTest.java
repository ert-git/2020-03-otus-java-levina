package ru.otus.edu.levina;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;

import org.junit.Assert;
import org.junit.Test;

/**
 * “ест основан на сравнении поведени€ DIYarrayList с поведением ArrayList 
 * @author levina
 *
 */
public class DIYarrayListTest {

    @Test
    public void test_01_listIterator() {
        ArrayList<String> arl = new ArrayList<>();
        Collections.addAll(arl, "s1", "s2", "s3");
        ListIterator<String> arlIt = arl.listIterator();

        DIYarrayList<String> myl = new DIYarrayList<>();
        Collections.addAll(myl, "s1", "s2", "s3");
        ListIterator<String> mylIt = myl.listIterator();

        Assert.assertEquals(arlIt.next(), mylIt.next());
        Assert.assertEquals(arlIt.previousIndex(), mylIt.previousIndex());
        Assert.assertEquals(arlIt.nextIndex(), mylIt.nextIndex());

        Assert.assertEquals(arlIt.next(), mylIt.next());
        Assert.assertEquals(arlIt.previousIndex(), mylIt.previousIndex());
        Assert.assertEquals(arlIt.nextIndex(), mylIt.nextIndex());

        Assert.assertEquals(arlIt.previous(), mylIt.previous());
        Assert.assertEquals(arlIt.previousIndex(), mylIt.previousIndex());
        Assert.assertEquals(arlIt.nextIndex(), mylIt.nextIndex());

        Assert.assertEquals(arlIt.previous(), mylIt.previous());
        Assert.assertEquals(arlIt.previousIndex(), mylIt.previousIndex());
        Assert.assertEquals(arlIt.nextIndex(), mylIt.nextIndex());

        arlIt.set("test1");
        mylIt.set("test1");
        Assert.assertEquals("test1", arlIt.next());
        Assert.assertEquals("test1", mylIt.next());

        arlIt.set("test2");
        mylIt.set("test2");

        Assert.assertEquals(arl.size(), myl.size());
        for (int i = 0; i < arl.size(); i++) {
            Assert.assertEquals(arl.get(i), myl.get(i));
        }
    }

    @Test
    public void test_02_addAll() {
        // Collections.addAll(Collection<? super T> c, T... elements)
        int n = 30;
        String[] newEls = new String[n];
        for (int i = 0; i < newEls.length; i++) {
            newEls[i] = "s" + i;
        }

        DIYarrayList<String> myl = new DIYarrayList<>();
        myl.add("s01");
        myl.add("s02");
        Collections.addAll(myl, newEls);

        ArrayList<String> arl = new ArrayList<>();
        arl.add("s01");
        arl.add("s02");
        Collections.addAll(arl, newEls);

        Assert.assertEquals(arl.size(), myl.size());
        for (int i = 0; i < arl.size(); i++) {
            Assert.assertEquals(arl.get(i), myl.get(i));
        }

    }

    @Test
    public void test_03_copy() {
        // Collections.static <T> void copy(List<? super T> dest, List<? extends T> src)
        int n = 30;
        String[] srcEls = new String[n];
        for (int i = 0; i < srcEls.length; i++) {
            srcEls[i] = "s" + i;
        }

        String[] destEls = new String[n];
        for (int i = 0; i < destEls.length; i++) {
            destEls[i] = "d" + i;
        }

        DIYarrayList<String> mylSrc = new DIYarrayList<>();
        Collections.addAll(mylSrc, srcEls);

        ArrayList<String> arlSrc = new ArrayList<>();
        Collections.addAll(arlSrc, srcEls);

        // check if orig lists are the same
       Assert.assertEquals(arlSrc.size(), mylSrc.size());
        for (int i = 0; i < arlSrc.size(); i++) {
            Assert.assertEquals(arlSrc.get(i), mylSrc.get(i));
        }

        DIYarrayList<String> mylDest = new DIYarrayList<>();
        Collections.addAll(mylDest, destEls);

        ArrayList<String> arlDest = new ArrayList<>();
        Collections.addAll(arlDest, destEls);

        Collections.copy(arlDest, arlSrc);
        Collections.copy(mylDest, mylSrc);

        Assert.assertEquals(arlDest.size(), mylDest.size());
        for (int i = 0; i < arlDest.size(); i++) {
            Assert.assertEquals(arlDest.get(i), mylDest.get(i));
        }

    }

    @Test
    public void test_04_sort() {
        //Collections.static <T> void sort(List<T> list, Comparator<? super T> c)
        int n = 30;
        String[] srcEls = new String[n];
        for (int i = 0; i < srcEls.length; i++) {
            srcEls[i] = "s" + i;
        }

        DIYarrayList<String> mylSrc = new DIYarrayList<>();
        Collections.addAll(mylSrc, srcEls);

        ArrayList<String> arlSrc = new ArrayList<>();
        Collections.addAll(arlSrc, srcEls);

        // check if orig lists are the same
        Assert.assertEquals(arlSrc.size(), mylSrc.size());
        for (int i = 0; i < arlSrc.size(); i++) {
            Assert.assertEquals(arlSrc.get(i), mylSrc.get(i));
        }
        
        Comparator<String> reverseComparator = new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        };

        Collections.sort(arlSrc, reverseComparator);
        Collections.sort(mylSrc, reverseComparator);

        Assert.assertEquals(arlSrc.size(), mylSrc.size());
        for (int i = 0; i < arlSrc.size(); i++) {
            Assert.assertEquals(arlSrc.get(i), mylSrc.get(i));
        }

    }
}
