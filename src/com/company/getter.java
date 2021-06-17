package com.company;

import java.util.*;

public class CollectionGetterSetter {
    private List<String> listTitles;

    public void setListTitles(List<String> titles) {
        this.listTitles = titles;
    }

    public List<String> getListTitles() {
        return this.listTitles;
    }

    public static void main(String[] args) {
        CollectionGetterSetter app = new CollectionGetterSetter();
        List<String> titles1 = new ArrayList();
        titles1.add("Name");
        titles1.add("Address");
        titles1.add("Email");
        titles1.add("Job");
        app.setListTitles(titles1);

        System.out.println("Titles 1: " + titles1);
        System.out.println();
        System.out.println();
        titles1.set(2, "Habilitation");

        List<String> titles2 = app.getListTitles();
        System.out.println("Titles 2: " + titles2);

        titles2.set(0, "Full name");

        List<String> titles3 = app.getListTitles();
        System.out.println("Titles 3: " + titles3);

        List<String> title4 = app.getListTitles();

        List<String> title5 = app.getListTitles();

    }

}


public class CollectionGetterSetterObject {
    private List<Person> listPeople = new ArrayList<Person>();

    public void setListPeople(List<Person> list) {
        for (Person aPerson : list) {
            this.listPeople.add((Person) aPerson.clone());
        }
    }

    public List<Person> getListPeople() {
        List<Person> listReturn = new ArrayList<Person>();
        for (Person aPerson : this.listPeople) {
            listReturn.add((Person) aPerson.clone());
        }

        return listReturn;
    }

    public static void main(String[] args) {
        CollectionGetterSetterObject app = new CollectionGetterSetterObject();

        List<Person> list1 = new ArrayList<Person>();
        list1.add(new Person("Peter"));
        list1.add(new Person("Alice"));
        list1.add(new Person("Mary"));

        app.setListPeople(list1);

        System.out.println("List 1: " + list1);

        list1.get(2).setName("Maryland");

        List<Person> list2 = app.getListPeople();
        System.out.println("List 2: " + list2);

        list1.get(0).setName("Peter Crouch");

        List<Person> list3 = app.getListPeople();
        System.out.println("List 3: " + list3);

    }
}