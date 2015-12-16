package com.activebeancoders.fitness.activebeancoders;

import com.activebeancoders.fitness.HibernateServiceApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HibernateServiceApplication.class)
public abstract class BaseTestIT {

}
