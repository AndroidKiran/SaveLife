package com.donate.savelife.requirements.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.donate.savelife.R;
import com.donate.savelife.core.UniqueList;
import com.donate.savelife.core.requirement.displayer.NeedsDisplayer;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.requirement.model.Needs;

/**
 * Created by ravi on 04/09/16.
 */
public class NeedsAdapter extends RecyclerView.Adapter<NeedViewHolder>{

    private final LayoutInflater inflater;
    private Needs needs;
    private NeedItemView needItemView;
    private NeedsDisplayer.NeedInteractionListener needInteractionListener;

    public NeedsAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        needs = new Needs(new UniqueList<Need>());
        setHasStableIds(true);
    }

    public void setData(Needs needs){
        if (needs.size() > 0){
            this.needs = needs;
            notifyDataSetChanged();
            needInteractionListener.onContentLoaded();
        } else {
            needInteractionListener.onEmpty();
        }

    }

    public void setMoreData(Needs needs){
        if (needs.size() > 0) {
            this.needs.addAll(needs.getNeeds());
            notifyItemRangeInserted(getItemCount(), needs.size());
        }
    }


    @Override
    public NeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        needItemView = (NeedItemView) inflater.inflate(R.layout.need_item, parent, false);
        return new NeedViewHolder(needItemView);
    }

    @Override
    public void onBindViewHolder(NeedViewHolder holder, int position) {
        holder.bind(needs.getNeed(position), needSelectionListener);
    }

    @Override
    public int getItemCount() {
        if (needs == null){
            return 0;
        }
        return needs.size();
    }

    public Needs getNeeds(){
        return this.needs;
    }

    public Need getLastItem(){
        return this.needs.getNeed(getItemCount() - 1);
    }


    public void attach(NeedsDisplayer.NeedInteractionListener needInteractionListener) {
        this.needInteractionListener = needInteractionListener;
    }

    public void detach(NeedsDisplayer.NeedInteractionListener needInteractionListener) {
        this.needInteractionListener = null;
    }

    private  NeedViewHolder.NeedSelectionListener needSelectionListener = new NeedViewHolder.NeedSelectionListener() {
        @Override
        public void onNeedSelected(Need need) {
            needInteractionListener.onNeedSelected(need);
        }
    };

}
