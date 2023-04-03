package ilovepc.playgroundforus.config.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:/application.properties")
public class DatabaseInfoMasterConfig {
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "pgfuDatasource", destroyMethod = "close")
    @ConfigurationProperties("spring.datasource.url")
    public HikariDataSource dataSource(DataSourceProperties properties){
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    /* MyBatis JAVA CONFIG!! */
    @Bean(name = "pgfuSessionFactory")
    public SqlSessionFactory pgfuSessionFactory(@Qualifier("pgfuDatasource") DataSource dataSource)throws Exception{
        return new DatabaseSqlSessionFactory().getSqlFactory(dataSource);
    }
}
