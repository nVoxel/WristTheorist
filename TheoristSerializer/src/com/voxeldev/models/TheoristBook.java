package com.voxeldev.models;

import java.util.ArrayList;
import java.util.List;

public class TheoristBook {
    public String title;
    public List<TheoristSubject> subjectList;
    
    public TheoristBook(String title){
        this.title = title;
        subjectList = new ArrayList<>();
    }
}