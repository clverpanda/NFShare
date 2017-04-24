package com.clverpanda.nfshare.model;

import java.io.Serializable;

/**
 * Created by clverpanda on 2017/4/5 0005.
 * It's the file for NFShare.
 */

public enum DataType implements Serializable
{
    PLAIN("普通文本", 1), APP("应用", 2), CONTACT("联系人", 3), FILE("文件", 4), STREAM("流媒体", 5), UNKNOWN("未知", 0);
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

    public int getIndex() {
        return index;
    }

    public static String getName(int index)
    {
        switch (index)
        {
            case 1:
                return DataType.PLAIN.getName();
            case 2:
                return DataType.APP.getName();
            case 3:
                return DataType.CONTACT.getName();
            case 4:
                return DataType.FILE.getName();
            case 5:
                return DataType.STREAM.getName();
            default:
                return "未知类型";
        }
    }
}
