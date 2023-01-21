package br.ifpe.serverless.ApplicationServerless.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import br.ifpe.serverless.ApplicationServerless.model.Bill;

public class LambdaMethodHandlerBill implements RequestStreamHandler {

	private String DYNAMO_TABLE = "Bills";
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		
		OutputStreamWriter writer = new OutputStreamWriter(output);
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		JSONParser parser = new JSONParser();
		JSONObject responseObject = new JSONObject();
		JSONObject responseBody = new JSONObject();
		
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
		DynamoDB dynamoDB = new DynamoDB(client);
		
		int id;
		Item resItem = null;
		
		try {
			JSONObject reqObject = (JSONObject) parser.parse(reader);
			//pathParameters
			if (reqObject.get("pathParameters") !=null ) {
				JSONObject pps = (JSONObject)reqObject.get("pathParameters");
				if (pps.get("id") !=null) {
					id = Integer.parseInt((String)pps.get("id"));
					resItem = dynamoDB.getTable(DYNAMO_TABLE).getItem("id",id);
				}
			}
			//queryStringParameters
			else if (reqObject.get("queryStringParameters") !=null) {
				JSONObject qps = (JSONObject) reqObject.get("queryStringParameters");
				if (qps.get("id") !=null) {
					id = Integer.parseInt((String)qps.get("id"));
					resItem = dynamoDB.getTable(DYNAMO_TABLE).getItem("id",id);
				}
			}
			
			if (resItem !=null) {
				Bill bill = new Bill(resItem.toJSON());
				responseBody.put("bill", bill);
				responseObject.put("bill", bill);
			} else {
				responseBody.put("message", "No Items Found");
				responseObject.put("statusCode", 404);				
			}
			
		} catch (Exception e) {
			context.getLogger().log("ERROR: " + e.getMessage());
		}
		writer.write(responseObject.toString());
		reader.close();
		writer.close();
	}

}
