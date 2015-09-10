package com.activebeancoders.controller.es;

import com.activebeancoders.Config;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Scanner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class DataLoadControllerTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public DataLoadController dataLoadController;

    @Test
    public void reloadActivities() throws Exception {
        dataLoadController.reloadActivities("10");
        new Scanner(System.in).nextLine();
    }

}
