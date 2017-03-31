package com.clverpanda.nfshare.Model;

/**
 * Created by clverpanda on 2017/3/31 0031.
 * It's the file for NFShare.
 */

public class ContactInfo
{
    private String name;
    private String number;
    private String sortKey;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
