package com.qa.gorest.utils;

import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.qa.gorest.frameworkexception.APIFrameworkException;

import io.restassured.response.Response;



public class JsonPathValidator {
	
	// This metod will get the response and convert it into a asString()
	private String getJsonResponseAsString(Response response) {
        return response.getBody().asString(); // asString return String
	}
	
	
	public <T> T read(Response response, String jsonPath) {
		String jsonResponse =  getJsonResponseAsString(response); // calling getJsonResponseAsString() and sending response peremeter
        try {													// jsonPath might give you some exception so use try catch block
        	return JsonPath.read(jsonResponse, jsonPath); // callling read mrthod and sending response as a String with jsonPath query		
        	// read() can return anythig so we can use Obj but better approach is use generic <T>
        }
        catch(PathNotFoundException e) {
        	e.printStackTrace();
        	throw new APIFrameworkException(jsonPath + "is not found...");
        }
	}
	
	
	// whatever jsonPath you give me it might give you a list so this method will read some list from response+jsonPath
	// <T> is a Type
	public <T> List<T> readList(Response response, String jsonPath) {
		String jsonResponse =  getJsonResponseAsString(response);
        try {
        	return JsonPath.read(jsonResponse, jsonPath);
        }
        catch(PathNotFoundException e) {
        	e.printStackTrace();
        	throw new APIFrameworkException(jsonPath + "is not found...");
        }
	}
	
	// what if i passing 2 peremeters id, image from product api. this will return list of map object. Key will be String and value will be Obj
	// Reference JsonPathTest.java
	public <T> List<Map<String, T>> readListOfMaps(Response response, String jsonPath) {
		String jsonResponse =  getJsonResponseAsString(response);
        try {
        	return JsonPath.read(jsonResponse, jsonPath);
        }
        catch(PathNotFoundException e) {
        	e.printStackTrace();
        	throw new APIFrameworkException(jsonPath + "is not found...");
        }
	}
	
	

}
