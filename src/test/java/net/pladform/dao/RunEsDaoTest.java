package net.pladform.dao;

import net.pladform.entity.Run;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RunEsDaoTest {

    public static RunEsDao runEsDao;

    @BeforeClass
    public static void beforeClass() {
        runEsDao = new RunEsDao();
    }

    @Test
    public void testSaveToElasticsearchThenRetrieve() throws Exception {
        // create a run
        Run toBeSaved = new Run();
        toBeSaved.id = "1";
        toBeSaved.name  = "my run around the house";
        toBeSaved.location = "Parker, CO";
        toBeSaved.miles = 1.2;

        // save
        runEsDao.save(toBeSaved);

        // get
        Run retrieved = runEsDao.get(toBeSaved.id);
        System.out.println(retrieved);

        Assert.assertNotNull(toBeSaved);
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(toBeSaved.id, retrieved.id);
        Assert.assertEquals(toBeSaved.name, retrieved.name);
        Assert.assertEquals(toBeSaved.location, retrieved.location);
        Assert.assertEquals(toBeSaved.miles, retrieved.miles);
        Assert.assertEquals(toBeSaved.steps, retrieved.steps);
        Assert.assertEquals(toBeSaved.caloriesBurned, retrieved.caloriesBurned);
    }

}
