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


class FacultyActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView // Manipulate recyclerview
    private lateinit var facList: ArrayList<User> // Array list for the users
    private lateinit var tempFacList: ArrayList<User> // We will use this one to display query results
    private lateinit var mAdapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDBReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        mDBReference = FirebaseDatabase.getInstance().getReference()

        facList = ArrayList()
        tempFacList = ArrayList()
        mAdapter = UserAdapter(this, facList)

        userRecyclerView = findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = mAdapter

        //*Read data stored from RecyclerView*

        //Get child node, and add an event listener (while implementing methods)
        mDBReference.child("user").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //Since method is called ONLY when data is changed; clear the screen and re-populate it
                facList.clear()
                for (postSnapshot in snapshot.children){

                    //Create USER OBJECT and add it to list since there are multiple users - type is User Object
                    val currentUser = postSnapshot.getValue(User::class.java)

                    //Make sure it doesn't show who's logged in or only faculty
                    if((mAuth.currentUser?.uid != currentUser?.uID) && (currentUser!!.isFaculty == true)) {
                        facList.add(currentUser!!)
                    }

                }
                //Notify Adapter
                mAdapter.updateData(facList)

            }

            override fun onCancelled(error: DatabaseError) {
                // Unused
            }

        })

        tempFacList.addAll(facList)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        //Search functionality
        val item = menu?.findItem(R.id.searchAction)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // TODO: "Not yet implemented"
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                println(newText)
                tempFacList.clear()
                val searchText = newText?.lowercase(Locale.getDefault())?.trim()
                if (searchText!!.isNotEmpty()) {
                    println(facList.toString())
                    facList.forEach {
                        //If user name or department, and is faculty (will uncomment once departments are required)
                        if (it.name?.lowercase(Locale.getDefault())!!.contains(searchText) /*|| it.department?.lowercase(Locale.getDefault())!!.contains(searchText))*/ && (it.isFaculty == true)){
                            tempFacList.add(it)
                        }
                    }

                    //update adapter to CUSTOM adapter
                    userRecyclerView.adapter = mAdapter
                    mAdapter.userList = tempFacList

                    mAdapter.notifyDataSetChanged()

                } else {

                    //If text is empty, inflate entire list
                    tempFacList.clear()
                    tempFacList.addAll(facList)
                    userRecyclerView.adapter!!.notifyDataSetChanged()

                }
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.logout){
            //wrote the logic for logout
            mAuth.signOut()
            val intent = Intent(this@FacultyActivity,SignInActivity::class.java)
            finish()
            startActivity(intent)
            return true
        } else if(item.itemId == R.id.groupChat){
            val intent1 = Intent(this@FacultyActivity,GroupChatActivity::class.java)
            startActivity(intent1)
            return true
        } else if(item.itemId == R.id.studentList) {
            val intent1 = Intent(this@FacultyActivity, MainActivity::class.java)
            startActivity(intent1)
            return true
        } else
            return true
    }
}