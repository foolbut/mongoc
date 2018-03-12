package self.foolbut.mongoc;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class MongoRepositoryAppFactory implements FactoryBean<IMongoRepository>,InitializingBean {
    private String appName;
    private String accessKey;
    private String publicKey;
    private IMongoRepository _repo;
    private String configURL;

    public MongoRepositoryAppFactory(String configUrl){
        Assert.notNull(configUrl);
        this.configURL = configUrl;
    }
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

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(appName);
        Assert.notNull(accessKey);
        Assert.notNull(publicKey);
    }
}
