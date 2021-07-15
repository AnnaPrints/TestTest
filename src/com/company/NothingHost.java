package com.company;

import javax.swing.*;

public class nothinghost extends jdialog {
    private jpanel contentpane;
    private jbutton buttonok;
    private jbutton buttoncancel;

    public nothinghost() {
        setcontentpane(contentpane);
        setmodal(true);
        getrootpane().setdefaultbutton(buttonok);
    }
}


