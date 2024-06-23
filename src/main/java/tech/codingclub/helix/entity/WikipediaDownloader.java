package tech.codingclub.helix.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tech.codingclub.helix.global.HTTPUrlConnectionExample;

public class WikipediaDownloader{

    private String keyword;

    public WikipediaDownloader(){

    }
    public WikipediaDownloader(String keyword){

        this.keyword = keyword;
    }
    public WikiResult getResult() {
        //1.Get clean keyword
        //2.Get url for wikipedia
        //3.Make GET request to wikipedia
        //4.Parse the useful result using jsoup
        //5.Showing result !
        if(this.keyword==null || this.keyword.length()==0){
            return null;
        }
        //STEP1
        this.keyword=this.keyword.replaceAll("[ ]+","_");
        //STEP2
        String wikipediaURL="https://en.wikipedia.org/wiki/"+keyword;
        String response="";
        String imageUrl = null;

        //STEP3
        try {
            String wikipediaResponseHtml= HTTPUrlConnectionExample.sendGet(wikipediaURL);

            Document document= Jsoup.parse(wikipediaResponseHtml,"https://en.wikipedia.org");
            Elements childElements=document.body().select(".mw-parser-output > *");
            int state=0;
            for(Element childElement:childElements){
                if(state==0){
                    if(childElement.tagName().equals("table")){
                        state=1;
                    }
                } else if (state==1) {
                    if(childElement.tagName().equals("p")){
                        state=2;
                        response+=childElement.text();
                        break;
                    }
                }
            }
            try{
                 imageUrl = document.body().select(".infobox img").get(0).attr("src");
            }
            catch(Exception e){

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        WikiResult wikiResult=new WikiResult(this.keyword,response,imageUrl);
        return wikiResult;

    }




}
