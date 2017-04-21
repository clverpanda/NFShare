package com.clverpanda.nfshare.dao.converter;

import com.clverpanda.nfshare.model.TaskStatus;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by clverpanda on 2017/4/21 0021.
 * It's the file for NFShare.
 */

public class TaskStatusConverter implements PropertyConverter<TaskStatus, Integer>
{
    @Override
    public TaskStatus convertToEntityProperty(Integer databaseValue)
    {
        if (databaseValue == null) return null;
        for (TaskStatus status : TaskStatus.values())
        {
            if (status.getIndex() == databaseValue)
                return status;
        }
        return TaskStatus.UNKNOWN;
    }

    @Override
    public Integer convertToDatabaseValue(TaskStatus entityProperty)
    {
        return entityProperty == null ? null : entityProperty.getIndex();
    }
}