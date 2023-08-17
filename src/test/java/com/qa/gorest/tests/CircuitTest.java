package com.qa.gorest.tests;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qa.gorest.base.BaseTest;
import com.qa.gorest.client.RestClient;
import com.qa.gorest.constants.APIHttpStatus;
import com.qa.gorest.utils.JsonPathValidator;

import io.restassured.response.Response;



public class CircuitTest extends BaseTest{
	
	// Impotent======> never mention/ maintain your jsonPath in the utility section
	
	@BeforeMethod
	public void getUserSetup() {
		restClient = new RestClient(prop, baseURI);
	}
	
	@Test
	public void getCircuitTest() {
		Response circuitResponse = restClient.get(CIRCUIT_ENDPOINT+"/2017/circuits.json", false, false);// get() return response
		int statusCode = circuitResponse.statusCode(); // get the statuscode
		Assert.assertEquals(statusCode, APIHttpStatus.OK_200.getCode());// APIHttpStatus.OK_200.getCode() come from Enum
		
		JsonPathValidator js = new JsonPathValidator();// create Object from JsonPathValidator class
		// give me the location of the countre which circuitId is  shanghai
		List<String> countryList = js.readList(circuitResponse, "$.MRData.CircuitTable.Circuits[?(@.circuitId == 'shanghai')].Location.country");
		System.out.println(countryList);
		Assert.assertTrue(countryList.contains("China"));
		
		
//					.then().log().all()
//						.assertThat().statusCode(APIHttpStatus.OK_200.getCode());
						
	}
	
	
	

}
