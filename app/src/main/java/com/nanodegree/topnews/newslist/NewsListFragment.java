package com.nanodegree.topnews.newslist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nanodegree.topnews.Constants;
import com.nanodegree.topnews.R;
import com.nanodegree.topnews.data.Preferences;
import com.nanodegree.topnews.databinding.FragmentNewsListBinding;
import com.nanodegree.topnews.interactor.GetNewsListUseCase;
import com.nanodegree.topnews.model.Article;
import com.nanodegree.topnews.model.ArticlesCollection;

import java.util.List;

import rx.Subscriber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentNewsListBinding binding;
    private OnFragmentInteractionListener mListener;
    private Context context;
    private LinearLayoutManager layoutManager;
    private NewsListAdapter adapter;
    private List<Article> listArticles;
    private GetNewsListUseCase getNewsListUseCase;
    private Activity activity;
    private ProgressDialog progressDialog;
    private String newsSourceId;

    public NewsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsListFragment newInstance(String param1, String param2) {
        NewsListFragment fragment = new NewsListFragment();
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

        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_news_list, container, false);

        layoutManager = new LinearLayoutManager(context);
        adapter = new NewsListAdapter((NewsListAdapter.NewsItemSelectionListener) activity, context, listArticles);

        binding.recyclerNewsList.setLayoutManager(layoutManager);
        binding.recyclerNewsList.setAdapter(adapter);

        if (activity instanceof NewsListActivity) {
            newsSourceId = Preferences.getString(context, Constants.NEWS_SOURCE_ID);
            getNewsListUseCase = new GetNewsListUseCase(context);
            doApiCallGetNewsList(newsSourceId);
        } else {
            //bookmarks
            if (adapter != null) {
                adapter.setData(listArticles);
            }
        }

        return binding.getRoot();
    }

    public void setData(List<Article> articles) {
        listArticles = articles;
        if (adapter != null) {
            adapter.setData(articles);
        }
    }

    public void refreshNewsList() {
        newsSourceId = Preferences.getString(context, Constants.NEWS_SOURCE_ID);
        doApiCallGetNewsList(newsSourceId);
    }

    private void doApiCallGetNewsList(String newsSourceId) {
        progressDialog.show();
        getNewsListUseCase.getNewsList(new GetNewsListSubscriber(), newsSourceId);
    }

    public void updateNewsSource(String newsSourceId) {
        getNewsListUseCase = new GetNewsListUseCase(context);
        doApiCallGetNewsList(newsSourceId);
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
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public NewsListAdapter getAdapter() {
        return adapter;
    }

    public RecyclerView getRecyclerView() {
        return binding.recyclerNewsList;
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

    public class GetNewsListSubscriber extends Subscriber<ArticlesCollection> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            progressDialog.dismiss();

            Snackbar snackbar =
                    Snackbar.make(binding.flNewsList, R.string.message_network_error, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(R.string.retry, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doApiCallGetNewsList(newsSourceId);
                }
            });
            snackbar.show();
        }

        @Override
        public void onNext(ArticlesCollection articlesCollection) {
            progressDialog.dismiss();
            adapter.setData(articlesCollection.getArticles());
        }
    }
}
