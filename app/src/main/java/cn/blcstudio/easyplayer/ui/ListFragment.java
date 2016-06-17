package cn.blcstudio.easyplayer.ui;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.blcstudio.easyplayer.MusicServiceHolder;
import cn.blcstudio.easyplayer.R;
import cn.blcstudio.easyplayer.model.Music;
import cn.blcstudio.easyplayer.util.MusicHelper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ListView listView;
    private int curOrder;
    private List<Music> musicList;
    private PlayerActivity playerActivity;

    public List<Music> getMusicList() {
        return musicList;
    }

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
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
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        // get player activity
        playerActivity = (PlayerActivity) getActivity();

        // get music list from media store
        curOrder = MusicHelper.ORDER_BY_NAME;
        musicList = MusicHelper.getInstance().getMusicList(getActivity().getApplicationContext()
                , curOrder);
        // get list view
        listView = (ListView) view.findViewById(R.id.list_view);
        // put music list to list view
        final MusicAdapter musicAdapter = new MusicAdapter(getActivity().getApplicationContext(), musicList);
        listView.setAdapter(musicAdapter);
        // long click listener
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), musicList.get(position).getUrl(), Toast.LENGTH_SHORT).show();

                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicServiceHolder.getInstance().getMusicService().setupService(musicList);
                MusicServiceHolder.getInstance().getMusicService().jumpTo(position);
                playerActivity.updateInfoDisplay();
            }
        });

        // order button
        Button btnOrderByName = (Button) view.findViewById(R.id.order_name);
        Button btnOrderBySinger = (Button) view.findViewById(R.id.order_singer);
        Button btnOrderByAlbum = (Button) view.findViewById(R.id.order_album);
        btnOrderByName.setOnClickListener(new OrderButtonClickListener(MusicHelper.ORDER_BY_NAME));
        btnOrderBySinger.setOnClickListener(new OrderButtonClickListener(MusicHelper.ORDER_BY_SINGER));
        btnOrderByAlbum.setOnClickListener(new OrderButtonClickListener(MusicHelper.ORDER_BY_ALBUM));

        return view;
    }

    private class OrderButtonClickListener implements View.OnClickListener {

        private int order;

        public OrderButtonClickListener(int _order) {
            order = _order;
        }

        @Override
        public void onClick(View v) {
            if (curOrder != order) {
                curOrder = order;
                MusicAdapter musicAdapter = (MusicAdapter) listView.getAdapter();
                musicList = MusicHelper.getInstance().getMusicList(getActivity().getApplicationContext()
                        , order);
                musicAdapter.setMusicList(musicList);
                listView.setAdapter(musicAdapter);
            }
        }
    }

    private class MusicAdapter extends BaseAdapter {

        // music list
        private List<Music> musicList;
        // context
        private Context context;

        // constructor
        public MusicAdapter(Context _context, List<Music> _musicList) {
            context = _context;
            musicList = _musicList;
        }

        // change music list
        public void setMusicList(List<Music> newList) {
            musicList = newList;
        }

        @Override
        public int getCount() {
            return musicList.size();
        }

        @Override
        public Object getItem(int position) {
            return musicList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // inflate layout
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            }
            // get music item
            Music music = (Music) getItem(position);
            // album art
            /*ImageView albumArtView = (ImageView) convertView.findViewById(R.id.item_album_art);
            Bitmap albumArtBmp = MediaUtil.getArtwork(getActivity().getApplicationContext()
                    , music.getId(), music.getAlbumId());
            if (albumArtBmp != null) {
                albumArtView.setImageBitmap(albumArtBmp);
            }
            else {
                albumArtView.setImageResource(R.drawable.default_album);
            }*/

            // title
            TextView title = (TextView) convertView.findViewById(R.id.item_title);
            title.setText(music.getTitle());
            // singer
            TextView singer = (TextView) convertView.findViewById(R.id.item_singer);
            singer.setText(music.getSinger());
            // album
            TextView albumTitle = (TextView) convertView.findViewById(R.id.item_album_title);
            albumTitle.setText(music.getAlbum());

            return convertView;
        }
    }
    private String getAlbumArt(long album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[] { "album_art" };
        Cursor cur = getActivity().getApplicationContext().getContentResolver().query(
                Uri.parse(mUriAlbums + "/" + Long.toString(album_id)),
                projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        cur = null;
        return album_art;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
