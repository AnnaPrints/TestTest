package com.company;

import java.util.Arrays;
import java.util.List;

public class Java8 {

    public static void main(String[] args) {

        Java8 obj = new Java8();

        List<String> list = Arrays.asList("node", "c++", "java", "javascript");

        // lambda
        //List<String> result = obj.map(list, x -> obj.sha256(x));

        // method reference
        List<String> result = obj.map(list, obj::sha256);

        result.forEach(System.out::println);



    }}
//fulltest
//copy
//guest

//rejointest