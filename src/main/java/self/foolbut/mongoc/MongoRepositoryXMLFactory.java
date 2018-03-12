package self.foolbut.mongoc;

import java.util.Arrays;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoRepositoryXMLFactory implements FactoryBean<IMongoRepository>,InitializingBean{

    private String dbName;
    private String userName;
    private String password;
    private String host;
    private Integer port;

    private IMongoRepository _repo;
    @Override
    public IMongoRepository getObject() throws Exception {
        return _repo;
    }

    @Override
    public Class<?> getObjectType() {
        return IMongoRepository.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public String getDbName() {
        return dbName;
    }
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public Integer getPort() {
        return port;
    }
    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(isBlank(dbName)) throw new NullPointerException("mongo dbName must be not null");
        if(isBlank(userName)) throw new NullPointerException("mongo username must be not null");
        if(isBlank(password)) throw new NullPointerException("mongo password must be not null");
        if(isBlank(host)) throw new NullPointerException("mongo host must be not null");
        if(null == port) throw new NullPointerException("mongo port must be not null");

        MongoCredential c = MongoCredential.createCredential(userName, dbName, password.toCharArray());
        MongoClient _client = new MongoClient(new ServerAddress(host,port),Arrays.asList(new MongoCredential[]{c}));
        _repo = new MongoRepository(_client,dbName);
    }
    
    private boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
}
