package com.hit.memory;

import com.hit.algorithm.IAlgoCache;
import com.hit.dm.DataModel;

public class CacheUnit<T>{

    private final IAlgoCache<Long, DataModel<T>> algo;

    public CacheUnit(IAlgoCache<Long, DataModel<T>> algo){
        this.algo = algo;
    }

    @SuppressWarnings("unchecked")
    public DataModel<T>[] getDataModels(Long[] ids){

        if(ids == null){
            return  null;
        }

        DataModel<T>[] data = new DataModel[ids.length];
        for (int i = 0; i < ids.length; i++) {
            data[i] = algo.getElement(ids[i]);
        }
        return data;
    }

    @SuppressWarnings("unchecked")
    public DataModel<T>[] putDataModels(DataModel<T>[] dataModels){

        if (dataModels == null){
            return null;
        }

        DataModel<T>[] dataRemoved = new DataModel[dataModels.length];
        for (int i = 0; i < dataModels.length; i++) {
            if(dataModels[i] != null){
                System.out.println(dataModels[i].getDataModelId());
                dataRemoved[i] = algo.putElement(dataModels[i].getDataModelId(), dataModels[i]);
            }
            else dataRemoved[i] = null;
        }
        return dataRemoved;
    }

    public void removeDataModels(Long[] ids){
        for (Long id : ids) {
            algo.removeElement(id);
        }
    }

}

