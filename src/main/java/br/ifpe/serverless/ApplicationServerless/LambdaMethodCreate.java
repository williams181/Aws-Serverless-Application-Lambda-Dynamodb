package br.ifpe.serverless.ApplicationServerless;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import br.ifpe.serverless.ApplicationServerless.model.PersonRequest;
import br.ifpe.serverless.ApplicationServerless.model.PersonResponse;

public class LambdaMethodCreate implements RequestHandler<PersonRequest, PersonResponse>{
	 private AmazonDynamoDB amazonDynamoDB;

	    private String DYNAMODB_TABLE_NAME = "http-crud-cadastro";
	
	  
	@Override
 public PersonResponse handleRequest(PersonRequest personRequest, Context context){
		//BasicConfigurator.configure();
		this.amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
	            .withRegion(Regions.US_EAST_1)
	            .build();
		
		 persistData(personRequest);

	      PersonResponse personResponse = new PersonResponse();
	      personResponse.setMessage("Saved Successfully!!!");
    
     return personResponse;
 }
 
 private void persistData(PersonRequest personRequest) throws ConditionalCheckFailedException {

 	DynamoDB dynamodb = new DynamoDB(amazonDynamoDB);
			
     Item item = new Item()
     		.withPrimaryKey("id",personRequest.getId())
     		.withString("nome",personRequest.getNome())
     		.withString("sobreNome",personRequest.getSobreNome())
     		.withInt("idade", personRequest.getIdade())
     		.withString("endereco",personRequest.getEndereco());
     
   
     Table table = dynamodb.getTable(DYNAMODB_TABLE_NAME);
     table.putItem(item);
 }
}
