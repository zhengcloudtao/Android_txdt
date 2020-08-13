package com.example.android_txdt;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.util.List;

public class addAdapter extends RecyclerView.Adapter<addAdapter.ViewHolder> {
    // FruitAdapter.ViewHolder是我们定义的一个内部类继承自ViewHolder
    private List<add> AddList;//地址列表
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView add;//地址文本框
        TextView Name;//地址名文本框

        ViewHolder(View view) {
            super(view);
            Name = (TextView) view.findViewById(R.id.name);
            add = itemView.findViewById(R.id.add);//初始化
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_item,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        add a = AddList.get(position);     //获取列表具体项
        holder.add.setText(a.getAdd());     //设置文本框内容
        holder.Name.setText(a.getName());   //设置文本框内容
        ///////////////////////////////////点击/////////////////////////////////////////////
        if (position == getthisPosition()) {
            System.out.println("点击" + a.getlat() + "," + a.getlng());
            TencentMap tencentMap = mMapView.getMap();
            UiSettings mapUiSettings = tencentMap.getUiSettings();
            CameraUpdate cameraSigma =
                    CameraUpdateFactory.newCameraPosition(new CameraPosition(
                            new LatLng(a.getlat(), a.getlng()), //新的中心点坐标
                            17,  //新的缩放级别
                            0, //俯仰角 0~45° (垂直地图时为0)
                            0)); //偏航角 0~360° (正北方为0)

            tencentMap.moveCamera(cameraSigma);
        } else {

        }
        ////////////////////////////////////点击////////////////////////////////////////////

        /**
         * 这个是设置 单击/长按  的事件的地方
         */
        if (onRecyclerViewItemClickListener != null) {
            //单击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**
                     * 拿到上面暴露的接口  的点击方法  里面的值和点击事件的position  相互賦值  保持一致
                     *
                     */
                    onRecyclerViewItemClickListener.onClick(position);
                }
            });
            //长按事件
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onRecyclerViewItemClickListener.onLongClick(position);
                    return false;
                }
            });
            //
        }


    }


    private OnItemClickListener onRecyclerViewItemClickListener;//点击接口

    public interface OnItemClickListener {//接口方法
        void onClick(int position);

        void onLongClick(int position);
    }

    public void setOnRecyclerViewItemClickListener(OnItemClickListener onItemClickListener) {//设置点击监听
        this.onRecyclerViewItemClickListener = onItemClickListener;
    }


    //先声明一个int成员变量
    private int thisPosition = -1;

    //再定义一个int类型的返回值方法
    public int getthisPosition() {
        return thisPosition;
    }

    //其次定义一个方法用来绑定当前参数值的方法
    //此方法是在调用此适配器的地方调用的，此适配器内不会被调用到
    public void setThisPosition(int thisPosition) {
        this.thisPosition = thisPosition;
    }

    public class RecyclerViewNoBugLinearLayoutManager extends LinearLayoutManager {
        public RecyclerViewNoBugLinearLayoutManager(Context context) {
            super(context);
        }

        public RecyclerViewNoBugLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public RecyclerViewNoBugLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                //try catch一下
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    //计算列表大小
    public int getItemCount() {
        return AddList.size();
    }

    private MapView mMapView;
    //参数赋值
    public addAdapter(List<add> fruitList, MapView mapview) {
        AddList = fruitList;
        this.mMapView = mapview;
    }
}
