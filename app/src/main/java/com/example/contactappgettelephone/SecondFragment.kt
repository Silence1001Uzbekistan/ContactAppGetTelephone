package com.example.contactappgettelephone

import android.os.Bundle
import android.telephony.SmsManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.contactappgettelephone.CLASS.Contact
import com.example.contactappgettelephone.databinding.FragmentSecondBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SecondFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SecondFragment : Fragment() {

    lateinit var binding: FragmentSecondBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSecondBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contact = arguments?.getSerializable("contact") as Contact

        binding.name.text = contact.name
        binding.phone.text = contact.number

        binding.sendMessage.setOnClickListener {

            val message = binding.message.text.toString().trim()
            if (message.isNotEmpty()) {
                val number = contact.number
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(number, null, message, null, null)
                findNavController().popBackStack()
            } else {
                Toast.makeText(binding.root.context, "Empty", Toast.LENGTH_SHORT).show()
            }

        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }


}