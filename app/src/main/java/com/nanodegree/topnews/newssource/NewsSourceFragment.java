package com.nanodegree.topnews.newssource;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nanodegree.topnews.R;
import com.nanodegree.topnews.databinding.FragmentNewsSourceBinding;
import com.nanodegree.topnews.interactor.GetNewsSourceUseCase;
import com.nanodegree.topnews.model.NewsSource;
import com.nanodegree.topnews.model.NewsSourcesCollection;

import java.util.List;

import rx.Subscriber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsSourceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsSourceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsSourceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentNewsSourceBinding binding;
    private OnFragmentInteractionListener mListener;
    private Context context;
    private LinearLayoutManager layoutManager;
    private NewsSourceAdapter adapter;
    private List<NewsSource> listNewsSource;
    private GetNewsSourceUseCase getNewsSourceUseCase;

    public NewsSourceFragment() {
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
    public static NewsSourceFragment newInstance(String param1, String param2) {
        NewsSourceFragment fragment = new NewsSourceFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_news_source, container, false);

        layoutManager = new LinearLayoutManager(context);
        adapter = new NewsSourceAdapter((NewsSourceActivity) context, context, listNewsSource);

        binding.recyclerSourceList.setLayoutManager(layoutManager);
        binding.recyclerSourceList.setAdapter(adapter);

        getNewsSourceUseCase = new GetNewsSourceUseCase(context);
        doApiCallGetNewsList();

        return binding.getRoot();
    }

    private void doApiCallGetNewsList() {
        getNewsSourceUseCase.getSources(new GetNewsSourceSubscriber());
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

    public NewsSourceAdapter getAdapter() {
        return adapter;
    }

    public RecyclerView getRecyclerView() {
        return binding.recyclerSourceList;
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

    public class GetNewsSourceSubscriber extends Subscriber<NewsSourcesCollection> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(NewsSourcesCollection newsSourcesCollection) {
            adapter.setData(newsSourcesCollection.getSources());
        }
    }
}
