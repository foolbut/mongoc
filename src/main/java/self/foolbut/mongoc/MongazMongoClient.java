package self.foolbut.mongoc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import self.foolbut.mongoc.annotation.EncryptUtils;
import self.foolbut.mongoc.ex.DatabaseNotAuthorizedException;

public class MongazMongoClient extends MongoClient implements InitializingBean{

    private String appName;
    private String accessToken;
    private String privateKey;
    private String publicKey;
    private String url;
    
    private MongoClient _client;
    private String _dbName;
    

    public MongazMongoClient(){
        
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub
        Assert.notNull(appName);
        Assert.notNull(accessToken);
        Assert.notNull(privateKey);
        Assert.notNull(publicKey);
        Assert.notNull(url);
        
        StringBuilder urlBuilder= new StringBuilder(url);
        if(!url.endsWith("/")){
            urlBuilder.append("/");
        }
        
        PublicKey publicKeyG = EncryptUtils.getPublicKey(publicKey);
        urlBuilder.append("config?_access=").append(accessToken).append("&_sign=").append(EncryptUtils.encrypt(accessToken.getBytes(), publicKeyG));
        
        ConnectionConfig config = getResponse(urlBuilder.toString());
        this._dbName = config.database;
        MongoCredential c = MongoCredential.createCredential(config.userName,config.database, config.password.toCharArray());
        this._client = new MongoClient(new ServerAddress(config.host,config.port),Arrays.asList(new MongoCredential[]{c}));
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public MongoDatabase getDatabase(final String databaseName){
        if(null == this._dbName || !this._dbName.equals(databaseName)){
            throw new DatabaseNotAuthorizedException(databaseName);
        }
        return this._client.getDatabase(_dbName);
    }
    
    private ConnectionConfig getResponse(String url) throws Exception{
        URL realUrl = new URL(url);
        URLConnection connection = realUrl.openConnection();
        // ����ͨ�õ���������
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // ����ʵ�ʵ�����
        connection.connect();
        Map<String, List<String>> map = connection.getHeaderFields();
        List<String> statusList = map.get("status");
        if(null == statusList || 0 == statusList.size()|| !"200".equals(statusList.get(0))){
            throw new RuntimeException("Connect to config server failed.");
        }
        BufferedReader in = null;
        StringBuilder resultBuilder = new StringBuilder();
        try{
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                resultBuilder.append(line);
            }
        }finally{
            if(null != in) in.close();
        }
        
        PrivateKey privateKeyG = EncryptUtils.getPrivateKey(privateKey);
        byte[] decrypted = EncryptUtils.decryptByPrivateKey(resultBuilder.toString(), privateKeyG);
        String finalString = new String(decrypted,"UTF-8");
        Document doc = Document.parse(finalString);
        
        ConnectionConfig config = new ConnectionConfig();
        config.database = doc.getString("database");
        config.host = doc.getString("host");
        config.password = doc.getString("password");
        config.userName = doc.getString("userName");
        config.port = doc.getInteger("port");
        return config;
    }
    
    
    private class ConnectionConfig{
        private String database;
        private String userName;
        private String password;
        private String host;
        private int port;
    }
}
