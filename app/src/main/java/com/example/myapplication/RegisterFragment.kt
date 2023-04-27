package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.User


class RegisterFragment : Fragment() {

    private val viewModel: MyApplicationViewModel by activityViewModels {
        MyApplicationViewModelFactory(
            (activity?.application as MyApplicationApplication).database
                .userDao()
        )
    }

    lateinit var user: User

    private class ViewHolder(view: View) {
        val userName: TextView = view.findViewById(R.id.user_name)
        val userFirstName: TextView = view.findViewById(R.id.user_first_name)
        val userEmail: TextView = view.findViewById(R.id.user_email)
        val saveAction: View = view.findViewById(R.id.save_action)
    }

    private var viewHolder: ViewHolder? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        viewHolder = ViewHolder(view)
        return view

    }

    /**
     * Binds views with the passed in [user] information.
     */
    private fun bind(user: User) {
        viewHolder?.apply {
            userName.setText(user.username, TextView.BufferType.SPANNABLE)
            userFirstName.setText(user.firstName, TextView.BufferType.SPANNABLE)
            userEmail.setText(user.email, TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener{ updateUser() }

        }
    }

    private fun navigateScreen(fragment: Fragment) {
        val navLogin = activity as FragmentNavigation
        navLogin.navigateFrag(fragment, false)
    }

    /**
     * Inserts the new Item into database and navigates up to list fragment.
     */
    private fun addNewUser() {
        //add nativevalidateform here later-if statement
        viewModel.addNewUser(
            viewHolder?.userName?.text.toString(),
            viewHolder?.userFirstName?.text.toString(),
            viewHolder?.userEmail?.text.toString(),
        )
        navigateScreen(HomeFragment())

    }


    /**
     * Updates an existing Item in the database and navigates up to list fragment.
     */

    private fun updateUser() {
        // if validateform then
        viewModel.updateUser(
            arguments?.getInt("itemId") ?: 0,
            viewHolder?.userName?.text.toString(),
            viewHolder?.userFirstName?.text.toString(),
            viewHolder?.userEmail?.text.toString()

        )
        navigateScreen(HomeFragment())

    }

    /**
     * Called when the view is created.
     * The itemId Navigation argument determines the edit item  or add new item.
     * If the itemId is positive, this method retrieves the information from the database and
     * allows the user to update it.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt("itemId") ?: 0
        if (id > 0) {
            viewModel.retrieveUser(id).observe(viewLifecycleOwner) {selectedUser ->
                user = selectedUser
                bind(user)
            }
        } else {
            viewHolder?.saveAction?.setOnClickListener {
                addNewUser()
            }
        }
    }
}