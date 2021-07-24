package com.hit.services;

import com.hit.dm.DataModel;

public class CacheUnitController<T>
{
    CacheUnitService<T> service;

    public CacheUnitController(){
        service = new CacheUnitService<>();
    }

    public boolean delete(DataModel<T>[] dataModels){
        return service.delete(dataModels);
    }

    public boolean update(DataModel<T>[] dataModels){
        return service.update(dataModels);
    }

    public DataModel<T>[] get(DataModel<T>[] dataModels){
        return service.get (dataModels);
    }

    public String getStatistics(){
        return service.getStatistics();
    }
}