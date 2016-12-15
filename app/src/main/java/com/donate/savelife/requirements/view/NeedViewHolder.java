package com.donate.savelife.requirements.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.donate.savelife.core.requirement.model.Need;


/**
 * Created by ravi on 04/09/16.
 */
public class NeedViewHolder extends RecyclerView.ViewHolder {

    private final NeedItemView needItemView;

    public NeedViewHolder(NeedItemView itemView) {
        super(itemView);
        this.needItemView = itemView;
    }

    public void bind(final Need need, final NeedSelectionListener needSelectionListener) {
        needItemView.display(need);
        needItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                needSelectionListener.onNeedSelected(need);
            }
        });
    }

    public interface NeedSelectionListener {
        void onNeedSelected(Need need);
    }

}
