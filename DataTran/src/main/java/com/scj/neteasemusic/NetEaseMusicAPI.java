package com.scj.neteasemusic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.scj.util.DateUtil;
import com.scj.util.EncodeDecodeUtil;
import com.scj.util.FileUtil;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.CharSet;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/12 0012.
 */
public class NetEaseMusicAPI {
    private final static String modulus = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7" +
            "b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280" +
            "104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932" +
            "575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b" +
            "3ece0462db0a22b8e7";
    private final static String nonce = "0CoJUm6Qyw8W8jud";
    private final static String pubKey = "010001";
    private final static  String headers[][] = {{"Accept","*/*"},
            {"Accept-Encoding","deflate,sdch"},
            {"Accept-Language","zh-CN,zh;q=0.8,gl;q=0.6,zh-TW;q=0.4"},
            {"Connection","keep-alive"},
            {"Content-Type","application/x-www-form-urlencoded"},
            {"Host","music.163.com"},
            {"Referer","http://music.163.com/search/"},
            {"User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36"}
    };
    private final static int METHOD_GET =0;
    private final static int METHOD_POST=1;
    private final static int METHOD_LOGIN=2;

    private final static String CACHE_PATH="f:/wymusic/cookie.json";

    private final static Logger LOGGER = LoggerFactory.getLogger(NetEaseMusicAPI.class);

    private String username;
    private String password;

    private RequestConfig requestConfig =null;
    private HttpClientContext httpClientContext =null;
    private CookieStore cookieStore =null;
    private String uid =null;
    private List<PlayList> playList =new ArrayList<>();

    private Map<String,PlayList> playListMap =new HashMap<>();

