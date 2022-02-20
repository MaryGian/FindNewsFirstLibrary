package model;

import model.newsapi.Article;
import model.newsapi.NewsResults;

public class NewsInfo {

    private String title;
    private String content;
    private String date;
    private String url_page;
    private String authors;
    private String publisher;



    public NewsInfo(String title, String content, String date, String url_page, String authors,String publisher) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.url_page = url_page;
        this.authors = authors;
        this.publisher = publisher;

    }

    public NewsInfo(Article article) {
        this.title= article.getTitle();
        this.content =article.getDescription();
        this.authors = article.getAuthor();
        this.date = article.getPublishedAt();
        this.url_page = article.getUrl();
        this.publisher= article.getSource().getName();

    }



    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getUrl_page() {
        return url_page;
    }

    public String getAuthors() {
        return authors;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUrl_page(String url_page) {
        this.url_page = url_page;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    @Override
    public String toString(){
        return "NewsInfo{ Publisher: "+publisher+"\n title: "+this.title +"\n content: "+ this.content+ "\n authror: "+this.authors+
                "\npublished at: "+this.date+ "\n url: "+this.url_page;
    }
}
