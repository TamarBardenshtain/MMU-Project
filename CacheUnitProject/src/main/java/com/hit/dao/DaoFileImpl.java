package com.hit.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.DataModel;

import javax.activity.InvalidActivityException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DaoFileImpl<T> implements IDao<Long, DataModel<T>>{

    private static final Gson gson = new Gson();

    private String filePath;
    private int capacity = 100;

    public DaoFileImpl() {
    }

    public DaoFileImpl(String filePath) {
        this.filePath = filePath;
        File file = new File (filePath);
        if (file.exists())
        {
            file.delete();
        }
        try {
            file.createNewFile();
            //Prepare empty database
            writeEmptyArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public DaoFileImpl(String filePath, int capacity) throws IOException {
        this.filePath = filePath;
        this.capacity = capacity;
        //Prepare empty database
        writeEmptyArray();
    }

    @Override
    public void delete(DataModel<T> entity) throws IOException {
        List<DataModel<T>> data = readData();
        data.remove(entity);
        writeData(data);
    }

    @Override
    public DataModel<T> find(Long id) throws IOException {
        List<DataModel<T>> data = readData();
        for (DataModel<T> item : data) {
            if(item.getDataModelId().equals(id)){
                return item;
            }
        }
        //if item not found
        return null;
    }

    @Override
    public void save(DataModel<T> entity) throws IOException {
        List<DataModel<T>> data = readData();
        //if database is full
        if(data.size() == capacity){
            throw new InvalidActivityException("Memory is full!");
        }
        data.add(entity);
        writeData(data);
    }

    //Write empty array to file to prepare a new database
    private void writeEmptyArray() throws IOException {
        Writer fileWriter = new FileWriter(filePath);
        gson.toJson(new ArrayList<DataModel<T>>(), fileWriter);
        fileWriter.close();
    }

    //Read all data from file
    private List<DataModel<T>> readData() throws IOException {
        Reader fileReader = new FileReader(filePath);
        List<DataModel<T>> data = gson.fromJson(fileReader, new TypeToken<List<DataModel<T>>>() {}.getType());
        fileReader.close();
        return data;
    }

    //Write all data to file
    private void writeData(List<DataModel<T>> data) throws IOException {
        Writer fileWriter = new FileWriter(filePath);
        gson.toJson(data, fileWriter);
        fileWriter.close();
    }
}
