package com.company;

public class override {
    void shoow()
    {
        System.out.println("Parent1's shoow()");
    }
}
// Inherited class
class Wow extends override{
    // This method overrides shoow() of Parent1
    @Override
    void shoow()
    {
        System.out.println("Child's shoow()");
    }
}