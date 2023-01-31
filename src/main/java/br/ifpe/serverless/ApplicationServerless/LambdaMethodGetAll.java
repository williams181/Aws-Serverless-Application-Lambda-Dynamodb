package br.ifpe.serverless.ApplicationServerless;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import br.ifpe.serverless.ApplicationServerless.model.Registro;



public class LambdaMethodGetAll implements RequestStreamHandler{
	
		private AmazonDynamoDB amazonDynamoDB;

	    private String DYNAMODB_TABLE_NAME = "Registros_contas_medicas";

		  
	@SuppressWarnings({ "unchecked", "null" })
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
	 
		
		@SuppressWarnings("unused")
		ScanRequest scanRequest = new ScanRequest()
			    .withTableName(DYNAMODB_TABLE_NAME);
		DynamoDBMapperConfig mapperConfig = new DynamoDBMapperConfig.Builder().withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(DYNAMODB_TABLE_NAME)).build();

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB, mapperConfig);
		
		JSONObject reqObject;
		Registro registro = new Registro();
		try {
		
			reqObject = (JSONObject) parser.parse(reader);
			System.out.println("antes do if");
			if(reqObject.get("pathParameters") == null) {
			
			List<Registro> scanResult = mapper.scan(Registro.class, scanExpression);
			System.out.println("antes do for");
			List<Registro> registros = new ArrayList<Registro>();
			
			for (Registro book : scanResult) {
				System.out.println("dentro do for");	
	        Item item = table.getItem("id", book.getId());
	        	
	         registro = new Registro(item.toJSON());
	         registros.add(registro);
	       
	        }
			if(!registros.isEmpty()) {
			responseBody.put("Registros",registros);
			responseObject.put("statusCode", 200);
			}else {
				responseBody.put("Message: ","nennhum item encontrado");
				responseObject.put("statusCode", 404);
			}
		}
		
		}catch (Exception e) {
			System.out.println("Error "+e.getMessage());
		}
		
			     
		
		OutputStreamWriter writer = new OutputStreamWriter(output);
		writer.write(responseBody.toString());
		
	 	reader.close();
	 	writer.close();
	       
	       
	 	
    
	}


	

}
