package PrefixTrie;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrefixTrieTest {
    PrefixTrie tree = new PrefixTrie();


    @Test
    void put() {
        assertTrue(tree.put("привет"));
        assertTrue(tree.put("таракашка"));
        assertTrue(tree.put("приз"));
        assertTrue(tree.put("приве"));
        assertTrue(tree.put("тр"));
        assertFalse(tree.put("тр"));
        assertFalse(tree.put("приве"));
        assertFalse(tree.put("привет"));
        assertThrows(IllegalArgumentException.class, () -> tree.put("."));
        assertThrows(IllegalArgumentException.class, () -> tree.put("8"));
        assertThrows(IllegalArgumentException.class, () -> tree.put(""));
    }

    @Test
    void delete() {
        tree.put("привилегия");
        tree.put("трамвай");
        tree.put("тромб");
        tree.put("тр");
        assertTrue(tree.delete("тр"));
        assertTrue(tree.delete("трамвай"));
        assertTrue(tree.delete("тромб"));
        assertTrue(tree.delete("привилегия"));
        assertThrows(IllegalArgumentException.class, () -> tree.delete("."));
        assertThrows(IllegalArgumentException.class, () -> tree.delete("356"));
        assertThrows(IllegalArgumentException.class, () -> tree.delete(""));
    }

    @Test
    void find() {
        tree.put("кентавр");
        tree.put("ревень");
        tree.put("приветик");
        assertFalse(tree.find("мексиканец"));
        assertTrue(tree.find("кентавр"));
        assertTrue(tree.find("ревень"));
        assertFalse(tree.find("привет"));
        assertThrows(IllegalArgumentException.class, () -> tree.find("."));
        assertThrows(IllegalArgumentException.class, () -> tree.find("356"));
        assertThrows(IllegalArgumentException.class, () -> tree.find(""));
    }


    @Test
    void findAll() {
        tree.put("хомяк");
        String a = "хомяк";
        ArrayList<String> expected = new ArrayList<String>();
        expected.add(a);
        assertArrayEquals(expected, (ArrayList<String>) tree.findAll("х"));
        tree.put("привередливый");
        tree.put("привередливые");
        tree.put("прирост");
        tree.put("привет");
        tree.put("пригвоздить");
        tree.put("привод");
        tree.put("приво");
        expected.clear();
        expected.add("привередливый");
        expected.add("привередливые");
        expected.add("привет");
        expected.add("привод");
        expected.add("приво");
        expected.add("прирост");
        expected.add("пригвоздить");
        assertArrayEquals(expected, (ArrayList<String>) tree.findAll("п"));
        expected.clear();
        assertArrayEquals(expected, (ArrayList<String>) tree.findAll("яр"));
        tree.put("метоксихлордиэтиламинометилбутинаминоакредин");
        expected.add("метоксихлордиэтиламинометилбутинаминоакредин");
        assertArrayEquals(expected, (ArrayList<String>) tree.findAll("м"));
        assertThrows(IllegalArgumentException.class, () -> tree.findAll("."));
        assertThrows(IllegalArgumentException.class, () -> tree.findAll(""));
        assertThrows(IllegalArgumentException.class, () -> tree.findAll("6"));
    }

    private boolean assertArrayEquals(List<String> expected, List<String> actual) {
        if (expected.size() == actual.size()) {
            for (int i = 0; i != expected.size(); i++) {
                if (expected.get(i).equals(actual.get(i))) {
                    continue;
                } else throw new AssertionError();
            }
        } else throw new AssertionError();
        return true;
    }
}