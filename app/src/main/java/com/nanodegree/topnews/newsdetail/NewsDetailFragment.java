package com.nanodegree.topnews.newsdetail;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.nanodegree.topnews.Constants;
import com.nanodegree.topnews.R;
import com.nanodegree.topnews.data.BookmarksManager;
import com.nanodegree.topnews.databinding.FragmentNewsDetailBinding;
import com.nanodegree.topnews.model.Article;
import com.nanodegree.topnews.util.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsDetailFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Context context;
    private Activity activity;
    private FragmentNewsDetailBinding binding;
    private ProgressDialog progressDialog;
    private ImageLoadingCallback imageLoadingCallback;
    private Article article;

    public interface ImageLoadingCallback {
        void onImageLoaded(Bitmap bitmap);
    }

    public NewsDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsDetailFragment newInstance(String param1, String param2) {
        NewsDetailFragment fragment = new NewsDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_detail, container, false);
        binding.setClickHandler(this);

        String jsonString = getActivity().getIntent().getStringExtra(Constants.NEWS_DETAIL);
        Gson gson = new Gson();
        article = gson.fromJson(jsonString, Article.class);

        binding.webDetailContent.setWebViewClient(new NewsDetailWebViewClient());
        binding.webDetailContent.getSettings().setJavaScriptEnabled(true);
        if (article != null) {
            binding.ivDetailBookmark.setSelected(BookmarksManager.isBookmarked(context, article));
            binding.tvDetailTitle.setText(article.getTitle());
            binding.tvDetailTime.setText(Utils.getDisplayTextTime(article.getPublishedAt()));
            binding.webDetailContent.loadUrl(article.getUrl());
        }

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                binding.ivDetailImage.setImageBitmap(bitmap);
                imageLoadingCallback.onImageLoaded(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        if (article != null && article.getUrlToImage() != null)

        {
            Picasso.with(context).load(article.getUrlToImage()).into(target);
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = (Activity) context;
        imageLoadingCallback = (ImageLoadingCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_detail_bookmark:
                if (binding.ivDetailBookmark.isSelected()) {
                    binding.ivDetailBookmark.setSelected(false);
                    BookmarksManager.deleteBookmark(context, article);
                } else {
                    binding.ivDetailBookmark.setSelected(true);
                    BookmarksManager.addBookmark(context, article);
                }
                break;

            default:
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class NewsDetailWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (activity == null || activity.isDestroyed()) {
                return;
            }
            progressDialog.show();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (activity == null || activity.isDestroyed()) {
                return;
            }
            progressDialog.dismiss();
            super.onPageFinished(view, url);
        }
    }
}
