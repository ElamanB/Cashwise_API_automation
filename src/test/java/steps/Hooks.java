package steps;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {


    @Before
    public static void setUp(Scenario scenario) {
        System.out.println("STARTING SCENARIO " + scenario.getName());

    }

    public static void cleanUp(Scenario scenario) {
        System.out.println("ENDING SCENARIO " + scenario.getName());

    }


}
