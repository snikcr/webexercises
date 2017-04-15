package practice;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class App extends AsyncConfigurerSupport implements CommandLineRunner {

	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final Logger logger = Logger.getLogger(App.class);
	
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
	
	@Override
	public Executor getAsyncExecutor() {

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("rest-practice-");
		executor.initialize();
		return executor;
	}
	
	@Override
	public void run(String... arg0) throws Exception {

		logger.info("Creating database.");
		
		jdbcTemplate.execute("drop table if exists users");
		jdbcTemplate.execute("create table users(id integer primary key asc not null, name text not null, birthdate text not null)");
		
		List<Object[]> list = Arrays.asList(
				"Frodo/28-01-1981",
				"Aragorn/20-10-1958",
				"Legolas/13-01-1977",
				"Gandalf/25-05-1939",
				"Samsagaz/10-02-1971",
				"Meriadoc/08-12-1976",
				"Peregrin/28-08-1968",
				//"Boromir/17-04-1959", will be added later
				"Gimli/05-05-1944")
			.stream()
			.map(data -> data.split("/"))
			.collect(Collectors.toList());
		
		jdbcTemplate.batchUpdate("insert into users(name, birthdate) values(?, ?)", list);
		
		logger.info("Database created.");
	}
	
	@Bean
	public DataSource dataSource() {
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName("org.sqlite.JDBC");
		dataSourceBuilder.url("jdbc:sqlite:practiceDB");
		return dataSourceBuilder.build();
	}
	
	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageBundle = new ReloadableResourceBundleMessageSource();
		messageBundle.setBasename("messages/messages");
		messageBundle.setDefaultEncoding("UTF-8");
		return messageBundle;
	}
}
