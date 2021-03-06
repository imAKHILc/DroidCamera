package studio.themad.droidcamera;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import studio.themad.droidcamera.Model.Photo;
import studio.themad.droidcamera.ViewHolder.PhotoViewHolder;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class GalleryActivity extends Activity {

    Activity context;
    DatabaseReference mRef;
    RecyclerView imageRecycler;
    FirebaseRecyclerAdapter mAdapter;
    public static RelativeLayout settings_panel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        context = this;

        settings_panel = (RelativeLayout) findViewById(R.id.settings_panel);
        imageRecycler = (RecyclerView) findViewById(R.id.imageRecycler);
        imageRecycler.setNestedScrollingEnabled(false);
        imageRecycler.setHasFixedSize(true);

        //recycler adapter
        mRef = FirebaseDatabase.getInstance().getReference().child("Photos");

        mAdapter = new FirebaseRecyclerAdapter<Photo, PhotoViewHolder>(Photo.class, R.layout.item_gallery, PhotoViewHolder.class, mRef) {
            @Override
            public void populateViewHolder(PhotoViewHolder viewHolder, Photo photo, int position) {
                viewHolder.setImage(photo.getLocation(), photo.getCloud());
                viewHolder.setId(mAdapter.getRef(position).getKey().toString());
                viewHolder.getItemId();
            }
        };
        imageRecycler.setAdapter(mAdapter);

        //reload recycler view
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mAdapter.notifyDataSetChanged();
                imageRecycler.setVisibility(View.VISIBLE);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        GridLayoutManager mManager;
        mManager = new GridLayoutManager(this, 3);
        //mManager.setReverseLayout(true);
        imageRecycler.setLayoutManager(mManager);
    }
}
