package com.yasic.bluetalk.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yasic.bluetalk.Object.BlueTalkUser;
import com.yasic.bluetalk.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by ESIR on 2016/2/29.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    /**
     * 建立实例的activity的context
     */
    private Context context;

    /**
     * 点击的监听器的对象
     */
    private OnItemClickListener onItemClickListener;

    /**
     * 用户列表
     */
    private List<BlueTalkUser> blueTalkUserList;

    public ChatListAdapter(Context context, List<BlueTalkUser> blueTalkUserList) {
        this.context = context;
        this.blueTalkUserList = blueTalkUserList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chatlist, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (onItemClickListener != null) {
            holder.cvSearchResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.cvSearchResult, position);
                }
            });

            holder.cvSearchResult.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongCick(holder.cvSearchResult, position);
                    return false;
                }
            });
        }
        holder.tvNickName.setText(blueTalkUserList.get(position).getNickName());
        holder.tvLastMessage.setText(blueTalkUserList.get(position).getLastMessage());
    }

    @Override
    public int getItemCount() {
        return blueTalkUserList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        /**
         * 包裹的cardview
         */
        private CardView cvSearchResult;

        /**
         * 显示昵称的textview
         */
        private TextView tvNickName;

        /**
         * 显示最新消息的textview
         */
        private TextView tvLastMessage;

        public MyViewHolder(View itemView) {
            super(itemView);
            cvSearchResult = (CardView) itemView.findViewById(R.id.cv_chatitem);
            tvNickName = (TextView) itemView.findViewById(R.id.tv_nickname);
            tvLastMessage = (TextView) itemView.findViewById(R.id.tv_lastmessage);
        }
    }

    /**
     * 点击事件的接口
     */
    public interface OnItemClickListener {

        /**
         * 短点击
         *
         * @param v        被点击的对象
         * @param position 被点击的view的位置
         */
        void onItemClick(View v, int position);

        /**
         * 长按
         *
         * @param v        被点击的对象
         * @param position 被点击的view的位置
         */
        void onItemLongCick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 提示数据有了变动，刷新数据的方法
     *
     * @param blueTalkUserList 变动之后的list
     */
    public void refresh(List<BlueTalkUser> blueTalkUserList) {
        this.blueTalkUserList = blueTalkUserList;
        notifyDataSetChanged();
    }
}
