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

import br.ifpe.serverless.ApplicationServerless.model.Registro;
import br.ifpe.serverless.ApplicationServerless.model.Message;

public class LambdaMethodCreate implements RequestHandler<Registro, Message>{
	 private AmazonDynamoDB amazonDynamoDB;

	    private String DYNAMODB_TABLE_NAME = "Registros_contas_medicas";
	
		  
	@Override
	 public Message handleRequest(Registro registro, Context context){
			
		this.amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
	            .withRegion(Regions.US_EAST_1)
	            .build();
		
		 persistData(registro);

	      Message personResponse = new Message();
	      personResponse.setMessage("Saved Successfully!!!");
    
     return personResponse;
	}
 
	private void persistData(Registro registro) throws ConditionalCheckFailedException {

		
		DynamoDB dynamodb = new DynamoDB(amazonDynamoDB);
		
	
		System.out.println("persist "+Registro.radom());
	     Item item = new Item()
	    		.withString("id", Registro.radom())
	     		.withString("medico",registro.getMedico())
	     		.withInt("CRM",registro.getCRM())
	     		.withString("hospital", registro.getHospital())
	     		.withString("CNPJ",registro.getCNPJ())
	     		.withString("paciente",registro.getPaciente())
	     		.withString("convenio",registro.getConvenio())
	     		.withString("acomodacao", registro.getAcomodacao())
	     		.withString("procedimento",registro.getProcedimento());
     
	   
	     Table table = dynamodb.getTable(DYNAMODB_TABLE_NAME);
	     table.putItem(item);
 }
}
