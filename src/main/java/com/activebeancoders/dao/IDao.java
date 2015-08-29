package com.activebeancoders.dao;

public interface IDao<T> {

    public T get(String id);
    public void save(T t);
    public void update(T t);

}
