package br.ifpe.serverless.ApplicationServerless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaMethodHandlerUppercase implements RequestHandler<String, String> {

	@Override
	public String handleRequest(String input, Context context) {
		LambdaLogger logger = context.getLogger();
		logger.log("Function '" + context.getFunctionName() + "' called");
		return input.toUpperCase();
	}
	
}
