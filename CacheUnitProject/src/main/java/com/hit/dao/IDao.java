package com.hit.dao;

import java.io.IOException;

public interface IDao<ID extends java.io.Serializable,T> {

    //Deletes a given entity
    public void delete(T entity) throws IOException;

    //Retrieves an entity by its id
    public T find(ID id) throws IOException;

    //Saves a given entity
    public void save(T entity) throws IOException;

}
