package br.ifpe.serverless.ApplicationServerless;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import br.ifpe.serverless.ApplicationServerless.model.PersonRequest;
import br.ifpe.serverless.ApplicationServerless.model.PersonResponse;

public class LambdaMethodDelete implements  RequestHandler<PersonRequest, PersonResponse>{
			private AmazonDynamoDB amazonDynamoDB;

		    private String DYNAMODB_TABLE_NAME = "http-crud-cadastro";
		    
		  
		@Override
	public PersonResponse handleRequest(PersonRequest personRequest, Context context){
			this.amazonDynamoDB = AmazonDynamoDBClient.builder()
		            .withRegion(Regions.US_EAST_1)
		            .build();
		
			getData(personRequest);
	
	     PersonResponse personResponse = new PersonResponse();
	     personResponse.setMessage("Solicitação feita com sucesso !!");
	 
	     return personResponse;
	}
	
	private void getData(PersonRequest personRequest) throws ConditionalCheckFailedException {
	
		DynamoDB dynamodb = new DynamoDB(amazonDynamoDB);
	  
		Table table =dynamodb.getTable(DYNAMODB_TABLE_NAME);
	
		table.deleteItem("id", personRequest.getId());
	  
		System.out.println("Removido!!");
	 
	}

}
