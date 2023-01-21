package br.ifpe.serverless.ApplicationServerless;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.junit.jupiter.api.Assertions;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import br.ifpe.serverless.ApplicationServerless.first.LambdaMethodHandlerUppercase;

@ExtendWith(MockitoExtension.class)
public class SimpleHandlerTest {

		
	private LambdaMethodHandlerUppercase lambdaMethodHandlerUppercase;
	
	@Mock
	Context context;
	
	@Mock
	LambdaLogger logger;
	
	@BeforeEach
	public void setup() {
		Mockito.when(context.getLogger()).thenReturn(logger);
		
		Mockito.doAnswer(call -> {
			System.out.println((String)call.getArgument(0));
			return null;
		}).when(logger).log(Mockito.anyString());
		
		lambdaMethodHandlerUppercase = new LambdaMethodHandlerUppercase();
	}
	
	@Test
	void shouldReturnUppercaseOfInput() {
		
		Mockito.when(context.getFunctionName()).thenReturn("handleRequest");
		
		Assertions.assertEquals("A", lambdaMethodHandlerUppercase.handleRequest("a", context));
		
	}
}
