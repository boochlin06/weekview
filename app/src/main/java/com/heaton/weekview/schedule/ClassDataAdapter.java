package com.heaton.weekview.schedule;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.heaton.weekview.constants.FormatConstants;
import com.heaton.weekview.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ClassDataAdapter extends RecyclerView.Adapter<ClassDataAdapter.TimeHolder> {

    private List<ClassData> classDataList;
    private DateFormat dateFormat;

    public ClassDataAdapter(List<ClassData> classDataList) {
        this.classDataList = classDataList;
        this.dateFormat = new SimpleDateFormat(FormatConstants.TIME_STAMP_LIST_ITEM_FORMAT);
    }

    public List<ClassData> getClassDataList() {
        return classDataList;
    }

    public void setClassDataList(List<ClassData> classDataList) {
        this.classDataList = classDataList;
    }

    @NonNull
    @Override
    public TimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_time, parent, false);
        TimeHolder holder = new TimeHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeHolder holder, int position) {
        holder.txtTime.setText(dateFormat.format(classDataList.get(position).getStartTime()));
        holder.txtTime.setTextColor(classDataList.get(position).isBooked()
                ? Color.GRAY : holder.itemView.getResources().getColor(R.color.colorAccent));
        holder.txtTime.setTypeface(holder.txtTime.getTypeface(), classDataList.get(position).isBooked()
                ? Typeface.NORMAL : Typeface.BOLD);
    }

    @Override
    public int getItemCount() {
        return classDataList.size();
    }

    public class TimeHolder extends RecyclerView.ViewHolder {
        TextView txtTime;

        public TimeHolder(@NonNull View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }
}
