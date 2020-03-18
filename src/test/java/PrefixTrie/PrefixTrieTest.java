package PrefixTrie;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PrefixTrieTest {
    PrefixTrie tree = new PrefixTrie();


    @Test
    void put() {
        assertTrue(tree.put("привет"));
        assertTrue(tree.put("таракашка"));
        assertTrue(tree.put("приз"));
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
        assertTrue(tree.delete("трамвай"));
        assertTrue(tree.delete("тромб"));
        assertFalse(tree.find("привет"));
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
        tree.put("привгвоздить");
        tree.put("привод");
        tree.put("приво");
        ArrayList<String> expected1 = new ArrayList<String>();
        expected1.add("привередливый");
        expected1.add("привередливые");
        expected1.add("прирост");
        expected1.add("привет");
        expected1.add("пригвоздить");
        expected1.add("привод");
        expected1.add("приво");
        assertArrayEquals(expected1, (ArrayList<String>) tree.findAll("п"));
        ArrayList<String> expected2 = new ArrayList<String>();
        tree.put("метоксихлордиэтиламинометилбутинаминоакредин");
        expected2.add("метоксихлордиэтиламинометилбутинаминоакредин");
        assertArrayEquals(expected2, (ArrayList<String>) tree.findAll("м"));
        assertThrows(IllegalArgumentException.class, () -> tree.findAll("."));
        assertThrows(IllegalArgumentException.class, () -> tree.findAll(""));
        assertThrows(IllegalArgumentException.class, () -> tree.findAll("6"));
    }

    private boolean assertArrayEquals(ArrayList<String> expected, ArrayList<String> actual) {
        return expected.equals(actual);
    }
}