package com.company;

public class CTest {
    //CTest1.java
    public static void main(String[] args) {
        System.out.printf("CTest1.java");
    }


}


class C2 {
    //c2
    public static void main(String[] args) {
        System.out.printf("c2");
    }
}

class C2New extends C2 {
    //c2
    public static void test(String[] args) {
        System.out.printf("c2");
    }
}

class Closss {
    public void method12345() {
        int a=1;
        int b=2;
        int c=a+b;
        int d=a+c;
    }
}