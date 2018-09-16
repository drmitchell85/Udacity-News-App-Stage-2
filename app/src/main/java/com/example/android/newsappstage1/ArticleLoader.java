package com.example.android.newsappstage1;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<List<Article>>{

    /**Tag for log messages*/
    private static final String LOG_TAG = ArticleLoader.class.getSimpleName();

    /**Query URL*/
    private String mUrl;

    public ArticleLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /** Background thread */
    @Override
    public List<Article> loadInBackground() {
        if(mUrl==null){
            return null;
        }

        // Perform network request, extract info, and parse
        List<Article> articles = QueryUtils.fetchArticleData(mUrl);
        return articles;
    }
}
