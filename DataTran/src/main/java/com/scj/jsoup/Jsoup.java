package com.scj.jsoup;

import com.alibaba.fastjson.JSONObject;
import com.scj.neteasemusic.NetEaseMusicAPI;
import org.apache.http.NameValuePair;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shengchaojie on 2017/5/20.
 */
public class Jsoup {

    private static final String BASE_URL = "http://music.163.com";
    public static final String text = "{\"username\": \"\", \"rememberLogin\": \"true\", \"password\": \"\"}";

    public static void main(String[] args) throws IOException {
        /*String html = "<html><head><title>First parse</title></head>"
                + "<body><p>Parsed HTML into a doc.</p></body></html>";
        Document doc = Jsoup.parse(html);
        System.out.println(doc.body());

        Document document2 = Jsoup.connect("http://music.163.com/discover/artist/cat?id=1001").get();
        System.out.println(document2.body());
        Elements songers = document2.select("ul#m-artist-box li a");
        for (Element p :songers){
            System.out.println(p.html()+" "+p.attr("href"));
        }*/

        //爬取歌手排行榜
        Document artistCatalogDoc = org.jsoup.Jsoup.connect("http://music.163.com/discover/artist").get();
        Elements catalogs = artistCatalogDoc.select("div.blk .cat-flag");
        Map<String,String> catalogMap =new HashMap<>();
        for (Element cat : catalogs) {
            catalogMap.put(cat.html(),BASE_URL+cat.attr("href"));
            System.out.println(cat.html() + " " + cat.attr("href"));
            break;
        }

        for(Map.Entry<String,String> entry:catalogMap.entrySet()){
            String catalogUrl =entry.getValue();
            //爬对应排行下面的自分类 热门 A B C D..
            List<String> itemUrls =new ArrayList<>();
            Document itemDoc = org.jsoup.Jsoup.connect(catalogUrl).get();
            Elements items =itemDoc.select("ul#initial-selector>li>a");
            System.out.println(entry.getKey());
            for(Element item :items){
                itemUrls.add(BASE_URL+item.attr("href"));
                System.out.println(item.attr("href")+" "+item.html());
                break;
            }
            //爬取item下面歌手
            for (String itemUrl :itemUrls){
                itemDoc = org.jsoup.Jsoup.connect(itemUrl).get();
                Elements singerItems =itemDoc.select("ul#m-artist-box li p a");
                for (Element singerItem:singerItems){
                    //把主页的url替换为所有专辑 /artist->/artist/album
                    String top50SongUrl =BASE_URL+singerItem.attr("href").trim();
                    String allAlbum =top50SongUrl.replace("artist","artist/album");
                    System.out.println(singerItem.html()+" "+allAlbum);
                    //爬取所有专辑 有分页
                    //需要先爬取分页 通过下一页 一页一页爬
                    Document albumDoc = org.jsoup.Jsoup.connect(allAlbum).get();
                    //爬取当前页
                    Elements albumEles =albumDoc.select("ul#m-song-module>li>div");
                    for (Element albumEle :albumEles){
                        System.out.println(albumEle.attr("title")+" "+BASE_URL+albumEle.select("a.msk").attr("href"));
                        crawlAlbumSong(BASE_URL+albumEle.select("a.msk").attr("href"));
                    }

                    //是否有下一页
                    Elements nextPageEle =albumDoc.select("div.u-page>.znxt");
                    boolean isHaveNext =nextPageEle.size()>0?nextPageEle.get(0).attr("href").contains("javascript")?false:true:false;
                    while(isHaveNext){
                        String nextPageUrl =nextPageEle.get(0).attr("href");
                        albumDoc = org.jsoup.Jsoup.connect(BASE_URL+nextPageUrl).get();
                        albumEles =albumDoc.select("ul#m-song-module>li>div");
                        nextPageEle =albumDoc.select("div.u-page>.znxt");
                        isHaveNext =nextPageEle.size()>0?nextPageEle.get(0).attr("href").contains("javascript")?false:true:false;
                        for (Element albumEle :albumEles){
                            System.out.println(albumEle.attr("title")+" "+BASE_URL+albumEle.select("a.msk").attr("href"));
                            crawlAlbumSong(BASE_URL+albumEle.select("a.msk").attr("href"));
                        }
                    }

                    break;
                }
            }

        }
    }

    public static void crawlAlbumSong(String albumUrl) throws IOException {
        Document albumDoc = org.jsoup.Jsoup.connect(albumUrl).get();
        Elements songs =albumDoc.select("ul.f-hide li a");
        for (Element song :songs){
            String url =BASE_URL+song.attr("href");
            System.out.println(song.html()+" "+url+" "+crawlAlbumSongCommentCount(url));
        }
    }

    public static String crawlAlbumSongCommentCount(String songDetailUrl) throws IOException {
        Document songDetailDoc = org.jsoup.Jsoup.connect(songDetailUrl).get();
        Elements countElement = songDetailDoc.select("#comment-box");
        String dataTId =countElement.get(0).attr("data-tid");
        //需要根据这个调用接口去拿值
        NetEaseMusicAPI netEaseMusicAPI =new NetEaseMusicAPI("","");
        List<NameValuePair> params = netEaseMusicAPI.encryptedRequest(text);
        HashMap<String,String> paramMap =new HashMap();
        for (NameValuePair param:params){
            paramMap.put(param.getName(),param.getValue());
        }
        Connection.Response response = org.jsoup.Jsoup.connect("http://music.163.com/weapi/v1/resource/comments/"+dataTId+"?csrf_token=")
                .method(Connection.Method.POST)
                .header("Referer",BASE_URL)
                .data(paramMap)
                .execute();
        //System.out.println(response.body());
        //System.out.println(dataTId);
        JSONObject jsonObject= (JSONObject) JSONObject.parse(response.body());

        if(jsonObject!=null&&jsonObject.containsKey("total")){
            return jsonObject.get("total").toString();
        }

        return "";
    }
}
