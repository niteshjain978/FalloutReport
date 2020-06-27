package com.fallout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Arrays;

@SpringBootApplication
public class FalloutApplication implements ApplicationRunner {

	static final Logger logger = LoggerFactory.getLogger(FalloutApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(FalloutApplication.class, args);
	}

	
	 @Override
	 public void run(ApplicationArguments args) throws Exception {
	        logger.info("Application started with command-line arguments: {}", Arrays.toString(args.getSourceArgs()));
	       
	        for (String name : args.getOptionNames()){
	            logger.info("arg-" + name + "=" + args.getOptionValues(name));
	        }

	        boolean containsWaveName = args.containsOption("wave.name");
	        logger.info("Contains wave.name: " + containsWaveName);
	        
	        boolean containsInputPath = args.containsOption("input.path");
	        logger.info("Contains input.path: " + containsInputPath);
	        
	        if( containsWaveName && containsInputPath) {
	        	App.Start(args.getOptionValues("wave.name").get(0), args.getOptionValues("input.path").get(0));
	        	logger.info("Successfully Report Generated");
	        }else {
	        	logger.info("Stop Application. Missing Argument");
	        }
	        
	        
	    }
}
