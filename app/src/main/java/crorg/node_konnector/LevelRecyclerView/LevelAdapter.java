package crorg.node_konnector.LevelRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import crorg.node_konnector.LevelRecyclerView.LevelFragment.OnListFragmentInteractionListener;
import crorg.node_konnector.R;
import crorg.node_konnector.dummy.LevelContent.LevelItem;

import java.io.Serializable;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link LevelItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.ViewHolder> implements Serializable {

    private final List<LevelItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private int level = 0;

    public LevelAdapter(List<LevelItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public static void setLevel(int level){

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_level, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);
        if(position <= level)
            holder.mlock.setVisibility(View.INVISIBLE);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mlock;
        public LevelItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mlock = (ImageView) view.findViewById(R.id.lock);
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
