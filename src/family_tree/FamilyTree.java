package family_tree;

import service.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FamilyTree<T extends Creature<T>> implements Iterable<T>, Serializable {
    private List<T> familyTree;

    public FamilyTree() {
        this.familyTree = new ArrayList<>();

    }

    public Creature getTop() {
        Service<T> service = new Service<T>();
        topOfTree(familyTree.getFirst());
        return service.getTreeTop();
    }

    public boolean setSpouse(T firstSpouce, T secondSpouce) {
        if (firstSpouce.getSpouse() == null && secondSpouce.getSpouse() == null) {
            firstSpouce.setSpouse(secondSpouce);
            secondSpouce.setSpouse(firstSpouce);
        } else {
            System.out.println(firstSpouce.getName() + " или " + secondSpouce.getName() + " уже имеют супруга, пожалуйста" +
                    "проставьте корректные статусы");
            return false;
        }
        if (!firstSpouce.isInTree()) {
            familyTree.add(firstSpouce);
            firstSpouce.setInTree();
        }
        if (!secondSpouce.isInTree()) {
            familyTree.add(secondSpouce);
            secondSpouce.setInTree();
        }
        System.out.println("Вы указали, что: " + firstSpouce.getName() + " и " + secondSpouce.getName() + " женаты");
        return true;
    }

    public boolean divorce(T firstSpouce, T secondSpouce) {
        if (firstSpouce.equals(secondSpouce.getSpouse())) {
            firstSpouce.setSpouse(null);
            secondSpouce.setSpouse(null);
            System.out.println(firstSpouce.getName() + " и " + secondSpouce.getName() + "более не женаты");
        } else {
            System.out.println(firstSpouce.getName() + " или " + secondSpouce.getName() + "не имеют супруга, пожалуйста" +
                    "проставьте корректные статусы");
        }
        return true;
    }

    public boolean setMother(T child, T mother) {
        child.setMother(mother);
        if (!child.isInTree()) {
            familyTree.add(child);
            child.setInTree();
        }
        if (!mother.isInTree()) {
            familyTree.add(mother);
            mother.setInTree();
        }
        if (mother.getChildren() != null && mother.getChildren().contains(child)) {
            System.out.println("Такой ребенок уже задан");
            return false;
        } else {
            mother.setChildren(child);
            System.out.println("Вы указали, что: " + mother.getName() + " мать " + child.getName());
        }
        return true;
    }

    public boolean setChildren(T parent, T child) {
        if (child.isInTree()) {
            familyTree.add(child);
            child.setInTree();
        }
        if (parent.isInTree()) {
            familyTree.add(parent);
            parent.setInTree();
        }
        if (parent.getChildren() != null && parent.getChildren().contains(child)) {
            System.out.println("Такой ребенок уже задан");
            return false;
        } else {
            parent.setChildren(child);
            System.out.println("Вы указали, что: " + child.getName() +
                    " является ребенком " + parent.getName());
        }
        return true;
    }

    public boolean setFather(T child, T father) {
        child.setFather(father);
        if (child.isInTree()) {
            familyTree.add(child);
            child.setInTree();
        }
        if (!father.isInTree()) {
            familyTree.add(father);
            father.setInTree();
        }
        if (father.getChildren() != null && father.getChildren().contains(child)) {
            System.out.println("Такой ребенок уже задан");
            return false;
        } else {
            father.setChildren(child);
            System.out.println("Вы указали, что: " + father.getName() + " отец " + child.getName());
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("Члены данной семьи: \n");
        for (T human : familyTree) {
            sb.append(human.getName()).append("\n");
        }
        return sb.toString();
    }

    public void sortByName() {
        familyTree.sort(new HumanComporatorByName());
    }

    public void sortByAge() {
        familyTree.sort(new HumanComporatorByAge());
    }

    //TODO: Дописать метод, который рекурсивно собирает дерево семьи
    public String showTree() {
        Service<T> service = new Service<T>();
        topOfTree(familyTree.getFirst());
        return showTreeService((T) service.getTreeTop());
    }

    private String showTreeService(T top) {
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        if (top != null) {
            sb.append(String.format("1. Старший член семейства: %s, %s г. р.", top.getName(), top.getBirthDate()));
            if (top.getSpouse() != null) {
                sb.append(String.format("Супруг старшего члена семейства: %s", top.getSpouse().getName()));
            }
            sb.append("Дети старшего члена семейства: ");
            for (T child : top.getChildren()) {
                if (flag) sb.append(", ");
                sb.append(child.getName());
                flag = true;
            }
        }
        return sb.toString();
    }

    private boolean topOfTree(T topEnter) {
        if (topEnter != null && topEnter.getFather() == null && topEnter.getMother() == null) {
            if (topEnter.getSpouse() != null) {
                if (topEnter.getSpouse().getFather() != null || topEnter.getSpouse().getMother() != null) {
                    topOfTree(topEnter.getSpouse());
                }
            } else {
                Service.setTreeTop(topEnter);
            }
        }
        if (topEnter != null) {
            topOfTree(topEnter.getFather());
            topOfTree(topEnter.getMother());
        }

        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new HumanIterator<T>(familyTree);
    }

}



