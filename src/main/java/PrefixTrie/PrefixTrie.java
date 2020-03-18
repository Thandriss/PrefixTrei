package PrefixTrie;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PrefixTrie {
    public static class Node {
        char inputName; //создаём узел с разветвлениями
        ArrayList<Node> next = new ArrayList<Node>();

        public Node(char c, ArrayList<Node> list) {
            inputName = c;
            next = list;
        }
        public Node () {
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


    Node root = new Node('.', new ArrayList<PrefixTrie.Node>()); //корень с разветвлениями
    private Exception ThrowEx (String s) {
        if (s.length() == 0 || s.equals(".") || s.matches("[0-9]+")) {
            throw new IllegalArgumentException();
        }
        return null;
    }


    private static ArrayList<Character> endIsEmpty(List<Node> nodes){
        ArrayList<Character> need = new ArrayList<Character>();
        if (nodes != null) {
            for (Node node: nodes) {
                if (node.next.isEmpty()) {
                    need.add(node.inputName);
                }
            }
        }
        return need;
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
            for (StringBuilder i: a) {
                StringBuilder d = new StringBuilder();
                d.append(tree.inputName);
                c.add(d.append(i));
            }
        }
        return c;
    }

    private static ArrayList<Integer> indNameOfEmptyEnd(List<Node> nodes) {
        int a = 0;
        ArrayList<Integer> need = new ArrayList<Integer>();
        if (nodes != null) {
            for (Node node: nodes) {
                if (node.next.isEmpty()) {
                    need.add(a);
                }
                a += 1;
            }
        }
        return need;
    }

    private static int findIndOfEmpty(List<Node> nodes, char c){
        ArrayList<Integer> list1 = indNameOfEmptyEnd(nodes);
        ArrayList<Character> list2 = endIsEmpty(nodes);
        int a = 0;
        for (Character i: list2) {
            if (i == c) return list1.get(a);
            a += 1;
        }
        return -1;
    }

    private static int indOfLetter (List<Node> nodes, char c) {
        int a = 0;
        for (Node node: nodes) {
            if (node.inputName == c && node.next.size() != 0) return a; else a += 1;
        }
        return -1;
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
        try {

            ThrowEx(s);
            Node begin = root;
            int a = 0;
            for (char letter : s.toLowerCase().toCharArray()) {
                if (!begin.next.contains(new Node(letter, new ArrayList<Node>()))) {
                    begin.next.add(new Node(letter, new ArrayList<Node>()));
                } else if (begin.next.contains(new Node(letter, new ArrayList<Node>())) && s.length() - 1 == a && !endIsEmpty(begin.next).contains(letter)) {
                    begin.next.add(new Node(letter, new ArrayList<Node>()));
                } else if (endIsEmpty(begin.next).contains(letter) && s.length() - 1 == a) {
                    return false;
                }
                a += 1;
                begin = getNode(begin.next, letter);
            }
            return true;
        } catch (IllegalArgumentException e) {
            throw  new IllegalArgumentException();
        }
    }


    public boolean delete (String s) {
        try {
            ThrowEx(s);
            ArrayList<Character> lettersToDelete = new ArrayList<Character>();
            Node startOfTree = root;
            int ind = 0;
            int save = 0;
            int len = s.length();
            for (char letter : s.toLowerCase().toCharArray()) { //находим номер уровня и букву, которую надо удалить
                if (startOfTree.next.contains(new Node(letter, new ArrayList<Node>()))) {
                    if (startOfTree.next.size() == 1 && (lettersToDelete.isEmpty())) {
                        lettersToDelete.add(letter);
                        ind = save;
                    } else if (startOfTree.next.size() > 1) {
                        lettersToDelete.clear();
                        ind = save;
                        lettersToDelete.add(letter);
                    }
                } else return false;
                save = save + 1;
                startOfTree = getNode(startOfTree.next, letter);
            }
            Node begin = root;
            int level = 0;
            for (char letterOne : s.toLowerCase().toCharArray()) { //удаление не нужной ноды
                if (begin.next.contains(new Node(letterOne, new ArrayList<Node>())) && !lettersToDelete.isEmpty() && letterOne == lettersToDelete.get(0) && level == ind) {
                    if (endIsEmpty(begin.next).contains(letterOne) && len - 1 == level) { // случай, если у нас пустые дети
                        begin.next.remove(findIndOfEmpty(begin.next, letterOne));
                        break;
                    } else {
                        begin.next.remove(indOfLetter(begin.next, letterOne)); // случай обычный с детьми
                    }
                } else if (begin.next.contains(new Node(letterOne, new ArrayList<Node>()))) {
                    begin = getNode(begin.next, letterOne);// переход дальше
                }
                level += 1;
            }
            return true;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }


    public boolean find (String s) {
        try {
            ThrowEx(s);
            int a = 0;
            Node start = root;
            int len = s.length();
            for (char letter : s.toLowerCase().toCharArray()) {
                if (!start.next.contains(new Node(letter, new ArrayList<Node>()))) {
                    return false;
                } else {
                    if (endIsEmpty(start.next).contains(letter) && a == len - 1) {
                        return true;
                    } else if (a == len - 1 && !endIsEmpty(start.next).contains(letter)) {
                        return false;
                    } else start = getNode(start.next, letter);
                }
                a += 1;
            }
            return true;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }


    public List<String> findAll (String s) {
        try {
            ArrayList<String> words = new ArrayList<String>();// слова, которые должны вывестись
            Node start = root;
            StringBuilder first = new StringBuilder();

            ThrowEx(s);
            int len1 = s.length();
            int count = 0;
            for (char letter : s.toLowerCase().toCharArray()) { // ищем конечный узел от строки от заданной строки и прполняем слово
                if (start.next.contains(new Node(letter, new ArrayList<Node>()))) {
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
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }
}
