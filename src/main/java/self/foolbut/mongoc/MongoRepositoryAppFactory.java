package self.foolbut.mongoc;


import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.bson.BsonDocument;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import static java.net.HttpURLConnection.HTTP_OK;

public class MongoRepositoryAppFactory implements FactoryBean<IMongoRepository>,InitializingBean {
    private String groupName;
    private String appName;
    private String accessKey;
    private String publicKey;
    private IMongoRepository _repo;
    private String configURL;

    public MongoRepositoryAppFactory(String configUrl){
        Assert.notNull(configUrl);
        this.configURL = configUrl;
    }

    public void setGroupName(String _groupName){
        this.groupName = _groupName;
    }
    public void setAppName(String _appName){
        this.appName = appName;
    }
    public void setAccessKey(String _accessKey) {
        accessKey = _accessKey;
    }

    public void setPublicKey(String _publicKey) {
        publicKey = _publicKey;
    }

    public void setConfigURL(String _configURL) {
        configURL = _configURL;
    }

    @Override
    public IMongoRepository getObject() throws Exception {
        if (null ==_repo) throw new NullPointerException("MongoRepository not generated");
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
        Assert.notNull(groupName);
        Assert.notNull(appName);
        Assert.notNull(accessKey);
        Assert.notNull(publicKey);

        StringBuilder respBuilder = new StringBuilder();
        BufferedReader br = null;
        try{
            URL url = new URL(configURL+"/config/"+groupName+"/"+appName+"/connect?_access="+accessKey);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(3);
            con.setReadTimeout(3);
            // connect server for db-connection info
            con.connect();
            int statusCode = con.getResponseCode();
            if(HTTP_OK  != statusCode){
                throw new IOException("config server responses error, status:"+ statusCode);
            }

            br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));


            String tmp = null;
            while((tmp = br.readLine()) != null){
                respBuilder.append(tmp);
            }
            br.close();
        }catch(Exception ex){
            if(null != br) br.close();
            throw ex;
        }


        String decrypted = _decrypt(respBuilder.toString());
        BsonDocument bson = BsonDocument.parse(decrypted);
        _configRepo(bson);

    }


    private String _decrypt(String resp){
        return resp;
    }

    private void _configRepo(BsonDocument bson){
        Assert.notNull(bson.getString("dbName"));
        Assert.notNull(bson.getString("userName"));
        Assert.notNull(bson.getString("password"));
        Assert.notNull(bson.getString("host"));
        Assert.notNull(bson.getInt32("port"));

        String dbName = bson.getString("dbName").getValue();
        String userName = bson.getString("userName").getValue();
        String password = bson.getString("password").getValue();
        String host = bson.getString("host").getValue();
        Integer port = bson.getInt32("port").getValue();

        MongoCredential c = MongoCredential.createCredential(userName, dbName, password.toCharArray());
        MongoClient _client = new MongoClient(new ServerAddress(host,port), Arrays.asList(new MongoCredential[]{c}));
        _repo = new MongoRepository(_client,dbName);
    }
}
