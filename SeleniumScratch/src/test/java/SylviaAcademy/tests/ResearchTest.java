package SylviaAcademy.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import SylviaAcademy.base.BaseTest;

public class ResearchTest extends BaseTest {
	

    @Test(retryAnalyzer = SylviaAcademy.resources.Retry.class)
    public void testFail() {
        System.out.println("Test started");
        
        // Ce test échoue intentionnellement
        Assert.fail("This test is meant to fail");
        
        System.out.println("Test completed");
    }

}
