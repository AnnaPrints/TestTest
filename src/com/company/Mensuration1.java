package com.company;

public class Mensuration1
{
    public double x, y, r;
    // Create getter method for each variable.
    public double getX()
    {
        return x;
    }
    public double getY()
    {
        return y;
    }
    public double getR()
    {
        return r;
    }
    // Create setter method for each variable in the class.
    public void setX(double x)
    {
        this.x = x;
    }
    public void setY(double y)
    {
        this.y = y;
    }
    public void setR(double r)
    {
        this.r = r;
    }
    // Now write logic of Rectangle and Circle.
    public double areaRec()
    {
        double area = x*y;
        return area;     
    }
    public double perRec()
    {
        double per = 2*(x+y);
        return per;
    }
    public double areaCircle()
    {
        double area =  3.14*r*r;
        return area;
    }
    public double circumCircle()
    {
        double circumference = 2*3.14*r;
        return circumference;
    }
}
public class MyTest1
{
    public static void main(String[] args)
    {
        Mensuration1 mens; // Creating reference.men's = new Mensuration();
        // Creating object.

// Set the values of the variables.
        mens.setX(20.5);
        mens.setY(30.5);
        mens.setR(12.5);
        double areaRec = mens.areaRec();
        double perRec = mens.perRec();
        double areaCircle = mens.areaCircle();
        double circumCircle = mens.circumCircle();

        System.out.println("Area of rectangle: " +areaRec);
        System.out.println("Perimeter of rectangle: " +perRec);
        System.out.println("Area of circle: " +areaCircle);
        System.out.println("Circumference of circle: " +circumCircle);
    }
}
