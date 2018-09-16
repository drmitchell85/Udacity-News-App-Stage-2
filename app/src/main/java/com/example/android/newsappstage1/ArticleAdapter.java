package com.example.android.newsappstage1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(Context context, List<Article> objects) {
        super(context, 0, objects);
    }

    /**
     * Override and prep the inflater
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Create inflater
        View listItemView = convertView;
        if(listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_list_view, parent, false);
        }

        // Get current variable position
        Article currentArticle = getItem(position);

        TextView articleNameTextView = listItemView.findViewById(R.id.article_name);
        articleNameTextView.setText(currentArticle.getArticleName());

        TextView articleTypeTextView = listItemView.findViewById(R.id.article_type);
        articleTypeTextView.setText(currentArticle.getArticleType());

        TextView articleSubjectTextView = listItemView.findViewById(R.id.article_subject);
        articleSubjectTextView.setText(currentArticle.getArticleSubject());

        TextView articleDateTextView = listItemView.findViewById(R.id.article_date);
        articleDateTextView.setText(currentArticle.getArticleDate());

        TextView articleAuthorTextView = listItemView.findViewById(R.id.article_author);
        articleAuthorTextView.setText(currentArticle.getArticleAuthor());

        return listItemView;

    }

}
