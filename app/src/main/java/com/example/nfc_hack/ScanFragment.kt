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
    private var sub_name = arrayOfNulls<String>(8)
    private var sub_date = arrayOfNulls<String>(8)
    private var sub_time = arrayOfNulls<String>(8)
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
                    if(name!=null) {
                        val storageReference =
                            FirebaseStorage.getInstance().reference.child(usn as String + ".jpeg")
                        val imageView = view.findViewById<ImageView>(R.id.imageView)
                        if (id != "none") {
                            GlideApp.with(view /* context */)
                                .load(storageReference)
                                .into(imageView)
                            textview.setText("Name:$name \nUSN:$usn")
                        }
                    }


                }
            })
        }

    }


}
