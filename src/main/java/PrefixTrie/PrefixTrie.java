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
            if (tree.leaf && !tree.next.isEmpty()) a.add(new StringBuilder());
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
        if (ThrowEx(s)) throw new IllegalArgumentException();//проверка на исключение
        ArrayList<Character> lettersToDelete = new ArrayList<Character>();// сохранение буквы, которую надо удалить
        Node startOfTree = root;//корень
        int ind = 0;// сохранённый индекс уровня для удаления ноды
        int save = 0;// текущий индекс уровня в первом цикле,
        int len = s.length();
        boolean empty = false; //этот флаг обозначает, что надо удалять не полное слово, а всего лишь убрать флаг окончания
        for (char letter : s.toLowerCase().toCharArray()) { //находим номер уровня и букву, которую надо удалить или убрать флаг "окончания"
            if (startOfTree.next.contains(new Node(letter, new ArrayList<Node>(), false))) {//проверка на наличие буквы
                if (startOfTree.next.size() == 1) {// условие, если в ассоциативном массиве один элемент
                    if (lettersToDelete.isEmpty() || (getNode(startOfTree.next, letter).leaf && len - 1 == save)) {
                        empty = false;
                        if (getNode(startOfTree.next, letter).leaf && len - 1 == save && !getNode(startOfTree.next, letter).next.isEmpty()) {//если ключевое слово заканчивается, а последняя буква слова в дереве имеет на пустые массивы дальше
                            lettersToDelete.clear();//удаляем, сохранённые буквы раньше
                            empty = true;//флаг, что надо убрать окончание слова(поменять с true на false)
                            lettersToDelete.add(letter);//добавить букву, котовая будет "удалена", как окончание слова
                            ind = save;//сохраняем индекс уровня, на котором находится последняя буква
                        }
                        if (lettersToDelete.isEmpty()) {//какая-то первоначальная буква, которая, возможно будет удалена
                            lettersToDelete.add(letter);//сохраняем букву
                            ind = save;//сохраняем индекс
                        }
                    }
                } else if (startOfTree.next.size() > 1) {// условие, если в ассоциативном массиве больше одного элемента
                    lettersToDelete.clear();//"забываем" буквы до
                    ind = save;// сохраняем индекс
                    lettersToDelete.add(letter);// сохраняем новую букву
                    if (getNode(startOfTree.next, letter).leaf && len - 1 == save && !getNode(startOfTree.next, letter).next.isEmpty()) empty = true;//флаг, что надо убрать окончание слова(поменять с true на false)
                    else empty = false;//флаг, что ничего убирать не надо
                }
            } else return false; // если нет буквы, то нет удаления
            save = save + 1;//текущий индекс уровня
            startOfTree = getNode(startOfTree.next, letter);//переход на новый узел по букве
        }
        Node begin = root;//заново проходим по всему дереву
        int level = 0;//первоначальный уровень
        for (char letterOne : s.toLowerCase().toCharArray()) { //удаление не нужной ноды
            if (begin.next.contains(new Node(letterOne, new ArrayList<Node>(), false)) && !lettersToDelete.isEmpty()
                    && letterOne == lettersToDelete.get(0) && level == ind) {// условие, при котором находится уровень и буква,которую надо удалить, чтобы удалилось слово
                if (empty && len - 1  == level) {//случай, если нам надо убрать флаг об "окончании" слова
                    getNode(begin.next, letterOne).leaf = false;//изменяем флаг
                    break;
                } else {//случаи, когда буква, которую надо удалить находится нанужном нам уровне, и нам всё равно, есть ли дети. Она должна быть удалена
                    begin.next.remove(getNode(begin.next, letterOne));// удаление
                    break;
                }
            } else if (begin.next.contains(new Node(letterOne, new ArrayList<Node>(), false))) {//условие перехода на следующий узел
                begin = getNode(begin.next, letterOne);// переход дальше
            }
            level += 1;//изменение текущего уровня
        }
        return true;//удаление проведено успешно
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
