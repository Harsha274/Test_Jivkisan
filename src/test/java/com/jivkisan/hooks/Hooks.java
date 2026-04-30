package com.jivkisan.hooks;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jivkisan.factory.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
 
public class Hooks {
    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);
 
    @Before
    public void setUp() {
        logger.info("Initializing Edge Browser for JivKisan Testing");
        DriverFactory.initDriver();
    }
 
    @After
    public void tearDown() {
        logger.info("Closing Edge Browser");
        DriverFactory.quitDriver();
    }
}