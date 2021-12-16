package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView // Manipulate recyclerview
    private lateinit var userList: ArrayList<User> // Array list for the users
    private lateinit var tempUserList: ArrayList<User> // We will use this one to display query results
    private lateinit var mAdapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDBReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initialize Authorization
        mAuth = FirebaseAuth.getInstance()
        mDBReference = FirebaseDatabase.getInstance().getReference()

        userList = ArrayList()
        tempUserList = ArrayList()

        mAdapter = UserAdapter(this, userList)

        //Define RecyclerView
        userRecyclerView = findViewById(R.id.userRecyclerView)
        //Set Layout Manager
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        //Set Adapter
        userRecyclerView.adapter = mAdapter


        //*Read data stored from RecyclerView*

        //Get child node, and add an event listener (while implementing methods)
        mDBReference.child("user").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //Since method is called ONLY when data is changed; clear the screen and re-populate it
                userList.clear()
                for (postSnapshot in snapshot.children){

                    //Create USER OBJECT and add it to list since there are multiple users - type is User Object
                    val currentUser = postSnapshot.getValue(User::class.java)

                    //Make sure it doesn't show who's logged in
                    if(mAuth.currentUser?.uid != currentUser?.uID) {
                        userList.add(currentUser!!)
                    }

                }
                //Notify Adapter
                mAdapter.updateData(userList)

            }

            override fun onCancelled(error: DatabaseError) {
                // Unused
            }

        })

        tempUserList.addAll(userList)

    }

    //Inflate the options menu (Pass in XML as well as menu variable)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)


        //Search functionality
        val item = menu?.findItem(R.id.searchAction)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                println(newText)
                tempUserList.clear()
                val searchText = newText?.lowercase(Locale.getDefault())?.trim()
                if (searchText!!.isNotEmpty()) {
                    println(userList.toString())
                    userList.forEach {
                        //If user name
                        if (it.name?.lowercase(Locale.getDefault())!!.contains(searchText)) {
                            tempUserList.add(it)
                        }
                    }

                    //update adapter to CUSTOM adapter
                    userRecyclerView.adapter = mAdapter
                    mAdapter.userList = tempUserList

                    mAdapter.notifyDataSetChanged()

                } else {

                    //If text is empty, inflate entire list
                    tempUserList.clear()
                    tempUserList.addAll(userList)
                    userRecyclerView.adapter!!.notifyDataSetChanged()

                }
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //Check which item was clicked
        if (item.itemId == R.id.logout) {
            //write logic for logout
            mAuth.signOut() //FireBase method

            //Send them back to login screen
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            finish()
            startActivity(intent)

            return true

        } else if (item.itemId == R.id.groupChat){
            val intent1 = Intent(this@MainActivity, GroupChatActivity::class.java)
            startActivity(intent1)

            return true

        }

        /* else if (item.itemId == R.id.facultyList){
            val intent1 = Intent(this@MainActivity, FacultyActivity::class.java)
            startActivity(intent1)
            return true
        }
        */

        return true
    }
}