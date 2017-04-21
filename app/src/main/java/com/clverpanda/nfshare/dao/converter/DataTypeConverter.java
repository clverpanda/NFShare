package com.clverpanda.nfshare.dao.converter;

import com.clverpanda.nfshare.model.DataType;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by clverpanda on 2017/4/21 0021.
 * It's the file for NFShare.
 */

public class DataTypeConverter implements PropertyConverter<DataType, Integer>
{
    @Override
    public DataType convertToEntityProperty(Integer databaseValue)
    {
        if (databaseValue == null) return null;
        for (DataType type : DataType.values())
        {
            if (type.getIndex() == databaseValue)
                return type;
        }
        return DataType.UNKNOWN;
    }

    @Override
    public Integer convertToDatabaseValue(DataType entityProperty)
    {
        return entityProperty == null ? null : entityProperty.getIndex();
    }
}