package kr.inhatc.springsss.configuration;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration //설정파일로 쓸수 있는애
@PropertySource("classpath:/application.properties")  //디비 위치 알려주기
public class DataBaseConfiguration {
	
	@Autowired
	private ApplicationContext applicationContext;   //spring framework 선택

	@Bean 
	@ConfigurationProperties(prefix = "spring.datasource.hikari") //""부분을 접두어로 가져오기
	public HikariConfig hikariConfig() {  //import hikariconfig 하기
		return new HikariConfig(); //Hikari를 설정할수 있는 객체를 만들어서 반환
	}

	@Bean
	public DataSource dataSource() throws Exception{    //datasource.sql 사용,throws 는 오류가 발생하면 위로 던짐
		DataSource dataSource = new HikariDataSource(hikariConfig());   //Hicari(config)
		System.out.println("===============>"+dataSource.toString());
		return dataSource;
	}
	
	@Bean   // 1번
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {//마이바티스 선택
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setMapperLocations(
				applicationContext.getResources("classpath:/mapper/**/sqk-*.xml") //첫번쨰
				);
		return sqlSessionFactoryBean.getObject();  //sqlsessionfactroybean선택
	}
	
	@Bean  //2번         마이 바티스 쓸려면 1.2번 설정해야됨
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
}

