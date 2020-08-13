package com.example.android_txdt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.ViewHolder> {
    // FruitAdapter.ViewHolder是我们定义的一个内部类继承自ViewHolder
    private List<search_list> AddList;
    private Button qx;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView1;
    private ImageButton location;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView add;
        TextView Name;
        //private final TextView titel;
        ViewHolder(View view) {
            super(view);
            //  fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            Name = (TextView) view.findViewById(R.id.name);
            add= itemView.findViewById(R.id.add);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.serch_item,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        search_list a = AddList.get(position);
        holder.add.setText(a.getAdd());
        holder.Name.setText(a.getName());




        ///////////////////////////////////點擊變色/////////////////////////////////////////////
        if (position == getthisPosition()) {
            System.out.println("点击"+a.getlat());
            TencentMap tencentMap = mMapView.getMap();
            UiSettings mapUiSettings = tencentMap.getUiSettings();
            CameraUpdate cameraSigma =
                    CameraUpdateFactory.newCameraPosition(new CameraPosition(
                            new LatLng(a.getlat(), a.getlng()), //新的中心点坐标
                            17,  //新的缩放级别
                            0, //俯仰角 0~45° (垂直地图时为0)
                            0)); //偏航角 0~360° (正北方为0)

            tencentMap.moveCamera(cameraSigma);
            recyclerView1.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            mMapView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            location.setVisibility(View.VISIBLE);
            qx.setVisibility(View.GONE);
            //点击软键盘外部，收起软键盘




        } else {

        }
        ////////////////////////////////////點擊變色////////////////////////////////////////////

        /**
         * 這裏是設置  點擊/長按  的事件的地方
         */
        if (onRecyclerViewItemClickListener != null) {
            //點擊事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**
                     * 拿到上面暴露的接口  的點擊方法  裏面的值和點擊事件的position  相互賦值  保持一致
                     * 算了，越説越亂，自己去理解吧
                     */
                    onRecyclerViewItemClickListener.onClick(position);
                }
            });
            //長按事件
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


    private OnItemClickListener onRecyclerViewItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);

        void onLongClick(int position);
    }
    public void setOnRecyclerViewItemClickListener(OnItemClickListener onItemClickListener) {
        this.onRecyclerViewItemClickListener = onItemClickListener;
    }




    //先声明一个int成员变量
    private int thisPosition=-1;
    //再定义一个int类型的返回值方法
    public int getthisPosition() {
        return thisPosition;
    }
    //其次定义一个方法用来绑定当前参数值的方法
    //此方法是在调用此适配器的地方调用的，此适配器内不会被调用到
    public void setThisPosition(int thisPosition) {
        this.thisPosition = thisPosition;
    }




    @Override
    public int getItemCount() {
        return AddList.size();
    }

    private MapView mMapView;
    private EditText search;
    public searchAdapter(List<search_list> fruitList, MapView mapview, RecyclerView recyclerView, RecyclerView recyclerView1, Button qx, ImageButton location,EditText search){
        AddList = fruitList;   this.mMapView=mapview;this.recyclerView=recyclerView;this.recyclerView1=recyclerView1;this.qx=qx;this.location=location;this.search=search;
    }

}
