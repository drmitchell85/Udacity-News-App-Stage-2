package com.example.android.newsappstage1;

public class Article {
    private String mArticleName;
    private String mArticleUrl;
    private String mArticleType;
    private String mArticleSubject;
    private String mArticleDate;
    private String mArticleAuthor;

    public Article(String articleName, String articleUrl, String articleType, String articleSubject,
                   String articleDate, String articleAuthor) {
        this.mArticleName = articleName;
        this.mArticleUrl = articleUrl;
        this.mArticleType = articleType;
        this.mArticleSubject = articleSubject;
        this.mArticleDate = articleDate;
        this.mArticleAuthor = articleAuthor;
    }

    public String getArticleName() {
        return mArticleName;
    }

    public String getArticleUrl() {
        return mArticleUrl;
    }

    public String getArticleType() {
        return mArticleType;
    }

    public String getArticleSubject() {
        return mArticleSubject;
    }

    public String getArticleDate() {
        return mArticleDate;
    }

    public String getArticleAuthor() {
        return mArticleAuthor;
    }
}
