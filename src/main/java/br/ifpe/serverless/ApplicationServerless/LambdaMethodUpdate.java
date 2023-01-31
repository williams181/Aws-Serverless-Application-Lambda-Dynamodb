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

import br.ifpe.serverless.ApplicationServerless.model.Registro;
import br.ifpe.serverless.ApplicationServerless.model.Message;

public class LambdaMethodUpdate implements RequestHandler<Registro, Message>{
	 private AmazonDynamoDB amazonDynamoDB;

	    private String DYNAMODB_TABLE_NAME = "Registros_contas_medicas";
	    
		  
		@Override
	public Message handleRequest(Registro registro, Context context){
			this.amazonDynamoDB = AmazonDynamoDBClient.builder()
		            .withRegion(Regions.US_EAST_1)
		            .build();
		
			updateData(registro);
	
	     Message message = new Message();
	     message.setMessage("Solicitação feita com sucesso !!");
	 
	     return message;
	}
	private void updateData(Registro registro) throws ConditionalCheckFailedException {
			
			DynamoDB dynamodb = new DynamoDB(amazonDynamoDB);
			Table table =dynamodb.getTable(DYNAMODB_TABLE_NAME);
	
			
		    Map<String, String> expressionAttributeNames = new HashMap<String, String>();
		    expressionAttributeNames.put("#M", "medico");
		    expressionAttributeNames.put("#C", "CRM");
		    expressionAttributeNames.put("#H", "hospital");
		    expressionAttributeNames.put("#CNPJ", "CNPJ");
		    expressionAttributeNames.put("#PAC", "paciente");
		    expressionAttributeNames.put("#CON", "convenio");
		    expressionAttributeNames.put("#A", "acomodacao");
		    expressionAttributeNames.put("#PRO", "procedimento");
		  
		    Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
		    expressionAttributeValues.put(":val1", registro.getMedico());
		    expressionAttributeValues.put(":val2", registro.getCRM());
		    expressionAttributeValues.put(":val3", registro.getHospital());
		    expressionAttributeValues.put(":val4", registro.getCNPJ());
		    expressionAttributeValues.put(":val5", registro.getPaciente());
		    expressionAttributeValues.put(":val6", registro.getConvenio());
		    expressionAttributeValues.put(":val7", registro.getAcomodacao());
		    expressionAttributeValues.put(":val8", registro.getProcedimento());
		
		    
		     
		    UpdateItemOutcome outcome = table.updateItem(
		        "id",          // key attribute name
		        registro.getId(),           // key attribute value
		        "set #M = :val1, #C = :val2 , #H = :val3, #CNPJ = :val4,"
		        + "#PAC = :val5, #CON = :val6 , #A = :val7, #PRO = :val8",// UpdateExpression
		      
		        expressionAttributeNames,
		        expressionAttributeValues
		        );
			
	   
	  
		    System.out.println("Editado!!"+ outcome.getUpdateItemResult());
	 
	}

}
