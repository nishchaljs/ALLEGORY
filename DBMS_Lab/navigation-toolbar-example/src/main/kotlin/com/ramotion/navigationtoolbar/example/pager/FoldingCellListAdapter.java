package com.ramotion.navigationtoolbar.example.pager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ramotion.foldingcell.FoldingCell;
import com.ramotion.navigationtoolbar.example.Model.publicationModel;
import com.ramotion.navigationtoolbar.example.R;

import java.util.HashSet;
import java.util.List;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class FoldingCellListAdapter extends ArrayAdapter<publicationModel> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    private Context c;

    public FoldingCellListAdapter(Context context, List<publicationModel> objects) {
        super(context, 0, objects);
        c=context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        publicationModel item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);

            TextView Update =  cell.findViewById(R.id.content_update_btn);
            TextView delete = cell.findViewById(R.id.content_delete_btn);
            TextView blank1 = cell.findViewById(R.id.blank1);
            TextView blank2 = cell.findViewById(R.id.blank2);
            TextView read = cell.findViewById(R.id.content_read_btn);

            Update.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            blank1.setVisibility(View.VISIBLE);
            blank2.setVisibility(View.VISIBLE);
            read.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://static1.squarespace.com/" +
                            "static/51f912e6e4b0cc5aa449f476/t/58502a43b8a79bf9d5651cdd/1481648710707/Poetry+for+Perseverance.pdf")));
                }
            });

            // binding view parts to view holder
            viewHolder.title = cell.findViewById(R.id.titleTextView);
            viewHolder.name = cell.findViewById(R.id.name);
            viewHolder.auth = cell.findViewById(R.id.content_name_view);
            viewHolder.date = cell.findViewById(R.id.content_delivery_date);
            viewHolder.time = cell.findViewById(R.id.content_delivery_time);

            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == item)
            return cell;

        // bind data from selected element to view through view holder
        viewHolder.title.setText(item.getName());
        viewHolder.name.setText(item.getName());
        viewHolder.date.setText(item.getDate());
        viewHolder.time.setText(item.getTime());
        viewHolder.auth.setText(item.getAuthor());

        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView name;
        TextView auth;
        TextView date;
        TextView time;
    }
}
