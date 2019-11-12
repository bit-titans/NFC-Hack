package com.example.nfc_hack


import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_subject.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class SubjectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subject, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var subcode = arguments?.getString("code")
        var subname = arguments?.getString("name")
        var subdate = arguments?.getString("date")
        var subtime = arguments?.getString("time")
        subCode.setText(subcode)
        subName.setText(subname)
        subDate.setText(subdate)
        subTime.setText(subtime?.trim())
        var calendar: Calendar = Calendar.getInstance()
        var paths = subdate!!.split("-")
        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(paths[0]))
        calendar.set(Calendar.MONTH,Integer.parseInt(paths[1])-1)
        calendar.set(Calendar.YEAR,Integer.parseInt(paths[2]))
        var timePaths = subtime!!.split(":")
        var hh=Integer.parseInt(timePaths[0])
        var mm=Integer.parseInt(timePaths[1].split(" ")[0])
        if(timePaths[1].split(" ")[1].equals("pm")&& hh>12){
            hh+=12
        }
        calendar.set(Calendar.HOUR_OF_DAY,hh)
        calendar.set(Calendar.MINUTE,mm)
        var intent: Intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.Events.TITLE, subname)
            .putExtra(CalendarContract.Events.EVENT_LOCATION,"Banglore Institute of Technology" )
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,calendar.getTime().getTime() )
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,calendar.timeInMillis+(60*60*3000))



        button.setOnClickListener { startActivity(intent)}


    }


}
