package PrefixTrie;

public class Main {
    public static void main (String[] args) {
        PrefixTrie tree = new PrefixTrie();
        tree.put("привередливый");
        tree.put("привередливые");
        tree.put("прирост");
        tree.put("привет");
        tree.put("пригвоздить");
        tree.put("привод");
        tree.put("приво");
        tree.put("голубь");
        tree.put("голубец");
        tree.put("в");
        System.out.println(tree.findAll("в"));
        System.out.println(tree.findAll("при"));
        System.out.println(tree.findAll("г"));
        tree.put("трамвай");
        System.out.println(tree.put("тромб"));
        System.out.println(tree.delete("тромб"));
        System.out.println(tree.find("тромб"));
        tree.put("тросик");
        tree.delete("трос");
        boolean a = tree.find("привет");
        System.out.println(a);
        System.out.println(tree.find("трос"));
    }
}
