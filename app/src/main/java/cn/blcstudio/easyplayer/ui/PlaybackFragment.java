package cn.blcstudio.easyplayer.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cn.blcstudio.easyplayer.MusicService;
import cn.blcstudio.easyplayer.MusicServiceHolder;
import cn.blcstudio.easyplayer.R;
import cn.blcstudio.easyplayer.model.Music;
import cn.blcstudio.easyplayer.playback.queue.QueueHolder;
import cn.blcstudio.easyplayer.util.MediaUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlaybackFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlaybackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaybackFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ImageView avatar;
    private TextView title;
    private TextView singer;
    private ImageButton btnNext, btnQueue;

    private PlayerActivity playerActivity;

    private OnFragmentInteractionListener mListener;

    public PlaybackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaybackFragment.
     */
    public static PlaybackFragment newInstance(String param1, String param2) {
        PlaybackFragment fragment = new PlaybackFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playback, container, false);

        // set animation to fullscreen
        avatar = (ImageView) view.findViewById(R.id.album_art);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FullscreenActivity.class);
                String transitionName = "transition_album_cover";
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                                avatar,          // The view which starts the transition
                                transitionName      // The transitionName of the view we’re transitioning to
                        );
                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
            }
        });

        // get music service
        playerActivity = (PlayerActivity) getActivity();

        // button
        btnNext = (ImageButton) view.findViewById(R.id.play_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicServiceHolder.getInstance().getMusicService().next();
                playerActivity.updateInfoDisplay();
            }
        });
        btnQueue = (ImageButton) view.findViewById(R.id.play_queue);
        btnQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicService musicService = MusicServiceHolder.getInstance().getMusicService();
                switch (musicService.getCurQueueType()) {
                    case QueueHolder.LIST_PLAY:
                        musicService.setQueueType(QueueHolder.LIST_REPEAT);
                        break;
                    case QueueHolder.LIST_REPEAT:
                        musicService.setQueueType(QueueHolder.SINGLE_REPEAT);
                        break;
                    case QueueHolder.SINGLE_REPEAT:
                        musicService.setQueueType(QueueHolder.LIST_PLAY);
                        break;
                }
                playerActivity.updateInfoDisplay();
            }
        });

        title = (TextView) view.findViewById(R.id.title);
        singer = (TextView) view.findViewById(R.id.artist);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    // update music information display
    public void updateInfoDisplay() {
        MusicService musicService = MusicServiceHolder.getInstance().getMusicService();
        Music curMusic = musicService.getCurMusic();
        title.setText(curMusic.getTitle());
        singer.setText(curMusic.getSinger());
        Bitmap avatarBmp = MediaUtil.getArtwork(getActivity().getApplicationContext()
                , curMusic.getId(), curMusic.getAlbumId());
        if (avatarBmp != null) {
            avatar.setImageBitmap(avatarBmp);
        } else {
            avatar.setImageResource(R.drawable.default_album);
        }
        switch (musicService.getCurQueueType()) {
            case QueueHolder.LIST_PLAY:
                btnQueue.setImageResource(R.drawable.list_play);
                break;
            case QueueHolder.LIST_REPEAT:
                btnQueue.setImageResource(R.drawable.list_repeat);
                break;
            case QueueHolder.SINGLE_REPEAT:
                btnQueue.setImageResource(R.drawable.single_repeat);
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        void onFragmentInteraction(Uri uri);
    }
}
