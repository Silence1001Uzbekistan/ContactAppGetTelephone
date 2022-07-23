package com.example.contactappgettelephone

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.contactappgettelephone.Adapter.ContactHelperAdapter
import com.example.contactappgettelephone.CLASS.Contact
import com.example.contactappgettelephone.databinding.FragmentHomeBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var list: ArrayList<Contact>

    lateinit var contactHelperAdapter: ContactHelperAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list = ArrayList()
        checkAllPermission()

    }

    private fun checkAllPermission() {

        Dexter.withContext(requireContext())
            .withPermissions(
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.CALL_PHONE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                    loadContacts()
                    loadAdapter()
                    Toast.makeText(requireContext(), "Checked", Toast.LENGTH_SHORT).show()

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {

                    findNavController().popBackStack()

                }
            }).check()

    }

    private fun loadContacts() {

        val contentResolver = requireActivity().contentResolver

        val phones = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        while (phones!!.moveToNext()) {
            val name =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))

            val number =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            list.add(Contact(name, number))

        }
        phones.close()

        list.sortBy {
            it.name
        }


    }

    private fun loadAdapter() {

        contactHelperAdapter =
            ContactHelperAdapter(list, object : ContactHelperAdapter.OnItemClickListener {
                override fun onPhoneClick(contactHelper: Contact) {
                    if (ContextCompat.checkSelfPermission(
                            binding.root.context,
                            Manifest.permission.CALL_PHONE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val phoneNumber = contactHelper.number
                        val intent = Intent(Intent.ACTION_CALL)
                        intent.data = Uri.parse("tel:$phoneNumber")
                        startActivity(intent)
                    } else {
                        checkAllPermission()
                    }
                }

                override fun onMessageClick(contactHelper: Contact) {
                    val bundle = Bundle()
                    bundle.putSerializable("contact", contactHelper)
                    findNavController().navigate(R.id.secondFragment, bundle)
                }


            })

        binding.rv.adapter = contactHelperAdapter


    }


}