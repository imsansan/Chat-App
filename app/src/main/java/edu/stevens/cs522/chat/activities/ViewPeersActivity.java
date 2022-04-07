package edu.stevens.cs522.chat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.stevens.cs522.chat.R;
import edu.stevens.cs522.chat.entities.Peer;
import edu.stevens.cs522.chat.ui.TextAdapter;
import edu.stevens.cs522.chat.viewmodels.PeersViewModel;


public class ViewPeersActivity extends FragmentActivity implements TextAdapter.OnItemClickListener<Peer> {

    /*
     *  See ChatServer for example of what to do, query peers database instead of messages database.
     */

    private TextAdapter<Peer> peerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peers);

        // Initialize the recyclerview and adapter for peers
        RecyclerView peersList = findViewById(R.id.peer_list);
        peersList.setLayoutManager(new LinearLayoutManager(this));

        peerAdapter = new TextAdapter<>(peersList, this);
        peersList.setAdapter(peerAdapter);

        // create the view model and query for a list of all peers
        PeersViewModel peersViewModel = new PeersViewModel(getApplication());
        LiveData<List<Peer>> peers = peersViewModel.fetchAllPeers();

        // observer for list of peers updates the peer adapter
        peers.observe(this,
                ms -> {
                    if( ms!= null){
                        peerAdapter.setDataset(ms);
                        peerAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /*
     * Callback interface defined in TextAdapter, for responding to clicks on rows.
     */
    @Override
    public void onItemClick(RecyclerView parent, View view, int position, Peer peer) {
        /*
         * Clicking on a peer brings up details
         */
        Intent intent = new Intent(this, ViewPeerActivity.class);
        intent.putExtra(ViewPeerActivity.PEER_KEY, peer);
        startActivity(intent);
    }

}
