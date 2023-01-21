package br.ifpe.serverless.ApplicationServerless;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import br.ifpe.serverless.ApplicationServerless.model.PersonRequest;
import br.ifpe.serverless.ApplicationServerless.model.PersonResponse;

public class LambdaMethodUpdate implements RequestHandler<PersonRequest, PersonResponse>{
	 private AmazonDynamoDB amazonDynamoDB;

	    private String DYNAMODB_TABLE_NAME = "http-crud-cadastro";
	    
		  
		@Override
	public PersonResponse handleRequest(PersonRequest personRequest, Context context){
			this.amazonDynamoDB = AmazonDynamoDBClient.builder()
		            .withRegion(Regions.US_EAST_1)
		            .build();
		
			updateData(personRequest);
	
	     PersonResponse personResponse = new PersonResponse();
	     personResponse.setMessage("Solicitação feita com sucesso !!");
	 
	  return personResponse;
		}
		private void updateData(PersonRequest personRequest) throws ConditionalCheckFailedException {
			
			DynamoDB dynamodb = new DynamoDB(amazonDynamoDB);
			Table table =dynamodb.getTable(DYNAMODB_TABLE_NAME);
	
			
		    Map<String, String> expressionAttributeNames = new HashMap<String, String>();
		    expressionAttributeNames.put("#N", "nome");
		    expressionAttributeNames.put("#S", "sobreNome");
		    expressionAttributeNames.put("#I", "idade");
		    expressionAttributeNames.put("#E", "endereco");
		  
		    Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
		    expressionAttributeValues.put(":val1", personRequest.getNome());
		    expressionAttributeValues.put(":val2", personRequest.getSobreNome());
		    expressionAttributeValues.put(":val3", personRequest.getIdade());
		    expressionAttributeValues.put(":val4", personRequest.getEndereco());
		     
		    UpdateItemOutcome outcome = table.updateItem(
		        "id",          // key attribute name
		        personRequest.getId(),           // key attribute value
		        "set #N = :val1, #S = :val2 , #I = :val3, #E = :val4",// UpdateExpression
		        expressionAttributeNames,
		        expressionAttributeValues
		        );
			
	   
	  
		    System.out.println("Editado!!"+ outcome.getUpdateItemResult());
	 
	}

}
