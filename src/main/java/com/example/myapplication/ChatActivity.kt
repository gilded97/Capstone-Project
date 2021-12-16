package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    //Declare
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDBReference: DatabaseReference

    //Used to create a unique "room" for sender/reciever to make sure messages are private
    //Used var because these may change
    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //Retrieve intent data (from user adapter? Has to be since name pops up on support bar before messages are even passed / started)

        val name = intent.getStringExtra("name")

        //Updating using sender/receiver "rooms"
        val receiverUid = intent.getStringExtra("uid") // instead of val uid = intent.getStringExtra("uid")

        //UID of current logged in user
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDBReference = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        //Set title to name of person you're talking to
        supportActionBar?.title = name

        //Initialize
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sendButton)

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        //Set LayoutManager and adapter
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        //logic for adding data to RecyclerView
        mDBReference.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()//List must be cleared everytime this is called so values don't repeat

                    //Use snapshot to get values and show it
                    for (postSnapshot in snapshot.children) { //Traverse children of databaseSnapshot\

                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!) // Add it to the list and make sure it's null safe

                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


        //add message to database
        sendButton.setOnClickListener {

            val message = messageBox.text.toString()
            val messageObject = Message(message, senderUid)

            //Get DB reference, under chats path, under senderRoom, under messages
            //with -push... See's if this is successful and sends the message to both the sender and receiver room
            mDBReference.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDBReference.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            //After message is sent, Clear the textbox
            messageBox.setText("")
        }
    }
}