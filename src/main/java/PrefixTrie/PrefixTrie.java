package PrefixTrie;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PrefixTrie {
    public static class Node {
        char inputName; //создаём узел с разветвлениями
        ArrayList<Node> next = new ArrayList<Node>();
        boolean leaf; //флаг окончания
        public Node(char c, ArrayList<Node> list, boolean flag) {
            inputName = c;
            next = list;
            leaf = flag;
        }
        @Override
        public int hashCode() {
            return Objects.hash(inputName, next);
        }
        @Override
        public boolean equals(Object o) {
            if (o instanceof Node) {
                return inputName == ((Node) o).inputName;
            } else return false;
        }
    }

    Node root = new Node('.', new ArrayList<PrefixTrie.Node>(), false); //корень с разветвлениями
    private boolean ThrowEx (String s) {
        return s.length() == 0 || s.equals(".") || s.matches("[0-9]+");
    }
    private ArrayList<StringBuilder> getAllStrings(Node tree) {
        ArrayList<StringBuilder> a = new ArrayList<StringBuilder>();
        ArrayList<StringBuilder> c = new ArrayList<StringBuilder>();
        if (tree.next.isEmpty()) {
            StringBuilder b = new StringBuilder();
            b.append(tree.inputName);
            a.add(new StringBuilder(b));
            return a;
        } else {
            for (Node i: tree.next) {
                a.addAll(getAllStrings(i));
            }
            if (tree.leaf && !tree.next.isEmpty()) a.add(new StringBuilder(tree.inputName));
            for (StringBuilder i: a) {
                StringBuilder d = new StringBuilder();
                d.append(tree.inputName);
                c.add(d.append(i));
            }
        }
        return c;
    }

    private static Node getNode(List<Node> nodes, char c) {
        ArrayList<Node> need = new ArrayList<Node>();
        for (Node node: nodes) {
            if (c == node.inputName) {
                return node;
            }
        }
        return null;
    }

    public boolean put (String s) {
        if (ThrowEx(s)) throw new IllegalArgumentException();
        Node begin = root;
        int a = 0;
        for (char letter : s.toLowerCase().toCharArray()) {
            if (!begin.next.contains(new Node(letter, new ArrayList<Node>(), false))) {
                begin.next.add(new Node(letter, new ArrayList<Node>(), false));
            } else if (begin.next.contains(new Node(letter, new ArrayList<Node>(), false)) && s.length() - 1 == a) {
                if (getNode(begin.next, letter).leaf) return false;
            }
            a += 1;
            begin = getNode(begin.next, letter);
        }
        begin.leaf = true;
        return true;
    }

    public boolean delete (String s) {
        if (ThrowEx(s)) throw new IllegalArgumentException();
        ArrayList<Character> lettersToDelete = new ArrayList<Character>();
        Node startOfTree = root;
        int ind = 0;
        int save = 0;
        int len = s.length();
        boolean empty = false; // обозначает, что надо удалять не полное слово
        for (char letter : s.toLowerCase().toCharArray()) { //находим номер уровня и букву, которую надо удалить или убрать флаг "окончания"
            if (startOfTree.next.contains(new Node(letter, new ArrayList<Node>(), false))) {
                if (startOfTree.next.size() == 1) {
                    if (lettersToDelete.isEmpty() || (getNode(startOfTree.next, letter).leaf && len - 1 == save)) {
                        empty = false;
                        if (getNode(startOfTree.next, letter).leaf && len - 1 == save && !getNode(startOfTree.next, letter).next.isEmpty()) {
                            lettersToDelete.clear();
                            empty = true;
                            lettersToDelete.add(letter);
                            ind = save;
                        }
                        if (lettersToDelete.isEmpty()) {
                            lettersToDelete.add(letter);
                            ind = save;
                        }
                    }
                } else if (startOfTree.next.size() > 1) {
                    lettersToDelete.clear();
                    ind = save;
                    lettersToDelete.add(letter);
                    if (getNode(startOfTree.next, letter).leaf && len - 1 == save) empty = true;
                    else empty = false;
                }
            } else return false;
            save = save + 1;
            startOfTree = getNode(startOfTree.next, letter);
        }
        Node begin = root;
        int level = 0;
        for (char letterOne : s.toLowerCase().toCharArray()) { //удаление не нужной ноды
            if (begin.next.contains(new Node(letterOne, new ArrayList<Node>(), false)) && !lettersToDelete.isEmpty()
                    && letterOne == lettersToDelete.get(0) && level == ind) {
                if (!getNode(begin.next, letterOne).leaf && len - 1 == level) { // случай, если у нас пустые дети
                    begin.next.remove(new Node(letterOne, new ArrayList<>(), false));
                    break;
                } else if (empty && len - 1  == level) {
                    getNode(begin.next, letterOne).leaf = false;
                    break;
                } else {
                    begin.next.remove(getNode(begin.next, letterOne));
                    break;// случай обычный с детьми
                }
            } else if (begin.next.contains(new Node(letterOne, new ArrayList<Node>(), false))) {
                begin = getNode(begin.next, letterOne);// переход дальше
            }
            level += 1;
        }
        return true;
    }

    public boolean find (String s) {
        if (ThrowEx(s)) throw new IllegalArgumentException();
        int a = 0;
        Node start = root;
        int len = s.length();
        for (char letter : s.toLowerCase().toCharArray()) {
            if (!start.next.contains(new Node(letter, new ArrayList<Node>(), false))) {
                return false;
            } else {
                if (getNode(start.next, letter).next.isEmpty() && a == len - 1) {
                    return true;
                } else if (a == len - 1 && !getNode(start.next, letter).next.isEmpty()) {
                    return false;
                } else if (a == len - 1 && getNode(start.next, letter).leaf) return true;
                else start = getNode(start.next, letter);
            }
            a += 1;
        }
        return true;
    }

    public List<String> findAll (String s) {
        ArrayList<String> words = new ArrayList<String>();// слова, которые должны вывестись
        Node start = root;
        StringBuilder first = new StringBuilder();

        if (ThrowEx(s)) throw new IllegalArgumentException();
        int len1 = s.length();
        int count = 0;
        for (char letter : s.toLowerCase().toCharArray()) { // ищем конечный узел от строки от заданной строки и прполняем слово
            if (start.next.contains(new Node(letter, new ArrayList<Node>(), false))) {
                if (count != len1 - 1) first.append(letter);
                start = getNode(start.next, letter);
            } else return words;
            count += 1;
        }
        Node rem = start;
        ArrayList<StringBuilder> futurePtTwo = getAllStrings(rem);
        for (StringBuilder i : futurePtTwo) {
            int len = i.length();
            first.append(i);
            words.add(first.toString());
            first.delete(first.length() - len, first.length());
        }
        return words;
    }
}
