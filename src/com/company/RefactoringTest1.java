package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

interface RenameInterface {

}

interface Age {

    interface ChildRenameInterface extends RenameInterface {

    }
    int x = 21;

    void getAge();
}

class RefactoringTest1 {

}

//class
class ClassRenameTest {
    static int myMethod(int x, int y) //parameter
    {
        return x + y;
    }

    public static void parameter(String[] args) {
        Properties capitals = new Properties();
        Set states;
        String stringg; //field
        boolean btest = true; //boolean variable

        capitals.put("Illinois", "Springfield");
        capitals.put("Missouri", "Jefferson City");
        capitals.put("Washington", "Olympia");
        capitals.put("California", "Sacramento"); //property
        capitals.put("Indiana", "Indianapolis");

        System.out.println(btest); //btest
        System.out.println(stringg);

        // Show all states and capitals in hashtable.
        states = capitals.keySet();   // get set-view of keys
        Iterator itr = states.iterator();

        while (itr.hasNext()) {
            stringg = (String) itr.next();
            System.out.println("The capital of " + stringg + " is " +
                    capitals.getProperty(stringg) + ".");
        }
        System.out.println();

        // look for state not in list -- specify default
        stringg = capitals.getProperty("Florida", "Not Found");
        System.out.println("The capital of Florida is " + stringg + ".");
    }

    public void simpleInlineVariable() {
        int test = 1;
        int a = test + 1; //inline variable "test"
    }

    public void inlineVariable() {
        AnotherClass.InnerClass aClass = anotherClass.innerClass;
        int a = aClass.i; //inline variable "aClass"
    }

}

class ChildTestRenameTest extends ClassRenameTest {
    static void checkAge(int age1) //parameter
    {

        // If age1 is less than 18, print "access denied"
        if (age1 < 18) {
            System.out.println("Access denied - You are not old enough!");

            // If age1 is greater than, or equal to, 18, print "access granted"
        } else {
            System.out.println("Access granted - You are old enough!");
        }

    }

    public static void inlineMethod(String[] args) {
        checkAge(20); // Call the checkAge method and pass along an age of 20
    }

    public ArrayList method() {
        String[] strings = {"a", "b", "c"};
        ArrayList list = add(strings);
        return list;//add
        System.out.printf("add");
    }

    private ArrayList add(String[] strings) //inline "add" method
    {
        ArrayList list = new ArrayList();
        for (int i = 0; i < strings.length; i++) {
            list.add(strings[i]);
        }
        return list;
    }
}


///**/
//interface

public class Constructor1 {
    AnotherClass anotherClass;
    public int varInt; //add

    public Constructor() {
        this(0);
    }

    public Constructor(int i) {
        varInt = i;

    }

    public void method() {
        Constructor aClass = new Constructor(); //"inline method"
        int a = 1;
        int b = a + anotherClass.intValue(); //introduce variable
        int c = b + anotherClass.intValue();
        int number = anotherClass.intValue();
    }
}

public class Superclass {

    int calculations1() {
        //
    }

    int calculations2() {
        //
    }
}

class Foo extends Superclass //inline superclass "Superclass"
{
    int someMethod() {

        if (something > calculations1()) {

            return calculations2(); //superclass
        }
    }
}

class AnonymousDemo {
    public static void main(String[] args) {

        MyClass obj = new MyClass();

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

//introduce parameter object
private void grawEdge (Graphics g, float edgeWidth, int x1, int x2, int yl, int y2) {
    final Graphics2D g2d = (Grapics2D) g;
    g2d.setStroke(new BasicStroke(edgeWidth));
    g.drawLine(x1, y1, x2, y2);
}

