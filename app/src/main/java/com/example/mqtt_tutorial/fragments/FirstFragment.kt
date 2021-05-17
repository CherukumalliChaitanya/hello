/*
 * Copyright (c) 2021 CodingWithChris - All Rights Reserved
 */

package com.example.mqtt_tutorial.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mqtt_tutorial.MainViewModel
import com.example.mqtt_tutorial.R
import com.example.mqtt_tutorial.printToast
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {


    // ViewModel anlegen:
    private lateinit var viewModel: MainViewModel





    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(),defaultViewModelProviderFactory).get(
            MainViewModel::class.java)

//        val etName:EditText = first_et_name
//        val etPwd:EditText = first_et_pwd
        first_btn_login.setOnClickListener {
            // Davor prüfen, ob überall Eingaben gemacht wurden...
            if(checkInputs())
                connectToBroker()
            else
                printToast(requireContext(),"Bitte alle Felder ausfüllen....")
        }

    }

    // Methode übernimmt Daten aus den Feldern und versucht Verbindung aufzubauen..
    private fun connectToBroker()
    {
        showViews(first_pb,first_tv_pb)
        blockInputs()
        // Get IP-Adresse:
        var address = "tcp://" + first_et_ip.text.toString()
        val port = first_et_port.text.toString()
        if(port != "")
            address = "$address:$port"
        val clientID = "ExampleAndroidClient"
        // Anlegen starten:
        viewModel.initClient(address,clientID){ status ->
            if(status)
            {
                val pwd = first_et_pwd.text.toString()
                val user = first_et_name.text.toString()
                // Connection:
                viewModel.connectClient(user,pwd){ status ->
                    hideViews(first_pb,first_tv_pb)
                    if(status)
                    {
                        unblockInputs()
                        findNavController().navigate(R.id.action_first_secpnd)
                    }
                    else
                    {
                        unblockInputs()
                        printToast(requireContext(),"Verbindung konnte nicht hergestellt werden...")
                    }

                }
            }
            else
            {
                unblockInputs()
            }
        }


    }


    private fun blockInputs()
    {
        first_et_name.isEnabled = false
        first_et_pwd.isEnabled = false
        first_et_port.isEnabled = false
        first_et_ip.isEnabled = false
        first_btn_login.isEnabled = false
    }

    private fun unblockInputs()
    {
        first_et_name.isEnabled = true
        first_et_pwd.isEnabled = true
        first_et_port.isEnabled = true
        first_et_ip.isEnabled = true
        first_btn_login.isEnabled = true
    }



    private fun checkInputs():Boolean
    {
        // Prüfen ob überall was steht...
        var status = true
        if(first_et_name.text.isNullOrEmpty())
            status =  false
        else if(first_et_pwd.text.isNullOrEmpty())
            status =  false
        else if(first_et_port.text.isNullOrEmpty())
            status =  false
        else if(first_et_ip.text.isNullOrEmpty())
            status =  false
        return status
    }












    private fun hideViews(vararg views: View)
    {
        for(i in views)
            i.visibility = View.GONE
    }

    private fun showViews(vararg views: View)
    {
        for(i in views)
            i.visibility = View.VISIBLE
    }
}