package hi.hugbo.verywowchat.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hi.hugbo.verywowchat.controllers.R;
import hi.hugbo.verywowchat.entities.ChatMessage;

public class ChatMessageAdapter extends RecyclerView.Adapter {

    private List<ChatMessage> mMessages = new ArrayList<>();

    public ChatMessageAdapter(List<ChatMessage> messages) {
        mMessages = messages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatbouble_send, parent, false);
        return new SentMessageHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ChatMessage message = mMessages.get(i);
        ((SentMessageHolder) viewHolder).name.setText(message.getMessage());
    }


    @Override
    public int getItemCount() {
        return mMessages.size();
    }


    private class SentMessageHolder extends RecyclerView.ViewHolder{

        TextView name;

        public SentMessageHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_message_body);
        }
    }
}
