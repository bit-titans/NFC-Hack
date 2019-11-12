package com.example.nfc_hack


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_scan.*

/**
 * A simple [Fragment] subclass.
 */
class ScanFragment : Fragment() {
    companion object{private const val RC_SIGN_IN = 123}
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var subs = arrayOfNulls<String>(8)
    private var subs_name = arrayOfNulls<String>(8)
    private var subs_date = arrayOfNulls<String>(8)
    private var subs_time = arrayOfNulls<String>(8)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var id : String
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if(currentUser == null){
            // Inflate the layout for this fragment
            // Choose authentication providers
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().setAllowNewAccounts(false).build()
            )

            // Create and launch sign-in intent
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN
            )
        }
        else{
            id = Input.id
            database = FirebaseDatabase.getInstance().reference.child("data").child(id)
            database.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var name =  p0.child("name").value
                    var usn = p0.child("usn").value
                    var storage = FirebaseStorage.getInstance()
                    if(id!="null") {
                        val storageReference =
                            FirebaseStorage.getInstance().reference.child(usn as String + ".jpg")
                        val imageView = view.findViewById<ImageView>(R.id.imageView)
                        if (id != "none") {
                            GlideApp.with(view /* context */)
                                .load(storageReference)
                                .into(imageView)
                            textview.setText("Name:$name \nUSN:$usn")

                            var i = 0
                            for (sub in p0.child("Subjects").children)
                            {
                                subs[i] = sub.child("code").value as String
                                subs_name[i] = sub.child("name").value as String
                                subs_date[i] = sub.child("date").value as String
                                subs_time[i++] = sub.child("time").value as String
                            }
                            subjectsApplied.visibility = View.VISIBLE
                            Sub1.visibility = View.VISIBLE
                            Sub2.visibility = View.VISIBLE
                            Sub3.visibility = View.VISIBLE
                            Sub4.visibility = View.VISIBLE
                            Sub5.visibility = View.VISIBLE
                            Sub6.visibility = View.VISIBLE
                            Sub7.visibility = View.VISIBLE
                            Sub8.visibility = View.VISIBLE
                            Sub1.setText(subs[0])
                            for(sub in subs) {
                                println(sub)
                            }
                            Sub1.setText(subs[0])
                            Sub2.setText(subs[1])
                            Sub3.setText(subs[2])
                            Sub4.setText(subs[3])
                            Sub5.setText(subs[4])
                            Sub6.setText(subs[5])
                            Sub7.setText(subs[6])
                            Sub8.setText(subs[7])
                            PassSub.subcode = subs
                            PassSub.subdate = subs_date
                            PassSub.subname = subs_name
                            PassSub.subtime = subs_time
                        }
                    }


                }
            })
        }

    }


}
