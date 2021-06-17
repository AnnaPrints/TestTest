package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

class RefactoringTest
{

}

//class
class ClassRenameTest {
    static int myMethod(int x, int y) //parameter
    {
        return x + y;
    }

    public void simpleInlineVariable()
    {
        int test = 1;
        int a = test + 1; //inline variable "test"
    }

    public void inlineVariable()
    {
        AnotherClass.InnerClass aClass = anotherClass.innerClass;
        int a = aClass.i; //inline variable "aClass"
    }

    public static void parameter(String args[])
    {
        Properties capitals = new Properties();
        Set states;
        String str; //field
        Boolean btest = true; //boolean variable

        capitals.put("Illinois", "Springfield");
        capitals.put("Missouri", "Jefferson City");
        capitals.put("Washington", "Olympia");
        capitals.put("California", "Sacramento"); //property
        capitals.put("Indiana", "Indianapolis");

        System.out.println(btest); //btest
        System.out.println(str);

        // Show all states and capitals in hashtable.
        states = capitals.keySet();   // get set-view of keys
        Iterator itr = states.iterator();

        while(itr.hasNext()) {
            str = (String) itr.next();
            System.out.println("The capital of " + str + " is " +
                    capitals.getProperty(str) + ".");
        }
        System.out.println();

        // look for state not in list -- specify default
        str = capitals.getProperty("Florida", "Not Found");
        System.out.println("The capital of Florida is " + str + ".");
    }

}

class ChildTestRenameTest extends ClassRenameTest
{
    static void checkAge(int age) //parameter
    {

        // If age is less than 18, print "access denied"
        if (age < 18) {
            System.out.println("Access denied - You are not old enough!");

            // If age is greater than, or equal to, 18, print "access granted"
        } else {
            System.out.println("Access granted - You are old enough!");
        }

    }

    public static void inlineMethod(String[] args) {
        checkAge(20); // Call the checkAge method and pass along an age of 20
    }

    public ArrayList method() {
        String[] strings = {"a","b","c"};
        ArrayList list=add(strings);
        return list;
    }

    private ArrayList add(String[] strings) //inline "add" method
    {
        ArrayList list = new ArrayList();
        for (int i=0; i< strings.length; i++)
        {list.add(strings[i]);}
        return list;
    }
}

public class Constructor {
    public int varInt;
    public Constructor() {
        this(0);
    }

    public Constructor(int i) {
        varInt=i;
    }

    public void method() {
        Constructor aClass=new Constructor(); //"inline method"
    }
}


public class Superclass {

    int calculations1() {}
    int calculations2() {}
}

class Foo extends Superclass //inline superclass "Superclass"
{
    int someMethod() {

        if (something > calculations1()) {

            return calculations2(); //superclass
        }
    }
}



//interface

interface RenameInterface
{

}

interface ChildRenameInterface extends RenameInterface
{

}

interface Age
{
    int x = 21;
    void getAge();
}
class AnonymousDemo
{
    public static void main(String[] args)
    {

        MyClass obj=new MyClass();

        obj.getAge();
    }
}

class MyClass implements Age //inline superclass "Age"
{
    @Override
    public void getAge() {
        System.out.print("Age is " + x);
    }
}

