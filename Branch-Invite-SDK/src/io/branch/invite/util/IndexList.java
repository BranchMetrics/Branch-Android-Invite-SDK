package io.branch.invite.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Class fo adding index list to the contact list. This adds an alphabet index on the right side of the contact list.
 */
public class IndexList extends ListView {
    final ArrayList<String> indexSessions_;
    final Context context_;
    int parentHeight_ = 0;
    int itemHeight_ = 0;
    /* Minimum height for the index */
    int itemMinimumHeight = 50;
    int indexCollapseIndex = 1;
    int totalDisplayItems_ = 0;
    final IndexAdapter indexAdapter_;
    ListView contentListView_;
    String lastSelectedContact_ = " ";

    public IndexList(Context context, ArrayList<String> sessionList, ListView contentListView) {
        super(context);
        context_ = context;
        indexSessions_ = sessionList;
        indexSessions_.remove(" ");
        this.setBackgroundColor(Color.GRAY);
        indexAdapter_ = new IndexAdapter();
        setAdapter(indexAdapter_);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        contentListView_ = contentListView;
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        if (height > 0) {
            itemHeight_ = (height / indexSessions_.size());
            if (itemHeight_ < itemMinimumHeight) {
                itemHeight_ = itemMinimumHeight;
                int totalDisplayableItems = height / itemHeight_;
                indexCollapseIndex = (indexSessions_.size() / totalDisplayableItems) + 1;
            }
        }
        totalDisplayItems_ = indexSessions_.size() / indexCollapseIndex;
        itemHeight_ = height / (totalDisplayItems_ + 1);
        indexAdapter_.notifyDataSetChanged();
    }

    private class IndexAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return totalDisplayItems_;
        }

        @Override
        public String getItem(int position) {
            return indexSessions_.get(position * indexCollapseIndex);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = new TextView(context_);
                ((TextView) view).setGravity(Gravity.CENTER);
                ((TextView) view).setTypeface(Typeface.DEFAULT_BOLD);
                ((TextView) view).setHeight(itemHeight_);
                ((TextView) view).setTextColor(Color.WHITE);
            }
            String idx = getItem(position);
            ((TextView) view).setText(idx);
            view.setTag(position);
            if (lastSelectedContact_.startsWith(idx)) {
                view.setBackgroundColor(Color.BLACK);
            } else {
                view.setBackgroundColor(Color.GRAY);
            }
            return view;
        }


    }


}
