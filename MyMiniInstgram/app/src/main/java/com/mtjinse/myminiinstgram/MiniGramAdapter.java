package com.mtjinse.myminiinstgram;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.binaryfork.spanny.Spanny;
import com.bumptech.glide.Glide;
import com.mtjinse.myminiinstgram.models.MiniGram;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MiniGramAdapter extends RecyclerView.Adapter<MiniGramAdapter.MiniGramViewHolder>{
    Context context;
    ArrayList<MiniGram> miniGramArrayList = new ArrayList<>();

    public MiniGramAdapter(Context context, ArrayList<MiniGram> miniGramArrayList) {
        this.context = context;
        this.miniGramArrayList = miniGramArrayList;
    }

    @NonNull
    @Override
    public MiniGramViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) { //밑에 만들어놓은 뷰홀더를 사용하기위해 생성해주는 메소드
        //from()은 어디에 붙일거냐인데 여기서 viewGroup은 여기서는 리사이클러뷰를 의미한다
        View rootView =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.minigram_item, viewGroup, false);
        MiniGramViewHolder miniGramViewHolder = new MiniGramViewHolder(rootView);
        return miniGramViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MiniGramViewHolder miniGramViewHolder, int i) {
        MiniGram miniGram = miniGramArrayList.get(i);
        if(miniGram != null){
            //"url": "/uploads/4b9052e1960a4325811b02e7fdaf1667.png"
            String imgUrl = MainActivity.BASE_URL + miniGram.getImage().getUrl();
            Glide.with(context).load(imgUrl).thumbnail(0.1f).error(R.drawable.ic_heart).into(miniGramViewHolder.userImg);
            Glide.with(context).load(imgUrl).into(miniGramViewHolder.contentImg);
            miniGramViewHolder.userName.setText(miniGram.getUser());

            if(miniGram.getLike()){
                miniGramViewHolder.heartBtn.setChecked(true);
            }else {
                miniGramViewHolder.heartBtn.setChecked(false);
            }

            Spanny spanny = new Spanny(miniGram.getUser(), new StyleSpan(Typeface.BOLD))
                    .append("  " + miniGram.getContent());

            miniGramViewHolder.contentTxt.setText(spanny);

        }
    }

    @Override
    public int getItemCount() {
        if(miniGramArrayList == null){
            return 0;
        }else {
            return miniGramArrayList.size();
        }
    }

    public class MiniGramViewHolder extends RecyclerView.ViewHolder{
        CircleImageView userImg;
        TextView userName;
        Button moreBtn;
        ImageView contentImg;
        CheckBox heartBtn;
        CheckBox bookmarkBtn;
        Button messageBtn;
        Button chatBtn;
        TextView contentTxt;

        public MiniGramViewHolder(@NonNull View itemView) {
            super(itemView);
             userImg = itemView.findViewById(R.id.userImg);
            userName = itemView.findViewById(R.id.userName);
            contentImg = itemView.findViewById(R.id.contentImg);
            heartBtn = itemView.findViewById(R.id.heartBtn);
            bookmarkBtn = itemView.findViewById(R.id.bookmarkBtn);
            messageBtn = itemView.findViewById(R.id.messageBtn);
            chatBtn = itemView.findViewById(R.id.chatBtn);
            contentTxt = itemView.findViewById(R.id.contentTxt);
            moreBtn = itemView.findViewById(R.id.moreBtn);

            heartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MiniGram miniGram = miniGramArrayList.get(getAdapterPosition());
                    if(miniGram.getLike()){
                        miniGram.setLike(false);
                        HeartEvent heartEvent = new HeartEvent(miniGram.getId(), false);
                        BusProvider.getInstance().post(heartEvent);
                    }else{
                        miniGram.setLike(true);
                        HeartEvent heartEvent = new HeartEvent(miniGram.getId(), true);
                        BusProvider.getInstance().post(heartEvent);
                    }
                }
            });
        }
    }
}
