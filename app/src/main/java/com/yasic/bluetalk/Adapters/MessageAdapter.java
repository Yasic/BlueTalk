package com.yasic.bluetalk.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yasic.bluetalk.Object.BlueTalkUser;
import com.yasic.bluetalk.Object.Message;
import com.yasic.bluetalk.R;

import java.util.InputMismatchException;
import java.util.List;

/**
 * Created by ESIR on 2016/2/29.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    /**
     * 建立实例的activity的context
     */
    private Context context;

    /**
     * 点击的监听器的对象
     */
    private OnItemClickListener onItemClickListener;

    /**
     * 消息列表
     */
    private List<Message> messageList;

    /**
     * 本地用户帐号
     */
    private String localUsrAccount;

    //建立枚举 2个item 类型
    public enum ITEM_TYPE {
        LEFT,
        RIGHT
    }

    public MessageAdapter(Context context, List<Message> messageList, String localUsrAccount) {
        this.context = context;
        this.messageList = messageList;
        this.localUsrAccount = localUsrAccount;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.LEFT.ordinal()){
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_messageleft,parent,false));
        }
        else {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_messageright, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        /*if (onItemClickListener != null) {
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
        }*/
        holder.tvNickName.setText(messageList.get(position).getPosterId());
        holder.tvMessage.setText(messageList.get(position).getMessageData());
        holder.tvSendMessageTime.setText(messageList.get(position).getSendTime());
    }

    @Override
    public int getItemViewType(int position) {
        //Enum类提供了一个ordinal()方法，返回枚举类型的序数，这里ITEM_TYPE.ITEM1.ordinal()代表0， ITEM_TYPE.ITEM2.ordinal()代表1
        //return position % 2 == 0 ? ITEM_TYPE.LEFT.ordinal() : ITEM_TYPE.LEFT.ordinal();
        return messageList.get(position).getPosterAccount().equals(localUsrAccount)?ITEM_TYPE.RIGHT.ordinal() : ITEM_TYPE.LEFT.ordinal();
    }

    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNickName;
        private TextView tvMessage;
        private TextView tvSendMessageTime;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvNickName = (TextView)itemView.findViewById(R.id.tv_nickname);
            tvMessage = (TextView)itemView.findViewById(R.id.tv_message);
            tvSendMessageTime = (TextView)itemView.findViewById(R.id.tv_sendmessagetime);
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
     * @param messageList 变动之后的list
     */
    public void refresh(List<Message> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }
}
