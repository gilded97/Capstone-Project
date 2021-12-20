package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

//Extends RecyclerView - *Make sure to Pass in RecyclerView.ViewHolder and implement members

//Once you implement members be sure to come back and pass in values to Message Adapter class's constructor
class MessageAdapter(val context: Context, val messageList: ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    //These variables used to determine which view we're going to use.
    //We will override getItemViewType which will return INT depending on view type
    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1) {
            //inflate receive
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
            return ReceivedViewHolder(view)
        } else {
            //inflate sent
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        //Get Current Message (outside of if statement so it can be used for both scenarios)
        val currentMessage = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java) {
            //do stuff for sentViewHolder

            //Create object as instance of class
            val viewHolder = holder as SentViewHolder

            holder.sentMessage.text = currentMessage.message

        } else {
            //do stuff for ReceivedViewHolder

            //Create object as instance of class?
            val viewHolder = holder as ReceivedViewHolder

            holder.receivedMessage.text = currentMessage.message

        }

    }


    //will return INT depending on view type
    override fun getItemViewType(position: Int): Int {
        //return super.getItemViewType(position)

        val currentMessage = messageList[position] // Message list comes from the message Adapter

        //If ID of sender matches in FireBase
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderID)) {
            return ITEM_SENT
        } else {
            return ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }


    //We need TWO ViewHolders!!! One for sent & Received
    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent_message)
    }

    class ReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val receivedMessage = itemView.findViewById<TextView>(R.id.txt_receive_message)
    }



}