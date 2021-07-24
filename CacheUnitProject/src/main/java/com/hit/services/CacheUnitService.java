package com.hit.services;

import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;

import java.io.IOException;

public class CacheUnitService<T> {

    private static final String algo = "LRU";
    private static final int capacity = 3;
    private int countSwaps, countRequests, countDataModels;
    private final CacheUnit<T> cacheUnit;
    IDao<Long, DataModel<T>> dao;


    public CacheUnitService()
    {
        try {
            this.dao = new DaoFileImpl<>("CacheUnitProject\\src\\main\\resources\\datasource.txt", capacity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.cacheUnit = new CacheUnit<>(new LRUAlgoCacheImpl<Long, DataModel<T>>(capacity));
        this.countRequests = 0;
        this.countDataModels = 0;
        this.countSwaps = 0;
    }

    public boolean delete(DataModel<T>[] dataModels){
        try{
            Long[] ids = new Long[dataModels.length];

            for (int i = 0; i <dataModels.length ; i++){
                ids[i] = dataModels[i].getDataModelId();
                dao.delete(dataModels[i]);
            }
            this.countRequests ++;
            cacheUnit.removeDataModels(ids);

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(DataModel<T>[] dataModels) {
        try {
            System.out.println("update request!");
            countRequests++;
            countDataModels += dataModels.length;
            DataModel<T>[] removedElements = cacheUnit.putDataModels(dataModels);
            for (DataModel<T> removedElement : removedElements) {
                if (removedElement != null) {
                    countSwaps++;
                    dao.save(removedElement);
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public DataModel<T>[] get(DataModel<T>[] dataModels){
        try{
            countRequests ++;
            countDataModels += dataModels.length;

            Long[] ids = new Long[dataModels.length];

            for (int i = 0; i < dataModels.length; i++){
                ids[i] = dataModels[i].getDataModelId();
            }
            DataModel<T>[] models = cacheUnit.getDataModels(ids);

            for (int i = 0; i < models.length; i++) {
                if(models[i] == null){
                    models[i] = dao.find(dataModels[i].getDataModelId());
                }
            }
            cacheUnit.putDataModels(models);

            return models;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getStatistics(){
        return algo + "," + capacity + "," + countSwaps + "," + countRequests + "," + countDataModels;
    }
}

