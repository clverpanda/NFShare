package com.clverpanda.nfshare.Model;

/**
 * Created by clverpanda on 2017/4/5 0005.
 * It's the file for NFShare.
 */

public enum DataType
{
    PLAIN("普通文本", 1), APP("应用", 2), CONTACT("联系人", 3), FILE("文件", 4), STREAM("流媒体", 5);
    private String name;
    private int index;

    DataType(String name, int index)
    {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
