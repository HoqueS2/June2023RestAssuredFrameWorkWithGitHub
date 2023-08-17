package com.qa.gorest.client;

import static io.restassured.RestAssured.given;

import java.util.Map;
import java.util.Properties;

import com.qa.gorest.constants.APIHttpStatus;
import com.qa.gorest.frameworkexception.APIFrameworkException;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class RestClient {
	
	// for time being we are using uri and token as a String later we move this to config property.
	//private static final String BASE_URI = "https://gorest.co.in";
	//private static final String BEARER_TOKEN = "e4b8e1f593dc4a731a153c5ec8cc9b8bbb583ae964ce650a741113091b4e2ac6";
	
	// In class level we declear RequestSpecBuilder reference veritable
	private static RequestSpecBuilder specBuilder;
	
	private Properties prop; // create reference veriable of Properties 
	private String baseURI;
	
	
	
	private boolean isAuthorizationHeaderAdded = false; // initially its false and then addAuthorizationHeader() method we reverse the false
	
	// create RequestSpecBuilder Object here and make it static so we can call it direct
	// this block will execute before creating the obj of this class and from main method. 
/*	static {
		specBuilder = new RequestSpecBuilder();
	}
	*/
	
	// this constructor will call when you create obj of this class
	// properties will help to get the data from config file
	public RestClient(Properties prop, String baseURI) {
		specBuilder = new RequestSpecBuilder();
		this.prop = prop;  // using reference veriable of Properties in the construction using this keyword
		this.baseURI = baseURI;
	}
	
	
	// 2. This method help to add header authorization token 
	
	public void addAuthorizationHeader() {
		if(!isAuthorizationHeaderAdded) {  // here we reversing the false means its true then add the header token
			specBuilder.addHeader("Authorization", "Bearer " + prop.getProperty("tokenId"));
			isAuthorizationHeaderAdded = true; // and then its become true
			// @Test gorest , once we add the header then we don't need to add the header oncw again 
		}
	}
	
		

	
	// 6. this method help to set different content type
		private void setReuqestContentType(String contentType) {//json-JSON-Json
			switch (contentType.toLowerCase()) {
			case "json":
				specBuilder.setContentType(ContentType.JSON);
				break;
			case "xml":
				specBuilder.setContentType(ContentType.XML);
				break;
			case "text":
				specBuilder.setContentType(ContentType.TEXT);
				break;	
			case "multipart":
				specBuilder.setContentType(ContentType.MULTIPART);
				break;	

			default:
				System.out.println("plz pass the right content type....");
				throw new APIFrameworkException("INVALIDCONTENTTYPE");
			}
		}
		
		

		// 1. using specBuilder as a global veriable and using this obj to set base uri 
		private RequestSpecification createRequestSpec(boolean includeAuth) {
			specBuilder.setBaseUri(baseURI);
			
			if(includeAuth) {addAuthorizationHeader();} // checking whether specific APIs has any header or not
			
			return specBuilder.build();
		}

	// 3. In case if we need to pass more content type or header for other call then we need to overload the createRequestSpec method
		private RequestSpecification createRequestSpec(Map<String, String> headersMap, boolean includeAuth) {
			specBuilder.setBaseUri(baseURI);
			//addAuthorizationHeader();
			if(includeAuth) {addAuthorizationHeader();}
			if(headersMap!=null) {
				specBuilder.addHeaders(headersMap);
			}
			return specBuilder.build();
		}

	// 4. Here we sending map of header with a query map.Again we need to overload the createRequestSpec method	
		private RequestSpecification createRequestSpec(Map<String, String> headersMap, Map<String,Object> queryParams, boolean includeAuth) {
			specBuilder.setBaseUri(baseURI);
			//addAuthorizationHeader();
			if(includeAuth) {addAuthorizationHeader();}
			if(headersMap!=null) {
				specBuilder.addHeaders(headersMap);
			}
			if(queryParams!=null) {
				specBuilder.addQueryParams(queryParams);
			}
			return specBuilder.build();
		}
		
	// 5. For POST call what if I want to pass content type like: json,xml with request Body. Again we need to overload the createRequestSpec method for post call	
		private RequestSpecification createRequestSpec(Object requestBody, String contentType,boolean includeAuth) {// Here we pass body in the form of POJO. so we use Object type to hold any kind of java obj refernce
			specBuilder.setBaseUri(baseURI);
			//addAuthorizationHeader();
			if(includeAuth) {addAuthorizationHeader();}
			setReuqestContentType(contentType); // 6. calling setReuqestContentTyp method from this class
			
			if(requestBody!=null) {  // requestBody is the pojo/ object here
				specBuilder.setBody(requestBody); // here we set the the body.
			}
			return specBuilder.build();
		}
		
		
		
	// 7. passing POJO, content type, Header
		private RequestSpecification createRequestSpec(Object requestBody, String contentType, Map<String, String> headersMap, boolean includeAuth) {
			specBuilder.setBaseUri(baseURI); // calling base URI
			//addAuthorizationHeader();    // here we add authorization bear token
			if(includeAuth) {addAuthorizationHeader();}
			setReuqestContentType(contentType);  // her we add the content type
			if(headersMap!=null) {
				specBuilder.addHeaders(headersMap); // here we adding the header
			}
			if(requestBody!=null) {
				specBuilder.setBody(requestBody); // add request body in the form of pojo
			}
			return specBuilder.build();  // and then build
		}
		
		
					
		
		            //Http Methods Utils:  GET / PUT/ DELETE....
	
//========================= GET CALL =====================================================
		
		// 8. Testng will call this method and this method internally call above method accordingly 
		// createRequestSpec() we make it private so that we can hide this from end user. (encapsulation)
		// If you wanna call then call this get() and internally get() will call all the private method in this class
		// here using boolean includeAuth flag to check if any specific api has header or not 
		//Http Methods Utils:
		public Response get(String serviceUrl, boolean includeAuth, boolean log) {		
			if(log) {
				return RestAssured.given(createRequestSpec(includeAuth)).log().all()
				.when()
					.get(serviceUrl);
			}
			return RestAssured.given(createRequestSpec(includeAuth)).when().get(serviceUrl);
			
		}
		
		
		
		public Response get(String serviceUrl, Map<String, String> headersMap, boolean includeAuth, boolean log) {
			
			if(log) {
				return RestAssured.given(createRequestSpec(headersMap, includeAuth)).log().all()
				.when()
					.get(serviceUrl);
			}
			return RestAssured.given(createRequestSpec(headersMap, includeAuth)).when().get(serviceUrl);
		}
		
		
		
		public Response get(String serviceUrl, Map<String, Object> queryParams,  Map<String, String> headersMap, boolean includeAuth, boolean log) {
			
			if(log) {
				return RestAssured.given(createRequestSpec(headersMap, queryParams, includeAuth)).log().all()
				.when()
					.get(serviceUrl);
			}
			return RestAssured.given(createRequestSpec(headersMap, queryParams, includeAuth)).when().get(serviceUrl);
		}
		
		
	//=============================== POST CALL ===============================================	
		
		public Response post(String serviceUrl, String contentType, Object requestBody, boolean includeAuth, boolean log) {
			if(log) {
				return RestAssured.given(createRequestSpec(requestBody, contentType, includeAuth)).log().all()
					.when()
						.post(serviceUrl);
			}
			return RestAssured.given(createRequestSpec(requestBody, contentType, includeAuth))
					.when()
						.post(serviceUrl);
		}
		
		
		public Response post(String serviceUrl, String contentType, Object requestBody, Map<String, String> headersMap, boolean includeAuth, boolean log) {
			if(log) {
				return RestAssured.given(createRequestSpec(requestBody, contentType, headersMap, includeAuth)).log().all()
					.when()
						.post(serviceUrl);
			}
			return RestAssured.given(createRequestSpec(requestBody, contentType, headersMap, includeAuth))
					.when()
						.post(serviceUrl);
		}
		
		
		public Response put(String serviceUrl, String contentType, Object requestBody, boolean includeAuth,  boolean log) {
			if(log) {
				return RestAssured.given(createRequestSpec(requestBody, contentType, includeAuth)).log().all()
					.when()
						.put(serviceUrl);
			}
			return RestAssured.given(createRequestSpec(requestBody, contentType, includeAuth))
					.when()
						.put(serviceUrl);
		}
		
		
		public Response put(String serviceUrl, String contentType, Object requestBody, Map<String, String> headersMap, boolean includeAuth, boolean log) {
			if(log) {
				return RestAssured.given(createRequestSpec(requestBody, contentType, headersMap, includeAuth)).log().all()
					.when()
						.put(serviceUrl);
			}
			return RestAssured.given(createRequestSpec(requestBody, contentType, headersMap, includeAuth))
					.when()
						.put(serviceUrl);
		}
		
		
		public Response patch(String serviceUrl, String contentType, Object requestBody, boolean includeAuth,  boolean log) {
			if(log) {
				return RestAssured.given(createRequestSpec(requestBody, contentType, includeAuth)).log().all()
					.when()
						.patch(serviceUrl);
			}
			return RestAssured.given(createRequestSpec(requestBody, contentType, includeAuth))
					.when()
						.patch(serviceUrl);
		}
		
		
		public Response patch(String serviceUrl, String contentType, Object requestBody, Map<String, String> headersMap, boolean includeAuth, boolean log) {
			if(log) {
				return RestAssured.given(createRequestSpec(requestBody, contentType, headersMap, includeAuth)).log().all()
					.when()
						.patch(serviceUrl);
			}
			return RestAssured.given(createRequestSpec(requestBody, contentType, headersMap, includeAuth))
					.when()
						.patch(serviceUrl);
		}
		
		
		public Response delete(String serviceUrl, boolean includeAuth, boolean log) {
			if(log) {
				return RestAssured.given(createRequestSpec(includeAuth)).log().all()
					.when()
						.delete(serviceUrl);
			}
			return RestAssured.given(createRequestSpec(includeAuth))
					.when()
						.delete(serviceUrl);
		}
		
		
	// For OAuth2.0 Amadeus API
		// This is a seperate API so we are not creating any specification
		public String getAccessToken(String serviceURL, String grantType, String clientId, String clientSecret  ) {
			//1. POST - get the access token
					RestAssured.baseURI = "https://test.api.amadeus.com";
					
					String accessToken = given().log().all()
						.contentType(ContentType.URLENC)
						.formParam("grant_type", grantType)
						.formParam("client_id", clientId)
						.formParam("client_secret", clientSecret)
					.when()
						.post(serviceURL)
					.then().log().all()
						.assertThat()
							.statusCode(APIHttpStatus.OK_200.getCode())
							.extract().path("access_token");  // once we get the token 
						
					System.out.println("access token: " + accessToken); 
				return 	accessToken; // this method will return token
					
		}
		

	}