    public NetEaseMusicAPI(String username,String password) {
        this.username =username;
        this.password =password;

        final int TIMEOUTMS = 15 * 1000;
        requestConfig = RequestConfig.custom().setConnectionRequestTimeout(TIMEOUTMS).setConnectTimeout(TIMEOUTMS).setSocketTimeout(TIMEOUTMS).build();
        requestConfig = RequestConfig.copy(requestConfig).setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
        httpClientContext =HttpClientContext.create();


        //需要读取本地序列化的cookie信息
        File file =new File(CACHE_PATH);
        if(file.exists()){
           ObjectInputStream ois =null;
            try {
                ois =new ObjectInputStream(new FileInputStream(file));
                cookieStore= (CookieStore) ois.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(cookieStore==null){
            cookieStore=new BasicCookieStore();
            httpClientContext.setCookieStore(cookieStore);
        }
    }

    //based on [darknessomi/musicbox](https://github.com/darknessomi/musicbox)
    public  List<NameValuePair> encryptedRequest(String text) {
        HashMap<String,String> result =new HashMap<>();
        String secKey = createSecretKey(16);
        String encText = aesEncrypt(aesEncrypt(text, nonce), secKey);
        String encSecKey = rsaEncrypt(secKey, pubKey, modulus);
        //System.out.println("encText:"+encText);
        //System.out.println("text:"+text);
        //System.out.println("encSecKey:"+encSecKey);

        List<NameValuePair> params =new ArrayList<>();
        params.add(new BasicNameValuePair("params",encText));
        params.add(new BasicNameValuePair("encSecKey",encSecKey));
        return params;
        //return "params=" + URLEncoder.encode(encText, "UTF-8") + "&encSecKey=" + URLEncoder.encode(encSecKey, "UTF-8");

    }

    //based on [darknessomi/musicbox](https://github.com/darknessomi/musicbox)
    private  String aesEncrypt(String text, String key) {
        try {
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(text.getBytes());

            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            //ignore
            return null;
        }
    }

    //based on [darknessomi/musicbox](https://github.com/darknessomi/musicbox)
    private  String rsaEncrypt(String text, String pubKey, String modulus){
        text = new StringBuilder(text).reverse().toString();
        BigInteger valueInt = hexToBigInteger(stringToHex(text));
        //BigInteger pubkey = hexToBigInteger("010001");
        //BigInteger modulus = hexToBigInteger("00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7");
        return valueInt.modPow(hexToBigInteger(pubKey), hexToBigInteger(modulus)).toString(16);
    }

    private BigInteger hexToBigInteger(String hex) {
        return new BigInteger(hex, 16);
    }

    private String stringToHex(String text)   {

        try {
            return DatatypeConverter.printHexBinary(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        return "";
        }
        }

//based on [darknessomi/musicbox](https://github.com/darknessomi/musicbox)
private  String createSecretKey(int i) {
        return RandomStringUtils.random(i, "0123456789abcde");
        }

    public String sendHttpRequest(int method, String action, String data, List<NameValuePair> params) {
        String response =null;
        CloseableHttpClient httpClient = HttpClients.createDefault();

        if(method ==METHOD_GET){
            HttpGet get =new HttpGet(action);
            get.setConfig(requestConfig);
            for(String[] header:headers){
                get.addHeader(header[0],header[1]);
            }
            try {
                response = httpClient.execute(get, new ResponseHandler<String>() {
                    @Override
                    public String handleResponse(HttpResponse response) {
                        try {
                            return FileUtil.getStringFromInputStream(response.getEntity().getContent());
                        }catch (IOException ex) {
                            return null;
                        }
                    }
                }, httpClientContext);
            }catch (IOException ex) {

            }
        }else if(method ==METHOD_POST){
            HttpPost post =new HttpPost(action);
            for(String[] header:headers){
                post.addHeader(header[0],header[1]);
            }
            try {
                post.setEntity(new StringEntity(data));
                response = httpClient.execute(post, new ResponseHandler<String>() {
                    @Override
                    public String handleResponse(HttpResponse response) {
                            try {
                                return FileUtil.getStringFromInputStream(response.getEntity().getContent());
                            }catch (IOException ex) {
                                return null;
                            }
                    }
                }, httpClientContext);
            }catch (Exception ex){

            }

        }else if(method ==METHOD_LOGIN){
            HttpPost post =new HttpPost(action);
            for(String[] header:headers){
                post.addHeader(header[0],header[1]);
            }
            post.setEntity(new UrlEncodedFormEntity(params, Charsets.UTF_8));
            try {
                response = httpClient.execute(post, new ResponseHandler<String>() {
                    @Override
                    public String handleResponse(HttpResponse response) {
                        try {
                            return FileUtil.getStringFromInputStream(response.getEntity().getContent());
                        }catch (IOException ex) {
                            return null;
                        }
                    }
                }, httpClientContext);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return response;
    }

    public void login(){
        String passwordEncode = EncodeDecodeUtil.encodeWithMD5(password);
        String text ="{\"username\": \""+username+"\", \"rememberLogin\": \"true\", \"password\": \""+passwordEncode+"\"}";
        String response =sendHttpRequest(METHOD_POST,"https://music.163.com/weapi/login?csrf_token=",null,encryptedRequest(text));

        LOGGER.debug("method:login,response:{}",response);
        JSONObject jsonObject = JSON.parseObject(response);
        if(!"200".equals(jsonObject.get("code").toString())){
            LOGGER.info("login failed");
            throw new NetEaseMusicLoginException("登录失败");
        }
        Map account =(Map) jsonObject.get("account");
        uid =account.get("id").toString();

        //序列化cookie到本地
        OutputStream os =null;
        ObjectOutputStream oos =null;
        try {
            os =new FileOutputStream(CACHE_PATH);
            oos =new ObjectOutputStream(os);
            oos.writeObject(cookieStore);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(oos!=null)
                    oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void getUserPlayList(){
        /*if(uid==null){
            System.out.println("uid not existed");
            return;
        }*/
        String action = MessageFormat.format("http://music.163.com/api/user/playlist/?offset={0}&limit={1}&uid={2}","0","100","30664411");
        String response =sendHttpRequest(METHOD_GET,action,null,null);
        JSONObject object = JSON.parseObject(response);
        List<JSONObject> playListObject =(List<JSONObject>)object.get("playlist");
        for (JSONObject jsonObject:playListObject){
            if(playList ==null){
                playList =new ArrayList<>();
                PlayList playListItem =new PlayList();
                playListItem.setId(jsonObject.get("id").toString());
                if(playListItem.getId()==null)
                    continue;
                playListItem.setName(jsonObject.get("name").toString());
                playListItem.setCoverImgUrl(jsonObject.get("coverImgUrl").toString());
                playListItem.setPlayCount(jsonObject.get("playCount").toString());
                getPlayListDetail(playListItem);
                playList.add(playListItem);
                playListMap.put(playListItem.getId(),playListItem);
            }else {
                //需要增加去重逻辑
                PlayList playListItem =new PlayList();
                playListItem.setId(jsonObject.get("id").toString());
                if(playListItem.getId()==null)
                    continue;
                playListItem.setName(jsonObject.get("name").toString());
                playListItem.setCoverImgUrl(jsonObject.get("coverImgUrl").toString());
                playListItem.setPlayCount(jsonObject.get("playCount").toString());
                getPlayListDetail(playListItem);
                playList.add(playListItem);
                playListMap.put(playListItem.getId(),playListItem);
            }
        }

        System.out.println(playList);
    }

    private void getPlayListDetail(PlayList playList){
        String action =MessageFormat.format("http://music.163.com/api/playlist/detail?id={0}",playList.getId());
        String response =sendHttpRequest(METHOD_GET,action,null,null);
        JSONObject object = JSON.parseObject(response);
        Map result = (Map) object.get("result");
        List<JSONObject> songObjects = (List<JSONObject>) result.get("tracks");
        List<Song> songs =new ArrayList<>();
        for (JSONObject songObject :songObjects){
            Song song =new Song();
            song.setId(songObject.get("id").toString());
            song.setName(songObject.get("name").toString());
            List<JSONObject> artistList = (List<JSONObject>) songObject.get("artists");
            String artistName =artistList.get(0).get("name").toString();
            song.setArtistName(artistName);
            song.setMp3Url(songObject.get("mp3Url").toString());
            songs.add(song);
        }
        playList.setSongs(songs);
    }

    public static void main(String[] args) {
        NetEaseMusicAPI api = new NetEaseMusicAPI("13388611621","Scj@1992");
        api.login();
        api.getUserPlayList();
        api.exportPlayListsAsExcel(api.getPlayList());
        //api.getPlayListDetail("436603882");//工作的时候
        //System.out.println(createSecretKey(16));
        /*String secKey = createSecretKey(16);
        String encText = aesEncrypt(aesEncrypt("123", nonce), secKey);
        System.out.println(encText);*/
    }

    public void exportPlayListsAsExcel(List<PlayList> playLists){
        String filePath ="f:/wymusic/export/";
        File file =new File(filePath);
        if(!file.exists()){
            file.mkdirs();
        }

        WritableWorkbook book =null;
        try {
            book = Workbook.createWorkbook(new File(filePath+DateUtil.getSystemTimeYYYYMM()+".xls"));
            int i =0;
            for(PlayList playList:playLists){
                String sheetName =playList.getName().substring(0,playList.getName().length()>31?31:playList.getName().length());
                WritableSheet sheet =book.createSheet(sheetName,i);
                sheet.addCell(new Label(0,0,"ID"));
                sheet.addCell(new Label(1,0,"歌名"));
                sheet.addCell(new Label(2,0,"歌手名"));
                sheet.addCell(new Label(3,0,"歌曲url"));
                if(playList.getSongs()!=null&&playList.getSongs().size()>0){
                    int row =1;
                    for(Song song :playList.getSongs()){
                        sheet.addCell(new Label(0,row,song.getId()));
                        sheet.addCell(new Label(1,row,song.getName()));
                        sheet.addCell(new Label(2,row,song.getArtistName()));
                        sheet.addCell(new Label(3,row++,song.getMp3Url()));
                    }
                }
            }
            book.write();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }finally {
            if(book!=null){
                try {
                    book.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    static class PlayList{
        private String id ;
        private String name;
        private String coverImgUrl;
        private String playCount;

        private List<Song> songs =new ArrayList<>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCoverImgUrl() {
            return coverImgUrl;
        }

        public void setCoverImgUrl(String coverImgUrl) {
            this.coverImgUrl = coverImgUrl;
        }

        public String getPlayCount() {
            return playCount;
        }

        public void setPlayCount(String playCount) {
            this.playCount = playCount;
        }

        public List<Song> getSongs() {
            return songs;
        }

        public void setSongs(List<Song> songs) {
            this.songs = songs;
        }

        @Override
        public String toString() {
            return "PlayList{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", coverImgUrl='" + coverImgUrl + '\'' +
                    ", playCount='" + playCount + '\'' +
                    ", songs=" + songs +
                    '}';
        }
    }

    static class Song{
        private String id;
        private String name;
        private String mp3Url;
        private String artistName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMp3Url() {
            return mp3Url;
        }

        public void setMp3Url(String mp3Url) {
            this.mp3Url = mp3Url;
        }

        public String getArtistName() {
            return artistName;
        }

        public void setArtistName(String artistName) {
            this.artistName = artistName;
        }

        @Override
        public String toString() {
            return "Song{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", mp3Url='" + mp3Url + '\'' +
                    ", artistName='" + artistName + '\'' +
                    '}';
        }
    }

    public String getUid() {
        return uid;
    }

    public List<PlayList> getPlayList() {
        return playList;
    }

    public Map<String, PlayList> getPlayListMap() {
        return playListMap;
    }
}
