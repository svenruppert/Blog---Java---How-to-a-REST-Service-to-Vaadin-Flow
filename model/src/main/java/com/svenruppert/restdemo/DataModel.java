package com.svenruppert.restdemo;

public interface DataModel {

  record DataObject(String id, String value) { }

}