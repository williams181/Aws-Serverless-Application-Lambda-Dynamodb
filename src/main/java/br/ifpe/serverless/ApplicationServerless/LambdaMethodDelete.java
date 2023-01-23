package br.ifpe.serverless.ApplicationServerless;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

public class LambdaMethodDelete implements  RequestStreamHandler{
			private AmazonDynamoDB amazonDynamoDB;

		    private String DYNAMODB_TABLE_NAME = "Arquivos";
		    
		  
    @SuppressWarnings("unchecked")
	@Override
	 public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException{
			this.amazonDynamoDB = AmazonDynamoDBClient.builder()
				            .withRegion(Regions.US_EAST_1)
			            .build();

			 	DynamoDB dynamodb = new DynamoDB(amazonDynamoDB);
			 	
				Table table =dynamodb.getTable(DYNAMODB_TABLE_NAME);
			 	
			 	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
				JSONParser parser = new JSONParser();
				JSONObject responseObject = new JSONObject();
			 	JSONObject responseBody = new JSONObject();
			 
			 
				JSONObject reqObject;
			
				int id;
					try {
						
						reqObject = (JSONObject) parser.parse(reader);
						
						if(reqObject.get("pathParameters") != null) {
							JSONObject pps = (JSONObject)reqObject.get("pathParameters");
							
							if(pps.get("id") != null) {
								id = Integer.parseInt((String) pps.get("id"));
								table.deleteItem("id", id);
								responseBody.put("Message: ", "deletetado com sucesso!");
								responseObject.put("statusCode", 200);
							}
						
						}
						else {
							responseBody.put("message: ","nenhum item encontrado");
							responseObject.put("statusCode", 404);
						}
						
						responseObject.put("body", responseBody.toString());
					}catch (ParseException e) {
					
					context.getLogger().log("Error: "+e.getMessage());
					}
			     
			
			
				OutputStreamWriter writer = new OutputStreamWriter(output);
				writer.write(responseObject.toString());
				
			 	reader.close();
			 	writer.close();
			       
			       
			 	
		    
			}
		 
	
	

}
