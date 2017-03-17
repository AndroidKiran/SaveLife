package com.donate.savelife.requirements.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.donate.savelife.R;
import com.donate.savelife.SortedList;
import com.donate.savelife.SortedNeedsList;
import com.donate.savelife.core.requirement.displayer.NeedsDisplayer;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.user.data.model.User;

import java.util.ListIterator;

/**
 * Created by ravi on 04/09/16.
 */
public class NeedsAdapter extends RecyclerView.Adapter<NeedViewHolder> {

    private final LayoutInflater inflater;
    private final SortedList<Need> needs;
    private NeedItemView needItemView;
    private NeedsDisplayer.NeedInteractionListener needInteractionListener;
    private User owner;

    public NeedsAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        needs = new SortedNeedsList();
        setHasStableIds(true);
    }

    public void setData(Need need, User owner) {
        if (this.needs.contains(need)) {
            remove(need);
        }

        this.needs.add(need);
        this.owner = owner;
        notifyDataSetChanged();

        if (this.needs.size() > 0) {
            needInteractionListener.onContentLoaded();
        } else {
            needInteractionListener.onEmpty();
        }
    }

    private void remove(Need need) {
        ListIterator<Need> needListIterator = needs.listIterator();
        while (needListIterator.hasNext()) {
            Need need1 = needListIterator.next();
            if (need1.getId().equals(need.getId())) {
                needListIterator.remove();
                return;
            }
        }
    }


    @Override
    public NeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        needItemView = (NeedItemView) inflater.inflate(R.layout.need_item, parent, false);
        return new NeedViewHolder(needItemView);
    }

    @Override
    public void onBindViewHolder(NeedViewHolder holder, int position) {
        holder.bind(needs.get(position), owner, needSelectionListener);
    }

    @Override
    public int getItemCount() {
        if (needs == null) {
            return 0;
        }
        return needs.size();
    }


    public void attach(NeedsDisplayer.NeedInteractionListener needInteractionListener) {
        this.needInteractionListener = needInteractionListener;
    }

    public void detach(NeedsDisplayer.NeedInteractionListener needInteractionListener) {
        this.needInteractionListener = null;
    }

    private NeedViewHolder.NeedSelectionListener needSelectionListener = new NeedViewHolder.NeedSelectionListener() {
        @Override
        public void onNeedSelected(Need need) {
            needInteractionListener.onNeedSelected(need);
        }
    };

}
