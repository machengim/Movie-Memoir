package com.fit5046.m3.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fit5046.m3.R;
import com.fit5046.m3.lib.NetworkConnection;
import com.fit5046.m3.ui.addmemoir.AddMemoirViewModel;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

// Used for the send tweet dialog.
public class SendTweetFragment extends DialogFragment {

    private SendTweetFragment root;
    private String message;
    private AddMemoirViewModel model;

    public SendTweetFragment() {
        root = this;
        this.message = "";
    }

    public SendTweetFragment(String message) {
        root = this;
        this.message = message;
    }

    public void setModel(AddMemoirViewModel model) {
        this.model = model;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return new TextView(getActivity());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Saved successfully! Wanna sent a tweet as below?\n\n\n\"" + message + "\"\n\n")
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    Thread t = new Thread(this::sendTweet);
                    t.start();
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    model.getTweetSent().setValue(1);
                    root.dismiss();
                });

        return builder.create();
    }

    // send tweet using the twitter api and twitter4j library which is downloaded and put into lib directory.
    private void sendTweet() {

        String token = NetworkConnection.TWITTER_TOKEN;
        String secret = NetworkConnection.TWITTER_TOKEN_SECRET;
        AccessToken access = new AccessToken(token, secret);
        Twitter twitter = new TwitterFactory().getInstance();
        String ckey = NetworkConnection.TWITTER_CONSUMER_KEY;
        String csecret = NetworkConnection.TWITTER_CONSUMER_KEY_SECRET;
        twitter.setOAuthConsumer(ckey, csecret);
        twitter.setOAuthAccessToken(access);

        try {
            twitter.updateStatus(message);
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        model.getTweetSent().postValue(2);
    }
}
