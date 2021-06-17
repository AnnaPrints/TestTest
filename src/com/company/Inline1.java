package com.company;

class Inline1 {
    public static void testMethod(String[] args) {

        int test = 6;
        int rename = 6; //rename
        System.out.println("rename");
        System.out.println(test);
        System.out.println("Inline1"); //Inline1
        int b = test + rename; //testMethod
        System.out.println(rename);
    }

    public static void main(String[] args) {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("args = " + args);
    }

}

class TestRest extends Inline1 {

}
