package org.hibernate.hsql.test;

import java.io.File;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HsqlTest {
	
	private Configuration cfg;
	private SessionFactory sf;
	private File outputDir;
	
	@Before
	public void setUp() {
		System.out.println("entering 'setUp()'");

		cfg = new Configuration();		
		cfg.setProperty(Environment.HBM2DDL_AUTO, "update");
		cfg.addResource("org/hibernate/hsql/test/UserGroup.hbm.xml");
		
		sf = cfg.buildSessionFactory();
		
		Session s = sf.openSession();
		
		User user = new User("max", "jboss");
		s.persist( user );		
		user = new User("gavin", "jboss");
		s.persist( user );		
		s.flush();		
		s.close();

		outputDir = new File("output");
		outputDir.mkdirs();
		
		System.out.println("exiting 'setUp()'");
	}
	
	@Test
	public void testHsql() {
		System.out.println("entering 'testHsql()'");
		
		Session session = null;
		SessionFactory sessionFactory = null;
		try {	
			Configuration configuration = cfg;
			Properties properties = configuration.getProperties();
			Environment.verifyProperties(properties);
			ConfigurationHelper.resolvePlaceHolders(properties);
			sessionFactory = configuration.buildSessionFactory();
			session = sessionFactory.openSession();
		} finally {			
			if(session!=null) {
				session.close();				
			}
			if(sessionFactory!=null) {
				sessionFactory.close();
			}			
		}
		
		System.out.println("exiting 'testHsql()'");
	}
	
	@After
	public void tearDown() throws Exception {
		System.out.println("entering 'tearDown()'");
		try {
			sf.close();
			FileUtils.deleteDirectory(new File("testdb"));
			FileUtils.deleteDirectory(outputDir);
		} finally {
			System.out.println("exiting 'tearDown()'");
		}
	}

}
