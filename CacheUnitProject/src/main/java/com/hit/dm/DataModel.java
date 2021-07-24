package com.hit.dm;

import java.io.Serializable;
import java.util.Objects;

public class DataModel<T> implements Serializable {

    private long id;
    private T content;

    public DataModel(){
    }

    public DataModel(long id, T content) {
        this.content = content;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataModel<?> dataModel = (DataModel<?>) o;
        return content.equals(dataModel.content) && id == dataModel.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, id);
    }

    public Long getDataModelId(){
        return this.id;
    }

    public void setDataModelId(Long id){
        this.id = id;
    }

    public T getContent(){
        return this.content;
    }

    public void setContent(T content){
        this.content = content;
    }

    public String toString(){
        return "Data entity: {id: " + this.id + ", content: " + this.content + "}";
    }

}

