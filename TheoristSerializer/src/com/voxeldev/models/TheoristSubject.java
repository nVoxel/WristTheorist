package com.voxeldev.models;

import java.util.ArrayList;
import java.util.List;

public class TheoristSubject {
    public String name;
    public List<TheoristNote> noteList;
    
    public TheoristSubject(String name){
        this.name = name;
        noteList = new ArrayList<>();
    }
}
