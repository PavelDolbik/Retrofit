package com.example.pdolbik.retrofitexample;

import com.google.gson.annotations.SerializedName;

/**
 * Created by p.dolbik on 10.12.2015.
 */
public class Repo {

    @SerializedName("id")
    private String idRep;

    private String name;

    @SerializedName("full_name")
    private String fullName;


    public Repo() {}


    public String getId()       { return idRep; }
    public String getName()     { return name; }
    public String getFullName() { return fullName; }
}
